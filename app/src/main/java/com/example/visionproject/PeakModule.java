package com.example.visionproject;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class PeakModule {

    private Mat referenceGraySmall = null;

    private double theta = 0.85;

    private static final double RC = 0.4;

    private static final int SMALL_WIDTH = 96;
    private static final int SMALL_HEIGHT = 72;

    private static final int MAX_ITA_ITERATIONS = 6;
    private static final int ITA_MIN_FOREGROUND = 100;

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getTheta() {
        return theta;
    }

    public ProcessResult processFrame(Mat rgbaInput) {
        long t0 = System.nanoTime();

        Mat gray = new Mat();
        Imgproc.cvtColor(rgbaInput, gray, Imgproc.COLOR_RGBA2GRAY);

        Mat graySmall = new Mat();
        Imgproc.resize(gray, graySmall, new Size(SMALL_WIDTH, SMALL_HEIGHT), 0, 0, Imgproc.INTER_AREA);

        if (referenceGraySmall == null) {
            referenceGraySmall = graySmall.clone();

            Mat emptyRoi = Mat.zeros(graySmall.size(), CvType.CV_8UC1);
            Mat emptyIta = Mat.zeros(graySmall.size(), CvType.CV_8UC1);

            long t1 = System.nanoTime();
            double ms = (t1 - t0) / 1_000_000.0;

            ProcessResult result = new ProcessResult(
                    ProcessResult.Status.INIT,
                    1.0,
                    ms,
                    Double.POSITIVE_INFINITY,
                    graySmall.cols(),
                    graySmall.rows(),
                    graySmall.clone(),
                    emptyRoi.clone(),
                    emptyIta.clone(),
                    0,
                    0,
                    0,
                    -1.0,
                    -1.0
            );

            gray.release();
            graySmall.release();
            emptyRoi.release();
            emptyIta.release();

            return result;
        }

        double r1 = computePCC(referenceGraySmall, graySmall);
        double cre = computeCRE(r1);

        Mat roiMask = computeRoiMask(referenceGraySmall, graySmall);
        int roiCount = Core.countNonZero(roiMask);

        ItaResult itaResult = runIta(roiMask);

        ProcessResult.Status status;
        if (r1 >= theta) {
            status = ProcessResult.Status.DISCARD;
        } else {
            status = ProcessResult.Status.PROCESS;
            referenceGraySmall.release();
            referenceGraySmall = graySmall.clone();
        }

        long t1 = System.nanoTime();
        double ms = (t1 - t0) / 1_000_000.0;

        ProcessResult result = new ProcessResult(
                status,
                r1,
                ms,
                cre,
                graySmall.cols(),
                graySmall.rows(),
                graySmall.clone(),
                roiMask.clone(),
                itaResult.mask.clone(),
                roiCount,
                itaResult.pixelCount,
                itaResult.iterations,
                itaResult.centroid.x,
                itaResult.centroid.y
        );

        gray.release();
        graySmall.release();
        roiMask.release();
        itaResult.mask.release();

        return result;
    }

    private double computeCRE(double r1) {
        double denom = 1.0 - r1;
        if (denom < 1e-6) {
            denom = 1e-6;
        }
        return RC / denom;
    }

    private double computePCC(Mat img1, Mat img2) {
        if (img1.rows() != img2.rows() || img1.cols() != img2.cols()) {
            throw new IllegalArgumentException("As imagens devem ter o mesmo tamanho para calcular PCC.");
        }

        if (img1.type() != CvType.CV_8UC1 || img2.type() != CvType.CV_8UC1) {
            throw new IllegalArgumentException("As imagens devem ser grayscale CV_8UC1.");
        }

        int rows = img1.rows();
        int cols = img1.cols();
        int total = rows * cols;

        byte[] data1 = new byte[total];
        byte[] data2 = new byte[total];
        img1.get(0, 0, data1);
        img2.get(0, 0, data2);

        double mean1 = 0.0;
        double mean2 = 0.0;

        for (int i = 0; i < total; i++) {
            mean1 += (data1[i] & 0xFF);
            mean2 += (data2[i] & 0xFF);
        }

        mean1 /= total;
        mean2 /= total;

        double num = 0.0;
        double den1 = 0.0;
        double den2 = 0.0;

        for (int i = 0; i < total; i++) {
            double x = (data1[i] & 0xFF) - mean1;
            double y = (data2[i] & 0xFF) - mean2;
            num += x * y;
            den1 += x * x;
            den2 += y * y;
        }

        double den = Math.sqrt(den1 * den2);
        if (den < 1e-12) {
            return 1.0;
        }

        return num / den;
    }

    private Mat computeRoiMask(Mat img1, Mat img2) {
        int rows = img1.rows();
        int cols = img1.cols();
        int total = rows * cols;

        byte[] data1 = new byte[total];
        byte[] data2 = new byte[total];
        img1.get(0, 0, data1);
        img2.get(0, 0, data2);

        double mean1 = 0.0;
        double mean2 = 0.0;

        for (int i = 0; i < total; i++) {
            mean1 += (data1[i] & 0xFF);
            mean2 += (data2[i] & 0xFF);
        }

        mean1 /= total;
        mean2 /= total;

        byte[] maskData = new byte[total];

        for (int i = 0; i < total; i++) {
            int x = data1[i] & 0xFF;
            int y = data2[i] & 0xFF;

            boolean negativeCorrelation =
                    (x < mean1 && y > mean2) ||
                            (x > mean1 && y < mean2);

            maskData[i] = negativeCorrelation ? (byte) 255 : 0;
        }

        Mat mask = new Mat(rows, cols, CvType.CV_8UC1);
        mask.put(0, 0, maskData);
        return mask;
    }

    private ItaResult runIta(Mat roiMask) {
        Mat current = roiMask.clone();
        int currentCount = Core.countNonZero(current);

        if (currentCount == 0) {
            return new ItaResult(current, 0, 0, new Point(-1, -1));
        }

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3, 3));
        int iterations = 0;

        for (int i = 0; i < MAX_ITA_ITERATIONS; i++) {
            iterations++;

            Mat blurred = new Mat();
            Mat next = new Mat();
            Mat diff = new Mat();

            Imgproc.GaussianBlur(current, blurred, new Size(5, 5), 0);
            Imgproc.threshold(blurred, next, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

            Imgproc.morphologyEx(next, next, Imgproc.MORPH_OPEN, kernel);
            Imgproc.morphologyEx(next, next, Imgproc.MORPH_CLOSE, kernel);

            int nextCount = Core.countNonZero(next);

            Core.absdiff(current, next, diff);
            int changed = Core.countNonZero(diff);

            current.release();
            blurred.release();
            diff.release();

            current = next;
            currentCount = nextCount;

            if (currentCount < ITA_MIN_FOREGROUND || changed == 0) {
                break;
            }
        }

        kernel.release();

        Point centroid = computeCentroid(current);
        return new ItaResult(current, currentCount, iterations, centroid);
    }

    private Point computeCentroid(Mat binaryMask) {
        Moments moments = Imgproc.moments(binaryMask, true);
        if (moments.m00 == 0.0) {
            return new Point(-1, -1);
        }

        double cx = moments.m10 / moments.m00;
        double cy = moments.m01 / moments.m00;
        return new Point(cx, cy);
    }

    public void resetReference() {
        if (referenceGraySmall != null) {
            referenceGraySmall.release();
            referenceGraySmall = null;
        }
    }

    private static class ItaResult {
        final Mat mask;
        final int pixelCount;
        final int iterations;
        final Point centroid;

        ItaResult(Mat mask, int pixelCount, int iterations, Point centroid) {
            this.mask = mask;
            this.pixelCount = pixelCount;
            this.iterations = iterations;
            this.centroid = centroid;
        }
    }
}
package com.example.visionproject;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.nio.ByteBuffer;

public class FrameAnalyzer implements ImageAnalysis.Analyzer {

    public interface AnalyzerListener {
        void onFrameProcessed(ProcessResult result);
    }

    private final PeakModule peakModule;
    private final AnalyzerListener listener;

    public FrameAnalyzer(PeakModule peakModule, AnalyzerListener listener) {
        this.peakModule = peakModule;
        this.listener = listener;
    }

    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        Mat rgba = null;

        try {
            rgba = imageProxyToRgbaMat(imageProxy);
            ProcessResult result = peakModule.processFrame(rgba);

            if (listener != null) {
                listener.onFrameProcessed(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rgba != null) {
                rgba.release();
            }
            imageProxy.close();
        }
    }

    private Mat imageProxyToRgbaMat(ImageProxy imageProxy) {
        int width = imageProxy.getWidth();
        int height = imageProxy.getHeight();

        ImageProxy.PlaneProxy[] planes = imageProxy.getPlanes();

        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];

        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        Mat yuv = new Mat(height + height / 2, width, CvType.CV_8UC1);
        yuv.put(0, 0, nv21);

        Mat rgba = new Mat();
        Imgproc.cvtColor(yuv, rgba, Imgproc.COLOR_YUV2RGBA_NV21);

        int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();
        if (rotationDegrees == 90) {
            Core.rotate(rgba, rgba, Core.ROTATE_90_CLOCKWISE);
        } else if (rotationDegrees == 180) {
            Core.rotate(rgba, rgba, Core.ROTATE_180);
        } else if (rotationDegrees == 270) {
            Core.rotate(rgba, rgba, Core.ROTATE_90_COUNTERCLOCKWISE);
        }

        yuv.release();
        return rgba;
    }
}
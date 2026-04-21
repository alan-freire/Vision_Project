package com.example.visionproject;

import org.opencv.core.Mat;

public class ProcessResult {
    public enum Status {
        INIT,
        DISCARD,
        PROCESS
    }

    public final Status status;
    public final double r1;
    public final double processingMs;
    public final double creSeconds;
    public final int frameWidth;
    public final int frameHeight;

    public final Mat processedGraySmall;
    public final Mat roiMaskSmall;
    public final Mat itaMaskSmall;

    public final int roiPixelCount;
    public final int itaPixelCount;
    public final int itaIterations;

    public final double centroidX;
    public final double centroidY;

    public ProcessResult(
            Status status,
            double r1,
            double processingMs,
            double creSeconds,
            int frameWidth,
            int frameHeight,
            Mat processedGraySmall,
            Mat roiMaskSmall,
            Mat itaMaskSmall,
            int roiPixelCount,
            int itaPixelCount,
            int itaIterations,
            double centroidX,
            double centroidY
    ) {
        this.status = status;
        this.r1 = r1;
        this.processingMs = processingMs;
        this.creSeconds = creSeconds;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.processedGraySmall = processedGraySmall;
        this.roiMaskSmall = roiMaskSmall;
        this.itaMaskSmall = itaMaskSmall;
        this.roiPixelCount = roiPixelCount;
        this.itaPixelCount = itaPixelCount;
        this.itaIterations = itaIterations;
        this.centroidX = centroidX;
        this.centroidY = centroidY;
    }
}
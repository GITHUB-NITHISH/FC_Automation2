//package com.automation.utils;
//
//import java.awt.AWTException;
//import java.awt.Graphics2D;
//import java.awt.Rectangle;
//import java.awt.RenderingHints;
//import java.awt.Robot;
//import java.awt.Toolkit;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import javax.imageio.ImageIO;
//import javax.imageio.ImageWriteParam;
//import javax.imageio.ImageWriter;
//import javax.imageio.stream.ImageOutputStream;
//
//import org.monte.media.Format;
//import org.monte.media.FormatKeys;
//import org.monte.media.avi.AVIWriter;
//import org.monte.media.math.Rational;
//
//import static org.monte.media.VideoFormatKeys.*;
//import static org.monte.media.FormatKeys.*;
//
///**
// * Reliable video recording utility using Monte's AVIWriter directly
// * with Java AWT Robot for screen capture.
// * Compatible with Java 11+ (including Java 21).
// */
//public class VideoRecorderUtil {
//
//    private static final ThreadLocal<VideoRecorderUtil> recorderThread = new ThreadLocal<>();
//
//    private AVIWriter aviWriter;
//    private Robot robot;
//    private Rectangle screenBounds;
//    private volatile boolean recording = false;
//    private Thread captureThread;
//    private String name;
//    private File outputFile;
//    private int trackIndex;
//
//    // Capture settings — tuned for ~80-100 MB per minute with DIB encoding
//    private static final int FRAMES_PER_SECOND = 2;        // 2 fps — sufficient for test review
//    private static final double SCALE_FACTOR = 0.33;        // 33% resolution (1920x1080 → 640x360)
//    private static final long FRAME_INTERVAL_MS = 1000 / FRAMES_PER_SECOND;
//
//    /**
//     * Start screen recording for a given scenario
//     */
//    public static void startRecording(String methodName) throws Exception {
//        VideoRecorderUtil recorder = new VideoRecorderUtil();
//        recorder.name = methodName.replaceAll("[^a-zA-Z0-9_\\-]", "_");
//        recorder.begin();
//        recorderThread.set(recorder);
//        System.out.println("Video Recording Started: " + recorder.name);
//    }
//
//    /**
//     * Stop screen recording
//     */
//    public static void stopRecording() throws Exception {
//        VideoRecorderUtil recorder = recorderThread.get();
//        if (recorder != null) {
//            recorder.end();
//            recorderThread.remove();
//            System.out.println("Video Recording Stopped: " + recorder.outputFile.getAbsolutePath());
//        }
//    }
//
//    private void begin() throws Exception {
//        // Setup output directory
//        File folder = new File("test-recordings");
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//
//        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        outputFile = new File(folder, name + "_" + timestamp + ".avi");
//
//        // Screen capture setup
//        screenBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
//        robot = new Robot();
//
//        // Calculate scaled dimensions for smaller file size
//        int scaledWidth = (int) (screenBounds.width * SCALE_FACTOR);
//        int scaledHeight = (int) (screenBounds.height * SCALE_FACTOR);
//        // Ensure even dimensions (required by video codecs)
//        scaledWidth = scaledWidth % 2 == 0 ? scaledWidth : scaledWidth - 1;
//        scaledHeight = scaledHeight % 2 == 0 ? scaledHeight : scaledHeight - 1;
//
//        // Setup AVI writer with DIB at reduced resolution
//        aviWriter = new AVIWriter(outputFile);
//        trackIndex = aviWriter.addTrack(new Format(
//                MediaTypeKey, FormatKeys.MediaType.VIDEO,
//                EncodingKey, ENCODING_AVI_DIB,
//                CompressorNameKey, ENCODING_AVI_DIB,
//                DepthKey, 24,
//                FrameRateKey, new Rational(FRAMES_PER_SECOND, 1),
//                WidthKey, scaledWidth,
//                HeightKey, scaledHeight
//        ));
//
//        final int captureWidth = scaledWidth;
//        final int captureHeight = scaledHeight;
//
//        recording = true;
//
//        // Start capture in background thread
//        captureThread = new Thread(() -> {
//            try {
//                while (recording) {
//                    long startTime = System.currentTimeMillis();
//
//                    BufferedImage screenshot = robot.createScreenCapture(screenBounds);
//
//                    // Scale down and convert to TYPE_3BYTE_BGR (required by AVI DIB)
//                    BufferedImage scaled = new BufferedImage(
//                            captureWidth, captureHeight, BufferedImage.TYPE_3BYTE_BGR);
//                    Graphics2D g2d = scaled.createGraphics();
//                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//                    g2d.drawImage(screenshot, 0, 0, captureWidth, captureHeight, null);
//                    g2d.dispose();
//
//                    synchronized (aviWriter) {
//                        if (recording) {
//                            aviWriter.write(trackIndex, scaled, 1);
//                        }
//                    }
//
//                    // Maintain consistent frame rate
//                    long elapsed = System.currentTimeMillis() - startTime;
//                    long sleepTime = FRAME_INTERVAL_MS - elapsed;
//                    if (sleepTime > 0) {
//                        Thread.sleep(sleepTime);
//                    }
//                }
//            } catch (Exception e) {
//                System.err.println("Video capture error: " + e.getMessage());
//            }
//        }, "video-recorder-" + name);
//
//        captureThread.setDaemon(true);
//        captureThread.start();
//    }
//
//    private void end() throws Exception {
//        recording = false;
//
//        // Wait for capture thread to finish
//        if (captureThread != null && captureThread.isAlive()) {
//            captureThread.join(3000);
//        }
//
//        // Close AVI writer
//        synchronized (aviWriter) {
//            if (aviWriter != null) {
//                try {
//                    aviWriter.close();
//                } catch (Exception e) {
//                    System.err.println("Error closing AVI writer: " + e.getMessage());
//                }
//            }
//        }
//    }
//}

package com.automation.utils;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.avi.AVIWriter;
import org.monte.media.math.Rational;

import static org.monte.media.VideoFormatKeys.*;
import static org.monte.media.FormatKeys.*;

/**
 * Ultra-High Quality Video Recorder
 *
 * TARGET:
 * - ~10MB per 4 minutes (~2.5MB per minute)
 * - Native Resolution (1.0 Scale)
 * - 15 FPS
 * - Extremely high clarity (CRF 14)
 */
public class VideoRecorderUtil {

    private static final ThreadLocal<VideoRecorderUtil> recorderThread =
            new ThreadLocal<>();

    private AVIWriter aviWriter;
    private Robot robot;
    private Rectangle screenBounds;
    private volatile boolean recording = false;
    private Thread captureThread;
    private String name;
    private File outputFile;
    private int trackIndex;

    // =====================================================
    // SETTINGS (Clarity Optimized)
    // =====================================================

    private static final int FRAMES_PER_SECOND = 8;

    private static final double SCALE_FACTOR = 0.8;

    private static final long FRAME_INTERVAL_MS =
            1000 / FRAMES_PER_SECOND;

    // =====================================================
    // START RECORDING
    // =====================================================

    public static void startRecording(String methodName)
            throws Exception {

        VideoRecorderUtil recorder = new VideoRecorderUtil();

        recorder.name = methodName.replaceAll("[^a-zA-Z0-9_\\-]", "_");

        recorder.begin();
        recorderThread.set(recorder);

        System.out.println("Video Recording Started: " + recorder.name);
    }

    // =====================================================
    // STOP RECORDING
    // =====================================================

    public static void stopRecording() throws Exception {
        VideoRecorderUtil recorder = recorderThread.get();
        if (recorder != null) {
            recorder.end();
            recorderThread.remove();
            System.out.println("Video Recording Stopped");
        }
    }

    // =====================================================
    // BEGIN RECORDING
    // =====================================================

    private void begin() throws Exception {
        File folder = new File("test-recordings");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        outputFile = new File(folder, name + "_" + timestamp + ".avi");

        screenBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        robot = new Robot();

        int scaledWidth = (int) (screenBounds.width * SCALE_FACTOR);
        int scaledHeight = (int) (screenBounds.height * SCALE_FACTOR);

        scaledWidth = (scaledWidth % 2 == 0) ? scaledWidth : scaledWidth - 1;
        scaledHeight = (scaledHeight % 2 == 0) ? scaledHeight : scaledHeight - 1;

        aviWriter = new AVIWriter(outputFile);
        trackIndex = aviWriter.addTrack(new Format(
                MediaTypeKey, FormatKeys.MediaType.VIDEO,
                EncodingKey, ENCODING_AVI_MJPG,
                CompressorNameKey, ENCODING_AVI_MJPG,
                DepthKey, 24,
                FrameRateKey, new Rational(FRAMES_PER_SECOND, 1),
                WidthKey, scaledWidth,
                HeightKey, scaledHeight
        ));

        final int captureWidth = scaledWidth;
        final int captureHeight = scaledHeight;
        recording = true;

        captureThread = new Thread(() -> {
            try {
//                while (recording) {
//                    long startTime = System.currentTimeMillis();
//
//                    BufferedImage screenshot = robot.createScreenCapture(screenBounds);
//                    BufferedImage frame = new BufferedImage(
//                            captureWidth, captureHeight, BufferedImage.TYPE_3BYTE_BGR);
//
//                    Graphics2D g2d = frame.createGraphics();
//                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
//                                       RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//                    g2d.drawImage(screenshot, 0, 0, captureWidth, captureHeight, null);
//                    g2d.dispose();
//
//                    synchronized (aviWriter) {
//                        if (recording) {
////                            aviWriter.write(trackIndex, frame, 1);
//                        	aviWriter.write(trackIndex, frame, FRAMES_PER_SECOND);
//                        }
//                    }
//
////                    long elapsed = System.currentTimeMillis() - startTime;
////                    long sleepTime = FRAME_INTERVAL_MS - elapsed;
////
////                    if (sleepTime > 0) {
////                        Thread.sleep(sleepTime);
////                    }
//                    long nextFrameTime = startTime + FRAME_INTERVAL_MS;
//
//                    while (System.currentTimeMillis() < nextFrameTime) {
//                        Thread.sleep(1);
//                    }
//                }
            	
            	long nextFrameTime = System.nanoTime();

            	while (recording) {

            	    nextFrameTime += 1_000_000_000L / FRAMES_PER_SECOND;

            	    BufferedImage screenshot =
            	            robot.createScreenCapture(screenBounds);

            	    BufferedImage frame =
            	            new BufferedImage(
            	                    captureWidth,
            	                    captureHeight,
            	                    BufferedImage.TYPE_3BYTE_BGR);

            	    Graphics2D g2d = frame.createGraphics();

            	    g2d.setRenderingHint(
            	            RenderingHints.KEY_INTERPOLATION,
            	            RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            	    g2d.drawImage(
            	            screenshot,
            	            0,
            	            0,
            	            captureWidth,
            	            captureHeight,
            	            null);

            	    g2d.dispose();

            	    synchronized (aviWriter) {

            	        if (recording) {

            	            aviWriter.write(trackIndex, frame, 1);
            	        }
            	    }

            	    long sleepNanos =
            	            nextFrameTime - System.nanoTime();

            	    if (sleepNanos > 0) {

            	        Thread.sleep(
            	                sleepNanos / 1_000_000,
            	                (int) (sleepNanos % 1_000_000));
            	    }
            	}
            } catch (Exception e) {
                System.err.println("Video capture error: " + e.getMessage());
            }
        }, "video-recorder-" + name);

        captureThread.setDaemon(true);
        captureThread.start();
    }

    private void end() throws Exception {
        recording = false;

        if (captureThread != null && captureThread.isAlive()) {
            captureThread.join(15000);
        }

        synchronized (aviWriter) {
            if (aviWriter != null) {
                try {
                    aviWriter.close();
                } catch (Exception e) {
                    System.err.println("Error closing AVI writer: " + e.getMessage());
                }
            }
        }

        compressVideoToMp4();
    }

    // =====================================================
    // AVI -> MP4 (Ultra Fidelity)
    // =====================================================

    private void compressVideoToMp4() {
        try {
            String mp4Name = outputFile.getAbsolutePath().replace(".avi", ".mp4");

            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg",
                    "-y",
                    "-i", outputFile.getAbsolutePath(),

                    "-vcodec", "libx264",

                    "-preset", "medium",

                    // CRF 14 (Increases storage usage to ~2.5MB/min for better quality)
                    "-crf", "14",

                    "-r", String.valueOf(FRAMES_PER_SECOND),

                    "-vf", "scale=trunc(iw/2)*2:trunc(ih/2)*2",

                    "-pix_fmt", "yuv420p",

                    "-movflags", "+faststart",

                    mp4Name
            );

            pb.inheritIO();
            long start = System.currentTimeMillis();
            Process process = pb.start();
            int exitCode = process.waitFor();
            long end = System.currentTimeMillis();

            if (exitCode == 0) {
                File mp4File = new File(mp4Name);
                System.out.println("Compression completed in: " + (end - start) + " ms");
                System.out.println("Final MP4 Size: " + 
                        String.format("%.2f", (mp4File.length() / (1024.0 * 1024.0))) + " MB");

                if (outputFile.exists()) {
                    outputFile.delete();
                }
            }

        } catch (Exception e) {
            System.err.println("Compression failed: " + e.getMessage());
        }
    }
}




package com.damaru.wavetools;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;

/**
 * This is just to test some stuff.
 */
public class Polygoons extends Application {

    int numRows = 4;
    int numCols = 128;

    //private final double[][] data = generateMockData();
    private double[][] data;

    //private final double[][] data = { {0.0, -.5, -.3}, {1., .8, -.9} };
    //private final double[][] data = {  {0.0, -1, 1, 0}, {0.0, -1, 1, 0}};

    Generator generator = new Generator();

    @Override
    public void start(Stage primaryStage) {
        int width = 2000;
        int height = 1000;
        int ribbonHeight = height / 4;
        double ribbonBase = height / 2 + ribbonHeight / 2;

        double scaleFactorX = 0.6;
        double scaleX = (((double) width) / numCols) * scaleFactorX;
        double scaleY = ribbonHeight / 2.2;
        Group root = new Group();

        Scene scene = new Scene(root, width, height, Color.BLACK);

        double offsetZ = 400.0 / Math.max(numRows, numCols);
        double offsetX = width / 10.0; // h
        double offsetY = height * ((1.0 - scaleFactorX) / 2.0);

        Log.log("sx: %.2f sy: %.2f ox: %.2f oy: %.2f oz: %.2f",
                scaleX, scaleY, offsetX, offsetY, offsetZ);

        data = generateNoiseData();

        for (int z = data.length - 1; z >= 0; z -= 1) {
            double screenX = 0.0;
            double screenY = 0.0;
            double extraOffsetY = 0.0;
//            Polygon poly = new Polygon();
            Polyline poly = new Polyline();
            double ribbonLeft = offsetX + (double) z * offsetZ;
            double ribbonBottom = ribbonBase - (double) z * offsetZ;
            double zeroLine = ribbonBottom - scaleY;
            Log.log("z: %d ribbonLeft: %.2f  ribbonBottom: %.2f zeroLine: %.2f", z, ribbonLeft, ribbonBottom, zeroLine);
            poly.getPoints().addAll(ribbonLeft, ribbonBottom);
            for (int x = 0; x < data[z].length; x++) {
                double extraOffsetX = z * offsetZ;
                screenX = offsetX + x * scaleX + extraOffsetX;
                double scaledData = data[z][x] * scaleY;
                extraOffsetY = z * offsetZ;
                screenY = zeroLine - scaledData - extraOffsetY;
//                Log.log("data %.2f scaledData %2.2f extraOffsetX: %.2f extraOffsetY: %.2f screenX: %.2f screenY: %.2f rb: %.2f",
//                       data[z][x], scaledData, extraOffsetX, extraOffsetY, screenX, screenY, ribbonBottom);
                poly.getPoints().addAll(screenX, screenY);
            }
            poly.getPoints().addAll(screenX, ribbonBase - extraOffsetY);
            poly.getPoints().addAll(ribbonLeft, ribbonBottom);

            poly.setStroke(Color.CYAN);
            poly.setStrokeWidth(1.0);
            poly.setOpacity(1.0);
            root.getChildren().add(poly);
        }

        primaryStage.setScene(scene);
        primaryStage.setTitle("Ribbon Terrain Visualization");
        primaryStage.show();
    }

    private double[][] generateNoiseData() {
        double[][] data = new double[numRows][numCols];
        MinMax minMax = generator.generateRawSampleFolded(data, 0L, .1, .1);
        Log.log("minMax: %s", minMax.toString());
        return data;
    }

    // Replace this with real data
    public double[][] generateMockData() {
        double[][] data = new double[numRows][numRows];
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numRows; c++) {
                data[r][c] = Math.sin((c + r) * 0.3) * Math.cos(c * 0.2) + Math.random() * 0.2;
            }
        }
        return data;
    }

    private static void testSmooth() {
        double[] data = { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0 };

        int numToSmooth = 8;
        int i = data.length - numToSmooth;
        double lastVal = data[i];
        i++;
        double secondVal = data[i];
        double slope = secondVal - lastVal; // dy/dx, but because dx is always 1, it's just dy
        double slopeIncrement = slope / numToSmooth;
        double newSlope = slope;

        while (i < data.length) {
            newSlope = newSlope - slopeIncrement;
            double newVal = lastVal + newSlope;
            data[i++] = newVal;
            lastVal = newVal;
        }

        for (double v : data) {
            Log.log("%f", v);
        }
    }

    public static void main(String[] args) {
        launch(args);
        //testSmooth();
    }
}

package com.damaru.waver;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import static com.damaru.waver.MainApplication.SCREEN_HEIGHT;
import static com.damaru.waver.MainApplication.SCREEN_WIDTH;

/**
 * This displays the wavetable on the screen.
 */
public class WavePainter {

    private final double ribbonHeight = SCREEN_HEIGHT / 4.0;
    private final double ribbonBase = SCREEN_HEIGHT * 0.4 + ribbonHeight / 2.0;

    public void draw(Pane pane, double[][] data) {

        int numRows = data.length;
        int numCols = data[0].length;
        double scaleFactorX = 0.4;
        double scaleX = (((double) SCREEN_WIDTH) / numCols) * scaleFactorX;
        double scaleY = ribbonHeight / 2.2;
        double offsetZ = 100.0 / numRows;
        double offsetX = SCREEN_WIDTH / 5.0;

//        Log.log("nr: %d nc: %d sx: %.2f sy: %.2f ox: %.2f oy: %.2f oz: %.2f",
//                numRows, numCols, scaleX, scaleY, offsetX, offsetY, offsetZ);

        for (int z = data.length - 1; z >= 0; z -= 1) {
            double screenX = 0.0;
            double screenY = 0.0;
            double extraOffsetY = 0.0;
            Polyline poly = new Polyline();
            double ribbonLeft = offsetX + (double) z * offsetZ;
            double ribbonBottom = ribbonBase - (double) z * offsetZ;
            double zeroLine = ribbonBottom - scaleY;
//            Log.log("z: %d ribbonLeft: %.2f  ribbonBottom: %.2f zeroLine: %.2f", z, ribbonLeft, ribbonBottom, zeroLine);
            poly.getPoints().addAll(ribbonLeft, ribbonBottom);
            for (int x = 0; x < data[z].length; x++) {
                double extraOffsetX = z * offsetZ;
                screenX = offsetX + x * scaleX + extraOffsetX;
                double scaledData = data[z][x] * scaleY;
                extraOffsetY = z * offsetZ;
                screenY = zeroLine - scaledData;
//                Log.log("data %.2f scaledData %2.2f extraOffsetX: %.2f extraOffsetY: %.2f screenX: %.2f screenY: %.2f rb: %.2f",
//                       data[z][x], scaledData, extraOffsetX, extraOffsetY, screenX, screenY, ribbonBottom);
                poly.getPoints().addAll(screenX, screenY);
            }
            poly.getPoints().addAll(screenX, ribbonBase - extraOffsetY);
            poly.getPoints().addAll(ribbonLeft, ribbonBottom);

            poly.setStroke(Color.CYAN);
            poly.setStrokeWidth(1.0);
            poly.setOpacity(1.0);
            pane.getChildren().add(poly);
        }

    }
}

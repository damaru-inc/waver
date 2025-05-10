package com.damaru.waver;

import java.util.Arrays;

/**
 * This generates wavetable data.
 */
public class Generator {

    /** This fills in the data using periodic waveforms. */
    public MinMax generateRawSamplePeriodic(double[][] data, long seed, double radiusBase, double radiusIncrement, double xIncrement) {
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;

        int tableSize = data.length;
        int sampleSize = data[0].length;
        Log.log("tableSize: %d sampleSize: %d\n", tableSize, sampleSize);

        for (int x = 0; x < tableSize; x++) {
            double radius = radiusBase + x * radiusIncrement;
            double xOffset = x * xIncrement;
            for (int y = 0; y < sampleSize; y++) {
                float val = periodicFunction(seed, radius, y, sampleSize, xOffset);
                //Log.log("radius: %.2f xOffset: %.2f y: %d val: %.4f", radius, xOffset, y, val);
                if (val < min) min = val;
                if (val > max) max = val;
                data[x][y] = val;
                //System.out.printf("j: %3d  lastSampleIndex: %3d %2.2f ", j, lastSampleIndex, val);
            }
            //Log.log("x: %3d first: %.2f last: %.2f", x, data[x][0], data[x][lastSampleIndex]);
        }

        return new MinMax(min, max);
    }

    /** This uses the circle technique as explained here: https://bleuje.com/tutorial3/ */
    private float periodicFunction(long seed, double radius, int index, int maxIndex, double xOffset) {
        // Map from 0 .. maxIndex to 0 .. 2PI to convert how far along we are, to radians in one circle.
        double radians = (index / (double) maxIndex) * Math.PI * 2.0;

        double x = radius * Math.cos(radians) + xOffset;
        double y = radius * Math.sin(radians);

        float ret = OpenSimplex2S.noise2(seed, x, y);
        //Log.log("radians: %f radius: %.2f x: %.2f y: %.2f ret: %.2f", radians, radius, x, y, ret);
        return ret;
    }

    /** This takes the raw data along with its minimum and maximum values, and scales it to -1.0 .. 1.0 */
    public MinMax normalize(double[][] rawSample, double[][] normalizedSample, MinMax origMinMax) {
        double origMin = origMinMax.min();
        double origMax = origMinMax.max();
        double existingRange = origMax - origMin; // difference between min and max
        double scale = 2.0 / existingRange;
        double offset = (origMin + origMax) / 2.0;

        // e.g. min = -0.2, max = 0.4.
        // existingRange = 0.6
        // offset = 0.1
        // scale = 16.666
        // normalized = val - offset * scale.
        // min = (-0.2 - 0.1) * 3.33 = -0.3 * 3.33 = -1
        // max = (0.4 - 0.1) * 3.33 = 0.3 * 3.33 = 1

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;


        for (int i = 0; i < rawSample.length; i++) {
            double[] row = rawSample[i];
            for (int j = 0; j < row.length; j++) {
                double val = (row[j] - offset) * scale;
                normalizedSample[i][j] = val;
                if (val < min) min = val;
                if (val > max) max = val;
            }

        }
        return new MinMax(min, max);
    }

    /* These are to support testing. */

    /** Fills the array with zeros. */
    public MinMax generateZeros(double[][] data) {
        for (int x = 0; x < data.length; x++) {
            double[] row = data[x];
            Arrays.fill(row, 0.0);
        }
        return new MinMax(0, 0);
    }

    public double[][] generateMockData(double[][] data) {
        int tableSize = data.length;
        int sampleSize = data[0].length;

        for (int r = 0; r < tableSize; r++) {
            for (int c = 0; c < sampleSize; c++) {
                data[r][c] = Math.sin((c + r) * 0.3) * Math.cos(c * 0.2) + Math.random() * 0.2;
            }
        }
        return data;
    }

}
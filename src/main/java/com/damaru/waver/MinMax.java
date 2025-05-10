package com.damaru.waver;

/**
 * As we generate wavetables, we keep track of the min and max values we get,
 * to simplify normalization.
 *
 */
public record MinMax(double min, double max) {
}

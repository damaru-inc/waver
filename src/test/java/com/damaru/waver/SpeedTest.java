package com.damaru.waver;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

public class SpeedTest {

    @Test
    void speedTest() {
        Instant start = Instant.now();

        int numToTest = 256 * 2048;


        for (int i = 0; i < numToTest; i++) {
            OpenSimplex2S.noise2(0L, i / (double) numToTest, i / (double) numToTest);
        }

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);

        Log.log("Duration for %d samples:  %s", numToTest, duration);

    }
}

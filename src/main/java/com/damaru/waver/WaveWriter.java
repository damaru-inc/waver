package com.damaru.waver;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * This writes the wavetable out to a wav file - 16 bit, mono, 48kHz.
 */
public class WaveWriter {

    // Audio format settings
    float sampleRate = 48000;
    int bytesPerSample = 2;
    int sampleSizeInBits = 8 * bytesPerSample; // 16-bit audio
    int channels = 1; // mono
    boolean signed = true;
    boolean bigEndian = false;
    AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

    public void convertAndSave(double[][] source, File file) throws IOException {
        int tableSize = source.length;
        int sampleSize = source[0].length;
        byte[] data = new byte[bytesPerSample * sampleSize * tableSize];
        convertToWaveSamples(source, data);
        save(data, sampleSize * tableSize, file);
    }

    private void convertToWaveSamples(double[][] source, byte[] target) {
        int tableSize = source.length;
        int sampleSize = source[0].length;

        for (int i = 0; i < tableSize; i++) {
            double[] row = source[i];

            for (int j = 0; j < sampleSize; j++) {
                double val = row[j];
                int sample = (int) (val * 32767);
                // Write the 16-bit sample into the byte array (little endian)
                int dataIndex = (i * sampleSize * bytesPerSample) + (j * bytesPerSample);
                target[dataIndex] = (byte) (sample & 0xFF);
                target[dataIndex + 1] = (byte) ((sample >> 8) & 0xFF);
            }
        }
    }

    private void save(byte[] data, int numSamples, File file) throws IOException {
        // Write the byte array into a wav file
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        AudioInputStream ais = new AudioInputStream(bais, audioFormat, numSamples);
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
        Log.log("file created: %s", file.getCanonicalPath());
    }


}

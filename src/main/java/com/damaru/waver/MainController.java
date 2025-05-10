package com.damaru.waver;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

/**
 * This is the main javafx controller. It manages the lifecycles of the other classes.
 */
public class MainController {

    public static final int SAMPLE_SIZE = 2048;

    @FXML
    private Slider radiusBaseSlider;

    @FXML
    private Slider radiusIncrementSlider;

    @FXML
    private Slider seedSlider;

    @FXML
    private Slider xIncrementSlider;

    @FXML
    private Slider wafeformsSlider;

    @FXML
    Pane pane;

    private double[][] normalizedData = null;
    private final Generator generator = new Generator();
    private final WavePainter wavePainter = new WavePainter();
    private final WaveWriter waveWriter = new WaveWriter();

    @FXML
    public void initialize() {
        radiusBaseSlider.setOnMouseReleased(e -> { refresh(); });
        radiusIncrementSlider.setOnMouseReleased(e -> { refresh(); });
        seedSlider.setOnMouseReleased(e -> { refresh(); });
        wafeformsSlider.setOnMouseReleased(e -> { refresh(); });
        xIncrementSlider.setOnMouseReleased(e -> { refresh(); });
        refresh();
    }

    /**
     * This takes the values of the controls, calls the generator and displays the wavetable.
     */
    @FXML
    protected void refresh() {
        //Log.log("Refresh: %s %s\n", radiusBaseField.getText(), radiusIncrementField.getText());
        pane.getChildren().clear();
        double radiusBase = radiusBaseSlider.getValue() * 0.1;
        double radiusIncrement = radiusIncrementSlider.getValue() * 0.01;
        long seed = (long) seedSlider.getValue() * 113; // randomly chosen
        double xIncrement = xIncrementSlider.getValue() * 0.01;
        int numWaveforms = (int) wafeformsSlider.getValue();

        if (numWaveforms == 0) {
            numWaveforms = 1;
        }

        //Log.log("seed: %d xInc: %.2f radB: %.2f radI: %.2f", seed, xIncrement, radiusBase, radiusIncrement);

        double[][] data = new double[numWaveforms][SAMPLE_SIZE];
        normalizedData = new double[numWaveforms][SAMPLE_SIZE];
        MinMax minMax = generator.generateRawSamplePeriodic(data, seed, radiusBase, radiusIncrement, xIncrement);
        generator.normalize(data, normalizedData, minMax);
        wavePainter.draw(pane, data);
    }

    @FXML
    protected void onSaveButtonClick(ActionEvent event) {
        Node source = (Node) event.getSource();
        Window stage = source.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(makeFileName());
        fileChooser.setTitle("Save Wavetable");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Wave files", "*.wav"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            Log.log("File : %s", file.getName());
            try {
                waveWriter.convertAndSave(normalizedData, file);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            Log.log("File is null.");
        }
    }

    private String makeFileName() {
        int numWaves = (int) wafeformsSlider.getValue();
        double radiusBase = radiusBaseSlider.getValue();
        double radiusIncrement = radiusIncrementSlider.getValue();
        long seed = (long) seedSlider.getValue();
        double xIncrement = xIncrementSlider.getValue();

        return String.format("waver-%03d-%.1f-%.1f-%.1f-%03d.wav", numWaves, radiusBase, radiusIncrement, xIncrement, seed);
    }
}
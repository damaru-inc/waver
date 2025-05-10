module com.damaru.waver {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.damaru.waver to javafx.fxml;
    exports com.damaru.waver;
}
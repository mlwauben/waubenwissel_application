module com.waubenwissel {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.ooxml;
    requires java.desktop;
    requires spire.xls;

    opens com.waubenwissel to javafx.fxml;
    exports com.waubenwissel;
}

module waubenwissel.application {
    exports com.waubenwissel;

    requires java.desktop;
    requires javafx.base;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.apache.poi.ooxml;
    requires org.apache.poi.poi;
    requires spire.xls;
    opens com.waubenwissel to javafx.fxml;

}

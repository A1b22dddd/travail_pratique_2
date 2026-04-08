module csi.tp2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens csi.tp2 to javafx.fxml;
    exports csi.tp2;
}
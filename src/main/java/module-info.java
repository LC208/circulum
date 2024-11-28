module ru.lc208.circulum {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.lc208.circulum to javafx.fxml;
    exports ru.lc208.circulum;
}
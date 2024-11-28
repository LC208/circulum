module ru.lc208.circulum {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires javafx.base;

    opens ru.lc208.circulum to javafx.fxml;
    opens ru.lc208.circulum.controllers to javafx.fxml;
    opens ru.lc208.circulum.entities to org.hibernate.orm.core;
    exports ru.lc208.circulum.controllers to javafx.fxml;
    exports ru.lc208.circulum;
    exports ru.lc208.circulum.entities;
}
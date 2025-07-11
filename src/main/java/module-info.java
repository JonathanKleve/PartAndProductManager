module kleve.PartAndProductManager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens kleve.PartAndProductManager to javafx.fxml;
    exports kleve.PartAndProductManager;
    exports kleve.PartAndProductManager.datamodel;
    opens kleve.PartAndProductManager.datamodel to javafx.fxml;
    exports kleve.PartAndProductManager.controllers;
    opens kleve.PartAndProductManager.controllers to javafx.fxml;
}
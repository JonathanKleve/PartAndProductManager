module kleve.testtwo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens kleve.testtwo to javafx.fxml;
    exports kleve.testtwo;
    exports kleve.testtwo.datamodel;
    opens kleve.testtwo.datamodel to javafx.fxml;
    exports kleve.testtwo.controllers;
    opens kleve.testtwo.controllers to javafx.fxml;
}
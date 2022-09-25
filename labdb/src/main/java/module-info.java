module com.labdb.labdb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.labdb.labdb to javafx.fxml;
    exports com.labdb.labdb;
}
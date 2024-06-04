module bryan.grade {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.healthmarketscience.jackcess;
    requires com.google.gson;


    opens bryan.grade to javafx.fxml, com.google.gson;
    exports bryan.grade;
}
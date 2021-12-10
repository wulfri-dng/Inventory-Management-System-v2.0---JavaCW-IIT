module com.example.priyalalstorejavacw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires mongo.java.driver;


    opens com.example.priyalalstorejavacw to javafx.fxml;
    exports com.example.priyalalstorejavacw;
}
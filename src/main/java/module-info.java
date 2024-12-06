module org.cs3270.airlineprojectmain {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;

    opens org.cs3270.airlineprojectmain to javafx.fxml;
    exports org.cs3270.airlineprojectmain;
    exports org.cs3270.airlineprojectmain.UserClasses;
    opens org.cs3270.airlineprojectmain.UserClasses to javafx.fxml;
}
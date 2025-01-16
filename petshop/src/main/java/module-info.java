module com.petshop.petshop {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.graphics;
    //requires javafx.base;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens controller to javafx.fxml;
    opens com.petshop.petshop to javafx.fxml;

    exports visao;
    exports controller;
    exports dominio;
    exports persistencia;
}
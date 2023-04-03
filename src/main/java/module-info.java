module me.corxl.capstone {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;

    opens me.corxl.capstoneclient to javafx.fxml;
    exports me.corxl.capstoneclient;
    exports me.corxl.capstoneclient.chess.pieces;
    exports me.corxl.capstoneclient.chess.players;
    opens me.corxl.capstoneclient.chess.players to javafx.fxml;
    exports me.corxl.capstoneclient.chess.spaces;
    opens me.corxl.capstoneclient.chess.spaces to javafx.fxml;
    exports me.corxl.capstoneclient.lobby;
    opens me.corxl.capstoneclient.lobby to javafx.fxml;
}
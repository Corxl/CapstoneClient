package me.corxl.capstoneclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.corxl.capstoneclient.chess.board.Board;

import java.net.URL;
import java.util.ResourceBundle;

public class ChessController implements Initializable {

    @FXML
    private VBox mainPane;

    private Board currentBoard;
    private Stage stage;

    public void setBoard(Board b) {
        this.currentBoard = b;
        mainPane.getChildren().add(0, currentBoard);
    }

    public final void setMainStage(Stage stage) {
        this.stage = stage;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public Board getMainBoard() {
        return currentBoard;
    }


    @FXML
    void quit(ActionEvent event) {
        //Board.checkKingsSaftey();
        stage.hide();
        System.exit(0);

//        Platform.runLater(() -> {
//            mainPane.getChildren().remove(currentBoard);
//            currentBoard = new Board(new Player("1"), new Player("2"), this);
//            mainPane.getChildren().add(0, currentBoard);
//        });
    }
}
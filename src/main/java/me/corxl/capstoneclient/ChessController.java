package me.corxl.capstoneclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import me.corxl.capstoneclient.chess.board.Board;
import me.corxl.capstoneclient.chess.pieces.TeamColor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChessController implements Initializable {

    @FXML
    private VBox mainPane;

    private static Board currentBoard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            currentBoard = new Board(null, null, this);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        mainPane.getChildren().add(0, currentBoard);
    }

    public static Board getMainBoard() {
        return currentBoard;
    }


    @FXML
    void resetGame(ActionEvent event) {
        //Board.checkKingsSaftey();
        boolean[][] moves = Board.getPossibleMovesByColor(TeamColor.WHITE);
        for (int i = 0; i < moves.length; i++) {
            for (int j = 0; j < moves[i].length; j++) {
                if (moves[i][j])
                    Board.getSpaces()[i][j].setSelected(true);
            }
        }

//        Platform.runLater(() -> {
//            mainPane.getChildren().remove(currentBoard);
//            currentBoard = new Board(new Player("1"), new Player("2"), this);
//            mainPane.getChildren().add(0, currentBoard);
//        });
    }
}
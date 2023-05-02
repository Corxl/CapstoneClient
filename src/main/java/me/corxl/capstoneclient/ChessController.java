package me.corxl.capstoneclient;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.corxl.capstoneclient.chess.board.Board;

import java.net.URL;
import java.util.ResourceBundle;

public class ChessController implements Initializable {

    @FXML
    private VBox mainPane;
    @FXML
    private HBox top;
    @FXML
    private VBox alertBox;
    @FXML
    private Label alertText;
    @FXML
    private Button quitButton;
    @FXML
    private StackPane mainWindowStackPane;
    private Board currentBoard;
    private Stage stage;

    private double xOffset, yOffset;

    public void setBoard(Board b) {
        this.currentBoard = b;
        mainPane.getChildren().add(0, currentBoard);
    }

    public final void setMainStage(Stage stage) {
        this.stage = stage;
    }

    public StackPane getMainWindowStackPane() {
        return this.mainWindowStackPane;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.alertBox.setVisible(false);
        top.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        top.setOnMouseDragged(event -> {
            this.stage.setX(event.getScreenX() - xOffset);
            this.stage.setY(event.getScreenY() - yOffset);
        });
    }

    public Board getMainBoard() {
        return currentBoard;
    }

    private void blur(Node node, boolean enableBlur) {
        node.setDisable(enableBlur);
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(enableBlur ? 10 : 0);
        node.setEffect(blur);
    }

    public void showDisconnectMessage(String message) {
        Platform.runLater(() -> {
            this.alertText.setText(message);
            this.blur(this.mainPane, true);
            this.quitButton.setDisable(true);
            this.blur(this.quitButton, true);
            this.alertBox.setVisible(true);
        });
    }


    @FXML
    void quit(ActionEvent event) {
        //Board.checkKingsSaftey();
        stage.hide();
        this.currentBoard.reset();
        System.exit(0);
//        Platform.runLater(() -> {
//            mainPane.getChildren().remove(currentBoard);
//            currentBoard = new Board(new Player("1"), new Player("2"), this);
//            mainPane.getChildren().add(0, currentBoard);
//        });
    }
}
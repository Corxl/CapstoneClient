package me.corxl.capstoneclient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.corxl.capstoneclient.chess.board.Board;
import me.corxl.capstoneclient.chess.board.BoardLayout;
import me.corxl.capstoneclient.chess.pieces.TeamColor;
import me.corxl.capstoneclient.chess.players.Player;
import me.corxl.capstoneclient.lobby.LobbyScreen;
import me.corxl.capstoneclient.lobby.LobbyState;

import java.io.IOException;

public class ChessMain extends Application {

    private ClientHandler CLIENT_CONNECTION;
    private static final String VERSION = "1.0.0";
    protected Stage lobbyStage, gameStage;
    private LobbyState STATE;

    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {
        this.lobbyStage = stage;
        loadLobbyWindow();


        //loadBoardWindow(ChessMain.stage);
    }

    public void toggleGameStage(boolean b) {
        if (b) {
            this.lobbyStage.hide();
            this.gameStage.show();
        } else {
            this.lobbyStage.show();
            this.gameStage.hide();
        }
    }

    protected void loadLobbyWindow() throws IOException, ClassNotFoundException {
        this.STATE = LobbyState.LOBBY;
        CLIENT_CONNECTION = new ClientHandler(this);
        FXMLLoader fxmlLoader = new FXMLLoader(ChessMain.class.getResource("lobby-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        lobbyStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);

        ((LobbyScreen) fxmlLoader.getController()).setMain(this);
        ((LobbyScreen) fxmlLoader.getController()).setClient(CLIENT_CONNECTION);
        lobbyStage.setTitle("Chess " + VERSION);
        lobbyStage.setScene(scene);
        lobbyStage.setResizable(false);
        lobbyStage.show();
    }

    protected void updateBoard(BoardLayout[][] layout, TeamColor c) throws IOException, ClassNotFoundException {
        System.out.println("update");
        if (this.STATE == LobbyState.LOBBY) {
            System.out.println("State Changed");
            Platform.runLater(() -> {
                try {
                    loadBoardWindow(layout, c);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            return;
        } else {
            CLIENT_CONNECTION.updateBoard(layout);
        }
    }

    protected void loadBoardWindow(BoardLayout[][] layout, TeamColor c) throws IOException, ClassNotFoundException {
        this.STATE = LobbyState.GAME;
        this.lobbyStage.hide();
        Board b = new Board(layout);
        //CLIENT_CONNECTION = new ClientHandler(this);
        CLIENT_CONNECTION.setBoard(b);
        b.setClient(CLIENT_CONNECTION);

        if (this.getClientConnection().getBoard().getClientPlayer() == null)
            this.getClientConnection().getBoard().setClientPlayer(new Player("", c));

        gameStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(ChessMain.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.TRANSPARENT);
        CLIENT_CONNECTION.setGameController(((ChessController) fxmlLoader.getController()));
        ((ChessController) fxmlLoader.getController()).setBoard(b);
        ((ChessController) fxmlLoader.getController()).setMainStage(gameStage);
        gameStage.setTitle("Chess " + VERSION);
        gameStage.setScene(scene);
        gameStage.initStyle(StageStyle.TRANSPARENT);
        gameStage.setResizable(false);
        gameStage.show();
    }

    public Stage getStage() {
        return this.lobbyStage;
    }

    public ClientHandler getClientConnection() {
        return CLIENT_CONNECTION;
    }

    public static void main(String[] args) {
        launch();
    }
}
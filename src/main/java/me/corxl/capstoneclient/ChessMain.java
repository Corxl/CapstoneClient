package me.corxl.capstoneclient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.corxl.capstoneclient.chess.board.Board;
import me.corxl.capstoneclient.chess.board.BoardLayout;
import me.corxl.capstoneclient.lobby.LobbyScreen;
import me.corxl.capstoneclient.lobby.LobbyState;

import java.io.IOException;

public class ChessMain extends Application {

    private ClientHandler CLIENT_CONNECTION;
    private static final String VERSION = "1.0.0";
    protected Stage stage;
    private LobbyState STATE;

    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {
        this.stage = stage;
        loadLobbyWindow();


        //loadBoardWindow(ChessMain.stage);
    }

    protected void loadLobbyWindow() throws IOException, ClassNotFoundException {
        this.STATE = LobbyState.LOBBY;
        CLIENT_CONNECTION = new ClientHandler(this);
        FXMLLoader fxmlLoader = new FXMLLoader(ChessMain.class.getResource("lobby-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 800);
        ((LobbyScreen) fxmlLoader.getController()).setMain(this);
        ((LobbyScreen) fxmlLoader.getController()).setClient(CLIENT_CONNECTION);
        stage.setTitle("Chess " + VERSION);
        stage.setScene(scene);
        stage.show();
    }

    protected void updateBoard(BoardLayout[][] layout) throws IOException, ClassNotFoundException {
        System.out.println("update");
        if (this.STATE == LobbyState.LOBBY) {
            System.out.println("State Changed");
            Platform.runLater(() -> {
                try {
                    loadBoardWindow(layout);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            return;
        } else {
            CLIENT_CONNECTION.updateBoard(layout);
        }
    }

    protected void loadBoardWindow(BoardLayout[][] layout) throws IOException, ClassNotFoundException {
        this.STATE = LobbyState.GAME;
        Board b = new Board(layout);
        CLIENT_CONNECTION = new ClientHandler(this);
        b.setClient(CLIENT_CONNECTION);
        FXMLLoader fxmlLoader = new FXMLLoader(ChessMain.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 800);
        ((ChessController) fxmlLoader.getController()).setBoard(b);
        stage.setTitle("Chess " + VERSION);
        stage.setScene(scene);
        stage.show();
    }

    public ClientHandler getClientConnection() {
        return CLIENT_CONNECTION;
    }

    public static void main(String[] args) {
        launch();
    }
}
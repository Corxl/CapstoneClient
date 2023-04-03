package me.corxl.capstoneclient.lobby;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import me.corxl.capstoneclient.ChessMain;
import me.corxl.capstoneclient.ClientHandler;
import me.corxl.capstoneclient.chess.pieces.PieceType;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LobbyScreen implements Initializable {

    @FXML
    private TextField codeInput;

    @FXML
    private Button createLobby;

    @FXML
    private Button join;

    @FXML
    private Label code;

    private ClientHandler client;
    private ChessMain main;
    private String lobbyCode;
    private PieceType[][] defaultPieces = new PieceType[][]{
            {PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN, PieceType.KING, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK},
            {PieceType.PAWN, PieceType.PAWN, PieceType.PAWN, PieceType.PAWN, PieceType.PAWN, PieceType.PAWN, PieceType.PAWN, PieceType.PAWN},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {PieceType.PAWN, PieceType.PAWN, PieceType.PAWN, PieceType.PAWN, PieceType.PAWN, PieceType.PAWN, PieceType.PAWN, PieceType.PAWN},
            {PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.KING, PieceType.QUEEN, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK}
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.join.setDisable(true);
    }

    public void setClient(ClientHandler client) {
        this.client = client;
    }

    @FXML
    void createLobby(ActionEvent event) throws IOException, ClassNotFoundException {
        this.lobbyCode = client.createLobby();
        code.setText("Lobby Code: " + lobbyCode + "\nWaiting for another player...");
    }

    public String getLobbyCode() {
        return this.lobbyCode;
    }

    @FXML
    void joinLobby(ActionEvent event) throws IOException, ClassNotFoundException {
        // if board is not null send them to the board screen...
        //Board b = client.joinLobby(this.codeInput.getText().trim());
        if (codeInput.getText().length() <= 2)
            return;
        Object lobbyData = client.joinLobby(codeInput.getText().trim());
        /*
                layout[i][i1][0] = PieceType.getTypeByKey(piece.getPieceType().getKey());
                layout[i][i1][1] = TeamColor.getTypeByKey(piece.getColor().getKey());
         */
        if (lobbyData instanceof String) {
            this.codeInput.setText("");
            this.code.setText((String) lobbyData);
            return;
        }
        //main.loadBoardWindow(layout);
    }

    @FXML
    void codeTyped() {
        System.out.println(this.codeInput.getText().length());
        if (this.codeInput.getText().length() == 7)
            this.join.setDisable(false);
        else
            this.join.setDisable(true);
    }

    @FXML
    void copyCode(MouseEvent event) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(this.lobbyCode), null);
    }

    public void setMain(ChessMain chessMain) {
        this.main = chessMain;
    }
}
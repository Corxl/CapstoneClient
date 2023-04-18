package me.corxl.capstoneclient.chess.board;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import me.corxl.capstoneclient.ClientHandler;
import me.corxl.capstoneclient.chess.pieces.Piece;
import me.corxl.capstoneclient.chess.pieces.PieceType;
import me.corxl.capstoneclient.chess.pieces.TeamColor;
import me.corxl.capstoneclient.chess.players.Player;
import me.corxl.capstoneclient.chess.spaces.BoardLocation;
import me.corxl.capstoneclient.chess.spaces.Space;
import me.corxl.capstoneclient.chess.spaces.SpaceColor;

import java.util.HashMap;

public class Board extends GridPane implements BoardInterface {

    private Player white, black;
    public boolean isPieceSelected;
    private Space[][] spaces;
    public boolean[][] selectedSpaces;
    private final HashMap<TeamColor, Boolean> isChecked = new HashMap<>();
    private final HashMap<TeamColor, TeamColor> opposingColors = new HashMap<>();
    private TeamColor turn;
    public Piece selectedPiece;
    private Player clientPlayer;
    private ClientHandler client;
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

    public Board(BoardLayout[][] layout) {
        isPieceSelected = false;
        //spaces = new Space[8][8];
        spaces = new Space[8][8];
        selectedSpaces = new boolean[8][8];
        turn = TeamColor.WHITE;
        this.setAlignment(Pos.CENTER);
        for (int i = 0; i < layout.length; i++) {
            for (int i1 = 0; i1 < layout[i].length; i1++) {
                SpaceColor color;
                if (i % 2 == 0) {
                    if (i1 % 2 == 0)
                        color = SpaceColor.LIGHT;
                    else
                        color = SpaceColor.DARK;
                } else {
                    if (i1 % 2 == 0)
                        color = SpaceColor.DARK;
                    else
                        color = SpaceColor.LIGHT;
                }
                BoardLayout boardLayout = layout[i][i1];
                BoardLocation loc = new BoardLocation(i, i1);
                spaces[i][i1] = layout[i][i1] == null ? new Space(color, new BoardLocation(i, i1), this) : new Space(color, loc, new Piece(boardLayout.getType(), boardLayout.getColor(), loc, this), this);
                this.add(spaces[i][i1], i1, i);
            }
        }
        isChecked.put(TeamColor.WHITE, false);
        isChecked.put(TeamColor.BLACK, false);
        opposingColors.put(TeamColor.WHITE, TeamColor.BLACK);
        opposingColors.put(TeamColor.BLACK, TeamColor.WHITE);
    }

    public ClientHandler getClient() {
        return this.client;
    }

    public void setClient(ClientHandler c) {
        this.client = c;
    }

    public void setClientPlayer(Player p) {
        clientPlayer = p;
    }

    public HashMap<TeamColor, TeamColor> getOpposingColor() {
        return opposingColors;
    }

    public HashMap<TeamColor, Boolean> getIsChecked() {
        return isChecked;
    }

    public void updateBoard(BoardLayout[][] layout) {
        for (int i = 0; i < spaces.length; i++) {
            for (int i1 = 0; i1 < spaces[i].length; i1++) {
                BoardLayout l = layout[i][i1];
                Space space = spaces[i][i1];
                if (l == null) {
                    space.setPiece(null);
                } else {
                    space.setPiece(new Piece(l.getType(), l.getColor(), new BoardLocation(i, i1), this));
                }

            }
        }
    }

    public Player getClientPlayer() {
        return clientPlayer;
    }

    @Override
    public Player getBlackPlayer() {
        return this.black;
    }

    @Override
    public Player getWhitePlayer() {
        return this.white;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    public Space[][] getSpaces() {
        return spaces;
    }

    @Override
    public void restart() {

    }

    @Override
    public void updateBoard(Space[][] spaces) {

    }

//    public static void simulateMove(Space[][] simSpaces, Piece p, BoardLocation newLocation, BoardLocation oldLocation) {
//        if (p == null)
//            return;
//        Space s = simSpaces[newLocation.getX()][newLocation.getY()];
//        Space old = simSpaces[oldLocation.getX()][oldLocation.getY()];
//        s.setPiece(p);
//        old.setPiece(null);
//    }

    public void clearSelections() {
        for (Space[] spArr : spaces) {
            for (Space sp : spArr) {
                sp.setSelected(false);
            }
        }
        selectedSpaces = new boolean[8][8];
    }

    public boolean isSelected(BoardLocation loc) {
        return this.selectedSpaces[loc.getX()][loc.getY()];
    }
}

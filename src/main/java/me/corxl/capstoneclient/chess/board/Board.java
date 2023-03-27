package me.corxl.capstoneclient.chess.board;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import me.corxl.capstoneclient.ChessController;
import me.corxl.capstoneclient.chess.pieces.Piece;
import me.corxl.capstoneclient.chess.pieces.PieceEnum;
import me.corxl.capstoneclient.chess.pieces.TeamColor;
import me.corxl.capstoneclient.chess.players.Player;
import me.corxl.capstoneclient.chess.spaces.BoardLocation;
import me.corxl.capstoneclient.chess.spaces.Space;
import me.corxl.capstoneclient.chess.spaces.SpaceColor;

import java.io.IOException;
import java.util.HashMap;

public class Board extends GridPane implements BoardInterface {

    private Player white, black;
    private ChessController controller;
    public static boolean isPieceSelected;
    private static Space[][] spaces;
    public static boolean[][] selectedSpaces;
    private final static HashMap<TeamColor, Boolean> isChecked = new HashMap<>();
    private final static HashMap<TeamColor, TeamColor> opposingColors = new HashMap<>();
    private static TeamColor turn;
    public static Piece selectedPiece;
    private static Player clientPlayer;
    private final PieceEnum[][] defaultPieces = new PieceEnum[][]{
            {PieceEnum.ROOK, PieceEnum.KNIGHT, PieceEnum.BISHOP, PieceEnum.QUEEN, PieceEnum.KING, PieceEnum.BISHOP, PieceEnum.KNIGHT, PieceEnum.ROOK},
            {PieceEnum.PAWN, PieceEnum.PAWN, PieceEnum.PAWN, PieceEnum.PAWN, PieceEnum.PAWN, PieceEnum.PAWN, PieceEnum.PAWN, PieceEnum.PAWN},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {PieceEnum.PAWN, PieceEnum.PAWN, PieceEnum.PAWN, PieceEnum.PAWN, PieceEnum.PAWN, PieceEnum.PAWN, PieceEnum.PAWN, PieceEnum.PAWN},
            {PieceEnum.ROOK, PieceEnum.KNIGHT, PieceEnum.BISHOP, PieceEnum.KING, PieceEnum.QUEEN, PieceEnum.BISHOP, PieceEnum.KNIGHT, PieceEnum.ROOK}
    };

    public Board(Player white, Player black, ChessController controller) throws IOException, ClassNotFoundException {
        isPieceSelected = false;
        //spaces = new Space[8][8];
        spaces = new Space[8][8];
        selectedSpaces = new boolean[8][8];
        turn = TeamColor.WHITE;
        this.controller = controller;
        this.white = white;
        this.black = black;
        this.setAlignment(Pos.CENTER);

        for (int i = 0; i < spaces.length; i++) {
            for (int j = 0; j < spaces[i].length; j++) {
                SpaceColor colorIndex;
                if (i % 2 == 0) {
                    if (j % 2 == 0)
                        colorIndex = SpaceColor.LIGHT;
                    else
                        colorIndex = SpaceColor.DARK;
                } else {
                    if (j % 2 == 0)
                        colorIndex = SpaceColor.DARK;
                    else
                        colorIndex = SpaceColor.LIGHT;
                }
                TeamColor c = (i < 2) ? TeamColor.BLACK : TeamColor.WHITE;
                BoardLocation loc = new BoardLocation(i, j);
                Space space = (defaultPieces[i][j] == null) ? new Space(colorIndex, loc) : new Space(colorIndex, loc, new Piece(defaultPieces[i][j], c, loc, false));
                //Space space = new Space(colorIndex, loc);
                spaces[i][j] = space;
                // Adds the space object to the Board at the grid index [i][j].
                this.add(space, j, i);
            }
        }
        isChecked.put(TeamColor.WHITE, false);
        isChecked.put(TeamColor.BLACK, false);
        opposingColors.put(TeamColor.WHITE, TeamColor.BLACK);
        opposingColors.put(TeamColor.BLACK, TeamColor.WHITE);
    }

    public static void setClientPlayer(Player p) {
        clientPlayer = p;
    }

    public static HashMap<TeamColor, TeamColor> getOpposingColor() {
        return opposingColors;
    }

    public static HashMap<TeamColor, Boolean> getIsChecked() {
        return isChecked;
    }

    public static TeamColor getTurn() {
        return turn;
    }

    public static void setTurn(TeamColor color) {
        turn = color;
    }


    public static boolean[][] getPossibleMovesByColor(TeamColor color) {
        boolean[][] moveSpaces = new boolean[8][8];
        for (Space[] space : spaces) {
            for (int j = 0; j < space.length; j++) {
                Space s = space[j];
                if (s.isEmpty())
                    continue;
                if (s.getPiece().getColor() != color)
                    continue;
                Piece p = s.getPiece();

                boolean[][] moves = Piece.getPossibleMoves(p, true);
                for (int x = 0; x < moves.length; x++) {
                    for (int y = 0; y < moves[x].length; y++) {
                        if (moves[x][y]) {
                            moveSpaces[x][y] = true;
                        }
                    }
                }

            }
        }
        return moveSpaces;
    }

    public static boolean[][] getPossibleMovesByColor(TeamColor color, boolean targetFriend) {
        boolean[][] moveSpaces = new boolean[8][8];
        for (Space[] space : spaces) {
            for (int j = 0; j < space.length; j++) {
                Space s = space[j];
                if (s.isEmpty())
                    continue;
                if (s.getPiece().getColor() != color)
                    continue;
                Piece p = s.getPiece();

                boolean[][] moves = Piece.getPossibleMoves(p, targetFriend);
                for (int x = 0; x < moves.length; x++) {
                    for (int y = 0; y < moves[x].length; y++) {
                        if (moves[x][y]) {
                            moveSpaces[x][y] = true;
                        }
                    }
                }

            }
        }
        return moveSpaces;
    }

    public static boolean[][] getPossibleMovesByColor(TeamColor color, Space[][] sps) {
        boolean[][] moveSpaces = new boolean[8][8];
        for (Space[] space : sps) {
            for (int j = 0; j < space.length; j++) {
                Space s = space[j];
                if (s.isEmpty())
                    continue;
                if (s.getPiece().getColor() != color)
                    continue;
                Piece p = s.getPiece();

                boolean[][] moves = Piece.getPossibleMoves(p, true, sps);
                for (int x = 0; x < moves.length; x++) {
                    for (int y = 0; y < moves[x].length; y++) {
                        if (moves[x][y]) {
                            moveSpaces[x][y] = true;
                        }
                    }
                }

            }
        }
        return moveSpaces;
    }

    public static boolean checkForGameOver() {
        TeamColor opposingColor = getOpposingColor().get(getTurn());
        System.out.println(opposingColor);
        boolean[][] possibleSpaces = getPossibleMovesByColor(opposingColor, false);

        for (int i = 0; i < possibleSpaces.length; i++) {
            for (int j = 0; j < possibleSpaces[i].length; j++) {
                if (possibleSpaces[i][j]) {
                    System.out.println(i + " | " + j);
                    return false;
                }
            }
        }
//        for (int i = 0; i < spaces.length; i++) {
//            for (int j = 0; j < spaces[i].length; j++) {
//                if (spaces[i][j].isEmpty())
//                    continue;
//                Piece p = spaces[i][j].getPiece();
//                if (p.getColor() != opposingColor)
//                    continue;
//                Piece pieceCopy = new Piece(p);
//                Space[][] spacesCopy = new Space[8][8];
//                for (int x = 0; x < spaces.length; x++) {
//                    for (int y = 0; y < spaces[x].length; y++) {
//                        spacesCopy[x][y] = new Space(spaces[x][y]);
//                    }
//                }
//                BoardLocation oldLoc = new BoardLocation(pieceCopy.getLocation());
//                BoardLocation newLoc = new BoardLocation(i, j);
//                Board.simulateMove(spacesCopy, pieceCopy, newLoc, oldLoc);
//                boolean[][] possMoves = Board.getPossibleMovesByColor(Board.getOpposingColor().get(pieceCopy.getColor()), spacesCopy);
//                boolean isChecked = Board.isInCheck(pieceCopy.getColor(), spacesCopy, possMoves);
//                if (!isChecked)
//                    return false;
//            }
//        }

        return true;
    }

    public static boolean isInCheck(TeamColor targetColor, Space[][] spaces, boolean[][] moveSpaces) {
        for (int i = 0; i < moveSpaces.length; i++) {
            for (int j = 0; j < moveSpaces[i].length; j++) {
                if (moveSpaces[i][j]) {
                    Space s = spaces[i][j];
                    if (s.isEmpty())
                        continue;
                    Piece p = s.getPiece();
                    if (p.getColor() != targetColor)
                        continue;
                    if (p.getPieceType() != PieceEnum.KING)
                        continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static Player getClientPlayer() {
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

    public static Space[][] getSpaces() {
        return spaces;
    }

    @Override
    public void restart() {

    }

    @Override
    public void updateBoard(Space[][] spaces) {

    }

    public static void setPiece(Piece p) {
        if (p == null)
            return;
        Space s = Board.getSpaces()[p.getLocation().getX()][p.getLocation().getY()];
        s.setPiece(p);
    }

    public static void setPiece(Piece p, BoardLocation newLocation) {
        if (p == null)
            return;
        Space s = Board.getSpaces()[newLocation.getX()][newLocation.getY()];
        s.setPiece(p);
    }

    public static void setPiece(Piece p, BoardLocation newLocation, BoardLocation oldLocation) {
        if (p == null)
            return;
        Space s = spaces[newLocation.getX()][newLocation.getY()];
        Space old = spaces[oldLocation.getX()][oldLocation.getY()];
        s.setPiece(p);
        old.setPiece(null);
    }

    public static void simulateMove(Space[][] simSpaces, Piece p, BoardLocation newLocation, BoardLocation oldLocation) {
        if (p == null)
            return;
        Space s = simSpaces[newLocation.getX()][newLocation.getY()];
        Space old = simSpaces[oldLocation.getX()][oldLocation.getY()];
        s.setPiece(p);
        old.setPiece(null);
    }

    public static void clearSelections() {
        for (Space[] spArr : spaces) {
            for (Space sp : spArr) {
                sp.setSelected(false);
            }
        }
        selectedSpaces = new boolean[8][8];
    }
}

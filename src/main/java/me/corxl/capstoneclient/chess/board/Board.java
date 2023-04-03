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

    public TeamColor getTurn() {
        return turn;
    }

    public void setTurn(TeamColor color) {
        turn = color;
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


    public boolean[][] getPossibleMovesByColor(TeamColor color) {
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

    public boolean[][] getPossibleMovesByColor(TeamColor color, boolean targetFriend) {
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

    public boolean[][] getPossibleMovesByColor(TeamColor color, Space[][] sps) {
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

    public boolean checkForGameOver() {
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
                    if (p.getPieceType() != PieceType.KING)
                        continue;
                    return true;
                }
            }
        }
        return false;
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

    public static void simulateMove(Space[][] simSpaces, Piece p, BoardLocation newLocation, BoardLocation oldLocation) {
        if (p == null)
            return;
        Space s = simSpaces[newLocation.getX()][newLocation.getY()];
        Space old = simSpaces[oldLocation.getX()][oldLocation.getY()];
        s.setPiece(p);
        old.setPiece(null);
    }

    public void clearSelections() {
        for (Space[] spArr : spaces) {
            for (Space sp : spArr) {
                sp.setSelected(false);
            }
        }
        selectedSpaces = new boolean[8][8];
    }
}

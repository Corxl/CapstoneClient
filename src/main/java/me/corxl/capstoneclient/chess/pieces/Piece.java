package me.corxl.capstoneclient.chess.pieces;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.corxl.capstoneclient.chess.board.Board;
import me.corxl.capstoneclient.chess.spaces.BoardLocation;
import me.corxl.capstoneclient.chess.spaces.Space;

import java.io.IOException;
import java.io.Serializable;

public class Piece extends VBox implements Serializable {
    private final TeamColor color;
    private BoardLocation location;
    private final PieceType pieceType;
    private boolean pawnMoved = false;
    private transient Board board;

    public Piece(Piece piece) {
        this.pieceType = piece.getPieceType();
        this.color = piece.getColor();
        this.location = piece.getLocation();
        this.pawnMoved = piece.pawnMoved;
    }
    public Piece(PieceType pieceType, TeamColor color, BoardLocation location, Board board) {
        this.color = color;
        this.location = location;
        this.pieceType = pieceType;
        this.board = board;
        this.setAlignment(Pos.CENTER);

        String p = this.isWhite()
                ?
                System.getProperty("user.dir") + "\\src\\main\\resources\\me\\corxl\\capstoneclient\\" + this.pieceType.fileLocation[0]
                :
                System.getProperty("user.dir") + "\\src\\main\\resources\\me\\corxl\\capstoneclient\\" + this.pieceType.fileLocation[1];
        Image i = new Image(p);
        ImageView v = new ImageView(i);
        v.setFitWidth(50);
        v.setFitHeight(50);
        this.getChildren().add(v);
        this.setOnMouseClicked((e) -> {
            try {
//                if (this.color != board.getTurn()) {
//                    return;
//                }
                //

                //
                //if (this.pieceType == PieceType.PAWN) {
                //this.board.createPromote(this.location);
                //}


                if (this.board.isSelected(this.location)) {
                    System.out.println("SELECTED");
                    board.getClient().requestMove(this.location, this.board.selectedPiece.getLocation());
                    board.clearSelections();
                    return;
                }
                board.clearSelections();
                System.out.println("STARTED GET POSS MOVES");
                boolean[][] possileMoves = board.getClient().getPossibleMoves(this, false);
                System.out.println("ENDED GET POSS MOVES");
                board.selectedSpaces = possileMoves;
                Space[][] spaces = board.getSpaces();
                for (int j = 0; j < possileMoves.length; j++) {
                    for (int k = 0; k < possileMoves[j].length; k++) {
                        if (possileMoves[j][k]) {
                            spaces[j][k].setSelected(true);
                        }
                    }
//                System.out.println(Arrays.toString(possileMoves[j]));
                }
                board.selectedPiece = this;
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    public static StackPane createPieceVisual(PieceType type, TeamColor color) {
        String p = color == TeamColor.WHITE
                ?
                System.getProperty("user.dir") + "\\src\\main\\resources\\me\\corxl\\capstoneclient\\" + type.fileLocation[0]
                :
                System.getProperty("user.dir") + "\\src\\main\\resources\\me\\corxl\\capstoneclient\\" + type.fileLocation[1];
        Image i = new Image(p);
        ImageView v = new ImageView(i);
        v.setFitWidth(50);
        v.setFitHeight(50);
        StackPane pane = new StackPane();
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().add(v);
        return pane;
    }

    public Board getBoard() {
        return this.board;
    }

    public boolean isBlack() {
        return this.color == TeamColor.BLACK;
    }

    public boolean isWhite() {
        return this.color == TeamColor.WHITE;
    }

    public boolean isPawnMoved() {
        return this.pawnMoved;
    }

    public void setPawnMoved(boolean b) {
        this.pawnMoved = b;
    }

    public TeamColor getColor() {
        return this.color;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public void setLocation(BoardLocation location) {
        this.location = location;
    }

    public BoardLocation getLocation() {
        return this.location;
    }

}

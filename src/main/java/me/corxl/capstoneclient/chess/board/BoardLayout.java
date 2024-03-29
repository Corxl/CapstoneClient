package me.corxl.capstoneclient.chess.board;

import me.corxl.capstoneclient.chess.pieces.PieceType;
import me.corxl.capstoneclient.chess.pieces.TeamColor;

import java.io.Serializable;

public class BoardLayout implements Serializable {

    private PieceType type;
    private TeamColor color;

    public BoardLayout(PieceType type, TeamColor color) {
        this.type = type;
        this.color = color;
    }

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public TeamColor getColor() {
        return color;
    }

    public void setColor(TeamColor color) {
        this.color = color;
    }
}

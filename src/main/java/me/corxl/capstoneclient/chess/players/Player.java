package me.corxl.capstoneclient.chess.players;

import me.corxl.capstoneclient.chess.pieces.Piece;
import me.corxl.capstoneclient.chess.pieces.TeamColor;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements PlayerInterface, Serializable {
    private final String name;
    private final TeamColor color;

    public Player(String name, TeamColor color) {
        this.name = name;
        this.color = color;
    }

    public TeamColor getTeamColor() {
        return this.color;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ArrayList<Piece> getRemainingPieces() {
        return null;
    }

    @Override
    public ArrayList<Piece> getDestroyedPieces() {
        return null;
    }
}

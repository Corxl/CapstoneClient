package me.corxl.capstoneclient;

import me.corxl.capstoneclient.chess.pieces.Piece;
import me.corxl.capstoneclient.chess.spaces.BoardLocation;
import me.corxl.capstoneclient.chess.spaces.Space;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandler {
    private Socket socket = null;
    private ObjectInputStream input = null;
    private ObjectOutputStream ioout = null;
    private static int port = 4909;

    public ClientHandler() throws IOException, ClassNotFoundException {
        InetAddress host = InetAddress.getLocalHost();
        socket = new Socket(host.getHostName(), port);
    }

    public void closeConnection() throws IOException {
        ioout.close();
        input.close();
        socket.close();
        System.out.println("Shutting down client connection...");
    }

    public boolean[][] getPossibleMoves(Piece p, boolean targetFriendly, Space[][] spaces) throws IOException, ClassNotFoundException {
        ioout = new ObjectOutputStream(socket.getOutputStream());
        Object[] data = new Object[]{"getPossibleMoves", p, targetFriendly, spaces};
        ioout.writeObject(data);
        input = new ObjectInputStream(socket.getInputStream());
        boolean[][] moveSpaces = (boolean[][]) input.readObject();
        return moveSpaces;
    }

    public Space[][] getDefaultSpaces() throws IOException, ClassNotFoundException {
        ioout = new ObjectOutputStream(socket.getOutputStream());
        Object[] data = new Object[]{"getDefaultSpaces"};
        ioout.writeObject(data);
        input = new ObjectInputStream(socket.getInputStream());
        return (Space[][]) input.readObject();
    }

    public Space[][] requestMove(Piece p, BoardLocation newLoc, BoardLocation oldLoc) throws IOException, ClassNotFoundException {
        ioout = new ObjectOutputStream(socket.getOutputStream());
        Object[] data = new Object[]{"requestMove", p, newLoc, oldLoc, null};
        ioout.writeObject(data);
        input = new ObjectInputStream(socket.getInputStream());
        return (Space[][]) input.readObject();
    }
}

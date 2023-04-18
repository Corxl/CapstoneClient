package me.corxl.capstoneclient;

import me.corxl.capstoneclient.chess.board.Board;
import me.corxl.capstoneclient.chess.board.BoardLayout;
import me.corxl.capstoneclient.chess.pieces.Piece;
import me.corxl.capstoneclient.chess.pieces.PieceType;
import me.corxl.capstoneclient.chess.pieces.TeamColor;
import me.corxl.capstoneclient.chess.spaces.BoardLocation;
import me.corxl.capstoneclient.chess.spaces.Space;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandler implements Serializable {
    private Socket socket = null;
    private ObjectInputStream input = null;
    private ObjectOutputStream ioout = null;
    private static int port = 4909;
    private transient Board board;
    private ChessMain main;
    private String clientID;
    private String gameKey;

    public ClientHandler(ChessMain chessMain) throws IOException, ClassNotFoundException {
        //this.board = board;
        this.main = chessMain;
        InetAddress host = InetAddress.getLocalHost();
        socket = new Socket(host.getHostName(), port);
        input = new ObjectInputStream(socket.getInputStream());
        this.clientID = (String) input.readObject();
        System.out.println(this.clientID + "?????");
        new ReceiveDataThread().start();
    }

    public Board getBoard() {
        return this.board;
    }

    public void setBoard(Board b) {
        this.board = b;
    }

    public void closeConnection() throws IOException {
        ioout.close();
        input.close();
        socket.close();
        System.out.println("Shutting down client connection...");
    }

    public boolean[][] getPossibleMoves(Piece p, boolean targetFriendly) throws IOException, ClassNotFoundException {

        //Object[] data = new Object[]{"getPossibleMoves", p.getLocation(), targetFriendly, board.getClientPlayer()};
        System.out.println("Gamekey = " + this.gameKey);
        if (p.getColor() != this.getBoard().getClientPlayer().getTeamColor())
            return new boolean[8][8];
        ioout = new ObjectOutputStream(socket.getOutputStream());
        Object[] data = new Object[]{"getPossibleMoves", this.clientID, this.gameKey, new int[]{p.getLocation().getX(), p.getLocation().getY()}};
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

    public PieceType requestMove(BoardLocation newLoc, BoardLocation oldLoc) throws IOException, ClassNotFoundException {
        ioout = new ObjectOutputStream(socket.getOutputStream());
        Object[] data = new Object[]{"requestMove", new int[]{newLoc.getX(), newLoc.getY()}, new int[]{oldLoc.getX(), oldLoc.getY()}, null};
        ioout.writeObject(data);
        input = new ObjectInputStream(socket.getInputStream());
//        if (input.readObject() == null)
//            return null;
        return PieceType.getTypeByKey((Integer) input.readObject());
    }

    public String createLobby() throws IOException, ClassNotFoundException {
        ioout = new ObjectOutputStream(socket.getOutputStream());
        Object[] data = new Object[]{"createLobby", this.clientID};
        ioout.writeObject(data);
        input = new ObjectInputStream(socket.getInputStream());
        return (String) input.readObject();
    }

    public Object joinLobby(String code) throws IOException, ClassNotFoundException {
        Object data = sendData(new Object[]{"joinLobby", this.clientID, code});
        return data;
    }

    public Object sendData(Object[] data) throws IOException, ClassNotFoundException {
        ioout = new ObjectOutputStream(socket.getOutputStream());
        ioout.writeObject(data);
        input = new ObjectInputStream(socket.getInputStream());
        return input.readObject();
    }

    public void updateBoard(BoardLayout[][] layout) {
        this.board.updateBoard(layout);
    }

    private class ReceiveDataThread extends Thread {
        private Socket socketdata = null;
        private ObjectInputStream inputData = null;
        private ObjectOutputStream iooutData = null;
        private static int port = 4909;

        ReceiveDataThread() throws IOException {
            InetAddress host = InetAddress.getLocalHost();
            this.socketdata = new Socket(host.getHostName(), port);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    input = new ObjectInputStream(socketdata.getInputStream());
                    //if (!(inputData.readObject() instanceof Object[])) continue;
                    Object[] data = (Object[]) input.readObject();
                    String dataType = (String) data[0];
                    if (!dataType.equals("updateBoard")) return;
                    Integer[][][] layout = (Integer[][][]) data[1];
                    BoardLayout[][] bLayout = new BoardLayout[8][8];
                    TeamColor c = TeamColor.getTypeByKey((Integer) data[2]);
                    for (int i = 0; i < layout.length; i++) {
                        for (int i1 = 0; i1 < layout[i].length; i1++) {
                            if (layout[i][i1][0] == null || layout[i][i1][1] == null) {
                                bLayout[i][i1] = null;
                                continue;
                            }
                            bLayout[i][i1] = new BoardLayout(PieceType.getTypeByKey(layout[i][i1][0]), TeamColor.getTypeByKey(layout[i][i1][1]));
                        }
                    }
                    System.out.println("-???????-");
                    main.updateBoard(bLayout, c);


                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Connection Terminated.");
                    System.exit(0);
                }
            }
        }
    }
}

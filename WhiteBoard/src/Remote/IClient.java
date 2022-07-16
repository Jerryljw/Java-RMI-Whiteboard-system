package Remote;

import WhiteBoardSystem.CanvasShape;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @Author Jiawei Luo
 * @Student ID: 1114028
 */
public interface IClient extends Remote {
    void worningFromManager(String message) throws RemoteException;

    void updateShapes(CanvasShape canvasShape) throws RemoteException;

    void updateChatBox(String chatMessage) throws RemoteException;

    boolean approveClientRequest(String username) throws RemoteException;

    void updatePeerList(ArrayList<String> userList) throws RemoteException;

    boolean requestFromPeer(String request) throws RemoteException;

    String getUsername() throws RemoteException;

    void reDraw() throws RemoteException;

    void closeWindow() throws RemoteException;

    void exit() throws RemoteException;

    void clearCanvas() throws RemoteException;

    void showEditing(String username) throws RemoteException;

}

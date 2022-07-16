package Remote;

import WhiteBoardSystem.CanvasShape;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @Author Jiawei Luo
 * @Student ID: 1114028
 */
public interface IWhiteBoard extends Remote {
    void SynchronizeCanvas(CanvasShape canvasShape) throws RemoteException;

    void SynchronizeMessage(String chatMessage) throws RemoteException;

    void SynchronizeUser() throws RemoteException;

    boolean checkPeerName(String peerName) throws RemoteException;

    boolean getApprove(String request) throws RemoteException;

    void registerPeer(String username) throws RemoteException;

    void registerManager(String IpAddress, String port, String name) throws RemoteException;

    ArrayList<CanvasShape> getCanvasShapeArrayList() throws RemoteException;

    ArrayList<IClient> getClientArrayList() throws RemoteException;

    void SynchronizeEditing(String username) throws RemoteException;

    void removePeer(String username) throws RemoteException;

    void peerExit(String username) throws RemoteException;

    void newFile() throws RemoteException;

    void openFile(ArrayList<CanvasShape> newShapes) throws RemoteException;

    void managerClose() throws RemoteException;

}

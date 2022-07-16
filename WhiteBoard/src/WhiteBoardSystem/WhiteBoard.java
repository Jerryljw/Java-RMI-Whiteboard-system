package WhiteBoardSystem;


import GUI.ManagerGUI;
import GUI.PeerGUI;
import Remote.IClient;
import Remote.IWhiteBoard;
import Util.parameters;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


/**
 * @Author Jiawei Luo
 * @Student ID: 1114028
 */
public class WhiteBoard extends UnicastRemoteObject implements IWhiteBoard {
    private ArrayList<CanvasShape> canvasShapeArrayList = new ArrayList<CanvasShape>();
    private ArrayList<IClient> clientArrayList = new ArrayList<>();
    private ArrayList<String> messageArrayList = new ArrayList<>();


    protected WhiteBoard() throws RemoteException {
        super();
    }


    @Override
    public synchronized void removePeer(String username) throws RemoteException {
        int index = -1;
        for (int i = 0; i < clientArrayList.size(); i++) {
            if (clientArrayList.get(i).getUsername().equals(username)) {
                index = i;
            }
        }
        if (index >= 0 && clientArrayList.get(index).getUsername().equals(username)) {
            IClient kickedClient = clientArrayList.get(index);
            kickedClient.closeWindow();
            clientArrayList.remove(index);
            this.SynchronizeMessage(parameters.managerMessage(kickedClient.getUsername() + " have been removed"));
            this.SynchronizeUser();
        } else {
            clientArrayList.get(0).updateChatBox("user not found.");
        }
    }

    @Override
    public synchronized void peerExit(String username) throws RemoteException {
        for (IClient iClient : clientArrayList) {
            if (iClient.getUsername().equals(username)) {
                clientArrayList.remove(iClient);
                break;
            }
        }
        this.SynchronizeMessage(parameters.managerMessage(username + " has exitd!\n"));
        this.SynchronizeUser();
    }

    @Override
    public synchronized void newFile() throws RemoteException {
        for (IClient iClient : clientArrayList) {
            iClient.clearCanvas();
        }
        this.canvasShapeArrayList = new ArrayList<CanvasShape>();
        for (CanvasShape canvasShape : this.canvasShapeArrayList) {
            this.SynchronizeCanvas(canvasShape);
        }
    }

    @Override
    public synchronized void openFile(ArrayList<CanvasShape> newShapes) throws RemoteException {
        for (IClient iClient : clientArrayList) {
            iClient.clearCanvas();
        }

        for (CanvasShape canvasShape : newShapes) {
            this.SynchronizeCanvas(canvasShape);
        }
    }

    @Override
    public void managerClose() throws RemoteException {
        for (IClient iClient : clientArrayList) {
            if (!iClient.getUsername().equals("Manager")) {
                iClient.worningFromManager("Manager is closing Whiteboard...Window is closing...");
                iClient.closeWindow();
            }
        }
    }

    @Override
    public synchronized void registerManager(String IpAddress, String port, String name) throws RemoteException {
        ManagerGUI managerGUI = new ManagerGUI(this, IpAddress, port, name);
        clientArrayList.add(managerGUI);
        this.SynchronizeUser();
    }

    @Override
    public synchronized void registerPeer(String username) throws RemoteException {
        PeerGUI peerGUI = new PeerGUI(this, username);
        peerGUI.Build();
        clientArrayList.add(peerGUI);
        this.SynchronizeUser();


    }

    @Override
    public ArrayList<CanvasShape> getCanvasShapeArrayList() throws RemoteException {
        return canvasShapeArrayList;
    }

    public synchronized void setCanvasShapeArrayList(ArrayList<CanvasShape> canvasShapeArrayList) {
        this.canvasShapeArrayList = canvasShapeArrayList;
    }

    @Override
    public ArrayList<IClient> getClientArrayList() throws RemoteException {
        return clientArrayList;
    }

    public void setClientArrayList(ArrayList<IClient> clientArrayList) {
        this.clientArrayList = clientArrayList;
    }

    @Override
    public synchronized void SynchronizeEditing(String username) throws RemoteException {
        for (IClient client : clientArrayList) {
            client.showEditing(username);
        }
    }

    public ArrayList<String> getMessageArrayList() {
        return messageArrayList;
    }

    public void setMessageArrayList(ArrayList<String> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }

    @Override
    public synchronized void SynchronizeCanvas(CanvasShape canvasShape) throws RemoteException {
        canvasShapeArrayList.add(canvasShape);
        for (IClient client : clientArrayList) {
            client.updateShapes(canvasShape);
        }

    }

    @Override
    public synchronized void SynchronizeMessage(String chatMessage) throws RemoteException {
        messageArrayList.add(chatMessage);
        for (IClient client : clientArrayList) {
            client.updateChatBox(chatMessage);
        }
    }

    @Override
    public synchronized void SynchronizeUser() throws RemoteException {
        ArrayList<String> peers = new ArrayList<>();
        for (IClient client : clientArrayList) {
            peers.add(client.getUsername());
        }
        for (IClient client : clientArrayList) {
            client.updatePeerList(peers);
        }

    }


    @Override
    public synchronized boolean checkPeerName(String peerName) throws RemoteException {
        for (IClient client : clientArrayList) {
            if (peerName.equals(client.getUsername())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public synchronized boolean getApprove(String request) throws RemoteException {
        for (IClient client : clientArrayList) {
            if ("Manager".equals(client.getUsername())) {
                return client.requestFromPeer(request);
            }
        }
        return false;
    }


}

package cmdStart;

import Remote.IWhiteBoard;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static Util.parameters.*;

/**
 * @Author Jiawei Luo
 * @Student ID: 1114028
 */
public class JoinWhiteBoard {
    private static final String DEFAULT_WHITEBOARD_NAME = "whiteboard";

    public static void main(String[] args) {
        if (args.length == 3 || args.length == 4) {
            if (!isValidPort(args[1])) {
                System.out.println("please run as -- java -jar JoinWhiteBoard.jar <serverIPAddress> <serverPort> username");
                System.out.println("or -- java JoinWhiteBoard.jar <serverIPAddress> <serverPort> username boardname");
                System.out.println("please provide a valid port in [1024-65535]");
            } else {
                String port = args[1];
                String name;
                if (args.length == 4) {
                    name = args[3];
                } else {
                    name = DEFAULT_WHITEBOARD_NAME;
                }

                String IpAddress = args[0];
                String address = "rmi://" + IpAddress + ":" + port + "/" + name;
                String username = args[2];

                try {
                    IWhiteBoard WBServer = (IWhiteBoard) Naming.lookup(address);
                    System.out.println("Successfully looked up to server.");
                    if (WBServer.checkPeerName(username)) {
                        if (WBServer.getApprove(username)) {
                            System.out.println("Welcome " + username);
                            WBServer.registerPeer(username);
                            String wellcomeMessage =
                                    chatMessageFormat(username,
                                            WELLCOME_MESSAGE + " " + username);
                            WBServer.SynchronizeMessage(wellcomeMessage);
                            System.out.println("Peer GUI successfully created.");
                        } else {
                            System.out.println("you have been rejected by manager");
                        }
                    } else {
                        System.out.println("This Username has been used, try again.");
                    }

                } catch (NotBoundException | MalformedURLException | RemoteException e) {
                    System.out.println("Cannot connect to whiteboard, " +
                            "please use valid Ip, port and name");
                }
                System.out.println("successfully looked up to - " + address);
                System.out.println("trying to get approve from Manager...");
            }
        } else {
            System.out.println("please run as -- java -jar JoinWhiteBoard.jar <serverIPAddress> <serverPort> username");
            System.out.println("or -- java -jar JoinWhiteBoard.jar <serverIPAddress> <serverPort> username boardname");
        }
    }
}

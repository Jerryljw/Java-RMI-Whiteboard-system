package cmdStart;

import Remote.IWhiteBoard;
import WhiteBoardSystem.RMIServer;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static Util.parameters.isValidPort;

/**
 * @Author Jiawei Luo
 * @Student ID: 1114028
 */
public class CreateWhiteBoard {
    private static final String DEFAULT_WHITEBOARD_NAME = "whiteboard";

    public static void main(String[] args) {
        if (args.length == 3 || args.length == 4) {

            if (!isValidPort(args[1])) {
                System.out.println("please run as -- java -jar CreateWhiteBoard.jar <serverIPAddress> <serverPort> boardname");
                System.out.println("please provide a valid port in [1024-65535]");
            } else {
                String port = args[1];
                String name;
                if (args.length == 4) {
                    name = args[2];
                } else {
                    name = DEFAULT_WHITEBOARD_NAME;
                }
                String IpAddress = args[0];


                try {
                    InetAddress localaddress = InetAddress.getByName(IpAddress);
                    String Ip = localaddress.getHostAddress();
                    RMIServer rmiServer = new RMIServer();
                    String address = "rmi://" + Ip + ":" + port + "/" + name;
                    rmiServer.start(address, port, Ip);
                    System.out.println("successfully registered RMI to - " + address);
                    IWhiteBoard WBServer = (IWhiteBoard) Naming.lookup(address);
                    System.out.println("successfully looked up to - " + address);
                    WBServer.registerManager(Ip, port, name);
                    System.out.println("Manager GUI is created, welcome manager.");
                } catch (NotBoundException | MalformedURLException | RemoteException e) {
                    System.out.println("Cannot connect to whiteboard, " +
                            "please use valid Ip, port and name");
                } catch (UnknownHostException unknownHostException) {
                    System.out.println("please provide correct ip address");
                    System.exit(0);
                }
            }
        } else {
            System.out.println("please run as -- java -jar CreateWhiteBoard.jar <serverIPAddress> <serverPort> boardname");
        }
    }
}

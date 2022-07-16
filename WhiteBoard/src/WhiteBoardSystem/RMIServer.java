package WhiteBoardSystem;

import Remote.IWhiteBoard;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @Author Jiawei Luo
 * @Student ID: 1114028
 */
public class RMIServer {
    public RMIServer() {
    }

    public void start(String host, String port) {
        try {
            InetAddress localaddress = InetAddress.getLocalHost();
            String IpAddress = localaddress.getHostAddress();
            System.setProperty("java.rmi.server.hostname", IpAddress);
            System.out.println("IP address set for rmi: " + IpAddress);
            int portnumber = Integer.parseInt(port);
            IWhiteBoard server = new WhiteBoard();
            Registry registry = LocateRegistry.createRegistry(portnumber);
            Naming.rebind(host, server);
        } catch (UnknownHostException unknownHostException) {
            System.out.println("please provide correct ip address");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void start(String host, String port, String IPAddress) {
        try {
            String IpAddress = IPAddress;
            System.setProperty("java.rmi.server.hostname", IpAddress);
            System.out.println("IP address set for rmi: " + IpAddress);
            int portnumber = Integer.parseInt(port);
            IWhiteBoard server = new WhiteBoard();
            Registry registry = LocateRegistry.createRegistry(portnumber);
            Naming.rebind(host, server);
        } catch (Exception e) {
            System.out.println("please provide correct ip address");
            System.exit(0);
        }
    }
}

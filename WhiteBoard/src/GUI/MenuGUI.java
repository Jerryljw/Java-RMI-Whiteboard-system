package GUI;

import Remote.IWhiteBoard;
import WhiteBoardSystem.RMIServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.rmi.Naming;

import static Util.parameters.*;

/**
 * @Author Jiawei Luo
 * @Student ID: 1114028
 */
public class MenuGUI {

    private final JFrame menuframe;
    private final JPanel promptPanel = new JPanel();
    private JPanel menuPanel;
    private JButton peerButton;
    private JButton exitButton;
    private JButton managerButton;
    private IWhiteBoard WBServer;

    public MenuGUI() {
        menuframe = new JFrame();
        menuframe.setBounds(300, 250, 600, 400);
        menuframe.setResizable(false);
        menuframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuframe.setMinimumSize(new Dimension(600, 400));
        menuframe.setMaximumSize(new Dimension(600, 400));
        menuframe.setPreferredSize(new Dimension(600, 400));
        setManagerButton();
        setPeerButton();
        setExitButton();
        menuframe.add(menuPanel);
        menuframe.setContentPane(menuPanel);
        menuframe.pack();
        menuframe.setVisible(true);
        System.out.println("Welcome! Menu is created, please select to create or join a whiteboard!");
    }

    public static void main(String[] args) {
        MenuGUI menuGUI = new MenuGUI();

    }

    private void setManagerButton() {
        managerButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        menuPanel.setVisible(false);
                        menuframe.setVisible(false);

                        JPanel inputPanel = new JPanel();

                        inputPanel.setMinimumSize(new Dimension(556, 376));
                        inputPanel.setMaximumSize(new Dimension(556, 376));
                        inputPanel.setPreferredSize(new Dimension(556, 376));

                        inputPanel.setLayout(null);
                        inputPanel.setVisible(true);

                        JLabel portLabel = new JLabel();
                        portLabel.setText("Port Number");
                        portLabel.setBounds(30, 110, 90, 50);

                        JTextField portField = new JTextField();
                        portField.setBounds(150, 110, 300, 50);

                        JLabel nameLabel = new JLabel();
                        nameLabel.setText("Board Name");
                        nameLabel.setBounds(30, 200, 90, 50);

                        JTextField nameField = new JTextField();
                        nameField.setBounds(150, 200, 300, 50);

                        JButton connectButton = new JButton();
                        connectButton.setBounds(225, 280, 150, 50);
                        connectButton.setText("connect");

                        inputPanel.add(portField);
                        inputPanel.add(portLabel);
                        inputPanel.add(nameField);
                        inputPanel.add(nameLabel);
                        inputPanel.add(connectButton);

                        menuframe.setContentPane(inputPanel);
                        menuframe.pack();
                        menuframe.setVisible(true);

                        connectButton.addActionListener(
                                new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            if (portField.getText().isEmpty() && nameField.getText().isEmpty()) {
                                                JOptionPane.showMessageDialog(menuframe,
                                                        "Please provide a Port number and a Board name",
                                                        "warning",
                                                        JOptionPane.WARNING_MESSAGE);
                                            } else if (portField.getText().isEmpty() || !isValidPort(portField.getText())) {
                                                JOptionPane.showMessageDialog(menuframe,
                                                        "Please provide a valid Port number in [1024-65535]",
                                                        "warning",
                                                        JOptionPane.WARNING_MESSAGE);
                                            } else if (nameField.getText().isEmpty()) {
                                                JOptionPane.showMessageDialog(menuframe,
                                                        "Please provide a Board name",
                                                        "warning",
                                                        JOptionPane.WARNING_MESSAGE);
                                            } else {
                                                String port = portField.getText();
                                                String name = nameField.getText();
                                                InetAddress localaddress = InetAddress.getLocalHost();
                                                String IpAddress = localaddress.getHostAddress();
                                                String address = "rmi://" + IpAddress + ":" + port + "/" + name;
                                                RMIServer rmiServer = new RMIServer();
                                                rmiServer.start(address, port);
                                                System.out.println("successfully registered RMI to - " + address);
                                                WBServer = (IWhiteBoard) Naming.lookup(address);
                                                System.out.println("successfully looked up to - " + address);
                                                WBServer.registerManager(IpAddress, port, name);
                                                System.out.println("Manager GUI is created, welcome manager.");
                                                menuframe.setVisible(false);
                                            }
                                        } catch (Exception exception) {
                                            JOptionPane.showMessageDialog(menuframe,
                                                    "Can not open Manager GUI, Error!!",
                                                    "ERROR",
                                                    JOptionPane.WARNING_MESSAGE);
                                        }
                                    }
                                }
                        );
                    }
                }
        );
    }

    private void setPeerButton() {
        peerButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        menuPanel.setVisible(false);
                        menuframe.setVisible(false);

                        JPanel inputPanel = new JPanel();

                        inputPanel.setMinimumSize(new Dimension(556, 376));
                        inputPanel.setMaximumSize(new Dimension(556, 376));
                        inputPanel.setPreferredSize(new Dimension(556, 376));

                        inputPanel.setLayout(null);
                        inputPanel.setVisible(true);

                        JLabel iPLabel = new JLabel();
                        iPLabel.setText("IP addr");
                        iPLabel.setBounds(30, 60, 90, 40);

                        JTextField iPField = new JTextField();
                        iPField.setBounds(150, 60, 300, 40);

                        JLabel portLabel = new JLabel();
                        portLabel.setText("Port");
                        portLabel.setBounds(30, 120, 90, 40);

                        JTextField portField = new JTextField();
                        portField.setBounds(150, 120, 300, 40);

                        JLabel remoteNameLabel = new JLabel();
                        remoteNameLabel.setText("name");
                        remoteNameLabel.setBounds(30, 180, 90, 40);

                        JTextField remoteNameField = new JTextField();
                        remoteNameField.setBounds(150, 180, 300, 40);

                        JLabel usernameLabel = new JLabel();
                        usernameLabel.setText("username");
                        usernameLabel.setBounds(30, 240, 90, 40);

                        JTextField usernameField = new JTextField();
                        usernameField.setBounds(150, 240, 300, 40);

                        JButton connectButton = new JButton();
                        connectButton.setBounds(225, 300, 150, 50);
                        connectButton.setText("connect");

                        inputPanel.add(iPField);
                        inputPanel.add(iPLabel);
                        inputPanel.add(remoteNameField);
                        inputPanel.add(remoteNameLabel);
                        inputPanel.add(portField);
                        inputPanel.add(portLabel);
                        inputPanel.add(usernameField);
                        inputPanel.add(usernameLabel);
                        inputPanel.add(connectButton);

                        menuframe.setContentPane(inputPanel);
                        menuframe.pack();
                        menuframe.setVisible(true);

                        connectButton.addActionListener(
                                new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        if (portField.getText().isEmpty() && usernameField.getText().isEmpty()) {
                                            JOptionPane.showMessageDialog(menuframe,
                                                    "Please provide a Port number and a Board name",
                                                    "warning",
                                                    JOptionPane.WARNING_MESSAGE);
                                        } else if (portField.getText().isEmpty() || !isValidPort(portField.getText())) {
                                            JOptionPane.showMessageDialog(menuframe,
                                                    "Please provide a valid Port number in [1024-65535]",
                                                    "warning",
                                                    JOptionPane.WARNING_MESSAGE);
                                        } else if (iPField.getText().isEmpty()) {
                                            JOptionPane.showMessageDialog(menuframe,
                                                    "Please provide a valid IP address",
                                                    "warning",
                                                    JOptionPane.WARNING_MESSAGE);
                                        } else if (remoteNameField.getText().isEmpty()) {
                                            JOptionPane.showMessageDialog(menuframe,
                                                    "Please provide a valid whiteboard name",
                                                    "warning",
                                                    JOptionPane.WARNING_MESSAGE);
                                        } else if (usernameField.getText().isEmpty()) {
                                            JOptionPane.showMessageDialog(menuframe,
                                                    "Please provide a username",
                                                    "warning",
                                                    JOptionPane.WARNING_MESSAGE);
                                        } else {

                                            try {
                                                String port = portField.getText();
                                                String name = remoteNameField.getText();
                                                String IpAddress = iPField.getText();
                                                String address = "rmi://" + IpAddress + ":" + port + "/" + name;
                                                WBServer = (IWhiteBoard) Naming.lookup(address);
                                                System.out.println("successfully looked up to - " + address);
                                                System.out.println("trying to get approve from Manager...");
                                                if (WBServer.checkPeerName(usernameField.getText())) {
                                                    if (WBServer.getApprove(usernameField.getText())) {
                                                        String username = usernameField.getText();
                                                        JOptionPane.showMessageDialog(menuframe, "Welcome, " +
                                                                        username, "Welcome",
                                                                JOptionPane.INFORMATION_MESSAGE);
                                                        WBServer.registerPeer(username);
                                                        menuframe.setVisible(false);
                                                        String wellcomeMessage =
                                                                chatMessageFormat(username,
                                                                        WELLCOME_MESSAGE + " " + username);
                                                        WBServer.SynchronizeMessage(wellcomeMessage);
                                                        System.out.println("Peer GUI successfully created.");
                                                    } else {
                                                        JOptionPane.showMessageDialog(menuframe,
                                                                "you have been rejected by manager",
                                                                "Welcome",
                                                                JOptionPane.WARNING_MESSAGE);
                                                    }
                                                } else {
                                                    JOptionPane.showMessageDialog(menuframe,
                                                            "This Username has been used, try again.",
                                                            "warning",
                                                            JOptionPane.WARNING_MESSAGE);
                                                }

                                            } catch (Exception exception) {
                                                JOptionPane.showMessageDialog(menuframe,
                                                        "Cannot connect to whiteboard, " +
                                                                "please use valid Ip, port and name",
                                                        "ERROR",
                                                        JOptionPane.WARNING_MESSAGE);
                                            }

                                        }
                                    }
                                }
                        );
                    }
                }
        );
    }

    private void setExitButton() {
        exitButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                }
        );
    }

}

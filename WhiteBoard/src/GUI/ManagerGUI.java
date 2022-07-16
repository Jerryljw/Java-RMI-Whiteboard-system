package GUI;

import Remote.IClient;
import Remote.IWhiteBoard;
import WhiteBoardSystem.CanvasShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static Util.parameters.chatMessageFormat;

/**
 * @Author Jiawei Luo
 * @Student ID: 1114028
 */
public class ManagerGUI implements IClient, MouseListener, MouseMotionListener, ActionListener, WindowListener {
    private final JFrame managerFrame;
    private final String[] shapes = {"pen", "line", "circle", "oval", "rectangle", "eraser", "text"};
    private final String username;
    private final int portNumber;
    private int ipAddress;
    private final String WBName;
    private JPanel managerPanel;
    private JPanel canvasPanel;
    private JPanel chatPanel;
    private JTextArea userTA;
    private JTextArea chatTA;
    private JScrollPane usersSP;
    private JScrollPane ChatSP;
    private JToolBar shapeColorBar;
    private JToolBar settingBar;
    private JButton newFileButton;
    private JButton openButton;
    private JButton saveButton;
    private JButton saveAsButton;
    private JButton fillButton;
    private JButton closeButton;
    private JButton colorButton;
    private JLabel colorLabel;
    private JButton penButton;
    private JButton lineButton;
    private JButton circleButton;
    private JButton ovalButton;
    private JButton rectButton;
    private JButton earserButton;
    private JButton textButton;
    private String shapeDrawing = "pen";
    private Color color = Color.BLACK;
    private int x1, x2, y1, y2;
    private JComboBox<Integer> strokeCB;
    private JLabel IpLabel;
    private JLabel portLabel;
    private JButton SendButton;
    private JButton kickButton;
    private JTextField sendTextField;
    private JLabel editingJLabel;
    private JLabel nameLabel;
    private final IWhiteBoard iWhiteBoard;
    private ArrayList<Point> pointArrayList;
    private Graphics2D canvasGraphics;
    private boolean isFill = false;
    private final String IpAddress;

    public ManagerGUI(IWhiteBoard whiteBoard, String IpAddress, String port, String WBName) {

        this.iWhiteBoard = whiteBoard;
        username = "Manager";
        this.WBName = WBName;
        this.IpAddress = IpAddress;

        this.portNumber = Integer.parseInt(port);
        IpLabel.setText(IpAddress);
        nameLabel.setText(WBName);
        portLabel.setText(port);
        managerFrame = new JFrame();
        managerFrame.setBounds(150, 100, 1490, 800);
        managerFrame.setResizable(true);
        managerFrame.add(managerPanel);
        managerFrame.setResizable(false);
        managerPanel.setComponentZOrder(canvasPanel, 1);
        managerPanel.setComponentZOrder(chatPanel, 2);
        setColorChooser();
        setStrokeCB();
        setShapeButtons();
        setSendButton();
        setKickButton();
        setFillButton();
        setFileButtons();
        chatTA.setAutoscrolls(true);
        sendTextField.setAutoscrolls(true);
        userTA.setAutoscrolls(true);

        canvasPanel.addMouseListener(this);
        canvasPanel.addMouseMotionListener(this);
        managerFrame.addWindowListener(this);
        managerFrame.pack();
        managerFrame.setVisible(true);
    }


    private void setFileButtons() {
        newFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    iWhiteBoard.newFile();
                } catch (RemoteException remoteException) {
                    JOptionPane.showMessageDialog(managerFrame, "Remote error occur...");
                }
            }
        });
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jFileChooser.setDialogTitle("please choose file path to open");
                File canvasFile = null;

                if (JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(managerFrame)) {
                    canvasFile = jFileChooser.getSelectedFile();
                    if (canvasFile != null) {
                        try {
                            FileInputStream fileInputStream = new FileInputStream(canvasFile);
                            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                            ArrayList<CanvasShape> shapes = (ArrayList<CanvasShape>) objectInputStream.readObject();
                            iWhiteBoard.openFile(shapes);
                            objectInputStream.close();
                            fileInputStream.close();
                            JOptionPane.showMessageDialog(managerFrame, "open file successfully");
                        } catch (ClassNotFoundException | IOException fileNotFoundException) {
                            JOptionPane.showMessageDialog(managerFrame,
                                    "can not open this file, try a correct file.");
                        }
                    }
                }
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jFileChooser.setDialogTitle("please choose file path to save");
                File canvasFile = null;
                if (JFileChooser.APPROVE_OPTION == jFileChooser.showSaveDialog(managerFrame)) {
                    canvasFile = jFileChooser.getSelectedFile();
                    if (canvasFile != null) {
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(canvasFile);
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                            objectOutputStream.writeObject(iWhiteBoard.getCanvasShapeArrayList());
                            objectOutputStream.close();
                            fileOutputStream.close();
                            JOptionPane.showMessageDialog(managerFrame, "file saved.");
                        } catch (IOException fileNotFoundException) {
                            JOptionPane.showMessageDialog(managerFrame, "File save error, try again..");
                        }
                    }

                }
            }
        });
        saveAsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jFileChooser.setDialogTitle("please choose file path to save");
                File canvasFile = null;
                if (JFileChooser.APPROVE_OPTION == jFileChooser.showSaveDialog(managerFrame)) {
                    canvasFile = jFileChooser.getSelectedFile();
                    if (canvasFile != null) {
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(canvasFile);
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                            objectOutputStream.writeObject(iWhiteBoard.getCanvasShapeArrayList());
                            objectOutputStream.close();
                            fileOutputStream.close();
                            JOptionPane.showMessageDialog(managerFrame, "file saved.");
                        } catch (IOException fileNotFoundException) {
                            JOptionPane.showMessageDialog(managerFrame, "File save error, try again..");
                        }
                    }

                }
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    iWhiteBoard.managerClose();
                    System.exit(0);
                } catch (RemoteException remoteException) {
                    JOptionPane.showMessageDialog(managerFrame, "Remote error occur...");
                }
            }
        });
    }

    private void setColorChooser() {
        colorLabel.setOpaque(true);
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = JColorChooser.showDialog(managerFrame, "choose color", null);
                if (color == null) {
                    return;
                }
                colorLabel.setBackground(color);
            }
        });
    }

    private void setStrokeCB() {
        strokeCB.addItem(1);
        strokeCB.addItem(3);
        strokeCB.addItem(5);
        strokeCB.addItem(7);
        strokeCB.addItem(9);
        strokeCB.addItem(11);
        strokeCB.addItem(15);
        strokeCB.addItem(20);
        strokeCB.addItem(25);
    }

    private void setSendButton() {
        SendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = sendTextField.getText();
                if (message != null) {
                    try {
                        iWhiteBoard.SynchronizeMessage(chatMessageFormat(username, message));
                        sendTextField.setText(null);
                    } catch (RemoteException remoteException) {
                        JOptionPane.showMessageDialog(managerFrame, "Remote error occur...");
                    }
                } else {
                    JOptionPane.showMessageDialog(managerFrame, "plz input your message to send");
                }

            }
        });
    }

    private void setFillButton() {
        fillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isFill) {
                    isFill = false;
                    fillButton.setBackground(Color.WHITE);
                } else {
                    isFill = true;
                    fillButton.setBackground(Color.LIGHT_GRAY);
                }
            }
        });
    }

    private void setKickButton() {
        kickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog(managerFrame, "input a username", "kick",
                        JOptionPane.PLAIN_MESSAGE, null, null, null).toString();
                try {
                    iWhiteBoard.removePeer(username);
                } catch (RemoteException remoteException) {
                    JOptionPane.showMessageDialog(managerFrame, "Remote error occur...");
                }
            }
        });
    }

    private void drawCanvasShape(CanvasShape canvasShape) {
        String shapeType = canvasShape.getShapeString();
        int x1 = canvasShape.getX1();
        int y1 = canvasShape.getY1();
        int x2 = canvasShape.getX2();
        int y2 = canvasShape.getY2();
        Color shapeColor = canvasShape.getColor();
        String username = canvasShape.getUsername();
        int strokeInt = canvasShape.getStrokeInt();
        Stroke stroke = new BasicStroke(strokeInt);
        canvasGraphics = (Graphics2D) canvasPanel.getGraphics();

        canvasGraphics.setPaint(shapeColor);
        if (shapeType.equals("line")) {
            canvasGraphics.setStroke(stroke);
            canvasGraphics.drawLine(x1, y1, x2, y2);
        } else if (shapeType.equals("circle") || shapeType.equals("oval") ||
                shapeType.equals("rectangle")) {
            canvasGraphics.setStroke(stroke);
            int height = Math.abs(y2 - y1);
            int width = Math.abs(x2 - x1);
            if (canvasShape.isFill()) {
                switch (shapeType) {
                    case "circle":
                        canvasGraphics.fillOval(Math.min(x1, x2), Math.min(y1, y2), Math.max(width, height), Math.max(width, height));
                        break;
                    case "oval":
                        canvasGraphics.fillOval(Math.min(x1, x2), Math.min(y1, y2), width, height);
                        break;
                    case "rectangle":
                        canvasGraphics.fillRect(Math.min(x1, x2), Math.min(y1, y2), width, height);
                        break;
                }
            } else {
                switch (shapeType) {
                    case "circle":
                        canvasGraphics.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.max(width, height), Math.max(width, height));
                        break;
                    case "oval":
                        canvasGraphics.drawOval(Math.min(x1, x2), Math.min(y1, y2), width, height);
                        break;
                    case "rectangle":
                        canvasGraphics.drawRect(Math.min(x1, x2), Math.min(y1, y2), width, height);
                        break;
                }
            }
        } else if (shapeType.equals("text")) {
            int size = canvasShape.getStrokeInt();
            canvasGraphics.setFont(new Font("Times New Roman", Font.PLAIN, size * 2 + 10));
            canvasGraphics.drawString(canvasShape.getText(), x1, y1);
        } else if (shapeType.equals("pen") || shapeType.equals("eraser")) {
            ArrayList<Point> points = canvasShape.getPoints();
            canvasGraphics.setStroke(stroke);
            for (int i = 1; i < points.size(); i++) {
                canvasGraphics.drawLine(points.get(i - 1).x, points.get(i - 1).y, points.get(i).x, points.get(i).y);
            }
        }

    }

    private void setShapeButtons() {
        chooseToolButton(0);
        penButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseToolButton(0);
                shapeDrawing = shapes[0];
            }
        });
        lineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseToolButton(1);
                shapeDrawing = shapes[1];
            }
        });
        circleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseToolButton(2);
                shapeDrawing = shapes[2];
            }
        });
        ovalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseToolButton(3);
                shapeDrawing = shapes[3];
            }
        });
        rectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseToolButton(4);
                shapeDrawing = shapes[4];
            }
        });
        earserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseToolButton(5);
                shapeDrawing = shapes[5];
            }
        });
        textButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseToolButton(6);
                shapeDrawing = shapes[6];
            }
        });
    }

    private void chooseToolButton(int index) {
        penButton.setBackground(Color.WHITE);
        lineButton.setBackground(Color.WHITE);
        circleButton.setBackground(Color.WHITE);
        ovalButton.setBackground(Color.WHITE);
        rectButton.setBackground(Color.WHITE);
        earserButton.setBackground(Color.WHITE);
        textButton.setBackground(Color.WHITE);
        switch (index) {
            case 0:
                penButton.setBackground(Color.lightGray);
                break;
            case 1:
                lineButton.setBackground(Color.lightGray);
                break;
            case 2:
                circleButton.setBackground(Color.lightGray);
                break;
            case 3:
                ovalButton.setBackground(Color.lightGray);
                break;
            case 4:
                rectButton.setBackground(Color.lightGray);
                break;
            case 5:
                earserButton.setBackground(Color.lightGray);
                break;
            case 6:
                textButton.setBackground(Color.lightGray);
                break;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        try {
            iWhiteBoard.SynchronizeEditing(username);
        } catch (RemoteException remoteException) {
            JOptionPane.showMessageDialog(managerFrame, "Remote error occur...");
        }
        x1 = e.getX();
        y1 = e.getY();
        if (shapeDrawing.equals("pen") || shapeDrawing.equals("eraser")) {
            pointArrayList = new ArrayList<Point>();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        try {
            iWhiteBoard.SynchronizeEditing(null);
        } catch (RemoteException remoteException) {
            JOptionPane.showMessageDialog(managerFrame, "Remote error occur...");
        }
        x2 = e.getX();
        y2 = e.getY();

        int strokeInShape = Integer.parseInt(strokeCB.getSelectedItem().toString());

        CanvasShape canvasShape;
        if (shapeDrawing.equals("pen") || shapeDrawing.equals("eraser")) {
            Color tempColor = color;
            if (shapeDrawing.equals("eraser")) {
                tempColor = Color.white;
            }
            canvasShape = new CanvasShape(shapeDrawing, tempColor, pointArrayList, strokeInShape);
        } else if (shapeDrawing.equals("text")) {
            canvasShape = new CanvasShape(shapeDrawing, color, x1, x2, y1, y2, strokeInShape);
            String texts = JOptionPane.showInputDialog(managerFrame, "input your text", "text",
                    JOptionPane.PLAIN_MESSAGE, null, null, null).toString();
            canvasShape.setText(texts);
            canvasShape.setStrokeInt(Integer.parseInt(strokeCB.getSelectedItem().toString()));
        } else {
            canvasShape = new CanvasShape(shapeDrawing, color, x1, x2, y1, y2, strokeInShape);
        }
        try {
            canvasShape.setFill(isFill);
            iWhiteBoard.SynchronizeCanvas(canvasShape);
        } catch (RemoteException remoteException) {
            JOptionPane.showMessageDialog(managerFrame, "Remote error occur...");
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x3 = e.getX();
        int y3 = e.getY();
        int x4 = x1;
        int y4 = y1;
        canvasGraphics = (Graphics2D) canvasPanel.getGraphics();
        Stroke tempStroke = new BasicStroke(Integer.parseInt(strokeCB.getSelectedItem().toString()));
        if (shapeDrawing.equals("pen") || shapeDrawing.equals("eraser")) {
            if (pointArrayList.size() > 0) {
                x4 = pointArrayList.get(pointArrayList.size() - 1).x;
                y4 = pointArrayList.get(pointArrayList.size() - 1).y;
            }
            Color tempColor = color;
            if (shapeDrawing.equals("eraser")) {
                tempColor = Color.white;
            }
            canvasGraphics.setPaint(tempColor);
            canvasGraphics.setStroke(tempStroke);
            canvasGraphics.drawLine(x4, y4, x3, y3);
            pointArrayList.add(new Point(x3, y3));
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void worningFromManager(String message) throws RemoteException {

    }

    @Override
    public void updateShapes(CanvasShape canvasShape) throws RemoteException {
        new Thread() {
            @Override
            public void run() {
                drawCanvasShape(canvasShape);
            }
        }.start();
    }

    @Override
    public void updateChatBox(String chatMessage) throws RemoteException {
        new Thread() {
            @Override
            public void run() {
                chatTA.append(chatMessage + "\n");
            }
        }.start();
    }

    @Override
    public boolean approveClientRequest(String username) throws RemoteException {
        return false;
    }

    @Override
    public void updatePeerList(ArrayList<String> userlist) throws RemoteException {
        new Thread() {
            @Override
            public void run() {
                userTA.setText(null);
                for (String user : userlist) {
                    userTA.append(user + "\n");
                }

            }
        }.start();
    }

    public boolean requestFromPeer(String request) throws RemoteException {
        return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(managerFrame, request + " wants to join your whiteboard.",
                "Request from Client", JOptionPane.YES_NO_OPTION);
    }

    @Override
    public String getUsername() throws RemoteException {
        return username;
    }

    @Override
    public void reDraw() throws RemoteException {
        new Thread() {
            @Override
            public void run() {
                ArrayList<CanvasShape> shapeArrayList = null;
                try {
                    shapeArrayList = iWhiteBoard.getCanvasShapeArrayList();
                } catch (RemoteException remoteException) {
                    JOptionPane.showMessageDialog(managerFrame, "Remote error occur...");
                }
                for (CanvasShape shape : shapeArrayList) {
                    drawCanvasShape(shape);
                }
            }
        }.start();
    }


    @Override
    public void closeWindow() throws RemoteException {

    }

    @Override
    public void clearCanvas() throws RemoteException {
        canvasPanel.repaint();
    }

    @Override
    public void showEditing(String username) throws RemoteException {
        new Thread() {
            @Override
            public void run() {
                if (username != null) {
                    editingJLabel.setText(username + " is editing.");
                } else {
                    editingJLabel.setText(null);
                }
            }
        }.start();

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            iWhiteBoard.managerClose();
            System.exit(0);
        } catch (RemoteException remoteException) {
            JOptionPane.showMessageDialog(managerFrame, "Remote error occur...");
        }

    }

    @Override
    public void windowClosed(WindowEvent e) {
        managerFrame.setVisible(false);
    }

    @Override
    public void exit() {
        managerFrame.setVisible(false);
    }

    @Override
    public void windowIconified(WindowEvent e) {

        try {
            reDraw();
        } catch (RemoteException remoteException) {
            JOptionPane.showMessageDialog(managerFrame, "Remote error occur...");
        }
    }

    @Override
    public void windowDeiconified(WindowEvent e) {

        try {
            reDraw();
        } catch (RemoteException remoteException) {
            JOptionPane.showMessageDialog(managerFrame, "Remote error occur...");
        }

    }

    @Override
    public void windowActivated(WindowEvent e) {

        try {
            reDraw();
        } catch (RemoteException remoteException) {
            JOptionPane.showMessageDialog(managerFrame, "Remote error occur...");
        }
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // not use it
    }
}

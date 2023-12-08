import java.io.*;
import java.net.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Chat {
    private JPanel panel1;
    private JTextField chatInput;
    private JButton sendButton;
    private JTextField inputIP;
    private JButton startButton;
    private JScrollPane chatWindow1;
    private JTextPane chatWindow;
    private String ipAddress;

    private Socket client;
    private Thread thread;

    public JTextPane getChatWindow() {
        return chatWindow;
    }

    public void setChatWindow(JTextPane chatWindow) {
        this.chatWindow = chatWindow;
    }

    public Chat() {
        Chat tempChat = this;
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ipAddress = inputIP.getText();
                String[] tempAr = ipAddress.split(":");
                inputIP.setVisible(false);
                startButton.setVisible(false);
                try {
                    client = new Socket(tempAr[0], Integer.parseInt(tempAr[1]));
                    chatWindow.setText("connected to " + ipAddress);
                    thread = new Thread(new Aktualisieren(client, tempChat));
                    thread.start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String temp = chatInput.getText();
                chatInput.setText("");
                try {
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    out.println(temp);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat");
        Chat chat = new Chat();
        frame.setContentPane(chat.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(400, 400);
    }
}

class Aktualisieren implements Runnable {
    private Socket client;
    private String temp1;
    private Chat chat;

    public Aktualisieren(Socket client, Chat chat) {
        this.client = client;
        this.chat = chat;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while ((temp1 = in.readLine()) != null) {
                chat.getChatWindow().setText(chat.getChatWindow().getText() + "\n" + temp1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
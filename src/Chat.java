import socketio.Socket;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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



        //thread = new Thread(new Aktualisieren(this,client));
        //thread.start();
        //if (thread.isAlive()){
        //   System.out.println("Thread started!");
        //}
        Chat tempChat = this;
        startButton.addActionListener(new ActionListener() {
            @Override


            public void actionPerformed(ActionEvent e) {
                ipAddress=inputIP.getText();
                String[] tempAr = ipAddress.split(":");
                inputIP.setVisible(false);
                startButton.setVisible(false);
                try {client = new Socket(tempAr[0], Integer.parseInt(tempAr[1]) );} catch (IOException ex) {}
                if(client.connect()){
                    chatWindow.setText("connceted to "+ipAddress);
                }
                thread = new Thread(new Aktualisieren(client,tempChat));
                thread.start();

            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            String temp = chatInput.getText();
            chatInput.setText("");
                //System.out.println(temp);
            try {
                client.write(temp+"\n");
            }catch (Exception e1){}
            }
        });

    }


    public static void main(String[] args){
        JFrame frame = new JFrame("Chat");
        Chat chat = new Chat();
        frame.setContentPane(chat.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(400,400);

    }
}

class Aktualisieren implements Runnable{

    private Socket client;
    private String temp1;
    private Chat chat;
    //private JTextPane chatWindow;
    public Aktualisieren(Socket client, Chat chat){
        this.client=client;
        //this.chatWindow = jTextPane;
        //this.chatWindow=chatWindow;
        this.chat=chat;
    }
    @Override
    public void run() {

            try{

                while(!(temp1 = client.readLine()).contains("exit")){
                    String tttemp = chat.getChatWindow().getText()+temp1;
                    System.out.println(tttemp);
                    chat.getChatWindow().setText(tttemp);
                }
            }catch (Exception e){}

    }
}
import socketio.ServerSocket;
import socketio.Socket;

import java.util.ArrayList;

public class MultiEchoServer{
   private int port = 5414;

   private ServerSocket server;
   private ArrayList<Socket> myClients;
   private ArrayList<Thread> myThreads;
   private String tempChat;

   public MultiEchoServer()throws Exception{
       server = new ServerSocket(port);
       myThreads = new ArrayList<>();
       myClients = new ArrayList<>();

       while(true){
           Socket client = server.accept();
           myClients.add(client);
           System.out.println("Client "+myClients.size()+" accepted!");
           Thread t = new Thread(new Kommunikation(client,myClients.size(),myClients));
           myThreads.add(t);
           t.start();
           System.out.println("Thread "+myThreads.size()+" started!");
           System.out.println();
       }

       /*System.out.println("waiting for client...");
       client = server.accept();
       System.out.println("client accepted!");*/

   }

   /*public void kommunizieren() throws Exception{
       String temp;
       while(!(temp = client.readLine()).contains("exit")){
            client.write("echo: "+temp+"\n");

        }
       System.out.println("closing connection...");
        beenden();

   }*/



   public static void main(String[] args)throws Exception{
       new MultiEchoServer();

   }





}

class Kommunikation implements Runnable{

    private Socket client;
    private int clientNR;
    private ArrayList<Socket> clientList;

    public Kommunikation(Socket client, int clientNR,ArrayList<Socket> clientList){
       this.client=client;
       this.clientNR=clientNR;
       this.clientList=clientList;
    }

    @Override
    public void run() {
        try{kommunizieren();}catch(Exception e){}
    }

    public void kommunizieren()throws Exception{
        String temp;
        while(!(temp = client.readLine()).contains("exit")){
            System.out.println(temp);
            for (Socket tempClient:clientList) {
                tempClient.write("user "+clientNR+": "+temp+"\n");
            }

        }
        System.out.println("closing connection...");
        beenden();
    }

    public void beenden()throws Exception{
        client.close();
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_server_using_gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author SWL
 */
public class ServersConnector
{
    public static void main(String[] args) throws IOException
    {
        
        ServerSocket server = new ServerSocket(8088);
        System.out.println("Server is listening 8088");
         
        Socket client;
         
        
        while (!server.isClosed())
        {
       
            
            System.out.println("Server is waiting 8088");
            client= server.accept();
 
            System.out.println("New client request received : " + client);


            ClientHandler obj = new ClientHandler(client);
            Thread t = new Thread(obj);
            t.start();
            

 
        }
        server.close();
    }
}
class connectorHandler implements Runnable{
   public static ArrayList<connectorHandler>connectors=new ArrayList<>();
   public Socket socket;
   private String name;
   private PrintWriter outFromServer;
   // private ServerSocket serverSocket;
    private Scanner inFromClient,inconsole;
    
    public connectorHandler(Socket socket){
        try{
            this.socket=socket;
            this.outFromServer=new PrintWriter(socket.getOutputStream());
            this.inFromClient=new Scanner(socket.getInputStream());
            this.name=inFromClient.nextLine();
            connectors.add(this);
             brodcast(name+" someone join");
            
        }
        catch (Exception e) {
			// TODO: handle exception
		}
    }
     @Override
    public void run() {
 
       while(socket.isConnected()) {
    	   try {
    		   String message;
    		   message=inFromClient.nextLine();
                   
    		   brodcast(message);
			} catch (Exception e) {
				// TODO: handle exception
			}
       }
    }
    
    void brodcast(String msg){
        for( connectorHandler connector : connectors){
            try{
                if(!connector.name.equals(name)){
                    connector.outFromServer.write(msg);
                    connector.outFromServer.println();
                }
            }catch(Exception e){
                
            }
        }
    }
    
}

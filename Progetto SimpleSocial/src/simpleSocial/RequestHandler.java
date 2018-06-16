package simpleSocial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class RequestHandler extends Thread{
	
	private String username;
	private ServerSocket listeningSocket;		// riferimento al welcoming socket associato al client
	private LinkedList<String> friendRequests;	// riferimento alla lista delle richieste di amicizia
	private boolean termina;
	
	public RequestHandler(String u, ServerSocket s, LinkedList<String> fr){
		username 		= u;
		listeningSocket = s;
		friendRequests 	= fr;
		termina 		= false;
	}
	
	public void run(){
		Socket s 			  = null;
		BufferedReader reader = null;
		String msg 			  = null;
		try {
			while(!termina){
				s 		= listeningSocket.accept();
				reader 	= new BufferedReader(new InputStreamReader(s.getInputStream()));
				msg 	= reader.readLine();
				if(msg.equals("END"))
					termina = true;
				else {
					System.out.println(msg);
					
					// legge il nome dell'utente che ha fatto la richiesta di amicizia,
					String senderUser = reader.readLine();
					
					if(!friendRequests.contains(senderUser)){
						// lo aggiunge alla lista interna del SocialClient
						synchronized(friendRequests){
							friendRequests.add(senderUser);
						}
						// lo appende al file
						BufferedWriter bf = new BufferedWriter(new FileWriter(username + ".requests", true));
						bf.append(senderUser);
						bf.newLine();
						bf.flush();
						bf.close();
					}
				}
					
			}
		} catch (IOException e) {
			System.out.println("error: errore di I/O. " + e.getMessage());
		} finally {
			try {
				if(reader != null) reader.close();
				if(s != null) 	   s.close();
				listeningSocket.close();
			} catch(IOException e){
				System.out.println("error: errore chiusura stream. " + e.getMessage());
			}
		}
	}
	
}

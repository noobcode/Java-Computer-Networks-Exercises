package simpleSocial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;

public interface ClientInterface {
	
	/** permette all'utente di registrarsi inserendo username e password
	 * 
	 * @param operation, 	operazione scelta dall'utente
	 * @param in, 			legge l'input da tastiera
	 * @param reader, 		legge dallo stream della socket
	 * @param writer, 		scrive sullo stream della socket
	 * @throws IOException
	 */
	public void signIn(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException;
	
	/** permette all'utente già registrato di effettuare il login
	 * 
	 * @param operation, 	operazione scelta dall'utente
	 * @param in, 			legge l'input da tastiera
	 * @param reader, 		legge dallo stream della socket
	 * @param writer, 		scrive sullo stream della socket
	 * @throws IOException
	 * @throws NotBoundException 
	 */
	public void login(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException, NotBoundException;
		
	
	/** permette all'utente di effettuare una richiesta di amicizia
	 * 
	 * @param operation, 	operazione scelta dall'utente
	 * @param in, 			legge l'input da tastiera
	 * @param reader, 		legge dallo stream della socket
	 * @param writer, 		scrive sullo stream della socket
	 * @throws IOException
	 */
	public void friendRequest(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException;
	
	/** permette all'utente di visualizzare le richieste di amicizia pendenti e scegliere se accettare/rifiutare una di queste.
	 * l'utente può inserire un nome e poi scrivere:
	 *  SI per accettare, NO per rifiutare.
	 * 
	 * @param operation, 	operazione scelta dall'utente
	 * @param in, 			legge l'input da tastiera
	 * @param reader, 		legge dallo stream della socket
	 * @param writer, 		scrive sullo stream della socket
	 * @throws IOException
	 */
	public void showFriendRequest(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException;
	
	/** permette all'utente di visualizzare la lista degli amici e sapere, per ognuno di essi, se sono online/offline.
	 * 
	 * @param operation, 	operazione scelta dall'utente
	 * @param reader, 		legge dallo stream della socket
	 * @param writer, 		scrive sullo stream della socket
	 * @throws IOException
	 */
	public void showFriendList(int operation, BufferedReader reader, BufferedWriter writer) throws IOException;
	
	/**
	 * permette di ricercare utenti inserendo il nome
	 * @param operation, 	operazione scelta dall'utente
	 * @param in, 			legge dallo stardard input
	 * @param reader, 		legge dallo stream della socket
	 * @param writer, 		scrive sullo stream della socket
	 * @throws IOException 
	 */
	public void searchUser(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException;	
	
	/**
	 * permette all'utente di pubblicare un messaggio
	 * @param operation,	operazione scelta dall'utente
	 * @param in,			legge dallo standard input
	 * @param reader,		legge dallo stream della socket
	 * @param writer,		scrive sullo stream della socket
	 * @throws IOException
	 */
	public void publishContent(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException;

	/**
	 * permette all'utente di seguire i messaggi pubblicati da un amico
	 * @param operation,	operazione scelta dall'utente
	 * @param in,			legge dallo standard input
	 * @param reader,		legge dallo stream della socket
	 * @param writer,		scrive sullo stream della socket
	 * @throws IOException
	 */
	public void registerInterest(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException;
	
	/** permette all'utente di effettuare il logout
	 * 
	 * @param operation, 	operazione scelta dall'utente
	 * @param writer, 		scrive sullo stream della socket
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void logout(int operation, BufferedWriter writer) throws IOException, InterruptedException;
		
	/**
	 * mostra i messaggi ricevuti contenuti nella lista.
	 * la lista viene svuotata.
	 * il file salvato su disco viene troncato a zero length.
	 * @throws FileNotFoundException
	 */
	public void showContents() throws FileNotFoundException; 
	
	
	public void refurbishToken(int operation, BufferedReader reader, BufferedWriter writer) throws IOException;
}

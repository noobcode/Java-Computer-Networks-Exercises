/* creazione di un server,
 * sottomissione di una sequenza di task al server. il server li eseguirà
 * in modo concorrente utilizzando un thread pool.
 * terminazione del server.
 */
public class Main {

	public static void main(String[] args) throws Exception {
		Server server = new Server();
		for(int i = 0; i < 10; i++){
			Task task = new Task("task " + i);
			server.executeTask(task);
			Thread.sleep(5000); /* in questo modo il programma riutilizza sempre lo stesso thread. */
		}
		
		server.endServer();
	}

}

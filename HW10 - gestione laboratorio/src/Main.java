
public class Main {
	
	public static void main(String[] args) {
		String usage = "usage: java Main nProfessori nTesisti nStudenti";
		// controllo numero argomenti
		if(args.length != 3){
			System.out.println(usage);
			System.exit(1);
		}
		
		// parsing argomenti
		int nProfessori = 0, nTesisti = 0, nStudenti = 0;
		try{
			nProfessori = Integer.parseInt(args[0]);
			nTesisti = Integer.parseInt(args[1]);
			nStudenti = Integer.parseInt(args[2]);
		}catch(NumberFormatException e){
			System.out.println(usage);
			e.printStackTrace();
			System.exit(1);
		}
		
		// inizializzazione
		int nThread = nProfessori+nTesisti+nStudenti;
		Thread[] t = new Thread[nThread];
		int N = 20; // numero computer nel laboratorio
		Laboratorio lab = new Laboratorio(N);
		spawnThread(t, nThread, nProfessori, nTesisti, nStudenti, lab);
		waitThread(t, nThread);
	}
	
	
	private static void spawnThread(Thread[] t, int nthread, int nProfessori, int nTesisti, int nStudenti, Laboratorio lab){
		for(int i = 0; i < nProfessori; i++)
				t[i] = new Thread(new Professore(i, lab));
		for(int i = 0; i < nTesisti; i++)
			t[i + nProfessori] = new Thread(new Tesista(i, lab));
		for(int i = 0; i < nStudenti; i++)
			t[i + nProfessori + nTesisti] = new Thread(new Studente(i,lab));
		
		// start
		for(int i = 0; i < nthread;i++)	t[i].start();
	}
	
	private static void waitThread(Thread[] t, int nThread){
		for(int i = 0; i < nThread; i++)
			try {
				t[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
}

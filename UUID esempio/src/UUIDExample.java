import java.util.UUID;

public class UUIDExample {

	public static final void main(String[] Args){
	    //generate random UUIDs
	    UUID idOne = UUID.randomUUID();
	    UUID idTwo = UUID.randomUUID();
	    
	    log("UUID One: " + idOne);
	    log("UUID Two: " + idTwo);
	    
	    System.out.println("hashcode " + idOne.hashCode());
	    System.out.println("hashcode " + idTwo.hashCode());
	    
	    
	} 

	
	private static void log(Object aObject){
	    System.out.println( String.valueOf(aObject) );
	  }
}

import java.io.Serializable;
import java.util.*;

public class PersistentTime implements Serializable {

	private static final long serialVersionUID = 1L;
	private Date time;
	
	public PersistentTime(){
		time = Calendar.getInstance().getTime();
	}
	
	public Date getTime(){
		return time;
	}
}

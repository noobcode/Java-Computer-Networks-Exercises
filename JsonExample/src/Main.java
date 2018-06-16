import java.io.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {
		JSONObject scatola = new JSONObject();
		JSONParser parser = new JSONParser();
		Writer f = new FileWriter("prova.txt");
		scatola.put("contiene", "viti");
		scatola.put("quantità", 100);
		scatola.put("volume", 12);
		scatola.put("magazzino", true);
		// la rappresentazione JSON corrisponde ad una stringa di caratteri
		System.out.println(scatola.toJSONString());
		JSONObject o = (JSONObject) parser.parse("{\"contiene\":\"caramelle\",\"magazzino\":true,\"quantità\":30,\"volume\":12}\"");
		System.out.println(o.toJSONString());
		System.out.println(o.get("contiene"));
	}

}	

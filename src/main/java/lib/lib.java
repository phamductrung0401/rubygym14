package lib;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class lib {
	
	//Lấy dữ liệu trong Body của 1 http request
	public static Object getBody(HttpServletRequest req) throws Exception {
		try {
			BufferedReader br = req.getReader();
			return JSONValue.parse(readAllLines(br));
		}
		catch(Exception e) {
			throw e;
		}
	}	
	//Chuyển đổi BufferedReader sang String
	public static String readAllLines(BufferedReader reader) throws IOException {
	    StringBuilder content = new StringBuilder();
	    String line;
	    
	    while ((line = reader.readLine()) != null) {
	        content.append(line);
	        content.append(System.lineSeparator());
	    }

	    return content.toString();
	}
}

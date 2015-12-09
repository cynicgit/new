package ip.cynic.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {

	public static String streamToString(InputStream in) throws IOException{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int len = 0;
		byte buffer[] = new byte[1024];
		while((len=in.read(buffer))!=-1){
			os.write(buffer, 0, len);
		}
		
		String result = os.toString("utf-8");
		os.close();
		in.close();
		return result;
	}
	
}

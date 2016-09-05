package com.example.server;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;



public class StreamTools {

	public static byte [] read(InputStream inputStream) throws Exception{

		System.out.println("那类");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte [] data = new byte[1024];
		int len=0;
		while((len=inputStream.read(data))!=-1){
			outputStream.write(data, 0, len);
		}
		System.out.println("这里");
		outputStream.close();
		return outputStream.toByteArray();
	}

}

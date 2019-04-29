package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class KeyboardTest {

	public static void main(String[] args) {
		BufferedReader br = null;
		
		//기반스트림(표준입력, 키보드, System.in)
		
		try {
			//보조스트림1
			//byte | byte | byte -> car
			InputStreamReader isr = new InputStreamReader(System.in, "utf-8");
		
			//보조스트림2
			//char | char | char | \n
			br = new BufferedReader(isr);
			
			//read
			String line = null;
			while((line = br.readLine()) != null) {
				if(line.equals("exit")) break;
				System.out.println(">>" + line);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(br != null) br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

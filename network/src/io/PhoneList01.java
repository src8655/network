package io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class PhoneList01 {

	public static void main(String[] args) {
		BufferedReader br = null;
		try {
			//기반스트림
			FileInputStream fis = new FileInputStream("phone.txt");
			//보조스트림1(bytes->char)
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			//보조스트림2
			br = new BufferedReader(isr);
			String line = null;
			while((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "\t ");
				
				int index = 0;
				while(st.hasMoreElements()) {
					String token = st.nextToken();
					if(index == 0) {
						System.out.print(token + " : ");
					}else if(index == 1) {
						System.out.print(token + "-");
					}else if(index == 2) {
						System.out.print(token + "-");
					}else if(index == 3){
						System.out.print(token);
					}
					
					index++;
				}
				System.out.println();
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

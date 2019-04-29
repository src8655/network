package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		InetAddress[] inetAddresses;
		Scanner scanner = new Scanner(System.in);
			
		while(true) {
			System.out.print("입력 : ");
			String hostname = scanner.nextLine();
			if(hostname.equals("exit"))  {
				System.out.println("종료.");
				break;
			}

			try {
				inetAddresses = InetAddress.getAllByName(hostname);
				for(InetAddress addr : inetAddresses) {
					System.out.println(hostname + " : " + addr.getHostAddress());
				}
			} catch (UnknownHostException e) {
				System.out.println("찾는데 실패하였습니다.");
			}
		}
			

	}

}

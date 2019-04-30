package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class UDPEchoClient {
	private static final String SERVER_IP = "192.168.0.5";
	private static final int SERVER_PORT = 7000;

	public static void main(String[] args) {
		Scanner scanner = null;
		DatagramSocket socket = null;
		
		try {
			//1. Scanner 생성(표준입력 연결)
			scanner = new Scanner(System.in);
			
			//2. 소켓 생성
			socket = new DatagramSocket();
			
			//3. 서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			log("connected");
			
			
			while(true) {
				//4. 키보드 입력 받기
				System.out.print(">>");
				String line = scanner.nextLine();
				if("quit".contentEquals(line)) {
					break;
				}
				
				//5. 데이터 쓰기
				byte[] sendData = line.getBytes("utf-8");
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, new InetSocketAddress(SERVER_IP, SERVER_PORT));
				socket.send(sendPacket);
				
				//6. 데이터 읽기
				DatagramPacket receivePacket = new DatagramPacket(new byte[UDPEchoServer.BUFFER_SIZE],  UDPEchoServer.BUFFER_SIZE);
				socket.receive(receivePacket);	//여기서 block됨
				
				//7. 콘솔출력
				byte[] data = receivePacket.getData();
				System.out.println("<<" + new String(data, "utf-8"));
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(scanner != null) scanner.close();
			if(socket != null && !socket.isClosed()) socket.close();
		}
	}

	//log하는놈
	public static void log(String log) {
		System.out.println("[client] " + log);
	}

}

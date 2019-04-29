package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {
	private static final int PORT = 7000;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		
		try {
			//1. 서버소켓 생성
			serverSocket = new ServerSocket();
			
			//2. 바인딩(binding)
			//	:Socket에 SocketAddress(IPAddress + Port)를 바인딩 한다.
			
			serverSocket.bind(new InetSocketAddress("0.0.0.0", PORT));
			//3. accept
			//	:클라이언트의 연결요청을 기다린다.
			//	:요청시 클라이언트의 정보(ip)를 받아온다.
			
			while(true) {
				log("server start...[port:" + PORT + "]");
				Socket socket = serverSocket.accept();	//blocking
				Thread thread = new EchoServerReceiveThread(socket);
				thread.start();
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
				try {
					if(serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	//log하는놈
	public static void log(String log) {
		System.out.println("[server#"+Thread.currentThread().getId()+"] " + log);
	}

}

package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		
		try {
			//1. 서버소켓 생성
			serverSocket = new ServerSocket();
			
			//2. 바인딩(binding)
			//   :Socket에 SocketAddress(IPAddress + Port)를 바인딩 한다.
			
			//InetAddress inetAddress = InetAddress.getLocalHost();
			//String localhost = inetAddress.getHostAddress();
			//serverSocket.bind(new InetSocketAddress(localhost, 5000));
			
			//serverSocket.bind(new InetSocketAddress(inetAddress, 5000));
			
			serverSocket.bind(new InetSocketAddress("0.0.0.0", 6000));
			System.out.println("[server] waiting client...");
			//3. accept
			//   :클라이언트의 연결요청을 기다린다.
			//	:요청시 클라이언트의 정보(ip)를 받아온다.
			Socket socket = serverSocket.accept();	//blocking
			
			//클라이언트가 누군지
			InetSocketAddress inetSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
			String remoteHostaddress = inetSocketAddress.getAddress().getHostAddress();
			int remotePort = inetSocketAddress.getPort();
			
			System.out.println("[server] connected by client["+remoteHostaddress+":"+remotePort+"]");
			
			
			//4. IOStream 받아오기
			//BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			InputStream is = null;
			OutputStream os = null;
			
			try {
				is = socket.getInputStream();
				os = socket.getOutputStream();
				while(true) {
					//5. 데이터 읽기
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer);
					if(readByteCount == -1) {
						//클라이언트가 정상종료
						//close() 메소드 호출
						System.out.println("[server] close client");
						break;
					}
					String data = new String(buffer, 0, readByteCount, "utf-8");
					System.out.println("[server] received:"+data);
					
					
					//6. 데이터 쓰기
					os.write(data.getBytes("utf-8"));
					
				}
				
				
				
				
			} catch (SocketException e) {
				System.out.println("[server] sudden closed by client");
			}catch (IOException e) {
				e.printStackTrace();
			}finally {
				if(is != null) is.close();
				if(os != null) os.close();
				if(socket != null && !socket.isClosed()) socket.close();
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

}

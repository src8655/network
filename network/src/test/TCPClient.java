package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClient {
	private static final String SERVER_IP = "192.168.0.5";
	private static final int SERVER_PORT = 6000;

	public static void main(String[] args) {
		//1. 소켓 생성
		Socket socket = new Socket();
		
		try {
			//2. 서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			System.out.println("[client] connected");
			
			//3. IOStream 받아오기
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			//4. 쓰기
			String data = "Hello World\n";
			os.write(data.getBytes("utf-8"));
			
			//5. 읽기
			byte[] buffer = new byte[256];
			int readByteCount = is.read(buffer);
			if(readByteCount == -1) {
				System.out.println("[client] closed by server");
			}
			
			//6. 데이터 쓰기
			data = new String(buffer, 0, readByteCount, "utf-8");
			System.out.println("[client] received : " + data);
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(socket != null && !socket.isClosed())
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}

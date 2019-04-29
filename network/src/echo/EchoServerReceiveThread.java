package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread {
	private Socket socket = null;
	
	public EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {

		//클라이언트가 누군지
		InetSocketAddress inetSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
		String remoteHostaddress = inetSocketAddress.getAddress().getHostAddress();
		int remotePort = inetSocketAddress.getPort();
		
		EchoServer.log("connected by client["+remoteHostaddress+":"+remotePort+"]");
		
		
		try {
			//4. IOStream 생성
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			//bw = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
			//true는 auto flush
			PrintWriter pr = new PrintWriter(new OutputStreamWriter(os, "utf-8"), true);
			
			
			while(true) {
				//5. 데이터 읽기
				String data = br.readLine();
				if(data == null) {
					EchoServer.log("closed by client");
					break;
				}
				
				EchoServer.log("received : " + data);
				
				//6. 데이터 쓰기
				pr.println(data);
			}
			
			
			
		} catch (SocketException e) {
			EchoServer.log("sudden closed by client");
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
				try {
					if(socket != null && !socket.isClosed()) socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}

}

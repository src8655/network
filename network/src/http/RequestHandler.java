package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class RequestHandler extends Thread {
	private static String documentRoot = "";
	
	static {
			documentRoot = RequestHandler.class.getClass().getResource("/webapp").getPath();

			System.out.println(documentRoot);
	}
	
	private Socket socket;
	
	public RequestHandler( Socket socket ) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
			consoleLog( "connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() );
			
			// get IOStream
			OutputStream os = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			
			String request = null;	//파싱
			while(true) {
				String line = br.readLine();
				//브라우저가 연결을 끊으면
				if(line == null) break;
				// Request Header만 읽음
				if("".equals(line)) break;
				//Header의 첫번째 라인만 처리
				if(request == null) request = line;
			}
			
			String[] tokens = request.split(" ");
			if("GET".equals(tokens[0])) {
				consoleLog("request : " + tokens[1]);
				responseStaticResource(os, tokens[1], tokens[2]);
				
			}else {	// POST, PUT, DELETE, HEAD, CONNECT
					// 와 같은 Method는 무시
				/*
				HTTP/1.1 400 File Not Found\r\n
				Content-Type:text/html; charset=utf-8\r\n
				*/
				
				response400Error(os, tokens[2], tokens[1]);
				consoleLog("Bad Request : " + tokens[1]);
			}
			
		} catch( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
				
			} catch( IOException ex ) {
				consoleLog( "error:" + ex );
			}
		}			
	}

	private void responseStaticResource(OutputStream os, String url, String protocol) throws IOException {
		if("/".equals(url)) {
			url = "/index.html";
		}
		
		//파일이 없으면
		File file = new File(documentRoot+url);
		if(!file.exists()) {
			/*
			HTTP/1.1 404 File Not Found\r\n
			Content-Type:text/html; charset=utf-8\r\n
			*/
			
			response404Error(os, protocol);
			
			return;
		}
		
		//nio
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());
		
		//응답
		os.write( (protocol + " 200 OK\r\n").getBytes( "UTF-8" ) );
		os.write( ("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		os.write( "\r\n".getBytes() );	//구분 ( 위 헤더 아래 바디의 구분공백 )
		os.write(body);
	}

	private void response404Error(OutputStream os, String protocol) throws IOException {
		File file = new File(documentRoot+"/error/404.html");
		byte[] body = Files.readAllBytes(file.toPath());
		
		//에러응답
		os.write( (protocol + " 404 File Not Found\r\n").getBytes( "UTF-8" ) );
		os.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
		os.write( "\r\n".getBytes() );
		os.write(body);
	}
	private void response400Error(OutputStream os, String protocol, String url) throws IOException {
		File file = new File(documentRoot+"/error/400.html");
		byte[] body = Files.readAllBytes(file.toPath());
		
		//에러창에 파일명 표시
		String bodys = new String(body, "utf-8");
		bodys = bodys.replace("#filename#", url);
		
		//에러응답
		os.write( (protocol + " 400 Bad Request\r\n").getBytes( "UTF-8" ) );
		os.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
		os.write( "\r\n".getBytes() );
		os.write(bodys.getBytes("utf-8"));
	}

	public void consoleLog( String message ) {
		System.out.println( "[RequestHandler#" + getId() + "] " + message );
	}
}

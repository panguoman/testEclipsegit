package project;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	int port = 8821;
	private Socket socket = null;
	
	public void start(){
		Thread thread = null;
		try{
			ServerSocket server = new ServerSocket(port);
			while(true){
				
				// public Socket accept() throws 
				// IOException侦听并接受到此套接字的连接。此方法在进行连接之前一直阻塞。
				socket = server.accept();
				System.out.println("建立socket连接");
				
				thread = new Thread(new SocketRun(socket));
				thread.start();
				
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(socket != null){
				try{
					socket.close();
				}catch(Exception e){
					socket = null;
					System.out.println("服务端 finally 异常:" + e.getMessage());
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
}

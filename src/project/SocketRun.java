package project;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketRun implements Runnable{
	private Socket socket = null;
	
	public SocketRun(Socket socket){
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
		System.out.println("----------------------------");
		DataInputStream input = null;
		DataOutputStream out = null;
		try{
			input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			
			//向客户端回复消息
			out = new DataOutputStream(socket.getOutputStream());
			
			while(true){
				String clientInputStr = input.readUTF();
				//处理客户端数据
				System.out.println("客户端发送过来的内容" + clientInputStr);
				System.out.println("请输入:\t");
				String s = null;
				s = new BufferedReader(new InputStreamReader(System.in)).readLine();
				out.writeUTF(s);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(Thread.currentThread() + "出错：" + e.getMessage());
		}finally{
			try {
				out.close();
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
}

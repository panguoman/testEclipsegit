package sendDocument;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketRun implements Runnable{
	private Socket socket = null;
	
	public SocketRun(Socket socket){
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
		DataInputStream input = null;
		DataOutputStream out = null;
		DataOutputStream fileOut = null;
		try{
			//接受客户端发送过来的消息
			input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			//接收文件名
			String fileName = input.readUTF();
			System.out.println("客户端发送过来的内容 文件名为:" + fileName);
			//接收文件大小
			Long fileSize = input.readLong();
			System.out.println("文件大小为:" + fileSize);
			//接收文件数据
			String savePath = "C:\\Users\\Administrator\\Desktop\\新建文件夹\\1111\\" + fileName;
			fileOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(savePath)));
			byte[] buf = new byte[1024];
			while(true){
				int read = 0;
				if(input != null){
					read = input.read(buf,0,buf.length);
				}
				if(read == -1){
					break;
				}
				fileOut.write(buf, 0, read);
			}
			fileOut.close();
			//向客户端回复消息
			out = new DataOutputStream(socket.getOutputStream());
			String s = "accept end";
			out.writeUTF(s);
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(Thread.currentThread() + "出错：" + e.getMessage());
		}finally{
			try {
				if(out != null)out.close();
				if(input != null)input.close();
				if(socket != null){
					socket.close();
					socket = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("服务端 finally 异常:" + e.getMessage());
			}
		}
		
		
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
}

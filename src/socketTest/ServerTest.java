package socketTest;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {
	
	int port = 8821;
	
	public void start(){
		Socket socket = null;
		try{
			ServerSocket server = new ServerSocket(port);
			while(true){
				//选择进行传输的文件
				String filePath = "C:\\Users\\Administrator\\Desktop\\新建文件夹\\222.png";
				File file = new File(filePath);
				
				System.out.println("文件长度" + (int)file.length());
				
				// public Socket accept() throws 
				// IOException侦听并接受到此套接字的连接。此方法在进行连接之前一直阻塞。
				socket = server.accept();
				System.out.println("建立socket连接");
				DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				dis.readByte();
				
				DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
				DataOutputStream ps = new DataOutputStream(socket.getOutputStream());
				
				//将文件名及长度传给客户端。这里要真正适用所有平台，例如中文名的处理，还需要加工，具体可以参见Think In Java 4th里有现成的代码。
				ps.writeUTF(file.getName());
				ps.flush();
				ps.writeLong((long)file.length());
				ps.flush();
				
				int bufferSize = 8192;
				byte[] buf = new byte[bufferSize];
				
				while(true){
					int read = 0;
					if (fis != null){
						read = fis.read();
					}
					
					if(read == -1){
						break;
					}
					ps.write(buf,0,read);
				}
				ps.flush();
				// 注意关闭socket链接哦，不然客户端会等待server的数据过来， 
				// 直到socket超时，导致数据不完整。
				fis.close();
				socket.close();
				System.out.println("文件传输完成");
				
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ServerTest().start();
	}
	
}

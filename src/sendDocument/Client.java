package sendDocument;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
	public final String IP_ADDR = "localhost";//服务器地址   
    public final int PORT = 8821;//服务器端口号    
    
    public static void main(String[] args) {
		new Client().uploadFile("111.png");
		new Client().uploadFile("222.png");
		new Client().uploadFile("333.png");
	}
    
    /**
     * 从服务器读取文件
     */
    public void downloadFile(){
    	
    }
    
    /**
     * 上传文件到服务器
     * @param name
     */
    public void uploadFile(String name) {    
        DataInputStream input = null;
        DataOutputStream out = null;
        DataInputStream fileInput = null;
        
        Socket socket = null;  
        try { 
        	File file = new File("C:\\Users\\Administrator\\Desktop\\新建文件夹\\" + name);
        	long fileSize = file.length();
        	String fileName = file.getName();
        	
            //创建一个流套接字并将其连接到指定主机上的指定端口号  
            socket = new Socket(IP_ADDR, PORT);    
            
            //向服务器端发送数据    
            out = new DataOutputStream(socket.getOutputStream()); 
            //向服务器端发送文件名
            out.writeUTF(fileName);
            out.flush();
            //向服务器端发送文件大小
            out.writeLong(fileSize);
            out.flush();
            //向服务器端发送 文件数据
            //读取文件内容
            fileInput = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            byte[] bup = new byte[1024];
            while(true){
            	int read = 0;
            	if(fileInput != null){
            		read = fileInput.read(bup,0,bup.length);
            	}
            	if(read == -1){ 
            		break;
            	}
            	out.write(bup, 0, read);
            }
            out.flush();
            out.close();
            //读取服务器端数据    
            input = new DataInputStream(socket.getInputStream()); 
            String ret = input.readUTF();  
            // 如接收到 "accept end" 则断开连接    
            if ("accept end".equals(ret)) {    
                System.out.println("客户端将关闭连接");    
            }
            
        } catch (Exception e) {  
            System.out.println("客户端异常:" + e.getMessage());   
        } finally {
        	try {
				if(input != null) input.close();
				if(fileInput != null) fileInput.close();
				if (socket != null) {  
                    socket.close();  
                    System.out.println("socket is closed");  
                    socket = null;   
                } 
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("客户端 finally 异常:" + e.getMessage());   
			}  
        }  
    } 
    
    
}

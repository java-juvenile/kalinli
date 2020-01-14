package com.maxwit.course;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    void start() {
        Socket s = null;
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            s = serverSocket.accept();
            DataInputStream dis = new DataInputStream(s.getInputStream());
            String str = dis.readUTF();
            String[] arr = str.split("/");

            File f = new File(arr[arr.length - 1]);
            System.out.println("文件长度:" + (int) f.length());
            FileInputStream fis = new FileInputStream(f);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF(f.getName());
            dos.flush();
            dos.writeLong((long) f.length());
            dos.flush();

            byte[] bytes = new byte[1024];
            int length = 0;
            long progress = 0;
            while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                dos.write(bytes, 0, length);
                dos.flush();
                progress += length;
                System.out.println("| " + (100 * progress / f.length()) + "% |");

            }
            System.out.println("======== 文件传输成功 ========");
            fis.close();
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    public static void main(String[] args) {
        FileServer fileServer = new FileServer();
        fileServer.start();
    }
}

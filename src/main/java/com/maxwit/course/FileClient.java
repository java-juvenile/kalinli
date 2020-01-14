package com.maxwit.course;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class FileClient {
   void accept(String ip, String p, String path) throws IOException {
      DataInputStream dis = null;
      DataOutputStream out = null;
      FileOutputStream fos = null;

      Integer port = Integer.parseInt(p);
      try {
         Socket socket = new Socket(ip, port);
         dis = new DataInputStream(socket.getInputStream());
         out = new DataOutputStream(socket.getOutputStream());
         out.writeUTF(path);
         String fileName = dis.readUTF();
         long fileLength = dis.readLong();
         File f = new File(fileName);

         fos = new FileOutputStream(f);
         byte[] bytes = new byte[1024];
         int length = 0;
         long progress = 0;
         while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
            fos.write(bytes, 0, length);
            fos.flush();
            progress += length;
            System.out.print("| " + (100*progress/fileLength) + "% |");
         }
         System.out.println();
         System.out.print(fileName +  ": 文件传输成功");
      } catch (IOException e) {
         e.printStackTrace();
         // TODO: handle exception
      }finally {
         if (dis != null) {
            dis.close();
         }
         if(out != null) {
            out.close();
         }
         if(fos != null) {
            fos.close();
         }
      }

   }

   public static void main(String[] args) throws IOException {
      FileClient socketClient = new FileClient();
      socketClient.accept(args[1], args[2], args[3]);
   }
}

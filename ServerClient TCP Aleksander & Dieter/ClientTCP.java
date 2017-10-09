import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class Client {
    OutputStream outToServer;
    DataOutputStream out;
    InputStream inFromServer;
    DataInputStream in;
    Socket client;
    String filePath;

    public Client(String serverName,int port, String filename){
        try {
            filePath = new File(".").getCanonicalPath();
            System.out.println("Connecting to " + serverName + " on port " + port);
            client = new Socket(serverName, port);
            client.setSoTimeout(10000); //Timeout
            System.out.println("Connected to " + client.getRemoteSocketAddress());
            client.setSoTimeout(1000000); //Timeout
            outToServer = client.getOutputStream();
            out = new DataOutputStream(outToServer);
            inFromServer = client.getInputStream();

            //Request file by sending the filename
            System.out.println("Requesting file: " + filename);
            out.writeUTF(filename);

            //Read file from server
            byte[] byteBuffer = new byte[16384];
            long t1 = System.nanoTime();
            OutputStream outputStream = new FileOutputStream(filePath+"\\Files\\" + filename);
            //BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            int bytesRead;
            while ((bytesRead = inFromServer.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                outputStream.write(byteBuffer,0,bytesRead);
            }
            outputStream.close();
            long t2 = System.nanoTime();
            System.out.println("Time: "+((t2-t1)*0.000000001) +"s");
            //Check file if length is ok
            File receivedFile = new File(filePath+"\\Files\\" + filename);
            if(receivedFile.length() == 0){
                receivedFile.delete();
                System.out.print("File doesn't exist, or an error occurred receiving it!");
            }else{
                System.out.println("File successfully received!");
            }

            client.close();
        } catch(SocketTimeoutException e){
            System.out.println("Error: TIMEOUT");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}



import java.io.*;
import java.net.*;

public class Client {
    String filePath = "C:\\Users\\Dieter\\OneDrive\\Documenten\\Distrubuted Systems\\UDP Client\\Files\\";
    DatagramSocket client = null;

    public Client(String serverName,int port, String filename) {
        try {
            client = new DatagramSocket(3001);
            client.setSoTimeout(10000);
            System.out.println("Connection to " +serverName +" on port " +port + "...");
            InetAddress address = InetAddress.getByName(serverName);

            //Send file request by sending filename
            DatagramPacket request = new DatagramPacket(filename.getBytes(),filename.getBytes().length,address,port);
            client.send(request);
            System.out.println("Connected! Requesting file: "+filename);

            //Wait for answer and write data to file
            byte[] byteBuffer = new byte[client.getReceiveBufferSize()];
            DatagramPacket answer = new DatagramPacket(byteBuffer,byteBuffer.length);
            client.receive(answer);
            OutputStream outputStream = new FileOutputStream(filePath + filename);
            //do{
                outputStream.write(answer.getData(),answer.getOffset(),answer.getLength());
               // if(answer.getLength() > 512)
                  //  client.receive(answer);
                System.out.println(answer.getLength());
            //}while (answer.getLength() > 512);
            outputStream.close();

            //Check file if length is ok
            File receivedFile = new File(filePath + filename);
            if(receivedFile.length() == 0){
                receivedFile.delete();
                System.out.print("File doesn't exist, or an error occurred receiving it!");
            }else{
                System.out.println("File successfully received!");
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

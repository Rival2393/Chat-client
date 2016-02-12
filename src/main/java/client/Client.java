package client;

import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private final static int PORT = 5678;
    private static InetAddress address;

    private Socket socket;

    private BufferedReader reader;
    private PrintWriter writer;
    private MessageReceiver receiver;


    /**
     * Within infinite loop it's try to read messages from socket and set them to console
     */
    private class MessageReceiver extends Thread{
        @Override
        public void run() {
            while(true){
                try{
                    String message = reader.readLine();
                    System.out.println(message);
                } catch (IOException e){
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Constructor creates socket connection, input and output steams.
     * Also sets message receiver to listen socket for incoming messages
     * @param address
     * @throws IOException
     */
    public Client(InetAddress address) throws IOException {
        socket = new Socket(address, PORT);

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);

        receiver = new MessageReceiver();
    }

    protected void authorise(String nickname) throws InvalidValue, IOException {
        writer.println(nickname);
        String serverAnswer;
        serverAnswer = reader.readLine();
        if(serverAnswer.equals("OK")){
            receiver.start();
            return;
        }
        else if (serverAnswer.equals("BANNED"))
            throw new InvalidValue("You are banned in this chat");
    }

    public void sendMessage(String message) {
        if(message != "exit")
            writer.println(message);
        else{
            try {
                System.out.println("Goodbye");
                writer.close();
                reader.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            System.exit(0);
        }
    }
}

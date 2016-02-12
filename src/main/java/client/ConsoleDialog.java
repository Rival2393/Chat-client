package client;

import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ConsoleDialog {

    static Scanner scanner = new Scanner(System.in);
    static InetAddress address;
    static String message;
    static Client client;

    public static void main(String[] args) {
        System.out.println("Please enter IP of chat server that you need (format xxx.xxx.xxx.xxx)");
        while(true) {
            try {
                address = InetAddress.getByName(scanner.nextLine());
                client = new Client(address);
                break;
            } catch (UnknownHostException e) {
                System.out.println("Invalid input. Please retry");
                continue;
            } catch (IOException e) {
                System.err.println("Unable to connect, unexpected error. Exiting...");
                System.exit(-1);
            }
        }

        System.out.println("Welcome to chat!");
        System.out.println("Please authorise (registration is automatic)");
        while(true) {
            System.out.println("Enter your nickname");

            String nickname = scanner.nextLine();
            try {
                client.authorise(nickname);
                break;
            } catch (InvalidValue invalidValue) {
                System.out.println(invalidValue.getMessage());
                return;
            } catch (IOException e) {
                System.err.println("Unexpected error. Please retry to authorise");
                continue;
            }
        }
        System.out.println("You have successfully entered");
        System.out.println("You need to enter 'exit' to left this chat whenever you want");
        while (true) {
            message = scanner.nextLine();
            client.sendMessage(message);
        }
    }
}


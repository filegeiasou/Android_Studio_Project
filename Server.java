import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 41007;

    public static void main(String[] args) {
        System.out.println("Server started on port " + PORT);
        System.out.println("Waiting for client connection...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleClient(clientSocket);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());      
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String command;                                                 // Command received from the client
            String osName = System.getProperty("os.name");              // Get the OS name
            String hostName = InetAddress.getLocalHost().getHostName();     // Get the hostname of the PC

            while ((command = in.readLine()) != null) {
                System.out.println("Received command: " + command);
                switch (command) {
                    case "Echo":
                        out.println(hostName + " - " + osName); 
                        break;
                    case "Restart":
                        out.println(hostName + " - Rebooting...");
                        break;
                    case "Shutdown":
                        out.println(hostName + " - Shutting down...");
                        break;
                    case "Restore":
                            out.println(hostName + " - Restoring...");
                            Thread.sleep(10000); // simulate delay of 60 seconds
                            out.println(hostName + " - Restored");
                            break;
                    default:
                        out.println("Unknown command: " + command);
                        break;
                }
                out.println("END"); // END OF MESSAGE
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
}
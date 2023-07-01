
import java.io.*; // for buffer in and out stream as we are sharing on network its done in bytes
import java.net.*; //this file holds the socket class

//SERVER IS ACTING AS A MIDDLE MAN BETWEEN ALL THE CLIENTS

//Socket -> To connect two endpoints over a network

public class Server {
    private ServerSocket serverSocket; // attributes //a class ServerSocket is in .net

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    } // constructor

    public void startServer() {
        System.out.println("Server Has Initiated");
        try {
            while (!serverSocket.isClosed()) { // it will keep running and keep listening as thread until socket is
                                               // closed
                Socket socket = serverSocket.accept(); // Socket is class in .net used to capture reference of
                // server socket after the socket accepts a client we will create a object for
                // client
                System.out.println("A new Client Has CONNECTED");
                // to create object / socket for each client
                ClientHandler clienthandler = new ClientHandler(socket);

                Thread thread = new Thread(clienthandler);
                // to create a thread client handler so it can be kept running concurrently
                thread.start(); // start thread

            }
        } catch (IOException e) {
            // TODO: handle exception
        }
    }

    // Function to close the socket
    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException // (tells that it can throw a IOexception )
    { // mostly buffer in and out
        ServerSocket serverSocket = new ServerSocket(6969); // class in .net //binds the port to the systems IP
        // ANY PORT NUMBER CAN BE USED
        Server server = new Server(serverSocket); // our user defined class
        server.startServer(); // start server thread
    }
}

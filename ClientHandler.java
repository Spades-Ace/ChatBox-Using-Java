import java.io.*;
import java.net.*;
import java.util.*;

//SOLE PURPOSE OF THIS FILE IS TO CONNECT CLIENTS TO CLIENTS

//buffer read is receive  //read from clients
//buffer write is send    //write to clients //at a time one is writing others are reading

//stream means bytes 
//reader writer means character
//we are sending data over socket in character

public class ClientHandler implements Runnable // runnable is the thread class
{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); // to maintain list of clients in an
                                                                               // array list DYNAMICALLY
    private Socket socket;
    private BufferedReader bufferedReader; // receiving from clients
    private BufferedWriter bufferedWriter; // sending to clients
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Client username
            this.clientUsername = bufferedReader.readLine(); // read from the buffer flush()
            clientHandlers.add(this); // add to list
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
            // call broadcast function hence buffer write

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override // implements a function in runnable //hence a thread
    // as soon as client object is created this class make a thread for that client
    // so it always keep running
    // So we can listen from clients and send data to client HENCE buffer read and
    // write
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                // broadcaste the message to all
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    // a thread any msg sent by a client will be broadcast to all the clients
    private void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) // for ecah loop to iterate through all the clients
        {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) // so the client doesn't broadcast the message
                                                                          // to themselves
                {
                    clientHandler.bufferedWriter.write(messageToSend); // output to send to the clients that message
                    clientHandler.bufferedWriter.newLine(); // so that next time it will be printed in next line
                    clientHandler.bufferedWriter.flush(); // to write the buffered content immediately on console
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter); // DESTROYYY EVRYTHING
                break;
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this); // will remove the client which is disconnected
        broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
    }

    // function to close every single object also removes the client object
    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler(); // call the above function
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (bufferedWriter != null) {
                bufferedWriter.close();
            }

            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

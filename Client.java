import java.net.*;
import java.util.*;
import java.io.*;

public class Client 
{
    private Socket socket;
    private BufferedReader bufferedReader;  // receiving  from clients
    private BufferedWriter bufferedWriter;  //  sending to clients 
    private String username;
    
    
    public Client(Socket socket, String username) {
       try 
       {
        this.socket=socket;    
        this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                //get bytes type cast it to characters 👆👇🏻
            //then read it in characters
        this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // Client username
        this.username =username;
       }
       catch (IOException e) 
       {
        closeEverything(socket,bufferedReader,bufferedWriter); // to Destroy everything if a exception is thrown
       } 
    }

    public void sendMessage() //This function is running as a Thread
    {
        try 
        {
            
            bufferedWriter.write(username); //output to send to the clients that message
            bufferedWriter.newLine();       //change to new line
            bufferedWriter.flush();         //to write the buffered content immediately on console

            Scanner scanner = new Scanner(System.in);

            while(socket.isConnected())
            {
                String messageToSend = scanner.nextLine();  //to read string from console
                bufferedWriter.write(username+" : "+messageToSend); //output to send to the clients that message
                bufferedWriter.newLine(); //change to new line
                bufferedWriter.flush(); //to write the buffered content immediately on console
            }
        } 
        catch (IOException e) 
        {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

  
    public void listenForMessage() // A thread to keep listening for server
    {
        new Thread(new Runnable()   //anonymous class directly we are using object without creating a refrence
        {

            @Override
            public void run()  //an abstract function in runnable ///for thread implementation
            {
                while(socket.isConnected())
                {
                    try {
                        String messageFromGroupChat = bufferedReader.readLine(); //keep listening for msg
                                                                                // if any from server
                        System.out.println(messageFromGroupChat); //print on console
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
            
        }).start(); //start the threat start() is defined in runnable
    }

    //this function closes all the objects SO DESTROYYY

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) 
    {
       // removeClientHandler();
        try 
        {
            if(bufferedReader!= null)
            {
                    bufferedReader.close();
            }

            if(bufferedWriter!= null)
            {
                    bufferedWriter.close();
            }

            if(socket != null)
            {
                socket.close();
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    
    }


    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your Username for group chat ");
        String username = scanner.nextLine();

        //create a socket to connect to client
        //IP and port respectively in socket

        
       try {
            Socket socket = new Socket("127.0.0.1", 6969); // IP and port respectively in socket of server
            Client client = new Client(socket, username); // create object of client taking socket connection and
                                                          // username

            // Starting threads
            client.listenForMessage(); // thread 1 //A thread to listen for incoming message from server
            client.sendMessage(); // thread 2 // A thread to send message to Server so ity can be broadcast
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



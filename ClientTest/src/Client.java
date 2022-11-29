import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
    public static void main(String[] args) throws IOException {
        InetAddress localIP;
        if(args.length == 0){
            /* Non è stato passato alcun indirizzo da args: client e server stanno sulla stessa macchina. */
            localIP = InetAddress.getByName(null);
        }else{
            localIP = InetAddress.getByName(args[0]);
        }

        Socket socket = null;
        BufferedReader buffInput = null, stdIn = null;
        PrintWriter buffOutput = null;


        try{
            socket = new Socket(localIP, 8189);
            System.out.println("Client started");
            System.out.println("Client socket: " + socket);

            /* Creazione stream di input per la socket. */
            InputStreamReader inputStream = new InputStreamReader(socket.getInputStream());
            buffInput = new BufferedReader(inputStream);

            /* Creazione stream di output per la socket. */
            OutputStreamWriter outputStream = new OutputStreamWriter(socket.getOutputStream());
            BufferedWriter buffWriter = new BufferedWriter(outputStream);
            buffOutput = new PrintWriter(buffWriter, true);

            /* Creazione stream di input da tastiera. */
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;


            while(true){
                System.out.println("Write a string: ");
                userInput = stdIn.readLine();
                System.out.println("Stringa inserita: " + userInput);

                /* Scrittura su buffer di output della socket. */
                buffOutput.println(userInput);

                /* Se il client vuole chiudere la connessione (ho già inviato il messaggio di chiusura)
                * il server ha chiuso, adesso il client deve chiudere. */
                if(userInput.compareTo("CLOSE") == 0){
                    break;
                }

                /* Mostro la risposta del server. */
                System.out.println("Server reply: " + buffInput.readLine());
            }
        }catch (UnknownHostException e){
            System.err.println("Don’t know about host "+ localIP);
            System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn’t get I/O for the connection to: " + localIP);
                System.exit(1);
        }

        System.out.println("Client: closing...");
        buffOutput.close();
        buffInput.close();
        stdIn.close();
        socket.close();
    }
}

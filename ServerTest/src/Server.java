import java.io.*;
import java.net.*;

public class Server {
    public static final int port = 8189;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started");
        System.out.println("Socket: " + serverSocket);

        Socket clientSocket = null;
        BufferedReader input = null;
        PrintWriter output = null;

        try{
            /*
            * Finchè non avviene una connessione il server resta in attesa su questa riga di codice.
            * Quando un client tenterà di connettersi, allora vuol dire che il server avrà accettato qualcosa
            * e quindi ritorna la socket del client che ha appena accettato.
            * */
            clientSocket = serverSocket.accept();
            System.out.println("Connection accepted from: " + clientSocket);

            /* Creazione stream di input da clientSocket. */
            InputStreamReader inputStream = new InputStreamReader(clientSocket.getInputStream());
            input = new BufferedReader(inputStream);

            /* Creazione stream di output su clientSocket. */
            OutputStreamWriter outputStream = new OutputStreamWriter(clientSocket.getOutputStream());
            BufferedWriter bfWriter = new BufferedWriter(outputStream);
            output = new PrintWriter(bfWriter, true);

            /* Funzione effettiva del server: inviare ciò che ha ricevuto al client. */
            while(true){
                String msgRcv = input.readLine();
                System.out.println("Message received: " + msgRcv);
                if(msgRcv != null){
                    output.println(msgRcv);
                }
                if(msgRcv != null && msgRcv.compareTo("CLOSE") == 0){
                    break;
                }
            }

        }catch (IOException e){
            System.err.println("Accept failed");
            System.exit(1);
        }

        /* Chiusura stream e socket. */
        input.close();
        output.close();
        serverSocket.close();
        clientSocket.close();
    }
}

package nonblocking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TrivialClient {
  public static void main(String[] args) throws Throwable {
    Socket socket = new Socket("127.0.0.1", 8000);
    BufferedReader in = new BufferedReader(
        new InputStreamReader(socket.getInputStream()));
    PrintWriter out = new PrintWriter(
        new OutputStreamWriter(socket.getOutputStream()));
    out.println("Hello from blocking code....");
    out.flush();
    System.out.println(in.readLine());
  }
}

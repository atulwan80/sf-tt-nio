package nonblocking;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;

public class Echo {
  public static void main(String[] args) throws Throwable {
    Selector sel = Selector.open();
    ServerSocketChannel ssc = ServerSocketChannel.open();
    ssc.bind(new InetSocketAddress(8000));
    ssc.configureBlocking(false);
    ssc.register(sel, SelectionKey.OP_ACCEPT);

    ByteBuffer bb = ByteBuffer.allocate(4096);
    CharBuffer cb = CharBuffer.allocate(4096);
    Charset charset = Charset.forName("UTF-8");
    CharsetDecoder decoder = charset.newDecoder();
    CharsetEncoder encoder = charset.newEncoder();

    while (true) {
      sel.select(); // blocking until something can be done
      System.out.println("Something can be done...");
      Iterator<SelectionKey> keys = sel.selectedKeys().iterator();
      while (keys.hasNext()) {
        SelectionKey key = keys.next();
        int readyOps = key.readyOps();
        if ((readyOps & SelectionKey.OP_ACCEPT) != 0) {
          // ready to process an accept. (accept has happened)
          System.out.println("Accepted something");
          SocketChannel sc = ((ServerSocketChannel)(key.channel())).accept();
          sc.configureBlocking(false);
          sc.register(sel, SelectionKey.OP_READ);
        }
        if ((readyOps & SelectionKey.OP_READ) != 0) {
          SocketChannel sc = (SocketChannel)key.channel();
          sc.read(bb);
          bb.flip();
          decoder.decode(bb, cb, true);
          cb.flip();
          String message = cb.toString();
          cb.clear();
          System.out.println("Read Text: " + message);
          sc.register(sel, SelectionKey.OP_WRITE, message);
        }
        if ((readyOps & SelectionKey.OP_WRITE) != 0) {
          SocketChannel sc = (SocketChannel)key.channel();
          String message = (String)key.attachment();
          System.out.println("Reply: " + message);
          cb.put("Reply: " + message + "\n");
          cb.flip();
          encoder.encode(cb, bb, true);
          bb.flip();
          cb.clear();
          sc.write(bb);
          bb.clear();
          sc.close();
        }
        keys.remove();
      }
    }
  }
}

package charsets;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class ShowText {
  public static void main(String[] args) {
    try (
        FileChannel fcIn = new FileInputStream("Data.txt").getChannel();
        FileChannel fcOut = new FileOutputStream("Copy.txt").getChannel();
    ) {
      Charset cs = Charset.forName("UTF-8");
      CharsetEncoder enc = cs.newEncoder();
      CharsetDecoder dec = cs.newDecoder();
      ByteBuffer bb = ByteBuffer.allocate(1024);
      CharBuffer cb = CharBuffer.allocate(1024);
      fcIn.read(bb);
      bb.flip();
      dec.decode(bb, cb, true);
      cb.flip();
      System.out.println("Read: " + cb.toString());
      bb.clear();
      enc.encode(cb, bb, true);
      bb.flip();
      fcOut.write(bb);
    } catch (IOException ioe) {
      System.out.println("Problem " + ioe.getMessage());
    }
  }
}

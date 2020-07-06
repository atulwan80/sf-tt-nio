package copying;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class UseBuffers {
  public static void main(String[] args) {
    long start = System.nanoTime();
    try (
        FileChannel fcIn = new FileInputStream("data.dat").getChannel();
        FileChannel fcOut = new FileOutputStream("copy.da").getChannel();
    ) {
      ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
      long count = 0;

      while ((count = fcIn.read(buffer)) >= 0) {
        buffer.flip();
        fcOut.write(buffer);
        buffer.clear();
      }
      System.out.println("Done copying");
    } catch (IOException e) {
      System.out.println("IO problem " + e.getMessage());;
    }
    long time = System.nanoTime() - start;
    System.out.println("Copied in " + (time / 1_000_000_000.0) + " seconds");
  }
}

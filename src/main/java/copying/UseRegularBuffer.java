package copying;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class UseRegularBuffer {
  public static void main(String[] args) {
    long start = System.nanoTime();
    try (
        BufferedInputStream bi = new BufferedInputStream(
            new FileInputStream("data.dat"));
        BufferedOutputStream bo = new BufferedOutputStream(
            new FileOutputStream("copy.dat"));
    ) {
      int count = 0;
      byte [] buffer = new byte[1024 * 1024];
      while ((count = bi.read(buffer)) >= 0) {
        bo.write(buffer, 0, count);
      }
      System.out.println("Done copying");
    } catch (IOException e) {
      System.out.println("IO problem " + e.getMessage());;
    }
    long time = System.nanoTime() - start;
    System.out.println("Copied in " + (time / 1_000_000_000.0) + " seconds");
  }
}

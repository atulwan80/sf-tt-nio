package copying;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class CopyUsingFiles {
  public static void main(String[] args) throws Throwable {
    long start = System.nanoTime();
    Files.copy(Paths.get("data.dat"), Paths.get("output.dat"),
        StandardCopyOption.REPLACE_EXISTING);
    long time = System.nanoTime() - start;
    System.out.println("Time is " + (time / 1_000_000_000.0));
  }
}

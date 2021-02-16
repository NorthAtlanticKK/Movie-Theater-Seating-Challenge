import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class MovieTheaterClient {
    public static void main(String[] args) {
        try {
            // loops over all files in the testScripts folder
            // for each file, get each line and process the reservation request
            File testScripts = new File("testScripts");
            for (File testFile : Objects.requireNonNull(testScripts.listFiles())) {
                Scanner sc = new Scanner(testFile);
                System.out.println("Running " + testFile);

                MovieTheater m = new MovieTheater();
                while(sc.hasNext()) {
                    System.out.println(m.reserveSeats(sc.nextLine()));
                }
                sc.close();
                System.out.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

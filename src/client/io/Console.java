package client.io;

import java.io.PrintStream;
import java.util.Scanner;

public class Console {
    private final PrintStream out = System.out;
    private final Scanner scanner = new Scanner(System.in);

    public String read() {
        return scanner.nextLine().strip();
    }

    public void println(Object o) {
        out.println(o);
    }

    public void print(Object o) {
        out.print(o);
    }
}

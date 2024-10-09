package org.a1trdx.tinycipher.misc.util;

import java.io.Console;
import java.util.Scanner;

public class ConsoleUtils {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Console CONSOLE = System.console();

    public static void print(String format, Object... data) {
        System.out.printf(format, data);
    }

    public static void printLine(String format, Object... data) {
        print(format + "%n", data);
    }

    public static String readLine() {
        return SCANNER.nextLine();
    }

    public static String readPassword() {
        if (CONSOLE == null) {
            return readLine();
        } else {
            return new String(CONSOLE.readPassword());
        }
    }
}

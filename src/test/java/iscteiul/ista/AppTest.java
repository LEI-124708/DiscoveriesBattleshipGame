package iscteiul.ista;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.*;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test @DisplayName("Application accepts the exit command and prints its banner")
    void exitsOnCommand() {
        var previousIn = System.in;
        var previousOut = System.out;
        var output = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream("desisto\n".getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(output));
            App.main(new String[0]);
            assertTrue(output.toString().contains("Battleship Game"));
        } finally {
            System.setIn(previousIn);
            System.setOut(previousOut);
        }
    }
}

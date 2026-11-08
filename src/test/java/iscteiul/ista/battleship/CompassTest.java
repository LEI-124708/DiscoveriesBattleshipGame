package iscteiul.ista.battleship;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class CompassTest {

 @ParameterizedTest @CsvSource({"n,NORTH","s,SOUTH","e,EAST","o,WEST","x,UNKNOWN","N,UNKNOWN","u,UNKNOWN"})
 @DisplayName("Portuguese lower-case compass parsing and unknown fallback")
 void parsing(char input,Compass expected) { assertEquals(expected,Compass.charToCompass(input)); }
 @ParameterizedTest @EnumSource(Compass.class) @DisplayName("Compass text matches direction character")
 void text(Compass c) { assertEquals(String.valueOf(c.getDirection()),c.toString()); }

}

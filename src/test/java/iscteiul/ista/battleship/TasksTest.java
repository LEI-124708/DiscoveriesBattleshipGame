package iscteiul.ista.battleship;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class TasksTest {

 @Test @DisplayName("Position and ship parsing honour the command format")
 void parsing() {
  assertEquals(new Position(2,3),Tasks.readPosition(new Scanner("2 3")));
  assertInstanceOf(Caravel.class,Tasks.readShip(new Scanner("caravela 2 3 e")));
  assertNull(Tasks.readShip(new Scanner("unknown 0 0 n")));
  assertThrows(InputMismatchException.class,()->Tasks.readPosition(new Scanner("x 0")));
 }
 @Test @DisplayName("Firing round consumes exactly three coordinate pairs")
 void round() {
  Game g=new Game(new Fleet()); Scanner in=new Scanner("0 0 2 2 4 4 6 6");
  Tasks.firingRound(in,g); assertEquals(3,g.getShots().size()); assertEquals(6,in.nextInt());
 }
 @Test @DisplayName("Fleet builder reads eleven legal ships and retries invalid input")
 void fleet() {
  StringBuilder input=new StringBuilder("unknown 0 0 n barca -1 0 n ");
  for(int i=0;i<11;i++) input.append("barca ").append((i/5)*2).append(" ").append((i%5)*2).append(" n ");
  assertEquals(11,Tasks.buildFleet(new Scanner(input.toString())).getShips().size());
  assertThrows(AssertionError.class,()->Tasks.buildFleet(null));
 }
 @Test @DisplayName("Console tasks accept exit and unknown commands without a fleet")
 void console() {
  InputStream old=System.in;
  try {
   System.setIn(new ByteArrayInputStream(new byte[0])); Tasks.taskA();
   System.setIn(new ByteArrayInputStream("unknown estado desisto".getBytes())); Tasks.taskB();
   System.setIn(new ByteArrayInputStream("unknown estado mapa desisto".getBytes())); Tasks.taskC();
   System.setIn(new ByteArrayInputStream("unknown estado mapa rajada ver desisto".getBytes())); Tasks.taskD();
  } finally { System.setIn(old); }
 }


 @Test @DisplayName("Console tasks build fleets, show status and accept misses")
 void consoleWithFleet() {
  StringBuilder fleet=new StringBuilder("nova ");
  for(int i=0;i<11;i++) fleet.append("barca ").append((i/5)*2).append(" ").append((i%5)*2).append(" n ");
  InputStream old=System.in; PrintStream oldOut=System.out;
  try {
   System.setOut(new PrintStream(new ByteArrayOutputStream()));
   System.setIn(new ByteArrayInputStream((fleet+"estado desisto").getBytes())); Tasks.taskB();
   System.setIn(new ByteArrayInputStream((fleet+"estado mapa desisto").getBytes())); Tasks.taskC();
   System.setIn(new ByteArrayInputStream((fleet+"estado mapa ver desisto").getBytes())); Tasks.taskD();
   System.setIn(new ByteArrayInputStream("barca 0 0 n 0 0 1 1 2 2 unknown 0 0 n".getBytes())); Tasks.taskA();
  } finally { System.setIn(old); System.setOut(oldOut); }
 }
}

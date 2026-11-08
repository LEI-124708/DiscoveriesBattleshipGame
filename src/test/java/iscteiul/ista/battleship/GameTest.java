package iscteiul.ista.battleship;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

 @Test @DisplayName("Misses and repeated shots update separate counters")
 void misses() {
  Game g=new Game(new Fleet()); assertEquals(0,g.getRemainingShips());
  assertEquals(0,g.getInvalidShots()); assertEquals(0,g.getRepeatedShots());
  assertNull(g.fire(new Position(0,0))); assertNull(g.fire(new Position(0,0)));
  assertEquals(1,g.getRepeatedShots()); assertEquals(1,g.getShots().size());
 }
 @ParameterizedTest @CsvSource({"-1,0","0,-1","11,0","0,11"})
 @DisplayName("Invalid shots are counted without being recorded")
 void invalid(int r,int c) { Game g=new Game(new Fleet()); assertNull(g.fire(new Position(r,c))); assertEquals(1,g.getInvalidShots()); assertTrue(g.getShots().isEmpty()); }
 @Test @DisplayName("KNOWN DEFECT: uninitialised hit and sink counters throw NPE")
 void uninitialisedCounters() {
  Fleet f=new Fleet(); var s=new Barge(Compass.NORTH,new Position(0,0)); f.addShip(s);
  Game g=new Game(f); assertThrows(NullPointerException.class,g::getHits);
  assertThrows(NullPointerException.class,g::getSunkShips);
  assertThrows(NullPointerException.class,()->g.fire(new Position(0,0)));
  assertFalse(s.stillFloating()); assertEquals(1,g.getShots().size());
 }
 @Test @DisplayName("KNOWN DEFECT: coordinate ten is accepted but cannot be rendered")
 void upperBoundary() {
  Game g=new Game(new Fleet()); g.fire(new Position(10,10));
  assertEquals(0,g.getInvalidShots()); assertEquals(1,g.getShots().size());
  assertThrows(ArrayIndexOutOfBoundsException.class,g::printValidShots);
 }
 @Test @DisplayName("Board output places ships and valid shots on a ten by ten map")
 void output() {
  Fleet f=new Fleet(); f.addShip(new Barge(Compass.NORTH,new Position(0,0))); Game g=new Game(f);
  PrintStream old=System.out; var out=new ByteArrayOutputStream();
  try {
   System.setOut(new PrintStream(out)); g.printFleet();
   assertEquals(10,out.toString().lines().count()); assertTrue(out.toString().startsWith("#........."));
   out.reset(); g.fire(new Position(9,9)); g.printValidShots();
   assertEquals(10,out.toString().lines().count()); assertTrue(out.toString().contains(".........X"));
  } finally { System.setOut(old); }
 }

}

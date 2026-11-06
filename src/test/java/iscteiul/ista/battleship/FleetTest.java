package iscteiul.ista.battleship;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class FleetTest {

 @Test @DisplayName("Placement accepts board corners and rejects overlap and adjacency")
 void placement() {
  Fleet f=new Fleet(); Ship s=new Barge(Compass.NORTH,new Position(0,0));
  assertTrue(f.addShip(s)); assertFalse(f.addShip(s));
  assertFalse(f.addShip(new Barge(Compass.NORTH,new Position(1,1))));
  assertTrue(f.addShip(new Barge(Compass.NORTH,new Position(9,9))));
  assertSame(s,f.shipAt(new Position(0,0))); assertNull(f.shipAt(new Position(4,4)));
  assertEquals(2,f.getShipsLike("Barca").size()); assertTrue(f.getShipsLike("Nau").isEmpty());
  assertEquals(2,f.getFloatingShips().size()); s.shoot(new Position(0,0));
  assertEquals(1,f.getFloatingShips().size());
 }
 @ParameterizedTest @CsvSource({"-1,0","0,-1","10,0","0,10"})
 @DisplayName("Placement rejects every board boundary violation")
 void boundaries(int r,int c) { assertFalse(new Fleet().addShip(new Barge(Compass.NORTH,new Position(r,c)))); }
 @Test @DisplayName("Current capacity permits eleven ships and rejects the twelfth")
 void capacity() {
  Fleet f=new Fleet();
  for(int i=0;i<11;i++) assertTrue(f.addShip(new Barge(Compass.NORTH,new Position((i/5)*2,(i%5)*2))));
  assertFalse(f.addShip(new Barge(Compass.NORTH,new Position(8,8)))); assertEquals(11,f.getShips().size());
 }
 @Test @DisplayName("Status output includes fleet and category; null category is guarded")
 void output() {
  Fleet f=new Fleet(); f.addShip(new Barge(Compass.NORTH,new Position(0,0)));
  PrintStream old=System.out; var out=new ByteArrayOutputStream();
  try { System.setOut(new PrintStream(out)); f.printStatus(); assertTrue(out.toString().contains("Barca")); }
  finally { System.setOut(old); }
  assertThrows(AssertionError.class,()->f.printShipsByCategory(null));
 }

}

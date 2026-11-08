package iscteiul.ista.battleship;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

 @Test @DisplayName("Coordinates, state transitions and textual representation")
 void state() {
  Position p=new Position(2,3);
  assertEquals(2,p.getRow()); assertEquals(3,p.getColumn());
  assertFalse(p.isOccupied()); assertFalse(p.isHit());
  p.occupy(); p.occupy(); p.shoot(); p.shoot();
  assertTrue(p.isOccupied()); assertTrue(p.isHit());
  assertEquals("Linha = 2 Coluna = 3",p.toString());
 }
 @Test @DisplayName("Equality uses coordinates, including after a shot")
 void equality() {
  Position p=new Position(2,3), q=new Position(2,3);
  assertEquals(p,p); assertEquals(p,q); assertEquals(p.hashCode(),q.hashCode());
  q.shoot(); q.occupy(); assertEquals(p,q);
  assertNotEquals(p,new Position(1,3)); assertNotEquals(p,new Position(2,4));
  assertNotEquals(p,null); assertNotEquals(p,"2,3");
 }
 @ParameterizedTest @CsvSource({"0,0,true","1,1,true","-1,-1,true","2,0,false","0,2,false","-2,0,false","0,-2,false"})
 @DisplayName("Adjacency includes same cell and diagonals but excludes distant cells")
 void adjacency(int r,int c,boolean expected) {
  assertEquals(expected,new Position(4,4).isAdjacentTo(new Position(4+r,4+c)));
 }

}

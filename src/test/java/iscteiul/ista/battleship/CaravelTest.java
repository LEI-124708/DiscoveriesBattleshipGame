package iscteiul.ista.battleship;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class CaravelTest {

 @ParameterizedTest @EnumSource(value=Compass.class,names={"NORTH","SOUTH","EAST","WEST"})
 @DisplayName("Caravel occupies expected cells for every valid orientation")
 void geometry(Compass c) {
  Position anchor=new Position(4,4); Ship s=new Caravel(c,anchor);
  assertEquals(2,s.getSize()); assertEquals("Caravela",s.getCategory());
  assertSame(anchor,s.getPosition()); assertSame(c,s.getBearing());
  Set<String> actual=new HashSet<>();
  for(IPosition p:s.getPositions()) actual.add(p.getRow()+","+p.getColumn());
  Set<String> expected=new HashSet<>();
  for(int i=0;i<2;i++) expected.add((4+((c==Compass.NORTH||c==Compass.SOUTH)?i:0))+","+(4+((c==Compass.EAST||c==Compass.WEST)?i:0)));
  assertEquals(expected,actual); assertEquals(s.getSize(),s.getPositions().size());
 }

 @Test @DisplayName("Unknown bearing is rejected; assertions guard null bearing")
 void invalid() {
  assertThrows(IllegalArgumentException.class,()->new Caravel(Compass.UNKNOWN,new Position(0,0)));
  assertThrows(AssertionError.class,()->new Caravel(null,new Position(0,0)));
 }

}

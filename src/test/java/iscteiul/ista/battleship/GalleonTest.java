package iscteiul.ista.battleship;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class GalleonTest {

 @ParameterizedTest @EnumSource(value=Compass.class,names={"NORTH","SOUTH","EAST","WEST"})
 @DisplayName("Galleon occupies expected cells for every valid orientation")
 void geometry(Compass c) {
  Position anchor=new Position(4,4); Ship s=new Galleon(c,anchor);
  assertEquals(5,s.getSize()); assertEquals("Galeao",s.getCategory());
  assertSame(anchor,s.getPosition()); assertSame(c,s.getBearing());
  Set<String> actual=new HashSet<>();
  for(IPosition p:s.getPositions()) actual.add(p.getRow()+","+p.getColumn());
  Set<String> expected=new HashSet<>();

  int[][] offsets=switch(c) {
   case NORTH -> new int[][]{{0,0},{0,1},{0,2},{1,1},{2,1}};
   case SOUTH -> new int[][]{{0,0},{1,0},{2,-1},{2,0},{2,1}};
   case EAST -> new int[][]{{0,0},{1,-2},{1,-1},{1,0},{2,0}};
   case WEST -> new int[][]{{0,0},{1,0},{1,1},{1,2},{2,0}};
   default -> throw new AssertionError();
  };
  for(int[] offset:offsets) expected.add((4+offset[0])+","+(4+offset[1]));
  assertEquals(expected,actual); assertEquals(s.getSize(),s.getPositions().size());
 }

 @Test @DisplayName("Unknown bearing is rejected; assertions guard null bearing")
 void invalid() {
  assertThrows(IllegalArgumentException.class,()->new Galleon(Compass.UNKNOWN,new Position(0,0)));
  assertThrows(AssertionError.class,()->new Galleon(null,new Position(0,0)));
 }

}

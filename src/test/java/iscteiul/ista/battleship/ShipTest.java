package iscteiul.ista.battleship;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

 @ParameterizedTest @CsvSource({"barca,Barge","caravela,Caravel","nau,Carrack","fragata,Frigate","galeao,Galleon"})
 @DisplayName("Factory maps every supported category")
 void factory(String kind,String cls) { assertEquals(cls,Ship.buildShip(kind,Compass.NORTH,new Position(3,3)).getClass().getSimpleName()); }
 @Test @DisplayName("Factory rejects unknown category without creating a ship")
 void unknown() { assertNull(Ship.buildShip("unknown",Compass.NORTH,new Position(0,0))); }
 @Test @DisplayName("Shooting misses, hits, repeated shots and sinking")
 void combat() {
  Ship s=new Caravel(Compass.EAST,new Position(2,2));
  assertTrue(s.stillFloating()); assertTrue(s.occupies(new Position(2,2)));
  assertFalse(s.occupies(new Position(9,9)));
  s.shoot(new Position(9,9)); assertTrue(s.stillFloating());
  s.shoot(new Position(2,2)); s.shoot(new Position(2,2)); assertTrue(s.stillFloating());
  s.shoot(new Position(2,3)); assertFalse(s.stillFloating());
  assertEquals("[Caravela e Linha = 2 Coluna = 2]",s.toString());
 }
 @ParameterizedTest @EnumSource(value=Compass.class,names={"NORTH","SOUTH","EAST","WEST"})
 @DisplayName("Extrema cover every cell, including negative offsets in galleons")
 void extrema(Compass c) {
  Ship s=new Galleon(c,new Position(4,4));
  assertEquals(s.getPositions().stream().mapToInt(IPosition::getRow).min().orElseThrow(),s.getTopMostPos());
  assertEquals(s.getPositions().stream().mapToInt(IPosition::getRow).max().orElseThrow(),s.getBottomMostPos());
  assertEquals(s.getPositions().stream().mapToInt(IPosition::getColumn).min().orElseThrow(),s.getLeftMostPos());
  assertEquals(s.getPositions().stream().mapToInt(IPosition::getColumn).max().orElseThrow(),s.getRightMostPos());
 }
 @Test @DisplayName("Ship proximity includes overlap and diagonal adjacency")
 void proximity() {
  Ship s=new Barge(Compass.NORTH,new Position(4,4));
  assertTrue(s.tooCloseTo(new Position(4,4))); assertTrue(s.tooCloseTo(new Position(5,5)));
  assertFalse(s.tooCloseTo(new Position(7,7)));
  assertTrue(s.tooCloseTo(new Barge(Compass.NORTH,new Position(5,5))));
  assertFalse(s.tooCloseTo(new Barge(Compass.NORTH,new Position(7,7))));
 }
 @Test @DisplayName("Assertions guard required constructor and method arguments")
 void assertions() {
  assertThrows(AssertionError.class,()->new Barge(null,new Position(0,0)));
  assertThrows(AssertionError.class,()->new Barge(Compass.NORTH,null));
  Ship s=new Barge(Compass.NORTH,new Position(0,0));
  assertThrows(AssertionError.class,()->s.occupies(null));
  assertThrows(AssertionError.class,()->s.shoot(null));
  assertThrows(AssertionError.class,()->s.tooCloseTo((IShip)null));
 }

}

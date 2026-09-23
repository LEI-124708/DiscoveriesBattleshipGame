/**
 * Representa uma posição no tabuleiro do jogo Batalha Naval.
 * Cada posição é definida por uma linha e uma coluna e pode estar
 * ocupada por um navio e/ou ter sido atingida por um tiro.
 */
package iscteiul.ista.battleship;

import java.util.Objects;

public class Position implements IPosition {
    private int row;
    private int column;
    private boolean isOccupied;
    private boolean isHit;

    /**
     * Cria uma nova posição no tabuleiro.
     *
     * @param row linha da posição
     * @param column coluna da posição
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
        this.isOccupied = false;
        this.isHit = false;
    }

    /**
     * Devolve a linha da posição.
     *
     * @return linha da posição
     */
    @Override
    public int getRow() {
        return row;
    }

    /**
     * Devolve a coluna da posição.
     *
     * @return coluna da posição
     */
    @Override
    public int getColumn() {
        return column;
    }


    @Override
    public int hashCode() {
        return Objects.hash(column, isHit, isOccupied, row);
    }

    /**
     * Verifica se esta posição é igual a outra posição.
     * Duas posições são consideradas iguais quando têm a mesma linha e coluna.
     *
     * @param otherPosition objeto a comparar
     * @return true se as posições forem iguais, false caso contrário
     */
    @Override
    public boolean equals(Object otherPosition) {
        if (this == otherPosition)
            return true;
        if (otherPosition instanceof IPosition) {
            IPosition other = (IPosition) otherPosition;
            return (this.getRow() == other.getRow() && this.getColumn() == other.getColumn());
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IPosition#isAdjacentTo(battleship.IPosition)
     */
    @Override
    public boolean isAdjacentTo(IPosition other) {
        return (Math.abs(this.getRow() - other.getRow()) <= 1 && Math.abs(this.getColumn() - other.getColumn()) <= 1);
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IPosition#occupy()
     */
    @Override
    public void occupy() {
        isOccupied = true;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IPosition#shoot()
     */
    @Override
    public void shoot() {
        isHit = true;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IPosition#isOccupied()
     */
    @Override
    public boolean isOccupied() {
        return isOccupied;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IPosition#isHit()
     */
    @Override
    public boolean isHit() {
        return isHit;
    }

    @Override
    public String toString() {
        return ("Linha = " + row + " Coluna = " + column);
    }

}

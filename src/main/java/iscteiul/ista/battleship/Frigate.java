/**
 *
 */
package iscteiul.ista.battleship;

public class Frigate extends Ship {
    private static final Integer SIZE = 4;
    private static final String NAME = "Fragata";

    /**
     * Cria uma embarcação do tipo fragata com a orientação e posição inicial
     * especificadas. A fragata ocupa SIZE posições consecutivas no tabuleiro,
     * na vertical quando orientada a NORTH ou SOUTH, ou na horizontal quando
     * orientada a EAST ou WEST.
     *
     * @param bearing orientação da fragata no tabuleiro
     * @param pos posição inicial da fragata no tabuleiro
     * @throws IllegalArgumentException se a orientação especificada não for
     *                                  válida para uma fragata
     */
    public Frigate(Compass bearing, IPosition pos) throws IllegalArgumentException {
        super(Frigate.NAME, bearing, pos);
        switch (bearing) {
            case NORTH:
            case SOUTH:
                for (int r = 0; r < SIZE; r++)
                    getPositions().add(new Position(pos.getRow() + r, pos.getColumn()));
                break;
            case EAST:
            case WEST:
                for (int c = 0; c < SIZE; c++)
                    getPositions().add(new Position(pos.getRow(), pos.getColumn() + c));
                break;
            default:
                throw new IllegalArgumentException("ERROR! invalid bearing for thr frigate");
        }
    }

    /**
     * Obtém o tamanho da carraca.
     *
     * @return número de posições ocupadas pela carraca no tabuleiro
     */
    @Override
    public Integer getSize() {
        return Frigate.SIZE;
    }

}

/**
 *
 */
package iscteiul.ista.battleship;

public class Carrack extends Ship {
    private static final Integer SIZE = 3;
    private static final String NAME = "Nau";

    /**
     * Cria uma embarcação do tipo carraca com a orientação e posição inicial
     * especificadas. A carraca ocupa {@code SIZE} posições consecutivas no
     * tabuleiro, na vertical quando orientada a {@code NORTH} ou {@code SOUTH},
     * ou na horizontal quando orientada a {@code EAST} ou {@code WEST}.
     *
     * @param bearing orientação da carraca no tabuleiro
     * @param pos posição inicial da carraca no tabuleiro
     * @throws IllegalArgumentException se a orientação especificada não for
     *                                  válida para uma carraca
     */
    public Carrack(Compass bearing, IPosition pos) throws IllegalArgumentException {
        super(Carrack.NAME, bearing, pos);
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
                throw new IllegalArgumentException("ERROR! invalid bearing for the carrack");
        }
    }

    /**
     * Obtém o tamanho da carraca.
     *
     * @return número de posições ocupadas pela carraca no tabuleiro
     */
    @Override
    public Integer getSize() {
        return Carrack.SIZE;
    }

}

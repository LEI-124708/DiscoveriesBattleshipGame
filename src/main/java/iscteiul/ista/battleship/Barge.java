package iscteiul.ista.battleship;
/**
 * Representa uma barca da frota.
 *
 * <p>A barca é o navio mais pequeno do jogo, ocupando apenas uma posição
 * do tabuleiro. A posição ocupada corresponde à posição recebida no
 * construtor.</p>
 *
 * @see Ship
 * @see IShip
 */
public class Barge extends Ship {
    /**
     * Número de posições ocupadas por uma barca.
     */
    private static final Integer SIZE = 1;
    /**
     * Categoria utilizada para identificar a barca.
     */
    private static final String NAME = "Barca";

    /**
     * Cria uma barca na posição indicada.
     *
     * <p>Como a barca ocupa apenas uma posição, a sua orientação não altera
     * as posições ocupadas, mas é mantida para respeitar o modelo comum
     * dos restantes navios.</p>
     *
     * @param bearing orientação atribuída à barca
     * @param pos posição ocupada pela barca no tabuleiro
     */
    public Barge(Compass bearing, IPosition pos) {
        super(Barge.NAME, bearing, pos);
        getPositions().add(new Position(pos.getRow(), pos.getColumn()));
    }

    /**
     * Devolve o número de posições ocupadas pela barca.
     *
     * @return tamanho da barca, sempre igual a {@code 1}
     */
    @Override
    public Integer getSize() {
        return SIZE;
    }

}

/**
 * Representa a implementação base de um navio do jogo.
 *
 * <p>Um navio possui uma categoria, uma orientação, uma posição inicial
 * e uma lista com todas as posições ocupadas no tabuleiro.</p>
 *
 * <p>As subclasses determinam o tamanho e preenchem a lista de posições
 * ocupadas pelo respetivo tipo de navio.</p>
 *
 * @see IShip
 * @see Barge
 * @see Caravel
 * @see Carrack
 * @see Frigate
 * @see Galleon
 */
package iscteiul.ista.battleship;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Ship implements IShip {

    private static final String GALEAO = "galeao";
    private static final String FRAGATA = "fragata";
    private static final String NAU = "nau";
    private static final String CARAVELA = "caravela";
    private static final String BARCA = "barca";

    /**
     * Cria um navio do tipo indicado.
     *
     * @param shipKind categoria do navio; deve corresponder a
     *                 {@code barca}, {@code caravela}, {@code nau},
     *                 {@code fragata} ou {@code galeao}
     * @param bearing orientação do navio no tabuleiro
     * @param pos posição inicial do navio
     * @return o navio criado ou {@code null} quando a categoria não é reconhecida
     */
static Ship buildShip(String shipKind, Compass bearing, Position pos) {
    static Ship buildShip(String shipKind, Compass bearing, Position pos) {
        Ship s;
        switch (shipKind) {
            case BARCA:
                s = new Barge(bearing, pos);
                break;
            case CARAVELA:
                s = new Caravel(bearing, pos);
                break;
            case NAU:
                s = new Carrack(bearing, pos);
                break;
            case FRAGATA:
                s = new Frigate(bearing, pos);
                break;
            case GALEAO:
                s = new Galleon(bearing, pos);
                break;
            default:
                s = null;
        }
        return s;
    }


    private String category;
    private Compass bearing;
    private IPosition pos;
    protected List<IPosition> positions;


    /**
     * Inicializa os atributos comuns a todos os navios.
     *
     * @param category categoria do navio
     * @param bearing orientação do navio no tabuleiro
     * @param pos posição inicial do navio
     */ 
    public Ship(String category, Compass bearing, IPosition pos) {
        assert bearing != null;
        assert pos != null;

        this.category = category;
        this.bearing = bearing;
        this.pos = pos;
        positions = new ArrayList<>();
    }

    /**
     * Devolve a categoria do navio.
     *
     * @return categoria do navio
     */
    @Override
    public String getCategory() {
        return category;
    }

    /**
     * Devolve as posições ocupadas pelo navio no tabuleiro.
     *
     * @return lista de posições ocupadas pelo navio
     */
    public List<IPosition> getPositions() {
        return positions;
    }

    /**
     * Devolve a posição inicial do navio.
     *
     * @return posição inicial do navio
     */
    @Override
    public IPosition getPosition() {
        return pos;
    }

    /**
     * Devolve a orientação do navio.
     *
     * @return orientação do navio no tabuleiro
     */
    @Override
    public Compass getBearing() {
        return bearing;
    }

    /**
     * Verifica se o navio ainda possui pelo menos uma posição não atingida.
     *
     * @return {@code true} se o navio ainda estiver a flutuar;
     *         {@code false} se todas as suas posições tiverem sido atingidas
     */
    @Override
    public boolean stillFloating() {
        for (int i = 0; i < getSize(); i++)
            if (!getPositions().get(i).isHit())
                return true;
        return false;
    }

    /**
     * Obtém a linha mais acima ocupada pelo navio.
     *
     * @return índice da linha superior ocupada
     */
    @Override
    public int getTopMostPos() {
        int top = getPositions().get(0).getRow();
        for (int i = 1; i < getSize(); i++)
            if (getPositions().get(i).getRow() < top)
                top = getPositions().get(i).getRow();
        return top;
    }

    /**
     * Obtém a linha mais abaixo ocupada pelo navio.
     *
     * @return índice da linha inferior ocupada
     */
    @Override
    public int getBottomMostPos() {
        int bottom = getPositions().get(0).getRow();
        for (int i = 1; i < getSize(); i++)
            if (getPositions().get(i).getRow() > bottom)
                bottom = getPositions().get(i).getRow();
        return bottom;
    }

    /**
     * Obtém a coluna mais à esquerda ocupada pelo navio.
     *
     * @return índice da coluna mais à esquerda
     */
    @Override
    public int getLeftMostPos() {
        int left = getPositions().get(0).getColumn();
        for (int i = 1; i < getSize(); i++)
            if (getPositions().get(i).getColumn() < left)
                left = getPositions().get(i).getColumn();
        return left;
    }

    /**
     * Obtém a coluna mais à direita ocupada pelo navio.
     *
     * @return índice da coluna mais à direita
     */
    @Override
    public int getRightMostPos() {
        int right = getPositions().get(0).getColumn();
        for (int i = 1; i < getSize(); i++)
            if (getPositions().get(i).getColumn() > right)
                right = getPositions().get(i).getColumn();
        return right;
    }

    /**
     * Verifica se o navio ocupa uma determinada posição.
     *
     * @param pos posição que se pretende verificar
     * @return {@code true} se a posição for ocupada pelo navio;
     *         {@code false} caso contrário
     */
    @Override
    public boolean occupies(IPosition pos) {
        assert pos != null;

        for (int i = 0; i < getSize(); i++)
            if (getPositions().get(i).equals(pos))
                return true;
        return false;
    }

    /**
     * Verifica se este navio está demasiado próximo de outro navio.
     *
     * <p>Dois navios estão demasiado próximos quando alguma das posições
     * ocupadas por um deles é adjacente a uma posição ocupada pelo outro.</p>
     *
     * @param other outro navio
     * @return {@code true} se os navios estiverem demasiado próximos;
     *         {@code false} caso contrário
     */
    @Override
    public boolean tooCloseTo(IShip other) {
        assert other != null;

        Iterator<IPosition> otherPos = other.getPositions().iterator();
        while (otherPos.hasNext())
            if (tooCloseTo(otherPos.next()))
                return true;

        return false;
    }

    /**
     * Verifica se uma posição está adjacente a alguma posição deste navio.
     *
     * @param pos posição que se pretende verificar
     * @return {@code true} se a posição estiver demasiado próxima do navio;
     *         {@code false} caso contrário
     */
    @Override
    public boolean tooCloseTo(IPosition pos) {
        for (int i = 0; i < this.getSize(); i++)
            if (getPositions().get(i).isAdjacentTo(pos))
                return true;
        return false;
    }


    /**
     * Regista um tiro numa posição ocupada pelo navio.
     *
     * <p>Se a posição indicada pertencer ao navio, essa posição é marcada
     * como atingida. Se não pertencer, o estado do navio não é alterado.</p>
     *
     * @param pos posição atingida pelo tiro
     */
    @Override
    public void shoot(IPosition pos) {
        assert pos != null;

        for (IPosition position : getPositions()) {
            if (position.equals(pos))
                position.shoot();
        }
    }


    /**
     * Produz uma representação textual do navio.
     *
     * @return texto com a categoria, orientação e posição inicial do navio
     */
    @Override
    public String toString() {
        return "[" + category + " " + bearing + " " + pos + "]";
    }

}

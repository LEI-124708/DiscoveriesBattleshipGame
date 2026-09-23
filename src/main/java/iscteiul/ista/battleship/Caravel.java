package iscteiul.ista.battleship;

/**
 * Represents a Caravel ("Caravela") ship in the Battleship game.
 * <p>
 * A Caravel occupies {@value #SIZE} consecutive grid positions. Its cells
 * are generated starting from an anchor position ({@code pos}) and extending
 * according to the given {@link Compass} bearing:
 * </p>
 * <ul>
 *   <li>{@code NORTH} or {@code SOUTH} &rarr; the ship extends along
 *       increasing rows, keeping the column fixed;</li>
 *   <li>{@code EAST} or {@code WEST} &rarr; the ship extends along
 *       increasing columns, keeping the row fixed.</li>
 * </ul>
 * <p>
 * Note that, as implemented, {@code NORTH}/{@code SOUTH} produce identical
 * placements (row increases from {@code pos}), and {@code EAST}/{@code WEST}
 * likewise produce identical placements (column increases from
 * {@code pos}); the bearing therefore selects the axis of extension rather
 * than a signed direction along it.
 * </p>
 *
 * @see Ship
 * @see Compass
 */
public class Caravel extends Ship {

    /** Number of grid positions occupied by a Caravel. */
    private static final Integer SIZE = 2;

    /** Display name used for this ship type. */
    private static final String NAME = "Caravela";

    /**
     * Creates a new Caravel anchored at {@code pos} and extending in the
     * axis indicated by {@code bearing} (see class documentation for how
     * each bearing maps to row/column extension).
     *
     * @param bearing the {@link Compass} direction the Caravel heads to;
     *                only {@code NORTH}, {@code SOUTH}, {@code EAST} and
     *                {@code WEST} are supported
     * @param pos     the anchor position from which the Caravel's cells
     *                are generated
     * @throws NullPointerException     if {@code bearing} is {@code null}
     * @throws IllegalArgumentException if {@code bearing} is
     *                                  {@link Compass#UNKNOWN}, the only
     *                                  {@code Compass} value not handled by
     *                                  this constructor
     */
    public Caravel(Compass bearing, IPosition pos) throws NullPointerException, IllegalArgumentException {
        super(Caravel.NAME, bearing, pos);

        if (bearing == null)
            throw new NullPointerException("ERROR! invalid bearing for the caravel");

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
                throw new IllegalArgumentException("ERROR! invalid bearing for the caravel");
        }

    }

    /**
     * Returns the number of grid positions occupied by a Caravel.
     *
     * @return the fixed size of a Caravel, i.e. {@value #SIZE}
     */
    @Override
    public Integer getSize() {
        return SIZE;
    }

}

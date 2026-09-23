/**
 *
 */
package iscteiul.ista.battleship;

/**
 * Represents a galleon ("Galeão"), the largest ship of the Discoveries
 * Battleship Game. A galleon occupies five cells and, unlike the other ships
 * (which are straight lines), has a "T" shape whose orientation depends on its
 * {@link Compass bearing}.
 * <p>
 * The fleet contains exactly one galleon.
 * <p>
 * The shape of the galleon for each bearing is shown below, where {@code O} is
 * the reference position ({@code pos}) received by the constructor (row
 * {@code r}, column {@code c}) and {@code X} are the remaining cells:
 *
 * <pre>
 *  NORTH        SOUTH        EAST         WEST
 *  (c..c+2)     (c-1..c+1)   (c-2..c)     (c..c+2)
 *
 *  O X X        . O .        . . O        O . .
 *  . X .        . X .        X X X        X X X
 *  . X .        X X X        . . X        X . .
 * </pre>
 *
 * Every shape spans three rows ({@code r} to {@code r + 2}) and three columns.
 * Note that the reference position is not always the top-left corner: for
 * {@link Compass#SOUTH} the ship extends one column to the left of {@code pos},
 * and for {@link Compass#EAST} it extends two columns to the left. This class
 * does not check whether the cells are inside the board; that validation is
 * performed when the ship is added to the fleet.
 *
 * @see Ship
 * @see Compass
 * @see IPosition
 */
public class Galleon extends Ship {

    /** Number of cells occupied by a galleon. */
    private static final Integer SIZE = 5;

    /** Name of this kind of ship (in the Discoveries version of the game). */
    private static final String NAME = "Galeao";

    /**
     * Builds a galleon with the given bearing, filling the five cells it
     * occupies according to the shape associated with that bearing (see the
     * class description).
     *
     * @param bearing the bearing the galleon heads to; must be one of
     *                {@link Compass#NORTH}, {@link Compass#EAST},
     *                {@link Compass#SOUTH} or {@link Compass#WEST}
     * @param pos     the reference position used to place the galleon on the
     *                board (row and column of the cell marked {@code O} in the
     *                class description)
     * @throws NullPointerException     if {@code bearing} is {@code null}
     * @throws IllegalArgumentException if {@code bearing} is not one of the
     *                                  four cardinal directions (e.g.
     *                                  {@link Compass#UNKNOWN})
     */
    public Galleon(Compass bearing, IPosition pos) throws IllegalArgumentException {
        super(Galleon.NAME, bearing, pos);

        if (bearing == null)
            throw new NullPointerException("ERROR! invalid bearing for the galleon");

        switch (bearing) {
            case NORTH:
                fillNorth(pos);
                break;
            case EAST:
                fillEast(pos);
                break;
            case SOUTH:
                fillSouth(pos);
                break;
            case WEST:
                fillWest(pos);
                break;

            default:
                throw new IllegalArgumentException("ERROR! invalid bearing for the galleon");
        }
    }

    /**
     * Returns the number of cells occupied by the galleon.
     *
     * @return the size of the galleon, which is always 5
     * @see Ship#getSize()
     */
    @Override
    public Integer getSize() {
        return Galleon.SIZE;
    }

    /**
     * Fills the positions of a galleon heading {@link Compass#NORTH}: a
     * horizontal bar of three cells starting at {@code pos} (towards the
     * right), with a two-cell stem going down from the middle of the bar.
     *
     * @param pos the reference position (top-left cell of the bar)
     */
    private void fillNorth(IPosition pos) {
        for (int i = 0; i < 3; i++) {
            getPositions().add(new Position(pos.getRow(), pos.getColumn() + i));
        }
        getPositions().add(new Position(pos.getRow() + 1, pos.getColumn() + 1));
        getPositions().add(new Position(pos.getRow() + 2, pos.getColumn() + 1));
    }

    /**
     * Fills the positions of a galleon heading {@link Compass#SOUTH}: a
     * two-cell vertical stem starting at {@code pos} (going down), ending in a
     * horizontal bar of three cells on the third row, centered on the stem's
     * column.
     *
     * @param pos the reference position (top cell of the stem)
     */
    private void fillSouth(IPosition pos) {
        for (int i = 0; i < 2; i++) {
            getPositions().add(new Position(pos.getRow() + i, pos.getColumn()));
        }
        for (int j = 2; j < 5; j++) {
            getPositions().add(new Position(pos.getRow() + 2, pos.getColumn() + j - 3));
        }
    }

    /**
     * Fills the positions of a galleon heading {@link Compass#EAST}: a
     * vertical bar of three cells in the column of {@code pos} (going down),
     * with a two-cell stem going left from the middle of the bar.
     *
     * @param pos the reference position (top cell of the bar)
     */
    private void fillEast(IPosition pos) {
        getPositions().add(new Position(pos.getRow(), pos.getColumn()));
        for (int i = 1; i < 4; i++) {
            getPositions().add(new Position(pos.getRow() + 1, pos.getColumn() + i - 3));
        }
        getPositions().add(new Position(pos.getRow() + 2, pos.getColumn()));
    }

    /**
     * Fills the positions of a galleon heading {@link Compass#WEST}: a
     * vertical bar of three cells in the column of {@code pos} (going down),
     * with a two-cell stem going right from the middle of the bar.
     *
     * @param pos the reference position (top cell of the bar)
     */
    private void fillWest(IPosition pos) {
        getPositions().add(new Position(pos.getRow(), pos.getColumn()));
        for (int i = 1; i < 4; i++) {
            getPositions().add(new Position(pos.getRow() + 1, pos.getColumn() + i - 1));
        }
        getPositions().add(new Position(pos.getRow() + 2, pos.getColumn()));
    }

}

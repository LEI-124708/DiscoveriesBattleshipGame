/**
 *
 */
package iscteiul.ista.battleship;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player's fleet of ships in the Battleship game.
 * <p>
 * A {@code Fleet} keeps track of the {@link IShip} instances placed on a
 * single {@value IFleet#BOARD_SIZE}&times;{@value IFleet#BOARD_SIZE} board
 * and enforces the game's placement rules (ships must stay inside the
 * board and must not be too close to one another) when ships are added.
 * </p>
 *
 * @see IFleet
 * @see IShip
 */
public class Fleet implements IFleet {

    /**
     * Prints every ship in {@code ships} to standard output, one per line,
     * using each ship's {@link Object#toString()} representation.
     *
     * @param ships the list of ships to print
     */
    static void printShips(List<IShip> ships) {
        for (IShip ship : ships)
            System.out.println(ship);
    }

    // -----------------------------------------------------

    /** The ships currently placed in this fleet. */
    private List<IShip> ships;

    /**
     * Creates an empty fleet, with no ships placed yet.
     */
    public Fleet() {
        ships = new ArrayList<>();
    }

    /**
     * Returns the list of ships currently in this fleet.
     *
     * @return the fleet's ships; may be empty, never {@code null}
     */
    @Override
    public List<IShip> getShips() {
        return ships;
    }

    /**
     * Attempts to add a ship to this fleet.
     * <p>
     * The ship is only added if all of the following hold:
     * </p>
     * <ul>
     *   <li>the fleet has not yet reached its maximum ship count &mdash;
     *       note that, due to the {@code <=} comparison against
     *       {@link IFleet#FLEET_SIZE}, up to {@code FLEET_SIZE + 1} ships
     *       are actually allowed (i.e. 11 ships in total, matching the
     *       game's fleet composition: 1 Galeao, 1 Fragata, 2 Nau,
     *       3 Caravela, 4 Barca);</li>
     *   <li>the ship fits entirely inside the board
     *       ({@link #isInsideBoard(IShip)});</li>
     *   <li>the ship is not too close to any ship already in the fleet
     *       ({@link #colisionRisk(IShip)}).</li>
     * </ul>
     *
     * @param s the ship to add
     * @return {@code true} if the ship was added, {@code false} if any of
     *         the conditions above were not met
     */
    @Override
    public boolean addShip(IShip s) {
        boolean result = false;
        if ((ships.size() <= FLEET_SIZE) && (isInsideBoard(s)) && (!colisionRisk(s))) {
            ships.add(s);
            result = true;
        }
        return result;
    }

    /**
     * Returns all ships in this fleet belonging to the given category.
     *
     * @param category the ship category to match (e.g. {@code "Caravela"})
     * @return the ships in this fleet whose category equals {@code category};
     *         may be empty, never {@code null}
     */
    @Override
    public List<IShip> getShipsLike(String category) {
        List<IShip> shipsLike = new ArrayList<>();
        for (IShip s : ships)
            if (s.getCategory().equals(category))
                shipsLike.add(s);

        return shipsLike;
    }

    /**
     * Returns all ships in this fleet that are still floating, i.e. that
     * have not yet been completely hit.
     *
     * @return the ships in this fleet for which
     *         {@link IShip#stillFloating()} is {@code true}; may be empty,
     *         never {@code null}
     */
    @Override
    public List<IShip> getFloatingShips() {
        List<IShip> floatingShips = new ArrayList<>();
        for (IShip s : ships)
            if (s.stillFloating())
                floatingShips.add(s);

        return floatingShips;
    }

    /**
     * Returns the ship in this fleet that occupies the given position, if
     * any.
     *
     * @param pos the position to check
     * @return the ship occupying {@code pos}, or {@code null} if no ship in
     *         this fleet occupies that position
     */
    @Override
    public IShip shipAt(IPosition pos) {
        for (int i = 0; i < ships.size(); i++)
            if (ships.get(i).occupies(pos))
                return ships.get(i);
        return null;
    }

    /**
     * Checks whether a ship's cells all lie within the board boundaries
     * (0 to {@value IFleet#BOARD_SIZE} &minus; 1, inclusive, in both row
     * and column).
     *
     * @param s the ship to check
     * @return {@code true} if every cell of {@code s} is inside the board
     */
    private boolean isInsideBoard(IShip s) {
        return (s.getLeftMostPos() >= 0 && s.getRightMostPos() <= BOARD_SIZE - 1 && s.getTopMostPos() >= 0
                && s.getBottomMostPos() <= BOARD_SIZE - 1);
    }

    /**
     * Checks whether placing ship {@code s} would put it too close to any
     * ship already present in this fleet (including diagonal adjacency).
     *
     * @param s the ship to check
     * @return {@code true} if {@code s} is too close to at least one ship
     *         already in the fleet
     */
    private boolean colisionRisk(IShip s) {
        for (int i = 0; i < ships.size(); i++) {
            if (ships.get(i).tooCloseTo(s))
                return true;
        }
        return false;
    }


    /**
     * Prints the full status of this fleet to standard output: all ships,
     * then only the floating ones, then the ships grouped by each of the
     * five known categories (Galeao, Fragata, Nau, Caravela, Barca).
     */
    public void printStatus() {
        printAllShips();
        printFloatingShips();
        printShipsByCategory("Galeao");
        printShipsByCategory("Fragata");
        printShipsByCategory("Nau");
        printShipsByCategory("Caravela");
        printShipsByCategory("Barca");
    }

    /**
     * Prints all the ships of this fleet belonging to a particular
     * category.
     *
     * @param category the category of ships of interest; must not be
     *                  {@code null}
     */
    public void printShipsByCategory(String category) {
        assert category != null;

        printShips(getShipsLike(category));
    }

    /**
     * Prints all the ships of this fleet that are still floating (i.e.
     * not yet fully hit).
     */
    public void printFloatingShips() {
        printShips(getFloatingShips());
    }

    /**
     * Prints all the ships currently in this fleet, regardless of state.
     */
    void printAllShips() {
        printShips(ships);
    }

}


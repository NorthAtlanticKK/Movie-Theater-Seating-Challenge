import java.util.HashMap;
import java.util.Map;

/**
 * A MovieTheater is a mutable representation of a movie theater and its seat reservations.
 * A MovieTheater contains 10 rows x 20 cols = 200 seats total. Each seat can be reserved
 * or not. A buffer of three seats and/or one row is required between seat reservations.
 * Users can reserve multiple seats in each reservation, and seats within each reservation
 * should be next to each other.
 */

public class MovieTheater {
    /**
     * Number of rows in the movie theater
     */
    public static final int ROWS = 10;

    /**
     * Number of seats for each row in the movie theater
     */
    public static final int SEATS_PER_ROW = 20;

    /**
     * Minimum distance between seats of different reservations
     */
    public static final int SOCIAL_DISTANCE_SEATS = 3;

    /**
     * Distance from a numeric value to its char counter part, from 0-A to 10-J
     */
    public static final int ROW_INT_TO_CHAR_OFFSET = 65;

    /**
     * Record of occupation or unavailable seats in the theater
     */
    private boolean[][] occupied;

    /**
     * Maps from reservation number to the reservation itself
     */
    private Map<String, Reservation> idToReservation;

    // Abstraction Function:
    //   A MovieTheater m represents a 2D array of integer values where the value of each
    //   coordinate in the 2D array represents the reservation number that reserved that
    //   seat, and 0 represents the seat is not yet reserved.

    // Representation invariant for every MovieTheater m:
    //     * reserved != null and is a 2D array of size ROW x SEATS_PER_ROW
    //     * reserved[i][j] is of format R#### where # is a digit between 0-9

    /**
     * @spec.effects Constructs a new MovieTheater.
     */
    public MovieTheater() {
        occupied = new boolean[ROWS][SEATS_PER_ROW];
        idToReservation = new HashMap<>();
    }

    /**
     * Reserves seats according to the given requests, returns the seats reserved for each reservation
     *
     * @param requestString a requests stored in a string, where "R#### 2" means the
     *                      reservation id R#### is requesting 2 seats
     * @return a reservation stored in a string, where "R#### I1, I2" means the reservation
     *         number R#### is granted seats I1 and I2
     *         if the reservation ID is already used or if the requested seats is at most 0, return
     *         a message explanation
     * @spec.requires request is in correct format R#### n, and each # is a digit between 0 to 9
     */
    public String reserveSeats(String requestString) {
        StringBuilder sb = new StringBuilder();
        String[] request = requestString.split(" ");
        String resID = request[0];
        if (idToReservation.containsKey(resID)) {
            return resID + " has already been reserved previous.";
        }
        int numSeats = Integer.parseInt(request[1]);
        if (numSeats <= 0) {
            return resID + " requested 0 or less seats, when it should at least request 1.";
        }
        int[] seatLoc = getEmptySeat(numSeats);
        int row = seatLoc[0];
        int col = seatLoc[1];
        if (row != -1) {
            Reservation reservation = new Reservation(resID, numSeats, row, col);
            idToReservation.put(resID, reservation);
            setUnavailable(row, col, numSeats);
            sb.append(resID).append(" ");
            for (int j = col; j < col + numSeats; j++) {
                sb.append((char) (row + ROW_INT_TO_CHAR_OFFSET)).append(j);
                if (j < col + numSeats - 1) {
                    sb.append(", ");
                }
            }
        } else {
            return resID + " failed to reserve seats. Requested seats over theater capacity.";
        }
        return sb.toString();
    }

    // returns row and col of the leftmost seat assigned, if available
    // returns -1 if no available cluster of seats can be assigned
    private int[] getEmptySeat(int numSeats) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j <= SEATS_PER_ROW - numSeats; j++) {
                boolean clear = true;
                for (int k = j; k < j + numSeats; k++) {
                    if (occupied[i][k]) {
                        clear = false;
                        break;
                    }
                }
                if (clear) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1};
    }

    // sets the reserved seats and its neighboring seats to unavailable
    private void setUnavailable(int row, int col, int numSeats) {
        for (int j = Math.max(0, col - SOCIAL_DISTANCE_SEATS);
             j < Math.min(col + numSeats + SOCIAL_DISTANCE_SEATS, SEATS_PER_ROW); j++) {
            occupied[row][j] = true;
        }
    }

    /**
     * A Reservation is a mutable representation of a reservation in a Movie Theater.
     * A Reservation is assigned an ID, number of seats, and the row and column of
     * its leftmost seat in the Movie Theater.
     */
    private class Reservation {
        String reservationID; // ID of this reservation
        int numSeats; // number of seats reserved together
        int row; // which row the reservation is in
        int column; // the position of the leftmost seat

        /**
         * @spec.effects Constructs a new Reservation.
         */
        public Reservation(String reservationID, int numSeats, int row, int column) {
            this.reservationID = reservationID;
            this.numSeats = numSeats;
            this.row = Character.toUpperCase(row);
            this.column = column;
        }
    }
}

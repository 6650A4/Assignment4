package Model;


/**
 * The type Lift ride.
 */
public class LiftRide {
    private Integer time;

    private Integer liftID;

    /**
     * Instantiates a new Lift ride.
     *
     * @param time the time
     * @param liftID the lift id
     */
    public LiftRide(Integer time, Integer liftID) {
        this.time = time;
        this.liftID = liftID;
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public Integer getTime() {
        return time;
    }

    /**
     * Gets lift id.
     *
     * @return the lift id
     */
    public Integer getLiftID() {
        return liftID;
    }

    @Override
    public String toString() {
        return "LiftRide{" +
                "time=" + time +
                ", liftID=" + liftID +
                '}';
    }
}

package Model;

/**
 * The type Resort.
 */
public class Resort {
    private String resortName;
    private int resortID;

    /**
     * Instantiates a new Resort.
     *
     * @param resortName the resort name
     * @param resortID the resort id
     */
    public Resort(String resortName, int resortID) {
        this.resortName = resortName;
        this.resortID = resortID;
    }

    /**
     * Gets resort name.
     *
     * @return the resort name
     */
    public String getResortName() {
        return resortName;
    }

    /**
     * Gets resort id.
     *
     * @return the resort id
     */
    public int getResortID() {
        return resortID;
    }


}

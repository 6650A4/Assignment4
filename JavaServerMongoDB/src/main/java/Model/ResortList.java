package Model;

import java.util.Arrays;
import java.util.List;

/**
 * The type Resort list.
 */
public class ResortList {
    private List<Resort> resorts = null;


    /**
     * Instantiates a new Resort list.
     */
    public ResortList() {
    }

    /**
     * Create example resort list.
     *
     * @return the resort list
     */
    static public ResortList createExample() {
        ResortList resorts = new ResortList();
        resorts.resorts = Arrays.asList(new Resort("the Louvre", 0), new Resort("Etihad Towers", 1));
        return resorts;
    }
}

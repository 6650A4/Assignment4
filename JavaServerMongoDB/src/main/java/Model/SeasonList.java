package Model;

import java.util.Arrays;
import java.util.List;

/**
 * The type Season list.
 */
public class SeasonList {
    private List<String> seasons;

    /**
     * Instantiates a new Season list.
     */
    public SeasonList() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    static public SeasonList getInstance() {
        SeasonList seasonList = new SeasonList();
        seasonList.seasons = Arrays.asList("2019", "2020");
        return seasonList;

    }


}

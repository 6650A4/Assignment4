package Model;

import java.util.Arrays;
import java.util.List;

/**
 * The type Statis list.
 */
public class StatisList {

  /**
   * The Endpoint stats.
   */
  public List<StatisBody> endpointStats;

  /**
   * Gets endpoint stats.
   *
   * @return the endpoint stats
   */
  public List<StatisBody> getEndpointStats() {
    return endpointStats;
  }

  /**
   * Instantiates a new Statis list.
   *
   * @param endpointStats the endpoint stats
   */
  public StatisList(List<StatisBody> endpointStats) {
    this.endpointStats = endpointStats;
  }

  /**
   * Get instance statis list.
   *
   * @return the statis list
   */
  public static StatisList getInstance(){
    StatisBody skierGet = new StatisBody("/skiers", "GET",0.0,0);
    StatisBody skierPost = new StatisBody("/skiers", "POST",0.0,0);
    StatisBody resortGet = new StatisBody("/resorts", "GET",0.0,0);
    StatisBody resortPost = new StatisBody("/resorts", "POST",0.0,0);
    return new StatisList(Arrays.asList(skierGet,skierPost,resortGet,resortPost));

  }

}

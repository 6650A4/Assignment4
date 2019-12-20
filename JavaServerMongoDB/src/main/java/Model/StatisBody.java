package Model;

/**
 * The type Statis body.
 */
public class StatisBody {
  private String URL;
  private String operation;
  private Double mean;
  private Integer max;

  /**
   * Instantiates a new Statis body.
   *
   * @param URL the url
   * @param operation the operation
   * @param mean the mean
   * @param max the max
   */
  public StatisBody(String URL, String operation, Double mean, Integer max) {
    this.URL = URL;
    this.operation = operation;
    this.mean = mean;
    this.max = max;
  }

  /**
   * Sets url.
   *
   * @param URL the url
   */
  public void setURL(String URL) {
    this.URL = URL;
  }

  /**
   * Sets operation.
   *
   * @param operation the operation
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Sets mean.
   *
   * @param mean the mean
   */
  public void setMean(Double mean) {
    this.mean = mean;
  }

  /**
   * Sets max.
   *
   * @param max the max
   */
  public void setMax(Integer max) {
    this.max = max;
  }
}

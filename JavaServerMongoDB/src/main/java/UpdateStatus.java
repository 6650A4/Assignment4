import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * The helper function class to update statistics status
 */
public class UpdateStatus {

  /**
   * Update status.
   *
   * @param prevTime the prev time
   * @param id the id
   */
  public static void updateStatus(long prevTime, String id) {
    MongoCollection<Document> statis = DbSingleton.getStatisCollection();
    long passed = System.currentTimeMillis() - prevTime;
    statis.updateOne(new Document("_id", id),
        new Document("$inc", new Document("sum", passed).append("total", 1)));
    statis.updateOne(new Document("_id", id), new Document("$max", new Document("max", passed)));

  }
}

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import java.util.Arrays;
import org.bson.Document;

/**
 * The type Db singleton, which contains all singletons needed to communicate with the databse
 */
public class DbSingleton {

  /**
   * The Client.
   */
  static MongoClient client;
  /**
   * The Db.
   */
  static MongoDatabase db;
  /**
   * The Skier collection.
   */
  static MongoCollection<Document> skierCollection;
  /**
   * The Resort collection.
   */
  static MongoCollection<Document> resortCollection;
  /**
   * The Resort sub collection.
   */
  static MongoCollection<Document> resortSubCollection;
  /**
   * The Resort season collection.
   */
  static MongoCollection<Document> resortSeasonCollection;
  /**
   * The Statis collection.
   */
  static MongoCollection<Document> statisCollection;
  /**
   * The Skiervertical collection.
   */
  static MongoCollection<Document> skierverticalCollection;
  /**
   * The Skiervertical lift detail collection.
   */
  static MongoCollection<Document> skierverticalLiftDetailCollection;
  /**
   * The Upsert.
   */
  static UpdateOptions upsert;


  /**
   * Gets resort season collection.
   *
   * @return the resort season collection
   */
  public static MongoCollection<Document> getResortSeasonCollection() {
    if (resortSeasonCollection == null) {
      resortSeasonCollection = getDb().getCollection("resortSeasonCollection");
    }
    return resortSeasonCollection;
  }

  /**
   * Gets client.
   *
   * @return the client
   */
  static MongoClient getClient() {
    if (client == null) {
      client = MongoClients.create("mongodb://54.185.10.35:27017");
    }
    return client;
  }

  /**
   * Gets db.
   *
   * @return the db
   */
  public static MongoDatabase getDb() {
    if (db == null) {
      db = getClient().getDatabase("hw2-10");
    }
    return db;
  }

  /**
   * Gets skier collection.
   *
   * @return the skier collection
   */
  public static MongoCollection<Document> getSkierCollection() {
    if (skierCollection == null) {
      skierCollection = getDb().getCollection("skier");
    }
    return skierCollection;
  }

  /**
   * Gets resort sub collection.
   *
   * @return the resort sub collection
   */
  public static MongoCollection<Document> getResortSubCollection() {
    if (resortSubCollection == null) {
      getResortCollection();
      resortSubCollection = getDb().getCollection("resortSubCollection");
    }
    return resortSubCollection;
  }

  /**
   * Gets skiervertical collection.
   *
   * @return the skiervertical collection
   */
  public static MongoCollection<Document> getSkierverticalCollection() {
    if (skierverticalCollection == null) {
      skierverticalCollection = getDb().getCollection("skierverticalCollection");
    }
    return skierverticalCollection;
  }

  /**
   * Gets skiervertical lift detail collection.
   *
   * @return the skiervertical lift detail collection
   */
  public static MongoCollection<Document> getSkierverticalLiftDetailCollection() {
    if (skierverticalLiftDetailCollection == null) {
      skierverticalLiftDetailCollection = getDb()
          .getCollection("skierverticalLiftDetailCollection");
    }
    return skierverticalLiftDetailCollection;
  }

  /**
   * Init resort.
   */
  public static void initResort() {
    if (resortCollection.countDocuments() == 0) {
      resortCollection.insertOne(new Document("_id", "0").append("resorts", Arrays
          .asList(new Document("_id", "0").append("resortName", "the Louvre").append("resortID", 0),
              new Document("_id", "1").append("resortName", "Etihad Towers")
                  .append("resortID", 1))));
      getResortSubCollection();
      resortSubCollection
          .insertMany(Arrays.asList(new Document("_id", "0"), new Document("_id", "1")));
    }
  }

  /**
   * Init static.
   */
  public static void initStatic() {
    if (statisCollection.countDocuments() == 0) {
      statisCollection.insertMany(
          Arrays.asList(
              new Document("_id", "0").append("sum", 0L).append("max", 0L).append("total", 0),
              new Document("_id", "1").append("sum", 0L).append("max", 0L).append("total", 0),
              new Document("_id", "2").append("sum", 0L).append("max", 0L).append("total", 0),
              new Document("_id", "3").append("sum", 0L).append("max", 0L).append("total", 0)));
    }
  }

  /**
   * Gets resort collection.
   *
   * @return the resort collection
   */
  public static MongoCollection<Document> getResortCollection() {
    if (resortCollection == null) {
      resortCollection = getDb().getCollection("resort");
      initResort();
    }
    return resortCollection;
  }

  /**
   * Gets statis collection.
   *
   * @return the statis collection
   */
  public static MongoCollection<Document> getStatisCollection() {
    if (statisCollection == null) {
      statisCollection = getDb().getCollection("statis");
      initStatic();
    }
    return statisCollection;
  }

  /**
   * Gets upsert.
   *
   * @return the upsert
   */
  public static UpdateOptions getUpsert() {
    if (upsert == null) {
      upsert = new UpdateOptions().upsert(true);
    }
    return upsert;
  }
}

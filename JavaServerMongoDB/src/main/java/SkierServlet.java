import Model.LiftRide;
import Model.Message.InvalidInput;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;

/**
 * The type Skier servlet.
 */
@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {

  private static final int VERTICAL = -200;
  private static final int SKIER = -300;


  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    long curTime = System.currentTimeMillis();
    String urlPath = req.getPathInfo();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String jsonString = "";
    int response = 0;
    // Verify the url
    if (urlPath == null || urlPath.isEmpty()) {
      response = 400;
    } else {
      String[] urlParts = urlPath.split("/");
      response = valPostUrl(urlPath);
      if (response == SKIER) {
        String json = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        // Verify request format
        LiftRide convertedObject = new Gson().fromJson(json, LiftRide.class);
        if (convertedObject.getLiftID() == null || convertedObject.getTime() == null) {
          response = 400;
        } else {
          // Url and request format is OK. Verify the resort ID and season
          String resortSeasonKey = urlParts[1] + "f" + urlParts[3];
          MongoCollection<Document> resortSeasonCollection = DbSingleton
              .getResortSeasonCollection();
          // Resort or season not found in the database
          if (resortSeasonCollection.find(new Document("_id", resortSeasonKey)).first() == null) {
            response = 404;
          } else {
            response = 201;
            String skierKey = resortSeasonKey + "f" + urlParts[5] + "f" + urlParts[7];
            MongoCollection<Document> skierVertical = DbSingleton.getSkierverticalCollection();
            MongoCollection<Document> skierDetail = DbSingleton
                .getSkierverticalLiftDetailCollection();
            skierDetail.updateOne(new Document("_id", skierKey), new Document("$push",
                new Document("liftRide", new Document("time", convertedObject.getTime())
                    .append("liftID", convertedObject.getLiftID()))), DbSingleton.getUpsert());
            skierVertical.updateOne(new Document("_id", skierKey),
                new Document("$inc", new Document("vertical", convertedObject.getLiftID() * 10)),
                DbSingleton.getUpsert());
          }
        }
      }
      if (response == 400) {
        jsonString = gson.toJson(new InvalidInput());
      }
    }

    res.setStatus(response);
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    out.print(jsonString);
    out.flush();
    UpdateStatus.updateStatus(curTime, "1");

  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    long curTime = System.currentTimeMillis();

    String urlPath = req.getPathInfo();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    String jsonString = null;
    int response = 0;
    if (urlPath == null || urlPath.isEmpty()) {
      response = 400;
    } else {
      String[] urlParts = urlPath.split("/");
      // Verify the url
      response = valGetUrl(urlParts);
      if (response == SKIER) {
        response = 200;
        String resortSeasonKey = urlParts[1] + "f" + urlParts[3];
        MongoCollection<Document> resortSeasonCollection = DbSingleton.getResortSeasonCollection();
        if (resortSeasonCollection.find(new Document("_id", resortSeasonKey)).first() == null) {
          response = 404;
        } else {
          String skierKey = resortSeasonKey + "f" + urlParts[5] + "f" + urlParts[7];
          MongoCollection<Document> skierVertical = DbSingleton.getSkierverticalCollection();
          Object curValue = skierVertical.find(new Document("_id", skierKey)).first()
              .get("vertical");
          Integer value = 0;
          if (curValue != null) {
            value = (Integer) curValue;
          }
          jsonString = value.toString();
        }
      } else if (response == VERTICAL) {
        response = 200;
        jsonString = "{}";
      } else if (response == 400) {
        jsonString = gson.toJson(new InvalidInput());
      }
    }
    res.setStatus(response);
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    out.print(jsonString);
    out.flush();
    UpdateStatus.updateStatus(curTime, "0");


  }

  /**
   * Validate the get request path and return corresponding response code.
   *
   * @param urlParts the url path
   * @return the integer
   */
  protected Integer valGetUrl(String[] urlParts) {
    if (urlParts.length == 3) {
      return valVerticalUrl(urlParts);
    } else if (urlParts.length == 8) {
      return valSkierUrl(urlParts);
    }
    return 400;
  }


  /**
   * Validate the vertical url path and return corresponding response code.
   *
   * @param urlParts the url parts
   * @return the integer
   */
  protected Integer valVerticalUrl(String[] urlParts) {
    if (!urlParts[1].equals("vertical")) {
      return 400;
    }
    return VERTICAL;
  }

  /**
   * Validate the skier url path and return corresponding response code.
   *
   * @param urlParts the url parts
   * @return the integer
   */
  protected Integer valSkierUrl(String[] urlParts) {
    if (!urlParts[2].equals("seasons") || !urlParts[4].equals("days") || !urlParts[6]
        .equals("skiers")) {
      return 400;
    }
    return SKIER;
  }

  /**
   * Validate the post request path and return corresponding response code.
   *
   * @param urlPath the url path
   * @return the integer
   */
  protected Integer valPostUrl(String urlPath) {
    String[] urlParts = urlPath.split("/");
    if (urlParts.length == 8) {
      return valSkierUrl(urlParts);
    }
    return 400;
  }


}

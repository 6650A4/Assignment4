import Model.Body;
import Model.Message.InvalidInput;
import Model.Message.ResortNotFound;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
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
 * The type Resort servlet.
 */
@WebServlet(name = "ResortServlet")
public class ResortServlet extends HttpServlet {

  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    {
      long curTime = System.currentTimeMillis();

      res.setContentType("text/plain");
      String urlPath = req.getPathInfo();

      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      String jsonString = "Write Successful!";

      // check we have a URL!
      if (urlPath == null || urlPath.isEmpty()) {
        res.setStatus(400);
        jsonString = gson.toJson(new InvalidInput());
      } else {
        String[] urlParts = urlPath.split("/");
        int response = valUrl(urlParts);
        if (response == 200) {
          MongoCollection<Document> resortSeason = DbSingleton.getResortSubCollection();
          if (resortSeason.find(new Document("_id", urlParts[1])).first() == null) {
            response = 404;
          } else {
            String json = req.getReader().lines()
                .collect(Collectors.joining(System.lineSeparator()));
            // Verify request
            String field = valSeason(json);
            if (field == null) {
              response = 400;
            } else {
              MongoCollection<Document> resortSub = DbSingleton.getResortSubCollection();
              Document pushItem = new Document("seasons", field);
              UpdateOptions options = new UpdateOptions().upsert(true);
              resortSub.updateOne(new Document("_id", urlParts[1]), new Document("$push", pushItem),
                  options);
              Document query = new Document("_id", urlParts[1] + "f" + field);
              MongoCollection compact = DbSingleton.getResortSeasonCollection();
              compact.updateOne(query, new Document("$setOnInsert", query), options);
              jsonString = resortSub.find(new Document("_id", urlParts[1]))
                  .projection(new Document("_id", 0)).first().toJson();
              response = 201;
            }
          }
        }
        if (response == 400) {
          jsonString = gson.toJson(new InvalidInput());
        } else if (response == 404) {
          jsonString = gson.toJson(new ResortNotFound());
        }
        res.setStatus(response);
      }
      PrintWriter out = res.getWriter();
      res.setContentType("application/json");
      res.setCharacterEncoding("UTF-8");
      out.print(jsonString);
      out.flush();

      UpdateStatus.updateStatus(curTime, "3");
    }
  }


  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    long curTime = System.currentTimeMillis();

    String urlPath = req.getPathInfo();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    MongoCollection<Document> resort = DbSingleton.getResortCollection();
    String jsonString = null;

    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(200);
      jsonString = resort.find().projection(new Document("_id", 0).append("resorts._id", 0)).first()
          .toJson();
    } else {
      String[] urlParts = urlPath.split("/");

      int response = valUrl(urlParts);
      if (response == 200) {
        MongoCollection<Document> resortSeason = DbSingleton.getResortSubCollection();

        if (resortSeason.find(new Document("_id", urlParts[1])).first() == null) {
          response = 404;
        } else {
          jsonString = resortSeason.find(new Document("_id", urlParts[1]))
              .projection(new Document("_id", 0)).first().toJson();
        }
      }
      if (response == 400) {
        jsonString = gson.toJson(new InvalidInput());
      } else if (response == 404) {
        jsonString = gson.toJson(new ResortNotFound());
      }
      res.setStatus(response);

    }

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    out.print(jsonString);
    out.flush();

    UpdateStatus.updateStatus(curTime, "2");
  }

  /**
   * Validate the url and return the response code.
   *
   * @param urlParts the url path
   * @return the integer
   */
  protected Integer valUrl(String[] urlParts) {
    for (int i = 0; i < urlParts.length; i++) {
      System.out.println(urlParts[i]);
    }
    if (urlParts.length != 3 || !urlParts[2].equals("seasons")) {
      return 400;
    } else {
      return 200;
    }

  }

  /**
   * Validate the json body for season post request..
   *
   * @param json the json
   * @return the boolean
   */
  protected String valSeason(String json) {
    Body convertedObject = new Gson().fromJson(json, Body.class);
    return convertedObject.getYear();
  }
}

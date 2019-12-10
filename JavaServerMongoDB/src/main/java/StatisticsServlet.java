import Model.StatisList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;

/**
 * The type Statistics servlet.
 */
@WebServlet(name = "StatisticsServlet")
public class StatisticsServlet extends HttpServlet {

  /**
   * Behavior is not defined in the swagger api, hence we do nothing.
   */
  protected void doPost(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {

  }

  protected void doGet(HttpServletRequest req,
      HttpServletResponse res)
      throws ServletException, IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    MongoCollection<Document> statis = DbSingleton.getStatisCollection();
    StatisList statisList = StatisList.getInstance();
    MongoCursor<Document> iter = statis.find().cursor();
    // All four kinds of statistics are saved in four documents, hence we iterate all of them.
    for (int i = 0; i < 4; i++) {
      Document cur = iter.next();
      statisList.getEndpointStats().get(i).setMax(cur.getLong("max").intValue());
      Integer total = cur.getInteger("total");
      // To avoid division by zero
      if (total == 0) {
        total++;
      }
      // In the document we store the sum of all latencies and total number of runs.
      statisList.getEndpointStats().get(i)
          .setMean(cur.getLong("sum").doubleValue() / total.doubleValue());
    }

    String jsonString = gson.toJson(statisList);
    int response = 200;
    res.setStatus(response);
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    out.print(jsonString);
    out.flush();

  }
}

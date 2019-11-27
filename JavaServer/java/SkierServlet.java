import DAL.LiftRidesDao;
import com.google.gson.Gson;
import io.swagger.client.model.LiftRide;
import io.swagger.client.model.SkierVertical;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {
    protected LiftRidesDao liftRidesDao;

    public void init() throws ServletException {
        liftRidesDao = LiftRidesDao.getInstance();
    }
    protected void doPost(
        HttpServletRequest request, HttpServletResponse res) throws ServletException, IOException {
        String recordPath = (String)getServletContext().getAttribute("skierPostStatPath");
        long startTime = System.currentTimeMillis();

        BufferedReader reader = request.getReader();
        String jsonString = "";
        try {
            for (String line; (line = reader.readLine()) != null; jsonString += line);
        } catch(IOException e) {

        }

        LiftRide liftRide = new LiftRide();

        try {
            liftRide = parseBody(jsonString);
        } catch (InvalidParameterException e) {

            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"message\": \"Invalid inputs\"}");
            return;
        }

        res.setContentType("application/json");
        String urlPath = request.getPathInfo();
        if (!isUrlValid(urlPath)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("{\"message\": \"not found\"}");
        } else {
            String[] urlParts = urlPath.split("/");
            int resortId = Integer.valueOf(urlParts[1]);
            int seasonId = Integer.valueOf(urlParts[3]);
            int dayId = Integer.valueOf(urlParts[5]);
            int skierId = Integer.valueOf(urlParts[7]);

            try {
                liftRidesDao.insertLiftRide(resortId, seasonId, dayId, skierId, liftRide);
                res.setStatus(HttpServletResponse.SC_CREATED);
                res.getWriter().write("{\"message\": \"ok\"}");
            } catch (SQLException e) {

            }
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        String urlPath = req.getPathInfo();
        String queryString = req.getQueryString();

        if (isUrlValid(urlPath + (queryString != null? "?" + queryString : ""))) {
            res.setStatus(HttpServletResponse.SC_OK);
            String[] urlParts = urlPath.split("/");
            if (urlParts.length == 3) {
                String[] queryParams = queryString.split("&");
                int skierId = Integer.valueOf(urlParts[1]);
                int resortId = Integer.valueOf(queryParams[0].split("=")[1]);
                int seasonId = -1;
                if (queryParams.length > 1) {
                    seasonId = Integer.valueOf(queryParams[1].split("=")[1]);
                }
                SkierVertical skierVertical = null;
                try {
                    skierVertical = liftRidesDao.getTotalVertical(skierId, resortId, seasonId);
                } catch (SQLException e) {

                }

                res.getWriter().write(new Gson().toJson(skierVertical));
            } else {
                int resortId = Integer.valueOf(urlParts[1]);
                int seasonId = Integer.valueOf(urlParts[3]);
                int dayId = Integer.valueOf(urlParts[5]);
                int skierId = Integer.valueOf(urlParts[7]);
                try {
                    int vertical = liftRidesDao.getVerticalBySkier(resortId, seasonId, dayId, skierId);
                    res.getWriter().write(String.valueOf(vertical));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("{\"message\": \"not found\"}");
        }
    }


    private boolean isUrlValid(String urlPath) {
        Pattern skierApiPattern = Pattern.compile("^/([\\d]+)/seasons/([\\d]+)/days/([\\d]+)/skiers/([\\d]+)$");
        Pattern verticalApiPattern = Pattern.compile("^/[\\d]+/vertical\\?resort=[\\d]+(&season=[\\d]+)?$");

        Matcher skierApiMatches = skierApiPattern.matcher(urlPath);
        Matcher verticalApiMatches = verticalApiPattern.matcher(urlPath);
        boolean skierApiFind = skierApiMatches.find();
        boolean verticalApiFind = verticalApiMatches.find();
        if (!skierApiFind && !verticalApiFind) {
            return false;
        }
        if (skierApiFind) {
            int day = Integer.parseInt(skierApiMatches.group(3));
            if (day > 366 || day < 1) {
                return false;
            }
        }
        return true;
    }

    private LiftRide parseBody(String jsonStr) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, LiftRide.class);
        } catch(Exception e){
            throw new InvalidParameterException("Invalid inputs");
        }
    }
}

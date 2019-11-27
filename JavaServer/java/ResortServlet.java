import DAL.ResortsDao;
import Model.Season;
import com.google.gson.Gson;
import io.swagger.client.model.ResortsList;
import io.swagger.client.model.SeasonsList;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "ResortServlet")
public class ResortServlet extends HttpServlet {
    protected ResortsDao resortsDao;


    public void init() throws ServletException {
        resortsDao = ResortsDao.getInstance();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String recordPath = (String)getServletContext().getAttribute("resortPostStatPath");
        long startTime = System.currentTimeMillis();
        BufferedReader reader = req.getReader();
        String jsonString = "";
        try {
            for (String line; (line = reader.readLine()) != null; jsonString += line);
        } catch (IOException e) {

        }

        int season = -1;

        try {
            season = parseBody(jsonString);
        } catch (InvalidParameterException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"message\": \"Invalid inputs\"}");
            return;
        }

        res.setContentType("application/json");
        String urlPath = req.getPathInfo();
        if (!isUrlValid(urlPath)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("{\"message\": \"not found\"}");
        } else {
            String[] urlParts = urlPath.split("/");
            int resortId = Integer.valueOf(urlParts[1]);
            try {
                if (resortsDao.getResortByResortId(resortId) == null) {
                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    res.getWriter().write(String.format("{\"message\": \"resort ID %d not found\"}",
                        resortId));
                } else {
                resortsDao.createSeason(season, resortId);
                res.setStatus(HttpServletResponse.SC_OK);
                res.getWriter().write("{\"message\": \"ok\"}");
                }
            } catch (SQLException e) {

            }
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String recordPath = (String)getServletContext().getAttribute("resortGetStatPath");
        long startTime = System.currentTimeMillis();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        String urlPath = req.getPathInfo();

        if (urlPath == null || urlPath.isEmpty() || urlPath.equals("/")) { // resorts
            res.setStatus(HttpServletResponse.SC_OK);
            ResortsList resortsList = null;
            try {
                resortsList = resortsDao.getAllResorts();
            } catch (SQLException e) {

            }
            res.getWriter().write(new Gson().toJson(resortsList));
        } else if (isUrlValid(urlPath)) { // seasons
            String[] urlParts = urlPath.split("/");
            int resortId = Integer.valueOf(urlParts[1]);
            try {
                if (resortsDao.getResortByResortId(resortId) == null) {
                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    res.getWriter().write(String.format("{\"message\": \"resort ID %d not found\"}",
                        resortId));
                } else {
                    res.setStatus(HttpServletResponse.SC_CREATED);
                    SeasonsList seasonsList = resortsDao.getSeasonsByResortId(resortId);
                    res.getWriter().write(new Gson().toJson(seasonsList));
                }
            } catch (SQLException e) {

            }
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("{\"message\": \"not found\"}");
        }
    }

    private boolean isUrlValid(String urlPath) {
        // urlPath  = "/1/seasons"
        // urlPath = "/"
        Pattern resortApiPattern = Pattern.compile("^/([\\d]+)/seasons$");
        Pattern seasonApiPattern = Pattern.compile("^/?$");
        return (resortApiPattern.matcher(urlPath).matches() || seasonApiPattern.matcher(urlPath).matches());
    }

    private int parseBody(String jsonStr) {
        try {
            Gson gson = new Gson();
            Season season = gson.fromJson(jsonStr, Season.class);
            if (season == null || String.valueOf(season.getYear()).length() != 4)
                throw new InvalidParameterException("Invalid inputs");
            return season.getYear();
        } catch(Exception e){
            throw new InvalidParameterException("Invalid inputs");
        }
    }
}
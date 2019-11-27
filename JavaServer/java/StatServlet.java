import DAL.StatsDao;
import Model.Stat;
import com.google.gson.Gson;
import io.swagger.client.model.APIStats;
import io.swagger.client.model.APIStatsEndpointStats;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "StatServlet")
public class StatServlet extends HttpServlet {
    protected StatsDao statsDao;

    public void init() throws ServletException {
        statsDao = StatsDao.getInstance();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {}

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        res.setStatus(HttpServletResponse.SC_OK);
        APIStats apiStats = new APIStats();
        int cnt = 0;
        int sum = 0;
        try {
            for (String url : new String[]{"/resorts", "/skiers"}) {
                for  (String operation : new String[]{"GET", "POST"}) {
                    APIStatsEndpointStats apiStatsEndpointStats = new APIStatsEndpointStats();
                    Stat stat = statsDao.getStat(url, operation);
                    apiStatsEndpointStats.setURL(url);
                    apiStatsEndpointStats.setOperation(operation);
                    apiStatsEndpointStats.setMean(stat.getCount()==0?0:stat.getSum()/stat.getCount());
                    apiStatsEndpointStats.setMax(stat.getMax());
                    cnt = stat.getCount();
                    sum = stat.getSum();
                    res.getWriter().write(new Gson().toJson(url+" "+operation+" cnt: "+cnt+" sum: "+sum+"\n"));
                    apiStats.addEndpointStatsItem(apiStatsEndpointStats);
                }
            }
        } catch (SQLException ignored) {

        }
        res.getWriter().write(new Gson().toJson(apiStats));
    }
}
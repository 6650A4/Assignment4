package ServerListener;

import DBConnection.RedisPool;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import redis.clients.jedis.Jedis;

public class Listener implements ServletRequestListener {

    private static final String COUNT = "count";
    private static final String MAX_LATENCY = "maxLatency";
    private static final String SUM_LATENCY = "sumLatency";
    private volatile long start;

    public void requestDestroyed(ServletRequestEvent event) {
        String url = ((HttpServletRequest) event.getServletRequest()).getServletPath();
        String operation = ((HttpServletRequest) event.getServletRequest()).getMethod();
        long responseTime = System.currentTimeMillis() - start;
        try (Jedis conn = RedisPool.getResource()) {
            String key = url + "-" +
                operation;
            System.out.println(key);
            if (conn.exists(key)) {
                Map<String, String> cacheValue = conn.hgetAll(key);
                int curSum = Integer.valueOf(cacheValue.get(SUM_LATENCY))+(int)responseTime;
                int curMax = Integer.valueOf(cacheValue.get(MAX_LATENCY));
                if(curSum<0){
                    //if overflow, restart
                    curSum = (int)responseTime;
                    conn.hset(key,COUNT,"0");
                    curMax = curSum;
                }

                conn.hincrBy(key, COUNT, 1);
                conn.hset(key, SUM_LATENCY, String.valueOf(curSum));
                curMax = Math.max(curMax, (int) responseTime);
                conn.hset(key, MAX_LATENCY, String.valueOf(curMax));

            } else {
                Map<String, String> values = new HashMap<>();
                values.put(COUNT, "1");
                values.put(MAX_LATENCY, String.valueOf(responseTime));
                values.put(SUM_LATENCY, String.valueOf(responseTime));
                conn.hset(key, values);
            }
        }
    }

    public void requestInitialized(ServletRequestEvent event) {
        start = System.currentTimeMillis();
    }

}

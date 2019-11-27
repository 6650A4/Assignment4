package DAL;

import DBConnection.RedisPool;
import java.sql.SQLException;
import java.util.Map;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

public class UpdateStatFromRedis implements Runnable {

    @Override
    public void run() {
        System.out.println("Update Stat from Redis");
        try (Jedis cacheConn = RedisPool.getResource()) {
            ScanParams scanParams = new ScanParams().count(10).match("*");
            String cur = ScanParams.SCAN_POINTER_START;
            do {
                ScanResult<String> scanResult = cacheConn.scan(cur, scanParams);

                for (String key : scanResult.getResult()) {
                    System.out.println("key: " + key);
                    String[] parts = key.split("-");
                    if(parts.length!=2)continue;
                    String url = parts[0];
                    String operation = parts[1];
                    Map<String, String> stat = cacheConn.hgetAll(key);
                    try{
                        StatsDao.getInstance().addStat(stat, url, operation);
                    }catch (SQLException EX){

                    }
                }
                cur = scanResult.getCursor();
            } while (!cur.equals(ScanParams.SCAN_POINTER_START));
        }
    }
}

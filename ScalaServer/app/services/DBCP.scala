package services

import org.apache.commons.dbcp2._


object DBCP{
  val dbUrl = "jdbc:mysql://database-1.cbbidv99xyac.us-east-1.rds.amazonaws.com:3306/DistributedSystem?characterEncoding=utf8&useSSL=false"
  var connectionPool = new BasicDataSource()
  connectionPool.setUsername("admin")
  connectionPool.setPassword("assignment4")
  connectionPool.setUrl(dbUrl)
  connectionPool.setMaxTotal(-1)
  connectionPool.setMinIdle(0);
  connectionPool.setMaxIdle(-1);
  connectionPool.setDriverClassName("com.mysql.jdbc.Driver")
  connectionPool.setMaxOpenPreparedStatements(100)
}

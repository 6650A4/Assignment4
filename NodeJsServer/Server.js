const express = require('express');
const app = express();
//const address = "localhost";
//const address = "ec2-3-80-179-196.compute-1.amazonaws.com";
//const address = "jovial-hawk-261523.appspot.com";
//const address = "ec2-user@ec2-18-207-199-234.compute-1.amazonaws.com";

//const port = 8080;

const bodyParser = require('body-parser');
app.use(bodyParser.raw());
app.use(bodyParser.json());

const mysql = require('mysql');
const pool = mysql.createPool({
    connectionLimit: 4000,
    host: 'database-1.cigkl0z72igu.us-east-1.rds.amazonaws.com',
    user: 'root',
    password: '12345678',
    database: 'CS6650'
});

app.get('/', function(request, response) {
    response.writeHead(200);
    response.write("CS6650 A4");
    response.end();
});


app.get('/skiers/:resortID/seasons/:seasonID/days/:dayID/skiers/:skierID', function(request, response) {
    pool.getConnection(function(err, connection) {
        // Use the connection
        if (err) {
            response.writeHead(400);
            response.end();
            return;
        }
        const selectQueryStatement = "SELECT vertical FROM Verticals WHERE url = ?;";
        connection.query(selectQueryStatement, request.path, function(err, result) {
            if (err) {
                connection.release();
                response.writeHead(400);
                response.end();
                return;
            }
            response.writeHead(200);
            response.write(String(result[0].vertical));
            response.end();
            connection.release();
        });
    });
});

app.post('/skiers/:resortID/seasons/:seasonID/days/:dayID/skiers/:skierID', function(request, response) {
    const url = request.path;
    const time = request.body['time'];
    const liftId = request.body['liftID'];
    const resortId = request.params['resortID'];
    const seasonId = request.params['seasonID'];
    const dayId = request.params['dayID'];
    const skierId = request.params['skierID'];
    pool.getConnection(function(err, connection) {
        // Use the connection
        if (err) {
            response.writeHead(400);
            response.end();
            return;
        }
        let insertQueryStatement = "INSERT IGNORE INTO LiftRides (resortId, seasonId, dayId, skierId, time, liftId)" +
            " VALUES (?,?,?,?,?,?)";
        connection.query(insertQueryStatement, [resortId, seasonId, dayId, skierId, time, liftId], function(err){
            if (err) {
                connection.release();
                response.writeHead(400);
                response.end();
                return;
            }
        });
        insertQueryStatement = "INSERT INTO Verticals (url, vertical) values(?,?)" +
            " ON DUPLICATE KEY UPDATE vertical = vertical + values(vertical)";
        connection.query(insertQueryStatement,[url, liftId * 10], function (err) {
            if (err) {
                connection.release();
                response.writeHead(400);
                response.end();
                return;
            }
        });
        connection.release();
        response.writeHead(201);
        response.end();
    });

});

app.listen(3000, () => console.log("Server Running"/*`Server running at http://${address}:${port}`*/));
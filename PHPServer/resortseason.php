<?php
require_once("db.php");
global $ConnectingDB;
header('Content-Type: application/json');
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $query = "SELECT * FROM Resorts INNER JOIN Seasons ON Resorts.ResortId = Seasons.ResortId WHERE Resorts.ResortId={$_GET["resortID"]}";
    $stmt = $ConnectingDB->query($query);
    $dataRes = $stmt->fetchAll(PDO::FETCH_ASSOC);
    http_response_code(200);
    echo json_encode($dataRes);
} else {
    $json = file_get_contents('php://input');
    $data = json_decode($json);
    $to_insert = "INSERT INTO Seasons(Season, ResortId) VALUES(:RESORTid,:SEASONid)";
    $stmt = $ConnectingDB->prepare($to_insert);
    $stmt->bindValue(':RESORTid', $_GET["resortID"]);
    $stmt->bindValue(':SEASONid', $data->year);
    $res = $stmt->execute();
    if ($res) {
        http_response_code(201);
        echo "{\"message\": \"Write successful\"}";
    } else {
        http_response_code(400);
        echo "{\"message\": \"Invalid inputs\"}";
    }
}

?>

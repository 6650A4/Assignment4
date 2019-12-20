<?php
require_once("db.php");
global $ConnectingDB;
header('Content-Type: application/json');
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $query = "SELECT SUM(Vertical) AS TotalVertical FROM (SELECT Vertical FROM LiftRides WHERE ResortId={$_GET["resortID"]} AND SeasonId={$_GET["seasonID"]} AND DayId={$_GET["dayID"]} AND SkierId={$_GET["skierID"]}) AS V";
    $stmt = $ConnectingDB->query($query);
    $dataRes = $stmt->fetch();
    echo $dataRes["TotalVertical"];
    http_response_code(200);
} else {
    $json = file_get_contents('php://input');
    $data = json_decode($json);
    $to_insert = "INSERT INTO LiftRides(ResortId, SeasonId, DayId, SkierId, StartTime, LiftId, Vertical) VALUES(:RESORTid,:SEASONid,:DAYid,:SKIERid,:STARTtime,:LIFTid,:LIFTtime)";
    $stmt = $ConnectingDB->prepare($to_insert);
    $stmt->bindValue(':RESORTid', $_GET["resortID"]);
    $stmt->bindValue(':SEASONid', $_GET["seasonID"]);
    $stmt->bindValue(':DAYid', $_GET["dayID"]);
    $stmt->bindValue(':SKIERid', $_GET["skierID"]);
    $stmt->bindValue(':STARTtime', $data->time);
    $stmt->bindValue(':LIFTid', $data->liftID);
    $stmt->bindValue(':LIFTtime', 10 * $data->liftID);
    $res = $stmt->execute();"SELECT SUM(Vertical) AS TotalVertical FROM (SELECT Vertical FROM LiftRides WHERE ResortId={$_GET["resortID"]} AND SeasonId={$_GET["seasonID"]} AND DayId={$_GET["dayID"]} AND SkierId={$_GET["skierID"]}) AS V";
    if ($res) {
        http_response_code(201);
        echo "{\"message\": \"Write successful\"}";
    } else {
        http_response_code(400);
        echo "{\"message\": \"Invalid inputs\"}";
    }
}

?>

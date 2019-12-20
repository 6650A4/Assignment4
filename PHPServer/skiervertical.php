<?php
require_once("db.php");
global $ConnectingDB;
header('Content-Type: application/json');
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $query = "WHERE ResortId={$_GET["dayID"]} AND SkierId={$_GET["skierID"]}";
    if ($_GET["seasonID"]>0) {
        $query.= " AND SeasonId={$_GET["seasonID"]}";
    }
    $all_query = "SELECT SeasonId, SUM(Vertical) AS TotalVertical FROM (SELECT SeasonId, Vertical FROM LiftRides " . $query . ") AS V GROUP BY SeasonId";
    $stmt = $ConnectingDB->query($query);
    $dataRes = $stmt->fetchAll(PDO::FETCH_ASSOC);
    echo json_encode($dataRes);
    http_response_code(200);
} else {
    http_response_code(400);
    echo "{\"message\": \"Invalid inputs\"}";
}

?>

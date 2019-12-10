<?php
require_once("db.php");
global $ConnectingDB;
header('Content-Type: application/json');
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $query =  "SELECT * FROM Resorts";
    $stmt = $ConnectingDB->query($query);
    $dataRes = $stmt->fetchAll(PDO::FETCH_ASSOC);
    http_response_code(200);
    echo json_encode($dataRes);
} else {
    http_response_code(400);
    echo "{\"message\": \"Invalid inputs\"}";
}

?>

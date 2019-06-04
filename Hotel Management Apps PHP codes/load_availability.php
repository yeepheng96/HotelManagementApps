<?php
error_reporting(0);
include_once("connect.php");
$roomid = $_POST['roomid'];
$sql = "SELECT * FROM allroomdb WHERE roomid = '$roomid'";
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["room"] = array();
    while ($row = $result ->fetch_assoc()){
        $availroom = array();
        $availroom[availability] = $row["availability"];
        array_push($response["room"], $availroom);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>
<?php
error_reporting(0);
include_once("connect.php");
$hotelid = $_POST['hotelid'];
$sql = "SELECT * FROM allroomdb WHERE hotelid = '$hotelid'";
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["room"] = array();
    while ($row = $result ->fetch_assoc()){
        $roomlist = array();
        $roomlist[roomid] = $row["roomid"];
        $roomlist[roomname] = $row["roomname"];
        $roomlist[roomtype] = $row["roomtype"];
        $roomlist[availability] = $row["availability"];
        array_push($response["room"], $roomlist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>
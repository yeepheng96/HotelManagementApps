<?php
error_reporting(0);
include_once("connect.php");
$location = $_POST['location'];
if (strcasecmp($location, "All") == 0){
    $sql = "SELECT * FROM allhoteldb"; 
}else{
    $sql = "SELECT * FROM allhoteldb WHERE location = '$location'";
}
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["allhotel"] = array();
    while ($row = $result ->fetch_assoc()){
        $allhotellist = array();
        $allhotellist[hotelid] = $row["hotelid"];
        $allhotellist[hotelname] = $row["hotelname"];
        $allhotellist[location] = $row["location"];
        $allhotellist[city] = $row["city"];
        $allhotellist[rate] = $row["rate"];
        array_push($response["allhotel"], $allhotellist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>
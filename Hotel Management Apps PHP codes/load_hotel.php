<?php
error_reporting(0);
include_once("connect.php");
$location = $_POST['location'];
$email = $_POST['email'];
if (strcasecmp($location, "All") == 0){
    $sql = "SELECT * FROM hoteldb WHERE email = '$email'"; 
}else{
    $sql = "SELECT * FROM hoteldb WHERE location = '$location' AND email = '$email'";
}
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["hotel"] = array();
    while ($row = $result ->fetch_assoc()){
        $hotellist = array();
        $hotellist[invoiceid] = $row["invoiceid"];
        $hotellist[hotelid] = $row["hotelid"];
        $hotellist[roomid] = $row["roomid"];
        $hotellist[hotelname] = $row["hotelname"];
        $hotellist[roomname] = $row["roomname"];
        $hotellist[roomtype] = $row["roomtype"];
        $hotellist[location] = $row["location"];
        $hotellist[startdate] = $row["startdate"];
        $hotellist[enddate] = $row["enddate"];
        $hotellist[starttime] = $row["starttime"];
        $hotellist[endtime] = $row["endtime"];
        array_push($response["hotel"], $hotellist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>
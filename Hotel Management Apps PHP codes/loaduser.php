<?php
error_reporting(0);
include_once("connect.php");
$email = $_POST['email'];
$sql = "SELECT * FROM hoteluser WHERE email = '$email'";
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["hoteluser"] = array();
    while ($row = $result ->fetch_assoc()){
        $userarray = array();
        $userarray[phone] = $row["phone"];
        $userarray[name] = $row["name"];
         array_push($response["hoteluser"], $userarray);
    }
    echo json_encode($response);
}

?>
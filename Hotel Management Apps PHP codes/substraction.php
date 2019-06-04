<?php
error_reporting(0);
include_once 'connect.php';

$roomid = $_POST['roomid'];
$availability = $_POST['availability'];

$booked = 1;

$math = $availability - $booked;
$availability = $math;

$sql = "update `allroomdb` set `availability` = '$availability' where `roomid`='$roomid'";

if ($conn->query($sql) === TRUE){
    echo "success";
}else {
    echo "failed";
}

?>
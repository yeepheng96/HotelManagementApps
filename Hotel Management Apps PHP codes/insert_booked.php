<?php
error_reporting(0);
include_once 'connect.php';
	$email = $_POST['email'];
	$hotelid = $_POST['hotelid'];
	$hotelname = $_POST['hotelname'];
	$location = $_POST['location'];
	$roomid = $_POST['roomid'];
	$roomname = $_POST['roomname'];
	$roomtype = $_POST['roomtype'];
	$startdate = $_POST['startdate'];
	$enddate = $_POST['enddate'];
	$starttime = $_POST['starttime'];
	$endtime = $_POST['endtime'];

	$sql = "INSERT INTO hoteldb (email,hotelid,hotelname,location,roomid,roomname,roomtype,startdate,enddate,starttime,endtime) VALUES ('$email','$hotelid','$hotelname','$location','$roomid','$roomname','$roomtype','$startdate','$enddate','$starttime','$endtime')";
    
	if ($conn->query($sql) === TRUE){
       echo "success";
    }else {
        echo "failed";
    }

?>
<?php
error_reporting(0);
include_once("connect.php");

$name = $_POST['name'];
$phone = $_POST['phone'];
$email=$_POST['email'];

$sqlinsert = "update `hoteluser` set `name` = '$name', `phone` = '$phone' where `email`='$email'";
if ($conn->query($sqlinsert) === TRUE){
    echo "success";
}else {
    echo "failed";
}

?>
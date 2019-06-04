<?php
error_reporting(0);
include_once("connect.php");
$email = $_POST['email'];
$oldpass = $_POST['key'];
$newpassword = sha1($_POST['newpass']);
 $sqlupdate = "UPDATE hoteluser SET password = '$newpassword' WHERE email = '$email' AND password = '$oldpass'";
  if ($conn->query($sqlupdate) === TRUE){
        echo "<font color='green'><h2><br><br>Your Password Has Been Success Reset!</h2></font>";
  }else{
      echo "<font color='red'><h2><br><br>Failed To Reset Password!</h2></font>";
  }

 
?>
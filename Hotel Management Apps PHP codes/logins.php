<?php
error_reporting(0);
include_once("connect.php");
$email = $_POST['email'];
$password = $_POST['password'];
$password = sha1($password);

$sql = "SELECT * FROM hoteluser WHERE email = '$email' AND password = '$password'";
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    while ($row = $result ->fetch_assoc()){
        echo $data = $row["name"].",".$row["phone"];
    }
}else{
    echo "failed";
}
?>
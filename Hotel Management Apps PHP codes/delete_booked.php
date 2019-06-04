<?php
error_reporting(0);
include_once("connect.php");
$invoiceid = $_POST['invoiceid'];

$sql = "DELETE FROM hoteldb WHERE invoiceid = '$invoiceid'";
$result = $conn->query($sql);
if ($conn->query($sql) === TRUE) {
    echo "success";
    
}else{
    echo "failed";
}
?>
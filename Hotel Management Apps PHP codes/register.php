<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
	$name = $_POST['name'];
	$password = $_POST['password'];
	$password = sha1($password);
	$email = $_POST['email'];
	$phone = $_POST['phone'];

	require_once 'connect.php';
    if (strlen($email) > 0 && strlen($name) > 0 && strlen($password) > 0 && strlen($phone) > 0){
	$sql = "INSERT INTO hoteluser (name,password,email,phone) VALUES ('$name','$password','$email','$phone')";
    
	if ($conn->query($sql) === TRUE){
       echo "success";
    }else {
        echo "failed";
    }
}else{
    echo "nodata";
}
}

?>
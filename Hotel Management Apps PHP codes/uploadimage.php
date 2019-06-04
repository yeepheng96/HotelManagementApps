<?php
error_reporting(0);
include_once("connect.php");

	$encoded_string= $_POST["encoded_string"];
	$image_name = $_POST["image_name"];
	$decoded_string = base64_decode($encoded_string);
	$path = '../profilepicture/'.$image_name;
	$file = fopen($path,'wb');
	$is_written = fwrite($file, $decoded_string);
	fclose($file);
	if($is_written > 0){
		echo "success";
	}else{
		echo "failed";
	}

?>

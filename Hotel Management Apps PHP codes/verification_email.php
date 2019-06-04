<?php
    //ini_set( 'display_errors', 1 );
    error_reporting(0);
    include_once("connect.php");
    $email = $_POST['email'];
    $sql = "SELECT * FROM hoteluser WHERE email = '$email'";
    $result = $conn->query($sql);
    if ($result->num_rows > 0) {
         while ($row = $result ->fetch_assoc()){
             $ran = $row["PASSWORD"];
         }
        $from = "hotelmanagementservice@gmail.com";
        $to = $email;
        $subject = "From Hotel Management Admin (Reset Password)";
        $message = "Click the link to reset your password :"."\n http://supersensitive-dabs.000webhostapp.com/reset_pass.php?email=".$email."&key=".$ran;
        $headers = "From:" . $from;
        mail($email,$subject,$message, $headers);
        echo "success";
    }else{
        echo "failed";
    }
    
    
?>

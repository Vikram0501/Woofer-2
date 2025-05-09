<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$username = "s2798790";
$password = "s2798790";
$database = "d2798790";
$conn = new mysqli("127.0.0.1", $username, $password, $database);
$username = $_GET['username'] ?? '';
$passwd = $_GET['password'] ?? '';
$email = $_GET['email'] ?? '';
$date = date("Y-m-d");

if ($conn->connect_error){
        die("Connection Error: " . $conn->connect_error);
}

if (empty($username) || empty($passwd) || empty($email)){
        die("ERR: MISSING FIELDS");
}

$checkstat = $conn->prepare("SELECT * FROM USERS WHERE USERNAME=?");
$checkstat->bind_param("s",$username);

$checkstat->execute();
$result = $checkstat->get_result();
if ($result->num_rows != 0) {
        echo "ERR: USERNAME ALREADY IN USE";
}
else {
        $insertstat = $conn->prepare("INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, DATE_CREATED) VALUES (?,?,?,?)");
        $insertstat->bind_param("ssss",$username,$passwd,$email,$date);
        if ($insertstat->execute()){
                echo "SUCCESS: USER REGISTERED";
        }
        else{
                echo "ERR: FAILED TO REGISTER";
        }
        $insertstat->close();
}
$checkstat->close();
$conn->close();
?>
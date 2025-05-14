<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$username = "s2798790";
$password = "s2798790";
$database = "d2798790";
$conn = new mysqli("127.0.0.1", $username, $password, $database);
$user_id = $_GET['user_id'] ?? '';

if ($conn->connect_error){
    die("Connection Error: " . $conn->connect_error);
}

if (empty($user_id)){
    die("ERR: MISSING FIELDS");
}
if (!is_numeric($user_id)) {
    die("ERR: INVALID USER ID");
}
$user_id = (int) $user_id;
$deletestat = $conn->prepare("DELETE FROM USERS WHERE USER_ID = ?");
if (!deletestat){
    die("ERR");
}
$deletestat->bind_param("i",$user_id);
if ($deletestat->execute()){
    echo "SUCCESS: USER DELETED";
}
else{
    echo "ERR: FAILED TO DELETE";
}
?>
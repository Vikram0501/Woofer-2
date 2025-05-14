<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$username = "s2798790";
$password = "s2798790";
$database = "d2798790";
$conn = new mysqli("127.0.0.1", $username, $password, $database);
$user1_id = $_GET['user1_id'] ?? '';
$user2_id = $_GET['user2_id'] ?? '';
$date = date('Y-m-d');

if ($conn->connect_error){
    die("Connection Error: " . $conn->connect_error);
}

if (empty($user1_id) || empty($user2_id)){
    die("ERR: MISSING FIELDS");
}
$checkstat = $conn->prepare("SELECT * FROM FRIEND_REQUEST WHERE ((USER_ID_SENDER = ? AND USER_ID_RECEIVER = ?) OR (USER_ID_SENDER = ? AND USER_ID_RECEIVER = ?)) AND ACCEPTED = TRUE");
$checkstat->bind_param("ssss", $user1_id, $user2_id, $user2_id, $user1_id);
$checkstat->execute();
$result = $checkstat->get_result();
if ($result->num_rows != 0){
    die("ERR: ALREADY FRIENDED");
}

$friendstat = $conn->prepare("INSERT INTO FRIEND_REQUEST VALUES (?,?,?,0)");
$likesstat->bind_param("sss", $user1_id, $user2_id, $date);
if ($likesstat->execute()){
    echo "SUCCESS: REQUEST SENT";
}
else{
    echo "ERR: FAILED TO UPDATE";
}

$checkstat->close();
$friendstat->close();
$conn->close();
?>
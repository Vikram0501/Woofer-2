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

$friendstat = $conn->prepare("SELECT * FROM FRIEND_REQUEST WHERE (USER_ID_SENDER = ? OR USER_ID_RECEIVER = ?) AND ACCEPTED = TRUE");
$friendstat->bind_param("ii", $user_id, $user_id);
$friendstat->execute();
$result = $friendstat->get_result();

$friends = array();
if ($result->num_rows == 0){
    echo "NO FRIENDS";
}
else{
    while ($row = $result->fetch_assoc()){
        $friends[]=$row;
    }
    echo json_encode($friends);
}
$friendstat->close();
$conn->close();
?>
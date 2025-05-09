<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$username = "s2798790";
$password = "s2798790";
$database = "d2798790";
$conn = new mysqli("127.0.0.1", $username, $password, $database);
$post_id = $_GET['post_id'] ?? '';
$user_id = $_GET['user_id'] ?? '';

if ($conn->connect_error){
    die("Connection Error: " . $conn->connect_error);
}

if (empty($post_id) || empty($user_id)){
    die("ERR: MISSING FIELDS");
}
$checkstat = $conn->prepare("SELECT * FROM USER_LIKES WHERE POST_ID = ? AND USER_ID = ?");
$checkstat->bind_param("ss", $post_id, $user_id);
$checkstat->execute();
$result = $checkstat->get_result();
if ($result->num_rows != 0){
    die("ERR: ALREADY LIKED");
}

$poststat = $conn->prepare("UPDATE POSTS SET LIKES = ((SELECT LIKES FROM POSTS WHERE POST_ID = ?) + 1) WHERE POST_ID = ?");
$poststat->bind_param("ss", $post_id, $post_id);
if ($poststat->execute()){
    echo "SUCCESS: LIKE ADDED";
}
else{
    echo "ERR: FAILED TO UPDATE";
}

$likesstat = $conn->prepare("INSERT INTO USER_LIKES VALUES (?,?)");
$likesstat->bind_param("ss", $post_id, $user_id);
if ($likesstat->execute()){
    echo "SUCCESS: LIKE USER ADDED";
}
else{
    echo "ERR: FAILED TO UPDATE";
}

$checkstat->close();
$poststat->close();
$likesstat->close();
$conn->close();
?>
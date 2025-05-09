<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$username = "s2798790";
$password = "s2798790";
$database = "d2798790";
$conn = new mysqli("127.0.0.1", $username, $password, $database);
$user_id = $_GET['user_id'] ?? '';
$content = $_GET['content'] ?? '';

$datetime = date('Y-m-d H:i:s');
$likes = 0;

if ($conn->connect_error){
    die("Connection Error: " . $conn->connect_error);
}

if (empty($user_id) || empty($content)){
    die("ERR: MISSING FIELDS");
}

$addpost = $conn->prepare("INSERT INTO POSTS(USER_ID,CONTENT,UPLOAD_DATE_TIME,LIKES)  VALUES (?,?,?,?)");
$addpost->bind_param("sssi",$user_id,$content,$datetime,$likes);
if ($addpost->execute()){
    echo "SUCCESS: POST ADDED";
}
else{
    echo "ERR: FAILED TO UPDATE";
}

$addpost->close();
$conn->close();
?>
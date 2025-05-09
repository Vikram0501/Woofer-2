<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$username = "s2798790";
$password = "s2798790";
$database = "d2798790";
$conn = new mysqli("127.0.0.1", $username, $password, $database);

$userid = $_GET['userid'] ?? '';

if ($conn->connect_error){
    die("Connection Error: " . $conn->connect_error);
}

if (empty($userid)){
    die("ERR: MISSING FIELDS");
}

$stat = $conn->prepare("SELECT * FROM POSTS WHERE USER_ID = ?");
$stat->bind_param("s",$userid);
$stat->execute();
$result = $stat->get_result();

$posts = array();
if ($result->num_rows == 0) {
    echo "NO POSTS";
}
else {
    while ($row = $result->fetch_assoc()){
        $posts[]=$row;
    }
    echo json_encode($posts);
}
$stat->close();
$conn->close();
?>
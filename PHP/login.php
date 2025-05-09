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

if ($conn->connect_error){
        die("Connection Error: " . $conn->connect_error);
}
$statement = $conn->prepare("SELECT * from USERS where USERNAME=? and PASSWORD=?");
$statement->bind_param("ss", $username, $passwd);

$statement->execute();
$result = $statement->get_result();
if ($result->num_rows == 0) {
        echo "ERR: USER NOT FOUND";
}
else {
        echo json_encode($result->fetch_assoc());
}
$statement->close();
$conn->close();
?>
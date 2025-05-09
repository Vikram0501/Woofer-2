<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$username = "s2798790";
$password = "s2798790";
$database = "d2798790";
$conn = new mysqli("127.0.0.1", $username, $password, $database);
$id = $_GET['id'] ?? '';
$input = $_GET['input'] ?? '';
$code = $_GET['code'] ?? '';

if ($conn->connect_error){
    die("Connection Error: " . $conn->connect_error);
}

if (empty($input) || empty($code) || empty($id)){
    die("ERR: MISSING FIELDS");
}

$allowedcodes = [
    'user' => 'USERNAME',
    'pass' => 'PASSWORD',
    'email' => 'EMAIL'
];

if (!array_key_exists($code, $allowedcodes)){
    die("ERR: INVALID CODE");
}
$column = $allowedcodes[$code];
$updatestat = $conn->prepare("UPDATE USERS SET $column = ? WHERE USER_ID = ?");
if (!$updatestat){
    die("ERR: PREP ERROR");
}

$updatestat->bind_param("ss", $input, $id);

if ($updatestat->execute()){
    echo "SUCCESS: USER REGISTERED";
}
else{
    echo "ERR: FAILED TO UPDATE";
}

$updatestat->close();
$conn->close();
?>
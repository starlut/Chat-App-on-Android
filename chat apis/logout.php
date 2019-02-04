<?php
    include "config/db.php";
    $data = file_get_contents('php://input'); // put the contents of the file i$
    $receive = json_decode($data); // decode the JSON feed

    // Connection Check
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }
    // Check POST METHOD
    if ($_SERVER["REQUEST_METHOD"] == 'POST'){
        // Data Receive
        $user_id  = $receive->user_id;

        $sql_check = "SELECT * FROM firebase_token where user_id = '" .$user_id . "'";
        if ($result = $conn->query($sql_check)) {
            $row = $result->fetch_array();
            $sql_delete = "DELETE FROM `firebase_token` WHERE firebase_token.id = '" .$row['id'] ."'";
            if ($conn->query($sql_delete) === true) {
                # code...
                echo "success";
                http_response_code(200);
            }else{
                echo "failed";
                http_response_code(405);
            }
        }
    }else{
        http_response_code(500);
    }

    $conn->close();
?>
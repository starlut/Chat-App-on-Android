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
        $token  = $receive->token;

        $sql_check = "SELECT * FROM firebase_token where user_id = '" .$user_id . "'";
        if ($result = $conn->query($sql_check)) {
         	if($result->num_rows == 0){
                $sql_insert = "INSERT INTO `firebase_token`(`id`, `user_id`, `token`) VALUES ('', '$user_id', '$token')";
                if ($conn->query($sql_insert) === true) {
                    echo "success";
                    http_response_code(200);
                }else{
                    http_response_code(406);
                }
            }else {
                $row = $result->fetch_array();
                $sql_update = "UPDATE `firebase_token` SET `token`= '$token' WHERE firebase_token.id = '" .$row['id'] . "'";
                if ($conn->query($sql_update) === true) {
                    echo "success";
                    http_response_code(200);
                }else{
                    http_response_code(407);
                }
            }
        }
    }else{
        http_response_code(500);
    }

    $conn->close();
?>
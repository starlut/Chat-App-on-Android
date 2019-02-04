<?php

    include "config/db.php";

    $return = array();
    $temp = array();

    if ($conn->connect_error) {
        die("Connection failed " .$conn->connect_error);
    }

    if ($_SERVER["REQUEST_METHOD"] == "GET") {
        parse_str($_SERVER['QUERY_STRING'], $query);
        $thread_id = $query['thread_id'];
        $uid = $query['uid'];

        $sql_check_for_message = "SELECT * FROM `message` WHERE thread_id = '" .$thread_id. "' ORDER BY time DESC";

        

        if ($result = $conn->query($sql_check_for_message)) {
            # code...
            if ($result->num_rows == 0) {
                http_response_code(404);
            }else{
                while ($row = $result->fetch_array()) {
                    $own = false;
                    if ($uid == $row['sender_id']) {
                        # code...
                        $own = true;
                    }else {
                        # code...
                        $own = false;
                    }

                    $temp[] = [
                        "id" => $row['message_id'],
                        "content_type" => $row['content_type'],
                        "content" => $row['content'],
                        "title" => $row['title'],
                        "own" => $own,
                        "time" => $row['time']
                    ];

                    $sql_update = "UPDATE `message` SET `isunread`= 0 WHERE `message_id` = '" .$row['message_id']. "'";

                    if ($conn->query($sql_update) === true) {
                        # code...
                        $return["message_thread_list"] = $temp;
                    }else{
                       http_response_code(403);
                    }
                }

                http_response_code(200);
                echo json_encode($return);
            }
        }else {
            # code...
            http_response_code(404);
        }

    } else {
        $return[] = ["status" => false];
            http_response_code(405);
    }

    $conn->close();
?>







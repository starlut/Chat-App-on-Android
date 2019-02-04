<?php

    include "config/db.php";

    $data = file_get_contents('php://input'); // put the contents of the file i$
    $receive = json_decode($data); // decode the JSON feed

    $return = array();

    if ($conn->connect_error) {
        die("Connection failed " .$conn->connect_error);
    }

    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $sender_id = $receive->sender_id;
        $receiver_id = $receive->receiver_id;
        $message = $receive->message;
        $id;
        $sql_check_duplicate = "SELECT * FROM message_thread_list where sender_id = '" .$receiver_id. "' and reciever_id = '" .$sender_id. "'";

        if($result = $conn->query($sql_check_duplicate)){
            if ($result->num_rows == 0) {
                $sql_check_duplicate = "SELECT * FROM message_thread_list where sender_id = '" .$sender_id. "' and reciever_id = '" .$receiver_id. "'";

                if ($result = $conn->query($sql_check_duplicate)) {
                    # code...
                    if ($result->num_rows == 0) {
                        # code...
                        $sql_insert = "INSERT INTO `message_thread_list`(`id`, `sender_id`, `reciever_id`, `last_message`, `last_message_time`, `isUnread`) VALUES (NULL, '$sender_id', '$receiver_id', '$message', CURRENT_TIMESTAMP, 1);";

                        if ($conn->query($sql_insert) === true) {
                            $last_id = $conn->insert_id;

                            $sql_insert = "INSERT INTO `message`(`message_id`, `content_type`, `content`, `title`, `sender_id`, `receiver_id`, `time`, `thread_id`, `isunread`) VALUES (NULL, 'message' , '$message' , '' , '$sender_id' ,  '$receiver_id' , CURRENT_TIMESTAMP , '$last_id' , 1)";


                            if ($conn->query($sql_insert) === true) {
                                # code...
                                $return[] = ["status" => true];
                                http_response_code(200);
                                echo json_encode($return);
                            }
                        }else{
                            $return[] = ["status" => false, "msg" => "cant INSERT"];
                            http_response_code(400);
                            echo json_encode($return);
                        }
                    }else{
                        http_response_code(409);
                        $return[] = ["status" => false , "msg" => "already exist" ];
                        echo json_encode($return);    
                    }
                }
            }else{
                http_response_code(409);
                $return[] = ["status" => false , "msg" => "already exists"];
                echo json_encode($return);
            }
        }
    } else {
        $return[] = ["status" => false];
        http_response_code(405);
        echo json_encode($return);
    }

    $conn->close();

?>


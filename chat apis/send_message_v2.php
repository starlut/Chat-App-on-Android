<?php
    include "config/db.php";
    include "firebase.php";
    
    $data = file_get_contents('php://input'); // put the contents of the file i$
    $receive = json_decode($data); // decode the JSON feed
    
    $msg = array();

    if ($conn->connect_error) {
        die("conncection failed" . $conn->connect_error);
    }

    if ($_SERVER["REQUEST_METHOD"] == 'POST')
    {   
        $sender_id = $receive->sender_id;
        $receiver_id = $receive->receiver_id;
        $content = $receive->content;
        $content_type = $receive->content_type;
        $title = $receive->title;
        $thread_id = $receive->thread_id;
        $id;

        $content = str_replace("'", "''", $content);

        $sql_insert = "INSERT INTO `message`(`message_id`, `content_type`, `content`, `title`, `sender_id`, `receiver_id`, `time`, `thread_id`, `isunread`) VALUES (NULL, '$content_type' , '$content' , '$title' , '$sender_id' , '$receiver_id' , CURRENT_TIMESTAMP , '$thread_id' , 1)";


        if ($conn->query($sql_insert) === true) {
            # code...
            $sql_update = "UPDATE `message_thread_list` SET `last_message` = '" .$content. "', `last_message_time` = CURRENT_TIMESTAMP, `isUnread` = 1 WHERE `message_thread_list`.`id` = '" .$thread_id . "' ";

            if ($conn->query($sql_update) === true) {
                # code...
                http_response_code(200);

                $sql_token = "SELECT `token` FROM `firebase_token` WHERE `user_id` = '".$receiver_id."'";

                if ($results = $conn->query($sql_token)) {
                    # code...
                    if ($results->num_rows == 0) {
                    	http_response_code(201);
                    }else{
                        $sql_check_user = "SELECT * FROM `user_info` WHERE `uid` = '".$sender_id."'";
                        
                        if ($result = $conn->query($sql_check_user)) {
                            # code...
                            if ($result->num_rows == 0) {
                                # code...

                                http_response_code(417);
                            }else{
                                while ($rows = $result->fetch_array()) {
                                    # code...

                                    $msg = [
                                        "sender_id" => $sender_id,
                                        "content_type" => $content_type,
                                        "content" => $content,
                                        "title" => $title,
                                        "thread_id" => $thread_id,
                                        "sender_name" => $rows['user_full_name'],
                                        "profile_picture" => $rows['profile_picture']
                                    ];
                                };
                            }
                        }

                        while ($row = $results->fetch_array()) {
                            # code...
                            http_response_code(200);
                            echo json_encode(["status" => true]);

                            sendMessage($msg, $row['token'], $msg['sender_name'], $msg['profile_picture'], $msg['content']);
                        };

                        
                    }
                }
            }else{
                http_response_code(408);
            }


        }else{
            http_response_code(409);
        }


    }else{
        http_response_code(403);
    }
    $conn->close();
?>


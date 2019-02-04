<?php
    include "config/db.php";
    
    $data = file_get_contents('php://input'); // put the contents of the file i$
    $receive = json_decode($data); // decode the JSON feed
    
    $return = array();

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


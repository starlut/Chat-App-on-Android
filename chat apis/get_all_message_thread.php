<?php

    include "config/db.php";

    $return = array();
    $temp = array();

    if ($conn->connect_error) {
        die("Connection failed " .$conn->connect_error);
    }

    if ($_SERVER["REQUEST_METHOD"] == "GET") {
        parse_str($_SERVER['QUERY_STRING'], $query);
        $uid = $query['uid'];

                $sql_check_as_sender = "SELECT * FROM `message_thread_list` WHERE sender_id = '" .$uid. "' or reciever_id = '" .$uid. "' ORDER BY isUnread";

                

                if ($result = $conn->query($sql_check_as_sender)) {
                    if ($result->num_rows == 0) {
                        //null
                        http_response_code(404);
                    }else{

                        while ($row = $result->fetch_array()) {
                            if ($row["reciever_id"] == $uid) {
                                # code...
                                $other_id = $row["sender_id"];
                            }else{
                                $other_id = $row["reciever_id"];
                            }
                            $temp[] = [
                                "thread_id" => $row['id'],
                                "other_id" => $other_id,
                                "last_message" => $row["last_message"],
                                "last_message_time" => $row["last_message_time"],
                                "isUnread" => $row['isUnread']
                            ];
                            $return["message_thread_list"] = $temp;
                        }

                        http_response_code(200);
                        echo json_encode($return);
                    }
                }
            }else{
                http_response_code(403);
            }
    $conn->close();
?>



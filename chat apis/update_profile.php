<?php

   include "config/db.php";

    $data = file_get_contents('php://input'); // put the contents of the file i$
    $receive = json_decode($data); // decode the JSON feed

    $return = array();

    if ($conn->connect_error) {
        die("Connection failed " .$conn->connect_error);
    }

    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $uid = $receive->uid;
        $user_name = $receive->user_name;
        $user_full_name = $receive->user_full_name;
        $password = $receive->password;
        $profile_picture = $receive->profile_picture;
        $position = $receive->position;

        $sql_check_for_existance = "SELECT * FROM user_info where uid = '" .$uid. "' and user_name = '" .$user_name. "'";

        if($result = $conn->query($sql_check_for_existance)){
            if ($result->num_rows == 0) {

                
                http_response_code(404);
                $return[] = ["status" => false , "msg" => "doesnt exists"];
            }else{

                $sql_update = "UPDATE `user_info` SET `uid` = '$uid' , `user_name` = '$user_name' , `user_full_name` = '$user_full_name' , `profile_picture` = '$profile_picture' ,`position` = 'position' WHERE `user_info`.uid = '" .$uid . "'";

                if ($conn->query($sql_update) === true) {
                    # code...
                    http_response_code(200);
                    $return[] = ["status" => true , "msg" => "update success"];
                }else{
                    http_response_code(406);
                    $return[] = ["status" => false , "msg" => "something went wrong"];
                }
            }
        }

        echo json_encode($return);
    } else {
        $return[] = ["status" => false];
        http_response_code(405);
        echo json_encode($return);
    }

    $conn->close();

?>


<?php

    include "config/db.php";

    $return = array();
    $temp = array();

    if ($conn->connect_error) {
        die("Connection failed " .$conn->connect_error);
    }

    if ($_SERVER["REQUEST_METHOD"] == "GET") {
        parse_str($_SERVER['QUERY_STRING'], $query);
        $api = $query['api'];
        $uid = $query['uid'];

           
        if ($api == "SINGLE") {
            $sql_all_user = "SELECT * FROM user_info where uid='" .$uid. "'";
            if ($result = $conn->query($sql_all_user)) {
                if ($result->num_rows == 0) {
                    $return[] = ["status" => false];
                    http_response_code(404);
                }else{
                    while ($row = $result->fetch_array()) {
                        $temp[] = [
                            "uid" => $row["uid"],
                            "username" => $row["user_name"],
                            "full_name" => $row["user_full_name"],
                            "picture" => $row["profile_picture"],
                            "position" => $row["position"],
                            "lastlogin" => $row["last_login"]
                        ];

                        $return{"user_info"} = $temp;
                    }

                    http_response_code(200);
                }
                echo json_encode($return);
            }
        } else if ($api == "ALL_USER") {
            $sql_all_user = "SELECT * FROM user_info";
            if ($result = $conn->query($sql_all_user)) {
                if ($result->num_rows == 0) {
                    $return[] = ["status" => false];
                    http_response_code(404);
                }else{
                    while ($row = $result->fetch_array()) {
                        $temp[] = [
                            "uid" => $row["uid"],
                            "username" => $row["user_name"],
                            "full_name" => $row["user_full_name"],
                            "picture" => $row["profile_picture"],
                            "position" => $row["position"],
                            "lastlogin" => $row["last_login"]
                        ];

                        $return{"user_info"} = $temp;
                    }

                    http_response_code(200);
                }
                echo json_encode($return);
            }
        }else{
            http_response_code(404);
        }

    } else {
        $return[] = ["status" => false];
            http_response_code(405);
    }

    $conn->close();
?>



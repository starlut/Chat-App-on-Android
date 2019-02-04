<?php

    $target_dir =  "uploads/";
    $filename = $_POST["filename"];
    

    if (file_exists($_FILES["fileToUpload"]["tmp_name"])) {
        $target_file = $target_dir.$filename;

         if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
            http_response_code(200);
            echo json_encode(array("file_location" => $target_file));

        } else {
            http_response_code(406);
            echo json_encode(array("message" => "Upload Failed.!"));

        }
    } else {

        http_response_code(405);

    }
?>


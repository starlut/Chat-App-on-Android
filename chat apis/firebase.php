<?php
 
 define( 'API_ACCESS_KEY', 'your_api_access_key_from_firebase' );
 
 

 function sendMessage($msg, $token, $user_name, $user_pic, $content){
    $fields = 
    [
        'to'  => $token,
        'priority' => 10,
        'notification' => [
            'title:' => $user_name,
            'body' => $content
        ],
        'data'      => [
            "msg" => $msg,
            'collapse_key' => 'star.lut.com.flutterchatdemo',
            'click_action' => 'FLUTTER_NOTIFICATION_CLICK'
        ]
    ];
 
    $headers = 
    [
        'Authorization: key=' . API_ACCESS_KEY,
        'Content-Type: application/json'
    ];
 
    $ch = curl_init();
    curl_setopt( $ch,CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send' );
    curl_setopt( $ch,CURLOPT_POST, true );
    curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
    curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
    curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
    curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
    $result = curl_exec($ch );
    curl_close( $ch );
    echo $result;
 }
?>
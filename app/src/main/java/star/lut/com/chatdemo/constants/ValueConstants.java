package star.lut.com.chatdemo.constants;

public interface ValueConstants {
    String CONTENT_TYPE_MESSAGE = "message";
    String CONTENT_TYPE_PICTURE = "picture";
    String CONTENT_TYPE_FILE = "others";

    String USER_API_ALL_USER = "ALL_USER";
    String USER_API_SINGLE_USER = "SINGLE_USER";

    String USER_OTHER_ID = "user_other_id";
    String USER_FULL_NAME = "user_other_full_name";
    String USER_OTHER_PIC = "user_other_pic";
    String MESSAGE_THREAD_ID = "message_thread_id";

    String FIREBASE_NOTIFICATION = "firebase_notification";

    int NOTIFICATION_ALWAYS = 0;
    int NOTIFICATION_CUSTOM = 1;
    int NOTIFICATION_NEVER = 2;

    long TIME_ONE_DAY = 86400000;

    int ALARM_TYPE_OFF = 0;
    int ALARM_TYPE_ON = 1;
    String ALARM_TYPE = "alarm_type";
}

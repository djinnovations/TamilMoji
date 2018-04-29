package dj.example.main.modules.fcm;

import dj.example.main.utils.IDUtils;

/**
 * Created by DJphy on 31-07-2017.
 */

public class FCMConstants {

    public static final boolean APPEND_MSGS = false;
    public static final String FCM_CALL = "FCM_APP_STARTED";

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // type of push messages
    public static final int PUSH_TYPE_CHATROOM = 1;
    public static final int PUSH_TYPE_USER = 2;

    // id to handle the notification in the notification try
    public static final int NOTIFICATION_ID = IDUtils.generateViewId();
    public static final int NOTIFICATION_ID_BIG_IMAGE = IDUtils.generateViewId();
}

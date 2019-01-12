package main.boy.pjt.etoll.helper;

/**
 * Created by Boy Panjaitan on 04/03/2018.
 */

public class MyConstant {
    //static final String BASE_URL = "http://192.168.43.197/highway/api/";
    static final String BASE_URL = "http://boypanjaitan.me/highway/api/";
    //static final String BASE_URL = "http://110.232.95.109/highway/api/";

    static class Endpoint{
        static final String registerPath        = "member/register/";
        static final String loadProfile         = "member/get-profile/";
        static final String updateProfile       = "member/set-profile/";
        static final String loginPath           = "member/login/";
        static final String getBalance          = "member/get-balance/";
        static final String checkBalance        = "member/check-balance/";
        static final String topUpBalance        = "member/top-up/";
        static final String getActivities       = "member/get-activities/";
        static final String getRoadList         = "road/get-all/";
        static final String placeRoadOrder      = "road/place-order/";
    }

    public static class System{
        public static final String warningMessageTitle  = "Oops ...";
        public static final String responseSuccess      = "success";
        public static final String loadingMessage       = "Please wait ...";
    }
}

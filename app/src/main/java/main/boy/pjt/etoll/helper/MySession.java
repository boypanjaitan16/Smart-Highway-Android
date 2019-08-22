package main.boy.pjt.etoll.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Boy Panjaitan on 03/02/2018.
 */

public class MySession {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // Shared preferences file name
    private static final String PREF_NAME   = "boy.pjt.wedor";
    private static final String COSTUMER_ID = "COSTUMER_ID";
    private static final String PASSWORD    = "PASSWORD";
    private static final String NAME        = "NAME";
    private static final String EMAIL       = "EMAIL";
    private static final String PHONE       = "PHONE";
    private static final String IS_LOGIN    = "IS_LOGIN";
    private static final String API_PATH    = "API_PATH";
    private static final String FBM_TOKEN   = "FBM_TOKEN";

    public MySession(Context context) {
        pref            = context.getSharedPreferences(PREF_NAME, 0);
        editor          = pref.edit();
    }

    public String getFbmToken(){
        return pref.getString(FBM_TOKEN, FBM_TOKEN);
    }

    public void setFbmToken(String token){
        editor.putString(FBM_TOKEN, token);
        editor.commit();
    }

    public String getApiPath(){
        return pref.getString(API_PATH, MyConstant.BASE_URL);
    }

    public void setApiPath(String api){
        editor.putString(API_PATH, api);
        editor.commit();
    }

    public String getCOSTUMER_ID() {
        return pref.getString(COSTUMER_ID, COSTUMER_ID);
    }

    public void setCOSTUMER_ID(String costumerId) {
        editor.putString(COSTUMER_ID, costumerId);
        editor.commit();
    }

    public String getPASSWORD() {
        return pref.getString(PASSWORD, PASSWORD);
    }

    public void setPASSWORD(String password) {
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public String getNAME() {
        return pref.getString(NAME, NAME);
    }

    public void setNAME(String name) {
        editor.putString(NAME, name);
        editor.commit();
    }

    public String getEMAIL() {
        return pref.getString(EMAIL, EMAIL);
    }

    public void setEMAIL(String email) {
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public String getPHONE() {
        return pref.getString(PHONE, PHONE);
    }

    public void setPHONE(String phone) {
        editor.putString(PHONE, phone);
        editor.commit();
    }

    public boolean getIS_LOGIN() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setIS_LOGIN(boolean isLogin) {
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.commit();
    }
}

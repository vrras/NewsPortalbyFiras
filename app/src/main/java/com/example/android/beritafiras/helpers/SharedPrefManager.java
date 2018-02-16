package com.example.android.beritafiras.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Firas Luthfi on 2/12/2018.
 */

public class SharedPrefManager {

    public static final String SP_BERITAFIRAS_APP = "spBeritafirasApp";

    public static final String SP_IDUSER= "spIdUser";
    public static final String SP_FULLNAME = "spFullname";
    public static final String SP_USERNAME = "spUsername";
    public static final String SP_ACCESS = "spAccess";

    public static final String SP_ALREADY_LOGINADMIN = "spAlreadyLoginAdmin";
    public static final String SP_ALREADY_LOGINREADER = "spAlreadyLoginReader";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_BERITAFIRAS_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSpBeritafirasApp() {
        return SP_BERITAFIRAS_APP;
    }

    public String getSpIduser() {
        return sp.getString(SP_IDUSER, "");
    }

    public String getSpFullname() {
        return sp.getString(SP_FULLNAME, "");
    }

    public String getSpUsername() {
        return sp.getString(SP_USERNAME, "");
    }

    public Boolean getSpAlreadyLoginAdmin() {
        return sp.getBoolean(SP_ALREADY_LOGINADMIN, false);
    }

    public Boolean getSpAlreadyLoginReader() {
        return sp.getBoolean(SP_ALREADY_LOGINREADER, false);
    }

    public String getSpAccess() {
        return sp.getString(SP_ACCESS, "");
    }
}

package in.kvsc.kalpavrikshpro;

import android.app.Application;
import android.content.SharedPreferences;
import utilities.Constant;

/**
 * Created by Rohan on 8/29/2015.
 *
 */
public class GlobalState extends Application {

    public static GlobalState mInstance;
    private static String mToken;

    public static GlobalState getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;//Creating an instance.
        SharedPreferences preferences = getSharedPreferences(Constant.USER_SHARED_PREFS,MODE_PRIVATE);
        mToken = preferences.getString(Constant.USER_TOKEN,null);

    }

    public String getToken() {
        return mToken;
    }

    public boolean isAunthenticated(){
        return mToken != null;
    }

    public void logout(){
        SharedPreferences preferences = getSharedPreferences(Constant.USER_SHARED_PREFS,MODE_PRIVATE);
        preferences.edit().clear().commit();
        mToken = null;
    }



}

package com.example.parkingman;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.example.parkingman.View.ActivityMain;
import com.example.parkingman.View.ActivityParkNew;

import java.lang.ref.WeakReference;

/**
 * Class used to expose static methods with application wide scope.
 * -> This class has to be pointed by the android:name=".App" in the AndroidManifest
 * */
public class App extends Application {

    // this is being held as WeakReference to avoid memory leak
    private static WeakReference<Context> context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = new WeakReference<Context>(this);
    }

    public static Context getContext() {
        return context.get();
    }

    public static Drawable drawable(int id) {
        return ContextCompat.getDrawable(context.get(), id);
    }

    public static String string(int id, Object... formatArgs){
        return context.get().getString(id, formatArgs);
    }

    public static void startMainActivity(){
        Context c = context.get();
        Intent intent = new Intent(c, ActivityMain.class);
        c.startActivity(intent);
    }

}

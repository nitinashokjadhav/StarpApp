package newproject.com.startapp;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;
public class StartApp extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

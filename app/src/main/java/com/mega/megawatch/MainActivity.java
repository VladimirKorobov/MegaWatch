package com.mega.megawatch;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;
import com.mega.megawatch.Views.View2D;
import com.mega.megawatch.Views.ViewGL;
import com.mega.graphics.Threads.MainThread;
import com.mega.graphics.Views.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    Handler handler;
    WatchThread mainThread;
    private MegaModel megaModel;
    private IViewSurface view;

    private final Lock lock = new ReentrantLock();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        megaModel = new MegaModel(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        view = new ViewGL(this, megaModel);
        //view = new View2D(this, megaModel);

        LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);
        layout.addView(view.getView());

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        handler = new Handler() {
            @Override
            public void
            handleMessage(Message msg)
            {
                {
                    lock.lock();
                    try
                    {
                        if(msg.what == 1)
                        {
                            view.Invalidate();
                        }
                    }
                    finally
                    {
                        lock.unlock();
                    }
                }
            }
        };
        mainThread = new WatchThread(handler);
        mainThread.start();
    }
    private void EndMainThread() {
        mainThread.EndThread();
        try {
            Thread.sleep(100);
        }
        catch (InterruptedException e) {
        }
    }
    public void CloseApp() {
        EndMainThread();
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                CloseApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onDestroy() {
        EndMainThread();
        super.onDestroy();
    }

}

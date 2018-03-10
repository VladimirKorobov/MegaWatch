package com.mega.megawatch.Views;

import android.content.Context;
import android.view.MotionEvent;
import com.mega.megawatch.MainActivity;
import com.mega.graphics.DrawObjects.DrawingModel;
import com.mega.graphics.Views.ViewSurface2D;

/**
 * Created by Vladimir on 31.03.2016.
 */
public class View2D extends ViewSurface2D {
    public View2D(Context context, DrawingModel model) {
        super(context, model);
    }

    @Override
    public boolean onTouchEvent(MotionEvent m) {
        ((MainActivity)this.getContext()).CloseApp();
        return true;
    }
}

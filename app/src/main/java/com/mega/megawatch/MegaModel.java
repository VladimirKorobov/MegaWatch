package com.mega.megawatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.text.format.Time;
import com.mega.graphics.DrawObjects.*;
import com.mega.graphics.Renderers.IRenderer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vladimir on 31.03.2016.
 */
public class MegaModel extends DrawingModel {
    private Context context;
    private TransformObject secondArrow;
    private TransformObject minuteArrow;
    private TransformObject hourArrow;

    private OffsetObject monthOffset;
    private OffsetObject dateOffset;
    private OffsetObject dayOffset;

    private RectF calendarRect;
    private float centerX;
    private float centerY;
    private Time today = new Time(Time.getCurrentTimezone());

    private String[] MonthsRus = {
            "Январь",
            "Февраль",
            "Март",
            "Апрель",
            "Май",
            "Июнь",
            "Июль",
            "Август",
            "Сентябрь",
            "Октябрь",
            "Ноябрь",
            "Декабрь"
    };

    private String[] MonthsEng = {
            "January",
            "February",
            "Mart",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
    };

    private String[] DaysRus = {
            "Воскресенье",
            "Понедельник",
            "Вторник",
            "Среда",
            "Четверг",
            "Пятница",
            "Суббота"
    };

    public MegaModel(Context context) {
        this.context = context;
    }

    private void AddArrows(float width, float height, float factor) {
        // Add second arrow
        BitmapObject bitmapSecondArrow = new BitmapObject(context, R.drawable.secondarrow);
        TransformObject transformObjectSecondArrow = new TransformObject(bitmapSecondArrow);
        transformObjectSecondArrow.getMatrix().postScale(factor, factor);
        RectF rect = transformObjectSecondArrow.getRect();
        float objectXCorner = centerX - rect.width() / 2;
        float objectYCorner = centerY - rect.height();
        transformObjectSecondArrow.getMatrix().postTranslate(objectXCorner, objectYCorner);
        secondArrow = new TransformObject(transformObjectSecondArrow);
        this.Add(secondArrow);
        // Add minute arrow
        BitmapObject bitmapMinuteArrow = new BitmapObject(context, R.drawable.minutearrow);
        TransformObject transformObjectMinuteArrow = new TransformObject(bitmapMinuteArrow);
        transformObjectMinuteArrow.getMatrix().postScale(factor, factor);
        rect = transformObjectMinuteArrow.getRect();
        objectXCorner = centerX - rect.width() / 2;
        objectYCorner = centerY - rect.height();
        transformObjectMinuteArrow.getMatrix().postTranslate(objectXCorner, objectYCorner);
        minuteArrow = new TransformObject(transformObjectMinuteArrow);
        this.Add(minuteArrow);

        // Add hour arrow
        BitmapObject bitmapHourArrow = new BitmapObject(context, R.drawable.hourarrow);
        TransformObject transformObjectHourArrow = new TransformObject(bitmapHourArrow);
        transformObjectHourArrow.getMatrix().postScale(factor, factor);
        rect = transformObjectHourArrow.getRect();
        objectXCorner = centerX - rect.width() / 2;
        objectYCorner = centerY - rect.height();
        transformObjectHourArrow.getMatrix().postTranslate(objectXCorner, objectYCorner);
        hourArrow = new TransformObject(transformObjectHourArrow);
        this.Add(hourArrow);
        // Add knob
        BitmapObject bitmapKnob = new BitmapObject(context, R.drawable.smallknob);
        TransformObject transformObject = new TransformObject(bitmapKnob);
        transformObject.getMatrix().postScale(factor, factor);
        rect = transformObject.getRect();
        objectXCorner = centerX - rect.width() / 2;
        objectYCorner = centerY - rect.height() / 2;
        transformObject.getMatrix().postTranslate(objectXCorner, objectYCorner);
        this.Add(transformObject);
    }
    private void AddDate()
    {
        //Month
        TextObject month = new TextObject();
        month.setSize(50);

        month.setColor(0xFF0000FF);

        monthOffset = new OffsetObject(0, 0,  month);

        this.Add(monthOffset);

        // Date
        TextObject date = new TextObject();
        date.setSize(50);
        date.setColor(0xFF0000FF);

        dateOffset = new OffsetObject(0, 0,  date);
        this.Add(dateOffset);

        // Day
        TextObject day = new TextObject();
        day.setSize(40);
        day.setColor(0xFF0000FF);

        dayOffset = new OffsetObject(0, 0,  day);
        this.Add(dayOffset);
    }
    private float AddWatch(float width, float height, DrawingModel model) {
        // Add case
        BitmapObject bitmap = new BitmapObject(context, R.drawable.watchcase);

        double kx = width / bitmap.getBitmap().getWidth();
        double ky = height / bitmap.getBitmap().getHeight();
        float k = (float) ((kx < ky) ? kx : ky);
        //k *= 0.8f;

        TransformObject transformObject = new TransformObject(bitmap);
        transformObject.getMatrix().postScale(k, k);

        RectF rect = transformObject.getRect();

        centerX = (width < height) ? width / 2 : height / 2;
        //centerX *= 0.8f;
        centerY = centerX;

        transformObject.getMatrix().postTranslate(centerX - rect.width() / 2, centerY - rect.height() / 2);

        model.Add(transformObject);

        return k;
    }

    private void AddCalendar(float width, float height, DrawingModel model) {
        float delta = height - centerY * 2;
        float startY = centerY * 2 + delta * 0.1f;
        delta *= 0.9f;
        BitmapObject bitmap = new BitmapObject(context, R.drawable.calendar);

        float ky = delta / bitmap.getBitmap().getHeight();
        float kx = width * 0.9f / bitmap.getBitmap().getHeight();
        float k = Math.min(kx, ky);
        TransformObject calendarObject = new TransformObject(bitmap);
        calendarObject.getMatrix().postScale(k, k);

        calendarRect = calendarObject.getRect();
        calendarObject.getMatrix().postTranslate(centerX - calendarRect.width() / 2, startY);
        calendarRect = calendarObject.getRect();

        model.Add(calendarObject);
    }

    private void UpdateCalendar(Time time)
    {
        TextObject month = (TextObject)monthOffset.getDrawingObject();
        month.setText(Integer.toString(time.year) + ", " + MonthsRus[time.month]);

        RectF rect = month.getRect();
        float startX = centerX - rect.width() / 2;
        float startY = (calendarRect.top + calendarRect.bottom) / 2 - rect.height() / 2;
        monthOffset.setX(startX);
        monthOffset.setY(startY);

        TextObject date = (TextObject)dateOffset.getDrawingObject();
        date.setText(Integer.toString(time.monthDay));

        rect = date.getRect();
        startX = centerX - rect.width() / 2;
        startY = (calendarRect.top + calendarRect.bottom) / 2 - rect.height() / 2;
        startY += 60;
        dateOffset.setX(startX);
        dateOffset.setY(startY);

        // Day
        TextObject day = (TextObject)dayOffset.getDrawingObject();
        day.setText(DaysRus[time.weekDay]);
        rect = day.getRect();
        startX = centerX - rect.width() / 2;
        startY += 60;
        dayOffset.setX(startX);
        dayOffset.setY(startY);
    }

    @Override
    public void Create(float width, float height) {
        this.model.clear();
        BitmapObject bitmapObject = new BitmapObject(width, height, Bitmap.Config.ARGB_8888);
        IRenderer bitmapRenderer = bitmapObject.CreateRenderer(context);
        DrawingModel bitmapModel = new DrawingModel();

        RectObject background = new RectObject();
        background.setLeft(0);
        background.setTop(0);
        background.setRight(width);
        background.setBottom(height);
        background.setColor(0xFF000000);
        bitmapModel.Add(background);

        //AddKnob(width, height, bitmapModel);
        float factor = AddWatch(width, height, bitmapModel);
        AddCalendar(width, height, bitmapModel);
        bitmapModel.Draw(bitmapRenderer);

        this.Add(bitmapObject);
        bitmapModel.Dispose();

        AddArrows(width, height, factor);
        AddDate();
    }
    @Override
    public void Draw(IRenderer renderer) {
        today.setToNow();

        float secondAngle = today.second * 6;
        secondArrow.getMatrix().reset();
        secondArrow.getMatrix().postRotate(secondAngle, centerX, centerY);
        float minuteAngle = today.minute * 6 + secondAngle / 60;
        minuteArrow.getMatrix().reset();
        minuteArrow.getMatrix().postRotate(minuteAngle, centerX, centerY);

        float hourAngle = today.hour * 30 + minuteAngle / 360 * 30;
        hourArrow.getMatrix().reset();
        hourArrow.getMatrix().postRotate(hourAngle, centerX, centerY);

        UpdateCalendar(today);

        super.Draw(renderer);
    }
}

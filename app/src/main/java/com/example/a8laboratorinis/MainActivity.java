package  com.example.a8laboratorinis;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    Context context = this;
    MediaPlayer mediaPlayer;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CanvasView(context){
            @Override
            public void soundMessage(boolean success)
            {
                if (mediaPlayer != null)
                {
                    if (mediaPlayer.isPlaying())
                    {
                        mediaPlayer.stop();
                    }
                }

                if(success)
                {
                    mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
                }
                else
                {
                    mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
                }
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            }
        });
    }

    public class CanvasView extends View
    {
        private Paint paint;
        private Canvas canvas = new Canvas();
        private boolean  stateClear = false;
        private final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        private final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        private double shapeRadius;
        private boolean stateDrawCircle = false;
        private float circleRadius;
        private double circleStartX;
        private double circleStartY;

        public CanvasView(Context context)
        {
            super(context);
            paintSettings();
            invalidate();
            setOnTouchListener(new SwipeAndTouch(context)
            {
                @Override
                public void SwipeLeft()
                {
                    stateClear = false;
                    invalidate();
                }

                @Override
                public void SingleTouch()
                {
                    stateClear = true;
                    stateDrawCircle = false;
                    invalidate();
                }

                @Override
                public void SecondTouchEntry(double radius, double x, double y)
                {
                    stateClear = false;
                    stateDrawCircle = true;
                    circleRadius = (float) radius / 2;
                    circleStartX = x;
                    circleStartY = y;
                    invalidate();
                }

                @Override
                public void SecondTouchEnd()
                {
                    soundMessage(circleRadius >= shapeRadius);
                    shapeRadius = circleRadius;
                }
            });
        }

        public void soundMessage(boolean isSuccess)
        {

        }

        public void paintSettings()
        {
            paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            this.canvas = canvas;
            if(!stateClear && stateDrawCircle == false)
            {
                generateShape(canvas);
            }
            else if (stateDrawCircle)
            {
                setCircle(circleRadius);
            }
            else
            {
                clearCanvas(canvas);
            }
        }

        private void generateShape(Canvas canvas)
        {
            float radius = new Random().nextFloat() * (screenWidth / 2 - 100 + 1) + 100;
            float x = new Random().nextFloat() * ((screenWidth - radius) - radius + 1) + radius;
            float y = new Random().nextFloat() * ((screenHeight - radius) - radius + 1) + radius;
            paint.setColor(Color.GREEN);
            shapeRadius = radius;
            int randomShapeNumber = new Random().nextInt(2 - 1 + 1) + 1;
            switch (randomShapeNumber)
            {
                case 1:
                    canvas.drawRect(x - radius, y - radius, x + radius, y + radius, paint);
                    break;
                case 2:
                    canvas.drawCircle(x, y, radius, paint);
            }
        }

        public void setCircle(float circleRadius)
        {
            paint.setColor(Color.GREEN);
            canvas.drawCircle((float) circleStartX, (float) circleStartY, circleRadius, paint);
        }

        public void clearCanvas(Canvas canvas)
        {
            canvas.drawColor(Color.WHITE);
        }
    }
}

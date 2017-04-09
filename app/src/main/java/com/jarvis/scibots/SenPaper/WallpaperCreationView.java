package com.jarvis.scibots.SenPaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by aniket on 07-04-2017.
 */

public class WallpaperCreationView extends View{

    private Paint drawPaint;
    private ArrayList<Integer> postionsX;
    private ArrayList<Integer> postionsY;
    int Depth = 5;
    int[] colors;
    int MagicNumber;
    int rectangles;
    int triangles;
    int circles;
    int BackgroundColor;


    public WallpaperCreationView(Context context,AttributeSet attrs) {
        super(context, attrs);
        SetPaints();



    }
/*Sets the sensors
* data for crafting the wallpaper
* */
    public void setValues(ArrayList<Integer> postionsX,
                          ArrayList<Integer> postionsY,
                          int Depth,int[] colors,
                          int MagicNumber,
                          int rectangles,
                          int trinagles,
                          int circles,
                          int BackgroundColor) {
        this.postionsY = postionsY;
        this.postionsX = postionsX;
        this.Depth = Depth;
        this.colors = colors;
        this.MagicNumber = MagicNumber;
        this.rectangles = rectangles;
        this.triangles = trinagles;
        this.circles = circles;
        this.BackgroundColor = BackgroundColor;

    }
    private void SetPaints() {
        drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        drawPaint.setColor(getResources().getColor(R.color.yellow_900));
        drawPaint.setStyle(Paint.Style.FILL);
        drawPaint.setShadowLayer(Depth, 0, 0, Color.BLACK);
        setLayerType(LAYER_TYPE_SOFTWARE, drawPaint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap  bitmap = Bitmap.createBitmap( canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c= new Canvas(bitmap);
        Rect myrect1=new Rect();
        myrect1.set(0,0,canvas.getWidth(),canvas.getHeight());
        drawPaint.setColor(BackgroundColor);
        canvas.drawRect(myrect1,drawPaint);
        c.drawRect(myrect1,drawPaint);

        Random r = new Random();
        int value = r.nextInt(800 - 300) + 300;

        int i = 0;
        while (i <postionsX.size()) {
            /*
            * draws the circles
            * */
            for (int j = 0; j < circles; j++) {
                drawPaint.setColor(colors[i]);
                canvas.drawCircle(postionsX.get(i), postionsY.get(i), value + i * MagicNumber, drawPaint);
                c.drawCircle(postionsX.get(i), postionsY.get(i), value + i * MagicNumber, drawPaint);
                i++;
            }

            /*Draws the triangles*/
            for (int j = 0; j < triangles; j++) {
                drawPaint.setColor(colors[i]);
                Path path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                path.moveTo(postionsX.get(i), postionsY.get(i));
                path.lineTo(r.nextInt(2000 - 800) + 800, r.nextInt(2000 - 800) + 800);
                path.lineTo(r.nextInt(2000 - 800) + 800, r.nextInt(2000 - 800) + 800);
                path.lineTo(postionsX.get(i), postionsY.get(i));
                path.close();
                canvas.drawPath(path, drawPaint);
                c.drawPath(path, drawPaint);
                i++;
            }
            /*draws the rectangles*/
            for (int j = 0; j < rectangles; j++) {
                Rect myrect = new Rect();
                int val1 = postionsX.get(i) + r.nextInt(2000 - 400) + 400;
                int val2 =  postionsY.get(i) + r.nextInt(2000 - 400) + 400;
                myrect.set(postionsX.get(i), postionsY.get(i),val1,val2);
                drawPaint.setColor(colors[i]);
                canvas.drawRect(myrect, drawPaint);
                c.drawRect(myrect, drawPaint);
                i++;
            }

            /*
            * Saves the Wallpaper offline
            **/



            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(Environment.getExternalStorageDirectory()+"/Wallpaper"+ System.currentTimeMillis()+ ".jpg")));
               Toast.makeText(getContext(),"Saved in Storage",Toast.LENGTH_SHORT).show();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onDraw(canvas);

    }
}

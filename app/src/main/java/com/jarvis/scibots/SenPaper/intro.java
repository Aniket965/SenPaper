package com.jarvis.scibots.SenPaper;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.ChangeColor;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Parallel;
import su.levenetc.android.textsurface.animations.Rotate3D;
import su.levenetc.android.textsurface.animations.Sequential;
import su.levenetc.android.textsurface.animations.ShapeReveal;
import su.levenetc.android.textsurface.animations.SideCut;
import su.levenetc.android.textsurface.animations.Slide;
import su.levenetc.android.textsurface.animations.TransSurface;
import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.contants.Pivot;
import su.levenetc.android.textsurface.contants.Side;

public class intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        TextSurface textSurface =(TextSurface)findViewById(R.id.intro);
        final Typeface Pacifico= Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(Pacifico);

        Text Senpaper = TextBuilder
                .create("SenPaper")
                .setPaint(paint)
                .setSize(64)
                .setAlpha(0)
                .setColor(Color.BLACK)
                .setPosition(Align.SURFACE_CENTER).build();

        Text wallgen = TextBuilder
                .create("Wallpaper generation")
                .setPaint(paint)
                .setSize(20)
                .setAlpha(0)
                .setColor(Color.BLACK)
                .setPosition(Align.BOTTOM_OF, Senpaper).build();

        Text sen = TextBuilder
                .create(" by Sensors")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.BLACK)
                .setPosition(Align.RIGHT_OF, wallgen).build();

        Text androidExperiment = TextBuilder
                .create("Android Experiment")
                .setPaint(paint)
                .setSize(30)
                .setAlpha(0)
                .setColor(Color.BLACK)
                .setPosition(Align.BOTTOM_OF, sen).build();


        textSurface.play(
                new Sequential(
                        ShapeReveal.create(Senpaper, 750, SideCut.show(Side.LEFT), false),
                        new Parallel(ShapeReveal.create(Senpaper, 600, SideCut.hide(Side.LEFT), false), new Sequential(Delay.duration(300), ShapeReveal.create(Senpaper, 600, SideCut.show(Side.LEFT), false))),
                        new Parallel(new TransSurface(500, wallgen, Pivot.CENTER), ShapeReveal.create(wallgen, 1300, SideCut.show(Side.LEFT), false)),
                        Delay.duration(500),
                        new Parallel(new TransSurface(750, sen, Pivot.CENTER), Slide.showFrom(Side.LEFT, sen, 750), ChangeColor.to(sen, 750, Color.WHITE)),
                        Delay.duration(500),
                        new Parallel(TransSurface.toCenter(androidExperiment, 500), Rotate3D.showFromSide(androidExperiment, 750, Pivot.TOP))

                )
        );
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {


            }

            public void onFinish() {
                Intent intent = new Intent(intro.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

        }.start();

    }
}

package com.jarvis.scibots.SenPaper;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class Wallpaper extends AppCompatActivity {
    ArrayList<Integer> DarkBackgrounds;
    int shapesCount = 0 ;
    int Depth = 0 ;
    int MagicNumber;
    public  ArrayList<Integer> postionsX ;
    public ArrayList<Integer> postionsY;
    public int[] SelectedColors;
    private WallpaperCreationView wView;
    int NOTriangles;
    int NORectangles;
    int NOCircles;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.start, R.anim.end);
        setContentView(R.layout.activity_wallpaper);
        TextView regen = (TextView)findViewById(R.id.back);
        final Typeface Pacifico= Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");

        DarkBackgrounds = new ArrayList<>();
        setupBackgrounds();






        regen.setTypeface(Pacifico);
        /*
        * gets data from bundle
        */
        Intent intent3 = getIntent();
        Bundle b = intent3.getExtras();
        int BackgroundValue = b.getInt("BackgroundIndex");
        shapesCount = b.getInt("TimeDiff");
        Depth = b.getInt("avgDepth");
        MagicNumber = b.getInt("magicColor");
        postionsX = b.getIntegerArrayList("XCordinates");
        postionsY = b.getIntegerArrayList("YCordinates");
        /*
        * Finds Random shapes ratio
        */
        
        Random r = new Random();
        NORectangles = r.nextInt(shapesCount);
        NOTriangles = r.nextInt(Math.abs(shapesCount - NORectangles ) );
        NOCircles = Math.abs(shapesCount - NOTriangles - NORectangles);
        SelectedColors = FindColors(shapesCount,MagicNumber);
        Log.d("list" ,  NOTriangles + " " + NORectangles + "    "+ NOCircles);

        wView = (WallpaperCreationView) findViewById(R.id.ViewMaster);
        wView.setValues(postionsX,postionsY,Depth,SelectedColors,MagicNumber,NORectangles,NOTriangles,NOCircles,getResources().getColor(DarkBackgrounds.get(BackgroundValue)));


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.wallpaperlayout);
        relativeLayout.setBackgroundColor(getResources().getColor(DarkBackgrounds.get(BackgroundValue)));


    }

/*
* finds the colors for the given number of shapes and Magnetic Value
*
* */
    private int[] FindColors(int shapesCount,int MagicColor) {
        int[] SelectedColors = new  int[shapesCount];
        String[] allColors = Wallpaper.this.getResources().getStringArray(R.array.materialcolors);
        Random r = new Random();
        int  ColorVariance = r.nextInt(255 - 0) + 0;
        int index;
        /*
        * 255 is the last index of the colors from material colors
        */
        if(MagicColor > 255 ){
            SelectedColors[0] = Color.parseColor(allColors[125]);

            index = 255;

        }
        else{
            index= MagicColor;
            SelectedColors[0] = Color.parseColor(allColors[index]);

        }

        if(shapesCount > 1 ){
            for(int i = 1 ; i < shapesCount ; i++ ){

                index = index+ ColorVariance;
                if(index > 255){
                    index= index- 255;
                }
                SelectedColors[i] = Color.parseColor(allColors[index]);

            }
        }

        return  SelectedColors;
    }

     /*fixes the Background colors for the
     * Background of the view
     * 
     */
    private void setupBackgrounds() {
        DarkBackgrounds.add(R.color.white);
        DarkBackgrounds.add(R.color.amber_900);
        DarkBackgrounds.add(R.color.yellow_900);
        DarkBackgrounds.add(R.color.orange_900);
        DarkBackgrounds.add(R.color.deep_orange_900);
        DarkBackgrounds.add(R.color.red_900);
        DarkBackgrounds.add(R.color.teal_900);
        DarkBackgrounds.add(R.color.light_green_900);
        DarkBackgrounds.add(R.color.green_900);
        DarkBackgrounds.add(R.color.brown_900);
        DarkBackgrounds.add(R.color.pink_900);
        DarkBackgrounds.add(R.color.blue_grey_900);
        DarkBackgrounds.add(R.color.teal_900);
        DarkBackgrounds.add(R.color.grey_700);
        DarkBackgrounds.add(R.color.deep_purple_900);
        DarkBackgrounds.add(R.color.purple_900);
        DarkBackgrounds.add(R.color.indigo_900);
        DarkBackgrounds.add(R.color.pink_A400);
        DarkBackgrounds.add(R.color.grey_900);
    }

   /*
   * Go back to main screen when regenrate is pressed
   */
    public void goback(View view) {
    Intent intent = new Intent(Wallpaper.this,MainActivity.class);
        startActivity(intent);
        finish();

    }
}



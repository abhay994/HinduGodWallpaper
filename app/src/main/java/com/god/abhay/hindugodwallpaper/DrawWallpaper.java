package com.god.abhay.hindugodwallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.gospelware.liquidbutton.LiquidButton;

import java.io.IOException;

/**
 * Created by abhay on 7/6/17.
 */

public class DrawWallpaper  {



    public void Wallpaper(ImageView imageView,LiquidButton liquidButton,Context context){



        imageView.buildDrawingCache();

        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(context);
        Bitmap bmap = imageView.getDrawingCache();
        liquidButton.setAutoPlay(true);

        try {


            myWallpaperManager.setBitmap(bmap);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            liquidButton.finishPour();
        }
    }
}

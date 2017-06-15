package com.god.abhay.hindugodwallpaper;



import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.artjimlop.altex.AltexImageDownloader;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.gospelware.liquidbutton.LiquidButton;
import com.squareup.picasso.Picasso;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;

import github.ishaan.buttonprogressbar.ButtonProgressBar;


public class Main2Activity extends AppCompatActivity {
    DatabaseReference mdatabase;
    ImageView imageView, imaged, images, imagew;
    LiquidButton liquidButton;
    int t;
    Uri uri;


    String post_image;
    Handler handler,handler2;
   TextView result;
    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        liquidButton = (LiquidButton) findViewById(R.id.button);
        result=(TextView)findViewById(R.id.textView2);
        imageView = (ImageView) findViewById(R.id.imageView2);
        imaged = (ImageView) findViewById(R.id.download);
        imagew = (ImageView) findViewById(R.id.wallaper);
        images = (ImageView) findViewById(R.id.share);



       handler = new Handler();
        handler2=new Handler();
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-4855672100917117/2877707381");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
        if(!mInterstitialAd.isLoaded()){
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

       /* liquidButton.setFillAfter(true);
        liquidButton.setAutoPlay(true);*/


        String mpost_key = getIntent().getExtras().getString("key_post");
        /*Toast.makeText(Main2Activity.this,post_key,Toast.LENGTH_LONG).show();*/
        mdatabase = FirebaseDatabase.getInstance().getReference().child("god");
        mdatabase.keepSynced(true);

        mdatabase.child(mpost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post_image = (String) dataSnapshot.child("image").getValue();
               /* Toast.makeText(getApplicationContext(),post_image,Toast.LENGTH_LONG).show();*/
            Glide.with(getApplicationContext()).load(post_image)

                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        imagew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(imageView.getDrawable()!=null) {

         Runnable runnable5=new Runnable() {
             @Override
             public void run() {
                 liquidButton.startPour();
                 DrawWallpaper wallpaper=new DrawWallpaper();
                 Context context=getApplicationContext();
                 wallpaper.Wallpaper(imageView,liquidButton,context);
             }
         };
                handler.postDelayed(runnable5,2);






             }

            }
        });


        liquidButton.setPourFinishListener(new LiquidButton.PourFinishListener() {
            @Override
            public void onPourFinish() {
                handler2.removeCallbacks(runnable2);
                handler2.postDelayed(runnable2,100);

            }

            @Override
            public void onProgressUpdate(float progress) {

            }
        });

        imaged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              if (imageView.getDrawable() != null) {
                    if ((ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                            (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {


                        ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);

                    }


                    if ((ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                            && (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                        File file = new File(Environment.getExternalStorageDirectory(), "Pictures/GOD");
                        if (!file.exists()) {
                            file.mkdir();

                        }
                        final AltexImageDownloader downloader = new AltexImageDownloader(new AltexImageDownloader.OnImageLoaderListener() {
                            @Override
                            public void onError(AltexImageDownloader.ImageError error) {
                                Toast.makeText(Main2Activity.this, "check connection", Toast.LENGTH_LONG).show();
                                imaged.setImageResource(R.drawable.ic_cancel_black_24dp);

                            }

                            @Override
                            public void onProgressChange(int percent) {
                                t = percent;
                                imaged.setImageResource(R.drawable.ic_sync_black_24dp);

                            }

                            @Override
                            public void onComplete(Bitmap result) {
                                handler.removeCallbacks(runnable1);
                                handler.postDelayed(runnable1, 500);
                                handler2.removeCallbacks(runnable2);
                                handler2.postDelayed(runnable2,100);


                            }

                        });
                        AltexImageDownloader.writeToDisk(getApplicationContext(), post_image, "GOD/lk");
                        downloader.download(post_image, true);

                    }


                }
            }

        });


        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   /* handler2.removeCallbacks(runnable2);
                    handler2.removeCallbacks(runnable2, 100);*/

                    if ((ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                            (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {


                        ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);

                    }







                    if(imageView.getDrawable()!=null) {
                        if ((ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                                && (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                        ImageView ivImage = (ImageView) findViewById(R.id.imageView2);
                        // Get access to the URI for the bitmap

                        Drawable mDrawable = ivImage.getDrawable();
                        Bitmap mBitmap = ((GlideBitmapDrawable) mDrawable).getBitmap();

                        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                                mBitmap, "Image Description", null);

                        uri = Uri.parse(path);
                        Downloaded downloaded = new Downloaded();

                        downloaded.execute();



                }
                    }

                  // Get access to the URI for the bitmap
            }
        });


 imageView.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         onBackPressed();
     }
 });



    }





    Runnable runnable1 =new Runnable() {
        @Override
        public void run() {
            imaged.setImageResource(R.drawable.ic_cloud_done_black_24dp);
        }










    };

    Runnable runnable2=new Runnable() {
        @Override
        public void run() {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
        }
    };
private class Downloaded extends AsyncTask<Void,String,String>{

        @Override
        protected String doInBackground(Void... uris) {

            Uri bmpUri =uri;
            if (bmpUri != null) {
                // Construct a ShareIntent with link to image

                Intent shareIntent = new Intent();


                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.god.abhay.hindugodwallpaper");
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.setType("image/*");

                // Launch sharing dialog for image
                startActivity(Intent.createChooser(shareIntent, "Share Image"));
            } else {
                // ...sharing failed, handle error
            }
            return null;
        }

    }

/*

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable

        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
*/



    @Override
    public void onBackPressed() {
        super.onBackPressed();
         supportFinishAfterTransition();

    }



}


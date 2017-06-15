package com.god.abhay.hindugodwallpaper;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    DatabaseReference mdatabase;

    public String keyman;
    private InterstitialAd mInterstitialAd;
    private static final int AD_VIEW=1;
    private static final int menu_VIEW=0;
    CardView cardView;

  static int Count=0;




    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_v);






        Button button1=(Button)findViewById(R.id.button2);
        TextView textView=(TextView)findViewById(R.id.textView);

      /*  Glide.with(this).load(R.drawable.god)
        .into(imageView);
*/
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

        // Set the CardView layoutParams

        final Handler handler = new Handler();



        mdatabase = FirebaseDatabase.getInstance().getReference().child("god");
        mdatabase.keepSynced(true);






         button1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = getIntent();
                 finish();
                 intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                 startActivity(intent);
             }
         });






        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        }
        recyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Handler handler2 = new Handler();

                if(Count==20||Count==40||Count==55||Count==75){

                    handler2.post(runnable2);
                    handler2.postDelayed(runnable2,3000);
                }


                return false;
            }
        });





        if(isInternetOn()){
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable,100);



            button1.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }else {
           /* final ImageView imageView = (ImageView) findViewById(R.id.imageView);




            Picasso.with(this).load(R.drawable.loade).into(imageView);*/
        }



    }


    public static class BlogerViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView img;
        int i;
        CardView cardView;
        public BlogerViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            cardView=(CardView)mView.findViewById(R.id.my_card);
        }

        public void setImg(Context cnt, String image) {
            img = (ImageView) mView.findViewById(R.id.image_v1);
            Glide.with(cnt).load(image)


                    .into(img);










        }
        public void postistion(int p) {
            i=  p;
           /* TextView textView1=(TextView)mView.findViewById(R.id.textViewe);
            textView1.setText(String.valueOf(i));*/

            if(i%3==0){
                cardView.setVisibility(View.VISIBLE);


                  cardView.setTranslationZ(10);
            }else {
                cardView.setTranslationZ(40);


            }

        }
    }
    public static class NativeExpressAdd extends RecyclerView.ViewHolder{

        public NativeExpressAdd(View itemView) {
            super(itemView);
        }
    }




    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection


        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
            return isConnected;

    }

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


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {


                FirebaseRecyclerAdapter<ImageObject, BlogerViewHolder> firebaseRecyclerAdapter = new
                        FirebaseRecyclerAdapter<ImageObject, BlogerViewHolder>(
                                ImageObject.class,
                                R.layout.my_card,
                                BlogerViewHolder.class,

                                mdatabase
                        ) {

                            @Override
                            protected void populateViewHolder(final BlogerViewHolder viewHolder, ImageObject model, int position) {
Count =position;

                                final String post_key = getRef(position).getKey();
                                keyman = post_key;
                                viewHolder.setImg(getApplicationContext(),model.getImage());

                              viewHolder.postistion(position);
                                 /*  viewHolder.setImg2(getApplicationContext(),model.getImage());*/




                                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        try {
                                            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                                            intent.putExtra("key_post", post_key);
                                        /*    startActivity(intent);*/
                                            String transitionName = getString(R.string.transition_string);

                                            // Define the view that the animation will start from
                                            View viewStart = findViewById(R.id.my_card);
                                            ActivityOptionsCompat options =

                                                    ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                                                         viewHolder.mView,   // Starting view
                                                            transitionName // The String
                                                    );
                                            //Start the Intent
                                            ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());

                                        } catch (Exception e) {

                                        }



                                    }
                                });



                            }
                        };
                DefaultItemAnimator defaultItemAnimator=new DefaultItemAnimator();
                defaultItemAnimator.setAddDuration(2000);

                recyclerView.setItemAnimator(defaultItemAnimator);

                recyclerView.setAdapter(firebaseRecyclerAdapter);

            } catch (Exception e) {

            }

        }
    };


}

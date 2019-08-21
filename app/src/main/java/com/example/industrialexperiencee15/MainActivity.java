package com.example.industrialexperiencee15;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {


//    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button Go = (Button) findViewById(R.id.button);
//        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation);
//        startCheckAnimation();


        Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent test = new Intent(MainActivity.this, Dashboard.class);
                MainActivity.this.startActivity(test);
            }
        });

    }


//    private void startCheckAnimation(){
//        ValueAnimator animator = ValueAnimator.ofFloat(0f,1f).setDuration(3000);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                lottieAnimationView.setProgress((Float)animation.getAnimatedValue());
//            }
//        });
//
//        if(lottieAnimationView.getProgress() == 0f){
//            animator.start();
//        }else{
//            lottieAnimationView.setProgress(0f);
//        }
//    }
}

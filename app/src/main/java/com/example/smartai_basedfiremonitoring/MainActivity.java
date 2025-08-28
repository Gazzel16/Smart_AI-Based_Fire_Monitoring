package com.example.smartai_basedfiremonitoring;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.example.smartai_basedfiremonitoring.Gemini.GeminiAdvisory;
import com.example.smartai_basedfiremonitoring.Gemini.GeminiAdvisoryDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        // Load Home by default
        if (savedInstanceState == null) {
            loadFragment(new UserSensorDashboardFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_dashboard) {
                selectedFragment = new UserSensorDashboardFragment();
            }

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            }

            if (item.getItemId() == R.id.nav_settings) {
                selectedFragment = new SettingsFragment();
            }


            // else if (item.getItemId() == R.id.nav_home) {
            //     selectedFragment = new HomeFragment();
            // }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });

        getAdviceFromGemini();
    }

    private void getAdviceFromGemini(){
        ImageView geminiAdvisory = findViewById(R.id.geminiAdvisory);
        LottieAnimationView geminiLottie = findViewById(R.id.geminiLottie);

        geminiAdvisory.setOnClickListener(v -> {
            // 1. Show Lottie now
            geminiLottie.setVisibility(View.VISIBLE);
            geminiLottie.playAnimation();

            // 2. Delay 2s, then hide Lottie + show dialog
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                geminiLottie.cancelAnimation();
                geminiLottie.setVisibility(View.GONE);

                // 3. Show dialog
                GeminiAdvisoryDialog dialog = new GeminiAdvisoryDialog();
                dialog.show(getSupportFragmentManager(), "GeminiDialog");

                // 4. Start HTTP request
                GeminiAdvisory.geminiAdvisory(this, dialog);

            }, 2000); // wait 2 seconds
        });

        geminiAdvisory.setOnTouchListener(new View.OnTouchListener() {
            float dX, dY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        return false;

                    case MotionEvent.ACTION_MOVE:
                        v.setX(event.getRawX() + dX);
                        v.setY(event.getRawY() + dY);
                        return true;

                    default:
                        return false;
                }
            }
        });

        geminiAdvisory.bringToFront();
        geminiAdvisory.invalidate();
    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
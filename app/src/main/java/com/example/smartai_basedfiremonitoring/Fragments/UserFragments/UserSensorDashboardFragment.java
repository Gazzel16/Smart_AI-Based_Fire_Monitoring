    package com.example.smartai_basedfiremonitoring.Fragments.UserFragments;

    import android.graphics.Color;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;

    import com.example.smartai_basedfiremonitoring.Model.User;
    import com.example.smartai_basedfiremonitoring.R;
    import com.example.smartai_basedfiremonitoring.Sensors.FlameSensor;
    import com.example.smartai_basedfiremonitoring.Sensors.HumidSensor;
    import com.example.smartai_basedfiremonitoring.Sensors.TempSensor;
    import com.github.mikephil.charting.charts.LineChart;
    import com.github.mikephil.charting.components.XAxis;
    import com.github.mikephil.charting.components.YAxis;
    import com.github.mikephil.charting.data.Entry;
    import com.github.mikephil.charting.data.LineData;
    import com.github.mikephil.charting.data.LineDataSet;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;

    public class UserSensorDashboardFragment extends Fragment {

        TextView timeFireDetected, flameDetector,tempAnalogOutput,
                tempStatus, humidAnalogOutput, humidStatus, smokeOutput, smokeStatus, username;

        LineChart tempLineChart, humidLineChart;
        private static ValueEventListener tempListener;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            View view =  inflater.inflate(R.layout.user_sensor_dashboard, container, false);

            timeFireDetected = view.findViewById(R.id.timeFireDetected);
            flameDetector = view.findViewById(R.id.flameDetector);

            tempAnalogOutput = view.findViewById(R.id.tempAnalogOutput);
            tempStatus = view.findViewById(R.id.tempStatus);

            humidAnalogOutput = view.findViewById(R.id.humidAnalogOutput);
            humidStatus = view.findViewById(R.id.humidStatus);

            smokeOutput = view.findViewById(R.id.smokeOutput);
            smokeStatus = view.findViewById(R.id.smokeStatus);

            tempLineChart = view.findViewById(R.id.tempLineChart);
            humidLineChart = view.findViewById(R.id.humidLineChart);

            FlameSensor.flameMonitoring(timeFireDetected, flameDetector, this);
            TempSensor.tempMonitoring(tempAnalogOutput, tempStatus, tempLineChart, this);
            HumidSensor.humidMonitoring(humidAnalogOutput, humidStatus, humidLineChart, this);

//            UserNameHandler(view);
            return view;
        }





        @Override
        public void onDestroyView() {
            super.onDestroyView();
            // stop Firebase listeners to avoid crashes
            TempSensor.removeListener();
            HumidSensor.removeListener();
            FlameSensor.removeListener();

        }

//        public void UserNameHandler(View view){
//
//            username = view.findViewById(R.id.username);
//            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
//
//            if (currentUser != null){
//                String userId = currentUser.getUid();
//                databaseReference.child(userId).get().addOnSuccessListener(snapShot -> {
//                    User user = snapShot.getValue(User.class);
//
//                    if (user != null){
//                        username.setText("Welcome! " + user.getUsername());
//                    }
//
//                }).addOnFailureListener(e -> {
//                    Toast.makeText(getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//
//            }
//
//        }

    }

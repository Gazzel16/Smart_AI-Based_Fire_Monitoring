    package com.example.smartai_basedfiremonitoring.Fragments.UserFragments;

    import android.graphics.Color;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.LinearLayout;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;

    import com.example.smartai_basedfiremonitoring.Model.User;
    import com.example.smartai_basedfiremonitoring.R;
    import com.example.smartai_basedfiremonitoring.Sensors.FlameSensor;
    import com.example.smartai_basedfiremonitoring.Sensors.HumidSensor;
    import com.example.smartai_basedfiremonitoring.Sensors.SmokeSensor;
    import com.example.smartai_basedfiremonitoring.Sensors.TempSensor;
    import com.github.mikephil.charting.charts.LineChart;
    import com.github.mikephil.charting.components.XAxis;
    import com.github.mikephil.charting.components.YAxis;
    import com.github.mikephil.charting.data.Entry;
    import com.github.mikephil.charting.data.LineData;
    import com.github.mikephil.charting.data.LineDataSet;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;

    public class UserSensorDashboardFragment extends Fragment {

        TextView timeFireDetected, flameDetector,tempAnalogOutput,
                tempStatus, humidAnalogOutput, humidStatus, smokeOutput, smokeStatus, username;

        LineChart tempLineChart, humidLineChart, smokeLineChart;

        LinearLayout isUnderMaintenance;
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
            smokeLineChart = view.findViewById(R.id.smokeLineChart);

            FlameSensor.flameMonitoring(timeFireDetected, flameDetector, this);
            TempSensor.tempMonitoring(tempAnalogOutput, tempStatus, tempLineChart, this);
            HumidSensor.humidMonitoring(humidAnalogOutput, humidStatus, humidLineChart, this);
            SmokeSensor.smokeSensor(smokeOutput, smokeStatus, smokeLineChart, this);

            isUnderMaintenance = view.findViewById(R.id.isUnderMaintenance);
            DatabaseReference maintenanceRef =
                    FirebaseDatabase.getInstance().getReference("MaintenanceLogs")
                            .child("isMaintenance")
                            .child("maintenance");

            maintenanceRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean state = snapshot.getValue(Boolean.class);

                    if (state != null && state) {
                        // TRUE → show box
                        isUnderMaintenance.setVisibility(View.VISIBLE);
                    } else {
                        // FALSE → hide the box
                        isUnderMaintenance.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

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


    }

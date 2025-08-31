    package com.example.smartai_basedfiremonitoring.Fragments.UserFragments;

    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;

    import com.example.smartai_basedfiremonitoring.R;
    import com.example.smartai_basedfiremonitoring.Sensors.FlameSensor;
    import com.example.smartai_basedfiremonitoring.Sensors.HumidSensor;
    import com.example.smartai_basedfiremonitoring.Sensors.TempSensor;
    import com.google.firebase.database.ValueEventListener;

    public class UserSensorDashboardFragment extends Fragment {

        TextView timeFireDetected, flameDetector,tempAnalogOutput,
                tempStatus, humidAnalogOutput, humidStatus, smokeOutput, smokeStatus;

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

            FlameSensor.flameMonitoring(timeFireDetected, flameDetector, this);
            TempSensor.tempMonitoring(tempAnalogOutput, tempStatus, this);
            HumidSensor.humidMonitoring(humidAnalogOutput, humidStatus, this);

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

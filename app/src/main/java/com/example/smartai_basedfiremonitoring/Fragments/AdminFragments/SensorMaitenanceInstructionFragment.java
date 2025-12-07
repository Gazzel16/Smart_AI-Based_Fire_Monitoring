package com.example.smartai_basedfiremonitoring.Fragments.AdminFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smartai_basedfiremonitoring.Adapter.SensorMaintenanceInstructionAdapter;
import com.example.smartai_basedfiremonitoring.Model.SensorMaintenanceInstructionModel;
import com.example.smartai_basedfiremonitoring.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SensorMaitenanceInstructionFragment extends Fragment {

    private RecyclerView rvInstructions;
    private TextView translateLanguage;
    private SensorMaintenanceInstructionAdapter adapter;
    private List<SensorMaintenanceInstructionModel> instructionList;

    public SensorMaitenanceInstructionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sensor_maintenance_instruction, container, false);

        translateLanguage = view.findViewById(R.id.translateLanguage);

        rvInstructions = view.findViewById(R.id.rvInstructions);
        rvInstructions.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadInstructions();

        adapter = new SensorMaintenanceInstructionAdapter(instructionList);
        rvInstructions.setAdapter(adapter);

        // Toggle language when button is clicked
        translateLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.toggleLanguage();

                // Change button text to indicate the next language
                if (adapter.isTagalog()) {
                    translateLanguage.setText("English"); // Next click will switch to English
                } else {
                    translateLanguage.setText("Tagalog"); // Next click will switch to Tagalog
                }
            }
        });

        return view;
    }

    private void loadInstructions() {
        instructionList = new ArrayList<>();

        instructionList.add(
                new SensorMaintenanceInstructionModel(
                        "KY026 Flame Sensor",
                        Arrays.asList(
                                "1. Clean the infrared lens using a microfiber cloth",
                                "2. Ensure no dust blocks the flame sensor opening",
                                "3. Keep wires tightened and no corrosion",
                                "4. Test sensor using a lighter (briefly from a distance)"
                        ),
                        Arrays.asList(
                                "1. Linisin ang infrared lens gamit ang microfiber cloth",
                                "2. Siguraduhin walang alikabok na nakaharang sa flame sensor",
                                "3. Higpitan ang mga wires at iwasan ang corrosion",
                                "4. Subukan ang sensor gamit ang lighter (maikli lang mula sa distansya)"
                        ),
                        R.drawable.flame_sensor_ic
                )
        );

        instructionList.add(
                new SensorMaintenanceInstructionModel(
                        "MQ2 Gas / Smoke Sensor",
                        Arrays.asList(
                                "1. Clean mesh with a soft brush (avoid liquids)",
                                "2. Ensure heater coil warms up properly",
                                "3. Avoid exposing it to water or oil"
                        ),
                        Arrays.asList(
                                "1. Linisin ang mesh gamit ang malambot na brush (iwasan ang likido)",
                                "2. Siguraduhin na umiinit nang maayos ang heater coil",
                                "3. Iwasang ma-expose sa tubig o langis"
                        ),
                        R.drawable.smoke_sensor_ic
                )
        );

        instructionList.add(
                new SensorMaintenanceInstructionModel(
                        "DHT22 Temperature & Humidity Sensor",
                        Arrays.asList(
                                "1. Wipe sensor grill gently using dry cloth",
                                "2. Do not expose to direct water splashes",
                                "3. Keep humidity stable for accurate reading"
                        ),
                        Arrays.asList(
                                "1. Punasan ang sensor grill gamit ang tuyong tela",
                                "2. Huwag ilantad sa direktang patak ng tubig",
                                "3. Panatilihin ang stable na humidity para tumpak na reading"
                        ),
                        R.drawable.dht22_ic
                )
        );

        instructionList.add(
                new SensorMaintenanceInstructionModel(
                        "ESP32 Board",
                        Arrays.asList(
                                "1. Use stable 3.3V–5V power supply",
                                "2. Keep wires firm and solder joints clean",
                                "3. Avoid dust and moisture"
                        ),
                        Arrays.asList(
                                "1. Gumamit ng stable na 3.3V–5V power supply",
                                "2. Higpitan ang mga wires at panatilihing malinis ang solder joints",
                                "3. Iwasan ang alikabok at kahalumigmigan"
                        ),
                        R.drawable.esp32_ic
                )
        );
    }


    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.VISIBLE);
        }
    }
}

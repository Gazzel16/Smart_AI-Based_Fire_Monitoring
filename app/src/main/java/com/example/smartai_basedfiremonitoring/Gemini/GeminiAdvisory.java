package com.example.smartai_basedfiremonitoring.Gemini;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeminiAdvisory {

    private static boolean isDataLoaded = false;
    private static GeminiAdvisoryDialog advisoryDialog;
    public static boolean getIsDataLoaded() {
        return isDataLoaded;
    }
    public static void  geminiAdvisory(Context context, GeminiAdvisoryDialog geminiAdvisoryDialog){
        advisoryDialog = geminiAdvisoryDialog;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sensors");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Double temp = snapshot.child("temperature").getValue(Double.class);
                    Double humid = snapshot.child("humidity").getValue(Double.class);

                    if (temp != null && humid != null) {
                        isDataLoaded = true;
                        // Call backend after data is loaded
                        new GenerateAdvisoryTask(advisoryDialog).execute(temp.intValue(), humid.intValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read data: " + error.getMessage());
            }
        });

    }

    private static class GenerateAdvisoryTask extends AsyncTask<Integer, Void, String> {

        private final GeminiAdvisoryDialog dialog;

        public GenerateAdvisoryTask(GeminiAdvisoryDialog dialog){
            this.dialog = dialog;
        }

        @Override
        protected String doInBackground(Integer... params) {

            if (params.length < 2) {
                return "Error: Missing temperature or humidity value.";
            }


            try {

                int temperature = params[0];
                int humidity = params[1];

                URL url = new URL("http://192.168.1.5:5000/generate-advisory");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("temperature", temperature);
                jsonParam.put("humidity", humidity);

                OutputStream os = conn.getOutputStream();
                os.write(jsonParam.toString().getBytes("UTF-8"));
                os.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                conn.disconnect();

                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getString("advisory");

            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String advisory){
            if (dialog != null){

                GeminiAdvisoryModel model = new GeminiAdvisoryModel("Advisory", advisory);
                dialog.addAdvisory(model);
            }
        }

    }
}

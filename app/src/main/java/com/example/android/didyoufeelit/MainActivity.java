/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.didyoufeelit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.style.UpdateAppearance;
import android.widget.TextView;

import static com.example.android.didyoufeelit.Utils.fetchEarthquakeData;

/**
 * Displays the perceived strength of a single earthquake event based on responses from people who
 * felt the earthquake.
 */
public class MainActivity extends AppCompatActivity {

    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*// Perform the HTTP request for earthquake data and process the response.
        Event earthquake = fetchEarthquakeData(USGS_REQUEST_URL);

        // Update the information displayed to the user.
        updateUi(earthquake);*/

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);

    }

    /**
     * Update the UI with the given earthquake information.
     */
    private void updateUi(Event earthquake) {
        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(earthquake.title);

        TextView tsunamiTextView = (TextView) findViewById(R.id.number_of_people);
        tsunamiTextView.setText(getString(R.string.num_people_felt_it, earthquake.numOfPeople));

        TextView magnitudeTextView = (TextView) findViewById(R.id.perceived_magnitude);
        magnitudeTextView.setText(earthquake.perceivedStrength);
    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, Event>{
        /**
         * 此方法在后台线程上激活（调用），因此我们可以执行
         * 诸如做出网络请求等长时间运行操作。
         *
         * 因为不能从后台线程更新 UI，所以我们仅返回
         * {@link Event} 对象作为结果。
         */
        @Override
        protected Event doInBackground(String... urls) {
            // 如果不存在任何 URL 或第一个 URL 为空，切勿执行请求。
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            Event result = Utils.fetchEarthquakeData(urls[0]);
            return result;
        }
        /**
         * 此方法是在完成后台工作后，在主 UI 线程上
         * 激活的。
         *
         * 可以在此方法内修改 UI。我们得到 {@link Event} 对象
         * （该对象从 doInBackground() 方法返回），并更新屏幕上的视图。
         */
        @Override
        protected void onPostExecute(Event result) {
            // 如果不存在任何结果，则不执行任何操作。
            if (result == null) {
                return;
            }

            /*super.onPostExecute(event);*/
            updateUi(result);
        }
    }
}

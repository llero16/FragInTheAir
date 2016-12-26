package com.example.llotis.fragintheair;


import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class MainFragment extends Fragment implements View.OnClickListener{
    private static final String API_KEY = BuildConfig.API_KEY;
    onChangeMadeListener mCallback;

    AutoCompleteTextView departureText = null, arrivalText = null;
    String departureString, arrivalString;

    int depFlag, arrFlag = 1;

    Button go;

    List<String> labels = new ArrayList<String>();
    List<String> values = new ArrayList<String>();
    ArrayAdapter adapter ;

    String urlString = "";

    LinearLayout layout;

    public interface onChangeMadeListener {
        public void onChangeMade(int id, String value);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_fragment, null);

        departureText = (AutoCompleteTextView)view.findViewById(R.id.departure_airport_editText);
        arrivalText = (AutoCompleteTextView)view.findViewById(R.id.arrival_airport_editText);
        go = (Button)view.findViewById(R.id.search_button);
        go.setOnClickListener(this);


        /*  Δοκιμάζω το onFocusChangeListener, δουλεύει
        arrivalText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus)
                    mCallback.onChangeMade(2, arrivalText.getText().toString());
            }
        });
        */

        //kanw to auto complete gia to departure text

        departureText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                depFlag = 1;
            }
        });

        departureText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(depFlag == 1) {
                    departureString = departureText.getText().toString();
                    AsyncTask<String, Integer, String> klhshAsyncTask = new FetchAutocompletedAirportTask().execute(departureString);
                    adapter = new
                            ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, labels);
                    try {
                        klhshAsyncTask.get(1000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }

                    departureText.setAdapter(adapter);
                    //posous xarakthres 8a grapseis mexri na arxisei na proteinei strings, 1-> apo to prwto gramma pou grafeis, duhhh
                    departureText.setThreshold(1);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        arrivalText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                arrFlag = 1;
            }
        });
        //kanw to auto complete gia to arrival text
        arrivalText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (arrFlag == 1) {
                    arrivalString = arrivalText.getText().toString();
                    AsyncTask<String, Integer, String> klhshAsyncTask = new FetchAutocompletedAirportTask().execute(arrivalString);
                    adapter = new
                            ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, labels);
                    try {
                        klhshAsyncTask.get(1000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }

                    arrivalText.setAdapter(adapter);
                    arrivalText.setThreshold(1);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        layout = (LinearLayout)view.findViewById(R.id.layout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(labels.contains(departureString)) {
                    departureText.setText(values.get(labels.indexOf(departureString)));
                    departureText.clearFocus();
                    depFlag = 0;
                    //gia kapoio logo de douleuei to concat kai eprepe na to kanw mpakalika me to urlString += blah blah
                    //urlString.concat("&origin=" + values.get(labels.indexOf(departureString)));
                    //urlString += "&origin=" + values.get(labels.indexOf(departureString));
                    //mCallback.onChangeMade(1, values.get(labels.indexOf(departureString)));
                }
                if(labels.contains(arrivalString)) {
                    arrivalText.setText(values.get(labels.indexOf(arrivalString)));
                    arrivalText.clearFocus();
                    arrFlag = 0;
                    //urlString += "&destination=" + values.get(labels.indexOf(arrivalString));
                    //mCallback.onChangeMade(2, values.get(labels.indexOf(arrivalString)));
                }

                Toast.makeText(getContext(), "urlString: " + urlString, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public class FetchAutocompletedAirportTask extends AsyncTask<String,Integer,String> {

        private String getDataFromJson(String AutocompleteJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String value = "value";
            final String label = "label";

            JSONArray parentArray = new JSONArray(AutocompleteJsonStr);

            StringBuffer finalStringBuffer = new StringBuffer();

            //tis adeiazw ka8e fora prin na tis gemisw gia na diagrafoun oi times apo thn prohgoumenh klhsh sto idio session

            for(int i=0; i<parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);

                String objectValue = finalObject.getString(value);
                String objectLabel = finalObject.getString(label);
                finalStringBuffer.append(objectValue + " - " + objectLabel + "\n");
                Log.v("*********", objectValue + " - " + objectLabel + "\n");

                if(!labels.contains(objectLabel))
                    labels.add(i, objectLabel);

                //8a mporousa na to kanw sthn epanw if, think about it
                if(!values.contains(objectValue))
                    values.add(i, objectValue);

            }
            Log.v("*********", "Megethos labels: " + labels.size());
            return finalStringBuffer.toString();

        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String autocompleteJsonStr = null;

            try {
                final String AIRPORT_AUTOCOMPLETE_BASE_URL = "https://api.sandbox.amadeus.com/v1.2/airports/autocomplete?apikey=" + API_KEY ;
                final String TERM = "term";

                Uri airportAutocompleteBuiltUri = Uri.parse(AIRPORT_AUTOCOMPLETE_BASE_URL)
                        .buildUpon()
                        .appendQueryParameter(TERM, strings[0])
                        .build();
                Log.v("****", "departure location string: " + strings[0]);

                URL autocompleteStringUrl = new URL(airportAutocompleteBuiltUri.toString());

                Log.v("*********", "Built URI: " + airportAutocompleteBuiltUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) autocompleteStringUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                autocompleteJsonStr = buffer.toString();
                Log.v("****", "JSON String: " + autocompleteJsonStr);
            } catch (IOException e) {
                Log.e("****", "Error ", e);
                // If the code didn't successfully get the data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("****", "Error closing stream", e);
                    }
                }
            }
            try {
                return getDataFromJson(autocompleteJsonStr);
            } catch (JSONException e) {
                Log.e("**********", e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);
            //ShowAlertDialogWithListview();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.search_button:
                mCallback.onChangeMade(1, departureText.getText().toString());
                mCallback.onChangeMade(2, arrivalText.getText().toString());
                mCallback.onChangeMade(3, null);
                break;

        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (onChangeMadeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


}

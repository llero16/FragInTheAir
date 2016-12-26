package com.example.llotis.fragintheair;

import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

public class MainActivity extends FragmentActivity implements MainFragment.onChangeMadeListener {

    private static final String API_KEY = BuildConfig.API_KEY;
    String LOG_TAG = "MY ACTIVITY LOG";

    String ORIGIN_PARAM = "";
    String DESTINATION_PARAM = "";

    String[] params;
    String info;

    int bufferLength;



    ArrayList<FlightObject> flightObjects = new ArrayList<FlightObject>();
    AsyncTask<String, Integer, String> klhshAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources resources = getResources();
        params = resources.getStringArray(R.array.string_array_name);

        MainFragment mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main, mainFragment, null).commit();



    }

    @Override
    public void onChangeMade(int id, String value) {

        switch (id) {
            case 1:
                ORIGIN_PARAM = value;
                Toast.makeText(getApplicationContext(), "ORIGIN_PARAM " + ORIGIN_PARAM, Toast.LENGTH_LONG).show();
                break;
            case 2:
                DESTINATION_PARAM = value;
                break;
            case 3:
                klhshAsyncTask = new FetchInfo().execute(null, null, info);
//                try {
//                    klhshAsyncTask.get(0, TimeUnit.MILLISECONDS);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (TimeoutException e) {
//                    e.printStackTrace();
//                }
                break;
        }



    }

    //aaaaaaaall the network operations will take place hear, dld AsyncTask, testing da network
    //kai outw ka8'ekshs

    public class FetchInfo extends AsyncTask<String,Integer,String> {

        private String getDataFromJson(String AutocompleteJsonStr)
                throws JSONException {




            JSONObject parentObject = new JSONObject(AutocompleteJsonStr);
            JSONArray resultsArray = parentObject.getJSONArray("results");

            StringBuffer finalStringBuffer = new StringBuffer();
            finalStringBuffer.append(resultsArray.length());
            //to currency einai idio gia ola ta apotelesmata opote de xreiazetai na to vazw ka8e fora,
            //mporei na einai la8os kai pou to exw san property tou FlightObject
            // Toast.makeText(getApplicationContext(), "results length bitches" + resultsArray.length(), Toast.LENGTH_LONG).show();



            //shmantikh info :
            /*
            -o pinakas results mporei na exei mexri     251 apotelesmata
            -o pinakas itineraries mporei na exei mexri 251 apotelesmata
            -o pinakas flights mporei na exei mexti       5 apotelesmata
            -ta flights einai auto pou lene ta legs(podia) ths pthshs


             */
            Log.d(LOG_TAG, "resultsArray length eksw apo th for = " + Integer.toString(resultsArray.length()));
            for(int i=0; i<resultsArray.length(); i++) {

//                Log.d(LOG_TAG, "resultsArray length = " + resultsArray.length());
//                Log.d(LOG_TAG, "i = " + i);
//                Log.d(LOG_TAG, "flightObjects.size = " + flightObjects.size());
                flightObjects.add(new FlightObject());
                flightObjects.get(i).setCurrency(parentObject.getString("currency"));

                JSONObject resultObject = resultsArray.getJSONObject(i);

                JSONArray itinerariesArray = resultObject.getJSONArray("itineraries");
                JSONObject itineraryObject = itinerariesArray.getJSONObject(0);
                JSONObject boundParentObject = itineraryObject.getJSONObject("outbound");

                JSONArray flightsArray = boundParentObject.getJSONArray("flights");

                JSONObject flightObject = flightsArray.getJSONObject(0);
                String departsAt = flightObject.getString("departs_at");
                String arrivesAt = flightObject.getString("arrives_at");
                flightObjects.get(i).setDepartsAt(departsAt);
                flightObjects.get(i).setArrivesAt(arrivesAt);

                JSONObject originObject = flightObject.getJSONObject("origin");
                String originAirport = originObject.getString("airport");
                String originTerminal = null;
                try {
                    originTerminal = originObject.getString("terminal");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                flightObjects.get(i).setOriginAirport(originAirport);
                flightObjects.get(i).setOriginTerminal(originTerminal);

                JSONObject destinationObject = flightObject.getJSONObject("destination");
                String destinationAirport = destinationObject.getString("airport");
                flightObjects.get(i).setDestinationAirport(destinationAirport);
                String destinationMarketingAirline = null;
                try {
                    destinationMarketingAirline = flightObject.getString("marketing_airline");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String destinationOperatingAirline = null;
                try {
                    destinationOperatingAirline = flightObject.getString("operating_airline");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String destinationFlightNumber = null;
                try {
                    destinationFlightNumber = flightObject.getString("flight_number");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String destinationAircraft = null;
                try {
                    destinationAircraft = flightObject.getString("aircraft");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                flightObjects.get(i).setDestinationMarketingAirline(destinationMarketingAirline);
                flightObjects.get(i).setDestinationOperatingAirline(destinationOperatingAirline);
                flightObjects.get(i).setDestinationFlightNumber(destinationFlightNumber);
                flightObjects.get(i).setDestinationAircraft(destinationAircraft);

                JSONObject bookingInfoObject = flightObject.getJSONObject("booking_info");
                String bookingInfoTravelClass = bookingInfoObject.getString("travel_class");
                String bookingInfoBookingCode = bookingInfoObject.getString("booking_code");
                String bookingInfoSeatsRemaining = bookingInfoObject.getString("seats_remaining");
                flightObjects.get(i).setBookingInfoTravelClass(bookingInfoTravelClass);
                flightObjects.get(i).setBookingInfoBookingCode(bookingInfoBookingCode);
                flightObjects.get(i).setBookingInfoSeatsRemaining(bookingInfoSeatsRemaining);

                JSONObject fareObject = resultObject.getJSONObject("fare");

                String totalPrice = fareObject.getString("total_price");
                flightObjects.get(i).setTotalPrice(totalPrice);

                JSONObject pricePerAdultObject = fareObject.getJSONObject("price_per_adult");
                String adultTotalFare = pricePerAdultObject.getString("total_fare");
                String adultTax = pricePerAdultObject.getString("tax");
                flightObjects.get(i).setAdultTotalFare(adultTotalFare);
                flightObjects.get(i).setAdultTax(adultTax);

                JSONObject restrictionObject = fareObject.getJSONObject("restrictions");
                Boolean refundableBoolean = restrictionObject.getBoolean("refundable");
                Boolean changePenaltiesBoolean = restrictionObject.getBoolean("change_penalties");
                flightObjects.get(i).setRefundableBoolean(refundableBoolean);
                flightObjects.get(i).setChangePenaltiesBoolean(changePenaltiesBoolean);


                finalStringBuffer.append("\n\n" +
                        "result :                     " + i + "\n" +
                        "departs_at:                  " + departsAt + "\n" +
                        "arrives_at:                  " + arrivesAt + "\n" +
                        "originAirport:               " + originAirport.toString() + "\n" +
                        "originTerminal:              " + originTerminal + "\n" +
                        "destinationAirport:          " + destinationAirport + "\n" +
                        "destinationMarketingAirline: " + destinationMarketingAirline + "\n" +
                        "destinationOperatingAirline: " + destinationOperatingAirline + "\n" +
                        "destinationFlightNumber:     " + destinationFlightNumber + "\n" +
                        "destinationAircraft:         " + destinationAircraft + "\n" +
                        "bookingInfoTravelClass:      " + bookingInfoTravelClass + "\n" +
                        "bookingInfoBookingCode:      " + bookingInfoBookingCode + "\n" +
                        "bookingInfoSeatsRemaining:   " + bookingInfoSeatsRemaining + "\n" +
                        "adultTotalFare:              " + adultTotalFare + "\n" +
                        "refundableBoolean:           " + refundableBoolean + "\n" +
                        "changePenaltiesBoolean:      " + changePenaltiesBoolean);



//            String objectValue = finalObject.getString(value);
//            String objectLabel = finalObject.getString(label);
//            finalStringBuffer.append(objectValue + " - " + objectLabel + "\n");


//                if(!labels.contains(objectLabel))
//                    labels.add(i, objectLabel);

                //8a mporousa na to kanw sthn epanw if, think about it
//                if(!values.contains(objectValue))
//                    values.add(i, objectValue);


                //tis adeiazw ka8e fora prin na tis gemisw gia na diagrafoun oi times apo thn prohgoumenh klhsh sto idio session

//            for(int i=0; i<resultsArray.length(); i++) {
//
//                JSONObject finalObject = resultsArray.getJSONObject(i);
//                flightObjects.add(new FlightObject());
//                flightObjects.get(i);
//
//                String objectValue = finalObject.getString(value);
//                String objectLabel = finalObject.getString(label);
//                finalStringBuffer.append(objectValue + " - " + objectLabel + "\n");
//                Log.v("*********", objectValue + " - " + objectLabel + "\n");
//
////                if(!labels.contains(objectLabel))
////                    labels.add(i, objectLabel);
//
//                //8a mporousa na to kanw sthn epanw if, think about it
////                if(!values.contains(objectValue))
////                    values.add(i, objectValue);
//
//            }


            }
            Log.e(LOG_TAG, " final string buffer: " + finalStringBuffer.toString());
            return finalStringBuffer.toString();

        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String jsonString = null;

            try {
                final String BASE_URL = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?apikey=" +API_KEY;

                Uri uri = Uri.parse(BASE_URL)
                        .buildUpon()
                        .appendQueryParameter("origin", "LHR")
                        .appendQueryParameter("destination", "ATH")
                        .appendQueryParameter("departure_date", "2017-01-01")
                        .appendQueryParameter("currency", "EUR")
                        .appendQueryParameter("nonstop","true")
                        .build();

                URL url = new URL(uri.toString());

                Log.v(LOG_TAG, "Built URI: " + uri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
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
                /*while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }*/



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

                jsonString = buffer.toString();
                Log.v(LOG_TAG, "JSON String: ΛΛΕΡΑ" + jsonString);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
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
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getDataFromJson(jsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
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
            //Toast.makeText(getApplicationContext(), "*****on post execute****" + strings, Toast.LENGTH_LONG).show();
            //Log.e(LOG_TAG, "ON POST EXECUTE " + strings);
            //ShowAlertDialogWithListview();

            // edw ta stelnei sto ResultsFragment kai emfanizontai ola mazi se ena textView,
            //douleuei kanonika alla den einai auto pou 8elw

/*
            ResultsFragment resultsFragment = new ResultsFragment();

            Bundle args = new Bundle();
            args.putString("value", strings);
            resultsFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.activity_main, resultsFragment);
            transaction.addToBackStack(null);

            transaction.commit();
*/

            //edw tha prospa8hsw na steilw thn ArrayList me ta flightObjects pou gemisa enw
            // ekana parsing thn apokrish tou JSON

            MyListFragment listFragment = new MyListFragment();
            Bundle listArgs = new Bundle();
            //epeidh 8elw na steilw ArrayList<Object> eprepe na kanw implements parceble
            //to FlightObject mou kai na xrhsimopoihsw putParcelableArrayList/getParcelableArrayList
            //gia thn antallagh dedomenwn apo activity se fragment
            listArgs.putParcelableArrayList("flight objects", flightObjects);
            listFragment.setArguments(listArgs);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.activity_main, listFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}

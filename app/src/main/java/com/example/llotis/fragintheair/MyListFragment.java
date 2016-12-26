package com.example.llotis.fragintheair;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 19/12/2016.
 */

public class MyListFragment extends ListFragment {
    String LOG_TAG = "TTTAG LOG";

    public ArrayList<FlightObject> flightObjects = new ArrayList<FlightObject>();

    TextView text, text2;
    ListView list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        //list = (ListView)view.findViewById(android.R.id.list);
        Log.i(LOG_TAG, "onCreateView");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        flightObjects = args.getParcelableArrayList("flight objects");
        Log.i(LOG_TAG, "onStart");
        if(1 == 1) {
            ListAdapter adapter = new CustomAdapter(getActivity(), flightObjects);
            //list.setAdapter(adapter);
            setListAdapter(adapter);
            Log.i(LOG_TAG, "1 == 1");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(LOG_TAG, "onActivityCreated");


    }


/*
    public class CustomAdapter extends ArrayAdapter<FlightObject> {

        public CustomAdapter(Context context, ArrayList<FlightObject> objects) {
            super(context, 0, objects);
            Log.i(LOG_TAG, "constructor tou adapter");
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i(LOG_TAG, "getView");
            // Get the data item for this position
            FlightObject object = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                Log.i(LOG_TAG, "mphke sthn if");
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_row_xml, parent, false);
            }

            // Populate the data into the template view using the data object
            text.setText(object.arrivesAt);
            text2.setText(object.totalPrice);
            // Return the completed view to render on screen
            return convertView;
        }
    }
    */
}






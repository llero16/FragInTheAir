package com.example.llotis.fragintheair;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Admin on 16/12/2016.
 */

public class ResultsFragment extends Fragment{

    TextView text, text2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.results_fragment, null);
        text = (TextView) view.findViewById(R.id.text);
        text2 = (TextView) view.findViewById(R.id.departureTime_textview);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        updateArticleView(args.getString("value"));


    }

    public void updateArticleView(String url) {
        text.setText(url);
    }
}

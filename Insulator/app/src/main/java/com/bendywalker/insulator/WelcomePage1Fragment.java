package com.bendywalker.insulator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


public class WelcomePage1Fragment extends Fragment {
    Button continueButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_page_1, container, false);

        continueButton = (Button) view.findViewById(R.id.button_continue);
        continueButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((WelcomeActivity) getActivity()).goForwardToFragment(new WelcomePage2Fragment());
            }
        });

        return view;
    }
}

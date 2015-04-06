package com.bendywalker.insulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class WelcomePage2Fragment extends Fragment {
    CardView readyToUseCard;
    MenuItem useInsulatorButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_page_2, container, false);
        setHasOptionsMenu(true);

        readyToUseCard = (CardView) view.findViewById(R.id.card_ready_to_use);

        MyPreferenceManager preferenceManager = new MyPreferenceManager(getActivity());
        preferenceManager.setIsFirstTimeOpen(false);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.welcome_2, menu);
        useInsulatorButton = menu.findItem(R.id.action_use_insulator);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_use_insulator:
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
        }

        return true;
    }
}

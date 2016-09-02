package org.hackillinois.app2017.Announcement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.hackillinois.app2017.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class AnnouncementListFragment extends Fragment {

    private ArrayList<Announcement> announcements =  new ArrayList<>();
    private AnnouncementAdapter adapter;

    @BindView(R.id.announcementList) RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_announcements, parent, false);
        ButterKnife.bind(this, view);

        Spinner filterMenu = (Spinner) view.findViewById(R.id.filterMenu);
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.filters, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterMenu.setAdapter(filterAdapter);

        adapter = new AnnouncementAdapter(announcements);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        testData();

        return view;
    }

    private void testData(){
        Announcement announcement = new Announcement("Yoooo", "This is a test message bruh");
        announcements.add(announcement);

        announcement = new Announcement("Food", "Dinner in ECEB very soon", 0);
        announcements.add(announcement);

        announcement = new Announcement("Yoooo", "This is a test message bruh");
        announcements.add(announcement);

        announcement = new Announcement("Yoooo", "This is a test message bruh");
        announcements.add(announcement);

        announcement = new Announcement("Yoooo", "This is a test message bruh");
        announcements.add(announcement);

        adapter.notifyDataSetChanged();
    }
}

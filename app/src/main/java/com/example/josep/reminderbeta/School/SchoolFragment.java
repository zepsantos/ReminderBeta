package com.example.josep.reminderbeta.School;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.josep.reminderbeta.R;


/**
 * Vai buscar a informação
 */
public class SchoolFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public SchoolFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_school, container, false);


    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


    }


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new SchoolOne(), "ONE");
        adapter.addFragment(new SchoolTwo(), "TWO");
        adapter.addFragment(new SchoolThree(), "THREE");
        viewPager.setAdapter(adapter);
    }

}

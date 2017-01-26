package com.parse.starter.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.starter.R;
import com.parse.starter.adapter.SearchAdapter;
import com.parse.starter.adapter.TimeLineAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeLineFragment extends Fragment {

    private ArrayList<ParseObject> posts;
    private ArrayAdapter<ParseObject> adapter;
    private ParseQuery query;
    private ListView listView;


    public TimeLineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //define o design do fragment
        View view = inflater.inflate(R.layout.fragment_time_line, container, false);

        // instancia o array e o adapter e seta para a list view

        posts = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.listView_Timeline);
        adapter = new TimeLineAdapter(getActivity(), posts);
        listView.setAdapter(adapter);
        // recupera as postagens
        getPostagens();

        return view;
    }

    private void getPostagens() {

        /*
         Recupera csas
        */
        query = ParseQuery.getQuery("TIMELINE");
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {//sucesso

                    if (objects.size() > 0) {
                        posts.clear();
                        for (ParseObject parseObject : objects) {
                            posts.add(parseObject);
                        }
                        adapter.notifyDataSetChanged();
                    }

                } else {//erro
                    e.printStackTrace();
                }

            }
        });

    }

}

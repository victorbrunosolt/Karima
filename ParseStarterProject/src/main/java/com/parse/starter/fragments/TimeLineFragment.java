package com.parse.starter.fragments;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.R;
import com.parse.starter.activity.CsaDetalhes;
import com.parse.starter.adapter.SearchAdapter;
import com.parse.starter.adapter.TimeLineAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeLineFragment extends Fragment {
    private RecyclerView mRecyclerViewTimeLine;
    private ArrayList<ParseObject> csas;
    private ArrayList<ParseUser> usuarios;
    private TimeLineAdapter adapter;
    private ParseQuery query;

    public TimeLineFragment() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_line, container, false);

        /*
         Montar GridView e adapter
        */
        csas = new ArrayList<>();
        usuarios = new ArrayList<>();
        mRecyclerViewTimeLine = (RecyclerView) view.findViewById(R.id.rv_time_line);
        adapter = new TimeLineAdapter(getActivity(), csas, usuarios);
        mRecyclerViewTimeLine.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewTimeLine.setLayoutManager(llm);

        //recupera postagens
        getPostagens();


        return view;
    }



    private void getPostagens() {

        /*
         Recupera csas
        */
        query = ParseQuery.getQuery("TIMELINE");
        query.orderByDescending("createdAt");
        query.include("USUARIO");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {//sucesso

                    if (objects.size() > 0) {
                        csas.clear();
                        for (ParseObject parseObject : objects) {
                            ParseUser usuario = parseObject.getParseUser("USUARIO");
                            usuarios.add(usuario);
                            csas.add(parseObject);

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

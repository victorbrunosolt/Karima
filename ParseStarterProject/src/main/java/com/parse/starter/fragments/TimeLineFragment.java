package com.parse.starter.fragments;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;

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
    private ListView listView;
    private ArrayList<ParseObject> csas;
    private ArrayAdapter<ParseObject> adapter;
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
        listView = (ListView) view.findViewById(R.id.listView_Timeline);
        adapter = new TimeLineAdapter(getActivity(), csas);
        listView.setAdapter(adapter);

        //recupera postagens
        getPostagens();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ParseObject parseObject = csas.get(i);
                // envia dados para o activity detalhes da csa
                Intent intent = new Intent(getActivity(), CsaDetalhes.class);
                String objectId = parseObject.getObjectId();
                intent.putExtra("objectId", objectId);


                startActivity(intent);



            }
        });

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
                        csas.clear();
                        for (ParseObject parseObject : objects) {
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

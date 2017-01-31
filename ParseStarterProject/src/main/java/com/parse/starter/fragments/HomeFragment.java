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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.starter.R;
import com.parse.starter.activity.CsaDetalhes;
import com.parse.starter.adapter.SearchAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private GridView gridView;
    private ArrayList<ParseObject> csas;
    private ArrayAdapter<ParseObject> adapter;
    private ParseQuery query;

    public HomeFragment() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /*
         Montar GridView e adapter
        */
        csas = new ArrayList<>();
        gridView = (GridView) view.findViewById(R.id.list_csa);
        adapter = new SearchAdapter(getActivity(), csas);
        gridView.setAdapter(adapter);

        //recupera postagens
          getPostagens();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        query = ParseQuery.getQuery("CSA");
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

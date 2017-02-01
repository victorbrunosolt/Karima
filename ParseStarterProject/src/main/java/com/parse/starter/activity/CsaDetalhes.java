package com.parse.starter.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.starter.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CsaDetalhes extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private Toolbar toolbar;
    private String objectId;
    private TextView textViewNome;
    private TextView textViewDescricao;
    private TextView textViewEndereco;
    private TextView textViewPreco;
    private TextView textViewCapacidade;
    private ImageView imagemViewFotoCsa;
    private ParseObject csa;
    private LocationManager lm;
    private Location location;
    private double longitude = -25.429675;
    private double latitude = -49.271870;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csa_detalhes);
        Intent intent = getIntent();
        objectId = intent.getStringExtra("objectId");
        getCsa();

        imagemViewFotoCsa = (ImageView) findViewById(R.id.imagem_fundo_Csa);
        textViewNome = (TextView) findViewById(R.id.nome_csa);
        textViewDescricao = (TextView) findViewById(R.id.descricao_csa);
        textViewEndereco = (TextView) findViewById(R.id.endereco_csa);
        textViewPreco = (TextView) findViewById(R.id.preco_csa);
        textViewCapacidade = (TextView) findViewById(R.id.capacidade_csa);


        // mapa

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Detalhes");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Detalhes CSA");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Local");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Local CSA");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Avalições");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Avalições CSA");
        host.addTab(spec);

    }

    private void getCsa() {

        /*
         Recupera csas
        */
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CSA");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {

                // caso csa diferente de nula
                if (e == null) {
                    csa = object;
                    //recuperando dados da csa
                    String nome = (String) csa.get("NOME");
                    String descricao = (String) csa.get("DESCRICAO");
                    String endereco = (String) csa.get("ENDERECO");
                    int preco = (int) csa.get("PRECO");
                    int capacidade = (int) csa.get("CAPACIDADE");


                    //setando os dados na tela

                    textViewNome.setText("Nome: " + nome);
                    textViewDescricao.setText("Descrição: " + descricao);
                    textViewEndereco.setText("Endereço: " + endereco);
                    textViewPreco.setText("Preço: " + Integer.toString(preco) + "Reais");
                    textViewCapacidade.setText("Capacidade: " + Integer.toString(capacidade));
                    Picasso.with(getApplicationContext())
                            .load(csa.getParseFile("IMAGEM").getUrl())
                            .fit()
                            .into(imagemViewFotoCsa);

                } else {
                    // something went wrong
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (lm != null) {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        map.setMyLocationEnabled(true);
        map.setTrafficEnabled(true);

        map.addMarker(new MarkerOptions().position(new LatLng(-25.443150, -49.238243)).title("Jardim Botânico"));
        map.addMarker(new MarkerOptions().position(new LatLng(-25.389681, -49.231168)).title("Parque Bacaheri"));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 11));
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}


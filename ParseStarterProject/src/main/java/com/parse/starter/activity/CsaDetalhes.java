package com.parse.starter.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

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



public class CsaDetalhes extends AppCompatActivity implements OnMapReadyCallback, LocationListener, DialogInterface{

    private String objectId;
    private TextView textViewNome;
    private TextView textViewDescricao;
    private TextView textViewEndereco;
    private TextView textViewPreco;
    private TextView textViewCapacidade;
    private ImageView imagemViewFotoCsa;
    private ParseObject csa;
    private LocationManager locationManager;
    private double latitudeCsa;
    private double longitudeCsa;
    private  GoogleMap map ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_csa_detalhes);
        Intent intent = getIntent();
        objectId = intent.getStringExtra("objectId");

        // metodos para recuperar dados da Activity

        criatabs();
        getComponentesTela();
        getCsa();
        // mapa

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    protected void onResume() {

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,this);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(getApplicationContext(), "GPS ATIVADO: ", Toast.LENGTH_SHORT).show();
        }else{
            showGPSDisabledAlertToUser();
        }



        super.onResume();
    }



    // ------------CSA --------------------

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
                    latitudeCsa =  csa.getParseGeoPoint("LOCALIZACAO").getLatitude();
                    longitudeCsa = csa.getParseGeoPoint("LOCALIZACAO").getLongitude();




                    //setando os dados na tela
                    map.addMarker(new MarkerOptions().position(new LatLng(latitudeCsa, longitudeCsa)).title(nome));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitudeCsa, longitudeCsa), 11));
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

    private void getComponentesTela(){

        imagemViewFotoCsa = (ImageView) findViewById(R.id.imagem_fundo_Csa);
        textViewNome = (TextView) findViewById(R.id.nome_csa);
        textViewDescricao = (TextView) findViewById(R.id.descricao_csa);
        textViewEndereco = (TextView) findViewById(R.id.endereco_csa);
        textViewPreco = (TextView) findViewById(R.id.preco_csa);
        textViewCapacidade = (TextView) findViewById(R.id.capacidade_csa);


    }


    //----------------------------------TABS----------------------------------------


    public void criatabs(){


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



    //---------------------------------MAPS-----------------------------------------

    @Override
    public void onMapReady(GoogleMap map) {

        this.map = map;

        try {
            map.setMyLocationEnabled(true);
            map.setTrafficEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);

        }catch (SecurityException ex){

            Toast.makeText(getApplicationContext(), "error: "+ex, Toast.LENGTH_SHORT).show();
        }



    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS está desativado deseja ativar?")
                .setCancelable(false)
                .setPositiveButton("ativar GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
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


    @Override
    public void cancel() {

    }

    @Override
    public void dismiss() {

    }


    //--------------------------Classe AsyncTask----------------------------------

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {


                int time = Integer.parseInt(params[0])*1000;

                Thread.sleep(5);
                resp = "Slept for " + params[0] + " seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CsaDetalhes.this,
                    "ProgressDialog",
                    "Wait for "+10+ " seconds");
        }


        @Override
        protected void onProgressUpdate(String... text) {
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
    }
}




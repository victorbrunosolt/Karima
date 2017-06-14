package com.parse.starter.activity;


import android.content.DialogInterface;
import android.content.Intent;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
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
import com.parse.starter.domain.CreditCard;
import com.parse.starter.domain.PaymentConnection;
import com.squareup.picasso.Picasso;

import java.util.Observable;
import java.util.Observer;

import cn.carbs.android.library.MDDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CsaDetalhes extends AppCompatActivity implements OnMapReadyCallback, LocationListener, DialogInterface, Observer{

    private Toolbar toolbarPrincipal;
    private String objectId;
    private TextView textViewNome;
    private TextView textViewDescricao;
    private TextView textViewEndereco;
    private TextView textViewPreco;
    private TextView textViewCapacidade;
    private ImageView imagemViewFotoCsa;
    private Button buttonAssinar;
    private ParseObject csa;
    private LocationManager locationManager;
    private double latitudeCsa;
    private double longitudeCsa;
    private  GoogleMap map ;
    private CollapsingToolbarLayout collapsingToolbar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_csa_detalhes);
        toolbarPrincipal = (Toolbar) findViewById(R.id.toolbar_detalhes);
        setSupportActionBar( toolbarPrincipal );
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
        buttonAssinar = (Button) findViewById(R.id.button_buy);


    }


    //----------------------------------TABS----------------------------------------


    public void criatabs(){


        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Detalhes");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Detalhes");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Local");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Local");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Avalições");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Avalições");
        host.addTab(spec);

        //Tab 4
        spec = host.newTabSpec("Fotos");
        spec.setContent(R.id.tab4);
        spec.setIndicator("Fotos");
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

    public void buy( View view ){
        new MDDialog.Builder(this)
                .setTitle("Pagamento")
                .setContentView(R.layout.payment)
                .setNegativeButton("Cancelar", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("Finalizar", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View root = v.getRootView();

                        buttonBuying( true );
                        CreditCard creditCard = new CreditCard(CsaDetalhes.this );
                        creditCard.setCardNumber( getViewContent( root, R.id.card_number ) );
                        creditCard.setName( getViewContent( root, R.id.name ) );
                        creditCard.setMonth( getViewContent( root, R.id.month ) );
                        creditCard.setYear( getViewContent( root, R.id.year ) );
                        creditCard.setCvv( getViewContent( root, R.id.cvv ) );
                        creditCard.setParcels( Integer.parseInt( getViewContent( root, R.id.parcels ) ) );

                        getPaymentToken( creditCard );
                    }
                })
                .create()
                .show();
    }

    private void buttonBuying( final boolean status ){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String label;

                label = getResources().getString(R.string.button_buy);
                if( status ){
                    label = getResources().getString(R.string.button_buying);
                }

                ((Button) findViewById(R.id.button_buy)).setText(label);
            }
        });
    }

    private String getViewContent( View root, int id ){
        EditText field = (EditText) root.findViewById(id);
        return field.getText().toString();
    }


    private void getPaymentToken( CreditCard creditCard ){
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled( true );
        webView.addJavascriptInterface( creditCard, "Android" );
        webView.loadUrl("file:///android_assets/index.html");
    }


    private void showMessage( final String message ){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText( CsaDetalhes.this, message, Toast.LENGTH_LONG ).show();
            }
        });
    }
    @Override
    public void update(Observable o, Object arg) {
        CreditCard creditCard = (CreditCard) o;

        if( creditCard.getToken() == null ){
            buttonBuying( false );
            showMessage( creditCard.getError() );
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.13:8888/android-payment/")
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        PaymentConnection paymentConnection = retrofit.create(PaymentConnection.class);
        Call<String> requester = paymentConnection.sendPayment(
                "988",
                450,
                creditCard.getToken(),
                creditCard.getParcels()
        );

        requester.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                buttonBuying( false );
                showMessage( response.body() );
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                buttonBuying( false );
                Log.e("log", "Error: "+t.getMessage());
            }
        });
    }
}




package com.parse.starter.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.starter.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.jar.Manifest;

/**
 * Created by victorbrunobotelho on 09/01/17.
 */

    public class SearchAdapter extends ArrayAdapter<ParseObject>  implements LocationListener{

    private Context context;
    private ArrayList<ParseObject> csas;
    private double distanciaCsa;
    private double latitudeCsa;
    private double longitudeCsa;
    private double latitudeUser;
    private double longitudeUser;
    private LocationManager locationManager;
    private Location location;
    public String[] mColors = {
            "263238", "DD2C00", "212121", "388E3C", "EF5350", "039BE5", "673AB7", "311B92"       //reds

    };



    public SearchAdapter(Context c, ArrayList<ParseObject> objects) {

        super(c, 0, objects);
        this.context = c;
        this.csas = objects;



        if(isLocationServicesAvailable(getContext())){
            Toast.makeText(context ,"ativado", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context ,"desativado", Toast.LENGTH_LONG).show();
        }



    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;

        /*
            Verifica se não existe o objeto view criado,
            pois a view utilizada é armazenado no cache do android e fica na variável
            convertView
        */
        if (view == null) {

            //Inicializa objeto para montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //monta a view a partir do xml
            view = inflater.inflate(R.layout.lista_csa_card, parent, false);
            // istanciando a classe holder e ligando as variaveis ao xml
            holder = new ViewHolder();
            holder.nomeCsa = (TextView) view.findViewById(R.id.text_nome_csa);
            holder.localCsa = (TextView) view.findViewById(R.id.text_local_csa);
            holder.distanciaCsa = (TextView) view.findViewById(R.id.geo_localizacao_csa);
            holder.imageView = (ImageView) view.findViewById(R.id.image_lista_csa);
            view.setTag(holder);



        }else{

            holder = (ViewHolder) view.getTag();
        }

        //verifica se existe postagens
        if (csas.size() > 0) {

            //recupera componentes da tela
            ImageView imagemPostagem = (ImageView) view.findViewById(R.id.image_lista_csa);

            //recuperando CSA apartirda posição
            ParseObject parseObject = csas.get(position);
            //recuperando dados do objeto csa
            String nomeCsa = (String) parseObject.get("NOME");
            String localCsa = (String) parseObject.get("ENDERECO");
            latitudeCsa =  parseObject.getParseGeoPoint("LOCALIZACAO").getLatitude();
            longitudeCsa = parseObject.getParseGeoPoint("LOCALIZACAO").getLongitude();



            //setando para o textView da  classe holder para mostrar na tela
            holder.nomeCsa.setText(nomeCsa);
            holder.localCsa.setText(localCsa);

            int i = new Random().nextInt(254);

            GradientDrawable shape = new GradientDrawable();
            shape.setColor(Color.parseColor ("#"+mColors[new Random().nextInt(7)]));
            holder.nomeCsa.setBackground(shape);
            holder.localCsa.setBackground(shape);
            holder.distanciaCsa.setBackground(shape);


            distanciaCsa =  calculaDistancia(latitudeUser,longitudeUser,latitudeCsa,longitudeCsa);


            holder.distanciaCsa.setText(String.format(Locale.CANADA,"%.0f", distanciaCsa)+" km ");


            //passando foto para o picasso mostrar na tela recuperado do parse
            Picasso.with(context)
                    .load(parseObject.getParseFile("IMAGEM").getUrl())
                    .fit()
                    .into(imagemPostagem);


        }

        return view;

    }

    private double calculaDistancia(double lat1, double lng1, double lat2, double lng2) {
        //double earthRadius = 3958.75;//miles
        double earthRadius = 6371;//kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist * 1.3; //em metros
    }


    @Override
    public void onLocationChanged(Location location) {

        longitudeUser = location.getLongitude();
        latitudeUser = location.getLatitude();

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

    public static boolean isLocationServicesAvailable(Context context) {
        int locationMode = 0;
        String locationProviders;
        boolean isAvailable = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();

            }


            isAvailable = (locationMode != Settings.Secure.LOCATION_MODE_OFF);
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            isAvailable = !TextUtils.isEmpty(locationProviders);

        }

        boolean coarsePermissionCheck = (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        boolean finePermissionCheck = (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);

        return isAvailable && (coarsePermissionCheck || finePermissionCheck);
    }



    static class ViewHolder {
        TextView localCsa;
        TextView distanciaCsa;
        TextView nomeCsa;
        ImageView imageView;
    }

}

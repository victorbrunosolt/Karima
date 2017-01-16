package com.parse.starter.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.starter.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victorbrunobotelho on 09/01/17.
 */

    public class SearchAdapter extends ArrayAdapter<ParseObject> {

    private Context context;
    private ArrayList<ParseObject> csas;

    public SearchAdapter(Context c, ArrayList<ParseObject> objects) {

        super(c, 0, objects);
        this.context = c;
        this.csas = objects;
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
            view = inflater.inflate(R.layout.lista_csa, parent, false);
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
            //Geocoder distanciaCsa = (Geocoder) parseObject.get("LOCALIZACAO");


            //setando para o textView da  classe holder para mostrar na tela
            holder.nomeCsa.setText(nomeCsa);
            holder.localCsa.setText(localCsa);
            //holder.distanciaCsa.setText(distanciaCsa);


            //passando foto para o picasso mostrar na tela recuperado do parse
            Picasso.with(context)
                    .load(parseObject.getParseFile("IMAGEM").getUrl())
                    .fit()
                    .into(imagemPostagem);


        }

        return view;

    }

    static class ViewHolder {
        TextView localCsa;
        TextView distanciaCsa;
        TextView nomeCsa;
        ImageView imageView;
    }
}

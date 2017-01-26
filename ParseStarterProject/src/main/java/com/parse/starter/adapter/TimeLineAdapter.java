package com.parse.starter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.parse.ParseObject;
import com.parse.starter.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by victorbrunobotelho on 24/01/17.
 */

public class TimeLineAdapter extends ArrayAdapter<ParseObject>{

    private Context context;
    private ArrayList<ParseObject> posts;


    public TimeLineAdapter(Context c, ArrayList<ParseObject> objects) {
        super(c, 0, objects);
        this.context = c;
        this.posts = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = convertView;


        /*
            Verifica se não existe o objeto view criado,
            pois a view utilizada é armazenado no cache do android e fica na variável
            convertView
        */
        if (view == null) {

            //Inicializa objeto para montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //monta a view a partir do xml
            view = inflater.inflate(R.layout.time_line, parent, false);

            // instanciando a classe holder e ligando as variaveis ao xml
            //   holder = new ViewHolderTimeLine();
             // ImageView imageViewPost = (ImageView) view.findViewById(R.id.imagem_view_time_line);
            // view.setTag(holder);

        }

        //verifica se existe postagens
        if (posts.size() > 0) {

            //recupera componentes da tela
            ImageView imagemPostagem = (ImageView) view.findViewById(R.id.imagem_view_time_line);


            //recuperando CSA apartirda posição
            ParseObject parseObject = posts.get(position);



            //passando foto para o picasso mostrar na tela recuperado do parse
            Picasso.with(context)
                    .load(parseObject.getParseFile("IMAGEM").getUrl())
                    .fit()
                    .into(imagemPostagem);

        }

        return view;

    }
}




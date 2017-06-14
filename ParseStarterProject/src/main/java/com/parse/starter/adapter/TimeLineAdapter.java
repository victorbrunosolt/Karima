package com.parse.starter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.starter.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by victorbrunobotelho on 09/01/17.
 */

public class TimeLineAdapter extends ArrayAdapter<ParseObject> {

    private Context context;
    private ArrayList<ParseObject> posts;
    private ArrayList<ParseUser> usuarios;

    public TimeLineAdapter(Context c, ArrayList<ParseObject> objects, ArrayList<ParseUser> objectsUsuarios) {

        super(c, 0, objects);
        this.context = c;
        this.posts = objects;
        this.usuarios = objectsUsuarios;
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
            view = inflater.inflate(R.layout.time_line_card, parent, false);
            // istanciando a classe holder e ligando as variaveis ao xml
            holder = new ViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.imagem_view_time_line);
            view.setTag(holder);



        }else{

            holder = (ViewHolder) view.getTag();
        }

        //verifica se existe postagens
        if (posts.size() > 0 & usuarios.size() > 0) {

            //recupera componentes da tela
            ImageView imagemPostagem = (ImageView) view.findViewById(R.id.imagem_view_time_line);
            TextView tvModel = (TextView) view.findViewById(R.id.tv_model);
            TextView tvBrand = (TextView) view.findViewById(R.id.tv_brand);

            //recuperando CSA apartirda posição
            ParseObject parseObject = posts.get(position);
            ParseUser parseUser = usuarios.get(position);
            String descricao = (String) parseObject.get("DESCRICAO");

//                String usuarioNome = parseUser.getUsername();
//                tvModel.setText(usuarioNome);

                tvModel.setText("Victor Bruno");



            tvBrand.setText(descricao);
            //passando foto para o picasso mostrar na tela recuperado do parse
            Picasso.with(context)
                    .load(parseObject.getParseFile("IMAGEM").getUrl())
                    .fit()
                    .into(imagemPostagem);


        }

        return view;

    }

    static class ViewHolder {

        ImageView imageView;
    }
}

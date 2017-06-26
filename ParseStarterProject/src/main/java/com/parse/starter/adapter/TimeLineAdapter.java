package com.parse.starter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.starter.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by victorbrunobotelho on 09/01/17.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {

    private Context mContext;
    private ArrayList<ParseObject> posts;
    private ArrayList<ParseUser> usuarios;
    private LayoutInflater mLayoutInflater;

    public TimeLineAdapter(Context c, ArrayList<ParseObject> objects, ArrayList<ParseUser> objectsUsuarios) {

        this.mContext = c;
        this.posts = objects;
        this.usuarios = objectsUsuarios;

    }



    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mLayoutInflater.from(mContext).inflate(R.layout.time_line_card, parent, false);
        TimeLineViewHolder holder = new TimeLineViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {


        ParseObject parseObject = posts.get(position);
        ParseUser parseUser = usuarios.get(position);

        if (parseUser != null) {
            String nomeUsuario = parseUser.getUsername();
            holder.tituloTimeLine.setText(nomeUsuario);
        } else {
            holder.tituloTimeLine.setText("Usuario Excluido");
        }


        String descricao = (String) parseObject.get("DESCRICAO");


        holder.descricaoTimeLine.setText(descricao);

        Glide.with(mContext).load(parseObject.getParseFile("IMAGEM").getUrl())
                .thumbnail(0.5f)
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imagemTimeLine);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    class TimeLineViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imagem_view_time_line)
        ImageView imagemTimeLine;
        @BindView(R.id.tv_titulo_time_line)
        TextView tituloTimeLine;
        @BindView(R.id.tv_descricao_time_line)
        TextView descricaoTimeLine;



        public TimeLineViewHolder(View view) {
            super(view);
            ButterKnife.bind(mContext, view);
            tituloTimeLine = (TextView) view.findViewById(R.id.tv_titulo_time_line);
            descricaoTimeLine = (TextView) view.findViewById(R.id.tv_descricao_time_line);
            imagemTimeLine = (ImageView) view.findViewById(R.id.imagem_view_time_line);





        }
    }

}

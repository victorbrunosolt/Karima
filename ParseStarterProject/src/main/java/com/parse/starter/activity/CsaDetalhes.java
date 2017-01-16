package com.parse.starter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.starter.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CsaDetalhes extends AppCompatActivity {

    private Toolbar toolbar;
    private String objectId;
    private TextView textViewNome;
    private TextView textViewDescricao;
    private TextView textViewEndereco;
    private TextView textViewPreco;
    private TextView textViewCapacidade;
    private ImageView imagemViewFotoCsa;
    private ParseObject csa ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csa_detalhes);
        Intent intent = getIntent();
        objectId = intent.getStringExtra("objectId");
        getCsa();

        imagemViewFotoCsa = (ImageView) findViewById(R.id.imagem_fundo_Csa) ;
        textViewNome = (TextView) findViewById(R.id.nome_csa);
        textViewDescricao = (TextView) findViewById(R.id.descricao_csa);
        textViewEndereco = (TextView) findViewById(R.id.endereco_csa);
        textViewPreco = (TextView) findViewById(R.id.preco_csa);
        textViewCapacidade = (TextView) findViewById(R.id.capacidade_csa);




        TabHost host = (TabHost)findViewById(R.id.tabHost);
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
                    String  nome = (String) csa.get("NOME");
                    String  descricao = (String) csa.get("DESCRICAO");
                    String  endereco = (String) csa.get("ENDERECO");
                    int  preco = (int) csa.get("PRECO");
                    int  capacidade = (int) csa.get("CAPACIDADE");


                    //setando os dados na tela

                    textViewNome.setText("Nome: "+nome);
                    textViewDescricao.setText("Descrição: "+descricao);
                    textViewEndereco.setText("Endereço: "+endereco);
                    textViewPreco.setText("Preço: "+Integer.toString(preco)+"Reais");
                    textViewCapacidade.setText("Capacidade: "+Integer.toString(capacidade));
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


    }


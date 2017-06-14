package com.parse.starter.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.starter.R;
import com.parse.starter.activity.CadastroActivity;
import com.parse.starter.activity.MainActivity;

import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsuariosFragment extends Fragment {
    private ParseUser usuario = new ParseUser();
    private Inflater inflater;
    private EditText editLoginUsuario;
    private EditText editLoginSenha;
    private Button botaoLogar;
    private ImageView fotoLogin;
    private View view ;
    private TextView cadastrar;

        public UsuariosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {





        // Inflate the layout for this fragment

        if (usuario.getCurrentUser() == null) {

            usuarioDeslogado(inflater,container);

        }else{

            usuarioLogado(inflater,container);
        }
        return   view;


    }

    private void verificarLogin(String usuario, String senha){
        ParseUser.logInInBackground(usuario, senha, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if( e==null ){//sucesso no login
                    Toast.makeText(getActivity(), "Login realizado com sucesso!!", Toast.LENGTH_LONG).show();
                    abrirAreaPrincipal();


                }else{//erro ao logar
                    Toast.makeText(getActivity(), "Erro ao fazer login, "
                            + e.getMessage() , Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void usuarioDeslogado(final LayoutInflater inflater, final ViewGroup container){

        view = inflater.inflate(R.layout.activity_login, container, false);
        editLoginUsuario = (EditText) view.findViewById(R.id.edit_login_usuario);
        editLoginSenha = (EditText) view.findViewById(R.id.edit_login_senha);
        botaoLogar = (Button) view.findViewById(R.id.button_logar);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usuario = editLoginUsuario.getText().toString();
                String senha = editLoginSenha.getText().toString();

                verificarLogin(usuario, senha);
            }
        });

        cadastrar = (TextView) view.findViewById(R.id.text_cadastro);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), CadastroActivity.class);
                startActivity(intent);

            }
        });




    }


    public void usuarioLogado(LayoutInflater inflater,ViewGroup container){

        view = inflater.inflate(R.layout.usuario_activity, container, false);
        fotoLogin = (ImageView) view.findViewById(R.id.foto_perfil);

    }




    private void abrirAreaPrincipal(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity( intent );
    }


}

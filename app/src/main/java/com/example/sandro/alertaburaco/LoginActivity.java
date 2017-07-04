package com.example.sandro.alertaburaco;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sandro.alertaburaco.Config.ConfigFirebase;
import com.example.sandro.alertaburaco.model.Pessoa;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText senha;
    private Button entrar;
    private Pessoa pessoa;
    private FirebaseAuth autenticacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarPessoaLogin(); //verifica se usuario está logado

        email = (EditText) findViewById(R.id.edit_login_email);
        senha = (EditText)findViewById(R.id.edit_login_senha);
        entrar =(Button)findViewById(R.id.button_logar);

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pessoa = new Pessoa();
                pessoa.setEmail(email.getText().toString());
                pessoa.setSenha(senha.getText().toString());

                validarlogin();
            }
        });

    }
    private void verificarPessoaLogin(){
        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser()!=null){
            iniciarsistema();
        }
    }
    private void validarlogin(){
        autenticacao= ConfigFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                pessoa.getEmail(),
                pessoa.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    iniciarsistema();
                    Toast.makeText(LoginActivity.this,"Login com sucesso",Toast.LENGTH_LONG).show();

                }else {
                    {
                        String erroExcecao = "";
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            erroExcecao = "Senha errada";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            erroExcecao = "Email invalido, digite um novo email";
                        } catch (Exception e) {
                            erroExcecao = "Erro em efetuar o login";
                            e.printStackTrace();
                        }
                        Toast.makeText(LoginActivity.this, "Erro login", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private void iniciarsistema(){
        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUser(View view){
        Intent intent = new Intent(LoginActivity.this,CadastroActivity.class);
        startActivity(intent);
    }

}


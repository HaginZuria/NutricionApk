package com.example.hagin.nutricion;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import com.example.hagin.nutricion.MainActivity;
import com.example.hagin.nutricion.R;
import android.content.SharedPreferences;
import android.content.Context;

import org.json.JSONObject;

public class LoginActivity extends Activity {
    //private String ip = "10.111.7.131:567";
    private String ip= "192.168.1.34:567";

    private Button btnLogin;
    //private Button btnActualizar;
    //private Button btnEliminar;
    //private Button btnObtener;
    //private Button btnListar;

    //Intent intent = new Intent(this, NavigationDrawerActivity.class);

    private EditText txtUsuario;
    private String email;
    private String foto;
    private EditText txtPassword;
    //private EditText txtTelefono;

    private TextView lblResultado;
    private ListView lstClientes;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        //btnActualizar = (Button)findViewById(R.id.btnActualizar);
        //btnEliminar = (Button)findViewById(R.id.btnEliminar);
        //btnObtener = (Button)findViewById(R.id.btnObtener);
        //btnListar = (Button)findViewById(R.id.btnListar);

        txtUsuario = (EditText)findViewById(R.id.txtUsuario);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        //txtTelefono = (EditText)findViewById(R.id.txtTelefono);

        //lblResultado = (TextView)findViewById(R.id.lblResultado);
        //lstClientes = (ListView)findViewById(R.id.lstClientes);


        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TareaWSLogin tarea = new TareaWSLogin();
                tarea.execute(
                        txtUsuario.getText().toString(),
                        txtPassword.getText().toString());
            }
        });


    }

    public void launchSecondActivity() {
        //Log.d(LOG_TAG, "Button clicked!");
        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("email", email);
        editor.putString("foto", foto);
        editor.commit();
        Log.d("EMAIL: ",email);
        Intent intent = new Intent(this, MainActivity.class);
        //String message = mMessageEditText.getText().toString();

        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    //Tarea As�ncrona para llamar al WS de consulta en segundo plano
    private class TareaWSLogin extends AsyncTask<String,Integer,Boolean> {
        private String password;
        String id, id2;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            id = params[0];
            id2 = params[1];

            HttpGet del =
                    new HttpGet("http://"+ip+"/WebServiceRest/Api/Usuarios/Usuario/" + id + "/" + id2);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);

                email = respJSON.getString("email");
                password = respJSON.getString("password");
                foto = respJSON.getString("foto");
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                //lblResultado.setText("" + email + "-" + password + "-" + foto);
                launchSecondActivity();
            }
            else{
                //lblResultado.setText("Inicio de sesión fallido " + id + " " + id2);

            }
        }
    }
}



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

import org.json.JSONObject;

public class LoginActivity extends Activity {

    private Button btnLogin;
    //private Button btnActualizar;
    //private Button btnEliminar;
    //private Button btnObtener;
    //private Button btnListar;

    //Intent intent = new Intent(this, NavigationDrawerActivity.class);

    private EditText txtUsuario;
    private EditText txtPassword;
    //private EditText txtTelefono;

    private TextView lblResultado;
    private ListView lstClientes;

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

        lblResultado = (TextView)findViewById(R.id.lblResultado);
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


        /*btnActualizar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TareaWSActualizar tarea = new TareaWSActualizar();
				tarea.execute(
						txtUsuario.getText().toString(),
						txtPassword.getText().toString(),
						txtTelefono.getText().toString());
			}
		});*/

        /*btnEliminar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TareaWSEliminar tarea = new TareaWSEliminar();
				tarea.execute(txtUsuario.getText().toString());
			}
		});*/

        /*btnObtener.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TareaWSObtener tarea = new TareaWSObtener();
				tarea.execute(txtUsuario.getText().toString());
			}
		});*/

        /*btnListar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TareaWSListar tarea = new TareaWSListar();
				tarea.execute();
			}
		});*/
    }

    public void launchSecondActivity() {
        //Log.d(LOG_TAG, "Button clicked!");

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

    //Tarea As�ncrona para llamar al WS de listado en segundo plano
	/*private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {

		private String[] clientes;

	    protected Boolean doInBackground(String... params) {

	    	boolean resul = true;

	    	HttpClient httpClient = new DefaultHttpClient();

			HttpGet del =
					new HttpGet("http://10.107.57.21:51674/Api/Clientes");

			del.setHeader("content-type", "application/json");

			try
	        {
	        	HttpResponse resp = httpClient.execute(del);
	        	String respStr = EntityUtils.toString(resp.getEntity());

	        	JSONArray respJSON = new JSONArray(respStr);

	        	clientes = new String[respJSON.length()];

	        	for(int i=0; i<respJSON.length(); i++)
	        	{
	        		JSONObject obj = respJSON.getJSONObject(i);

		        	int email = obj.getInt("Id");
		        	String password = obj.getString("Nombre");
		        	int telefCli = obj.getInt("Telefono");

	        		clientes[i] = "" + email + "-" + password + "-" + telefCli;
	        	}
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
		    	//Rellenamos la lista con los nombres de los clientes
	    		//Rellenamos la lista con los resultados
	        	ArrayAdapter<String> adaptador =
	        		    new ArrayAdapter<String>(MainActivity.this,
	        		        android.R.layout.simple_list_item_1, clientes);

	        	lstClientes.setAdapter(adaptador);
	    	}
	    }
	}*/

    //Tarea As�ncrona para llamar al WS de consulta en segundo plano
    private class TareaWSLogin extends AsyncTask<String,Integer,Boolean> {

        private String email;
        private String password;
        private String foto;
        String id, id2;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            id = params[0];
            id2 = params[1];

            HttpGet del =
                    new HttpGet("http://10.111.7.131:567/WebServiceRest/Api/Usuarios/Usuario/" + id + "/" + id2);

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
                lblResultado.setText("" + email + "-" + password + "-" + foto);
                launchSecondActivity();

            }
            else{
                lblResultado.setText("Inicio de sesión fallido " + id + " " + id2);

            }
        }
    }

    //Tarea As�ncrona para llamar al WS de actualizaci�n en segundo plano
	/*private class TareaWSActualizar extends AsyncTask<String,Integer,Boolean> {

	    protected Boolean doInBackground(String... params) {

	    	boolean resul = true;

	    	HttpClient httpClient = new DefaultHttpClient();

			HttpPut put = new HttpPut("http://10.107.57.21:51674/Api/Clientes/Cliente");
			put.setHeader("content-type", "application/json");

			try
	        {
				//Construimos el objeto cliente en formato JSON
				JSONObject dato = new JSONObject();

				dato.put("Id", Integer.parseInt(params[0]));
				dato.put("Nombre", params[1]);
				dato.put("Telefono", Integer.parseInt(params[2]));

				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);

	        	HttpResponse resp = httpClient.execute(put);
	        	String respStr = EntityUtils.toString(resp.getEntity());

	        	if(!respStr.equals("true"))
	        		resul = false;
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
	    		lblResultado.setText("Actualizado OK.");
	    	}
	    }
	}*/

    //Tarea As�ncrona para llamar al WS de eliminaci�n en segundo plano
	/*private class TareaWSEliminar extends AsyncTask<String,Integer,Boolean> {

	    protected Boolean doInBackground(String... params) {

	    	boolean resul = true;

	    	HttpClient httpClient = new DefaultHttpClient();

			String id = params[0];

			HttpDelete del =
					new HttpDelete("http://10.107.57.21:51674/Api/Clientes/Cliente/" + id);

			del.setHeader("content-type", "application/json");

			try
	        {
	        	HttpResponse resp = httpClient.execute(del);
	        	String respStr = EntityUtils.toString(resp.getEntity());

	        	if(!respStr.equals("true"))
	        		resul = false;
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
	    		lblResultado.setText("Eliminado OK.");
	    	}
	    }
	}*/

    //Tarea As�ncrona para llamar al WS de inserci�n en segundo plano
	/*private class TareaWSLogin extends AsyncTask<String,Integer,Boolean> {

	    protected Boolean doInBackground(String... params) {

	    	boolean resul = true;

	    	HttpClient httpClient = new DefaultHttpClient();

			HttpPost post = new HttpPost("http://10.117.7.131:567/Api/Usuarios/Usuario");
			post.setHeader("content-type", "application/json");

			try
	        {
				//Construimos el objeto cliente en formato JSON
				JSONObject dato = new JSONObject();

				//dato.put("Id", Integer.parseInt(txtUsuario.getText().toString()));
				dato.put("email", params[0]);
				dato.put("password", Integer.parseInt(params[1]));

				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);

	        	HttpResponse resp = httpClient.execute(post);
	        	String respStr = EntityUtils.toString(resp.getEntity());

	        	if(!respStr.equals("true"))
	        		resul = false;
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
	    		lblResultado.setText("Insertado OK.");
	    	}
	    }
	}*/
}



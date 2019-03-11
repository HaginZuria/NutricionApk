package com.example.hagin.nutricion;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrarCaloriasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrarCaloriasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarCaloriasFragment extends Fragment implements AdapterView.OnItemClickListener {
    //private String ip = "10.111.7.131:567";
    private String ip= "192.168.1.34:567";
    private Spinner spinner, spinnerTipoComida;
    private Integer codigo;

    private OnFragmentInteractionListener mListener;
    private Character tipoComida;
    private EditText txtCantidad;
    private Button btnInsertar;
    private TextView lblResultado;
    //txtCantidad.getText().toString(),

    public RegistrarCaloriasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrarCaloriasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrarCaloriasFragment newInstance(String param1, String param2) {
        RegistrarCaloriasFragment fragment = new RegistrarCaloriasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registrar_calorias, container, false);

        spinner = view.findViewById(R.id.spinner);
        spinnerTipoComida = view.findViewById(R.id.spinnerTipoComida);
        lblResultado = (TextView) view.findViewById(R.id.labelResultado);
        txtCantidad = (EditText) view.findViewById(R.id.cantidad);
        btnInsertar = (Button)view.findViewById(R.id.registrarCal);
        //final Date todayDate = Calendar.getInstance().getTime();
        final String todayDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String email = sharedpreferences.getString("email","");
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TareaWSInsertar tarea = new TareaWSInsertar();
                tarea.execute(
                        email, todayDate.toString(),tipoComida.toString(),codigo.toString());
            }
        });

        new TareaWSGetAlimentos().execute();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

   @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //Tarea As�ncrona para llamar al WS de consulta en segundo plano
    private class TareaWSGetAlimentos extends AsyncTask<String,Integer,Boolean> {

        private String[] alimentos;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet("http://"+ip+"/WebServiceRest/Api/Alimentos/");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                alimentos = new String[respJSON.length()];

                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);

                    int codigo = obj.getInt("codigo");
                    String descripcion = obj.getString("descripcion");
                    int calorias = obj.getInt("calorias");

                    alimentos[i] = "" + codigo + "-" + descripcion + "-" + calorias;
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
                spinner.setAdapter(new ArrayAdapter<String>(RegistrarCaloriasFragment.this.getContext(), android.R.layout.simple_list_item_1, alimentos));
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selected = parent.getItemAtPosition(position).toString();
                        Context context = parent.getContext();
                        CharSequence text = selected;
                        codigo = position+1;
                       // int duration = Toast.LENGTH_SHORT;

                        //Toast toast = Toast.makeText(context, "Meow"+codigo, duration);
                        //toast.show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                String[] listaTipo ={"Desayuno","Almuerzo","Cena"};
                spinnerTipoComida.setAdapter(new ArrayAdapter<String>(RegistrarCaloriasFragment.this.getContext(), android.R.layout.simple_list_item_1, listaTipo));
                spinnerTipoComida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selected = parent.getItemAtPosition(position).toString();
                        Context context = parent.getContext();
                        CharSequence text = selected;
                        switch (position){
                            case 1://Almuerzo
                                tipoComida = 'A';
                                break;
                            case 2://Cena
                                tipoComida = 'C';
                                break;
                            default://Desayuno
                                tipoComida = 'D';
                                break;
                        }
                        //Toast.makeText(context, "Char: "+tipoComida, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            }
        }
    }


    //Tarea As�ncrona para llamar al WS de inserci�n en segundo plano
    private class TareaWSInsertar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://"+ip+"/WebServiceRest/Api/Calorias/Caloria");
            post.setHeader("content-type", "application/json");
            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                dato.put("email", params[0]);
                dato.put("fecha", params[1]);
                dato.put("tipoComida", params[2]);
                dato.put("codigoAlimento", Integer.parseInt(params[3]));
                dato.put("cantidad", txtCantidad.getText().toString());
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
            }else{
                lblResultado.setText("No se ha podido insertar.");
            }
        }
    }
}

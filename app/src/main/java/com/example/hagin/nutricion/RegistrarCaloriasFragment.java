package com.example.hagin.nutricion;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hagin.nutricion.Models.Alimento;
import com.example.hagin.nutricion.Models.Caloria;
import com.example.hagin.nutricion.Models.Usuario;
import com.example.hagin.nutricion.Services.AlimentoService;
import com.example.hagin.nutricion.Services.CaloriaService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrarCaloriasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrarCaloriasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarCaloriasFragment extends Fragment implements AdapterView.OnItemClickListener {
    private String baseUrl = "http://192.168.1.37:3306/";
    //private String baseUrl= "http://10.111.7.131:3306/";
    List<Alimento> listaAlimentos = new ArrayList<>();
    private Spinner spinner, spinnerTipoComida;
    private Integer codigo;

    private OnFragmentInteractionListener mListener;
    private Character tipoComida;
    private EditText txtCantidad;
    private Button btnInsertar;
    private TextView lblResultado;
    //txtCantidad.getText().toString(),
    private AlimentoService alimentoService;
    private CaloriaService caloriaService;
    private String fecha;
    private Calendar myCalendar = Calendar.getInstance();
    private EditText edittext;

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

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        alimentoService = retrofit.create(AlimentoService.class);
        caloriaService = retrofit.create(CaloriaService.class);

        spinner = view.findViewById(R.id.spinner);
        spinnerTipoComida = view.findViewById(R.id.spinnerTipoComida);
        lblResultado = (TextView) view.findViewById(R.id.labelResultado);
        txtCantidad = (EditText) view.findViewById(R.id.cantidad);
        btnInsertar = (Button)view.findViewById(R.id.registrarCal);
        edittext= (EditText) view.findViewById(R.id.date);
        edittext.setKeyListener(null);
        final String todayDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String email = sharedpreferences.getString("email","");

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Caloria caloria = new Caloria();
                caloria.setEmail(email);
                caloria.setCodigoAlimento(codigo);
                caloria.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
                caloria.setFecha(fecha);
                caloria.setTipoComida(tipoComida.toString());
               // System.out.println("antes de la cola, "+fecha);

                final Call<Caloria> insertarCaloria = caloriaService.setCalorias(caloria);

                insertarCaloria.enqueue(new Callback<Caloria>(){
                    @Override
                    public void onResponse(Call<Caloria> call, Response<Caloria> response) {
                        //System.err.println("entra onresponse");
                        if(response.isSuccessful()){
                            lblResultado.setText("Insertado OK.");
                           // System.out.println("sakcesful");
                        }else{
                           // System.out.println("elsa: "+response.message());
                            lblResultado.setText("No se ha podido insertar");
                        }
                    }
                    @Override
                    public void onFailure(Call<Caloria> call, Throwable t  ) {
                        lblResultado.setText("No se ha podido insertar.");
                        //System.out.println("feiyur");
                    }
                });
            }
        });
        Call<List<Alimento>> lista = alimentoService.getAlimentos();
        lista.enqueue(new Callback<List<Alimento>>(){
            @Override
            public void onResponse(Call<List<Alimento>> call, Response<List<Alimento>> response) {
                if(response.isSuccessful()){
                    listaAlimentos = response.body();
                    String[] alimentos = new String[listaAlimentos.size()];
                    for(int i=0; i<listaAlimentos.size(); i++)
                    {
                        Alimento obj = listaAlimentos.get(i);

                        int codigo = obj.getCodigo();
                        String descripcion = obj.getDescripcion();
                        int calorias = obj.getCalorias();

                        alimentos[i] = "" + codigo + "-" + descripcion + "-" + calorias;
                    }
                    //System.out.println(alimentos);

                    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateLabel();
                        }
                    };
                    edittext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            new DatePickerDialog(RegistrarCaloriasFragment.this.getContext(), date, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });

                    spinner.setAdapter(new ArrayAdapter<String>(RegistrarCaloriasFragment.this.getContext(), android.R.layout.simple_list_item_1, alimentos));
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selected = parent.getItemAtPosition(position).toString();
                            Context context = parent.getContext();
                            CharSequence text = selected;
                            codigo = position+1;
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
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<List<Alimento>> call, Throwable t  ) {
                if(t instanceof IOException){
                    //lblResultado.setText("Inicio de sesión fallido " + usuario + " " + contrasena + " " + t.getMessage());
                }
                else {
                    //lblResultado.setText("Inicio de sesión fallido " + usuario + " " + contrasena );
                }
            }
        });
        return view;
    }

    private void updateLabel(){
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        int day = myCalendar.get(Calendar.DAY_OF_MONTH); // get the selected day of the month
        String dayString;
        if(day<10){
            dayString = "0"+String.valueOf(day);
        }else{
            dayString = String.valueOf(day);
        }
        int month = myCalendar.get(Calendar.MONTH)+1; // get the selected month
        String monthString;
        if(month<10){
            monthString = "0"+String.valueOf(month);
        }else{
            monthString = String.valueOf(month);
        }
        int year = myCalendar.get(Calendar.YEAR);; // get the selected year
        fecha = dayString +"-"+ monthString +"-"+ year;
        System.out.println("cgvh, "+fecha);

        edittext.setText(sdf.format(myCalendar.getTime()));
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
}

package com.example.mapa6;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapa6.databinding.ActivityMapsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    String texto = "", texto2 = "", texto3 = "";
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public String geocodificacionInversa(String strAddress){
        Geocoder coder = new Geocoder(this);

        Address location = null;
        double lat = 0.0;
        double lng = 0.0;
        String res="";
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress, 10);
            if (address == null) {
                return "no hay direccion";
            }
            int cont=address.size();
            for (int i = 0; i <= cont-1; i++) {
                location = address.get(i);
                res=res+strAddress+":"+location.getLatitude()+","+location.getLongitude()+"\n";
            }

        } catch (Exception e) {
            return "error geocoder";
        }
        return res;
    }

    public String geocodificacion(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String add="";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            add = obj.getAddressLine(0);
            add = add + "," + obj.getAdminArea();
            add = add + "," + obj.getCountryName();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return add;
    }

    public void Dialogo(String mensaje){
        AlertDialog.Builder dialogo=new AlertDialog.Builder(this);
        dialogo.setTitle("aqui dos geo")
                .setMessage(mensaje);
        dialogo.show();
    }

    public void tacoAlitas(){
        RequestQueue servicio= Volley.newRequestQueue(this);
        String url="http://192.168.1.73/services/seleccionar.php";
        JsonArrayRequest json = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject json = null;

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                json = response.getJSONObject(i);
                                texto = json.getString("lon");
                                texto2 = json.getString("lat");
                                texto3 = json.getString("dire");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Toast.makeText(getApplicationContext(), texto+ " "+texto2, Toast.LENGTH_SHORT).show();
        servicio.add(json);
    }
}

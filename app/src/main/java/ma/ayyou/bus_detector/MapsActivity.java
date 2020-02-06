package ma.ayyou.bus_detector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{
    //class pour la recherche et suivre les cordonnées du bus sur le maps
    public static Circle my_circle;
    public static Circle[] bus_circle;
    public static Timer timer;
    private boolean arrive;
    private Location location;
    senddata send;
    Marker[] marker;
    public static speaker parleur;
    Marker marker2;
    public int i;
    public String knownName="";
    String lastknownName="";
    public static  double latitude,longitude;
     location loc;
    public static double[] lat,log;
    FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMap;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        i=0;
        arrive=false;
        parleur=new speaker(MapsActivity.this);
        parleur.initializespeechRecognizer();
        parleur.initializeTextToSpeech("Votre recherche est lancé");
        marker=new Marker[100];
        bus_circle= new Circle[100];
        log = new double[100];
        lat= new double[100];
        for(int j=0;j<marker.length;j++){
            marker[j]=null;
            bus_circle[j]=null;
            lat[j]=0.0;
            log[j]=0.0;
        }
        senddata.nbr_bus=0;
        my_circle=null;
        marker2=null;
        send =new senddata(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            }
        } else {
            ///buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);//afficher ma localisation sur le map
        }
        loc =new location(this);
        if(loc.configure()!=null){
           //Toast.makeText(this, "votre lat"+loc.configure().getLatitude()+"votre long "+loc.configure().getLongitude()+"votre alt "+loc.configure().getAltitude(), Toast.LENGTH_SHORT).show();
            latitude=loc.configure().getLatitude();
            longitude=loc.configure().getLongitude();
            LatLng sydney = new LatLng(loc.configure().getLatitude(),loc.configure().getLongitude());
            marker2 = mMap.addMarker(new MarkerOptions().position(sydney)
                    .title("vous etes là"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14));
            drawCircle_my(sydney);
        }

       final Handler handler = new Handler();
        timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                           mes_coordonnées();
                           bus_coordonnées();
                        } catch (Exception e) {
                            //Toast.makeText(getApplicationContext(), "erreur"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 3000, 10*1000);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                timer.cancel();
                Intent intent = new Intent(getApplicationContext(),select_bus.class);
                startActivity(intent);
                finish();
            }
        });
        bus_coordonnées();
    }
    /////méthode pour dessiner le circle
    public void drawCircle_my(LatLng latLng){
        if(my_circle!=null){
            my_circle.remove();
        }
        my_circle = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(60)
                .clickable(true)
                .strokeColor(Color.rgb(46,90,10))
                .fillColor(Color.TRANSPARENT));
        //Toast.makeText(MapsActivity.this,"centre est : "+circle.getCenter()+"rayon est : "+circle.getRadius(),Toast.LENGTH_SHORT).show();
    }
    public void drawCircle_bus(LatLng latLng){
        if(bus_circle[i]!=null){
            bus_circle[i].remove();
        }
        bus_circle[i] = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(1.5)
                .clickable(true)
                .strokeColor(Color.rgb(200,10,10))
                .fillColor(Color.rgb(10,10,200)));
        if(arrive==false){
            if(isCircleContains(my_circle,latLng)){
                arrive=true;
                MapsActivity.parleur.speake("le bus "+select_bus.bus+" est arrivé");
                MapsActivity.timer.cancel();
            }
            else if(lat[i]!=0.0 && log[i]!=0.0){
                bus_place(latLng);
            }
            else{

            }
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void bus_coordonnées(){
        ///à chaque click un markeur s'ajoute sur le map
        send.getdata();
        for(i=0;i<senddata.nbr_bus;i++){
            if(lat[i]!=0.00 && log[i]!= 0.00){
            if(marker[i]!=null){
                marker[i].remove();
            }
                if(send.your_bus[i]==true){
                LatLng sydney = new LatLng(lat[i],log[i]);
                marker[i] = mMap.addMarker(new MarkerOptions().position(sydney).alpha(34)
                        .title("le bus "+select_bus.bus+" est là"));
                drawCircle_bus(sydney);
            }
            send.your_bus[i]=false;
        }
        }

        senddata.nbr_bus=0;
        //Toast.makeText(getApplicationContext(), ""+senddata.nbr_bus, Toast.LENGTH_SHORT).show();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void mes_coordonnées(){
        ///à chaque click un markeur s'ajoute sur le map
        send.getdata();
        //Toast.makeText(getApplicationContext(), ""+lat, Toast.LENGTH_SHORT).show();
        if(latitude!=0.00 && longitude != 0.00){
            if(marker2!=null){
                marker2.remove();
            }
            LatLng sydney = new LatLng(latitude,longitude);
            marker2 = mMap.addMarker(new MarkerOptions().position(sydney).alpha(34)
                    .title("Vous etes là"));
            drawCircle_my(sydney);
        }
        else{
        }
    }
    public void  bus_place(LatLng latLng){
      Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getApplicationContext(), "add : "+addresses, Toast.LENGTH_SHORT).show();
       // String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
       // String city = addresses.get(0).getLocality();
      // String state = addresses.get(0).getSubAdminArea();
      // String country = addresses.get(0).getCountryName();
       // String postalCode = addresses.get(0).getPostalCode();
        if(addresses!=null){
            knownName = addresses.get(0).getFeatureName();
            if(knownName.equals(lastknownName)|| knownName.equals("Unnamed Road")){

            }
            else{
                parleur.speake("le bus est en "+knownName);

            }
        }
        lastknownName=knownName;
    }
    private boolean isCircleContains(Circle circle, LatLng latLng) {
        double r = circle.getRadius();
        LatLng center = circle.getCenter();
        double cX = center.latitude;
        double cY = center.longitude;
        double pX = latLng.latitude;
        double pY = latLng.longitude;
        float[] results = new float[1];
        Location.distanceBetween(cX, cY, pX, pY, results);

        if (results[0] < r) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
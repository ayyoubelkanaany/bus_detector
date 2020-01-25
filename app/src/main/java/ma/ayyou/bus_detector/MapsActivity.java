package ma.ayyou.bus_detector;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
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
import java.util.Timer;
import java.util.TimerTask;
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{
    //class pour la recherche et suivre les cordonnées du bus sur le maps
    private Circle circlevar2;
    private Circle circlevar;
    public Timer timer;
    private Location location;
    senddata send;
    Marker marker;
    Marker marker2;
    public static  double latitude,longitude;
     location loc;
    public static double lat,log;
    FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMap;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        marker=null;
        circlevar=null;
        circlevar2=null;
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
           /// Toast.makeText(this, "votre lat"+loc.configure().getLatitude()+"votre long "+loc.configure().getLongitude()+"votre alt "+loc.configure().getAltitude(), Toast.LENGTH_SHORT).show();
            latitude=loc.configure().getLatitude();
            longitude=loc.configure().getLongitude();
            LatLng sydney = new LatLng(loc.configure().getLatitude(),loc.configure().getLongitude());
            marker2 = mMap.addMarker(new MarkerOptions().position(sydney)
                    .title("vous etes là"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            drawCircle(sydney);
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
                            Toast.makeText(loc, "erreur", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 1000);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                timer.cancel();
                if(senddata.play==true){
                    senddata.mediaPlayer.stop();
                }
                Intent intent = new Intent(getApplicationContext(),select_bus.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /////méthode pour dessiner le circle
    public void drawCircle(LatLng latLng){
        if(circlevar2!=null){
            circlevar2.remove();
        }
        circlevar2 = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(200)
                .clickable(true)
                .strokeColor(Color.rgb(46,90,10))
                .fillColor(Color.TRANSPARENT));
        //Toast.makeText(MapsActivity.this,"centre est : "+circle.getCenter()+"rayon est : "+circle.getRadius(),Toast.LENGTH_SHORT).show();
    }
    public void drawCircle2(LatLng latLng){
        if(circlevar!=null){
            circlevar.remove();
        }

        circlevar = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(50)
                .clickable(true)
                .strokeColor(Color.rgb(33,45,10))
                .fillColor(Color.TRANSPARENT));
        /* Toast.makeText(MapsActivity.this,"centre est : "+circle.getCenter()+"rayon est : "+circle.getRadius(),Toast.LENGTH_SHORT).show(); */

    }
    public void bus_coordonnées(){
        ///à chaque click un markeur s'ajoute sur le map
        send.getdata();
        //Toast.makeText(getApplicationContext(), ""+lat, Toast.LENGTH_SHORT).show();
        if(lat!=0.00 && log != 0.00){
            if(marker!=null){
                marker.remove();
                circlevar.remove();
            }
            if(send.your_bus==true){
                LatLng sydney = new LatLng(lat,log);
                marker = mMap.addMarker(new MarkerOptions().position(sydney).alpha(34)
                        .title("le bus est là"));
                drawCircle2(sydney);
            }
            send.your_bus=false;

        }
        else{

        }
    }
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
            drawCircle(sydney);
        }
        else{
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
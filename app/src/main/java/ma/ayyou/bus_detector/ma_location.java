package ma.ayyou.bus_detector;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class ma_location extends AppCompatActivity implements LocationListener {
    public static TextView longitude, latitude, altitude;
    public static EditText bus;
    private Button retour;
    public Timer timer;
    public static MediaPlayer mediaPlayer;
    speaker parleur;
    private boolean first=false;
    public Location location;
    LocationManager locationManager;
    location loc;////objets location
    senddata data; /// objets senddata poue envoyer les coordonnées
    private Button numéro;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_location);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sonnerie);
        parleur = new speaker(this);
        parleur.initializespeechRecognizer();
        parleur.initializeTextToSpeech("entrer le numéro de bus à diffuser");
        loc = new location(this);
        data = new senddata(this);
        this.longitude = findViewById(R.id.longitude);
        this.bus = findViewById(R.id.bus);
        this.retour = findViewById(R.id.retour);
        this.latitude = findViewById(R.id.latitude);
        this.altitude = findViewById(R.id.altitude);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bus.getText().toString().isEmpty()){
                    parleur.speake("entrer le numéro de bus");
                }
                else{
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                    timer.cancel();
                }

            }
        });
        retour.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                timer.cancel();
                return false;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED | checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE};
                requestPermissions(permission, 3);
            } else {
                /// Toast.makeText(context, "hello1", Toast.LENGTH_SHORT).show();
            }
        }
        boolean isenable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isenable) {
            ////exécuter si le gps est activer
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                ///si la localisation n'est pas null en change les coordonnées qui sont static
                longitude.setText("" + location.getLongitude());
                latitude.setText("" + location.getLatitude());
                altitude.setText("" + location.getAltitude());
            } else {

            }
        } else {
            int REQUEST_ENABLE_LOCATION = 6;
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_ENABLE_LOCATION);
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 3);
        }
        ///Timer permet d'envoyer chaque 10 secondes les coordonnées
         timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {

                if (bus.getText().toString().isEmpty() || latitude.getText().toString().isEmpty() || longitude.getText().toString().isEmpty()) {

                } else {
                    if(first==false){
                        parleur.speake("la diffusion a commencée");
                        first=true;
                    }
                    data.envoyer(bus.getText().toString(), latitude.getText().toString(),longitude.getText().toString());
                    mediaPlayer.start();

                }
            }
        }, 6000,  30000);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (resultCode == RESULT_CANCELED) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, this);
                location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    ///si la localisation n'est pas null en change les coordonnées qui sont static
                    longitude.setText(""+location.getLongitude());
                    latitude.setText(""+location.getLatitude());
                    altitude.setText(""+location.getAltitude());
                }
            }
            else{
                //Toast.makeText(getApplicationContext(), "le gps doit etre activer", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent );
            }
        }
        if(requestCode == 2){
            if(requestCode==RESULT_CANCELED){
                parleur.speake("le gps est ouvert");
            }
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(context, "location change ", Toast.LENGTH_SHORT).show();
        ////à chaque changelent de la location longitude, latitude change ils sont static
        longitude.setText(""+ location.getLongitude());
        latitude.setText(""+location.getLatitude());
        altitude.setText(""+ location.getAltitude());
        ///Toast.makeText(context, "apres location change ", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
        parleur.speake("ouvrez le gps");
        Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent,2);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

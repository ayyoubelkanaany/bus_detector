package ma.ayyou.bus_detector;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Timer;
import java.util.TimerTask;

public class ma_location extends AppCompatActivity {
  public static TextView  longitude,latitude,altitude;
  public static EditText bus;
   location loc;////objets location
   senddata data; /// objets senddata poue envoyer les coordonnées
   private Button numéro;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_location);
        loc = new location(this);
        data=new senddata(this);
        this.longitude = findViewById(R.id.longitude);
        this.numéro=findViewById(R.id.numéro);
        this.bus=findViewById(R.id.bus);
        this.latitude = findViewById(R.id.latitude);
        this.altitude = findViewById(R.id.altitude);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                }
            }
        }
        else {

            }
            if (loc.configure() != null) {
                ///si la localisation n'est pas null en change les coordonnées qui sont static
                longitude.setText(""+loc.configure().getLongitude());
                latitude.setText(""+loc.configure().getLatitude());
                altitude.setText(""+loc.configure().getAltitude());

            } else {
                longitude.setText("ligitude");
                latitude.setText("latitude");
                altitude.setText("altitude");
            }
            numéro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ///Timer permet d'envoyer chaque 3mins les coordonnées
            Timer timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    data.envoyer(bus.getText().toString(),latitude.getText().toString(),longitude.getText().toString());
                }
            },100,3*60*1000);

    }
}

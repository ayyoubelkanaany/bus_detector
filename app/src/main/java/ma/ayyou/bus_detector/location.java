package ma.ayyou.bus_detector;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

class location extends AppCompatActivity implements LocationListener {
    ////cette classe récupère la localisation de mon téléphone et envoie les coordonnées
    public Location location;
    ma_location my_location;
    LocationManager locationManager;
    private Context context;
    public location(Context context){
        this.context=context;
        locationManager= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //my_location=new ma_location();
    }
    ////implementation des méthodes de locationlistener
    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(context, "alt : " + location.getAltitude() + "long : " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        ////à chaque changelent de la location longitude, latitude change ils sont static
        my_location.longitude.setText(""+ location.getLongitude());
        my_location.latitude.setText(""+location.getLatitude());
        my_location.altitude.setText(""+ location.getAltitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        /// Toast.makeText(context, ""+extras.describeContents(), LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    //appeller si le GPS est fermer
    @Override
    public void onProviderDisabled(String provider) {
        Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public Location configure(){
       ///methode pour récupérer la localisation initiale et le service localisation
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

            if(context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED||context.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)==PackageManager.PERMISSION_DENIED)
            {
                String[] permission ={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_NETWORK_STATE};
                requestPermissions(permission,3);
            }
            else{
                /// Toast.makeText(context, "hello1", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            // Toast.makeText(context, "hello2", Toast.LENGTH_SHORT).show();

        }
        boolean isenable=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isenable){
            ////exécuter si le gps est activer
            /// Toast.makeText(context, "if", Toast.LENGTH_SHORT).show();

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,1,this);
            location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            // Toast.makeText(context, "location : "+location.getLongitude(), Toast.LENGTH_SHORT).show();
            return location;
        }
        else{
            int REQUEST_ENABLE_LOCATION=6;
            requestPermissions(
                    new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                    REQUEST_ENABLE_LOCATION );
        }
        /// Toast.makeText(context, ""+location, LENGTH_SHORT).show();
        return null;
    }
}

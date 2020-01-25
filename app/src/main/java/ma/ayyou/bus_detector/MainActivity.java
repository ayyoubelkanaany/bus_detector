package ma.ayyou.bus_detector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
   private Button send,detect;
///class principale pour choisir soit l'envoie des coordonnées soit la recherche du bus
   speaker parleur;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.send=findViewById(R.id.send);
        this.detect=findViewById(R.id.detect);
        parleur = new speaker(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        if ( checkSelfPermission( Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED ) {
            int REQUEST_ENABLE_LOCATION=6;
            requestPermissions(
                    new String[] {  Manifest.permission.RECORD_AUDIO  },
                    REQUEST_ENABLE_LOCATION);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                ////lancer l'écoute
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                parleur.speechRecognizer.startListening(intent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parleur.speake(send.getText().toString());

            }
        });
        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parleur.speake(detect.getText().toString());
            }
        });
        send.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ma_location.class);
                startActivity(intent);
                return false;
            }
        });
        detect.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent=new Intent(getApplicationContext(),select_bus.class);
                startActivity(intent);
                return false;
            }
        });
        parleur.initializeTextToSpeech("bienvenu sur le menu principale qui vous permet d'envoyer votre localisation ou de chercher un bus ");
        parleur.initializespeechRecognizer();
    }


}

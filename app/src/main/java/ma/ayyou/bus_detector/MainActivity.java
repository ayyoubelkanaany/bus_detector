package ma.ayyou.bus_detector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {
    private Button send, detect;
    ///class principale pour choisir soit l'envoie des coordonnées soit la recherche du bus
    speaker parleur;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.send = findViewById(R.id.send);
        this.detect = findViewById(R.id.detect);
        parleur = new speaker(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            int REQUEST_ENABLE_LOCATION = 6;
            requestPermissions(
                    new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.CALL_PHONE},REQUEST_ENABLE_LOCATION);
        }
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ////lancer l'écoute
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                parleur.speechRecognizer.startListening(intent);
                return false;
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
                Intent intent = new Intent(getApplicationContext(), ma_location.class);
                startActivity(intent);
                finish();
                return false;
            }
        });
        detect.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(), select_bus.class);
                startActivity(intent);
                finish();
                return false;
            }
        });
        parleur.initializeTextToSpeech("bienvenu sur le menu principale");
        parleur.initializespeechRecognizer();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void call() {
        String number = "tel:" + "0654276218";
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        startActivity(intent);
        }


}

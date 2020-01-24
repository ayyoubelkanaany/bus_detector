package ma.ayyou.bus_detector;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class select_bus extends AppCompatActivity {
    ///class pour entrer le numéroo de bus et passer vers le maps
    private EditText text;
    public static String bus;
    private Button btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,supprimer,detecte,start;
    speaker parleur;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bus);
        parleur= new speaker(this);
        this.btn0=findViewById(R.id.btn0);
        this.btn1=findViewById(R.id.btn1);
        this.btn2=findViewById(R.id.btn2);
        this.btn3=findViewById(R.id.btn3);
        this.btn4=findViewById(R.id.btn4);
        this.btn5=findViewById(R.id.btn5);
        this.btn6=findViewById(R.id.btn6);
        this.btn7=findViewById(R.id.btn7);
        this.btn8=findViewById(R.id.btn8);
        this.btn9=findViewById(R.id.btn9);
        this.detecte=findViewById(R.id.detecte_bus);
        this.supprimer=findViewById(R.id.supprimer);
        this.start=findViewById(R.id.start_detect);
        this.text=findViewById(R.id.editText);
        FloatingActionButton fab = findViewById(R.id.fab);
        parleur.initializeTextToSpeech();
        parleur.initializespeechRecognizer();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parleur.speake("zero");
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parleur.speake("un");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parleur.speake("deux");
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parleur.speake("trois");
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parleur.speake("quatre");
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parleur.speake("cinq");
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parleur.speake("six");
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parleur.speake("sept");
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parleur.speake("huit");
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parleur.speake("neuf");
            }
        });
        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parleur.speake("supprimer");
            }
        });
        detecte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parleur.speake("Trouver le bus");
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parleur.speake("commencer la recherche");
            }
        });
        btn0.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                text.setText(text.getText()+""+btn0.getText());
                return true;
            }
        });
        btn1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                text.setText(text.getText()+""+btn1.getText());
                return true;
            }
        });
        btn2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                text.setText(text.getText()+""+btn2.getText());
                return true;
            }
        });
        btn3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                text.setText(text.getText()+""+btn3.getText());
                return true;
            }
        });
        btn4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                text.setText(text.getText()+""+btn4.getText());
                return true;
            }
        });
        btn5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                text.setText(text.getText()+""+btn5.getText());
                return true;
            }
        });
        btn6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                text.setText(text.getText()+""+btn6.getText());
                return true;
            }
        });
        btn7.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                text.setText(text.getText()+""+btn7.getText());
                return true;
            }
        });
        btn8.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                text.setText(text.getText()+""+btn8.getText());
                return true;
            }
        });
        btn9.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                text.setText(text.getText()+""+btn9.getText());
                return true;
            }
        });
        start.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                bus=text.getText().toString();
                Intent intent =new Intent(getApplicationContext(),MapsActivity.class);
                //Toast.makeText(select_bus.this, "avant start", Toast.LENGTH_SHORT).show();
                startActivityForResult(intent,2);
                //Toast.makeText(select_bus.this, "apès start", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
        supprimer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                text.setText(text.getText().subSequence(0,text.length()-1));
                return true;
            }
        });
        detecte.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                parleur.speake("vous voulez chercher le bus numéro "+text.getText());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                parleur.speechRecognizer.startListening(intent);
                return true;
            }
        });
    }

}

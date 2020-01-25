package ma.ayyou.bus_detector;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class speaker extends AppCompatActivity {
    private TextToSpeech myTTs;
    private Context context;
    public SpeechRecognizer speechRecognizer;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public speaker(Context context) {
        this.context=context;
        }
///méthode speak
    public void speake(String message) {
        if(Build.VERSION.SDK_INT>21){
            myTTs.speak(message, TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else{
            myTTs.speak(message,TextToSpeech.QUEUE_FLUSH,null);

        }
    }
    ///methode pour initialiser le recognizer qui gère la voix
    public void initializespeechRecognizer() {
        if(speechRecognizer.isRecognitionAvailable(context)){
            speechRecognizer=SpeechRecognizer.createSpeechRecognizer(context);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    Toast.makeText(context, "ready", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onBeginningOfSpeech() {
                    Toast.makeText(context, "begenning", Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onRmsChanged(float rmsdB) {



                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                    Toast.makeText(context, "bufferreceive", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onEndOfSpeech() {


                }

                @Override
                public void onError(int error) {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();


                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onResults(Bundle results) {
                    List<String> result=results.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
                    Toast.makeText(context, ""+result.get(0), Toast.LENGTH_SHORT).show();
                    try {
                        processresult(result.get(0));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onPartialResults(Bundle partialResults) {
                    Toast.makeText(context  , "partial", Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                    Toast.makeText(context, "event", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    ///methode appellé lorsque le speetchRecognizer est initialisé
    public void initializeTextToSpeech(final String menu) {
        myTTs=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTs.getEngines().size()==0){
                    Toast.makeText(context, "no tts", Toast.LENGTH_SHORT).show();
                }
                else{
                    myTTs.setLanguage(Locale.FRANCE);
                    speake(menu);
                }

            }
        });
    }
    ////méhode poiur gérer les commandes
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void processresult(String command) throws InterruptedException {
        command=command.toLowerCase();
            if(command.indexOf("quelle heure")!=-1){
                Date now=new Date();
                String time= DateUtils.formatDateTime(this,now.getTime(),DateUtils.FORMAT_SHOW_TIME);
                Toast.makeText(context, "time : "+time, Toast.LENGTH_SHORT).show();
                //speake("c'est"+time);
            }
        if(command.indexOf("oui")!=-1) {
            ///speake("je suis a votre service patienter une minute ");
            //Intent intent =new Intent(context,MapsActivity.class);
            //Toast.makeText(context, "avant start", Toast.LENGTH_SHORT).show();
            //startActivityForResult(intent,2);
           // Toast.makeText(context, "apès start", Toast.LENGTH_SHORT).show();

        }
        if(command.indexOf("yes")!=-1) {
            speake("I am at your service wait for a minute");}
        if(command.indexOf("bus")!=-1) {
            speake(" quel bus?");
            Thread.sleep(3000);
            Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
            speechRecognizer.startListening(intent);
        }
        if(command.indexOf("un")!=-1) {
            speake("d'accord");
            Thread.sleep(5000);
            Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
            speechRecognizer.startListening(intent);
        }

        if(command.indexOf("merci")!=-1) {
            speake("je vous en prie");
        }




    }

}

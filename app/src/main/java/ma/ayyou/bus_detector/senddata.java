package ma.ayyou.bus_detector;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryResult;

import java.util.Arrays;
public class senddata {
    //classe qui envoie les coordonnées en utilisant SDK PABNUB
    PubNub pubnub;
    public static int nbr_bus=0;
    Context context;
    public static MediaPlayer mediaPlayer;
    speaker parleur;
    public  boolean[] your_bus;
    public static boolean play=false;
    static String[] info=new String[3];
    String name_channel;
    private int j;
    PNConfiguration pnConfiguration;
    @RequiresApi(api = Build.VERSION_CODES.M)

    public senddata(Context context){
    ///construteur pour configurer la connexion
    this.context=context;
    pnConfiguration = new PNConfiguration();
    pnConfiguration.setSubscribeKey("sub-c-51413e6a-1398-11ea-ad5f-8ede71033476");///clé pour écouter sur une chaine
    pnConfiguration.setPublishKey("pub-c-36e81a45-2037-4095-bdec-760b961f5c24");///clé pour envoyer sur une chaine
    name_channel="bus";///nom de la chaine
    pnConfiguration.setSecure(false);
    your_bus = new boolean[100];
    pubnub = new PubNub(pnConfiguration);
    pubnub.subscribe()////commencer a écouter
            .channels(Arrays.asList(name_channel)) // subscribe to channels
            .withPresence()
            .execute();
}
///méthode pour envoyer
public void envoyer(String bus,String longitude,String latitude){
    pubnub.publish()
            .message(Arrays.asList(bus,longitude,latitude))///le message à envoyer
            .channel(name_channel)
            .async(new PNCallback<PNPublishResult>() {
                ///callback aprèe l'envoie pour
                @Override
                public void onResponse(PNPublishResult result, PNStatus status) {

                }
            });
}
///méthode pour récupérer les données
@RequiresApi(api = Build.VERSION_CODES.M)
public void getdata(){
    j=0;
    pubnub.history()
            .channel(name_channel) // where to fetch history from
            .count(30) // how many items to fetch 1 le dernier message
            .async(new PNCallback<PNHistoryResult>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onResponse(PNHistoryResult result, PNStatus status) {
                        for (int i = 29; i >= 0; i--) {
                            /// Toast.makeText(context, "bus : "+result.getMessages().get(i).getEntry().getAsJsonArray().get(0).getAsInt(), Toast.LENGTH_SHORT).show();
                            if (result.getMessages().get(i).getEntry().getAsJsonArray().get(2).getAsDouble() != 0.0 && result.getMessages().get(i).getEntry().getAsJsonArray().get(1).getAsDouble() != 0.0) {
                                int num_bus = result.getMessages().get(i).getEntry().getAsJsonArray().get(0).getAsInt();
                                //Toast.makeText(context, "le meme bus "+num_bus, Toast.LENGTH_SHORT).show();
                                if (num_bus == select_bus.bus) {
                                    your_bus[j] = true;
                                    nbr_bus++;
                                    j++;
                                    MapsActivity.log[29 - i] = result.getMessages().get(i).getEntry().getAsJsonArray().get(2).getAsDouble();
                                    MapsActivity.lat[29 - i] = result.getMessages().get(i).getEntry().getAsJsonArray().get(1).getAsDouble();
                                }
                            }
                        }
                }
            });

}
}

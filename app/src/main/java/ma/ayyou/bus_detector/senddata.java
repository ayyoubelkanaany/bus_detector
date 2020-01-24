package ma.ayyou.bus_detector;
import android.content.Context;
import android.widget.Toast;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import java.util.Arrays;

public class senddata {
    //classe qui envoie les coordonnées en utilisant PABNUB c'est une SDK payant lorsque le volume des données transmées devient important
    PubNub pubnub;
    Context context;
    static String[] info=new String[3];
    String name_channel;
    PNConfiguration pnConfiguration;
public senddata(Context context){
    ///construteur pour configurer la connexion
    this.context=context;
    pnConfiguration = new PNConfiguration();
    pnConfiguration.setSubscribeKey("sub-c-51413e6a-1398-11ea-ad5f-8ede71033476");///clé pour écouter sur une chaine
    pnConfiguration.setPublishKey("pub-c-36e81a45-2037-4095-bdec-760b961f5c24");///clé pour envoyer sur une chaine
    name_channel="bus";///nom de la chaine
    pnConfiguration.setSecure(false);
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
                    // handle publish result, status always present, result if successful
                    // status.isError to see if error happened
                    Toast.makeText(context,"time : "+result.getTimetoken(),Toast.LENGTH_SHORT).show();
                    getdata();
                }
            });

}
///méthode pour récupérer les données
public void getdata(){
    pubnub.history()
            .channel(name_channel) // where to fetch history from
            .count(1) // how many items to fetch 1 le dernier message
            .async(new PNCallback<PNHistoryResult>() {
                @Override
                public void onResponse(PNHistoryResult result, PNStatus status) {
                    Toast.makeText(context, "log : "+result.getMessages().get(0).getEntry().getAsJsonArray().get(2).getAsDouble(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "lat : "+result.getMessages().get(0).getEntry().getAsJsonArray().get(1).getAsDouble(), Toast.LENGTH_SHORT).show();
                    MapsActivity.log=result.getMessages().get(0).getEntry().getAsJsonArray().get(2).getAsDouble();
                    MapsActivity.lat=result.getMessages().get(0).getEntry().getAsJsonArray().get(1).getAsDouble();
                    String num_bus = result.getMessages().get(0).getEntry().getAsJsonArray().get(0).toString();

                }
            });

}
}

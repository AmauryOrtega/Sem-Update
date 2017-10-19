package aroc.admin;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{
    private static final String TAG = "MyFirabaseInsIDService";

    @Override
    public void onTokenRefresh() {
        // Obteniendo token actualizado
        String token_nuevo = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Token actualizado: " + token_nuevo);

        // Aqui se puede guardar el token en otros servidores
    }
}

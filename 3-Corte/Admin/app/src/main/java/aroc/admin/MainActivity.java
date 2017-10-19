package aroc.admin;

// ID Firebase: fir-admin-78c2b

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button botonToken = (Button)findViewById(R.id.button_token);
        botonToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obteniendo token
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.i(TAG, "Token: " + token);
                Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package app.remotebindingserver.com.remotebindingserver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    
    private static final String TAG = MainActivity.class.getSimpleName();
    
    private Button startBoundServiceButton, stopBoundServiceButton;
    private Intent serviceIntent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        startBoundServiceButton = findViewById(R.id.start_bound_service_button_id);
        stopBoundServiceButton = findViewById(R.id.stop_bound_service_button_id);
        
        startBoundServiceButton.setOnClickListener(this);
        stopBoundServiceButton.setOnClickListener(this);
        
        serviceIntent = new Intent(this, BoundService.class);
    }
    
    @Override
    public void onClick(View view) {
        int id = view.getId();
        
        switch (id) {
            case R.id.start_bound_service_button_id:
                startService(serviceIntent);
                Toast.makeText(getApplicationContext(),"Service Started",Toast.LENGTH_LONG).show();
                Log.e(TAG,"Service started");
                break;
            case R.id.stop_bound_service_button_id:
                Toast.makeText(getApplicationContext(),"Service stopped",Toast.LENGTH_LONG).show();
                Log.e(TAG,"Service stopped");
                stopService(serviceIntent);
                break;
        }
    }
}

package com.example.dairogarcia.serviceout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


//Que bonita es la vida

public class MainActivity extends AppCompatActivity {
    /**
     * Text views para los datos
     */
    private TextView memoryUsageText;
    private TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener las etiquetas
        memoryUsageText = (TextView) findViewById(R.id.memory_ava_text);
        progressText = (TextView) findViewById(R.id.progress_text);

        // Obtener botón de monitoreo de memoria
        ToggleButton button = (ToggleButton) findViewById(R.id.toggleButton);

        // Setear escucha de acción
        button.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Intent intentMemoryService = new Intent(
                                getApplicationContext(), MemoryService.class);
                        if (isChecked) {
                            startService(intentMemoryService); //Iniciar servicio
                        } else {
                            stopService(intentMemoryService); // Detener servicio
                        }
                    }
                }
        );


        // Filtro de acciones que serán alertadas
        IntentFilter filter = new IntentFilter(
                Constants.ACTION_RUN_ISERVICE);
        filter.addAction(Constants.ACTION_RUN_SERVICE);
        filter.addAction(Constants.ACTION_MEMORY_EXIT);
        filter.addAction(Constants.ACTION_PROGRESS_EXIT);

        // Crear un nuevo ResponseReceiver
        ResponseReceiver receiver =
                new ResponseReceiver();
        // Registrar el receiver y su filtro
        LocalBroadcastManager.getInstance(this).registerReceiver(
                receiver,
                filter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Broadcast receiver que recibe las emisiones desde los servicios
    private class ResponseReceiver extends BroadcastReceiver {

        // Sin instancias
        private ResponseReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constants.ACTION_RUN_SERVICE:
                    memoryUsageText.setText(intent.getStringExtra(Constants.EXTRA_MEMORY));
                    Toast.makeText(getApplicationContext(), "ffff", Toast.LENGTH_SHORT).show();
                    break;

                case Constants.ACTION_RUN_ISERVICE:
                    progressText.setText(intent.getIntExtra(Constants.EXTRA_PROGRESS, -1) + "");
                    break;

                case Constants.ACTION_MEMORY_EXIT:
                    memoryUsageText.setText("Memoria");
                    break;

                case Constants.ACTION_PROGRESS_EXIT:
                    progressText.setText("Progreso");
                    break;
            }
        }
    }
}

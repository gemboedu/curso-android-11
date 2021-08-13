package com.gembodev.a03simpletorchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.gembodev.a03simpletorchapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private boolean torchStatus = false;
    private boolean torchDelayedStatus = false;
    private boolean isTorchAvailable;
    private CameraManager cameraManager;
    private String cameraId;
    private Handler handlerTorch;
    private final int DELAYED_TORCH = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        initApp();
        events();

    }

    private void initApp() {
        isTorchAvailable = getApplicationContext()
                .getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isTorchAvailable) {
            binding.ivTorch.setEnabled(false);
            Toast.makeText(MainActivity.this, "Torch is not available", Toast.LENGTH_SHORT).show();
        }
        handlerTorch = new Handler();

        try {
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (Exception e) {

        }
    }

    private void events() {
        binding.ivTorch.setOnClickListener(v -> {

            if (torchStatus) {
                // turn off
                torchStatus = false;
                Toast.makeText(MainActivity.this, "Torch is OFF", Toast.LENGTH_SHORT).show();
                binding.ivTorch.setImageResource(R.drawable.torch_on);
            } else {
                // turn on
                torchStatus = true;
                Toast.makeText(MainActivity.this, "Torch is ON", Toast.LENGTH_SHORT).show();
                binding.ivTorch.setImageResource(R.drawable.torch_off);
            }
            switchTorch(torchStatus);
        });


        binding.buttomStart.setOnClickListener(v -> {
            if (torchDelayedStatus) {
                torchDelayedStatus = false;
                handlerTorch.removeCallbacks(torchRunnable);
                switchTorch(false);
            } else {
                torchDelayedStatus = true;
                torchRunnable.run();
            }
        });


    }

    private Runnable torchRunnable = new Runnable() {
        @Override
        public void run() {
            if (torchStatus) {
                // turn off
                torchStatus = false;
            } else {
                // turn on
                torchStatus = true;
            }
            switchTorch(torchStatus);
            handlerTorch.postDelayed(this, DELAYED_TORCH);
        }
    };


    public void switchTorch(boolean status) {
        try {
            cameraManager.setTorchMode(cameraId, status);
        } catch (Exception e) {

        }
    }
}
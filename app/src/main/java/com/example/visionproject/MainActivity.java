package com.example.visionproject;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private PreviewView previewView;
    private ImageCapture imageCapture;

    private ImageView imageViewProcessada;
    private SeekBar seekBarThreshold1;
    private SeekBar seekBarThreshold2;
    private TextView txtThreshold1;
    private TextView txtThreshold2;

    private File ultimaFotoArquivo;
    private Bitmap ultimaBitmapOriginal;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startCamera();
                } else {
                    Toast.makeText(this, "Permissão da câmera negada", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(this, "OpenCV não inicializou", Toast.LENGTH_LONG).show();
        }

        previewView = findViewById(R.id.previewView);
        imageViewProcessada = findViewById(R.id.imageViewProcessada);
        seekBarThreshold1 = findViewById(R.id.seekBarThreshold1);
        seekBarThreshold2 = findViewById(R.id.seekBarThreshold2);
        txtThreshold1 = findViewById(R.id.txtThreshold1);
        txtThreshold2 = findViewById(R.id.txtThreshold2);

        Button btnCapturar = findViewById(R.id.btnCapturar);
        Button btnProcessar = findViewById(R.id.btnProcessar);

        atualizarTextosThreshold();

        btnCapturar.setOnClickListener(v -> tirarFoto());
        btnProcessar.setOnClickListener(v -> processarUltimaFoto(true));

        seekBarThreshold1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                atualizarTextosThreshold();
                processarUltimaFoto(false);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarThreshold2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                atualizarTextosThreshold();
                processarUltimaFoto(false);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void atualizarTextosThreshold() {
        txtThreshold1.setText("Limiar 1: " + seekBarThreshold1.getProgress());
        txtThreshold2.setText("Limiar 2: " + seekBarThreshold2.getProgress());
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder().build();

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture
                );

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao iniciar câmera", Toast.LENGTH_LONG).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void tirarFoto() {
        if (imageCapture == null) {
            Toast.makeText(this, "Câmera ainda não pronta", Toast.LENGTH_SHORT).show();
            return;
        }

        File pasta = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (pasta != null && !pasta.exists()) {
            pasta.mkdirs();
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        ultimaFotoArquivo = new File(pasta, "foto_" + timestamp + ".jpg");

        ImageCapture.OutputFileOptions outputOptions =
                new ImageCapture.OutputFileOptions.Builder(ultimaFotoArquivo).build();

        imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        ultimaBitmapOriginal = BitmapFactory.decodeFile(ultimaFotoArquivo.getAbsolutePath());

                        if (ultimaBitmapOriginal != null) {
                            imageViewProcessada.setImageBitmap(ultimaBitmapOriginal);
                            Toast.makeText(MainActivity.this,
                                    "Foto capturada com sucesso",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "Foto salva, mas não foi possível abrir a imagem",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        exception.printStackTrace();
                        Toast.makeText(MainActivity.this,
                                "Erro ao capturar foto",
                                Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void processarUltimaFoto(boolean salvarNaGaleria) {
        if (ultimaFotoArquivo == null || !ultimaFotoArquivo.exists()) {
            return;
        }

        if (ultimaBitmapOriginal == null) {
            ultimaBitmapOriginal = BitmapFactory.decodeFile(ultimaFotoArquivo.getAbsolutePath());
        }

        if (ultimaBitmapOriginal == null) {
            Toast.makeText(this, "Não foi possível carregar a foto", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap bitmapOriginal = ultimaBitmapOriginal.copy(Bitmap.Config.ARGB_8888, true);

        Mat matOriginal = new Mat();
        Utils.bitmapToMat(bitmapOriginal, matOriginal);

        Mat matCinza = new Mat();
        Imgproc.cvtColor(matOriginal, matCinza, Imgproc.COLOR_RGBA2GRAY);

        Mat matSuavizada = new Mat();
        Imgproc.GaussianBlur(matCinza, matSuavizada, new Size(5, 5), 0);

        double limiar1 = seekBarThreshold1.getProgress();
        double limiar2 = seekBarThreshold2.getProgress();
        if (limiar2 <= limiar1) {
            limiar2 = limiar1 + 1;
        }

        Mat matBordas = new Mat();
        Imgproc.Canny(matSuavizada, matBordas, limiar1, limiar2);

        Mat matCinzaRGBA = new Mat();
        Mat matSuavizadaRGBA = new Mat();
        Mat matBordasRGBA = new Mat();

        Imgproc.cvtColor(matCinza, matCinzaRGBA, Imgproc.COLOR_GRAY2RGBA);
        Imgproc.cvtColor(matSuavizada, matSuavizadaRGBA, Imgproc.COLOR_GRAY2RGBA);
        Imgproc.cvtColor(matBordas, matBordasRGBA, Imgproc.COLOR_GRAY2RGBA);

        Bitmap bitmapCinza = Bitmap.createBitmap(matCinzaRGBA.cols(), matCinzaRGBA.rows(), Bitmap.Config.ARGB_8888);
        Bitmap bitmapSuavizada = Bitmap.createBitmap(matSuavizadaRGBA.cols(), matSuavizadaRGBA.rows(), Bitmap.Config.ARGB_8888);
        Bitmap bitmapBordas = Bitmap.createBitmap(matBordasRGBA.cols(), matBordasRGBA.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(matCinzaRGBA, bitmapCinza);
        Utils.matToBitmap(matSuavizadaRGBA, bitmapSuavizada);
        Utils.matToBitmap(matBordasRGBA, bitmapBordas);

        imageViewProcessada.setImageBitmap(bitmapBordas);

        if (salvarNaGaleria) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

            salvarBitmapNaGaleria(bitmapOriginal, "original_" + timestamp + ".png");
            salvarBitmapNaGaleria(bitmapCinza, "cinza_" + timestamp + ".png");
            salvarBitmapNaGaleria(bitmapSuavizada, "suavizada_" + timestamp + ".png");
            salvarBitmapNaGaleria(bitmapBordas, "bordas_" + timestamp + ".png");

            Toast.makeText(this,
                    "4 imagens salvas na galeria",
                    Toast.LENGTH_LONG).show();
        }

        matOriginal.release();
        matCinza.release();
        matSuavizada.release();
        matBordas.release();
        matCinzaRGBA.release();
        matSuavizadaRGBA.release();
        matBordasRGBA.release();
    }

    private void salvarBitmapNaGaleria(Bitmap bitmap, String nomeArquivo) {
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, nomeArquivo);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/VisionProject");

            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (uri == null) {
                return;
            }

            OutputStream outputStream = getContentResolver().openOutputStream(uri);
            if (outputStream == null) {
                return;
            }

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
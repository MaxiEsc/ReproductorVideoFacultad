package com.maxescobar.mireproductorvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainVideo extends AppCompatActivity {

    private static final String VIDEO_SAMPLE = "mivideo";
    private VideoView mVideoView;

    private int mTiempoVideo = 0;
    private static final String PLAYBACK_TIME = "play_time";

    private Button btnPause_Play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video);

        mVideoView = (VideoView) findViewById(R.id.miVideo);
        btnPause_Play = (Button) findViewById(R.id.btnPause_Play);

        MediaController mMediaController = new MediaController(MainVideo.this);
        mMediaController.setMediaPlayer(mVideoView);

        if(savedInstanceState != null){
            mTiempoVideo = savedInstanceState.getInt(PLAYBACK_TIME);
        }
    }

    private Uri getMedia(String nombre){
        return Uri.parse("android.resource://" + getPackageName()+ "/raw/" + nombre);
    }

    private void inicializePlayer(){
        Uri videUri = getMedia(VIDEO_SAMPLE);
        mVideoView.setVideoURI(videUri);
        if(mTiempoVideo > 0){
            mVideoView.seekTo(mTiempoVideo);
        }else{
            mVideoView.seekTo(1);
        }
        mVideoView.start();
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()  {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(MainVideo.this,"Fin de la reproduccion",Toast.LENGTH_SHORT).show();
                mVideoView.seekTo(1);
            }
        });
    }


    private void releasePlayer(){

        mVideoView.stopPlayback();
    }

    @Override
    protected void onStart(){
        super.onStart();
        inicializePlayer();
    }

    @Override
    protected void onStop(){
        super.onStop();
        releasePlayer();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            mVideoView.pause();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(PLAYBACK_TIME,mVideoView.getCurrentPosition());
    }

    //Metodo para volver al primer activity del Menu
    public void btnVolver(View vista){
        Intent vuelta = new Intent(MainVideo.this, MainActivity.class);
        startActivity(vuelta);
    }

    //Metodo para pausar Video
    public void btnPausarReproducir(View vista){

        if (mVideoView.isPlaying()){
            mVideoView.pause();
            btnPause_Play.setBackgroundResource(R.drawable.play);
            Toast.makeText(MainVideo.this,"Pausado",Toast.LENGTH_SHORT).show();
        }else{
            mVideoView.start();
            btnPause_Play.setBackgroundResource(R.drawable.pause);
            Toast.makeText(MainVideo.this,"Reproduciendo",Toast.LENGTH_SHORT).show();
        }

    }

    //Metodo para el boton Atras
    public void btnAtras(View vista){
        if (mVideoView.canSeekBackward()){
            mVideoView.seekTo(1);
        }else{
            Toast.makeText(MainVideo.this,"No se puede retroceder mas",Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para el boton Siguiente
    public void btnSiguiente(View vista){
        if (mVideoView.canSeekForward()){
            mVideoView.seekTo(mVideoView.getDuration() - 1);
        }else{
            Toast.makeText(MainVideo.this,"No Se puede Adelantar mas",Toast.LENGTH_SHORT).show();
        }
    }

}
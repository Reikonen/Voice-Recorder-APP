package com.example.aplikasiperekamsuara.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;


import com.example.aplikasiperekamsuara.R;
import com.example.aplikasiperekamsuara.Services.RecordingService;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordFragment extends Fragment {

    @BindView(R.id.chronometer) Chronometer chronometer;
    @BindView(R.id.recording_status_txt) TextView recordingStatusTxt;
    @BindView(R.id.btnRecord) FloatingActionButton recordBtn;
    @BindView(R.id.btnPause) Button btnPause;


    private boolean mStartRecording = true;
    private boolean mPauseRecording = true;
    long timeWhenPaused = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View recordView = inflater.inflate(R.layout.fragment_record,container, false);
        ButterKnife.bind(this, recordView);
        return recordView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnPause.setVisibility(View.GONE);
        recordBtn.setColorPressed(getResources().getColor((R.color.colorPrimary)));
    }

    @OnClick(R.id.btnRecord)
    public void recordAudio()
    {
        onRecord(mStartRecording);
        mStartRecording = !mStartRecording;
    }
    private void onRecord(boolean start)
    {
        Intent intent = new Intent(getActivity(), RecordingService.class);

        if(start)
        {
            recordBtn.setImageResource(R.drawable.ic_media_stop);
            Toast.makeText(getContext(), "Rekaman Dimulai", Toast.LENGTH_LONG).show();

            File folder = new File(Environment.getExternalStorageDirectory()+"/MySoundRec");

            if(!folder.exists())
            {
                folder.mkdir();
            }

            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();

            getActivity().startService(intent);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            recordingStatusTxt.setText("Sedang Merekam...");
        }
        else
        {
            recordBtn.setImageResource(R.drawable.ic_mic_white);
            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
            timeWhenPaused=0;
            recordingStatusTxt.setText("Pencet tombol untuk merekam");

            getActivity().stopService((intent));
        }
    }

}

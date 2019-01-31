package com.example.sudo.hardtogetup.activity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sudo.hardtogetup.R;
import com.example.sudo.hardtogetup.models.MathProblem;
import com.example.sudo.hardtogetup.utils.RealmUtils;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tvTask)
    TextView tvTask;
    @BindView(R.id.tvAnswerOne)
    TextView tvAnswerOne;
    @BindView(R.id.tvAnswerTwo)
    TextView tvAnswerTwo;
    @BindView(R.id.tvAnswerThree)
    TextView tvAnswerThree;
    @BindView(R.id.tvAnswerFour)
    TextView tvAnswerFour;
    @BindView(R.id.rlAnswerOne)
    RelativeLayout rlAnswerOne;
    @BindView(R.id.rlAnswerTwo)
    RelativeLayout rlAnswerTwo;
    @BindView(R.id.rlAnswerThree)
    RelativeLayout rlAnswerThree;
    @BindView(R.id.rlAnswerFour)
    RelativeLayout rlAnswerFour;

    private ArrayList<MathProblem> mathProblems = new ArrayList<>();
    int randomQuestionNumber;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        Realm.init(getApplicationContext());
        //audiomanager koji je loop i koji ponavlja pjesmu sve dok se ne odgovori točno na pitanje
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.song);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        //ukoliko je uređaj u silent modu programski mu palimo zvučnike
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        sleep(1000);
                        audioManager.setMode(AudioManager.MODE_IN_CALL);
                        if (!audioManager.isSpeakerphoneOn())
                            audioManager.setSpeakerphoneOn(true);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
       // dohvaćamo sve podatke iz realma
        mathProblems.addAll(RealmUtils.getAllMAthProblems());
        rlAnswerOne.setOnClickListener(this);
        rlAnswerTwo.setOnClickListener(this);
        rlAnswerThree.setOnClickListener(this);
        rlAnswerFour.setOnClickListener(this);
//metoda za stvaranje pitanja
        getQuestion();
    }

    private void getQuestion() {

        Random randomNumber = new Random();
        //random trebamo da dohvatimo random pitanje za screen
        randomQuestionNumber = randomNumber.nextInt(mathProblems.size());
        //random place služi za postavljanje
        int randomPlace = randomNumber.nextInt(3);

        //ovisno koji je random place tako i postavljamo odgovore
        tvTask.setText(mathProblems.get(randomQuestionNumber).getProblem());
        switch (randomPlace) {
            case 0:
                tvAnswerOne.setText(mathProblems.get(randomQuestionNumber).getAnswer());
                tvAnswerTwo.setText(mathProblems.get(randomQuestionNumber).getWrongAnswer1());
                tvAnswerThree.setText(mathProblems.get(randomQuestionNumber).getWrongAnswer2());
                tvAnswerFour.setText(mathProblems.get(randomQuestionNumber).getWrongAnswer3());
                break;
            case 1:
                tvAnswerOne.setText(mathProblems.get(randomQuestionNumber).getWrongAnswer1());
                tvAnswerTwo.setText(mathProblems.get(randomQuestionNumber).getAnswer());
                tvAnswerThree.setText(mathProblems.get(randomQuestionNumber).getWrongAnswer2());
                tvAnswerFour.setText(mathProblems.get(randomQuestionNumber).getWrongAnswer3());
                break;
            case 2:
                tvAnswerOne.setText(mathProblems.get(randomQuestionNumber).getWrongAnswer1());
                tvAnswerTwo.setText(mathProblems.get(randomQuestionNumber).getWrongAnswer2());
                tvAnswerThree.setText(mathProblems.get(randomQuestionNumber).getAnswer());
                tvAnswerFour.setText(mathProblems.get(randomQuestionNumber).getWrongAnswer3());
                break;
            case 3:
                tvAnswerOne.setText(mathProblems.get(randomQuestionNumber).getWrongAnswer1());
                tvAnswerTwo.setText(mathProblems.get(randomQuestionNumber).getWrongAnswer2());
                tvAnswerThree.setText(mathProblems.get(randomQuestionNumber).getWrongAnswer3());
                tvAnswerFour.setText(mathProblems.get(randomQuestionNumber).getAnswer());
                break;

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rlAnswerOne : checkAnswer(tvAnswerOne);
            break;
            case R.id.rlAnswerTwo : checkAnswer(tvAnswerTwo);
                break;
            case R.id.rlAnswerThree : checkAnswer(tvAnswerThree);
                break;
            case R.id.rlAnswerFour : checkAnswer(tvAnswerFour);
                break;
        }
    }

    //provjera odgovora
    private void checkAnswer(TextView tvClickedAnswer) {
        if (tvClickedAnswer.getText().toString().equals( mathProblems.get(randomQuestionNumber).getAnswer())){
            finish();
        }
        else {
            getQuestion();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }
}

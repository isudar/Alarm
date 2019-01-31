package com.example.sudo.hardtogetup.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.sudo.hardtogetup.R;
import com.example.sudo.hardtogetup.models.MathProblem;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.example.sudo.hardtogetup.utils.UIUtils.showSnackBar;

public class NewMathProblemActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ivDismiss)
    ImageView ivDismiss;
    @BindView(R.id.etMathProblem)
    EditText etMathProblem;
    @BindView(R.id.etCorrectAnswer)
    EditText etCorrectAnswer;
    @BindView(R.id.etWrongAnswer1)
    EditText etWrongAnswer1;
    @BindView(R.id.etWrongAnswer2)
    EditText etWrongAnswer2;
    @BindView(R.id.etWrongAnswer3)
    EditText etWrongAnswer3;
    @BindView(R.id.buttonDone)
    Button buttonDone;
    @BindView(R.id.view)
    RelativeLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_math_problem);
        ButterKnife.bind(this);
        setUi();
        etMathProblem.requestFocus();
    }

    private void setUi() {
        buttonDone.setOnClickListener(this);
        ivDismiss.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivDismiss:
                finish();
                break;
            case R.id.buttonDone:
                validateEntries();
                break;
        }
    }

    //provjera dal je sve uneseno
    void validateEntries() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (etMathProblem.getText().toString().isEmpty() || etCorrectAnswer.getText().toString().isEmpty() || etWrongAnswer1.getText().toString().isEmpty()
                || etWrongAnswer2.getText().toString().isEmpty() || etWrongAnswer3.getText().toString().isEmpty())
            showSnackBar(view, getString(R.string.enter_all_data));
        else
            saveDataToRealmAndExit();

    }

    //spremanje podataka
    private void saveDataToRealmAndExit() {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        MathProblem mathProblem = new MathProblem();
        mathProblem.setProblem(etMathProblem.getText().toString());
        mathProblem.setAnswer(etCorrectAnswer.getText().toString());
        mathProblem.setWrongAnswer1(etWrongAnswer1.getText().toString());
        mathProblem.setWrongAnswer2(etWrongAnswer2.getText().toString());
        mathProblem.setWrongAnswer3(etWrongAnswer3.getText().toString());
        realm.copyToRealm(mathProblem);
        realm.commitTransaction();
        realm.close();
        finish();
    }
}

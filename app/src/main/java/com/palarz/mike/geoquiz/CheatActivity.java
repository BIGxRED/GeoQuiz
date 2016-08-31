package com.palarz.mike.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CheatActivity extends AppCompatActivity {

    //Keys for Intent objects
    private static final String EXTRA_ANSWER_IS_TRUE =  //Key for Intent from QuizActivity
            "com.palarz.mike.geoquiz.answer_is_true";
    private static final String EXTRA_IS_ANSWER_SHOWN = //Key for Intent to QuizActivity
            "com.palarz.mike.geoquiz.answer_shown";

    //Key for mIsAnswerShown
    private static final String KEY_IS_ANSWER_SHOWN =
            "com.palarz.mike.geoquiz.is_answer_shown";

    private boolean mAnswerIsTrue;  //Answer received from QuizActivity

    //Stores the result if the user actually pressed the Cheat button
    private boolean mIsAnswerShown = false;

    //All references to the views within the layout
    private TextView mAnswerTextView;
    private Button mShowAnswer;

    //Tag used for debugging
    private static final String TAG = "CheatActivity";

    //Used by QuizActivity to send Intent to CheatActivity
    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    //Prepares the Intent to be sent back to QuizActivity
    private void setAnswerShownResult(){
        Intent data = new Intent();
        data.putExtra(EXTRA_IS_ANSWER_SHOWN,mIsAnswerShown);
        setResult(RESULT_OK, data);
    }

    //Returns the value of mIsAnswerShown; used by QuizActivity
    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_IS_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAnswerIsTrue)
                    mAnswerTextView.setText(R.string.true_button);
                else
                    mAnswerTextView.setText(R.string.false_button);
                mIsAnswerShown = true;
                setAnswerShownResult();
            }
        });

        if(savedInstanceState != null) {
            mIsAnswerShown = savedInstanceState.getBoolean(KEY_IS_ANSWER_SHOWN, false);
            //Crucial line!!! We want to know if the user cheated or not, regardless if they pressed
            //the cheat button after screen was re-oriented
            setAnswerShownResult();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_IS_ANSWER_SHOWN,mIsAnswerShown);
    }
}

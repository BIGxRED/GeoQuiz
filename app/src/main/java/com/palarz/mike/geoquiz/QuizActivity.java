package com.palarz.mike.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class QuizActivity extends AppCompatActivity {

    //All references to the views within the layout
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;

    //Array of questions
    private Question [] mQuestionBank = new Question[] {
            new Question(R.string.question_oceans,true, false),
            new Question(R.string.question_mideast, false, false),
            new Question(R.string.question_africa, false, false),
            new Question(R.string.question_americas, true, false),
            new Question(R.string.question_asia,true, false)
    };

    //Index used to keep track of which question should be displayed next
    private int mCurrentIndex = 0;

    private static final String TAG = "QuizActivity";   //tag used for logging/debugging

    //Keys that are used within onSavedInstanceState()
    private static final String KEY_INDEX = "index";
    private static final String KEY_QUESTION_BANK = "questionBank";

    //Request code used for startActivityForResult()
    private static final int REQUEST_CODE_CHEAT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(false);
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Rollover iteration through array; nifty!
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex--;
                if (mCurrentIndex < 0)
                    mCurrentIndex = mQuestionBank.length - 1;
                updateQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent cheatIntent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(cheatIntent, REQUEST_CODE_CHEAT);
            }
        });

        if(savedInstanceState != null){
            //We want to be sure that mCurrentIndex and mQuestionBank are restored if orientation
            //is flipped
            //mQuestionBank needs to be stored so that the mIsCheater parameter within each instance
            //of the classes are retained
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
            mQuestionBank = (Question[]) savedInstanceState.getParcelableArray(KEY_QUESTION_BANK);
        }

        //Display the first question in mQuestionBank right when the activity is started
        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.d(TAG, "onActivityResult()");
        if(resultCode != Activity.RESULT_OK)
            return;
        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null)
                return;
            //If resultCode is RESULT_OK and the requestCode is 0 (or REQUEST_CODE_CHEAT),
            //then set the mIsCheater paramter of the current Question object accordingly
            mQuestionBank[mCurrentIndex].setIsCheater(CheatActivity.wasAnswerShown(data));
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState()");
        //Save mCurrentIndex so that the same question is displayed as prior to orientation flip
        //Save mQuestionBank so that the results of mIsCheater are kept
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
        savedInstanceState.putParcelableArray(KEY_QUESTION_BANK, mQuestionBank);
    }

    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResID();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResID = 0;

        if(mQuestionBank[mCurrentIndex].getIsCheater())
            messageResID = R.string.judgement_toast;

        else {
            if (userPressedTrue == answerIsTrue)
                messageResID = R.string.correct_toast;
            else
                messageResID = R.string.incorrect_toast;
        }

        Toast.makeText(this, messageResID, Toast.LENGTH_SHORT).show();
    }
}

package in.ac.iiitd.gursimran14041.primality_quiz_mc_assign2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    /* ***************************** Defining TAGS ****************************************/
    private final String LOG_TAG = "LOG:main_activity";
    private final String SCORE_SAVE_TAG = "UserScore";
    private final String NUMBER_SAVE_TAG = "CurrentNumber";
    private final String PRIMALITY_SAVE_TAG = "CurrentPrimality";
    private final String BUTTON_STATE_SAVE_TAG = "ButtonEnabled";
    private final String COLOR_BAR_SAVE_TAG = "ColorBar_Color";
    private final String SOURCE_INTENT_TAG = "IntentSource";
    private final String NUMBER_INTENT_TAG = "CurrentNumber";
    private final String PRIMALITY_INTENT_TAG = "CurrentPrimality";
    private final String SCORE_INTENT_TAG = "CurrentScore";
    private final String CHEAT_INTENT_TAG = "IfCheatUsed";
    private final String HINT_INTENT_TAG = "IfHintTaken";
    /* ***************************** A Context Name **********************************************/
    private final String THIS_CONTEXT = "activity_main";
    private final MainActivity CONTEXT_MAIN = this;
    /* ************ Defining holders for all views in R.layout.activity_main *********************/
    private Button
            mYes_Button,
            mNo_Button,
            mHint_Button,
            mCheat_Button,
            mNext_Button;
    private TextView
            mScore_TextView,
            mQuestion_TextView;
    private View
            mColorBar;
    /* ******************************* Defining Data Holders ************************************/
    private long mUserScore;
    private int
            mCurrentNumber,
            mColorBar_Color;
    private boolean
            mCurrentPrimality,
            mButtons_State,
            mHintTaken,
            mCheated;

    /* ******************** Declaring Default Values for Views **********************************/
    private int
            mColorBar_DEFAULT_BGCOLOR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Logging
        Log.i(LOG_TAG, "onCreate");

        /* ********* Binding class members to view objects **********************/
        // mScore_TextView --> main_score_TextView
        mScore_TextView = (TextView) findViewById(R.id.main_score_TextView);
        // mQuestion_TextView --> main_question_TextView
        mQuestion_TextView = (TextView) findViewById(R.id.main_question_TextView);
        // mYes_Button --> main_yes_Button
        mYes_Button = (Button) findViewById(R.id.main_yes_Button);
        // mNo_Button --> main_no_Button
        mNo_Button = (Button) findViewById(R.id.main_no_Button);
        // mHint_Button --> main_hint_Button
        mHint_Button = (Button) findViewById(R.id.main_hint_Button);
        // mCheat_Button --> main_cheat_Button
        mCheat_Button = (Button) findViewById(R.id.main_cheat_Button);
        // mNext_Button --> main_next_Button
        mNext_Button = (Button) findViewById(R.id.main_next_Button);
        // mColorBar --> main_colourBar
        mColorBar = findViewById(R.id.main_colorBar);

        /* *************** Default Values ***************************************/
        mColorBar_DEFAULT_BGCOLOR = Color.WHITE;

        /* ********* Define onClick Functions for Buttons ***********************/
        // mYes_Button
        mYes_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateResponse(view);
            }
        });
        // mNo_Button
        mNo_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateResponse(view);
            }
        });
        // mHint_Button
        mHint_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call Hint Activity
                Intent hintActivity = new Intent(CONTEXT_MAIN, HintActivity.class);
                hintActivity.putExtra(SOURCE_INTENT_TAG, THIS_CONTEXT);
                hintActivity.putExtra(NUMBER_INTENT_TAG, mCurrentNumber);
                hintActivity.putExtra(PRIMALITY_INTENT_TAG, mCurrentPrimality);
                hintActivity.putExtra(SCORE_INTENT_TAG, mUserScore);
                startActivity(hintActivity);
            }
        });
        // mCheat_Button
        mCheat_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call Cheat Activity
                Intent cheatActivity = new Intent(CONTEXT_MAIN, CheatActivity.class);
                cheatActivity.putExtra(SOURCE_INTENT_TAG, THIS_CONTEXT);
                cheatActivity.putExtra(NUMBER_INTENT_TAG, mCurrentNumber);
                cheatActivity.putExtra(PRIMALITY_INTENT_TAG, mCurrentPrimality);
                cheatActivity.putExtra(SCORE_INTENT_TAG, mUserScore);
                startActivity(cheatActivity);
            }
        });
        // mNext_Button
        mNext_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetEnvironment();
                generateNewDataSet();
                setUpView();
            }
        });

        /* ************* Check if called by an Intent ***************************/
        Intent callerIntent = getIntent();
        Bundle extras = callerIntent.getExtras();

        if (extras != null && extras.getString(SOURCE_INTENT_TAG).compareTo("activity_hint") == 0) {
            mHintTaken = extras.getBoolean(HINT_INTENT_TAG);
            mCurrentNumber = extras.getInt(NUMBER_INTENT_TAG);
            mCurrentPrimality = extras.getBoolean(PRIMALITY_INTENT_TAG);
            mUserScore = extras.getLong(SCORE_INTENT_TAG);
            mButtons_State = true;
            mColorBar_Color = Color.WHITE;
            setUpView();
        } else if (extras != null && extras.getString(SOURCE_INTENT_TAG).compareTo("activity_cheat") == 0) {
            mCheated = extras.getBoolean(CHEAT_INTENT_TAG);
            mCurrentNumber = extras.getInt(NUMBER_INTENT_TAG);
            mCurrentPrimality = extras.getBoolean(PRIMALITY_INTENT_TAG);
            mUserScore = extras.getLong(SCORE_INTENT_TAG);
            mButtons_State = true;
            mColorBar_Color = Color.WHITE;
            setUpView();
        } else {
            /* ******* Check savedStateBundle for state resume **********************/
            if (savedInstanceState == null) {
            /* ******** Creating a new State for this Activity ***********/
                // Initialize Data Members
                initializeAllData();
                // Kick start the first function to start the game
                generateNewDataSet();
                setUpView();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(LOG_TAG, "onSaveInstanceState");

        outState.putLong(SCORE_SAVE_TAG, mUserScore);
        outState.putInt(NUMBER_SAVE_TAG, mCurrentNumber);
        outState.putBoolean(PRIMALITY_SAVE_TAG, mCurrentPrimality);
        outState.putBoolean(BUTTON_STATE_SAVE_TAG, mButtons_State);
        outState.putInt(COLOR_BAR_SAVE_TAG, mColorBar_Color);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onRestoreInstanceState");

        mUserScore = savedInstanceState.getLong(SCORE_SAVE_TAG);
        mCurrentNumber = savedInstanceState.getInt(NUMBER_SAVE_TAG);
        mCurrentPrimality = savedInstanceState.getBoolean(PRIMALITY_SAVE_TAG);
        mButtons_State = savedInstanceState.getBoolean(BUTTON_STATE_SAVE_TAG);
        mColorBar_Color = savedInstanceState.getInt(COLOR_BAR_SAVE_TAG);
        setUpView();
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        Log.i(LOG_TAG, "onDestroy");
        super.onDestroy();
    }

    /******************************
     * App specific methods
     ****************************************/

    private void initializeAllData() {
        this.mUserScore = 0;
        this.mCurrentNumber = 0;
        this.mCurrentPrimality = false;
        this.mButtons_State = true;
        this.mColorBar_Color = mColorBar_DEFAULT_BGCOLOR;
        this.mHintTaken = false;
        this.mCheated = false;
    }

    private void resetEnvironment() {
        // Reset Data Values
        this.mCurrentNumber = 0;
        this.mCurrentPrimality = false;
        this.mHintTaken = false;
        this.mCheated = false;

        // Reset Specific changes that might have been made along the way
        mButtons_State = true;
        mColorBar_Color = mColorBar_DEFAULT_BGCOLOR;
    }

    private void setUpView() {
        Spanned questionText;
        if (mHintTaken) {
            questionText = Html.fromHtml("Is <b>" + Integer.toString(mCurrentNumber) +
                    "</b> a Prime Number ?<br /><small>Hint has been Used</small>");
        } else if (mCheated) {
            questionText = Html.fromHtml("Is <b>" + Integer.toString(mCurrentNumber) +
                    "</b> a Prime Number ?<br /><small>Cheat has been Used</small>");
        } else {
            questionText = Html.fromHtml("Is <b>" + Integer.toString(mCurrentNumber) +
                    "</b> a Prime Number ?");
        }

        mScore_TextView.setText(Long.toString(mUserScore));
        mQuestion_TextView.setText(questionText);

        setColorBar(mColorBar_Color);
        setButtonsEnabledState(mButtons_State);
    }

    private void generateNewDataSet() {
        Random rand = new Random();
        mCurrentNumber = rand.nextInt(1000) + 1;

        mCurrentPrimality = true;
        for (int i = 2; i <= Math.sqrt(mCurrentNumber) + 1; i++) {
            if (mCurrentNumber % i == 0) {
                mCurrentPrimality = false;
                break;
            }
        }
    }

    private void updateScore(long update) {
        mUserScore += update;
    }

    private void responseAction(boolean correct) {
        if (correct) {
            updateScore(10);
            Toast.makeText(this, "CORRECT", Toast.LENGTH_SHORT).show();
            mColorBar_Color = Color.GREEN;
        } else {
            updateScore(-10);
            Toast.makeText(this, "INCORRECT", Toast.LENGTH_SHORT).show();
            mColorBar_Color = Color.RED;
        }
    }

    private void validateResponse(View clickedButton) {
        // Disable the buttons
        mButtons_State = false;

        if (clickedButton.getId() == mYes_Button.getId()) {
            // Yes Button Clicked
            if (mCurrentPrimality) {
                // Current Number is Prime --> CORRECT
                responseAction(true);
            } else {
                // Current Number is Composite --> INCORRECT
                responseAction(false);
            }
        } else {
            // No Button Clicked
            if (mCurrentPrimality) {
                // Current Number is Prime --> INCORRECT
                responseAction(false);
            } else {
                // Current Number is Composite --> CORRECT
                responseAction(true);
            }
        }

        setUpView();
    }

    private void setButtonsEnabledState(boolean state) {
        this.mYes_Button.setEnabled(state);
        this.mNo_Button.setEnabled(state);
        this.mHint_Button.setEnabled(state);
        this.mCheat_Button.setEnabled(state);

        mButtons_State = state;
    }

    private void setColorBar(int colour) {
        mColorBar.setBackgroundColor(colour);
        mColorBar_Color = colour;
    }
}

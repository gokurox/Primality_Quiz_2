package in.ac.iiitd.gursimran14041.primality_quiz_mc_assign2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {


    /* ************************** Defining TAGS **************************************************/
    private final String LOG_TAG = "LOG:cheat_activity";
    private final String THIS_CONTEXT = "activity_cheat";
    private final String CHEAT_SAVE_TAG = "CheatUsed";
    private final String NUMBER_SAVE_TAG = "CurrentNumber";
    private final String SOURCE_INTENT_TAG = "IntentSource";
    private final String NUMBER_INTENT_TAG = "CurrentNumber";
    private final String PRIMALITY_INTENT_TAG = "CurrentPrimality";
    private final String SCORE_INTENT_TAG = "CurrentScore";
    private final String CHEAT_INTENT_TAG = "IfCheatUsed";
    private final String HINT_INTENT_TAG = "IfHintTaken";
    /* ************ Defining holders for all views in R.layout.activity_main *********************/
    private Button
            mGenerateCheat_Button;
    private TextView
            mCheatText_TextView;
    private CheatActivity CONTEXT_CHEAT = this;

    private int mNumber;
    private long mUserScore;
    private boolean mCheatUsed, mPrimality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        Log.i(LOG_TAG, "onCreate");

        /************************** Bind Views to Objects ****************************************/
        mGenerateCheat_Button = (Button) findViewById(R.id.cheat_generateCheat_Button);
        mCheatText_TextView = (TextView) findViewById(R.id.cheat_cheatText_TextView);

        Intent mainActivity = getIntent();
        Bundle extras = mainActivity.getExtras();
        mNumber = extras.getInt(NUMBER_INTENT_TAG);
        mPrimality = extras.getBoolean(PRIMALITY_INTENT_TAG);
        mUserScore = extras.getLong(SCORE_INTENT_TAG);
        mCheatUsed = false;

        mGenerateCheat_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheatUsed = true;
                setView();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(LOG_TAG, "onSaveInstanceState");

        outState.putInt(NUMBER_SAVE_TAG, mNumber);
        outState.putBoolean(CHEAT_SAVE_TAG, mCheatUsed);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onRestoreInstanceState");

        mNumber = savedInstanceState.getInt(NUMBER_SAVE_TAG);
        mCheatUsed = savedInstanceState.getBoolean(CHEAT_SAVE_TAG);
        setView();
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        Log.i(LOG_TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(CONTEXT_CHEAT, MainActivity.class);
        mainActivity.putExtra(SOURCE_INTENT_TAG, THIS_CONTEXT);
        mainActivity.putExtra(CHEAT_INTENT_TAG, mCheatUsed);
        mainActivity.putExtra(NUMBER_INTENT_TAG, mNumber);
        mainActivity.putExtra(PRIMALITY_INTENT_TAG, mPrimality);
        mainActivity.putExtra(SCORE_INTENT_TAG, mUserScore);
        startActivity(mainActivity);
        super.onBackPressed();
    }

    private Boolean isPrime() {
        boolean isPrime = true;

        for (int i = 2; i <= Math.sqrt(mNumber) + 1; i++) {
            if (mNumber % i == 0) {
                isPrime = false;
                break;
            }
        }

        return isPrime;
    }

    private void setView() {
        String hintText = String.format("%d is ", mNumber);

        if (isPrime()) {
            hintText += "Prime.";
        } else {
            hintText += "Not Prime.";
        }
        mCheatText_TextView.setText(hintText);
    }
}

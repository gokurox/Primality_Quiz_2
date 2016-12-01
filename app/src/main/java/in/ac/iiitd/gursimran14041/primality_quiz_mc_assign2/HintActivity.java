package in.ac.iiitd.gursimran14041.primality_quiz_mc_assign2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashSet;

public class HintActivity extends AppCompatActivity {

    /* ************************** Defining TAGS **************************************************/
    private final String LOG_TAG = "LOG:hint_activity";
    private final String THIS_CONTEXT = "activity_hint";
    private final String HINT_SAVE_TAG = "HintUsed";
    private final String NUMBER_SAVE_TAG = "CurrentNumber";
    private final String SOURCE_INTENT_TAG = "IntentSource";
    private final String NUMBER_INTENT_TAG = "CurrentNumber";
    private final String PRIMALITY_INTENT_TAG = "CurrentPrimality";
    private final String SCORE_INTENT_TAG = "CurrentScore";
    private final String CHEAT_INTENT_TAG = "IfCheatUsed";
    private final String HINT_INTENT_TAG = "IfHintTaken";
    /* ************ Defining holders for all views in R.layout.activity_main *********************/
    private Button
            mGenerateHint_Button;
    private GridView
            mFactors_GridView;
    private TextView
            mHintText_TextView;
    private HintActivity CONTEXT_HINT = this;

    private int mNumber;
    private long mUserScore;
    private boolean mHintUsed, mPrimality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);

        Log.i(LOG_TAG, "onCreate");

        /************************** Bind Views to Objects ****************************************/
        mGenerateHint_Button = (Button) findViewById(R.id.hint_generateHint_Button);
        mFactors_GridView = (GridView) findViewById(R.id.hint_factors_GridView);
        mHintText_TextView = (TextView) findViewById(R.id.hint_hintText_TextView);

        Intent mainActivity = getIntent();
        Bundle extras = mainActivity.getExtras();
        mNumber = extras.getInt(NUMBER_INTENT_TAG);
        mPrimality = extras.getBoolean(PRIMALITY_INTENT_TAG);
        mUserScore = extras.getLong(SCORE_INTENT_TAG);
        mHintUsed = false;

        mGenerateHint_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHintUsed = true;
                setView();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(LOG_TAG, "onSaveInstanceState");

        outState.putInt(NUMBER_SAVE_TAG, mNumber);
        outState.putBoolean(HINT_SAVE_TAG, mHintUsed);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onRestoreInstanceState");

        mNumber = savedInstanceState.getInt(NUMBER_SAVE_TAG);
        mHintUsed = savedInstanceState.getBoolean(HINT_SAVE_TAG);
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
        Intent mainActivity = new Intent(CONTEXT_HINT, MainActivity.class);
        mainActivity.putExtra(SOURCE_INTENT_TAG, THIS_CONTEXT);
        mainActivity.putExtra(HINT_INTENT_TAG, mHintUsed);
        mainActivity.putExtra(NUMBER_INTENT_TAG, mNumber);
        mainActivity.putExtra(PRIMALITY_INTENT_TAG, mPrimality);
        mainActivity.putExtra(SCORE_INTENT_TAG, mUserScore);
        startActivity(mainActivity);
        super.onBackPressed();
    }

    private HashSet<Integer> generateFactors() {
        HashSet<Integer> numFactors = new HashSet<>();

        for (int i = 1; i <= mNumber; i++) {
            if (mNumber % i == 0) {
                numFactors.add(i);
            }
        }

        return numFactors;
    }

    private void setView() {
        String hintText = String.format("Factors of %d are as follows:\n", mNumber);

        HashSet<Integer> T = generateFactors();
        Integer[] A = T.toArray(new Integer[T.size()]);
        Arrays.sort(A);
        for (int i = 0; i < A.length; i++) {
            hintText += A[i];
            if (i < A.length - 1)
                hintText += ", ";
        }
        mHintText_TextView.setText(hintText);
    }
}

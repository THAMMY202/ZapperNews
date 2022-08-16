package com.thamsanqa.zappernews;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.thamsanqa.zappernews.ui.ListFragment;

/**
 * This activity is the app entry point and it fetch data from the api
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.FragmentContainer,new ListFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
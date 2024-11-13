package com.inoi.todolistapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.widget.TextView;

import com.inoi.todolistapps.Fragments.RegisterFragment;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


        showFragment(new RegisterFragment(), "RegFrag");
    }

    private void showFragment(Fragment fragment, String tag){
        try {
            fragment.setEnterTransition(new Slide(Gravity.END));
            fragment.setExitTransition(new Slide(Gravity.START));
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.container_main, fragment, tag);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
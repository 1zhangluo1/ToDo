package com.example.needtodo;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SetTime extends BaseActivity {

    private BottomNavigationView bottomNavigationView;
    private OutList outList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);

    }
    private void bottomNavi(int position) {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(fragmentTransaction);
        if (position==1){
            if (outList==null){
                outList = new OutList();
                fragmentTransaction.add(R.id.todo_list,outList);}
            else {fragmentTransaction.show(outList);}}
        fragmentTransaction.commit();
    }
    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (outList!=null){
            fragmentTransaction.hide(outList);
        }

    }
}
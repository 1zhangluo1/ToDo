package com.example.needtodo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewPaper2BottomAdapter extends FragmentStateAdapter {
    boolean isSelect = false;
    public MyViewPaper2BottomAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ToDo();
            case 1:
                return new OutList();
            default:
                return new HadDoneList();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

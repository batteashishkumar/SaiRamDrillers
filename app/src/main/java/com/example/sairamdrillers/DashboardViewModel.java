package com.example.sairamdrillers;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    public void onClicktvcreate(View view)
    {
        toAvoidDoubleTapOnBtn(view);
        view.getContext().startActivity(new Intent(view.getContext(), CreateCustomer.class));
    }

    private void toAvoidDoubleTapOnBtn(final View view) {
        view.setEnabled(false);
        view.setClickable(false);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                view.setEnabled(true);
                view.setClickable(true);
            }
        },2000);
    }
}

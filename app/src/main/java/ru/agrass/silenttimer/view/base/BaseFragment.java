package ru.agrass.silenttimer.view.base;

import android.support.v4.app.Fragment;

import ru.agrass.silenttimer.view.activity.MainActivityView;


public abstract class BaseFragment extends Fragment {

    public abstract FragmentView getIView();

    public abstract void setActivityView(MainActivityView activityView);

}

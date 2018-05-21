package ru.agrass.silenttimer.view.timerlist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.agrass.silenttimer.R;
import ru.agrass.silenttimer.model.Timer;
import ru.agrass.silenttimer.model.parsers.TimeParser;
import ru.agrass.silenttimer.presenter.TimerListPresenter;
import ru.agrass.silenttimer.view.activity.MainActivityView;
import ru.agrass.silenttimer.view.adapters.TimerRecyclerViewAdapter;
import ru.agrass.silenttimer.view.base.BaseFragment;
import ru.agrass.silenttimer.view.base.FragmentView;
import ru.agrass.silenttimer.view.custom.view.dialog.TimePickerDialogC;


public class TimerListFragment extends BaseFragment implements TimerListView {
    private static final String TAG = TimerListFragment.class.getSimpleName();

    private MainActivityView activityView;
    private TimerRecyclerViewAdapter mAdapter;
    private TimerListPresenter presenter;
    private TimePickerDialogC timePicker;
    private Unbinder unbinder;

    @BindView(R.id.floatingActionButtonAdd)
    FloatingActionButton buttonAdd;
    @BindView(R.id.listViewTimer)
    RecyclerView mRecyclerView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public static TimerListFragment newInstance(MainActivityView mainActivityView) {
        Bundle args = new Bundle();
        TimerListFragment fragment = new TimerListFragment();
        fragment.setActivityView(mainActivityView);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        presenter = new TimerListPresenter(getContext(), this);
        mAdapter = new TimerRecyclerViewAdapter(new ArrayList<>());
        timePicker = new TimePickerDialogC();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        buttonAdd.setOnClickListener(this::addTimer);
        mAdapter.setDeleteTimerClickListener(this::deleteTimer);
        mAdapter.setChangeTimerListener(this::changeTimer);
        mAdapter.setEditTimeClickListener(this::editTime);

        if (savedInstanceState == null) {
            presenter.getTimers();
        }
        return view;
    }

    private void deleteTimer(Timer timer) {
        presenter.deleteTimer(timer);
    }

    private void changeTimer(Timer timer) {
        Log.e(TAG, "Change timer " + new Gson().toJson(timer));
        presenter.updateTimer(timer);
    }

    private void editTime(Timer timer, int type) {
        if (type == TimerRecyclerViewAdapter.TIME_FROM) {
            showDialog(
                    TimeParser.getIntHourFromString(timer.getTimeFrom()),
                    TimeParser.getIntMinuteFromString(timer.getTimeFrom()),
                    (hour, minute) -> {
                        timer.setTimeFrom(TimeParser.intToString(hour, minute));
                        mAdapter.notifyTimerChanged(timer);
                        mAdapter.notifyDataSetChanged();
                    }
            );
            return;
        }
        if (type == TimerRecyclerViewAdapter.TIME_TO) {
            showDialog(
                    TimeParser.getIntHourFromString(timer.getTimeTo()),
                    TimeParser.getIntMinuteFromString(timer.getTimeTo()),
                    (hour, minute) -> {
                        timer.setTimeTo(TimeParser.intToString(hour, minute));
                        mAdapter.notifyTimerChanged(timer);
                        mAdapter.notifyDataSetChanged();
                    }
            );
        }
    }

    private void showDialog(int hour, int minute, TimePickerDialogC.OnTimeSelectedListener listener) {
        timePicker.setOnTimeSelectedListener(listener);
        timePicker.setTime(hour, minute);
        assert getFragmentManager() != null;
        timePicker.show(getFragmentManager(), TimePickerDialogC.class.getSimpleName());
    }

    private void addTimer(View view) {
        Log.e(TAG, "OnClickListener");
        presenter.addTimer();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public FragmentView getIView() {
        return this;
    }

    @Override
    public void setActivityView(MainActivityView activityView) {
        this.activityView = activityView;
    }

    @Override
    public void showTimers(List<Timer> list) {
        Log.e(TAG, new Gson().toJson(list));
        mAdapter.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNewTimer(Timer timer) {

    }

    @Override
    public void onDestroy() {
        presenter.destroy();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}

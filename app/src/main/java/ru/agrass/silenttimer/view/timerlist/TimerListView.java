package ru.agrass.silenttimer.view.timerlist;

import java.util.List;

import ru.agrass.silenttimer.model.Timer;
import ru.agrass.silenttimer.view.base.FragmentView;


public interface TimerListView extends FragmentView {

    void showTimers(List<Timer> list);
    void showNewTimer(Timer timer);

}

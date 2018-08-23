package ru.agrass.silenttimer.view.timerlist;

import ru.agrass.silenttimer.model.entity.Timer;
import ru.agrass.silenttimer.view.base.IPresenter;

public interface ITimerListPresenter<V extends TimerListView> extends IPresenter<V> {

    void getTimers();
    void updateTimer(Timer timer);
    void addTimer();
    void deleteTimer(Timer timer);

}

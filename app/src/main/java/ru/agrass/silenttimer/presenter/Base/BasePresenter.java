package ru.agrass.silenttimer.presenter.Base;

import org.reactivestreams.Subscription;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.agrass.silenttimer.utils.rx.AppSchedulerProvider;
import ru.agrass.silenttimer.utils.rx.SchedulerProvider;

public abstract class BasePresenter implements Presenter {

    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected final SchedulerProvider mSchedulerProvider = new AppSchedulerProvider();

    protected BasePresenter() {}

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

}

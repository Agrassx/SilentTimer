package ru.agrass.silenttimer.view.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.agrass.silenttimer.utils.rx.AppSchedulerProvider;
import ru.agrass.silenttimer.utils.rx.SchedulerProvider;

public abstract class BasePresenter<V extends IView> implements IPresenter<V> {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final SchedulerProvider schedulerProvider = new AppSchedulerProvider();
    private V view;

    protected BasePresenter() {}

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public SchedulerProvider getSchedulerProvider() {
        return schedulerProvider;
    }

    public V getView() {
        return view;
    }

    @Override
    public void onAttach(V view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        this.view = null;
        this.compositeDisposable.dispose();
    }
}

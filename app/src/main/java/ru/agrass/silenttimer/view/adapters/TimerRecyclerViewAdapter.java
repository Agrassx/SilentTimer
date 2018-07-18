package ru.agrass.silenttimer.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.agrass.silenttimer.R;
import ru.agrass.silenttimer.model.Timer;
import ru.agrass.silenttimer.view.custom.view.CheckBoxDay;

public class TimerRecyclerViewAdapter extends RecyclerView.Adapter<TimerRecyclerViewAdapter.ViewHolder> {
    private static final String LOG = TimerRecyclerViewAdapter.class.getSimpleName();

    public static final int TIME_TO = 0;
    public static final int TIME_FROM = 1;

    private OnDeleteTimerClickListener deleteTimerClickListener;
    private OnChangeSwitchListener onChangeSwitchListener;
    private OnEditTimeClickListener editTimeClickListener;
    private OnChangeTimerListener changeTimerListener;
    private List<Timer> list;

    public TimerRecyclerViewAdapter(List<Timer> timerList) {
        this.list = timerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_timer_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(
                list.get(position),
                changeTimerListener,
                editTimeClickListener,
                onChangeSwitchListener,
                deleteTimerClickListener
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(@NonNull List<Timer> list) {
        if (this.list.isEmpty()) {
            this.list.addAll(list);
            return;
        }
        this.list.clear();
        this.list.addAll(list);
    }

    public void add(@NonNull Timer timer) {
        this.list.add(timer);
    }

    public Timer getTimer(int index) {
        return list.get(index);
    }

    public void notifyTimerChanged(Timer timer) {
        if (changeTimerListener == null) return;
        changeTimerListener.onChange(timer);
    }

    public void setChangeTimerListener(OnChangeTimerListener changeTimerListener) {
        this.changeTimerListener = changeTimerListener;
    }

    public void setDeleteTimerClickListener(OnDeleteTimerClickListener deleteTimerClickListener) {
        this.deleteTimerClickListener = deleteTimerClickListener;
    }

    public void setOnChangeSwitchListener(OnChangeSwitchListener onChangeSwitchListener) {
        this.onChangeSwitchListener = onChangeSwitchListener;
    }

    public void setEditTimeClickListener(OnEditTimeClickListener editTimeClickListener) {
        this.editTimeClickListener = editTimeClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewTimeFrom) TextView textViewTimeFrom;
        @BindView(R.id.textViewTimeTo) TextView textViewTimeTo;
        @BindView(R.id.imageDeleteTimer) ImageView imageDeleteTimer;
        @BindView(R.id.switchItemTimer) Switch switchItemTimer;
        @BindView(R.id.linearLayoutDaysOfWeek) LinearLayout daysOfWeek;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final Timer timer, final OnChangeTimerListener changeTimerListener,
                  final OnEditTimeClickListener editTimeClickListener,
                  final OnChangeSwitchListener onSwitchListener,
                  final OnDeleteTimerClickListener deleteTimerClickListener) {

            CheckBoxDay[] days = new CheckBoxDay[7];

            bindSwitchTimer(switchItemTimer, timer, changeTimerListener, onSwitchListener);

            bindCheckBoxDays(timer, days, changeTimerListener);

            bindTextViewTimeFrom(
                    timer,
                    textViewTimeFrom,
                    timer.getTimeFrom(),
                    editTimeClickListener
            );

            bindTextViewTimeTo(
                    timer,
                    textViewTimeTo,
                    timer.getTimeTo(),
                    editTimeClickListener
            );

            bindDeleteButton(imageDeleteTimer, timer, deleteTimerClickListener);

        }

        private void bindTextViewTimeFrom(Timer timer, TextView textView, String stringTime,
                                          OnEditTimeClickListener editTimeClickListener) {
            textView.setText(stringTime);
            if (editTimeClickListener != null) {
                textView.setOnClickListener(
                        view -> editTimeClickListener.onEditTimeClickListener(timer, TIME_FROM)
                );
            }
        }

        private void bindTextViewTimeTo(Timer timer, TextView textView, String stringTime,
                                        OnEditTimeClickListener editTimeToClickListener) {

            textView.setText(stringTime);
            if (editTimeToClickListener != null) {
                textView.setOnClickListener(
                        view -> editTimeToClickListener.onEditTimeClickListener(timer, TIME_TO)
                );
            }

        }

        private void bindCheckBoxDays(Timer timer, CheckBoxDay[] days,
                                      OnChangeTimerListener changeTimerListener) {
            for (int i = 0; i < daysOfWeek.getChildCount(); i++) {
                days[i] = (CheckBoxDay) daysOfWeek.getChildAt(i);
                days[i].setCheck(timer.getDaysMap().get(days[i].getDayName()));
                days[i].setOnClickListener(
                        view -> {
                            Map<String, Boolean> map = timer.getDaysMap();
                            map.remove(((CheckBoxDay) view).getDayName());
                            map.put(
                                    ((CheckBoxDay) view).getDayName(),
                                    ((CheckBoxDay) view).isCheck()
                            );
                            timer.setDaysMap(map);
                            if (changeTimerListener != null) {
                                changeTimerListener.onChange(timer);
                            }
                        }
                );
            }
        }

        private void bindSwitchTimer(Switch switchItemTimer, Timer timer,
                                     OnChangeTimerListener changeTimerListener,
                                     OnChangeSwitchListener onSwitchListener) {
            switchItemTimer.setChecked(timer.isEnable());
            switchItemTimer.setOnClickListener(
                    view -> {
                        timer.setEnable(((Switch) view).isChecked());
                        if (changeTimerListener != null) {
                            changeTimerListener.onChange(timer);
                        }
                        if (onSwitchListener != null) {
                            onSwitchListener.onSwitch(timer);
                        }
                    }
            );

        }

        private void bindDeleteButton(ImageView deleteTimer, Timer timer,
                                      OnDeleteTimerClickListener deleteTimerClickListener) {
            if (deleteTimerClickListener != null) {
                deleteTimer.setOnClickListener(
                        v -> deleteTimerClickListener.onDelete(timer)
                );
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Timer item, View sharedView);
    }

    public interface OnChangeSwitchListener {
        void onSwitch(Timer timer);
    }

    public interface OnDeleteTimerClickListener {
        void onDelete(Timer timer);
    }

    public interface OnChangeTimerListener {
        void onChange(Timer timer);
    }

    public interface OnEditTimeClickListener {
        void onEditTimeClickListener(Timer timer, int typeTime);
    }


}

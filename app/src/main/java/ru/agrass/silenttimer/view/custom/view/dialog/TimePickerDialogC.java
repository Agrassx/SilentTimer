package ru.agrass.silenttimer.view.custom.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.TimePicker;


public class TimePickerDialogC implements android.app.TimePickerDialog.OnTimeSetListener,
        DialogInterface.OnCancelListener {

    private OnTimeSelectedListener onTimeSelected;
    private android.app.TimePickerDialog instance;

    public TimePickerDialogC(Context context) {
        createNewTimePickerInstance(context);
    }

    private void createNewTimePickerInstance(Context context) {
        instance = new android.app.TimePickerDialog(
                context,
                this,
                0,
                0,
                true
        );
        instance.setOnCancelListener(this);
    }

    public void show() {
        instance.show();
    }

    public void setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelected) {
        this.onTimeSelected = onTimeSelected;
    }

    public void setTime(int hour, int minute) {
        instance.updateTime(hour, minute);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        if (onTimeSelected == null) {
            dismiss();
            return;
        }
        onTimeSelected.onTimeSelected(hour, minute);
        dismiss();
    }

    public void dismiss() {
        onTimeSelected = null;
        instance.dismiss();
        createNewTimePickerInstance(instance.getContext());
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        dismiss();
    }

    public interface OnTimeSelectedListener {
        void onTimeSelected(int hour, int minute);
    }
}

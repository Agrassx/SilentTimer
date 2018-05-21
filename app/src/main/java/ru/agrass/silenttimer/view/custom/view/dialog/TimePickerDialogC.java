package ru.agrass.silenttimer.view.custom.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.app.TimePickerDialog;
import android.widget.TimePicker;

public class TimePickerDialogC extends AppCompatDialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {

    private OnTimeSelectedListener onTimeSelected;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new android.app.TimePickerDialog(getContext(), this, 0, 0, true);
    }

    @Override
    public void show(@NonNull FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    public void setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelected) {
        this.onTimeSelected = onTimeSelected;
    }

    public void setTime(int hour, int minute) {
        ((android.app.TimePickerDialog) getDialog()).updateTime(hour, minute);
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        onTimeSelected = null;
    }

    public interface OnTimeSelectedListener {
        void onTimeSelected(int hour, int minute);
    }
}

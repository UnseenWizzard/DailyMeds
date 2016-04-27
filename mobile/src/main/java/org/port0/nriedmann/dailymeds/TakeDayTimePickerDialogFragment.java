package org.port0.nriedmann.dailymeds;

import android.app.AlertDialog;
import android.app.Dialog;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

/**
 * Created by captain on 30.08.2015.
 */
public class TakeDayTimePickerDialogFragment extends DialogFragment {

    private boolean[] daysChecked = {false,false,false,false,false,false,false};
    private Med medToEdit;

    public void setMed(Med med){
        this.medToEdit=med;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        daysChecked = medToEdit.getTakeDayArray();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditActivity parent = (EditActivity)getActivity();
        builder.setTitle(R.string.title_dialog_daytime);
        builder.setMultiChoiceItems(getResources().getStringArray(R.array.days_array), daysChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // do something when choice clicked?
                daysChecked[which] = isChecked;
            }
        });
        builder.setPositiveButton(R.string.action_next, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // store days and time in med object

                // show time choice
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //save time
                        medToEdit.setTakeTimeH(hourOfDay);
                        medToEdit.setTakeTimeM(minute);
                        //save days
                        medToEdit.setTakeDays(daysChecked);
                        //set boolean true
                        medToEdit.setTakeTimeSet(true);
                        //time changed, needs to reset alarms
                        medToEdit.setAlarmsSet(false);
                        //update display
                        parent.updateContent(true);
                    }
                },medToEdit.getTakeHour(),medToEdit.getTakeMinute(),false);
                timePickerDialog.show();
            }
        });
        builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //go back
            }
        });
        return builder.create();
    }


}

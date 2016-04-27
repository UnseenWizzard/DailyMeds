package org.port0.nriedmann.dailymeds;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by captain on 30.08.2015.
 */
public class ImageColorPickerDialogFragment  extends DialogFragment{

    private Med medToEdit;

    public void setMed(Med med){
        this.medToEdit=med;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_dialog_icon);
        //builder.setView(R.layout.med_icon_chooser);
        builder.setPositiveButton(R.string.action_next, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //store chosen icon and color
            }
        });
        builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // go back
            }
        });


        return builder.create();
    }

}

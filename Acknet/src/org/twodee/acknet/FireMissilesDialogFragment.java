package org.twodee.acknet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

@SuppressLint("NewApi")
public class FireMissilesDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Alert Dialog Generic")
               .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!
                   }
               });
               //.setNegativeButton("CACA", new DialogInterface.OnClickListener() {
               //    public void onClick(DialogInterface dialog, int id) {
               //        // User cancelled the dialog
               //    }
               //});
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
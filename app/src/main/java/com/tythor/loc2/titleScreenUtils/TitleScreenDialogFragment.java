package com.tythor.loc2.titleScreenUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.tythor.loc2.GameActivity;
import com.tythor.loc2.GameView;
import com.tythor.loc2.TitleView;
import com.tythor.loc2.ViewController;

import static com.tythor.loc2.GameActivity.screenHeight;
import static com.tythor.loc2.GameActivity.screenWidth;
import static com.tythor.loc2.GameActivity.context;

/**
 * Created by Tyler on 2/25/2017.
 */

public class TitleScreenDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Secret found!")
                .setMessage("Play the title screen level?")
                .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Set game to TitleLevel
                        ViewController.changeToGameView("TitleLevel");

                        // Hide creditsView
                        GameActivity.parentLayout.removeView(TitleView.creditsView);
                        TitleView.overlayOn = false;
                    }
                })
                .setNegativeButton("It's too hard!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing
                    }
                });


        return buildImmersive(builder);
    }


    @Override
    public void show(FragmentManager fragmentManager, String tag) {
        super.show(fragmentManager, tag);
        getFragmentManager().executePendingTransactions();
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    private AlertDialog buildImmersive(AlertDialog.Builder builder) {
        final AlertDialog alertDialog = builder.create();

        alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                         WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        alertDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    setSystemUiVisibility(alertDialog);
                }

            }
        });

        return alertDialog;
    }

    private static void setSystemUiVisibility(AlertDialog alertDialog) {
        alertDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                                                             | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                                             | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                                             | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                                                             | View.SYSTEM_UI_FLAG_FULLSCREEN
                                                                             | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}

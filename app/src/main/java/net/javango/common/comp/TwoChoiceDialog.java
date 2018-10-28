package net.javango.common.comp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import net.javango.carcare.R;

import java.io.Serializable;

/**
 * A generic two-choice dialog (OK/Cancel)
 * <p>Usage example:
 * <pre>
 *  TwoChoiceDialog dialog = TwoChoiceDialog.create(null, message, new TwoChoiceDialog.ChoiceListener() {
 *      @Override
 *      public void onPositiveChoice() {
 *          // some action
 *      }
 *  });
 *  dialog.show(getFragmentManager(), "tag");
 * </pre>
 */
public class TwoChoiceDialog extends DialogFragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_MESSAGE = "message";
    private static final String ARG_LISTENER = "listener";

    private ChoiceListener listener;

    public static abstract class ChoiceListener implements Serializable {
        public abstract void onPositiveChoice();

        public void onNegativeChoice() {
            // by default, do nothing
        }
    }

    public static TwoChoiceDialog create(Fragment parent, int titleId, int messageId, ChoiceListener choiceListener) {
        return create(parent.getString(titleId), parent.getString(messageId), choiceListener);
    }

    public static TwoChoiceDialog create(String title, String message, ChoiceListener choiceListener) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putSerializable(ARG_LISTENER, choiceListener);
        TwoChoiceDialog fragment = new TwoChoiceDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(ARG_TITLE);
        String message = getArguments().getString(ARG_MESSAGE);
        ChoiceListener listener = (ChoiceListener) getArguments().getSerializable(ARG_LISTENER);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (d, which) -> listener.onPositiveChoice())
                .setNegativeButton(android.R.string.cancel, (d, which) -> listener.onNegativeChoice())
                .create();
        return dialog;
    }
}

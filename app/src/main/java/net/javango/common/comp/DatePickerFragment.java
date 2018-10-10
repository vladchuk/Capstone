package net.javango.common.comp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import net.javango.carcare.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A generic date picker component. It requires two parameters, dialog title and a date.
 * <p>Usage example:
 * <pre>
 *      private static final String DIALOG_DATE = "DialogDate";
 *      private static final int REQUEST_DATE = 0;
 *      ...
 *      FragmentManager manager = getFragmentManager();
 *      DatePickerFragment dialog = DatePickerFragment.newInstance("Dialog Title", new Date());
 *      dialog.setTargetFragment(ServiceDetailFragment.this, REQUEST_DATE);
 *      dialog.show(manager, DIALOG_DATE);
 * </pre>
 * <p>
 * The selected date is retrieved like this:
 * <pre>
 *     public void onActivityResult(int requestCode, int resultCode, Intent data) {
 *         if (resultCode != Activity.RESULT_OK) {
 *             return;
 *         }
 *         if (requestCode == REQUEST_DATE) {
 *             Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
 *             binding.serviceDetailDateValue.setText(Formatter.formatFull(date));
 *         }
 *     }
 * </pre>
 */
public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "net.javango.common_date";

    private static final String ARG_TITLE = "title";
    private static final String ARG_DATE = "date";

    public static DatePickerFragment newInstance(String title, Date date) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putSerializable(ARG_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        DatePicker datePicker = view.findViewById(R.id.dialog_date_picker);
        datePicker.init(year, month, day, null);
        String title = getArguments().getString(ARG_TITLE);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year, month, day).getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}

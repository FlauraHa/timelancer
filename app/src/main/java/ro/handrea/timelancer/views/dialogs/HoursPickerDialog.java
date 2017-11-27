package ro.handrea.timelancer.views.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import ro.handrea.timelancer.R;
import ro.handrea.timelancer.views.listeners.TimeSetListener;

/**
 * Created on 11/27/17.
 */

public class HoursPickerDialog extends AlertDialog implements DialogInterface.OnClickListener {
    private static final int HOUR_PICKER_MIN_VALUE = 0;
    private static final int HOUR_PICKER_MAX_VALUE = 24;
    private static final int MINUTE_PICKER_MIN_VALUE = 0;
    private static final int MINUTE_PICKER_MAX_VALUE = 3;

    private Context mContext;
    private View mTimeDialogView;
    private NumberPicker mHourPicker;
    private NumberPicker mMinutePicker;
    private TimeSetListener mTimeSetListener;
    private String[] mMinutesArray;

    public HoursPickerDialog(Context context, TimeSetListener listener) {
        super(context);
        this.mContext = context;
        this.mTimeSetListener = listener;
        this.mMinutesArray = mContext.getResources()
                .getStringArray(R.array.dialog_number_picker_minutes);

        buildTimePickerDialog();
    }

    private void buildTimePickerDialog() {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTimeDialogView = layoutInflater.inflate(R.layout.dialog_hours_picker, null);
        setView(mTimeDialogView);
        initializeNumberPickers();
        setTitle(mContext.getString(R.string.dialog_title_hours));
        setButton(BUTTON_POSITIVE, mContext.getString(R.string.button_label_ok), this);
        setButton(BUTTON_NEGATIVE, mContext.getString(R.string.button_label_cancel), (OnClickListener) null);
        create();
    }

    private void initializeNumberPickers() {
        mHourPicker = mTimeDialogView.findViewById(R.id.hourPicker);
        mHourPicker.setMinValue(HOUR_PICKER_MIN_VALUE);
        mHourPicker.setMaxValue(HOUR_PICKER_MAX_VALUE);
        mHourPicker.setFormatter(i -> String.format("%02d", i));

        mMinutePicker = mTimeDialogView.findViewById(R.id.minutePicker);
        mMinutePicker.setMinValue(MINUTE_PICKER_MIN_VALUE);
        mMinutePicker.setMaxValue(MINUTE_PICKER_MAX_VALUE);
        mMinutePicker.setDisplayedValues(mMinutesArray);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mTimeSetListener != null) {
                    mTimeSetListener.onTimeSet(mHourPicker.getValue(),
                            Integer.parseInt(mMinutesArray[mMinutePicker.getValue()]));
                }
                break;
            default:
                break;
        }
        dismiss();
    }
}
package ro.handrea.timelancer.views.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ro.handrea.timelancer.R;
import ro.handrea.timelancer.models.Project;
import ro.handrea.timelancer.views.listeners.ProjectCreatedListener;

/**
 * Created on 11/25/17.
 */

public class NewProjectDialog extends AlertDialog implements View.OnClickListener {
    private static final String TAG = NewProjectDialog.class.getSimpleName();

    private ProjectCreatedListener mListener;
    private Context mContext;
    private EditText mNameEditText;

    public NewProjectDialog(Context ctx, ProjectCreatedListener listener) {
        super(ctx);
        mContext = ctx;
        mListener = listener;
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = layoutInflater.inflate(R.layout.dialog_new_project, null);

        setView(dialogView);
        setTitle(ctx.getString(R.string.dialog_title_new_project));
        setButton(BUTTON_POSITIVE, ctx.getString(R.string.button_label_ok), (OnClickListener) null);
        setButton(BUTTON_NEGATIVE, ctx.getString(R.string.button_label_cancel), (OnClickListener) null);
        mNameEditText = dialogView.findViewById(R.id.edit_text_project_name);
        create();
    }

    @Override
    public void show() {
        super.show();
        // We can set View.OnClickListener on buttons only after showing the dialog
        getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(this);
    }

    // onPositiveButtonClick
    // We use View.OnClickListener instead of DialogInterface.OnClickListener because we don't want
    // the dialog to be dismissed implicitly after onClick was called, if the inserted field is not valid
    @Override
    public void onClick(View v) {
        String projectName = mNameEditText.getText().toString().trim();

        if (isValid(projectName)) {
            mListener.onProjectCreated(new Project(projectName));
            mNameEditText.setText("");
            String message = mContext.getString(R.string.toast_message_project_created);
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    private boolean isValid(String name) {
        boolean isDataOk = true;
        if (name == null || name.equals("")) {
            mNameEditText.setError(mContext.getString(R.string.dialog_error_message_invalid));
            isDataOk = false;
        }

        return isDataOk;
    }
}

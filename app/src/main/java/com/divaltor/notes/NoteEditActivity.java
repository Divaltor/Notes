package com.divaltor.notes;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.divaltor.notes.db.DbUtil;
import com.divaltor.notes.db.Note;
import com.divaltor.notes.utils.ViewUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class NoteEditActivity extends Activity {
    public final static String EXTRA_NOTE = "note";

    /**
     * Note that is being edited
     */
    private Note note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ViewUtil.applyLightActivity(this);

        note = getIntent().getParcelableExtra(EXTRA_NOTE);

        EditText editText = findViewById(R.id.et_edit);
        editText.setText(note.text);

        TextView textView = findViewById(R.id.tv_title);
        textView.setText(note.title);
        textView.setOnClickListener(view -> {
            EditText editText1 = new EditText(this);
            editText1.setText(note.title);
            int dimension = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = params.rightMargin = params.topMargin = params.bottomMargin = dimension;
            editText1.setLayoutParams(params);
            FrameLayout frameLayout = new FrameLayout(this);
            frameLayout.addView(editText1);

            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.edit_title)
                    .setView(frameLayout)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        note.title = editText1.getText().toString();
                        textView.setText(note.title);
                    })
                    .show();
        });

        findViewById(R.id.iv_back).setOnClickListener(view -> onBackPressed());
        findViewById(R.id.fab).setOnClickListener(view -> {
            note.text = editText.getText().toString();
            DbUtil.insertOrUpdate(note);
            setResult(RESULT_OK);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.leave)
                .setMessage(R.string.leave_without_saving)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    setResult(RESULT_CANCELED);
                    finish();
                }).show();
    }
}

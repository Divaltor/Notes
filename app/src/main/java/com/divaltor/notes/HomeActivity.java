package com.divaltor.notes;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divaltor.notes.db.DbUtil;
import com.divaltor.notes.db.Note;
import com.divaltor.notes.utils.TextUtil;
import com.divaltor.notes.utils.ViewUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private final static int REQUEST_NOTE = 100;

    private List<Note> notes = Collections.emptyList();
    private LinearLayout noNotes;
    private RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtil.applyLightActivity(this);

        notes = DbUtil.getNotes();

        noNotes = findViewById(R.id.ll_no_notes);
        setupNotesRecycler(findViewById(R.id.rv_recycler));

        findViewById(R.id.fab).setOnClickListener(v-> {
            Note note = new Note();
            note.title = TextUtil.getCurrentDate();
            startEditor(note);
        });

        updateNotes();
    }

    /**
     * Starts editor activity
     * @param note Note to start with
     */
    private void startEditor(Note note) {
        Intent intent = new Intent(this, NoteEditActivity.class);
        intent.putExtra(NoteEditActivity.EXTRA_NOTE, note);
        startActivityForResult(intent, REQUEST_NOTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NOTE && resultCode == RESULT_OK) {
            notes = DbUtil.getNotes();
            updateNotes();
        }
    }

    /**
     * Updates notes view
     */
    private void updateNotes() {
        mRecycler.getAdapter().notifyDataSetChanged();
        mRecycler.setVisibility(notes.isEmpty() ? View.GONE : View.VISIBLE);
        noNotes.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
    }

    /**
     * Setups recycler view for notes
     * @param recyclerView Recycler to setup
     */
    private void setupNotesRecycler(RecyclerView recyclerView) {
        mRecycler = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new RecyclerView.Adapter<NoteHolder>() {
            {
                setHasStableIds(true);
            }

            @NonNull
            @Override
            public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new NoteHolder(parent);
            }

            @Override
            public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
                Note note = notes.get(position);
                holder.title.setText(note.title);
                holder.subtitle.setText(TextUtil.truncate(note.text));

                int clr;
                switch (note.index % 4) {
                    default:
                    case 0:
                        clr = ContextCompat.getColor(HomeActivity.this, R.color.note_color_1);
                        break;
                    case 1:
                        clr = ContextCompat.getColor(HomeActivity.this, R.color.note_color_2);
                        break;
                    case 2:
                        clr = ContextCompat.getColor(HomeActivity.this, R.color.note_color_3);
                        break;
                    case 3:
                        clr = ContextCompat.getColor(HomeActivity.this, R.color.note_color_4);
                        break;
                }
                holder.itemView.setOnClickListener(v -> startEditor(note));
                holder.itemView.setOnLongClickListener(v -> {
                    new MaterialAlertDialogBuilder(HomeActivity.this)
                            .setTitle(R.string.delete)
                            .setMessage(R.string.delete_description)
                            .setNegativeButton(android.R.string.cancel, null)
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                DbUtil.remove(note);
                                notes.remove(note);
                                updateNotes();
                            }).show();
                    return true;
                });
                holder.card.setCardBackgroundColor(clr);
            }

            @Override
            public int getItemCount() {
                return notes.size();
            }

            @Override
            public long getItemId(int position) {
                return notes.get(position).id;
            }
        });

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int position = parent.getChildViewHolder(view).getAdapterPosition();
                if (position == 0)
                    outRect.top = getResources().getDimensionPixelSize(R.dimen.item_top_bottom_padding);
                else
                    outRect.top = getResources().getDimensionPixelSize(R.dimen.item_sibling_padding);

                if (position == parent.getAdapter().getItemCount() - 1)
                    outRect.bottom = getResources().getDimensionPixelSize(R.dimen.item_top_bottom_padding);
            }
        });
    }

    private final static class NoteHolder extends RecyclerView.ViewHolder {
        TextView title = itemView.findViewById(R.id.tv_title);
        TextView subtitle = itemView.findViewById(R.id.tv_subtitle);
        CardView card = itemView.findViewById(R.id.cv_card);

        NoteHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false));
        }
    }
}
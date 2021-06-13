package com.example.mynotes.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mynotes.R;

public class EditNoteFragment extends Fragment {
    private static final String NOTE_EXTRA_KEY = "NOTE_EXTRA_KEY";

    private Button saveButton;
    private EditText subjectEditText;
    private EditText textEditText;
    private EditText phoneEditText;

    @Nullable
    private NoteEntity note = null;

    public static EditNoteFragment newInstance(@Nullable NoteEntity note) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(NOTE_EXTRA_KEY, note);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);
        saveButton = view.findViewById(R.id.save_button);
        subjectEditText = view.findViewById(R.id.subject_edit_text);
        textEditText = view.findViewById(R.id.text_edit_text);
        phoneEditText = view.findViewById(R.id.phone_edit_text);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        note = (NoteEntity) getArguments().getSerializable(NOTE_EXTRA_KEY);
        fillNote(note);

        saveButton.setOnClickListener(v -> {
            getContract().saveNote(gatherNote());
        });
    }

    private void fillNote(NoteEntity note) {
        if (note == null) return;
        subjectEditText.setText(note.subject);
        textEditText.setText(note.text);
        phoneEditText.setText(note.phone);
    }

    private NoteEntity gatherNote() {
        return new NoteEntity(
                note == null ? NoteEntity.generateNewId() : note.id,
                subjectEditText.getText().toString(),
                note == null ? NoteEntity.getCurrentDate() : note.creationDate,
                textEditText.getText().toString(),
                phoneEditText.getText().toString()
        );
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof Contract)) {
            throw new IllegalStateException("Activity must implement Contract");
        }
    }

    private Contract getContract() {
        return (Contract) getActivity();
    }

    public interface Contract {
        void saveNote(NoteEntity note);
    }
}
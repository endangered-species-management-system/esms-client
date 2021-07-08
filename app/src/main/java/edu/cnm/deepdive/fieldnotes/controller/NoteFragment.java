package edu.cnm.deepdive.fieldnotes.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import edu.cnm.deepdive.fieldnotes.NavigationDirections.OpenNote;
import edu.cnm.deepdive.fieldnotes.R;
import edu.cnm.deepdive.fieldnotes.databinding.FragmentNoteBinding;
import edu.cnm.deepdive.fieldnotes.model.entity.Note;
import edu.cnm.deepdive.fieldnotes.model.entity.Note.Category;
import edu.cnm.deepdive.fieldnotes.viewmodel.MainViewModel;

public class NoteFragment extends DialogFragment implements TextWatcher {

  private MainViewModel viewModel;
  private FragmentNoteBinding binding;
  private AlertDialog alertDialog;
  private Long noteId;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = FragmentNoteBinding.inflate(LayoutInflater.from(getContext()));
  }



  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    //noinspection ConstantConditions
    alertDialog = new AlertDialog.Builder(getContext())
        .setTitle(R.string.new_note)
        .setView(binding.getRoot())
        .setNeutralButton(android.R.string.cancel, (dlg, which) -> {})
        .setPositiveButton(android.R.string.ok, (dlg, which) -> saveNote())
        .create();
    alertDialog.setOnShowListener((dlg) -> {
      binding.note.addTextChangedListener(this);
      checkSubmitConditions();
    });
    return alertDialog;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //noinspection ConstantConditions
    viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
    if (getArguments() != null) {
      NoteFragmentArgs args = NoteFragmentArgs.fromBundle(getArguments());
      noteId = Long.getLong(String.valueOf(args.getNoteId()));
    }
  }

  private void checkSubmitConditions() {
    Button positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
    positive.setEnabled(!binding.note.getText().toString().trim().isEmpty());
  }

  private void saveNote() {
    Note note = new Note();
    String newNote = binding.note.getText().toString().trim();
    note.setNote(newNote);
    note.setId(noteId);
    note.setCategory(Category.SPECIMEN);
    viewModel.saveNote(note);
  }


  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void afterTextChanged(Editable editable) {
    checkSubmitConditions();
  }
}
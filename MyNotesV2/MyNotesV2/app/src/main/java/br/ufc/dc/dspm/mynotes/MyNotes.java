package br.ufc.dc.dspm.mynotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class MyNotes extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public int dia, mes, ano;
    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);
    Calendar dateCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);
    }

    public void addNote(View view) {
        EditText titleText = (EditText) findViewById(R.id.editTextTitle);
        EditText contentText = (EditText) findViewById(R.id.editTextContent);
        ContentValues values = new ContentValues();
        values.put(NotesProvider.TITLE, titleText.getText().toString());
        values.put(NotesProvider.CONTENT, contentText.getText().toString());
        if (dateCalendar != null){
            values.put(NotesProvider.DATE, formatter.format(dateCalendar.getTime()));
        }
        Uri uri = getContentResolver().insert(NotesProvider.CONTENT_URI, values);
    }

    public void listNotes(View view) throws ParseException {
        StringBuffer buffer = new StringBuffer();
        String URL = NotesProvider.URL;
        Uri notesURI = Uri.parse(URL);
        Cursor cursor = getContentResolver().query(notesURI, null, null, null, NotesProvider.ID);
        Vector<String> values = new Vector<>();
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(NotesProvider.ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(NotesProvider.TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndex(NotesProvider.CONTENT)));
                note.setDate(cursor.getString(cursor.getColumnIndex(NotesProvider.DATE)));
                values.add(note.toString());
            } while (cursor.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }
    //Clicar e Editar Notas
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        final String itemF = item.substring(1,2);
        Intent intent = new Intent();
        position = Math.abs(position);
        final int positionF = position+1;
        String testString = Integer.toString(position);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View viewD = inflater.inflate(R.layout.dialog, null);
        //--retornando cursor
        String URL = NotesProvider.URL;
        Uri notesURI = Uri.parse(URL);
        Cursor cursor = getContentResolver().query(notesURI, null, null, null, NotesProvider.ID);
        final Note noteEscolhido = new Note();
        if (cursor.moveToFirst()) {
            do {
            if(cursor.getInt(cursor.getColumnIndex(NotesProvider.ID))==Integer.parseInt(itemF)){
                noteEscolhido.setId(cursor.getInt(cursor.getColumnIndex(NotesProvider.ID)));
                noteEscolhido.setTitle(cursor.getString(cursor.getColumnIndex(NotesProvider.TITLE)));
                noteEscolhido.setContent(cursor.getString(cursor.getColumnIndex(NotesProvider.CONTENT)));
                noteEscolhido.setDate(cursor.getString(cursor.getColumnIndex(NotesProvider.DATE)));
            }
        } while (cursor.moveToNext());
        }
        //--retornando cursor
        final EditText titulo = (EditText) viewD.findViewById(R.id.editTextTitle);
        titulo.setText(noteEscolhido.getTitle()+"");
        final EditText conteúdo = (EditText) viewD.findViewById(R.id.editTextContent);
        conteúdo.setText(noteEscolhido.getContent()+"");
        final TextView data = (TextView) viewD.findViewById(R.id.data);
        data.setText(noteEscolhido.getDate());
        final ContentValues valuesF = new ContentValues();
        builder.setView(viewD)
                // Add action buttons
                .setPositiveButton("Editar Nota", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String position = itemF;
                        valuesF.put(NotesProvider.TITLE, titulo.getText().toString());
                        valuesF.put(NotesProvider.CONTENT, conteúdo.getText().toString());
                        if (dateCalendar != null)
                            valuesF.put(NotesProvider.DATE, formatter.format(dateCalendar.getTime()));
                        String[] fieldValues = new String[1];
                        fieldValues[0] = position;
                        int i = getContentResolver().update(NotesProvider.CONTENT_URI, valuesF,null,fieldValues);
//                        Toast.makeText(getBaseContext(), i, Toast.LENGTH_LONG).show();

//                        noteDAO.update(noteEscolhido);
//                        atualizar();

                    }
                });
        builder.setView(viewD)
                .setNegativeButton("Deletar Nota", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String position = itemF;
                        String[] fieldValues = new String[1];
                        fieldValues[0] = position;
                        int i = getContentResolver().delete(NotesProvider.CONTENT_URI,null,fieldValues);
//                        noteDAO.delete(noteEscolhido.getId());
//                        atualizar();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    //Data
    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    //Data
    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        Calendar newCalendar = Calendar.getInstance();
        String oi;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            startDateDefault();

            return new DatePickerDialog(getActivity(), this, ano, mes, dia);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            dateCalendar = Calendar.getInstance();
            dateCalendar.set(year, month, day);
            TextView data = (TextView) findViewById(R.id.data);
            data.setText(day+"/"+month+"/"+year);
            // Do something with the time chosen by the user
        }
    }
    public void startDateDefault() {
        if (ano == 0) {
            Calendar calendar = Calendar.getInstance();
            ano = calendar.get(Calendar.YEAR);
            mes = calendar.get(Calendar.MONTH);
            dia = calendar.get(Calendar.DAY_OF_MONTH);
        }
    }
}

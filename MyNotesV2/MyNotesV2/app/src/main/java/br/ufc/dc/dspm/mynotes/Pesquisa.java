package br.ufc.dc.dspm.mynotes;

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
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class Pesquisa extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
        Calendar dateCalendarInicial,dateCalendarFinal;
        Calendar dateCalendar;
public int diai=0, mesi=0, anoi=0,diaf=0, mesf=0, anof=0;
private static final SimpleDateFormat formatter = new SimpleDateFormat(
        "yyyy-MM-dd", Locale.ENGLISH);

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);
        }
public int pesquise(View view){
        if(dateCalendarInicial==null||dateCalendarFinal==null) {
        Toast.makeText(this,"Preencha os campos de inicio e fim de data.",Toast.LENGTH_SHORT).show();
        return 1;
        }
        atualizarPesquisa(dateCalendarInicial.getTime(), dateCalendarFinal.getTime());
        return 0;
        }
public void atualizarData(){
        TextView dataInicial = (TextView) findViewById(R.id.dataInicial);
        dataInicial.setText(diai+"/"+mesi+"/"+anoi);
        TextView dataFinal = (TextView) findViewById(R.id.dataFinal);
        dataFinal.setText(diaf+"/"+mesf+"/"+anof);
        }
    //Listar Notas Pesquisa
    public int atualizarPesquisa(Date Inicial, Date Final){
        String URL = NotesProvider.URL;
        Uri notesURI = Uri.parse(URL);
        Note note = new Note();
        Cursor cursor = getContentResolver().query(notesURI, null, null, null, NotesProvider.ID);
        List<Note> notes = listByIterval(Inicial,Final);
            if(notes==null){
                Toast.makeText(this,"Não há nenhuma nota nesse intervalo.",Toast.LENGTH_SHORT).show();
                return 1;
            }
        Iterator<Note> it = notes.iterator();
        Vector<String> values = new Vector<>();
        while (it.hasNext()) {
        note = it.next();
        values.add(note.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
        return 0;
    }
        //Data inicial e final
        public void showDatePickerDialogInicial(View view) {
            DialogFragment newFragment = new DatePickerFragmentInicial();
            newFragment.show(getFragmentManager(), "datePicker");
        }
        public void showDatePickerDialogFinal(View view) {
            DialogFragment newFragment = new DatePickerFragmentFinal();
            newFragment.show(getFragmentManager(), "datePicker");
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

    public class DatePickerFragmentInicial extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        Calendar newCalendar = Calendar.getInstance();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            startDateDefault();

            return new DatePickerDialog(getActivity(), this, anoi, mesi, diai);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            dateCalendarInicial = Calendar.getInstance();
            dateCalendarInicial.set(year, month, day);
            diai = day; mesi = month; anoi = year;
            TextView dataInicial = (TextView) findViewById(R.id.dataInicial);
            dataInicial.setText(diai+"/"+mesi+"/"+anoi);
            // Do something with the time chosen by the user
        }
    }


public class DatePickerFragmentFinal extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    Calendar newCalendar = Calendar.getInstance();



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        startDateDefault();

        return new DatePickerDialog(getActivity(), this, anof, mesf, diaf);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        dateCalendarFinal = Calendar.getInstance();
        dateCalendarFinal.set(year, month, day);
        diaf = day; mesf = month; anof = year;
        TextView dataFinal = (TextView) findViewById(R.id.dataFinal);
        dataFinal.setText(diaf+"/"+mesf+"/"+anof);
        // Do something with the time chosen by the user
    }
}
    public void startDateDefault() {
        if (anof == 0||anoi==0) {
            Calendar calendar = Calendar.getInstance();
            anof = calendar.get(Calendar.YEAR);
            mesf = calendar.get(Calendar.MONTH);
            diaf = calendar.get(Calendar.DAY_OF_MONTH);
            anoi = calendar.get(Calendar.YEAR);
            mesi = calendar.get(Calendar.MONTH);
            diai = calendar.get(Calendar.DAY_OF_MONTH);
        }
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

    public List<Note> listByIterval(Date initialDate, Date finalDate){
        int cont=0;
        List<Note> notes = null;
        String URL = NotesProvider.URL;
        Uri notesURI = Uri.parse(URL);
        Cursor result = getContentResolver().query(notesURI, null, null, null, NotesProvider.ID);
        if (result != null && result.getCount() > 0) {
            Log.v("Pesquisa", "tentando5 = " + initialDate + " " + finalDate);
            notes = new ArrayList<Note>();
            result.moveToFirst();
            while (result.isAfterLast() == false) {
                Note note = new Note();
                try {
                    if(formatter.parse(result.getString(3)).getTime()>initialDate.getTime()&&formatter.parse(result.getString(3)).getTime()<finalDate.getTime()) {
                        note.setId(result.getInt(0));
                        note.setTitle(result.getString(1));
                        note.setContent(result.getString(2));
                        note.setDate(result.getString(3));
                        notes.add(note);
                        cont++;
                    }
                    result.moveToNext();
                } catch (ParseException e) {
                    note.setDate(null);
                }
            }
        }
        result.close();
        if(cont==0){
            notes = null;
        }
        return notes;
    }
}

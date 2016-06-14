package br.ufc.dc.dspm.mynotes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyNotes extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public int dia, mes, ano;
    private NoteDAO noteDAO;
    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);
    Calendar dateCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);
        noteDAO = new NoteDAO(this);
        try {
            atualizar();
        }catch (Exception e){

        }
   }
    public void pesquise(View view){
        Intent intent = new Intent(this,Pesquisa.class);
        startActivity(intent);

    }
    //Listar Notas
    public void atualizar(){
        Note note = new Note();
        List<Note> notes = noteDAO.list();
        Iterator<Note> it = notes.iterator();
        Vector<String> values = new Vector<>();
        while (it.hasNext()) {
            note = it.next();
            values.add(note.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
        TextView data = (TextView) findViewById(R.id.data);
        data.setText("Escolha a data");
    }
    //Clicar e Editar Notas
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Intent intent = new Intent();
        position = Math.abs(position);
        String testString = Integer.toString(position);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View viewD = inflater.inflate(R.layout.dialog, null);
        final Note noteEscolhido = noteDAO.retrieve(position);
        final EditText titulo = (EditText) viewD.findViewById(R.id.editTextTitle);
        titulo.setText(noteEscolhido.getTitle()+"");
        final EditText conteúdo = (EditText) viewD.findViewById(R.id.editTextContent);
        conteúdo.setText(noteEscolhido.getContent()+"");
        final TextView data = (TextView) viewD.findViewById(R.id.data);
        data.setText(formatter.format(noteEscolhido.getDate())+"");
        builder.setView(viewD)
                // Add action buttons
                .setPositiveButton("Editar Nota", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        noteEscolhido.setTitle(titulo.getText().toString());
                        noteEscolhido.setContent(conteúdo.getText().toString());
                        noteEscolhido.setDate(dateCalendar.getTime());
                        if (dateCalendar != null)
                            noteEscolhido.setDate(dateCalendar.getTime());
                        noteDAO.update(noteEscolhido);
                        atualizar();

                    }
                });
        builder.setView(viewD)
                .setNegativeButton("Deletar Nota", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                noteDAO.delete(noteEscolhido.getId());
                atualizar();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void addNote(View view) {
        EditText titulo = (EditText) findViewById(R.id.editTextTitle);
        EditText conteuto = (EditText) findViewById(R.id.editTextContent);
        TextView data = (TextView) findViewById(R.id.data);
        if((titulo.getText().toString()=="")&&(conteuto.getText().toString()=="")) {
            Toast.makeText(this,"Por favor preencha os dados",Toast.LENGTH_SHORT).show();
        }
        else{
            EditText titleText = (EditText) findViewById(R.id.editTextTitle);
            EditText contentText = (EditText) findViewById(R.id.editTextContent);
            Note note = new Note();
            note.setTitle(titleText.getText().toString());
            note.setContent(contentText.getText().toString());
            if (dateCalendar != null)
                note.setDate(dateCalendar.getTime());
            noteDAO.create(note);
            atualizar();
            titulo.setText("");
            conteuto.setText("");
        }
    }
    //Data
    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
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

package br.ufc.dc.dspm.mynotes;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
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
    private NoteDAO noteDAO;
    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);
        noteDAO = new NoteDAO(this);
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
        Note note = new Note();
        List<Note> notes = noteDAO.listByIterval(Inicial,Final);
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
                        atualizarPesquisa(dateCalendarInicial.getTime(), dateCalendarFinal.getTime());

                    }
                });
        builder.setView(viewD)
                .setNegativeButton("Deletar Nota", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        noteDAO.delete(noteEscolhido.getId());
                        atualizarPesquisa(dateCalendarInicial.getTime(), dateCalendarFinal.getTime());
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

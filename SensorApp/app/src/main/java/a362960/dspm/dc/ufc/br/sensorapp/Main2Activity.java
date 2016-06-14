package a362960.dspm.dc.ufc.br.sensorapp;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

public class Main2Activity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SensorManager mSensorManager;

    Vector<String> values = new Vector<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> mList= mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i = 0; i < mList.size(); i++) {
            values.add("\n" + mList.get(i).getName() + "\n" + mList.get(i).getVendor() + "\n" + mList.get(i).getVersion());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
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

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}

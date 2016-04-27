package org.port0.nriedmann.dailymeds;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private static Med intialMed;
    private static Med medToEdit;
    private static MainActivity parent;
    boolean edit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_edit);
        NumberPicker waitPicker = (NumberPicker) findViewById(R.id.medWaitPicker);
        waitPicker.setMinValue(0);
        waitPicker.setMaxValue(180);
        if (intent.getIntExtra(Med.MED_ID,-1)>-1){
            intialMed=MainActivity.getMedFromListByID(intent.getIntExtra(Med.MED_ID, -1));
            edit=true;
        } else {
            intialMed=new Med(MainActivity.getCurrentID(),this);
            edit=false;
        }
        medToEdit=intialMed.clone();

    }

    @Override
    protected void onResume(){
        super.onResume();
        updateContent(false);
    }

    public void updateContent(boolean justTimeButton){
        if (!justTimeButton) {
            ImageButton image = (ImageButton) findViewById(R.id.medButton);
            EditText name = (EditText) findViewById(R.id.nameText);
            EditText description = (EditText) findViewById(R.id.descText);
            NumberPicker waitPicker = (NumberPicker) findViewById(R.id.medWaitPicker);
            image.setImageDrawable(medToEdit.getImage(this));
            image.setBackgroundColor(medToEdit.getColor(this));
            name.setText(medToEdit.getName());
            description.setText(medToEdit.getDescription());
            waitPicker.setValue(medToEdit.getWaitTime());
        }

        Button time = (Button) findViewById(R.id.medTakeButton);
        if (medToEdit.isTakeTimeSet()) {
            time.setText(getResources().getString(R.string.med_take_text_change) + " " + medToEdit.getTakeHour() + ":" + medToEdit.getTakeMinute() + ", " + medToEdit.getTakeDays(this));
        } else {
            time.setText(getString(R.string.med_take_text_new));
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        medToEdit.setName(((EditText) findViewById(R.id.nameText)).getText().toString());
        medToEdit.setDescription(((EditText) findViewById(R.id.descText)).getText().toString());
        //medToEdit.setTakeTime();
        medToEdit.setWaitTime(((NumberPicker) findViewById(R.id.medWaitPicker)).getValue());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            medToEdit.setName(((EditText) findViewById(R.id.nameText)).getText().toString());
            medToEdit.setDescription(((EditText) findViewById(R.id.descText)).getText().toString());
            //medToEdit.setTakeTime();
            medToEdit.setWaitTime(((NumberPicker) findViewById(R.id.medWaitPicker)).getValue());
            intialMed=medToEdit.clone();
            Log.d("dailymed time set",intialMed.isTakeTimeSet()+""+intialMed.getTakeHour());
            MainActivity.addMed(intialMed);

            Toast saved = Toast.makeText(getApplicationContext(),R.string.med_saved,Toast.LENGTH_SHORT);
            saved.show();

            Intent intent = new Intent();
            intent.putExtra(Med.MED_ID, intialMed.getId());
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDayTimePickerDialog(View v) {
        // Create an instance of the dialog fragment and show it
        TakeDayTimePickerDialogFragment dialog = new TakeDayTimePickerDialogFragment();
        dialog.setMed(medToEdit);
        dialog.show(getSupportFragmentManager(), "DayTimePickerFragment");
    }

    public void showImageColorPicker(View v){
        //TODO INTENT TO OPEN IMAGE COLOR ACT
        Intent imageIntent = new Intent(this,ImageColorActivity.class);
        imageIntent.putExtra(Med.MED_ID,medToEdit.getId());
        startActivity(imageIntent);
    }

    public static Med getMedToEdit(int id){
        if (id==medToEdit.getId()){
            return medToEdit;
        }
        return null;
    }
}

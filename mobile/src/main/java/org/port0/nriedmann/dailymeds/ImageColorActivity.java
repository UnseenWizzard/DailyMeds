package org.port0.nriedmann.dailymeds;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by captain on 31.08.2015.
 */
public class ImageColorActivity extends AppCompatActivity {

    Med medToEdit;
    int icon = 0;
    int color = 0;
    ImageButton[] icons;
    Button[] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int id = intent.getIntExtra(Med.MED_ID,-1);
        if (id<0){
            finish();
        }
        icons= new ImageButton[4];
        colors = new Button[6];
        medToEdit = EditActivity.getMedToEdit(id);
        this.icon=medToEdit.getImageID();
        this.color=medToEdit.getColorID();

        setContentView(R.layout.med_icon_chooser);
        icons[0] = (ImageButton) findViewById(R.id.image1);
        icons[1] = (ImageButton) findViewById(R.id.image2);
        icons[2] = (ImageButton) findViewById(R.id.image3);
        icons[3] = (ImageButton) findViewById(R.id.image4);
        colors[0] = (Button) findViewById(R.id.color1);
        colors[1] = (Button) findViewById(R.id.color2);
        colors[2] = (Button) findViewById(R.id.color3);
        colors[3] = (Button) findViewById(R.id.color4);
        colors[4] = (Button) findViewById(R.id.color5);
        colors[5] = (Button) findViewById(R.id.color6);
     }

    @Override
    protected void onResume(){
        super.onResume();
        setColor(color);
        setIcon(icon);
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
            medToEdit.setColor(color);
            medToEdit.setImage(icon);

            Toast saved = Toast.makeText(getApplicationContext(),R.string.med_saved,Toast.LENGTH_SHORT);
            saved.show();

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setIcon(int id){
        icon=id;
        for (int i=0;i<icons.length;i++){
            icons[i].setAlpha(0.3f);
        }
        icons[icon].setAlpha(1.0f);
    }

    public void imageSelected(View v){
        int id=0;
        switch (v.getId()){
            case(R.id.image1):
                id=0;
                break;
            case(R.id.image2):
                id=1;
                break;
            case(R.id.image3):
                id=2;
                break;
            case(R.id.image4):
                id=3;
                break;
        }
        setIcon(id);
    }

    private void setColor(int id){
        color=id;
        for (int i=0;i<colors.length;i++){
            colors[i].setAlpha(0.3f);
        }
        colors[color].setAlpha(1.0f);
    }
    public void colorSelected(View v){
        int id=0;
        switch (v.getId()){
            case(R.id.color1):
                id=0;
                break;
            case(R.id.color2):
                id=1;
                break;
            case(R.id.color3):
                id=2;
                break;
            case(R.id.color4):
                id=3;
                break;
            case(R.id.color5):
                id=4;
                break;
            case(R.id.color6):
                id=5;
                break;
        }
        setColor(id);
    }

}

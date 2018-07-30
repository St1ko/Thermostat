package nl.tue.thermostat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import org.thermostatapp.util.*;

/**
 * Created by s152888 on 13-6-2016.
 */
public class Monday extends Activity{
    int days = 0;
    String[] dayArray = new String[5];
    String[] nightArray =  new String[5];
    double dtemp;
    double ntemp;
    TextView dayTemp;
    TextView nightTemp;
    TextView Dtime1, Dtime2, Dtime3, Dtime4, Dtime5, Ntime1,Ntime2, Ntime3, Ntime4, Ntime5;
    Vibrator myVib;
    String[] dayName = new String[7];
    EditText setday;
    EditText setnight;
    Button dayTempPlus, dayTempMinus, nightTempPlus, nightTempMinus;
    WeekProgram wpg;


    TextView day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monday);
        dayTempPlus = (Button)findViewById(R.id.dayTempPlus);
        dayTempMinus = (Button) findViewById(R.id.dayTempMinus);
        nightTempPlus = (Button) findViewById(R.id.nightTempPlus);
        nightTempMinus = (Button) findViewById(R.id.nightTempMinus);
        dayTemp = (TextView) findViewById(R.id.dayTemp);
        nightTemp = (TextView) findViewById(R.id.nightTemp);
        ImageView dayplus = (ImageView)findViewById(R.id.dayplus);
        ImageView daymin = (ImageView)findViewById(R.id.daymin);
        ImageView trash1 = (ImageView) findViewById(R.id.trash1);
        ImageView trash2 = (ImageView) findViewById(R.id.trash2);
        ImageView trash3 = (ImageView) findViewById(R.id.trash3);
        ImageView trash4 = (ImageView) findViewById(R.id.trash4);
        ImageView trash5 = (ImageView) findViewById(R.id.trash5);

        Button add = (Button)findViewById(R.id.add);
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        setday = (EditText)findViewById(R.id.setday);
        setnight = (EditText)findViewById(R.id.setnight);


        HeatingSystem.BASE_ADDRESS = "http://wwwis.win.tue.nl/2id40-ws/37";
        HeatingSystem.WEEK_PROGRAM_ADDRESS = HeatingSystem.BASE_ADDRESS + "/weekProgram";

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dtemp = Double.parseDouble(HeatingSystem.get("dayTemperature"));
                    ntemp = Double.parseDouble(HeatingSystem.get("nightTemperature"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dayTemp.setText(dtemp + "\u2103");
                            nightTemp.setText(ntemp + "\u2103");
                        }
                    });
                    wpg = HeatingSystem.getWeekProgram();
                    refreshDisplay();
                } catch (Exception e) {
                    System.err.println("Error from getdata" + e);
                }
            }
        }).start();




        days = days%7;
        dayName[0] = "Monday";
        dayName[1] = "Tuesday";
        dayName[2] = "Wednesday";
        dayName[3] = "Thursday";
        dayName[4] = "Friday";
        dayName[5] = "Saturday";
        dayName[6] = "Sunday";
        day = (TextView)findViewById(R.id.day);

        dayplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myVib.vibrate(50);
                days++;
                days = days%7;
                day.setText(dayName[days]);
                refreshDisplay();

            }
        });
        daymin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myVib.vibrate(50);
                days--;
                if (days < 0)
                    days = 6;
                days = days%7;
                day.setText(dayName[days]);
                refreshDisplay();
            }
        });

        dayTempPlus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dtemp++;
                dayTemp.setText(dtemp + " \u2103");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HeatingSystem.put("dayTemperature", Double.toString(dtemp));
                        } catch (Exception e) {
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
                if (dtemp >= 30) {
                    dayTempPlus.setEnabled(false);
                } else {
                    dayTempPlus.setEnabled(true);
                }
                if (dtemp <= 5) {
                    dayTempMinus.setEnabled(false);
                } else {
                    dayTempMinus.setEnabled(true);
                }
            }
        });

        dayTempMinus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dtemp--;
                dayTemp.setText(dtemp + " \u2103");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HeatingSystem.put("dayTemperature", Double.toString(dtemp));
                        } catch (Exception e) {
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
                if (dtemp >= 30) {
                    dayTempPlus.setEnabled(false);
                } else {
                    dayTempPlus.setEnabled(true);
                }
                if (dtemp <= 5) {
                    dayTempMinus.setEnabled(false);
                } else {
                    dayTempMinus.setEnabled(true);
                }
            }
        });

        nightTempPlus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ntemp++;
                nightTemp.setText(ntemp + " \u2103");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HeatingSystem.put("nightTemperature", Double.toString(ntemp));
                        } catch (Exception e) {
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
                if (ntemp >= 30) {
                    nightTempPlus.setEnabled(false);
                } else {
                    nightTempPlus.setEnabled(true);
                }
                if (ntemp <= 5) {
                    nightTempMinus.setEnabled(false);
                } else {
                    nightTempMinus.setEnabled(true);
                }
            }
        });

        nightTempMinus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ntemp--;
                nightTemp.setText(ntemp + " \u2103");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HeatingSystem.put("nightTemperature", Double.toString(ntemp));
                        } catch (Exception e) {
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
                if (ntemp >= 30) {
                    nightTempPlus.setEnabled(false);
                } else {
                    nightTempPlus.setEnabled(true);
                }
                if (ntemp <= 5) {
                    nightTempMinus.setEnabled(false);
                } else {
                    nightTempMinus.setEnabled(true);
                }
            }
        });

        trash1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 0; i<10; i++) {
                                if (wpg.data.get(dayName[days]).get(i).getTime().equals(dayArray[0]) && wpg.data.get(dayName[days]).get(i).getType().equals("day")) {
                                    wpg.data.get(dayName[days]).set(i, new Switch("day", false, "00:00"));
                                    break;
                                }
                            }
                            for (int i = 0; i<10; i++){
                                if(wpg.data.get(dayName[days]).get(i).getTime().equals(nightArray[0]) &&wpg.data.get(dayName[days]).get(i).getType().equals("night")){
                                    wpg.data.get(dayName[days]).set(i, new Switch("night", false, "00:00"));
                                    break;
                                }
                            }
                            HeatingSystem.setWeekProgram(wpg);
                            refreshDisplay();

                    } catch (Exception e){
                        System.err.println("Error from getdata" + e);
                    }
                }


        }).start();
        }
    });
        trash2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 0; i<10; i++) {
                                if (wpg.data.get(dayName[days]).get(i).getTime().equals(dayArray[1]) && wpg.data.get(dayName[days]).get(i).getType().equals("day")) {
                                    wpg.data.get(dayName[days]).set(i, new Switch("day", false, "00:00"));
                                    break;
                                }
                            }

                            for (int i=0; i<10; i++){
                                if(wpg.data.get(dayName[days]).get(i).getTime().equals(nightArray[1]) &&wpg.data.get(dayName[days]).get(i).getType().equals("night")){
                                    wpg.data.get(dayName[days]).set(i, new Switch("night", false, "00:00"));
                                    break;
                                }
                            }
                            HeatingSystem.setWeekProgram(wpg);
                            refreshDisplay();

                        } catch (Exception e){
                            System.err.println("Error from getdata" + e);
                        }
                    }


                }).start();
            }
        });
        trash3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 0; i<10; i++) {
                                if (wpg.data.get(dayName[days]).get(i).getTime().equals(dayArray[2]) && wpg.data.get(dayName[days]).get(i).getType().equals("day")) {
                                    wpg.data.get(dayName[days]).set(i, new Switch("day", false, "00:00"));
                                    break;
                                }
                            }
                            for (int i=0; i<10; i++){
                                if(wpg.data.get(dayName[days]).get(i).getTime().equals(nightArray[2]) &&wpg.data.get(dayName[days]).get(i).getType().equals("night")){
                                    wpg.data.get(dayName[days]).set(i, new Switch("night", false, "00:00"));
                                    break;
                                }
                            }
                            HeatingSystem.setWeekProgram(wpg);
                            refreshDisplay();

                        } catch (Exception e){
                            System.err.println("Error from getdata" + e);
                        }
                    }


                }).start();
            }
        });
        trash4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 0; i<10; i++) {
                                if (wpg.data.get(dayName[days]).get(i).getTime().equals(dayArray[3]) && wpg.data.get(dayName[days]).get(i).getType().equals("day")) {
                                    wpg.data.get(dayName[days]).set(i, new Switch("day", false, "00:00"));
                                    break;
                                }
                            }
                            for (int i=0; i<10; i++){
                                if(wpg.data.get(dayName[days]).get(i).getTime().equals(nightArray[3]) &&wpg.data.get(dayName[days]).get(i).getType().equals("night")){
                                    wpg.data.get(dayName[days]).set(i, new Switch("night", false, "00:00"));
                                    break;
                                }
                            }
                            HeatingSystem.setWeekProgram(wpg);
                            refreshDisplay();

                        } catch (Exception e){
                            System.err.println("Error from getdata" + e);
                        }
                    }


                }).start();
            }
        });
        trash5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 0; i<10; i++) {
                                if (wpg.data.get(dayName[days]).get(i).getTime().equals(dayArray[4]) && wpg.data.get(dayName[days]).get(i).getType().equals("day")) {
                                    wpg.data.get(dayName[days]).set(i, new Switch("day", false, "00:00"));
                                    break;
                                }
                            }
                            for (int i=0; i<10; i++){
                                if(wpg.data.get(dayName[days]).get(i).getTime().equals(nightArray[4]) &&wpg.data.get(dayName[days]).get(i).getType().equals("night")){
                                    wpg.data.get(dayName[days]).set(i, new Switch("night", false, "00:00"));
                                    break;
                                }
                            }
                            HeatingSystem.setWeekProgram(wpg);
                            refreshDisplay();

                        } catch (Exception e){
                            System.err.println("Error from getdata" + e);
                        }
                    }


                }).start();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            wpg = HeatingSystem.getWeekProgram();
                            System.out.println(wpg.data.get(dayName[days]).get(3).getType());
                            if (Switch.isValidTimeSyntax(setday.getText().toString())!= false && Switch.isValidTimeSyntax(setnight.getText().toString())!= false && !setday.getText().toString().equals("00:00") && !setnight.getText().toString().equals("00:00")) {
                                for(int i = 0; i<10; i++){
                                    if(wpg.data.get(dayName[days]).get(i).getState() == false && wpg.data.get(dayName[days]).get(i).getType().equals("night")) {
                                        wpg.data.get(dayName[days]).set(i,new Switch("night", true, setnight.getText().toString()));
                                        break;
                                    }
                                }

                                for(int i = 0; i<10; i++){
                                    if(wpg.data.get(dayName[days]).get(i).getState() == false && wpg.data.get(dayName[days]).get(i).getType().equals("day")) {
                                        wpg.data.get(dayName[days]).set(i,new Switch("day", true, setday.getText().toString()));
                                        break;
                                    }
                                }


                                HeatingSystem.setWeekProgram(wpg);
                                refreshDisplay();
                                wpg = new HeatingSystem().getWeekProgram();
                            }
                        } catch (Exception e) {
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

    }

    protected void refreshDisplay(){
        Dtime1 = (TextView) findViewById(R.id.Dtime1);
        Dtime2 = (TextView) findViewById(R.id.Dtime2);
        Dtime3 = (TextView) findViewById(R.id.Dtime3);
        Dtime4 = (TextView) findViewById(R.id.Dtime4);
        Dtime5 = (TextView) findViewById(R.id.Dtime5);
        Ntime1 = (TextView) findViewById(R.id.Ntime1);
        Ntime2 = (TextView) findViewById(R.id.Ntime2);
        Ntime3 = (TextView) findViewById(R.id.Ntime3);
        Ntime4 = (TextView) findViewById(R.id.Ntime4);
        Ntime5 = (TextView) findViewById(R.id.Ntime5);

        for (int i = 0; i < 5 ; dayArray[i] = " ", i++);
        for (int i = 0; i < 5 ; nightArray[i] = " ", i++);

        for(int i = 0, j = 0; j<10; j++){
            if (wpg.data.get(dayName[days]).get(j).getType().equals("day") && wpg.data.get(dayName[days]).get(j).getState()){
                dayArray[i] = wpg.data.get(dayName[days]).get(j).getTime();
                System.out.println("day " + i + " " + dayArray[i]);
                i++;
            }

        }

        for(int i = 0, j = 0; j<10; j++){
            if (wpg.data.get(dayName[days]).get(j).getType().equals("night") && wpg.data.get(dayName[days]).get(j).getState()){
                nightArray[i] = wpg.data.get(dayName[days]).get(j).getTime();
                System.out.println("night " + i + " " + nightArray[i]);
                i++;
            }
        }

        runOnUiThread(new Runnable() {


            @Override
            public void run() {

                Dtime1.setText(dayArray[0]);
                Dtime2.setText(dayArray[1]);
                Dtime3.setText(dayArray[2]);
                Dtime4.setText(dayArray[3]);
                Dtime5.setText(dayArray[4]);

                Ntime1.setText(nightArray[0]);
                Ntime2.setText(nightArray[1]);
                Ntime3.setText(nightArray[2]);
                Ntime4.setText(nightArray[3]);
                Ntime5.setText(nightArray[4]);
            }
        });
    }
}

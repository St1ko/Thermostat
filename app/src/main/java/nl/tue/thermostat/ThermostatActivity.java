package nl.tue.thermostat;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.devadvance.circularseekbar.CircularSeekBar;
import com.devadvance.circularseekbar.CircularSeekBar.OnCircularSeekBarChangeListener;
import org.thermostatapp.util.HeatingSystem;


import java.net.ConnectException;

public class ThermostatActivity extends Activity {


    public static double vtemp;
    int stemp;
    String getDay, getTime, getVacState, getCTemp, getTTemp;
    static Button bPlus;
    static Button bMinus;
    static TextView temp;
    TextView day;
    TextView clock;
    TextView curTemp;
    ToggleButton vacation;
    CircularSeekBar circularSeekBar;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermostat);

        bPlus = (Button)findViewById(R.id.bPlus);
        bMinus = (Button)findViewById(R.id.bMinus);
        Button weekOverview = (Button)findViewById(R.id.week_overview);
        vacation = (ToggleButton)findViewById(R.id.vacation);

        day = (TextView)findViewById(R.id.day);
        temp = (TextView)findViewById(R.id.temp);
        clock = (TextView)findViewById(R.id.clock);
        curTemp = (TextView)findViewById(R.id.curTemp);

        circularSeekBar = (CircularSeekBar) findViewById(R.id.circularSeekBar1);

        handler = new Handler();

        //Updates the clock
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Runnable networkThread = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getTime = HeatingSystem.get("time");
                            getDay = HeatingSystem.get("day");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    clock.setText(getTime);
                                    day.setText(getDay);
                                }
                            });
                        } catch (ConnectException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Thread t = new Thread(networkThread);
                t.start();
                handler.postDelayed(this, 200);
            }
        };
        handler.postDelayed(runnable, 200);

        //Auto updates current temperature with server value
        final Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                Runnable networkThread = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getCTemp = HeatingSystem.get("currentTemperature");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    curTemp.setText(getCTemp+ "\u00b0");
                                }
                            });
                        } catch (Exception e) {
                            System.err.println("Error from getdata " + e);
                        }
                    }
                };
                Thread c = new Thread(networkThread);
                c.start();
                handler.postDelayed(this, 500);
            }
        };
        handler.postDelayed(runnable1, 500);

        //Auto updates target temperature and seekbar with server's targettemperature
        final Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                Runnable networkThread = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getTTemp = HeatingSystem.get("targetTemperature");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    temp.setText(getTTemp+ "\u00b0");
                                    vtemp = Double.parseDouble(getTTemp);
                                    stemp = (int) ((vtemp * 10) - 50);
                                    circularSeekBar.setProgress(stemp);
                                }
                            });
                        } catch (Exception e) {
                            System.err.println("Error from getdata " + e);
                        }
                    }
                };
                Thread n = new Thread(networkThread);
                n.start();
                handler.postDelayed(runnable2, 1000);
            }
        };
        handler.postDelayed(runnable2, 1000);

        //checks whether weekprogramstate is on/off and updates the state of vacation button
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getVacState = HeatingSystem.get("weekProgramState");
                    if (getVacState.equals("off")) {
                        vacation.setChecked(true);
                    }
                } catch (Exception e) {
                    System.err.println("Error from getdata" + e);
                }
            }
        }).start();


        //When vacation button is pressed, weekProgramState is changed
        vacation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vacation.isChecked()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HeatingSystem.put("weekProgramState", "off");
                            } catch (Exception e) {
                                System.err.println("Error from getdata " + e);
                            }
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HeatingSystem.put("weekProgramState", "on");
                            } catch (Exception e) {
                                System.err.println("Error from getdata " + e);

                            }
                        }
                    }).start();
                }
            }
        });

        //When weekoverview button is pressed, the weekoverview activity is openend
        weekOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Monday.class);
                startActivity(intent);
            }
        });

        //Controls seekbar behaviour
        circularSeekBar.setOnSeekBarChangeListener(new OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                vtemp = (double) progress/10 + 5;
                temp.setText(vtemp+"\u00b0");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String tt = Double.toString(vtemp);
                            HeatingSystem.put("targetTemperature", tt);
                        } catch (Exception e) {
                            System.err.println("Error from getdata "+e);
                        }
                    }
                }).start();

                withinBounderies();
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        //Controls plus button
        bPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vtemp = ((10 * vtemp) + 1)/10;
                temp.setText(vtemp+"\u00b0");
                stemp = (int) (vtemp * 10) - 50;
                circularSeekBar.setProgress(stemp);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String tt = Double.toString(vtemp);
                            HeatingSystem.put("targetTemperature", tt);

                        } catch (Exception e) {
                            System.err.println("Error from getdata "+e);
                        }
                    }
                }).start();

                withinBounderies();
            }
        });

        //Controls minus button;
        bMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vtemp = ((10 * vtemp) - 1)/10;
                temp.setText(vtemp+"\u00b0");
                stemp = (int) (vtemp * 10) - 50;
                circularSeekBar.setProgress(stemp);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String tt = Double.toString(vtemp);
                            HeatingSystem.put("targetTemperature", tt);

                        } catch (Exception e) {
                            System.err.println("Error from getdata "+e);
                        }
                    }
                }).start();

                withinBounderies();
            }
        });
    }

    public static void withinBounderies() {
        if (vtemp >= 30) {
            bPlus.setEnabled(false);
        } else {
            bPlus.setEnabled(true);
        }

        if (vtemp <= 5) {
            bMinus.setEnabled(false);
        } else {
            bMinus.setEnabled(true);
        }
    }

}



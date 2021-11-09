package co.jottan.drinkwater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
  // local variables
  private SharedPreferences preferences;

  private TimePicker timePicker;
  private EditText editText;
  private Button button;

  private boolean activated = false;

  private int hour;
  private int minute;
  private int interval;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // filling local variables
    timePicker = findViewById(R.id.timePicker);
    editText = findViewById(R.id.editText);
    button = findViewById(R.id.button);

    timePicker.setIs24HourView(true);
    button.setOnClickListener(clicked);

    preferences = getSharedPreferences("db", MODE_PRIVATE);

    activated = preferences.getBoolean("activated", false);

    if (activated) {

      int interval = preferences.getInt("interval", 0);
      int hour = preferences.getInt("hour", timePicker.getCurrentHour());
      int minute = preferences.getInt("minute", timePicker.getCurrentMinute());

      int newColor = ContextCompat.getColor(MainActivity.this, R.color.black);

      button.setText(R.string.pause);
      button.setBackgroundColor(newColor);
      timePicker.setCurrentHour(hour);
      timePicker.setCurrentMinute(minute);
      editText.setText(String.valueOf(interval));
    }

  }

  public View.OnClickListener clicked = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      String sInterval = editText.getText().toString();

      if (sInterval.isEmpty()) {
        Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
        return;
      }

      hour = timePicker.getCurrentHour();
      minute = timePicker.getCurrentMinute();
      interval = Integer.parseInt(sInterval);

      if (!activated) {
        int newColor = ContextCompat.getColor(MainActivity.this, R.color.black);
        button.setText(R.string.pause);
        button.setBackgroundColor(newColor);
        activated = true;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("activated", true);
        editor.putInt("hour", hour);
        editor.putInt("minute", minute);
        editor.putInt("interval", interval);
        editor.apply();

      } else {
        int newColor = ContextCompat.getColor(MainActivity.this, R.color.purple_500);
        button.setText(R.string.button_start_ptBR);
        button.setBackgroundColor(newColor);
        activated = false;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("activated", false);
        editor.remove("hour");
        editor.remove("minute");
        editor.remove("interval");
        editor.apply();
      }

      Log.i("Interval", "hour: "+hour+" minute: "+minute+" interval: "+interval);
    }
  };

}
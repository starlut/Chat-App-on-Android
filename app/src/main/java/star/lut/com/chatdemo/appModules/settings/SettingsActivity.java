package star.lut.com.chatdemo.appModules.settings;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import star.lut.com.chatdemo.Base.BaseActivity;
import star.lut.com.chatdemo.R;
import star.lut.com.chatdemo.constants.ValueConstants;
import star.lut.com.chatdemo.db.PreferenceManager;

public class SettingsActivity extends BaseActivity {
    @BindView(R.id.clCustomNotification) public ConstraintLayout clCustomNotification;
    @BindView(R.id.etFrom) public EditText etFrom;
    @BindView(R.id.etTo) public EditText etTO;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    RadioButton radioButton;

    String from, to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager manager = new PreferenceManager(this);
        if (manager.getNotificationSettings() == 0){
            radioGroup.check(R.id.rbAlways);
        }else if (manager.getNotificationSettings() == 1){
            radioGroup.check(R.id.rbOfficeTime);
        }else {
            radioGroup.check(R.id.rbNoNot);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_settings;
    }


    public void checkButton(View view){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);

        if (radioId == R.id.rbOfficeTime){
            clCustomNotification.setVisibility(View.VISIBLE);
        }else {
            clCustomNotification.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btnApply)
    public void applySettings(){
        String notificationSettings = radioButton.getText().toString();
        PreferenceManager manager = new PreferenceManager(this);
        Toast.makeText(this, "you will be notified: "+notificationSettings , Toast.LENGTH_LONG).show();
        if (notificationSettings.equals(getString(R.string.rb_always))){
            manager.setNotificationSettings(ValueConstants.NOTIFICATION_ALWAYS);
        }else if (notificationSettings.equals(getString(R.string.rb_office_time))) {
            if (from != null && to != null) {
                manager.setNotificationSettings(ValueConstants.NOTIFICATION_CUSTOM);
                manager.setNotificationTimeFrom(from);
                manager.setNotificationTimeTo(to);
            }else {
                Toast.makeText(this, "Custom notification time requires you to enter valid time", Toast.LENGTH_LONG).show();
            }
        } else {
            manager.setNotificationSettings(ValueConstants.NOTIFICATION_NEVER);
        }

        finish();
    }

    @OnClick(R.id.etFrom)
    public void etFrom(){
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                from = hourOfDay+":"+ minute;
                etFrom.setText(from);
            }
        }, hour, minute, true);
        timePickerDialog.setTitle("Select from time");
        timePickerDialog.show();
    }

    @OnClick(R.id.etTo)
    public void etTo(){
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                to = hourOfDay+":"+ minute;
                etTO.setText(to);
            }
        }, hour, minute, true);
        timePickerDialog.setTitle("Select to time");
        timePickerDialog.show();
    }

    @OnClick(R.id.btnDone)
    public void doneCustomAlarm(){
        if (from != null && to != null){
            clCustomNotification.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btnCancel)
    public void cancelCustomAlarm(){
        clCustomNotification.setVisibility(View.GONE);
        radioGroup.check(R.id.rbAlways);
    }
}

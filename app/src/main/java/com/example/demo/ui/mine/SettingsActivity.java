package com.example.demo.ui.mine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.demo.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            EditTextPreference serverIp = findPreference(getResources().getString(R.string.server_ip_key));
            EditTextPreference serverPort = findPreference(getResources().getString(R.string.server_port_key));
            EditTextPreference serverPassword = findPreference(getResources().getString(R.string.server_password_key));
            String str = sharedPreferences.getString(getResources().getString(R.string.server_ip_key), getResources().getString(R.string.not_set));
            if (str.isEmpty()) {
                str = getResources().getString(R.string.not_set);
            }
            serverIp.setSummary(str);
            str = sharedPreferences.getString(getResources().getString(R.string.server_port_key), getResources().getString(R.string.not_set));
            if (str.isEmpty()) {
                str = getResources().getString(R.string.not_set);
            }
            serverPort.setSummary(str);
            str = sharedPreferences.getString(getResources().getString(R.string.server_password_key), getResources().getString(R.string.not_set));
            if (str.isEmpty()) {
                str = getResources().getString(R.string.not_set);
            }
            else {
                str = getResources().getString(R.string.password);
            }
            serverPassword.setSummary(str);
            serverIp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (newValue.toString().isEmpty()) {
                        serverIp.setSummary(getResources().getString(R.string.not_set));
                    }
                    else {
                        serverIp.setSummary(newValue.toString());
                    }
                    return true;
                }
            });
            serverPort.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (newValue.toString().isEmpty()) {
                        serverPort.setSummary(getResources().getString(R.string.not_set));
                    }
                    else {
                        serverPort.setSummary(newValue.toString());
                    }
                    return true;
                }
            });
            serverPort.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });
            serverPassword.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (newValue.toString().isEmpty()) {
                        serverPassword.setSummary(getResources().getString(R.string.not_set));
                    }
                    else {
                        serverPassword.setSummary(getResources().getString(R.string.password));
                    }
                    return true;
                }
            });
            serverPassword.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    editText.setText("");
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
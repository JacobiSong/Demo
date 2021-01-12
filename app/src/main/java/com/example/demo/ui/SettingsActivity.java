package com.example.demo.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
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
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            EditTextPreference serverIp = findPreference(getResources().getString(R.string.server_ip_key));
            EditTextPreference serverPort = findPreference(getResources().getString(R.string.server_port_key));
            String str = sharedPreferences.getString(getResources().getString(R.string.server_ip_key), getResources().getString(R.string.not_set));
            if (str.isEmpty()) {
                str = getResources().getString(R.string.not_set);
            }
            if (serverIp != null) {
                serverIp.setSummary(str);
                serverIp.setOnPreferenceChangeListener((preference, newValue) -> {
                    if (newValue.toString().isEmpty()) {
                        serverIp.setSummary(getResources().getString(R.string.not_set));
                    } else {
                        serverIp.setSummary(newValue.toString());
                    }
                    return true;
                });
            }
            str = sharedPreferences.getString(getResources().getString(R.string.server_port_key), getResources().getString(R.string.not_set));
            if (str.isEmpty()) {
                str = getResources().getString(R.string.not_set);
            }
            if (serverPort != null) {
                serverPort.setSummary(str);
                serverPort.setOnPreferenceChangeListener((preference, newValue) -> {
                    if (newValue.toString().isEmpty()) {
                        serverPort.setSummary(getResources().getString(R.string.not_set));
                    }
                    else {
                        serverPort.setSummary(newValue.toString());
                    }
                    return true;
                });
                serverPort.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
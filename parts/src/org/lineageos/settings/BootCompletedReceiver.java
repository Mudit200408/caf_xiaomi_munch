/*
 * Copyright (C) 2018 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings;
import android.os.IBinder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.SystemProperties;
import androidx.preference.PreferenceManager;
import org.lineageos.settings.utils.FileUtils;
import org.lineageos.settings.dirac.DiracUtils;
import org.lineageos.settings.thermal.ThermalUtils;
import org.lineageos.settings.refreshrate.RefreshUtils;

public class BootCompletedReceiver extends BroadcastReceiver {
	   private static final String DC_DIMMING_ENABLE_KEY = "dc_dimming_enable";
    private static final String DC_DIMMING_NODE = "/sys/devices/platform/soc/soc:qcom,dsi-display-primary/anti_flicker";

    @Override
    public void onReceive(final Context context, Intent intent) {
    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (!intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            return;
        }

        // Dirac
        DiracUtils.onBootCompleted(context);

        // Thermal Profiles
        ThermalUtils.startService(context);

        // Per app refresh rate
        RefreshUtils.startService(context);
        
        FileUtils.enableService(context);
        
        final IBinder displayToken = 
        boolean dcDimmingEnabled = sharedPrefs.getBoolean(DC_DIMMING_ENABLE_KEY, false);
        FileUtils.writeLine(DC_DIMMING_NODE, dcDimmingEnabled ? "1" : "0"); 
    }
}

/*
     Copyright (C) 2024 the risingOS Android Project
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at
          http://www.apache.org/licenses/LICENSE-2.0
     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
*/
package com.android.systemui.lockscreen;

import android.content.Context;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AppVolume;
import android.media.AudioManager;
import android.os.DeviceIdleManager;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.android.systemui.Dependency;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.R;

import java.util.List;

public class ActivityLauncherUtils {

    private final Context mContext;
    private final ActivityStarter mActivityStarter;
    private PackageManager mPackageManager;

    public ActivityLauncherUtils(Context context) {
        this.mContext = context;
        this.mActivityStarter = Dependency.get(ActivityStarter.class);
        mPackageManager = mContext.getPackageManager();
    }

    public String getInstalledMusicApp() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_MUSIC);
        final  List<ResolveInfo> musicApps = mPackageManager.queryIntentActivities(intent, 0);
        ResolveInfo musicApp = musicApps.isEmpty() ? null : musicApps.get(0);
        return musicApp != null ? musicApp.activityInfo.packageName : "";
    }

    private void launchAppIfAvailable(Intent launchIntent, @StringRes int appTypeResId) {
        final List<ResolveInfo> apps = mPackageManager.queryIntentActivities(launchIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (!apps.isEmpty()) {
            mActivityStarter.startActivity(launchIntent, true);
        } else {
            showNoDefaultAppFoundToast(appTypeResId);
        }
    }
    
    public void launchMediaPlayerApp() {
        String packageName = getActiveMediaPackage();
        if (!packageName.isEmpty()) {
            Intent launchIntent = mPackageManager.getLaunchIntentForPackage(packageName);
            if (launchIntent != null) {
                mActivityStarter.startActivity(launchIntent, true);
            }
        }
    }
    
    public String getActiveMediaPackage() {
        return getActiveVolumeApp() == "" ? getInstalledMusicApp() : getActiveVolumeApp();
    }

    private void showNoDefaultAppFoundToast(@StringRes int appTypeResId) {
        Toast.makeText(mContext, mContext.getString(appTypeResId) + " not found", Toast.LENGTH_SHORT).show();
    }
    
    private String getActiveVolumeApp() {
        String appVolumeActivePackageName = "";
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        for (AppVolume av : audioManager.listAppVolumes()) {
            if (av.isActive()) {
                appVolumeActivePackageName = av.getPackageName();
                break;
            }
        }
        return appVolumeActivePackageName;
    }
}

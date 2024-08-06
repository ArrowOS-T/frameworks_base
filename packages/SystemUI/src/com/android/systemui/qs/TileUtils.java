/*
* Copyright (C) 2022-2024 crDroid Android Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.UserHandle;
import android.provider.Settings;

public class TileUtils {

   public static boolean isQsWidgetsEnabled(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), 
            "qs_widgets_enabled",0, UserHandle.USER_CURRENT) != 0;
   }

   public static boolean canShowQsWidgets(Context context) {
        return isQsWidgetsEnabled(context)
            && context.getResources().getConfiguration().orientation 
            != Configuration.ORIENTATION_LANDSCAPE;
   }
}

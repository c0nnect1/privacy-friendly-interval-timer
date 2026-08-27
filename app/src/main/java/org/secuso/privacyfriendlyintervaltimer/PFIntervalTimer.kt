/*
 This file is part of Privacy Friendly Intervaltimer.

 Privacy Friendly Sudoku is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.

 Privacy Friendly Sudoku is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Privacy Friendly Sudoku. If not, see <http://www.gnu.org/licenses/>.
 */
package org.secuso.privacyfriendlyintervaltimer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.getSystemService
import androidx.work.Configuration
import org.secuso.privacyfriendlyintervaltimer.backup.BackupCreator
import org.secuso.privacyfriendlyintervaltimer.backup.BackupRestorer
import org.secuso.privacyfriendlybackup.api.pfa.BackupManager

class PFIntervalTimer : Application(), Configuration.Provider {
    override fun onCreate() {
        BackupManager.backupCreator = BackupCreator()
        BackupManager.backupRestorer = BackupRestorer()
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        createNotificationChannel()
    }

    /**
     * The workout notification is posted to this channel. Without it, Android 8 and above drop
     * the notification silently and the timer cannot run as a foreground service.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            IntervalTimerApp.CHANNEL_ID,
            getString(R.string.notification_channel_workout),
            NotificationManager.IMPORTANCE_LOW
        )
        channel.setShowBadge(false)

        getSystemService<NotificationManager>()?.createNotificationChannel(channel)
    }

    override val workManagerConfiguration by lazy {
        Configuration.Builder().setMinimumLoggingLevel(Log.INFO).build()
    }
}

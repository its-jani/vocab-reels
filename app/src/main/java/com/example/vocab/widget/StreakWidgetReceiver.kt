package com.example.vocab.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 * AppWidgetProvider entry point for the Streak & Goal Tracker widget.
 * Registered in AndroidManifest.xml; binds res/xml/streak_widget_info.xml.
 */
class StreakWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = StreakWidget()
}

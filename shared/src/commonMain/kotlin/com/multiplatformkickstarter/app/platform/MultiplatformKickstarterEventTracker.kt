package com.multiplatformkickstarter.app.platform

import co.touchlab.kermit.Logger

class MultiplatformKickstarterEventTracker : Tracker {
    override fun onEventTracked(event: TrackEvents) {
        Logger.d { "Event: ${event.name}" }
    }
}

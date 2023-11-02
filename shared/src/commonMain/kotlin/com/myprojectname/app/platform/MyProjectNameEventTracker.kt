package com.myprojectname.app.platform

import co.touchlab.kermit.Logger

class MyProjectNameEventTracker : Tracker {
    override fun onEventTracked(event: TrackEvents) {
        Logger.d { "Event: ${event.name}" }
    }
}

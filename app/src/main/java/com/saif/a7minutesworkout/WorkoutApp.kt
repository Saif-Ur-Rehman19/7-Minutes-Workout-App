package com.saif.a7minutesworkout

import android.app.Application

class WorkoutApp : Application() {

    val db by lazy {
        HistoryDatabase.getInstance(this)
        //lazy should be used when any of your objects are heavy and they take a long time to
       // initialize. Here lazy properties can help to skip that delay which could be caused by the
      // initialization of those objects. As lazy would only initialize the variable when it is
     // called, otherwise it won't be created.
    }
}
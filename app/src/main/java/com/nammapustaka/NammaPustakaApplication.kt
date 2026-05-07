package com.nammapustaka

import android.app.Application
import com.nammapustaka.data.AppDatabase

class NammaPustakaApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}

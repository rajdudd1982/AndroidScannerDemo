package com.scanner.demo.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class PdfCreationWorker(context: Context, parameters: WorkerParameters) : Worker(context, parameters) {

    override fun doWork(): Result {
        TODO("Not yet implemented")
    }

    override fun onStopped() {
        super.onStopped()
    }

}
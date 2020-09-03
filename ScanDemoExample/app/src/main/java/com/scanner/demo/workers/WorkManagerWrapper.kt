package com.scanner.demo.workers

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkManagerWrapper {

  fun getWorkManager(context: Context) : WorkManager = WorkManager.getInstance(context)

  fun enqueueRequest(tag: String, context: Context, values: Map<String, String>) {
      var constraints = Constraints.Builder().build()
      var workRequest =
              OneTimeWorkRequestBuilder<PdfCreationWorker>()
                      .setConstraints(constraints)
                      .addTag(tag)
                      .setBackoffCriteria(BackoffPolicy.LINEAR, 2000, TimeUnit.MILLISECONDS)
                      .setInputData(Data.Builder().putAll(values).build())
                      .build()
      getWorkManager(context).enqueueUniqueWork(tag, ExistingWorkPolicy.REPLACE, workRequest).state
  }

}
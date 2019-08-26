package com.minghan.lomotif.media.dataSource

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class BackgroundThreadExecutor : Executor {
    private val executorService = Executors.newFixedThreadPool(3)
    override fun execute(command: Runnable) {
        executorService.execute(command)
    }
}

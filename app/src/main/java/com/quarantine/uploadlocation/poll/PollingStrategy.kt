package com.quarantine.uploadlocation.poll

interface PollingStrategy {

    fun getNextPollInterval():Long

    fun currentPollInterval():Long

    fun reset()

}
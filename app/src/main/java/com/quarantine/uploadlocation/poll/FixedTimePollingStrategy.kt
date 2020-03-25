package com.quarantine.uploadlocation.poll

class FixedTimePollingStrategy(val pollingInterval:Long):
    PollingStrategy {

    override fun getNextPollInterval(): Long {
        return pollingInterval
    }

    override fun currentPollInterval(): Long {
        return pollingInterval
    }
    override fun reset() {
        // nothing to do
    }
}
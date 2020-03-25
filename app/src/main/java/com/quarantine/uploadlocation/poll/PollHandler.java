package com.quarantine.uploadlocation.poll;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class PollHandler extends Handler {
    private static final int MESSAGE_START_POLLING = 1;
    private static final int MESSAGE_POLL = 2;
    private static final int MESSAGE_STOP_POLLING = 3;

    private PollingStrategy pollingStrategy;

    private PollCallback pollCallback;

    public PollHandler(long pollingInterval, PollCallback pollCallback, Looper looper) {
        this(new FixedTimePollingStrategy(pollingInterval), pollCallback, looper);
    }

    public PollHandler(PollingStrategy pollingStrategy, PollCallback pollCallback, Looper looper) {
        super(looper);
        this.pollingStrategy = pollingStrategy;
        this.pollCallback = pollCallback;
    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MESSAGE_START_POLLING:
                removeMessages(MESSAGE_POLL);
                sendMessageDelayed(getPollMessage(), pollingStrategy.getNextPollInterval());
                break;

            case MESSAGE_POLL:
                if (pollCallback != null) {
                    pollCallback.onPolled();

                    if (pollCallback.shouldPollNext()) {
                        sendMessageDelayed(getPollMessage(), pollingStrategy.getNextPollInterval());
                    } else {
                        if (pollCallback != null) pollCallback.onPolledCompleted();
                    }
                }
                break;

            case MESSAGE_STOP_POLLING:
                if(pollCallback != null){
                    pollCallback.stopPolling();
                }
                break;
        }
    }

    public static Message getMessageToStartPolling(boolean resetIfAlreadyStarted) {
        Message message = new Message();
        message.what = MESSAGE_START_POLLING;
        message.arg1 = resetIfAlreadyStarted ? 1 : 0;
        return message;
    }

    private Message getPollMessage() {
        Message message = new Message();
        message.what = MESSAGE_POLL;
        return message;
    }

    public static Message getMessageToStop() {
        Message message = new Message();
        message.what = MESSAGE_STOP_POLLING;
        return message;
    }

    public void removeCallback() {
        this.pollCallback = null;
    }

    public interface PollCallback {
        void onPolled();

        void onPolledCompleted();

        boolean shouldPollNext();

        void
        stopPolling();
    }

    public long getPollingInterval() {
        return pollingStrategy.currentPollInterval();
    }

}
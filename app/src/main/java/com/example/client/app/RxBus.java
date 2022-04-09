package com.example.client.app;

import com.example.client.models.event.Event;

import io.reactivex.subjects.PublishSubject;


public class RxBus {

    private static PublishSubject<Event> mInstance;

    public static PublishSubject<Event> getInstance() {

        if (mInstance == null) {
            mInstance = PublishSubject.create();
        }
        return mInstance;
    }
}

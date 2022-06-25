package com.example.client.app

import com.example.client.models.event.Event
import io.reactivex.subjects.PublishSubject

object RxBus {
    private var mInstance: PublishSubject<Event>? = null
    fun newInstance(): PublishSubject<Event> {
        if (mInstance == null) {
            mInstance = PublishSubject.create()
        }
        return mInstance!!
    }
}
package com.example.client.models.event

open class Event(var key: String)

class ValueEvent<T>(keyValue: String, var value: T): Event(keyValue)
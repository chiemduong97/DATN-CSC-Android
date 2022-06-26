package com.example.client.app

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class Firebase private constructor() {
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    fun ref(): StorageReference {
        val uuid = UUID.randomUUID().toString() + ".jpg"
        return storage.reference.child("images/$uuid")
    }

    companion object {
        var firebase: Firebase? = null
        fun newInstance(): Firebase {
            if (firebase == null) {
                firebase = Firebase()
            }
            return firebase!!
        }
    }

}
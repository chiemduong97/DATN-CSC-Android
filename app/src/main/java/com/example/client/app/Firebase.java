package com.example.client.app;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class Firebase {
    public static Firebase firebase;
    private FirebaseStorage storage;
    private Firebase(){
        storage = FirebaseStorage.getInstance();
    }

    public static Firebase getInstance(){
        if(firebase == null){
            throw new NullPointerException("Firebase is null!");
        }
        return firebase;
    }

    public static void createInstance(){
        if(firebase == null){
            firebase = new Firebase();
        }
    }

    public StorageReference ref(){
        String uuid =  UUID.randomUUID().toString() + ".jpg";
        StorageReference reference = storage.getReference().child("images/"+ uuid);
        return reference;
    }

}

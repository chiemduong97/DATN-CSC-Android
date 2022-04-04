package com.example.client.screens.subject.present;

public interface ISubjectPresent {
    void onShowSubject(int id);
    void onShowSubjectsByCategory(int category);
    void onShowMoreSubjects(int id, String method);
    void onRegisterSubject(int user,int subject,Double amount);
    void onShowSubjectsByUser(int user);
    void enableRegister(int user,int subject);
}

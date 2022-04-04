package com.example.client.screens.subject.activity;

import com.example.client.models.message.MessageModel;
import com.example.client.models.subject.SubjectModel;

import java.util.List;

public interface ISubjectView {
    void showSubject(SubjectModel item);
    void showSubjectsByCategory(List<SubjectModel> items);
    void showMoreSubjects(List<SubjectModel> items);
    void showSubjectByUser(List<SubjectModel> items);
    void registerSubject(MessageModel message);
    void checkEnableRegister(MessageModel message);
}

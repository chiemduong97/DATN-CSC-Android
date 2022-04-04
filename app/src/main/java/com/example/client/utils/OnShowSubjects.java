package com.example.client.utils;

import com.example.client.models.subject.SubjectModel;

import java.util.List;

public interface OnShowSubjects {
    List<SubjectModel> showSubjectsByCategory(int category);
}

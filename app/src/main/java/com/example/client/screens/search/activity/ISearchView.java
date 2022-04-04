package com.example.client.screens.search.activity;

import com.example.client.models.subject.SubjectModel;

import java.util.List;

public interface ISearchView {
    void onSearchSubjects(List<SubjectModel> items);
}

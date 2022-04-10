package com.example.client.screens.search.present;

import com.example.client.screens.search.activity.ISearchView;


public class SearchPresent implements ISearchPresent {
    private ISearchView sView;
    public SearchPresent(ISearchView sView){
        this.sView = sView;
    }
    @Override
    public void searchSubjects(String query) {

    }
}

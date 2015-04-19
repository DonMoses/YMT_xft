package com.ymt.demo1.dbTables;

import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/4/18
 */

public class SearchString extends DataSupport {
    private int id;
    private String searchedString;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearchedString() {
        return searchedString;
    }

    public void setSearchedString(String searchedString) {
        this.searchedString = searchedString;
    }
}
package com.sjsu.respiratoryhelper.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseModel {

    @SerializedName("result")
    public List<ResultItem> resultItems = new ArrayList<>();

    public List<ResultItem> getResultItems() {
        return resultItems;
    }
}

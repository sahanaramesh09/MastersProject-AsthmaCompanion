package com.sjsu.respiratoryhelper.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultItem {

    @SerializedName("Temperature")
    @Expose
    private Integer temperature;
    @SerializedName("Humidity")
    @Expose
    private Integer humidity;
    @SerializedName("AsthmaRisk")
    @Expose
    private String asthmaRisk;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("CO")
    @Expose
    private Integer cO;
    @SerializedName("NO2")
    @Expose
    private Integer nO2;
    @SerializedName("O3")
    @Expose
    private Integer o3;

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public String getAsthmaRisk() {
        return asthmaRisk;
    }

    public void setAsthmaRisk(String asthmaRisk) {
        this.asthmaRisk = asthmaRisk;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCO() {
        return cO;
    }

    public void setCO(Integer cO) {
        this.cO = cO;
    }

    public Integer getNO2() {
        return nO2;
    }

    public void setNO2(Integer nO2) {
        this.nO2 = nO2;
    }

    public Integer getO3() {
        return o3;
    }

    public void setO3(Integer o3) {
        this.o3 = o3;
    }

}
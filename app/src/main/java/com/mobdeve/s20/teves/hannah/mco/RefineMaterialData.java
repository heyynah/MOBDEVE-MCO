package com.mobdeve.s20.teves.hannah.mco;

public class RefineMaterialData {
    private String materialName;
    private int materialAmount;

    public RefineMaterialData(String materialName, int materialAmount) {
        this.materialName = materialName;
        this.materialAmount = materialAmount;
    }

    public String getMaterialName() {
        return materialName;
    }

    public int getMaterialAmount() {
        return materialAmount;
    }
}


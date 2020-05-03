package com.jd.enums;

import lombok.Getter;

public enum GatherTypeEnum {
    SKU(1,"sku","Dim Sku"),
    PRODUCT(2,"product","Dim Product");
    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private String instruction;

    GatherTypeEnum(int id, String name, String instruction){
        this.id = id;
        this.name = name;
        this.instruction = instruction;
    }

    public static GatherTypeEnum valueOf (int id){
        for (GatherTypeEnum gatherTypeEnum: GatherTypeEnum.values()) {
            if (gatherTypeEnum.getId() == id) {
                return gatherTypeEnum;
            }
        }
        return null;
    }
}
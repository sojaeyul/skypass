package com.koreanair.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class SalesKey {
    private String yyyymm;
    private String storeId;

    public SalesKey(Sales salesDto) {
        this.yyyymm = salesDto.getYyyymm();
        //this.storeId = salesDto.getStoreId();
    }

}

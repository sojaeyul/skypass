package com.koreanair.dto;

import java.util.HashMap;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class ServiceMDEEntriesGroupKey {
	private String membershipid;
	private String membershipresourceid;
	private String resource;
	private String operation;
	private String action;
	private String createdat;
	
    public ServiceMDEEntriesGroupKey(HashMap<String, Object> map) {
        this.membershipid = (String)map.get("membershipid");
        this.action = (String)map.get("action");
    }
}

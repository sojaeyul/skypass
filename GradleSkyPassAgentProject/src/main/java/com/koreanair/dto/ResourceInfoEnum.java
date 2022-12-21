package com.koreanair.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceInfoEnum {

	MEMBERSHIP("com.koreanair.biz.MembershipBiz","MEMBERSHIP","멤버십"),
	SUBSCRIPTION("SUBSCRIPTION","SUBSCRIPTION","신청"),
	RELATIONS("RELATIONS","RELATIONS","처지"),
	CARDS("CARDS","CARDS","카드"),
	TIERS("TIERS","TIERS","계층"),
	ACTIVITY("com.koreanair.biz.ActivityBiz","ACTIVITIES","활동"),
	ACTIVITYDETAILRESOURCE("ACTIVITYDETAILRESOURCE","ACTIVITYDETAILRESOURCE","활동 세부 리소스"),
	BALANCE("BALANCE","BALANCE","균형"),
	RETROCLAIMS("RETROCLAIMS","RETROCLAIMS","소급 청구"),
	NOTES("NOTES","NOTES","노트"),
	REGISTRATIONS("REGISTRATIONS","REGISTRATIONS","등록"),
	VOUCHERS("VOUCHERS","VOUCHERS","바우처"),
	PARTNERPROFILE("PARTNERPROFILE","PARTNERPROFILE","파트너 프로필"),
	ACCRUALS("ACCRUALS","ACCRUALS","발생"),
	TERMSANDCONDITIONS("TERMSANDCONDITIONS","TERMSANDCONDITIONS","이용약관")	
	;
	
	private String bizClass;
	private String spec;
	private String description;
	
}

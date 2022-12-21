package com.koreanair.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceInfoEnum {

	MEMBERSHIP("com.koreanair.biz.MembershipBiz","멤버십"),
	SUBSCRIPTION("SUBSCRIPTION","신청"),
	RELATIONS("RELATIONS","처지"),
	CARDS("CARDS","카드"),
	TIERS("TIERS","계층"),
	ACTIVITIES("ACTIVITIES","활동"),
	ACTIVITYDETAILRESOURCE("ACTIVITYDETAILRESOURCE","활동 세부 리소스"),
	BALANCE("BALANCE","균형"),
	RETROCLAIMS("RETROCLAIMS","소급 청구"),
	NOTES("NOTES","노트"),
	REGISTRATIONS("REGISTRATIONS","등록"),
	VOUCHERS("VOUCHERS","바우처"),
	PARTNERPROFILE("PARTNERPROFILE","파트너 프로필"),
	ACCRUALS("ACCRUALS","발생"),
	TERMSANDCONDITIONS("TERMSANDCONDITIONS","이용약관")	
	;
	
	private String bizClass;
	private String description;
	
}

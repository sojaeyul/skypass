package com.koreanair.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceInfoEnum {

	MEMBERSHIP("com.koreanair.biz.MembershipBiz","MEMBERSHIP","membership","A membership represents a contract between an entity (individual, organization) and a loyalty program, which allows the entity to benefit from this program."),
	ACTIVITY("com.koreanair.biz.ActivityBiz","ACTIVITIES","activities","Related membership Activity subresources included in massive export"),//멤버십 활동 관련 하위 리소스
	SUBSCRIPTION("com.koreanair.biz.SubscriptionBiz","SUBSCRIPTION","subscriptions","Related membership Subscription subresources included in massive export"),//멤버십 신청 관련 하위 리소스
	
	RELATIONS("RELATIONS","RELATIONS","","Related membership Relation resources included in massive export"),//멥버십 관계 관련 리소스
	CARDS("CARDS","CARDS","","Related membership Card subresources included in massive export"),//멤버십 카드 관련 하위 리소스
	TIERS("TIERS","TIERS","","Related membership Tier subresources included in massive export"),//멤버십 등급 관련 하위 리소스
	ACTIVITYDETAILRESOURCE("ACTIVITYDETAILRESOURCE","ACTIVITYDETAILRESOURCE","","활동 세부 리소스"),
	BALANCE("BALANCE","BALANCE","","Current balance of the membership"),//멤버십의 현재 잔액
	RETROCLAIMS("RETROCLAIMS","RETROCLAIMS","","Related retro resources included in massive export"),//Related retro 리소스
	NOTES("NOTES","NOTES","","Agent notes resources included in massive export"),//상담원 메모 리소스
	REGISTRATIONS("REGISTRATIONS","REGISTRATIONS","","Promotion registrations resources inclueded in massive export"),//프로모션 등록 리소스
	VOUCHERS("VOUCHERS","VOUCHERS","","Voucher resources included in massive export"),//바우처 리소스
	PARTNERPROFILE("PARTNERPROFILE","PARTNERPROFILE","","PartnerProfile information"),//PartnerProfile 정보
	ACCRUALS("ACCRUALS","ACCRUALS","","Accrual resources included in massive export"),//적립금 자원(ACR)
	TERMSANDCONDITIONS("TERMSANDCONDITIONS","TERMSANDCONDITIONS","","Terms and conditions resources included in massive export")//이용약관 리소스
	
	//BILLING("com.koreanair.biz.BillingsBiz","BILLINGS","billings","Billings of the member"),//회원 청구서	
	;
	
	private String bizClass;
	private String spec;
	private String nodeRoot;
	private String description;
	
}

package com.koreanair.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.koreanair.dto.Sales;
import com.koreanair.dto.SalesKey;

public class TestGroupBy {

	public static void main(String[] args) throws Exception  {
		List<Sales> dtos = new ArrayList<>();
		//for(int i=0; i<500000; i++) {
	        Sales dto1 = new Sales("2020-11", "1", 6233800l);
	        Sales dto2 = new Sales("2020-11", "1", 725700l);
	        Sales dto3 = new Sales("2020-11", "2", 285300l);
	        Sales dto4 = new Sales("2020-11", "2", 437800l);
	        Sales dto5 = new Sales("2020-12", "1", 12991300l);
	        Sales dto6 = new Sales("2020-12", "1", 2650500l);
	        Sales dto7 = new Sales("2020-12", "2", 108400l);
	        Sales dto8 = new Sales("2020-12", "2", 445600l);
	        Sales dto9 = new Sales("2020-12", "1", 1314100l);
	        Sales dto10 = new Sales("2021-01", "1", 5660700l);
	        Sales dto11 = new Sales("2021-01", "3", 904300l);
	        Sales dto12 = new Sales("2021-01", "1", 8000l);
	        Sales dto13 = new Sales("2021-01", "1", 221500l);
	        Sales dto14 = new Sales("2021-01", "1", 391700l);
	        dtos.add(dto1);
	        dtos.add(dto2);
	        dtos.add(dto3);
	        dtos.add(dto4);
	        dtos.add(dto5);
	        dtos.add(dto6);
	        dtos.add(dto7);
	        dtos.add(dto8);
	        dtos.add(dto9);
	        dtos.add(dto10);
	        dtos.add(dto11);
	        dtos.add(dto12);
	        dtos.add(dto13);
	        dtos.add(dto14);
		//}

		System.out.println("total size " + dtos.size());
		
        Map<SalesKey, List<Sales>> collect = dtos.stream()
                .collect(Collectors.groupingBy(SalesKey::new));//Method Reference
        
        /*
        for (Entry<SalesKey, List<Sales>> entrySet : collect.entrySet()) {
            System.out.println(entrySet.getKey() + " : " + entrySet.getValue());
        }
        */
        // then
        int sum = 0;
        System.out.println("=======================================");
        for (SalesKey salesKey : collect.keySet()) {
        	List<Sales> list = collect.get(salesKey);
        	
            //System.out.println(salesKey.hashCode() + " :: key:value = " + salesKey + ":" + list);
        	//System.out.println("key:value = " + salesKey + " :: size = "+ list);
        	        	
            Comparator<Sales> storeIdASC = Comparator.comparing(Sales::getStoreId);//내림차순
            Comparator<Sales> saleDESC = Comparator.comparing(Sales::getSales)
            											.reversed();//오름차순
        	list.sort(Comparator.comparing(Sales::getYyyymm)
        							.thenComparing(storeIdASC)
        							.thenComparing(saleDESC));
        	        
        	
        	
        	//list.forEach(System.out::println);
        	list.forEach(a -> System.out.println("     "+a));
        	
        	sum += collect.get(salesKey).size();
							           
        }
        System.out.println("=======================================");

        System.out.println("sum size " + sum);
        
	}
}

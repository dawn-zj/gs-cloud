package com.gs.common.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ListUtil {
	/**
	 * list去重复
	 * @param <T>
	 * @return 
	 * */
	private <T> List<T> distinctList(List<T> list) {
		Set<T> set = new TreeSet<>(new Comparator<T>() {
			@Override
			public int compare(T obj1, T obj2) {
				try {
					Class<?> objClass1 =  obj1.getClass();
					Method[] methods = objClass1.getMethods();
					String idMethodName = "";
					for(Method method : methods) {
						if(method.getName().equalsIgnoreCase("getId")) {
							idMethodName = method.getName();
						}
					}
					
					Method idMethod1 = objClass1.getMethod(idMethodName, null);
					Long id1 = (Long)idMethod1.invoke(obj1, null);
					
					Class<?> objClass2 =  obj2.getClass();
					Method idMethod2 = objClass2.getMethod(idMethodName, null);
					Long id2 = (Long)idMethod2.invoke(obj2, null);
					
					return id1.compareTo(id2);
				} catch (Exception e) {
					//LoggerUtil.errorlog("list去重失败",e);
				}
				return 0;
			}
		});
		set.addAll(list);
		return new ArrayList<>(set);
	}
}

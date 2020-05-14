package com.jzkj.gyxg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtil {
	private static final Logger logger = LoggerFactory.getLogger("errorLogger");
	
	public static Map<String, Object> convertToMap(Serializable bean) {
		return convertToMap(bean, null, null);
	}
	
	public static Map<String, Object> convertToMap(Serializable bean, String excludes) {
		return convertToMap(bean, null, excludes);
	}
	
	public static Map<String, Object> convertToMap(Serializable bean, PropertyDescriptor[] beanProperties) {
		return convertToMap(bean, beanProperties, null);
	}
	
	public static Map<String, Object> convertToMap(Serializable bean, PropertyDescriptor[] beanProperties, String excludes) {
		Map<String, Object> map = new HashMap<>();
		String[] excludeFields;
		
		if (bean == null) {
			return map;
		}
		
		if (beanProperties == null || beanProperties.length == 0) {
			BeanInfo beanInfo;
			
			try {
				beanInfo = Introspector.getBeanInfo(bean.getClass());
				beanProperties = beanInfo.getPropertyDescriptors();
			} catch (Exception e) {
				ExceptionUtil.logStackTrace(logger, e);
				return map;
			}
		}
		
		if (beanProperties == null || beanProperties.length == 0) {
			return map;
		}
		
		if (StringUtil.isEmpty(excludes)) {
			excludeFields = new String[0];
		}
		else {
			excludeFields = excludes.split("[\\x20\\t]*,[\\x20\\t]*");
		}
		
		for (PropertyDescriptor property : beanProperties) {
			String propertyName = property.getName();
			Method getter = property.getReadMethod();
			
			if (StringUtil.isEmpty(propertyName) || ArrayUtil.inArray(propertyName, excludeFields) || getter == null || !getter.isAccessible()) {
				continue;
			}
			
			try {
				map.put(propertyName, getter.invoke(bean));
			} catch (Exception e) {
				map.remove(propertyName);
			}
		}
		
		return map;
	}
	
	public static List<Map<String, Object>> convertToMap(List<Serializable> beans) {
		return convertToMap(beans, null);
	}
	
	public static List<Map<String, Object>> convertToMap(List<Serializable> beans, String excludes) {
		if (beans == null || beans.isEmpty()) {
			return new ArrayList<Map<String, Object>>();
		}
		
		List<Map<String, Object>> list = new ArrayList<>();
		PropertyDescriptor[] beanProperties = getBeanProperties(beans.get(0));
		
		for (Serializable bean : beans) {
			list.add(convertToMap(bean, beanProperties, excludes));
		}
		
		return list;
	}
	
	private static PropertyDescriptor[] getBeanProperties(Serializable bean) {
		try {
			return Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
		} catch (Exception e) {
			ExceptionUtil.logStackTrace(logger, e);
			return new PropertyDescriptor[0];
		}
	}
}
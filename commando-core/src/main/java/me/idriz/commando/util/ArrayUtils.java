package me.idriz.commando.util;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {
	
	public static <T> List<T> copyArrayIntoList(T[] array) {
		List<T> list = new ArrayList<>(array.length);
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
}

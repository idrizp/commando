package me.idriz.commando.util;

import java.util.HashMap;
import java.util.Map;

public class PrimitiveUtils {
	
	public static final Map<Class<?>, Class<?>> BOXED_TO_PRIMITIVE = new HashMap<>();
	public static final Map<Class<?>, Class<?>> PRIMITIVE_TO_BOXED = new HashMap<>();
	
	static {
		registerType(Boolean.class, boolean.class);
		registerType(Integer.class, int.class);
		registerType(Float.class, float.class);
		registerType(Long.class, long.class);
		registerType(Double.class, double.class);
		registerType(Short.class, short.class);
		registerType(Character.class, char.class);
	}
	
	private static void registerType(Class<?> boxed, Class<?> primitive) {
		BOXED_TO_PRIMITIVE.put(boxed, primitive);
		PRIMITIVE_TO_BOXED.put(primitive, boxed);
	}
	
	public static boolean areEqualPrimitives(Class<?> nonPrimitiveType, Class<?> primitiveType) {
		return nonPrimitiveType == PRIMITIVE_TO_BOXED.get(primitiveType) && primitiveType == BOXED_TO_PRIMITIVE
				.get(nonPrimitiveType);
	}
	
}

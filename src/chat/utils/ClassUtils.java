package chat.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ClassUtils {
	
	/**
	 * @return 类声明的所有的属性
	 */
	public static List<Field> getAllDeclaredFields(Class<?> clazz) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		classes.add(clazz);
		while (clazz.getSuperclass() != Object.class) {
			clazz = clazz.getSuperclass();
			classes.add(clazz);	
		}
		List<Field> fields = new ArrayList<Field>();
		for (int i = classes.size() - 1; i >= 0; i--) {
			clazz = classes.get(i);
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		}
		return fields;
	}

}

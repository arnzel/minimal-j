package ch.openech.mj.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.joda.time.Partial;
import org.joda.time.ReadablePartial;

import ch.openech.mj.edit.value.Properties;

public class Keys {

	private static final Logger logger = Logger.getLogger(Keys.class.getName());
	private static final Map<Object, PropertyInterface> properties = new IdentityHashMap<Object, PropertyInterface>();

	private static final List<Object> keyObjects = new ArrayList<>();
	private static final Map<String, Object> methodKeyByName = new HashMap<String, Object>();
	
	
	/**
	 * Warning: Should only be call once per class
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T of (Class<T> clazz) {
		T object;
		try {
			object = clazz.newInstance();
			keyObjects.add(object);
			fillFields(object, null, 0);
			return object;
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static boolean isKeyObject(Object object) {
		return keyObjects.contains(object) || properties.containsKey(object);
	}

	@SuppressWarnings("unchecked")
	public static <T> T methodOf(Object keyObject, String methodName, Class<T> returnType) {
		String qualifiedMethodName = null;
		
		if (keyObjects.contains(keyObject)) {
			qualifiedMethodName = keyObject.getClass().getName() + "." + methodName;
		} else {
			PropertyInterface property = properties.get(keyObject);
			qualifiedMethodName = property.getFieldPath() + "." + methodName;
		}
		
		if (methodKeyByName.containsKey(qualifiedMethodName)) {
			return (T) methodKeyByName.get(qualifiedMethodName);
		}
		T t = (T)createKey(returnType, methodName, null);
		methodKeyByName.put(qualifiedMethodName, t);
		
		PropertyInterface property = Properties.getMethodProperty(keyObject.getClass(), methodName);
		if (!keyObjects.contains(keyObject)) {
			property = new ChainedProperty(properties.get(keyObject), property);
		}
		properties.put(t, property);
		
		return t;
	}
	
	private static <T> void fillFields(T object, PropertyInterface enclosingProperty, int depth) throws IllegalAccessException, InstantiationException {
		for (Map.Entry<String, PropertyInterface> entry : Properties.getProperties(object.getClass()).entrySet()) {
			PropertyInterface property = entry.getValue();
			
			Object value = null;
			Class<?> type = property.getFieldClazz();

			if (property.isFinal()) {
				value = property.getValue(object);
			} else {
				value = createKey(type, property.getFieldName(), property.getDeclaringClass());
				property.setValue(object, value);	
			}
			
			if (enclosingProperty != null) {
				property = new ChainedProperty(enclosingProperty, property);
			}

			boolean fill = !type.getName().startsWith("java") && !type.getName().startsWith("org.joda");
			if (fill && depth < 6) {
				fillFields(value, property, depth + 1);
			}
			
			properties.put(value, property);
		}
	}
	
	private static Object createKey(Class<?> type, String fieldName, Class<?> declaringClass) {
		if (type == String.class) {
			return new String(fieldName);
		} else if (type == Integer.class) {
			return new Integer(0);
		} else if (Enum.class.isAssignableFrom(type)) {
			Class<Enum> enumClass = (Class<Enum>) type;
			return EnumUtils.createEnum(enumClass, fieldName);
		} else if (type == Boolean.class) {
			return new Boolean(false);
		} else if (type == BigDecimal.class) {
			return new BigDecimal(0);
		} else if (type == ReadablePartial.class) {
			return new Partial();
		} else {
			// note: LocalDate, LocaleDateTime etc have an empty constructor
			// so they are constructed in the else branch
			try {
				return type.newInstance();
			} catch (Exception x) {
				if (declaringClass != null) {
					logger.severe("Could not instantiat " + fieldName + " in class " + declaringClass);
				} else {
					logger.severe("Could not instantiat " + fieldName);				
				}
				return null;
			}
		}
	}
	
	public static PropertyInterface getProperty(Object key) {
		return properties.get(key);
	}
	
	public static boolean isFieldProperty(PropertyInterface property) {
		if (property instanceof Properties.MethodProperty) return false;
		if (property instanceof ChainedProperty) {
			ChainedProperty chainedProperty = (ChainedProperty) property;
			return isFieldProperty(chainedProperty.property1) && isFieldProperty(chainedProperty.property2);
		}
		return true;
	}
	
	static class ChainedProperty implements PropertyInterface {
		private final PropertyInterface property1;
		private final PropertyInterface property2;

		public ChainedProperty(PropertyInterface property1, PropertyInterface property2) {
			this.property1 = property1;
			this.property2 = property2;
		}
		
		@Override
		public Class<?> getDeclaringClass() {
			return property2.getDeclaringClass();
		}

		@Override
		public Object getValue(Object object) {
			Object value1 = property1.getValue(object);
			Object value2 = property2.getValue(value1);
			return value2;
		}

		@Override
		public void setValue(Object object, Object value) {
			Object value1 = property1.getValue(object);
			property2.setValue(value1, value);
		}

		@Override
		public String getFieldName() {
			return property2.getFieldName();
		}
		
		@Override
		public String getFieldPath() {
			return property1.getFieldPath() + "." + property2.getFieldPath();
		}
		
		@Override
		public Type getType() {
			return property2.getType();
		}
		
		@Override
		public Class<?> getFieldClazz() {
			return property2.getFieldClazz();
		}

		@Override
		public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
			return property2.getAnnotation(annotationClass);
		}

		@Override
		public boolean isFinal() {
			return property2.isFinal();
		}
	}

	public static PropertyInterface[] getProperties(Object[] keys) {
		PropertyInterface[] properties = new PropertyInterface[keys.length];
		for (int i = 0; i<keys.length; i++) {
			properties[i] = getProperty(keys[i]);
		}
		return properties;
	}

}
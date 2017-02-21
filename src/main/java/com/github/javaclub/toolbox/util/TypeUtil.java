/*
 * @(#)TypeUtil.java	2009-12-14
 *
 * Copyright (c) 2009 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.github.javaclub.toolbox.core.JRuntimeException;

import org.springframework.util.Assert;

/**
 * Utility to work with Java 5 generic type parameters.
 * Mainly for internal use within the framework.
 *
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: TypeUtil.java 392 2011-09-16 14:28:56Z gerald.chen.hz $
 */
public abstract class TypeUtil {
	
	private static Map cache = new HashMap();

	/**
	 * Check if the right-hand side type may be assigned to the left-hand side
	 * type following the Java generics rules.
	 * @param lhsType the target type
	 * @param rhsType	the value type that should be assigned to the target type
	 * @return true if rhs is assignable to lhs
	 */
	public static boolean isAssignable(Type lhsType, Type rhsType) {
		Assert.notNull(lhsType, "Left-hand side type must not be null");
		Assert.notNull(rhsType, "Right-hand side type must not be null");
		if (lhsType.equals(rhsType)) {
			return true;
		}
		if (lhsType instanceof Class && rhsType instanceof Class) {
			return TypeUtil.isAssignable((Class) lhsType, (Class) rhsType);
		}
		if (lhsType instanceof ParameterizedType && rhsType instanceof ParameterizedType) {
			return isAssignable((ParameterizedType) lhsType, (ParameterizedType) rhsType);
		}
		if (lhsType instanceof WildcardType) {
			return isAssignable((WildcardType) lhsType, rhsType);
		}
		return false;
	}
	
	/**
     * <p>Checks if one <code>Class</code> can be assigned to a variable of
     * another <code>Class</code>.</p>
     *
     * <p>Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method,
     * this method takes into account widenings of primitive classes and
     * <code>null</code>s.</p>
     *
     * <p>Primitive widenings allow an int to be assigned to a long, float or
     * double. This method returns the correct result for these cases.</p>
     *
     * <p><code>Null</code> may be assigned to any reference type. This method
     * will return <code>true</code> if <code>null</code> is passed in and the
     * toClass is non-primitive.</p>
     *
     * <p>Specifically, this method tests whether the type represented by the
     * specified <code>Class</code> parameter can be converted to the type
     * represented by this <code>Class</code> object via an identity conversion
     * widening primitive or widening reference conversion. See
     * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>,
     * sections 5.1.1, 5.1.2 and 5.1.4 for details.</p>
     *
     * @param cls  the Class to check, may be null
     * @param toClass  the Class to try to assign into, returns false if null
     * @return <code>true</code> if assignment possible
     */
    public static boolean isAssignable(Class cls, Class toClass) {
        if (toClass == null) {
            return false;
        }
        // have to check for null, as isAssignableFrom doesn't
        if (cls == null) {
            return !(toClass.isPrimitive());
        }
        if (cls.equals(toClass)) {
            return true;
        }
        if (cls.isPrimitive()) {
            if (toClass.isPrimitive() == false) {
                return false;
            }
            if (Integer.TYPE.equals(cls)) {
                return Long.TYPE.equals(toClass)
                    || Float.TYPE.equals(toClass)
                    || Double.TYPE.equals(toClass);
            }
            if (Long.TYPE.equals(cls)) {
                return Float.TYPE.equals(toClass)
                    || Double.TYPE.equals(toClass);
            }
            if (Boolean.TYPE.equals(cls)) {
                return false;
            }
            if (Double.TYPE.equals(cls)) {
                return false;
            }
            if (Float.TYPE.equals(cls)) {
                return Double.TYPE.equals(toClass);
            }
            if (Character.TYPE.equals(cls)) {
                return Integer.TYPE.equals(toClass)
                    || Long.TYPE.equals(toClass)
                    || Float.TYPE.equals(toClass)
                    || Double.TYPE.equals(toClass);
            }
            if (Short.TYPE.equals(cls)) {
                return Integer.TYPE.equals(toClass)
                    || Long.TYPE.equals(toClass)
                    || Float.TYPE.equals(toClass)
                    || Double.TYPE.equals(toClass);
            }
            if (Byte.TYPE.equals(cls)) {
                return Short.TYPE.equals(toClass)
                    || Integer.TYPE.equals(toClass)
                    || Long.TYPE.equals(toClass)
                    || Float.TYPE.equals(toClass)
                    || Double.TYPE.equals(toClass);
            }
            // should never get here
            return false;
        }
        return toClass.isAssignableFrom(cls);
    }

	private static boolean isAssignable(ParameterizedType lhsType, ParameterizedType rhsType) {
		if (lhsType.equals(rhsType)) {
			return true;
		}
		Type[] lhsTypeArguments = lhsType.getActualTypeArguments();
		Type[] rhsTypeArguments = rhsType.getActualTypeArguments();
		if (lhsTypeArguments.length != rhsTypeArguments.length) {
			return false;
		}
		for (int size = lhsTypeArguments.length, i = 0; i < size; ++i) {
			Type lhsArg = lhsTypeArguments[i];
			Type rhsArg = rhsTypeArguments[i];
			if (!lhsArg.equals(rhsArg) &&
					!(lhsArg instanceof WildcardType && isAssignable((WildcardType) lhsArg, rhsArg))) {
				return false;
			}
		}
		return true;
	}

	private static boolean isAssignable(WildcardType lhsType, Type rhsType) {
		Type[] upperBounds = lhsType.getUpperBounds();
		Type[] lowerBounds = lhsType.getLowerBounds();
		for (int size = upperBounds.length, i = 0; i < size; ++i) {
			if (!isAssignable(upperBounds[i], rhsType)) {
				return false;
			}
		}
		for (int size = lowerBounds.length, i = 0; i < size; ++i) {
			if (!isAssignable(rhsType, lowerBounds[i])) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns true if the provided class is a type supported natively (as
	 * opposed to a bean).
	 * 
	 * @param type {@link java.lang.Class} type to be tested
	 */
	public static boolean isNativeType(final Class type) {

		// to return an arbitrary object use Object.class
		return (type == boolean.class || type == Boolean.class || type == byte.class || type == Byte.class
				|| type == short.class || type == Short.class || type == int.class || type == Integer.class
				|| type == long.class || type == Long.class || type == float.class || type == Float.class
				|| type == double.class || type == Double.class || type == char.class || type == Character.class
				|| type == byte[].class || type == Byte[].class || type == char[].class || type == Character[].class
				|| type == String.class || type == BigDecimal.class || type == java.util.Date.class
				|| type == java.sql.Date.class || type == java.sql.Time.class || type == java.sql.Timestamp.class
				|| type == java.io.InputStream.class || type == java.io.Reader.class || type == java.sql.Clob.class
				|| type == java.sql.Blob.class || type == Object.class);
	}
	
	/**
	 * 把value转换成clazz类型的对象
	 *
	 * @param clazz clazz类型
	 * @param value 需要被转换的值
	 * @return 转换后的对象
	 */
	public static Object convert(Class clazz, Object value) {
		Object result = null;
		String name = null;
		try {
			name = clazz.getName();
			// System.out.println(name);
		} catch (Exception e) {
			throw new JRuntimeException("不太可能出现的异常", e);
		}
		
		String stringVal = value.toString();
		if(name.equals("java.lang.Byte") || name.equals("byte")) {
			result = Byte.valueOf(stringVal);
		} else if(name.equals("java.lang.Short") || name.equals("short")) {
			result = Short.valueOf(stringVal);
		} else if(name.equals("java.lang.Integer") || name.equals("int")) {
			result = Integer.valueOf(stringVal);
		} else if(name.equals("java.lang.Boolean") || name.equals("boolean")) {
			result = Boolean.valueOf(stringVal);
		} else if(name.equals("java.lang.Character") || name.equals("char")) {
			result = new Character(stringVal.charAt(0));
		} else if(name.equals("java.lang.Double") || name.equals("double")) {
			result = Double.valueOf(stringVal);
		} else if(name.equals("java.lang.Float") || name.equals("float")) {
			result = Float.valueOf(stringVal);
		} else if(name.equals("java.lang.Long") || name.equals("long")) {
			result = Long.valueOf(stringVal);
		} else if(name.equals("java.lang.String")) {
			result = String.valueOf(stringVal);
		} else if (name.equals("java.util.Date")) {
			result = DateUtil.toDate(value);
		} else if (name.equals("java.sql.Date")) {
			result = DateUtil.toSqlDate(value);
		} else if (name.equals("java.sql.Timestamp")) {
			result = DateUtil.toTimestamp(value);
		} else if (name.equals("java.sql.Time")) {
			result = DateUtil.toTime(value);
		} else if(name.equals("java.math.BigDecimal")) {
			result = new BigDecimal(stringVal);
		}
		return result;
	}
	
	/**
	 * 将一个给定的值转换为某个POJO的一个field的对应数据类型对象
	 *
	 * @param field
	 * @param value
	 * @return
	 */
	public static Object convert(Field field, Object value) {
		if(value == null) {
			return null;
		}
		Object result = null;
		String name = null;
		try {
			name = field.getType().getName();
			// System.out.println(name);
		} catch (Exception e) {
			throw new JRuntimeException("不太可能出现的异常", e);
		}
		
		String stringVal = value.toString();
		if(name.equals("java.lang.Byte") || name.equals("byte")) {
			result = Byte.valueOf(stringVal);
		} else if(name.equals("java.lang.Short") || name.equals("short")) {
			result = Short.valueOf(stringVal);
		} else if(name.equals("java.lang.Integer") || name.equals("int")) {
			result = Integer.valueOf(stringVal);
		} else if(name.equals("java.lang.Boolean") || name.equals("boolean")) {
			result = Boolean.valueOf(stringVal);
		} else if(name.equals("java.lang.Character") || name.equals("char")) {
			result = new Character(stringVal.charAt(0));
		} else if(name.equals("java.lang.Double") || name.equals("double")) {
			result = Double.valueOf(stringVal);
		} else if(name.equals("java.lang.Float") || name.equals("float")) {
			result = Float.valueOf(stringVal);
		} else if(name.equals("java.lang.Long") || name.equals("long")) {
			result = Long.valueOf(stringVal);
		} else if(name.equals("java.lang.String")) {
			result = String.valueOf(stringVal);
		} else if (name.equals("java.util.Date")) {
			result = DateUtil.toDate(value);
		} else if (name.equals("java.sql.Date")) {
			result = DateUtil.toSqlDate(value);
		} else if (name.equals("java.sql.Timestamp")) {
			result = DateUtil.toTimestamp(value);
		} else if (name.equals("java.sql.Time")) {
			result = DateUtil.toTime(value);
		}
		return result;
	}
	
	public static Class getType(String property, Object target) {
        Class ret = Object.class;
        property = "set" + property;

        Method[] methods = (Method[]) cache.get(target.getClass());
        if (methods == null) {
            methods = target.getClass().getMethods();
            cache.put(target.getClass(), methods);
        }
        for (int i = 0; i < methods.length; i++) {
            if (property.equalsIgnoreCase(methods[i].getName())) {
                Class[] paramClass = methods[i].getParameterTypes();
                if (paramClass.length == 1) {
                    return paramClass[0];
                }
            }
        }

        return ret;
    }
	
	/**
     * ResultSet.getObject() returns an Integer object for an INT column.  The
     * setter method for the property might take an Integer or a primitive int.
     * This method returns true if the value can be successfully passed into
     * the setter method.  Remember, Method.invoke() handles the unwrapping
     * of Integer into an int.
     * 
     * @param value The value to be passed into the setter method.
     * @param type The setter's parameter type.
     * @return boolean True if the value is compatible.
     */
	public static boolean isCompatibleType(Object value, Class type) {
        // Do object check first, then primitives
        if (value == null || type.isInstance(value)) {
            return true;

        } else if (
            type.equals(Integer.TYPE) && Integer.class.isInstance(value)) {
            return true;

        } else if (type.equals(Long.TYPE) && Long.class.isInstance(value)) {
            return true;

        } else if (
            type.equals(Double.TYPE) && Double.class.isInstance(value)) {
            return true;

        } else if (type.equals(Float.TYPE) && Float.class.isInstance(value)) {
            return true;

        } else if (type.equals(Short.TYPE) && Short.class.isInstance(value)) {
            return true;

        } else if (type.equals(Byte.TYPE) && Byte.class.isInstance(value)) {
            return true;

        } else if (
            type.equals(Character.TYPE) && Character.class.isInstance(value)) {
            return true;

        } else if (
            type.equals(Boolean.TYPE) && Boolean.class.isInstance(value)) {
            return true;

        } else {
            return false;
        }
    }
	
	public static Class getWrapperClass(Class primitive)
    {
        if (primitive == int.class)
            return java.lang.Integer.class;
        else if (primitive == short.class)
            return java.lang.Short.class;
        else if (primitive == boolean.class)
            return java.lang.Boolean.class;
        else if (primitive == byte.class)
            return java.lang.Byte.class;
        else if (primitive == long.class)
            return java.lang.Long.class;
        else if (primitive == double.class)
            return java.lang.Double.class;
        else if (primitive == float.class)
            return java.lang.Float.class;
        else if (primitive == char.class)
            return java.lang.Character.class;
        
        return null;
    }
    
    public static String getWrapper(String primitive)
    {
        if (primitive.equals("int"))
            return "Integer";
        else if (primitive.equals("short"))
            return "Short";
        else if (primitive.equals("boolean"))
            return "Boolean";
        else if (primitive.equals("byte"))
            return "Byte";
        else if (primitive.equals("long"))
            return "Long";
        else if (primitive.equals("double"))
            return "Double";
        else if (primitive.equals("float"))
            return "Float";
        else if (primitive.equals("char"))
            return "Character";
        
        return null;
    }

    public static Class getPrimitiveClass(Class wrapper)
    {
        if (wrapper == java.lang.Integer.class)
            return int.class;
        else if (wrapper == java.lang.Short.class)
            return short.class;
        else if (wrapper == java.lang.Boolean.class)
            return boolean.class;
        else if (wrapper == java.lang.Byte.class)
            return byte.class;
        else if (wrapper == java.lang.Long.class)
            return long.class;
        else if (wrapper == java.lang.Double.class)
            return double.class;
        else if (wrapper == java.lang.Float.class)
            return float.class;
        else if (wrapper == java.lang.Character.class)
            return char.class;
        
        return null;
    }
    
    public static Class getPrimitiveClassFromName(String primitive) {
        if (primitive.equals("int"))
            return int.class;
        else if (primitive.equals("short"))
            return short.class;
        else if (primitive.equals("boolean"))
            return boolean.class;
        else if (primitive.equals("byte"))
            return byte.class;
        else if (primitive.equals("long"))
            return long.class;
        else if (primitive.equals("double"))
            return double.class;
        else if (primitive.equals("float"))
            return float.class;
        else if (primitive.equals("char"))
            return char.class;
        
        return null;
    }
	
	public static void main(String[] args) {
		System.out.println(int.class.getName());
	}
}

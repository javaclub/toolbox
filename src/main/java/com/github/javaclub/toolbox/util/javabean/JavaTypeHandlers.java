/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.github.javaclub.toolbox.util.javabean;

import java.util.HashMap;
import java.util.Map;

import com.github.javaclub.toolbox.core.JRuntimeException;
import com.github.javaclub.toolbox.core.Strings;
import com.github.javaclub.toolbox.util.DateUtil;
import com.github.javaclub.toolbox.util.NumberUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Java data type handler.
 * 
 */
@SuppressWarnings("unchecked")
public class JavaTypeHandlers {

	private static transient final Log log = LogFactory.getLog(JavaTypeHandlers.class);

	public static final Map COMMON_DATA_TYPE_HANDLERS = new HashMap();

	static {
		try {
			COMMON_DATA_TYPE_HANDLERS.put("byte", ByteHandler.class
					.newInstance());
			COMMON_DATA_TYPE_HANDLERS.put(Byte.class.getName(),
					ByteHandler.class.newInstance());

			COMMON_DATA_TYPE_HANDLERS.put("short", ShortHandler.class
					.newInstance());
			COMMON_DATA_TYPE_HANDLERS.put(Short.class.getName(),
					ShortHandler.class.newInstance());

			COMMON_DATA_TYPE_HANDLERS.put("char", CharacterHandler.class
					.newInstance());
			COMMON_DATA_TYPE_HANDLERS.put(Character.class.getName(),
					CharacterHandler.class.newInstance());

			COMMON_DATA_TYPE_HANDLERS.put("int", IntegerHandler.class
					.newInstance());
			COMMON_DATA_TYPE_HANDLERS.put(Integer.class.getName(),
					IntegerHandler.class.newInstance());

			COMMON_DATA_TYPE_HANDLERS.put("boolean", BooleanHandler.class
					.newInstance());
			COMMON_DATA_TYPE_HANDLERS.put(Boolean.class.getName(),
					BooleanHandler.class.newInstance());

			COMMON_DATA_TYPE_HANDLERS.put("long", LongHandler.class
					.newInstance());
			COMMON_DATA_TYPE_HANDLERS.put(Long.class.getName(),
					LongHandler.class.newInstance());

			COMMON_DATA_TYPE_HANDLERS.put("float", FloatHandler.class
					.newInstance());
			COMMON_DATA_TYPE_HANDLERS.put(Float.class.getName(),
					FloatHandler.class.newInstance());

			COMMON_DATA_TYPE_HANDLERS.put("double", DoubleHandler.class
					.newInstance());
			COMMON_DATA_TYPE_HANDLERS.put(Double.class.getName(),
					DoubleHandler.class.newInstance());

			COMMON_DATA_TYPE_HANDLERS.put("java.util.Date", DateHandler.class
					.newInstance());
			COMMON_DATA_TYPE_HANDLERS.put("java.sql.Date", SqlDateHandler.class
					.newInstance());
			COMMON_DATA_TYPE_HANDLERS.put("java.sql.Timestamp",
					TimestampHandler.class.newInstance());
			COMMON_DATA_TYPE_HANDLERS.put("java.sql.Time", TimeHandler.class
					.newInstance());

			COMMON_DATA_TYPE_HANDLERS.put("java.math.BigInteger",
					BigIntegerHandler.class.newInstance());
			COMMON_DATA_TYPE_HANDLERS.put("java.math.BigDecimal",
					BigDecimalHandler.class.newInstance());

			COMMON_DATA_TYPE_HANDLERS.put(String.class.getName(),
					StringHandler.class.newInstance());

		} catch (InstantiationException e) {
			log.error("init IDataTypeHandler failed.", e);
		} catch (IllegalAccessException e) {
			log.error("init IDataTypeHandler failed.", e);
		}
	}

	public static IDataTypeHandler getUnsupportedDataHandler(Class fieldType) {
		return new UnsupportedDataHandler(fieldType);
	}

	/**
	 * 将字符串转换为指定的数据类型。
	 * 
	 * @param value
	 *            要转换的字符串
	 * @param className
	 *            要转换成的类型，如int, float等。
	 * 
	 */
	public static Object convertValueToType(String value, String className) {
		IDataTypeHandler mh = (IDataTypeHandler) COMMON_DATA_TYPE_HANDLERS
				.get(className);

		if (mh == null) {
			throw new JRuntimeException("unknown data type :" + className);
		}

		return mh.getValue(value);
	}

	public static void main(String[] args) {
		System.out.println(byte[].class);
	}

}

class BigDecimalHandler implements IDataTypeHandler {

	public Object getValue(String fieldValue) {
		return NumberUtil.createBigDecimal(fieldValue);
	}
}

class BigIntegerHandler implements IDataTypeHandler {

	public Object getValue(String fieldValue) {
		return NumberUtil.createBigInteger(fieldValue);
	}
}

class ByteHandler implements IDataTypeHandler {

	public Object getValue(String fieldValue) {
		return Byte.valueOf(fieldValue);
	}
}

class ShortHandler implements IDataTypeHandler {

	public Object getValue(String fieldValue) {
		return Short.valueOf(fieldValue);
	}
}

class IntegerHandler implements IDataTypeHandler {

	public Object getValue(String fieldValue) {
		return Integer.valueOf(fieldValue);
	}
}

class CharacterHandler implements IDataTypeHandler {

	public Object getValue(String fieldValue) {
		return Strings.isEmpty(fieldValue) ? null : Character
				.valueOf(fieldValue.charAt(0));
	}
}

class StringHandler implements IDataTypeHandler {

	public Object getValue(String fieldValue) {
		return fieldValue;
	}
}

class LongHandler implements IDataTypeHandler {

	public Object getValue(String fieldValue) {
		return Long.valueOf(fieldValue);
	}
}

class FloatHandler implements IDataTypeHandler {

	public Object getValue(String fieldValue) {
		return Float.valueOf(fieldValue);
	}
}

class DoubleHandler implements IDataTypeHandler {

	public Object getValue(String fieldValue) {
		return Double.valueOf(fieldValue);
	}
}

class DateHandler implements IDataTypeHandler {

	public Object getValue(String fieldValue) {
		return DateUtil.toDate(fieldValue);
	}
}

class SqlDateHandler implements IDataTypeHandler {

	public Object getValue(String fieldValue) {
		return DateUtil.toSqlDate(fieldValue);
	}
}

class TimeHandler implements IDataTypeHandler {

	public Object getValue(String fieldValue) {
		return DateUtil.toTime(fieldValue);
	}
}

class TimestampHandler implements IDataTypeHandler {

	public Object getValue(String fieldValue) {
		return DateUtil.toTimestamp(fieldValue);
	}
}

class BooleanHandler implements IDataTypeHandler {

	public Object getValue(String fieldValue) {
		char c = fieldValue.charAt(0);
		if (c == '1' || c == 'y' || c == 'Y' || c == 't' || c == 'T')
			return Boolean.TRUE;

		return Boolean.FALSE;
	}
}

@SuppressWarnings("unchecked")
class UnsupportedDataHandler implements IDataTypeHandler {

	private Class fieldType;

	public UnsupportedDataHandler(Class fieldType) {
		this.fieldType = fieldType;
	}

	public Object getValue(String fieldValue) {
		throw new JRuntimeException("unknown data type :" + fieldType);
	}
}

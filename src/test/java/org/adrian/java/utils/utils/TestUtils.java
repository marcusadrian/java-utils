package org.adrian.java.utils.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtils {

	private static final Logger LOG = LoggerFactory.getLogger(TestUtils.class);
	
	public static void debug(Supplier<?> supplier) {
		if (LOG.isDebugEnabled()) {
			Object o = supplier.get();
			String s = o instanceof String ? (String) o : ToStringBuilder.reflectionToString(o);
			LOG.debug(s);
		}
	}
	
	public static Date toDate(String yyyyMMdd) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(yyyyMMdd);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}

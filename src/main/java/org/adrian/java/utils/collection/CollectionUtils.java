package org.adrian.java.utils.collection;

import java.util.Collections;
import java.util.List;

public class CollectionUtils {

	private CollectionUtils() {
		// utility class
	}
	
	/**
	 * Null-safe variant of {@link Collections#unmodifiableList(List)}.
	 * @param children can be {@literal null}
	 * @return never {@literal null}
	 */
	public static <T> List<T> unmodifiableList(List<? extends T> children) {
		return children != null ? Collections.unmodifiableList(children) : Collections.emptyList();
	}

}

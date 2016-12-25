package org.adrian.java.utils.jpa;

import java.util.function.BiFunction;
import java.util.function.Function;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PredicateUtils {

	private static final Logger LOG = LoggerFactory.getLogger(PredicateUtils.class);
	
	
	public static <E, T> Predicate toPredicate(Root<E> root, CriteriaBuilder builder, String propertyName, T value, ComparatorKeyword comparatorKeyword) {
		if (value == null) {
			return null;
		}
		LOG.debug("{}.{} {} {}", root.getJavaType().getSimpleName(), propertyName, comparatorKeyword.symbol, value);
		return comparatorKeyword.toPredicate(root, builder, propertyName, value);
	}

	public static <E, T, R> Predicate toPredicate(Root<E> root, CriteriaBuilder builder, String propertyName, T value, ComparatorKeyword comparatorKeyword, Function<T, R> mapper) {
		if (value == null) {
			return null;
		}
		return toPredicate(root, builder, propertyName, mapper.apply(value), comparatorKeyword);
	}
	
	public static <E, T> Predicate toPredicate(Root<E> root, CriteriaBuilder builder, String propertyName, T value) {
		return toPredicate(root, builder, propertyName, value, ComparatorKeyword.DEFAULT);
	}

	public static <E, T, R> Predicate toPredicate(Root<E> root, CriteriaBuilder builder, String propertyName, T value, Function<T, R> mapper) {
		
		return toPredicate(root, builder, propertyName, value, ComparatorKeyword.DEFAULT, mapper);
	}
	
	public static enum ComparatorKeyword {
		eq("=") {
			@Override
			public <E, T> Predicate toPredicate(Root<E> root, CriteriaBuilder builder, String propertyName, T value) {
				return builder.equal(root.<T> get(propertyName), value);
			}
		},
		lt("<") {
			@Override
			public <E, T> Predicate toPredicate(Root<E> root, CriteriaBuilder builder, String propertyName, T value) {
				return toPredicate(root, builder, propertyName, value, builder::lt, builder::lessThan);
			}
		},
		gt(">") {
			@Override
			public <E, T> Predicate toPredicate(Root<E> root, CriteriaBuilder builder, String propertyName, T value) {
				return toPredicate(root, builder, propertyName, value, builder::gt, builder::greaterThan);
			}
		},
		le("<=") {
			@Override
			public <E, T> Predicate toPredicate(Root<E> root, CriteriaBuilder builder, String propertyName, T value) {
				return toPredicate(root, builder, propertyName, value, builder::le, builder::lessThanOrEqualTo);
			}
		},
		ge(">=") {
			@Override
			public <E, T> Predicate toPredicate(Root<E> root, CriteriaBuilder builder, String propertyName, T value) {
				return toPredicate(root, builder, propertyName, value, builder::ge, builder::greaterThanOrEqualTo);
			}
		};

		private String symbol;
		
		private static final ComparatorKeyword DEFAULT = ComparatorKeyword.eq;
		
		private ComparatorKeyword(String symbol) {
			this.symbol = symbol;
		}
		
		public String getSymbol() {
			return symbol;
		}
		
		public abstract <E, T> Predicate toPredicate (Root<E> root, CriteriaBuilder builder, String propertyName, T value);
		
		private static <E, T, Y extends Comparable<? super Y>> Predicate toPredicate(
				Root<E> root,
				CriteriaBuilder builder,
				String propertyName,
				T value,
				BiFunction<Expression<? extends Number>, Number, Predicate> numberFunction,
				BiFunction<Expression<? extends Y>, Y, Predicate> compareFunction
				) {
			if (value instanceof Number) {
				return numberFunction.apply(root.get(propertyName), (Number) value);
			} else if (value instanceof Comparable) {
				@SuppressWarnings({ "unchecked" })
				Y y = (Y) value;
				return compareFunction.apply(root.get(propertyName), y);
			} else {
				throw new IllegalArgumentException(String.format("Unsupported type : %s", value.getClass().getName()));
			}
		}
	}
}

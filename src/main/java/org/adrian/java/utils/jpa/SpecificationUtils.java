package org.adrian.java.utils.jpa;

import java.util.function.Function;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.adrian.java.utils.jpa.PredicateUtils.ComparatorKeyword;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationUtils {

	public static <E, T> Specification<E> toSpecification(String propertyName, T value, ComparatorKeyword comparatorKeyword) {
		return (Root<E> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
			return PredicateUtils.toPredicate(root, builder, propertyName, value, comparatorKeyword);
		};
	}
	
	public static <E, T, R> Specification<E> toSpecification(String propertyName, T value, ComparatorKeyword comparatorKeyword, Function<T, R> mapper) {
		return (Root<E> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
			return PredicateUtils.toPredicate(root, builder, propertyName, value, comparatorKeyword, mapper);
		};
	}
	
	public static <E, T> Specification<E> toSpecification(String propertyName, T value) {
		return (Root<E> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
			return PredicateUtils.toPredicate(root, builder, propertyName, value);
		};
	}
	
	public static <E, T, R> Specification<E> toSpecification(String propertyName, T value, Function<T, R> mapper) {
		return (Root<E> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
			return PredicateUtils.toPredicate(root, builder, propertyName, value, mapper);
		};
	}


}

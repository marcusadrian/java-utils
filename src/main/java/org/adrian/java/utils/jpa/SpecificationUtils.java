package org.adrian.java.utils.jpa;

import java.util.function.Function;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.adrian.java.utils.jpa.PredicateUtils.ComparatorKeyword;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationUtils {

	public static <T, P> Specification<T> toSpecification(String propertyName, P value, ComparatorKeyword comparatorKeyword) {
		return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
			return PredicateUtils.toPredicate(root, builder, propertyName, value, comparatorKeyword);
		};
	}
	
	public static <T, P, R> Specification<T> toSpecification(String propertyName, P value, ComparatorKeyword comparatorKeyword, Function<P, R> mapper) {
		return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
			return PredicateUtils.toPredicate(root, builder, propertyName, value, comparatorKeyword, mapper);
		};
	}
	
	public static <T, P> Specification<T> toSpecification(String propertyName, P value) {
		return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
			return PredicateUtils.toPredicate(root, builder, propertyName, value);
		};
	}
	
	public static <T, P, R> Specification<T> toSpecification(String propertyName, P value, Function<P, R> mapper) {
		return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
			return PredicateUtils.toPredicate(root, builder, propertyName, value, mapper);
		};
	}


}

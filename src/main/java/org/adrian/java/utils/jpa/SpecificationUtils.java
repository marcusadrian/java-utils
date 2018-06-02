package org.adrian.java.utils.jpa;

import org.adrian.java.utils.jpa.PredicateUtils.ComparatorKeyword;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class SpecificationUtils {

    public static <E, T extends Comparable<? super T>> Specification<E> toSpecification(String propertyName, T value, ComparatorKeyword comparatorKeyword) {
        return (Root<E> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                PredicateUtils.toPredicate(root, builder, propertyName, value, comparatorKeyword);
    }

    public static <E, T extends Comparable<? super T>> Specification<E> toSpecification(String propertyName, T value) {
        return (Root<E> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                PredicateUtils.toPredicate(root, builder, propertyName, value);
    }

    public static <E, T extends Comparable<? super T>> Specification<E> toSpecification(String propertyName, List<T> values) {
        if (CollectionUtils.isEmpty(values)) {
            return null;
        }
        Specifications<E> specifications = null;
        for (T value : values) {
            if (specifications == null) {
                specifications = Specifications.where(toSpecification(propertyName, value));
            } else {
                specifications = specifications.or(toSpecification(propertyName, value));
            }
        }
        return specifications;
    }

}

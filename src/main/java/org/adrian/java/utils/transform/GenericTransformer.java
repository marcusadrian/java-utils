package org.adrian.java.utils.transform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Transforms a source object into a target object.
 *
 * @param <S> the source object
 * @param <T> the target object
 * 
 * @author Marcus Adrian
 */
public interface GenericTransformer<S, T> {

    /**
     * Updates target with the source attributes.
     * @param not {@code null}
     * @param not {@code null}
     * @param params additional objects needed for the transformation might be passed in and casted accordingly inside using methods
     * @return {@code null} if source is {@code null}, otherwise the transformed object
     */
    default void update(S source, T target, Object... params)
    {
        Objects.requireNonNull(source, "source == null");
        Objects.requireNonNull(target, "target == null");
        doTransform(source, target, params);
    }

    /**
     * Transforms a single source object into a target object.
     * @param source might be {@code null}
     * @param params additional objects needed for the transformation might be passed in and casted accordingly inside using methods
     * @return {@code null} if source is {@code null}, otherwise the transformed object
     */
    default T transform(S source, Object... params)
    {
        if (source == null)
        {
            return null;
        }
        else
        {
            T target = instantiate(source, params);
            doTransform(source, target, params);
            return target;
        }
    }
    
    /**
     * Instantiate a new target object. Parameter {@literal source} might be used but often is not (simple constructor creation).
     * @param source
     * @param params additional objects needed for the transformation might be passed in and casted accordingly inside using methods
     * @return
     */
    T instantiate(S source, Object... params);
    
    /**
     * Both passed in values (source and target) are not {@literal null}, so a {@literal null} check is not necessary. 
     * @param source
     * @param params additional objects needed for the transformation might be passed in and casted accordingly inside using methods
     * @param target
     */
    void doTransform(S source, T target, Object... params);
    
    /**
     * Transforms a collection of source objects into target objects.
     * @param sources might be {@code null}
     * @param params additional objects needed for the transformation might be passed in and casted accordingly inside using methods
     * @return never {@code null} unless sources is {@code null}
     */
    default List<T> transform(Collection<? extends S> sources, Object... params)
    {
        if (sources == null)
        {
            return null;
        }
        return transform(sources, new ArrayList<T>(), params);
    }
    
    /**
     * Transforms the sources objects and adds them to the passed in targets collection.
     * @param sources might be {@code null}
     * @param targets not {@code null}
     * @param params additional objects needed for the transformation might be passed in and casted accordingly inside using methods
     * @return the passed in {@code targets} parameter to enable a fluent programming style
     */
	default <C extends Collection<? super T>> C transform(Collection<? extends S> sources, C targets, Object... params) {
		Objects.requireNonNull(targets, "targets == null");
		if (sources != null) {
			for (S source : sources) {
				targets.add(transform(source, params));
			}
		}
		return targets;
	}

}

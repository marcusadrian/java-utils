package org.adrian.java.utils.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.adrian.java.utils.exception.UnknownEnumValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Support to convert from/to {@link Enum}.
 * @author Marcus Adrian
 *
 * @param <E> the enum type
 * @param <ID> the type of the enum's converted value, we call it {@literal idValue}
 */
public class EnumSupport<E extends Enum<E>, ID> {
	
	// The enums mapped by their converted values
	private Map<ID, E> enumsById;
	
	// All the enums
	private E[] enumValues;
	
	// If conversion fails, this value can be returned for fail-safe conversions.
	private E onFailValue;
	
	// Describes the enum conversion.
	private Function<E, ID> enumToId;
	
	private static final Logger LOG = LoggerFactory.getLogger(EnumSupport.class);

	/**
	 * The {@literal enumValues} this instance is working with.
	 * @return never {@literal null}
	 */
	public E[] getEnumValues() {
		return Arrays.copyOf(enumValues, enumValues.length);
	}
	
	/**
	 * The {@literal idValues} in order of the {@literal enumValues}.
	 * @return the non {@literal null} list of {@literal idValues}
	 */
	public List<ID> idValues() {
		return addIdValues(new ArrayList<>());
	}

	/**
	 * The {@literal idValues} in (iteration) order of the passed in {@literal enumValues}.
	 * @param enumValues not {@literal null}
	 * @return the non {@literal null} list of {@literal idValues}
	 */
	public List<ID> idValues(Collection<E> enumValues) {
		return addIdValues(new ArrayList<>(), enumValues);
	}

	/**
	 * The {@literal idValues} in order of the {@literal enumValues} which are complement of {@literal enumValuesToSkip}.
	 * @param enumValuesToSkip not {@literal null}
	 * @return the non {@literal null} list of {@literal idValues}
	 */
	public List<ID> idValuesComplementOf(EnumSet<E> enumValuesToSkip) {
		return addIdValuesComplementOf(new ArrayList<>(), enumValuesToSkip);
	}
	
	/**
	 * The {@literal idValues} in order of the {@literal enumValues} except the {@literal onFailValue} (if not {@literal null}).
	 * @return the non {@literal null} list of {@literal idValues}
	 */
	public List<ID> idValuesComplementOfOnFailValue() {
		return addIdValuesComplementOfOnFailValue(new ArrayList<>());
	}

	/**
	 * Adds the {@literal idValues} to the passed in collection which in turn is returned (fluent programming).
	 * This is done in the order of the {@literal enumValues}.
	 * @param coll must not be {@literal null}
	 * @return the passed in collection
	 */
	public <C extends Collection<ID>> C addIdValues(C coll) {
		Objects.requireNonNull(coll, "coll == null");
		return addIdValues(coll, Arrays.asList(enumValues));
	}
	
	/**
	 * Adds the {@literal idValues} of the passed in {@literal enumValues} to the passed in collection which in turn is returned (fluent programming).
	 * This is done in the iteration order of the passed in {@literal enumValues}.
	 * @param coll must not be {@literal null}
	 * @param enumValues must not be {@literal null}
	 * @return the passed in collection
	 */
	public <C extends Collection<ID>> C addIdValues(C coll, Collection<E> enumValues) {
		Objects.requireNonNull(coll, "coll == null");
		Objects.requireNonNull(enumValues, "enumValues == null");
		
		enumValues
			.stream()
			.map(e -> toId(e))
			.forEach(coll::add);
		return coll;
	}

	/**
	 * Adds the {@literal idValues} which are complement of {@literal enumValuesToSkip} to the passed in collection which in turn is returned (fluent programming).
	 * This is done in the order of the {@literal enumValues}.
	 * @param coll must not be {@literal null}
	 * @param enumValues must not be {@literal null}
	 * @return the passed in collection
	 */
	public <C extends Collection<ID>> C addIdValuesComplementOf(C coll, Set<E> enumValuesToSkip) {
		Objects.requireNonNull(coll, "coll == null");
		Objects.requireNonNull(enumValuesToSkip, "enumValuesToSkip == null");

		Arrays.asList(enumValues)
			.stream()
			.filter(e -> !enumValuesToSkip.contains(e))
			.map(e -> toId(e))
			.forEach(coll::add);
		return coll;
	}

	/**
	 * Adds the {@literal idValues} except the {@literal onFailValue} (if not {@literal null}) to the passed in collection which in turn is returned (fluent programming).
	 * This is done in the order of the {@literal enumValues}.
	 * @param coll must not be {@literal null}
	 * @return the passed in collection
	 */
	public <C extends Collection<ID>> C addIdValuesComplementOfOnFailValue(C coll) {
		Objects.requireNonNull(coll, "coll == null");

		if (onFailValue != null) {
			return addIdValuesComplementOf(coll, EnumSet.of(onFailValue));
		} else {
			return addIdValues(coll);
		}
	}

	/**
	 * @param enumValues {@literal values()}} or subset of them
	 * @param enumToId function that specifies how to identify an enum
	 * @param onFailValue default enum value to return if conversion fails
	 */
	public EnumSupport(E[] enumValues, Function<E, ID> enumToId, E onFailValue) {
		this.enumValues = enumValues;
		this.onFailValue = onFailValue;
		this.enumToId = enumToId;
		enumsById = new HashMap<>();
		E previous;
		for (E e : enumValues) {
			previous = enumsById.put(enumToId.apply(e), e);
			if (previous != null) {
				throw new IllegalArgumentException(String.format("Trying to store different values under the same key (%s) : %s, %s", enumToId.apply(e), previous, e));
			}
		}
	}

	/**
	 * Calls constructor {@link #EnumSupport(Enum[], Enum, Function)} with a {@literal null} {@literal onFailValue} 
	 * @param enumValues {@literal values()}} or subset of them
	 * @param enumToId function that specifies how to identify an enum
	 */
	public EnumSupport(E[] enumValues, Function<E, ID> enumToId) {
		this(enumValues, enumToId, null);
	}

	/**
	 * Transforms the {@literal id} in its corresponding enum.
	 * Fail safe, returns {@link onFailValue} if transformation fails because of incorrect {@literal value}.
	 * @param id string identifier for the enum
	 * @return {@literal null} if {@literal id} is {@literal null}, otherwise the enum value
	 */
	public E toEnumFailSafe(ID id) {
		if (id == null) {
			return null;
		}
		E found = enumsById.get(id);
		if (found == null) {
			LOG.warn(errorMessage(id, enumValues));
			found = onFailValue;
		}
		return found;
	}
	
	private String errorMessage(ID value, Enum<?> ... values) {
		return String.format("Unknown %s value : %s, supported values are : %s", values[0].getClass().getSimpleName(), value, Arrays.asList(values));
	}


	/**
	 * Transforms the {@literal id} in its corresponding enum.
	 * @param id string identifier for the enum
	 * @return {@literal null} if {@literal id} is {@literal null}, otherwise the corresponding enum
	 * @throws UnknownEnumValueException if transformation fails because of incorrect {@literal id}.
	 */
	public E toEnum(ID id) {
		if (id == null) {
			return null;
		}
		E found = enumsById.get(id);
		if (found == null) {
			throw new UnknownEnumValueException(errorMessage(id, enumValues));
		}
		return found;
	}
	
	/**
	 * Creates a support instance that defaults to string {@literal ID} values provided by calling {@link Enum#name()}.
	 * @param enumValues {@literal values()}} or subset of them
	 * @param onFailValue default enum value to return if conversion fails
	 * @return the default support
	 */
	public static <E extends Enum<E>> EnumSupport<E, String> defaultSupport(E[] enumValues, E onFailValue) {
		return new EnumSupport<E, String>(enumValues, EnumSupport.<E>getDefaultFunction(), onFailValue);
	}
	
	public static <E extends Enum<E>> Function<E, String> getDefaultFunction() {
		return new Function<E, String>() {
			@Override
			public String apply(E e) {
				return e.name();
			}
		};
	}
	
	/**
	 * Calls {@link #defaultSupport(Enum[], Enum)} with a {@literal null} {@literal onFailValue} value.
	 * @param enumValues {@literal values()}} or subset of them
	 * @param onFailValue default enum value to return if conversion fails
	 * @return the default support
	 */
	public static <E extends Enum<E>> EnumSupport<E, String> defaultSupport(E[] enumValues) {
		return new EnumSupport<E, String>(enumValues, EnumSupport.<E>getDefaultFunction(), null);
	}
	
	/**
	 * Alternative construction, {@literal defaultSupport(SomeEnum.values())} and {@literal defaultSupport(SomeEnum.class)} are equivalent.
	 * @param clazz the {@link Enum} class
	 * @return the default support
	 * @see #defaultSupport(Enum[])
	 */
	public static <E extends Enum<E>> EnumSupport<E, String> defaultSupport(Class<E> clazz) {
		return defaultSupport(clazz.getEnumConstants());
	}
	
	/**
	 * Alternative construction, {@literal defaultSupport(SomeEnum.values(), onFailValue)} and {@literal defaultSupport(SomeEnum.class, onFailValue)} are equivalent.
	 * @param clazz the {@link Enum} class
	 * @param onFailValue default enum value to return if conversion fails
	 * @return the default support
	 * @see #defaultSupport(Enum[], Enum))
	 */
	public static <E extends Enum<E>> EnumSupport<E, String> defaultSupport(Class<E> clazz, E onFailValue) {
		return defaultSupport(clazz.getEnumConstants(), onFailValue);
	}

	/**
	 * Transforms the enum to its corresponding {@literal id} value.
	 * @param e
	 * @return
	 */
	public ID toId(E e) {
		if (e == null) {
			return null;
		}
		return enumToId.apply(e);
	}

}


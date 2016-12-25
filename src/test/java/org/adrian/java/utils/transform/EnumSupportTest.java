package org.adrian.java.utils.transform;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;


public class EnumSupportTest {

	private static enum TestEnum {
		ABC(1), BCD(2), CDE(3), DEF(4);

		private Integer code;
		
		private TestEnum(int code) {
			this.code = code;
		}
		public Integer getCode() {
			return code;
		}
	}
	
	@Test
	public void getEnumValues() {
		getEnumValues(TestEnum.values());
		getEnumValues(new TestEnum[] { TestEnum.ABC, TestEnum.DEF });
	}
	
	private void getEnumValues(TestEnum[] testEnums) {
		EnumSupport<TestEnum, String> enumSupport = EnumSupport.defaultSupport(testEnums);
		TestEnum[] enumValues = enumSupport.getEnumValues();
		assertTrue(Arrays.equals(enumValues, testEnums));
		// test immutable, that is the non-identity
		assertTrue(enumValues != testEnums);
		assertTrue(enumValues != enumSupport.getEnumValues());
	}
	
	@Test
	public void idValues() {
		EnumSupport<TestEnum, String> enumSupportNameAsId = EnumSupport.defaultSupport(TestEnum.values());
		
		List<String> names = enumSupportNameAsId.idValues();
		assertEquals("[ABC, BCD, CDE, DEF]", names.toString());
		
		names = enumSupportNameAsId.idValues(Arrays.asList(new TestEnum[] { TestEnum.ABC, TestEnum.DEF }));
		assertEquals("[ABC, DEF]", names.toString());
		
		EnumSupport<TestEnum, Integer> enumSupportCodeAsId = new EnumSupport<>(TestEnum.values(), TestEnum::getCode);
		
		List<Integer> codes = enumSupportCodeAsId.idValues();
		assertEquals("[1, 2, 3, 4]", codes.toString());
		
		codes = enumSupportCodeAsId.idValues(Arrays.asList(new TestEnum[] { TestEnum.ABC, TestEnum.DEF }));
		assertEquals("[1, 4]", codes.toString());
	}
	
	@Test
	public void idValuesComplementOf() {
		EnumSupport<TestEnum, String> enumSupport = EnumSupport.defaultSupport(TestEnum.values());
		List<String> names = enumSupport.idValuesComplementOf(EnumSet.of(TestEnum.CDE));
		assertEquals("[ABC, BCD, DEF]", names.toString());
	}

	@Test
	public void idValuesComplementOfOnFailValue() {
		EnumSupport<TestEnum, String> enumSupport = EnumSupport.defaultSupport(TestEnum.values(), TestEnum.DEF);
		List<String> names = enumSupport.idValuesComplementOfOnFailValue();
		// DEF is the onFailValue and thus ignored when this method is applied
		assertEquals("[ABC, BCD, CDE]", names.toString());
	}

	@Test
	public void addIdValues() {
		EnumSupport<TestEnum, String> enumSupport = EnumSupport.defaultSupport(TestEnum.values());
		
		// set as argument
		HashSet<String> set = enumSupport.addIdValues(new HashSet<>());
		assertEquals(4, set.size());
		
		// list as argument
		ArrayList<String> list = enumSupport.addIdValues(new ArrayList<>());
		assertEquals("[ABC, BCD, CDE, DEF]", list.toString());
		enumSupport.addIdValues(list);
		assertEquals("[ABC, BCD, CDE, DEF, ABC, BCD, CDE, DEF]", list.toString());
	
		// be selective by considering only some
		Set<TestEnum> some = EnumSet.of(TestEnum.DEF, TestEnum.ABC);
		list = enumSupport.addIdValues(new ArrayList<>(), some);
		assertTrue(list.contains("ABC"));
		assertTrue(list.contains("DEF"));
		assertEquals(some.size(), list.size());
	}
	
	@Test
	public void addIdValuesComplementOf() {
		EnumSupport<TestEnum, String> enumSupport = EnumSupport.defaultSupport(TestEnum.values());
		
		ArrayList<String> list = enumSupport.addIdValuesComplementOf(new ArrayList<>(), EnumSet.of(TestEnum.DEF, TestEnum.ABC));
		assertTrue(list.contains("BCD"));
		assertTrue(list.contains("CDE"));
		assertEquals(2, list.size());
	}
	
	@Test
	public void addIdValuesComplementOfOnFailValue() {
		EnumSupport<TestEnum, Integer> enumSupport = new EnumSupport<>(TestEnum.values(), TestEnum::getCode, TestEnum.DEF);
		List<Integer> codes = enumSupport.addIdValuesComplementOfOnFailValue(new ArrayList<>());
		// DEF is the onFailValue and thus ignored when this method is applied
		assertEquals("[1, 2, 3]", codes.toString());
	}
	
	@Test
	public void toEnumFailSafe() {
		EnumSupport<TestEnum, Integer> enumSupport = new EnumSupport<>(TestEnum.values(), TestEnum::getCode);
		assertEquals(TestEnum.ABC, enumSupport.toEnumFailSafe(1));
		assertNull(enumSupport.toEnumFailSafe(999));
		
		enumSupport = new EnumSupport<>(TestEnum.values(), TestEnum::getCode, TestEnum.DEF);
		assertEquals(TestEnum.BCD, enumSupport.toEnumFailSafe(2));
		assertEquals(TestEnum.DEF, enumSupport.toEnumFailSafe(999));
	}

	
}

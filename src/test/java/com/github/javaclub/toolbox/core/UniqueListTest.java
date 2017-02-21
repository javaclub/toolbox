package com.github.javaclub.toolbox.core;

import java.util.Arrays;
import java.util.List;

import com.github.javaclub.toolbox.core.UniqueList;

import org.junit.BeforeClass;
import org.junit.Test;

public class UniqueListTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Test
	public void testAddT() {
		List<Integer> list = new UniqueList<Integer>();
		list.add(2);
		list.add(3);
		list.add(new Integer(3));
		for (int i = 0; i < 100; i++) {
			list.add(10);
		}
		System.out.println(list);
	}

	@Test
	public void testAddAllCollectionOfQextendsT() {
		List<String> result = new UniqueList<String>();
		result.add("Hello");
		result.add("hello");
		result.add("haha");
		
		List<String> list = Arrays.asList(new String[] {"kkk", "hello", "haha"});
		result.addAll(list);
		
		System.out.println(result);
	}

}

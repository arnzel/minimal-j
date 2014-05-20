package org.minimalj.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.minimalj.model.Keys;
import org.minimalj.model.PropertyInterface;

public class KeysTest {

	@Test
	public void accessString() {
		TestClass1 object1 = new TestClass1();
		
		object1.s1 = "Test";
		PropertyInterface property = Keys.getProperty(TestClass1.KEYS.s1);
		Assert.assertEquals("Get should return the value of the public field", object1.s1, property.getValue(object1));
	}

	@Test
	public void accessSubString() {
		TestClass2 testObject2 = new TestClass2();

		testObject2.testClass1.s1 = "TestS1";
		PropertyInterface property = Keys.getProperty(TestClass2.KEYS.testClass1.s1);
		Assert.assertEquals("Get should return the value of referenced Objects", testObject2.testClass1.s1, property.getValue(testObject2));
	}

	@Test
	public void accessViaGetter() {
		TestClass2 testObject2 = new TestClass2();

		testObject2.setS3("access5");
		PropertyInterface property = Keys.getProperty(TestClass2.KEYS.getS3());
		Assert.assertEquals("If private, get should use the getter method", testObject2.getS3(), property.getValue(testObject2));
		
		property.setValue(testObject2, "access5a");
		Assert.assertEquals("If private, get should use the setter method to change value", testObject2.getS3(), property.getValue(testObject2));
	}
	
	@Test
	public void accessViaIs() {
		TestClass2 testObject2 = new TestClass2();

		testObject2.setB1(true);
		PropertyInterface property = Keys.getProperty(TestClass2.KEYS.getB1());
		Assert.assertEquals("For private boolean, get should use the isXy method", testObject2.getB1(), property.getValue(testObject2));
		
		property.setValue(testObject2, Boolean.FALSE);
		Assert.assertEquals("For private boolean, set should use the setXy method", testObject2.getB1(), property.getValue(testObject2));
	}
	
	@Test
	public void accessChildViaIs() {
		TestClass2 testObject2 = new TestClass2();
		
		testObject2.testClass1.b2 = true;
		testObject2.testClass1b.b2 = false;
		
		PropertyInterface property = Keys.getProperty(TestClass2.KEYS.testClass1.getB2());
		Assert.assertEquals("For private boolean, get should use the isXy method, even for related objects", testObject2.testClass1.getB2(), property.getValue(testObject2));

		PropertyInterface propertyB = Keys.getProperty(TestClass2.KEYS.getTestClass1b().getB2());
		Assert.assertEquals("For private boolean, get should use the isXy method, even for related objects", testObject2.getTestClass1b().getB2(), propertyB.getValue(testObject2));
		testObject2.testClass1b.b2 = true;
		Assert.assertEquals("For private boolean, get should use the isXy method, even for related objects", testObject2.getTestClass1b().getB2(), propertyB.getValue(testObject2));
		
		testObject2.testClass1.b2 = false; 
		Assert.assertEquals("For private boolean, get should use the isXy method, even for related objects", testObject2.testClass1.getB2(), property.getValue(testObject2));
	}

	@Test
	public void updateList() {
		TestClass3 testClass3 = new TestClass3();
		List<TestClass1> list = testClass3.list;
		list.add(new TestClass1());
		Assert.assertEquals(1, testClass3.list.size());
		
		PropertyInterface property = Keys.getProperty(TestClass3.KEYS.list);
		property.setValue(testClass3, list);
		
		Assert.assertEquals("After set a final list field with its existing values the content must be the same", 1, testClass3.list.size());
		
		List<TestClass1> list2 = new ArrayList<>();
		list2.add(new TestClass1());
		list2.add(new TestClass1());
		property.setValue(testClass3, list2);
		Assert.assertEquals("Update of final list field with new values failed", list2.size(), testClass3.list.size());
	}
	
	public static class TestClass1 {
		public static final TestClass1 KEYS = Keys.of(TestClass1.class);
		
		public String s1;
		private Boolean b2;
		
		public Boolean getB2() {
			if (Keys.isKeyObject(this)) return Keys.methodOf(this, "b2", Boolean.class);
			
			return b2;
		}
		
		public void setB2(Boolean b2) {
			this.b2 = b2;
		}
	}

	public static class TestClass2 {
		public static final TestClass2 KEYS = Keys.of(TestClass2.class);
		
		public String s2;
		private String s3;
		private Boolean b1;
		public final TestClass1 testClass1 = new TestClass1();
		private final TestClass1 testClass1b = new TestClass1();
		public final TestClass1 tc1 = new TestClass1();
		
		public String getS3() {
			if (Keys.isKeyObject(this)) return Keys.methodOf(this, "s3", String.class);

			return s3;
		}
		
		public void setS3(String s3) {
			this.s3 = s3;
		}
		
		public Boolean getB1() {
			if (Keys.isKeyObject(this)) return Keys.methodOf(this, "b1", Boolean.class);
			
			return b1;
		}
		
		public void setB1(Boolean b1) {
			this.b1 = b1;
		}

		public TestClass1 getTestClass1b() {
			if (Keys.isKeyObject(this)) return Keys.methodOf(this, "testClass1b", TestClass1.class);
			return testClass1b;
		}
		
	}

	public static class TestClass3 {
		public static final TestClass3 KEYS = Keys.of(TestClass3.class);
		
		public final List<TestClass1> list = new ArrayList<>();
	}
	
}
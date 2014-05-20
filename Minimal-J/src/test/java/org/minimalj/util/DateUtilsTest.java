package org.minimalj.util;

import java.util.Locale;

import junit.framework.Assert;

import org.joda.time.ReadablePartial;
import org.junit.Test;
import org.minimalj.util.DateUtils;


public class DateUtilsTest {

	
	@Test
	public void parseCH_01_02_1923() {
		Assert.assertEquals("1923-02-01", DateUtils.parseCH("01.02.1923", true));
		Assert.assertEquals("1923-02-01", DateUtils.parseCH("01.02.1923", false));
	}

	@Test
	public void parseCH_1_02_1923() {
		Assert.assertEquals("1923-02-01", DateUtils.parseCH("1.02.1923", true));
		Assert.assertEquals("1923-02-01", DateUtils.parseCH("1.02.1923", false));
	}

	@Test
	public void parseCH_1_2_1923() {
		Assert.assertEquals("1923-02-01", DateUtils.parseCH("1.2.1923", true));
		Assert.assertEquals("1923-02-01", DateUtils.parseCH("1.2.1923", false));
	}

	@Test
	public void parseCH_1_2_23() {
		Assert.assertEquals("1923-02-01", DateUtils.parseCH("1.2.23", true));
		Assert.assertEquals("1923-02-01", DateUtils.parseCH("1.2.23", false));
	}
	
	@Test
	public void parseCH_7_8_2010() {
		Assert.assertEquals("2010-08-07", DateUtils.parseCH("7.8.2010", true));
		Assert.assertEquals("2010-08-07", DateUtils.parseCH("7.8.2010", false));
	}

	@Test
	public void parseCH_10_8_2010() {
		Assert.assertEquals("2010-08-10", DateUtils.parseCH("10.8.2010", true));
		Assert.assertEquals("2010-08-10", DateUtils.parseCH("10.8.2010", false));
	}

	@Test
	public void parseCH_10_8_10() {
		Assert.assertEquals("2010-08-10", DateUtils.parseCH("10.8.10", true));
		Assert.assertEquals("2010-08-10", DateUtils.parseCH("10.8.10", false));
	}
	
	@Test
	public void parseCH_1_2_13() {
		Assert.assertEquals("2013-02-01", DateUtils.parseCH("1.2.13", true));
		Assert.assertEquals("2013-02-01", DateUtils.parseCH("1.2.13", false));
	}

	@Test
	public void parseCH_01_02_13() {
		Assert.assertEquals("2013-02-01", DateUtils.parseCH("01.02.13", true));
		Assert.assertEquals("2013-02-01", DateUtils.parseCH("01.02.13", false));
	}

	@Test
	public void parseCH_010213() {
		Assert.assertEquals("2013-02-01", DateUtils.parseCH("010213", true));
		Assert.assertEquals("2013-02-01", DateUtils.parseCH("010213", false));
	}

	@Test
	public void parseCH_010223() {
		Assert.assertEquals("1923-02-01", DateUtils.parseCH("010223", true));
		Assert.assertEquals("1923-02-01", DateUtils.parseCH("010223", false));
	}

	@Test
	public void parseCH_01021923() {
		Assert.assertEquals("1923-02-01", DateUtils.parseCH("01021923", true));
		Assert.assertEquals("1923-02-01", DateUtils.parseCH("01021923", false));
	}

	@Test
	public void parseCH_1899() {
		Assert.assertEquals("1899", DateUtils.parseCH("1899", true));
	}

	@Test
	public void parseCH_1899_Not_Part() {
		Assert.assertEquals("", DateUtils.parseCH("1899", false));
	}

	@Test
	public void parseCH_1901() {
		Assert.assertEquals("1901-02", DateUtils.parseCH("02.1901", true));
	}

	@Test
	public void parseCH_1901_Not_Part() {
		Assert.assertEquals("", DateUtils.parseCH("02.1901", false));
	}
	
	@Test
	public void parseCH_091274() {
		Assert.assertEquals("1974-12-09", DateUtils.parseCH("091274", true));
		Assert.assertEquals("1974-12-09", DateUtils.parseCH("091274", false));
	}
	
	@Test
	public void parseCH_09121974() {
		Assert.assertEquals("1974-12-09", DateUtils.parseCH("09121974", true));
		Assert.assertEquals("1974-12-09", DateUtils.parseCH("09121974", false));
	}
	
	@Test
	public void formatPartialYYYY() {
		Locale.setDefault(Locale.GERMAN);
		ReadablePartial p = DateUtils.newPartial("2012");
		Assert.assertEquals("2012", DateUtils.formatPartial(p));
		p = DateUtils.newPartial("998");
		Assert.assertEquals("0998", DateUtils.formatPartial(p));
	}
	
	@Test
	public void formatPartialYYYYMM() {
		Locale.setDefault(Locale.GERMAN);
		ReadablePartial p = DateUtils.newPartial("2012", "09");
		Assert.assertEquals("2012-09", DateUtils.formatPartial(p));
		p = DateUtils.newPartial("2012", "10");
		Assert.assertEquals("2012-10", DateUtils.formatPartial(p));
	}

	@Test
	public void formatPartialYYYYMMDD() {
		Locale.setDefault(Locale.GERMAN);
		ReadablePartial p = DateUtils.newPartial("2012", "09", "23");
		Assert.assertEquals("2012-09-23", DateUtils.formatPartial(p));
		p = DateUtils.newPartial("2012", "10", "8");
		Assert.assertEquals("2012-10-08", DateUtils.formatPartial(p));
	}

}
package ch.openech.mj.example.model;

import ch.openech.mj.example.ExampleFormats;
import ch.openech.mj.model.Keys;
import ch.openech.mj.model.annotation.Sizes;
import ch.openech.mj.util.StringUtils;

@Sizes(ExampleFormats.class)
public class HouseNumber {

	public static final HouseNumber HOUSE_NUMBER = Keys.of(HouseNumber.class);
	
	public String houseNumber;
	public String dwellingNumber;

	public String concatNumbers() {
		if (StringUtils.isBlank(houseNumber) && StringUtils.isBlank(dwellingNumber)) return null;
	
		if (StringUtils.isBlank(houseNumber)) return dwellingNumber;
		if (StringUtils.isBlank(dwellingNumber)) return houseNumber;
		
		return houseNumber + "&nbsp;/&nbsp;" + dwellingNumber;
	}

}

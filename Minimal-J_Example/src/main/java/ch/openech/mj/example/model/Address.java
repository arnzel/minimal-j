package ch.openech.mj.example.model;

import java.util.List;

import ch.openech.mj.edit.validation.Validation;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.example.ExampleFormats;
import ch.openech.mj.model.EnumUtils;
import ch.openech.mj.model.Keys;
import ch.openech.mj.model.annotation.Required;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.model.annotation.Sizes;
import ch.openech.mj.util.StringUtils;

@Sizes(ExampleFormats.class)
public class Address implements Validation {
	
	public static final Address ADDRESS = Keys.of(Address.class);

	// person
	public MrMrs mrMrs;
	
	// organisation oder person
	public String title;
	public String firstName, lastName;
	
	// all
	@Size(ExampleFormats.addressLine)
	public String addressLine1, addressLine2;
	public String street;
	public final HouseNumber houseNumber = new HouseNumber();
	@Size(4)
	public Integer postOfficeBoxNumber;
	public String postOfficeBoxText;
	public String locality;
	public String country = "CH";
	@Size(ExampleFormats.foreignZipCode)
	public String zip;
	@Size(4)
	public Integer swissZipCodeId;
	@Required
	public String town;
	
	public String getSwissZipCode() {
		if (isSwiss() && !StringUtils.isEmpty(zip)) {
			int pos = zip.indexOf(" ");
			if (pos < 0) {
				return zip;
			} else {
				return zip.substring(0, pos);
			}
		} else {
			return null;
		}
	}

	public String getSwissZipCodeAddOn() {
		if (isSwiss() && !StringUtils.isEmpty(zip)) {
			int pos = zip.indexOf(" ");
			if (pos < 0 || pos == zip.length()-1) {
				return null;
			} else {
				return zip.substring(pos+1);
			}
		} else {
			return null;
		}
	}
	
	/*
	public String getSwissZipCodeId() {
		if (!isSwiss()) return null;
		String swissZipCode = getSwissZipCode();
		String swissZipCodeAddOn = getSwissZipCodeAddOn();
		Plz plz = PlzImport.getInstance().getPlz(swissZipCode, swissZipCodeAddOn);
		if (plz != null) {
			return "" + plz.onrp;
		} else {
			return null;
		}
	}
	*/
	
	public boolean isEmpty() {
		return StringUtils.isBlank(town);
 	}
	
	public boolean isPerson() {
		// Bei Person muss lastName gesetzt sein
		return !StringUtils.isBlank(lastName);
	}
	
	public boolean isSwiss() {
		return "CH".equals(country) || "LI".equals(country);
	}
	
	public String toHtml() {
		StringBuilder s = new StringBuilder();
		toHtml(s);
		return s.toString();
	}
	
	public void toHtml(StringBuilder s) {
		StringUtils.appendLine(s, EnumUtils.getText(mrMrs), title, firstName, lastName);
		StringUtils.appendLine(s, addressLine1);
		StringUtils.appendLine(s, addressLine2);
		StringUtils.appendLine(s, street, houseNumber.concatNumbers());
		StringUtils.appendLine(s, postOfficeBoxText, postOfficeBoxNumber != null ? postOfficeBoxNumber.toString() : null);
		if ("CH".equals(country)) {
			StringUtils.appendLine(s, zip, town);
		} else {
			StringUtils.appendLine(s, country, zip, town);
		}
		StringUtils.appendLine(s, locality);
	}

	private static final String MESSAGE = "Plz muss aus 4 Ziffern oder 4 Ziffern + 2 Zusatzziffern bestehen";
	
	private String validateZip() {
		if (!isSwiss() || StringUtils.isEmpty(zip)) return null;
		if (zip.length() == 4 || zip.length() == 7) {
			for (int i = 0; i<4; i++) {
				if (!Character.isDigit(zip.charAt(i))) {
					return MESSAGE;
				}
			}
			if (zip.length() == 7) {
				if (zip.charAt(4) != ' ') return MESSAGE;
				for (int i = 5; i<7; i++) {
					if (!Character.isDigit(zip.charAt(i))) {
						return MESSAGE;
					}
				}
			}
		} else {
			return MESSAGE;
		}
		return null;
	}

	@Override
	public void validate(List<ValidationMessage> validationResult) {
		String message = validateZip();
		if (message != null) {
			validationResult.add(new ValidationMessage(ADDRESS.zip, message));
		}
	}

}

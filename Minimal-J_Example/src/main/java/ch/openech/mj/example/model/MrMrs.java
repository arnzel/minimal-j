package ch.openech.mj.example.model;


public enum MrMrs {
	Frau,
	Herr,
	Fraeulein;

	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}
}

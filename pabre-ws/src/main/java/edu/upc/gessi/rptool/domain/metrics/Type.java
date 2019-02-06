package edu.upc.gessi.rptool.domain.metrics;

public enum Type {
    DOMAIN("domain"), FLOAT("float"), INTEGER("integer"), STRING("string"), TIME("time"), SET("set");

    private final String name;
    private final static Type[] enumValues = Type.values();

    private Type(String s) {
	name = s;
    }

    @Override
    public String toString() {
	return this.name;
    }

    public boolean equalsName(String otherName) {
	return name.equals(otherName);
    }

    public static Type fromString(String code) {
	for (Type e : enumValues)
	    if (e.equalsName(code))
		return e;
	return null;
    }

}
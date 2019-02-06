package edu.upc.gessi.rptool.domain;

/**
 * {@link Enum} used to indicate the dependency type, can be:
 * <ul>
 * <li>{@link #IMPLIES}</li>
 * <li>{@link #EXCLUDES}</li>
 * <li>{@link #CONTRIBUTES}</li>
 * <li>{@link #DAMAGES}</li>
 * </ul>
 * 
 * @author Awais Iqbal
 *
 */
public enum DependencyTypeDomain {

    IMPLIES("implies"), EXCLUDES("excludes"), CONTRIBUTES("contributes"), DAMAGES("damages");

    private final String name;

    private static final DependencyTypeDomain[] enumValues = DependencyTypeDomain.values();

    private DependencyTypeDomain(String s) {
	name = s;
    }

    @Override
    public String toString() {
	return name;
    }

    public boolean equalsName(String otherName) {
	return name.equals(otherName);
    }

    public static DependencyTypeDomain fromInteger(int value) throws IndexOutOfBoundsException {
	return enumValues[value];
    }

    public static DependencyTypeDomain fromString(String code) {
	for (DependencyTypeDomain e : enumValues) {
	    if (e.equalsName(code))
		return e;
	}
	return null;
    }
}

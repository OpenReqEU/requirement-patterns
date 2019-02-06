package edu.upc.gessi.rptool.domain;

/**
 * {@link Enum} indicates the direction of the dependencies, can be:
 * <ul>
 * <li>{@link #UNIDIRECTIONAL}</li>
 * <li>{@link #BIDIRECTIONAL}</li>
 * </ul>
 * 
 * @author Awais Iqbal
 *
 */
public enum DependencyDirectionDomain {

    UNIDIRECTIONAL("unidirectional"), BIDIRECTIONAL("bidirectional");

    private final String name;
    private final static DependencyDirectionDomain[] enumValues = DependencyDirectionDomain.values();

    private DependencyDirectionDomain(String s) {
	name = s;
    }

    @Override
    public String toString() {
	return name;
    }

    public boolean equalsName(String otherName) {
	return name.equals(otherName);
    }

    public static DependencyDirectionDomain fromInteger(int value) throws IndexOutOfBoundsException {
	return enumValues[value];
    }

    public static DependencyDirectionDomain fromString(String code) {
	for (DependencyDirectionDomain e : enumValues)
	    if (e.equalsName(code))
		return e;
	return null;
    }
}

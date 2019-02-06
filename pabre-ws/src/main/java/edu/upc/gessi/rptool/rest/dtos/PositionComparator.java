package edu.upc.gessi.rptool.rest.dtos;

import java.util.Comparator;

public class PositionComparator implements Comparator<Positionable> {
    @Override
    public int compare(Positionable p1, Positionable p2) {
	return p1.getPos() - p2.getPos();
    }
}

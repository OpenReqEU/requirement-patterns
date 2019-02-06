package edu.upc.gessi.rptool.rest.dtos;

import edu.upc.gessi.rptool.rest.dtos.patternelements.RequirementPatternDTO;

public interface ClassifierDTOInterface {

    public void addRequirementPattern(RequirementPatternDTO rpdto);

    public long getId();
}

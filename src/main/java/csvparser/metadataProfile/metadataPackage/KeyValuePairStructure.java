package csvparser.metadataProfile.metadataPackage;

import csvparser.metadataProfile.identifierPackage.AbstractStructureIdentifier;
import csvparser.metadataProfile.linesPackage.AbstractLinesParent;
import csvparser.metadataProfile.types.LineType;

public class KeyValuePairStructure extends AbstractMetadataStructure {

    public KeyValuePairStructure(String separator, LineType line, Boolean fixedPosition, AbstractLinesParent[] lines, AbstractStructureIdentifier identifier) {
        super(separator, line, fixedPosition, true, lines, identifier);
    }

}

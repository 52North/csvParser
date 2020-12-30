package csvparser.metadataProfile.linesPackage;

import csvparser.metadataProfile.structureGroupPackage.SimpleStructureList;
import csvparser.metadataProfile.vocabularyTermsPackage.FormatStructure;

public class Value {
    public SimpleStructureList[] structureGroup;
    public FormatStructure format;

    public Value() { }

    public Value(SimpleStructureList[] structureGroup) {
        this.structureGroup = structureGroup;
    }
    
    public Value(SimpleStructureList[] structureGroup, FormatStructure format) {
        this.structureGroup = structureGroup;
        this.format = format;
    }
}

package csvparser.metadataProfile.linesPackage;

import csvparser.metadataProfile.structureGroupPackage.IterationStructureList;

public class KeyFirstField {
    public Block block;
    public IterationStructureList[] structureGroup;

    public KeyFirstField(Block block, IterationStructureList[] structureGroup) {
        this.block = block;
        this.structureGroup = structureGroup;
    }
}

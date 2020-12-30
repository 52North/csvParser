package csvparser.metadataProfile.structureGroupPackage;

public class SimpleStructureList extends AbstractStructureList {
    public SimpleStructure[] structure;

    public SimpleStructureList(Boolean optional, SimpleStructure[] structure) {
        super(optional);
        this.structure = structure;
    }
}

package csvparser.metadataProfile.structureGroupPackage;

public class IgnoreStructureList extends AbstractStructureList {
    public Boolean ignore;
    public SimpleStructure[] structure;

    public IgnoreStructureList(Boolean optional, Boolean ignore, SimpleStructure[] structure) {
        super(optional);
        this.ignore = ignore;
        this.structure = structure;
    }
}

package csvparser.metadataProfile.structureGroupPackage;

import csvparser.metadataProfile.types.StringType;

public class SimpleStructure implements IStructure {
    public String string;
    public StringType type;

    public SimpleStructure(String string, StringType type) {
        this.string = string;
        this.type = type;
    }

    public SimpleStructure(SimpleStructure that) {
        this.string = that.string;
        this.type = that.type;
    }
}

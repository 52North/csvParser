package csvparser.metadataProfile.structureGroupPackage;

import csvparser.metadataProfile.types.LinkNumberType;

public class IterationStructure implements IStructure {
    public Integer start;
    public LinkNumberType link;

    public IterationStructure(Integer start, LinkNumberType link) {
        this.start = start;
        this.link = link;
    }

    public IterationStructure(IterationStructure that) {
        this.start = that.start;
        this.link = that.link;
    }
}

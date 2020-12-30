package csvparser.metadataProfile.linesPackage;

import csvparser.metadataProfile.structureGroupPackage.SimpleStructureList;
import csvparser.metadataProfile.types.LineType;

public class FirstField {
    public Boolean different;
    public Boolean title;
    public LineType titleScope;
    public SimpleStructureList[] structureGroup;

    public FirstField() {
        this.different = false;
    }

    public FirstField(Boolean title, LineType titleScope, SimpleStructureList[] structureGroup) {
        this.different = true;
        this.title = title;
        this.titleScope = titleScope;
        this.structureGroup = structureGroup;
    }
}

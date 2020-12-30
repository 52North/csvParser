package csvparser.metadataProfile.identifierPackage;

public abstract class AbstractStructureIdentifier {
    public AbstractLineIdentifier idx;

    public AbstractStructureIdentifier() {
        this.idx = null;
    }

    public AbstractStructureIdentifier(AbstractLineIdentifier idx) {
        this.idx = idx;
    }
    
}

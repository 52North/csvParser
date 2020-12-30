package csvparser.metadataProfile.linesPackage;

public abstract class AbstractLinesParent {
    public Boolean fixedPosition;
    public Boolean skip;

    public AbstractLinesParent(Boolean fixedPosition, Boolean skip) {
        this.fixedPosition = fixedPosition;
        this.skip = skip;
    }
}

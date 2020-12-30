package csvparser.metadataProfile.identifierPackage;

public class StartIdentifier extends AbstractLineIdentifier {
    public Integer start;

    public StartIdentifier(Integer start) {
        this.start = start;
    }

    public StartIdentifier(Integer length, Integer start) {
        super(length);
        this.start = start;
    }
}

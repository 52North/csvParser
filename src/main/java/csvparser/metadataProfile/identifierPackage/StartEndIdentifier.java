package csvparser.metadataProfile.identifierPackage;

public class StartEndIdentifier extends AbstractLineIdentifier {
    public Integer start;
    public Integer end;

    public StartEndIdentifier(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }

    public StartEndIdentifier(Integer length, Integer start, Integer end) {
        super(length);
        this.start = start;
        this.end = end;
    }
}

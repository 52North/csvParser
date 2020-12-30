package csvparser.metadataProfile.identifierPackage;

public class EndIdentifier extends AbstractLineIdentifier {
    public Integer end;

    public EndIdentifier(Integer end) {
        this.end = end;
    }

    public EndIdentifier(Integer length, Integer end) {
        super(length);
        this.end = end;
    }
}

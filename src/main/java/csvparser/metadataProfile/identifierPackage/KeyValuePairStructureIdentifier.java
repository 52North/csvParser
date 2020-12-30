package csvparser.metadataProfile.identifierPackage;

import csvparser.metadataProfile.types.StringLocation;

public class KeyValuePairStructureIdentifier extends AbstractStructureIdentifier {
    public StringLocation position;
    public Boolean connected;
    public String literal;
    // public IValueIdentifier[] ignore;

    public KeyValuePairStructureIdentifier(StringLocation position, Boolean connected, String literal) {
        super();
        this.position = position;
        this.connected = connected;
        this.literal = literal;
    }

    public KeyValuePairStructureIdentifier(StringLocation position, Boolean connected, String literal,
            AbstractLineIdentifier idx) {
        super(idx);
        this.position = position;
        this.connected = connected;
        this.literal = literal;
    }
}

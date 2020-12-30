package csvparser.metadataProfile.vocabularyTermsPackage;

public class MetadataCategory {
    public String literal;
    public String content;
    public String vocabularyTerm;

    public MetadataCategory(String literal, String content) {
        this.literal = literal;
        this.content = content;
    }

    public MetadataCategory(String literal, String content, String vocabularyTerm) {
        this.literal = literal;
        this.content = content;
        this.vocabularyTerm = vocabularyTerm;
    }
}

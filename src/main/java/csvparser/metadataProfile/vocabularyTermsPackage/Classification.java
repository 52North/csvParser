package csvparser.metadataProfile.vocabularyTermsPackage;

public class Classification {
    public String literal;
    public String vocabularyTerm;
    public String vocabulary;

    public Classification(String literal, String vocabularyTerm, String vocabulary) {
        this.literal = literal;
        this.vocabularyTerm = vocabularyTerm;
        this.vocabulary = vocabulary;
    }
}

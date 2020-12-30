package csvparser.metadataProfile.vocabularyTermsPackage;

public class VocabularyTerm {
    public String vocabularyTerm;
    public String vocabulary;
    public String literal;
    
    public DataStructure data;

    public VocabularyTerm(String vocabularyTerm, String vocabulary, String literal) {
        this.vocabularyTerm = vocabularyTerm;
        this.vocabulary = vocabulary;
        this.literal = literal;
    }

    public VocabularyTerm(String vocabularyTerm, String vocabulary, String literal, DataStructure data) {
        this.vocabularyTerm = vocabularyTerm;
        this.vocabulary = vocabulary;
        this.literal = literal;
        this.data = data;
    }
}

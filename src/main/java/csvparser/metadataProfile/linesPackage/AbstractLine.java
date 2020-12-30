package csvparser.metadataProfile.linesPackage;

import csvparser.metadataProfile.vocabularyTermsPackage.Classification;
import csvparser.metadataProfile.vocabularyTermsPackage.MetadataCategory;
import csvparser.metadataProfile.vocabularyTermsPackage.VocabularyTerm;

public abstract class AbstractLine extends AbstractLinesParent {
    public String separator;
    public String vocabulary;
    public Boolean header;
    public Value value;
    public MetadataCategory[] category;
    public Classification[] classification;
    public VocabularyTerm[] vocabularyTerms;

    public AbstractLine(Boolean fixedPosition, String separator, String vocabulary, Boolean header, Value value) {
        super(fixedPosition, false);
        this.separator = separator;
        this.vocabulary = vocabulary;
        this.header = header;
        this.value = value;
    }

    public AbstractLine(Boolean fixedPosition, String separator, String vocabulary, Boolean header, Value value,
            MetadataCategory[] category, Classification[] classification, VocabularyTerm[] vocabularyTerms) {
        super(fixedPosition, false);
        this.separator = separator;
        this.vocabulary = vocabulary;
        this.header = header;
        this.value = value;
        this.category = category;
        this.classification = classification;
        this.vocabularyTerms = vocabularyTerms;
    }
}

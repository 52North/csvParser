package csvparser.metadataProfile.linesPackage;

import csvparser.metadataProfile.vocabularyTermsPackage.Classification;
import csvparser.metadataProfile.vocabularyTermsPackage.MetadataCategory;
import csvparser.metadataProfile.vocabularyTermsPackage.VocabularyTerm;

public class SimpleLine extends AbstractLine {
    public FirstField firstField;

    // public SimpleLine(Boolean fixedPosition, String separator, String vocabulary, Boolean header, Value value,
    //         FirstField firstField) {
    //     super(fixedPosition, separator, vocabulary, header, value);
    //     this.firstField = firstField;
    // }

    public SimpleLine(Boolean fixedPosition, String separator, String vocabulary, Boolean header, Value value,
            MetadataCategory[] category, Classification[] classification, VocabularyTerm[] vocabularyTerms,
            FirstField firstField) {
        super(fixedPosition, separator, vocabulary, header, value, category, classification, vocabularyTerms);
        this.firstField = firstField;
    }
        
}

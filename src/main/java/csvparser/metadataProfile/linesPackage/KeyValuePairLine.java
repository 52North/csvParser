package csvparser.metadataProfile.linesPackage;

import csvparser.metadataProfile.vocabularyTermsPackage.Classification;
import csvparser.metadataProfile.vocabularyTermsPackage.MetadataCategory;
import csvparser.metadataProfile.vocabularyTermsPackage.VocabularyTerm;

public class KeyValuePairLine extends AbstractLine {
    public KeyFirstField key;

    // public KeyValuePairLine(Boolean fixedPosition, String separator, String vocabulary, Boolean header, Value value,
    //         KeyFirstField key) {
    //     super(fixedPosition, separator, vocabulary, header, value);
    //     this.key = key;
    // }

    public KeyValuePairLine(Boolean fixedPosition, String separator, String vocabulary, Boolean header, Value value,
            MetadataCategory[] category, Classification[] classification, VocabularyTerm[] vocabularyTerms,
            KeyFirstField key) {
        super(fixedPosition, separator, vocabulary, header, value, category, classification, vocabularyTerms);
        this.key = key;
    }

}

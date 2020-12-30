package csvparser.metadataProfile;

import csvparser.metadataProfile.filePackage.FileMetadata;
import csvparser.metadataProfile.identifierPackage.KeyValuePairStructureIdentifier;
import csvparser.metadataProfile.identifierPackage.SimpleStructureIdentifier;
import csvparser.metadataProfile.identifierPackage.StartEndIdentifier;
import csvparser.metadataProfile.identifierPackage.StartIdentifier;
import csvparser.metadataProfile.metadataPackage.AbstractMetadataStructure;
import csvparser.metadataProfile.trimPackage.AbstractTrimmer;
import csvparser.metadataProfile.trimPackage.IndexLiteralTrimmer;
import csvparser.metadataProfile.trimPackage.LineByIdxTrimmer;
import csvparser.metadataProfile.trimPackage.LineyByLiteralTrimmer;
import csvparser.metadataProfile.types.LineType;
import csvparser.metadataProfile.vocabularyTermsPackage.MetadataCategory;
import csvparser.metadataProfile.vocabularyTermsPackage.VocabularyTerm;

public class DataConfiguration {
    /* file name */
    public FileMetadata fileMetadata;
    /* special characters */
    public String separatorSymbol;
    /* trim */
    public AbstractTrimmer[] trimmers;
    /* metadata */
    public AbstractMetadataStructure[] metadata;
    /* data */
    public AbstractMetadataStructure[] data;
    /* vocabulary terms */
    public VocabularyTerm[] vocabularyTerms;
    /* category */
    public MetadataCategory[] category;

    /**
     * Constructor
     * @param fileMetadata
     * @param separatorSymbol
     * @param trimmers
     * @param metadata
     * @param data
     * @param vocabularyTerms
     * @param category
     */
    public DataConfiguration(FileMetadata fileMetadata, String separatorSymbol, AbstractTrimmer[] trimmers,
            AbstractMetadataStructure[] metadata, AbstractMetadataStructure[] data, VocabularyTerm[] vocabularyTerms,
            MetadataCategory[] category) {
        this.fileMetadata = fileMetadata;
        this.separatorSymbol = separatorSymbol;
        this.trimmers = trimmers;
        this.metadata = metadata;
        this.data = data;
        this.vocabularyTerms = vocabularyTerms;
        this.category = category;
    }

    public String[] trimDataString(String[] input) {

        String[] res = input;
        if (this.trimmers.length > 0) {
			Integer idxTrimCounter = 0;
			for (int i = 0; i < this.trimmers.length; i++) {
                AbstractTrimmer trimObj = this.trimmers[i];
                res = trimObj.trimString(res, idxTrimCounter);
                if (trimObj instanceof LineByIdxTrimmer || trimObj instanceof LineyByLiteralTrimmer) {
                    idxTrimCounter++;
                }
            }
        }
        return res;
    }

    public String[] identifyMetadataBlocks(String[] input) {
        for ( AbstractMetadataStructure el: this.metadata) {
            el.identifyBlock(input, this.separatorSymbol);
        }
        return input;
    }


}

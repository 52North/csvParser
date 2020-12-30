package csvparser.metadataProfile.vocabularyTermsPackage;

public class DataStructure {
    public Boolean header;
    public MetadataCategory[] category;
    public Classification[] classification;
    public FormatStructure format;

    public DataStructure(Boolean header) {
        this.header = header;
    }

    public DataStructure(Boolean header, MetadataCategory[] category, Classification[] classification, FormatStructure format) {
        this.header = header;
        this.category = category;
        this.classification = classification;
        this.format = format;
    }
}

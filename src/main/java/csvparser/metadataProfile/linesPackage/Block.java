package csvparser.metadataProfile.linesPackage;

public class Block {
    public Boolean multipleLines;
    public Boolean connected;

    public Block(Boolean multipleLines, Boolean connected) {
        this.multipleLines = multipleLines;
        this.connected = connected;
    }
}

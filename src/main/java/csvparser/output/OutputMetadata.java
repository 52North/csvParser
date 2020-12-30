package csvparser.output;

public class OutputMetadata {
    public OutputKeyValue[] file;
    public OutputKeyValue[] column;
    public OutputKeyValue[] global;

    public OutputMetadata(OutputKeyValue[] file, OutputKeyValue[] column, OutputKeyValue[] global) {
        this.file = file;
        this.column = column;
        this.global = global;
    }
}

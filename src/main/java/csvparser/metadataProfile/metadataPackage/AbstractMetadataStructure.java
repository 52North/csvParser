package csvparser.metadataProfile.metadataPackage;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import csvparser.App;
import csvparser.metadataProfile.identifierPackage.AbstractStructureIdentifier;
import csvparser.metadataProfile.identifierPackage.KeyValuePairStructureIdentifier;
import csvparser.metadataProfile.identifierPackage.SimpleStructureIdentifier;
import csvparser.metadataProfile.identifierPackage.StartEndIdentifier;
import csvparser.metadataProfile.identifierPackage.StartIdentifier;
import csvparser.metadataProfile.linesPackage.AbstractLinesParent;
import csvparser.metadataProfile.linesPackage.KeyValuePairLine;
import csvparser.metadataProfile.linesPackage.SimpleLine;
import csvparser.metadataProfile.linesPackage.SkipLine;
import csvparser.metadataProfile.types.LineType;

public abstract class AbstractMetadataStructure {
    public String separator;
    public LineType line;
    public Boolean fixedPosition;
    public Boolean keyvalue;
    public AbstractLinesParent[] lines;
    public AbstractStructureIdentifier identifier;

    public String[] identifiedDataBlock; // will be added after trimming

    public AbstractMetadataStructure(String separator, LineType line, Boolean fixedPosition, Boolean keyvalue,AbstractLinesParent[] lines, AbstractStructureIdentifier identifier) {
        this.separator = separator;
        this.line = line;
        this.fixedPosition = fixedPosition;
        this.keyvalue = keyvalue;
        this.lines = lines;
        this.identifier = identifier;
    }

    public void identifyBlock(String[] inputDataContent, String mainSeparatorSymbol) {
        Integer minIdx = 0;
        Integer maxIdx = null;
        Integer currIdx = 0;

        ArrayList<String> clonedMetadataBlockList = App.cloneArrayOfObjects(String.class, inputDataContent);
        String[] metadataBlock = clonedMetadataBlockList.toArray(new String[clonedMetadataBlockList.size()]);
        ArrayList<String> clonedDataBlockList = App.cloneArrayOfObjects(String.class, inputDataContent);
        String[] dataBlock = clonedDataBlockList.toArray(new String[clonedDataBlockList.size()]);

        ArrayList<String> clonedInputDataContentList = App.cloneArrayOfObjects(String.class, inputDataContent);
        String[] checkDataBlock = clonedInputDataContentList.toArray(new String[clonedInputDataContentList.size()]);

        if (this.identifier != null && this.identifier.idx != null) {
            if (this.identifier.idx instanceof StartIdentifier) {
                StartIdentifier seIdx = (StartIdentifier) this.identifier.idx;
                minIdx = seIdx.start;
            } else if (this.identifier.idx instanceof StartEndIdentifier) {
                StartEndIdentifier seIdx = (StartEndIdentifier) this.identifier.idx;
                minIdx = seIdx.start;
                maxIdx = seIdx.end;
            }
        }

        if (this.line.equals(LineType.Column)) {
            // TODO later
        }

        if (this.line.equals(LineType.Row)) {
            if (this.identifier instanceof SimpleStructureIdentifier) {

                for (int i = minIdx; i < this.lines.length; i++) {
                    AbstractLinesParent line = this.lines[i];

                    if (line instanceof SkipLine) {
                        if (i == this.lines.length - 1) {
                            dataBlock = Arrays.copyOfRange(metadataBlock, i + 1, metadataBlock.length);
                            metadataBlock = Arrays.copyOfRange(metadataBlock, 0, i + 1);
                            checkDataBlock = ArrayUtils.remove(checkDataBlock, i - currIdx);
                            currIdx++;
                        }
                        continue;
                    }

                    if (line.fixedPosition || (!line.fixedPosition && this.lines.length <= 1)) {
                        String lineSep = "";
                        if (line instanceof SimpleLine) {
                            SimpleLine sLine = (SimpleLine) line;
                            lineSep = sLine.separator;
                        } else if (line instanceof KeyValuePairLine) {
                            KeyValuePairLine kvpLine = (KeyValuePairLine) line;
                            lineSep = kvpLine.separator;
                        }

                        String[] splittedLine = inputDataContent[i].split(lineSep != "" ? lineSep : mainSeparatorSymbol);
                        for (int j = 0; j < splittedLine.length; j++) {
                            splittedLine[j] = splittedLine[j].trim();
                        }
                        this.identifiedDataBlock = splittedLine;

                        // TODO add for keyvalue==true to metadataGlobal + check for structureGroup and check for vocabularies and format
                        // this.metadataGlobal = this.metadataGlobal.concat([[line.vocabulary, splittedLine[1]]]);

                        dataBlock = Arrays.copyOfRange(metadataBlock, i + 1, metadataBlock.length);
                        metadataBlock = Arrays.copyOfRange(metadataBlock, 0, i + 1);
                        checkDataBlock = ArrayUtils.remove(checkDataBlock, i - currIdx);
                        currIdx++;
                    } else {
                        // TODO later check if fixedPosition not true
                    }
                }
            } else if (this.identifier instanceof KeyValuePairStructureIdentifier) {

                // TODO
                this.identifiedDataBlock = new String[0];
            }
        }
    }

}

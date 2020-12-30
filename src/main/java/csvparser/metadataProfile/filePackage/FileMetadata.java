package csvparser.metadataProfile.filePackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import csvparser.metadataProfile.structureGroupPackage.IStructure;
import csvparser.metadataProfile.structureGroupPackage.IterationStructureList;
import csvparser.metadataProfile.structureGroupPackage.SimpleStructure;
import csvparser.metadataProfile.types.SourceType;
import csvparser.metadataProfile.types.StringType;
import csvparser.metadataProfile.vocabularyTermsPackage.VocabularyTerm;
import csvparser.output.OutputKeyValue;

public class FileMetadata {
    /* file name */
    public String fileNameLiteral;
    public IterationStructureList[] fileNameStructureGroup;
    public Boolean fileNameMultipleFiles;
    /* file path */
    public String filePath;
    public SourceType filePathType;

    public FileMetadata(String fileNameLiteral, IterationStructureList[] fileNameStructureGroup,
            Boolean fileNameMultipleFiles) {
        this.fileNameLiteral = fileNameLiteral;
        this.fileNameStructureGroup = fileNameStructureGroup;
        this.fileNameMultipleFiles = fileNameMultipleFiles;
    }

    public FileMetadata(String fileNameLiteral, IterationStructureList[] fileNameStructureGroup, Boolean fileNameMultipleFiles, String filePath, SourceType filePathType) {
        this.fileNameLiteral = fileNameLiteral;
        this.fileNameStructureGroup = fileNameStructureGroup;
        this.fileNameMultipleFiles = fileNameMultipleFiles;
        this.filePath = filePath;
        this.filePathType = filePathType;
    }

    public OutputKeyValue[] parseOutputFileNameStructure() {
        if (this.fileNameStructureGroup.length > 0) {
            IterationStructureList[] sg = this.fileNameStructureGroup;
            String literal = this.fileNameLiteral;

            List<OutputKeyValue> outputKV = new ArrayList<OutputKeyValue>();
            VocabularyTerm[] vocabularies = new VocabularyTerm[0];

            String vocabMain = "vocabularyTermMain";

            outputKV = this.solveStructureGroup(sg, literal, outputKV, vocabularies, vocabMain);

            OutputKeyValue[] res = outputKV.toArray(new OutputKeyValue[outputKV.size()]);
            return res;
        } else {
            return new OutputKeyValue[0];
        }
    }

    private List<OutputKeyValue> solveStructureGroup(IterationStructureList[] inputSg, String inputLiteral, List<OutputKeyValue> inputRes, VocabularyTerm[] inputVocabularies, String vocabMain) {

        if (inputLiteral.equals("")) {
			return inputRes;
		}

		if (inputSg.length == 1 && inputSg[0].structure.length == 1) {
            try {
                // only one structure element exists - take rest of dataEl and put together with structure element
                if (inputSg[0].structure[0].getClass() == SimpleStructure.class) {
                    SimpleStructure struc = (SimpleStructure) inputSg[0].structure[0];
                    if (struc.type.equals(StringType.Vocabulary)) {
                        inputRes.add(new OutputKeyValue(struc.string, inputLiteral));
                    } else {
                        inputRes.add(new OutputKeyValue(vocabMain, inputLiteral));
                    }
                }
            } catch (Exception e) {
                System.out.println("Error");
                e.printStackTrace();
            }
            return inputRes;
		} else {

            
            // check if literal anywhere in structuregroup
            Integer literalIdxGroup = -1;
            Integer literalIdxStructure = -1;
            try {
                for (int i = 0; i < inputSg.length; i++) {
                    IterationStructureList sgEl = inputSg[i];
                    for (int j = 0; j < sgEl.structure.length; j++) {
                        if (sgEl.structure[j].getClass() == SimpleStructure.class) {
                            SimpleStructure struc = (SimpleStructure) sgEl.structure[j];
                            if (struc.type == StringType.Literal) {
                                literalIdxStructure = j;
                                literalIdxGroup = i;
                                break;
                            }
                        }
                    }
                    if (literalIdxStructure >= 0) {
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error");
                e.printStackTrace();
            }

			if (literalIdxGroup >= 0) {
                IStructure[] literalObjStructure = inputSg[literalIdxGroup].structure;
                
                if (literalObjStructure[literalIdxStructure].getClass() == SimpleStructure.class) {
                    SimpleStructure litObjStruc = (SimpleStructure) literalObjStructure[literalIdxStructure];

                    if (litObjStruc.type == StringType.Literal) {
                        if (inputLiteral.contains(litObjStruc.string)) {

                            try {
                                // String[] splitRes = inputLiteral.split(litObjStruc.string);
                                String[] splitRes = this.splitString(inputLiteral, litObjStruc.string);
    
                                String stringFront = splitRes[0].trim();
                                String stringBack = splitRes[1].trim();
    
                                // slice structure obj with literal from structureGroup
                                IStructure[] structureFront = Arrays.copyOfRange(inputSg[literalIdxGroup].structure, 0, literalIdxStructure);
                                IStructure[] structureBack = Arrays.copyOfRange(inputSg[literalIdxGroup].structure, literalIdxStructure + 1, inputSg[literalIdxGroup].structure.length);                        
                                // splice

                                ArrayList<IterationStructureList> clonedInputSgListFront = this.cloneArrayOfObjects(inputSg);
                                ArrayList<IterationStructureList> clonedInputSgListBack = this.cloneArrayOfObjects(inputSg);

                                IterationStructureList[] structureGroupFront = clonedInputSgListFront.toArray(new IterationStructureList[clonedInputSgListFront.size()]);
                                IterationStructureList[] structureGroupBack = clonedInputSgListBack.toArray(new IterationStructureList[clonedInputSgListBack.size()]);
    
                                if (literalIdxGroup < 1) {
                                    // front without previously choosen literal
                                    structureGroupFront[literalIdxGroup].setStructure(structureFront); // = structureFront;
                                    // back with previously choosen literal
                                    structureGroupBack[literalIdxGroup].setStructure(structureBack);
    
                                    List<OutputKeyValue> resFront = this.solveStructureGroup(structureGroupFront, stringFront, inputRes, inputVocabularies, vocabMain);
                                    List<OutputKeyValue> resBack = this.solveStructureGroup(structureGroupBack, stringBack, inputRes, inputVocabularies, vocabMain);
                                } else {
                                    // front without previously choosen literal
                                    if (structureFront.length > 0) {
                                        // cut structure where literal was found, if structure front still has vocabulary
                                        structureGroupFront = Arrays.copyOfRange(inputSg, 0, literalIdxGroup + 1);
                                        structureGroupFront[literalIdxGroup].setStructure(structureFront);
                                    } else {
                                        // cut group from structure obj where literal was found
                                        structureGroupFront = Arrays.copyOfRange(inputSg, 0, literalIdxGroup);
                                    }
                                    // back with previously choosen literal
                                    structureGroupBack = Arrays.copyOfRange(inputSg, literalIdxGroup, inputSg.length);
                                    structureGroupBack[0].setStructure(structureBack);
    
                                    List<OutputKeyValue> resFront = this.solveStructureGroup(structureGroupFront, stringFront, inputRes, inputVocabularies, vocabMain);
                                    List<OutputKeyValue> resBack = this.solveStructureGroup(structureGroupBack, stringBack, inputRes, inputVocabularies, vocabMain);
                                }
                            } catch (Exception e) {
                                System.out.println("Error");
                                e.printStackTrace();
                            }
                            return inputRes;
                        } else {
                            try {
                                // because the literal does not exist, structure element will be cut from structureGroups
                                // splice
                                inputSg = Arrays.copyOfRange(inputSg, literalIdxGroup + 1, inputSg.length);
                                List<OutputKeyValue> resNew = this.solveStructureGroup(inputSg, inputLiteral.trim(), inputRes, inputVocabularies, vocabMain);
                            } catch (Exception e) {
                                System.out.println("Error");
                                e.printStackTrace();
                            }
                            return inputRes;
                        }
                    } else {
                        System.out.println("should not be matched");
                        return inputRes;
                    }
                } else {
                    System.out.println("should not be matched");
                    return inputRes;
                }
			} else {
                // TODO later 
                System.out.println("vocabularies matching");
				return inputRes;
			}
		}
    }

    private String[] splitString(String originString, String splitString) {
        if (originString.endsWith(splitString)) {
            String[] res = {originString.split(splitString)[0], ""};
            return res;
        } else {
            return originString.split(splitString, 2);
        }
    }

    private ArrayList<IterationStructureList> cloneArrayOfObjects(IterationStructureList[] arrayOfObjects) {
        try {
            ArrayList<IterationStructureList> list = new ArrayList<IterationStructureList>();
            for (IterationStructureList el: arrayOfObjects) {
                list.add(new IterationStructureList(el));
            }
            return list;
        } catch (Exception e) {
            System.out.println("Error in clone array of objects");
            e.printStackTrace();
        }
        return new ArrayList<IterationStructureList>();
    }

}

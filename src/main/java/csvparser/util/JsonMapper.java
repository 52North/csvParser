package csvparser.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import csvparser.metadataProfile.DataConfiguration;
import csvparser.metadataProfile.filePackage.FileMetadata;
import csvparser.metadataProfile.identifierPackage.AbstractLineIdentifier;
import csvparser.metadataProfile.identifierPackage.EndIdentifier;
import csvparser.metadataProfile.identifierPackage.AbstractStructureIdentifier;
import csvparser.metadataProfile.identifierPackage.KeyValuePairStructureIdentifier;
import csvparser.metadataProfile.identifierPackage.SimpleStructureIdentifier;
import csvparser.metadataProfile.identifierPackage.StartEndIdentifier;
import csvparser.metadataProfile.identifierPackage.StartIdentifier;
import csvparser.metadataProfile.linesPackage.AbstractLinesParent;
import csvparser.metadataProfile.linesPackage.Block;
import csvparser.metadataProfile.linesPackage.FirstField;
import csvparser.metadataProfile.linesPackage.KeyFirstField;
import csvparser.metadataProfile.linesPackage.KeyValuePairLine;
import csvparser.metadataProfile.linesPackage.SimpleLine;
import csvparser.metadataProfile.linesPackage.SkipLine;
import csvparser.metadataProfile.linesPackage.Value;
import csvparser.metadataProfile.metadataPackage.AbstractMetadataStructure;
import csvparser.metadataProfile.metadataPackage.KeyValuePairStructure;
import csvparser.metadataProfile.metadataPackage.SimpleFieldStructure;
import csvparser.metadataProfile.structureGroupPackage.IStructure;
import csvparser.metadataProfile.structureGroupPackage.IterationStructure;
import csvparser.metadataProfile.structureGroupPackage.IterationStructureList;
import csvparser.metadataProfile.structureGroupPackage.SimpleStructure;
import csvparser.metadataProfile.structureGroupPackage.SimpleStructureList;
import csvparser.metadataProfile.trimPackage.AbstractTrimmer;
import csvparser.metadataProfile.trimPackage.IndexLiteralTrimmer;
import csvparser.metadataProfile.trimPackage.LineByIdxTrimmer;
import csvparser.metadataProfile.trimPackage.LineyByLiteralTrimmer;
import csvparser.metadataProfile.types.LineType;
import csvparser.metadataProfile.types.LinkNumberType;
import csvparser.metadataProfile.types.StringLocation;
import csvparser.metadataProfile.types.StringType;
import csvparser.metadataProfile.vocabularyTermsPackage.Classification;
import csvparser.metadataProfile.vocabularyTermsPackage.DataStructure;
import csvparser.metadataProfile.vocabularyTermsPackage.MetadataCategory;
import csvparser.metadataProfile.vocabularyTermsPackage.VocabularyTerm;

public class JsonMapper {

    public static DataConfiguration mapConfigFromJson(String input, String nameDataFile) {
        JSONObject configObject = new JSONObject(input);

        /* file name */
        FileMetadata fileMetadata = null;
        /* special characters */
        String separatorSymbol = null;
        /* trim */
        AbstractTrimmer[] trimmers = null;
        /* metadata */
        AbstractMetadataStructure[] metadata = null;
        /* data */
        AbstractMetadataStructure[] data = null;
        /* vocabulary terms */
        VocabularyTerm[] vocabularyTerms = null;
        /* category */
        MetadataCategory[] category = null;

        try {

            /* parse file object */
            if (configObject.has("file") && configObject.getJSONObject("file").has("name")) {
                String fileNameLiteral = "";
                Boolean fileNameMultipleFiles = false;
                IterationStructureList[] fileNameStructureGroup = new IterationStructureList[0];
                
                JSONObject cfgFileName = configObject.getJSONObject("file").getJSONObject("name");
                if (cfgFileName.has("literal")) {
                    fileNameLiteral = cfgFileName.getString("literal");
                }
                if (cfgFileName.has("multipleFiles")) {
                    fileNameMultipleFiles = cfgFileName.getBoolean("multipleFiles");
                }
                if (cfgFileName.has("structureGroup")) {
                    JSONArray cfgFileStructureGroup = cfgFileName.getJSONArray("structureGroup");
                    IterationStructureList[] fnsg = parseIterationStructureList(cfgFileStructureGroup);
                    fileNameStructureGroup = fnsg;
                }
    
                if (fileNameLiteral.equals("")) {
                    fileNameLiteral = nameDataFile;
                }
    
                fileMetadata = new FileMetadata(fileNameLiteral, fileNameStructureGroup, fileNameMultipleFiles);
            }
    
            /* parse trim array */
            if (configObject.has("trim")) {
                JSONArray cfgTrim = configObject.getJSONArray("trim");
                if (cfgTrim.length() > 0) {
                    List<AbstractTrimmer> trimmerList = new ArrayList<AbstractTrimmer>();
                    Iterator<Object> cfgTrimIterator = cfgTrim.iterator();
                    while (cfgTrimIterator.hasNext()) {
                        JSONObject cfgTrimObj = (JSONObject) cfgTrimIterator.next();
    
                        /* IndexLiteralTrimmer vs LineTrimmer */
                        if (cfgTrimObj.has("what") && cfgTrimObj.has("where") && cfgTrimObj.has("idx") && cfgTrimObj.has("literal")) {
                            /* IndexLiteralTrimmer */
                            trimmerList.add(new IndexLiteralTrimmer(cfgTrimObj));
                        } else if (cfgTrimObj.has("what") && cfgTrimObj.has("idx")) {
                            /* LineByIdxTrimmer */
                            trimmerList.add(new LineByIdxTrimmer(cfgTrimObj));
                        } else if (cfgTrimObj.has("what") && cfgTrimObj.has("literal")) {
                            /* LineByIdxTrimmer */
                            trimmerList.add(new LineyByLiteralTrimmer(cfgTrimObj));
                        } else {
                            // not implemented
                        }
                    }
                    trimmers = trimmerList.toArray(new AbstractTrimmer[trimmerList.size()]);
                } else {
                    trimmers = new AbstractTrimmer[0];
                }
            }
    
            /* parse specialCharacters object */
            if (configObject.has("specialCharacters")) {
                JSONObject cfgSpecialCharacters = configObject.getJSONObject("specialCharacters");
                if (cfgSpecialCharacters.has("separator")) {
                    separatorSymbol = cfgSpecialCharacters.getString("separator");
                    // TODO later - add quotation mark, escape mark
                }
            }
    
            /* parse metadata array */
            if (configObject.has("metadata")) {
                JSONArray cfgMetadata = configObject.getJSONArray("metadata");
                metadata = parseAbstractMetadataStructure(cfgMetadata);
            }
    
            // /* parse data array */
            if (configObject.has("data")) {
                JSONArray cfgData = configObject.getJSONArray("data");
                data = parseAbstractMetadataStructure(cfgData);
            }
    
            /* parse vocabularyTerms array */
            if (configObject.has("vocabularyTerms")) {
                JSONArray cfgVocabularyTerms = configObject.getJSONArray("vocabularyTerms");
                vocabularyTerms = parseVocabularyTerms(cfgVocabularyTerms);
            }
    
            /* parse category array */
            if (configObject.has("category")) {
                JSONArray cfgCategory = configObject.getJSONArray("category");
                category = parseCategory(cfgCategory);
            }
        } catch (Exception e) {
            System.out.println("Error: creating DataConfiguration.");
            e.printStackTrace();
        }


        return new DataConfiguration(fileMetadata, separatorSymbol, trimmers, metadata, data, vocabularyTerms, category);
    }



    /**
     * Parse metadata and data object of JSON configuration file.
     * @param cfgMetadata
     * @return
     */
    private static AbstractMetadataStructure[] parseAbstractMetadataStructure(JSONArray cfgMetadata) {
        if (cfgMetadata.length() > 0) {
            List<AbstractMetadataStructure> metadataList = new ArrayList<AbstractMetadataStructure>();

            Iterator<Object> cfgMetadataIterator = cfgMetadata.iterator();
            while (cfgMetadataIterator.hasNext()) {
                JSONObject cfgMetadataObj = (JSONObject) cfgMetadataIterator.next();

                String metadataObjSeparator = cfgMetadataObj.getString("separator");
                LineType metadataObjLine = LineType.valueOf(cfgMetadataObj.getString("line"));
                Boolean metadataObjFixedPosition = cfgMetadataObj.getBoolean("fixedPosition");
                Boolean metadataObjKeyvalue = cfgMetadataObj.getBoolean("keyvalue");

                JSONObject cfgMetadataObjIdentifier = cfgMetadataObj.getJSONObject("identifier");

                /* KeyValuePairStructure vs SimpleFieldStructure */
                /* identifier */
                AbstractStructureIdentifier metadataObjIdentifier = null;
                if (cfgMetadataObjIdentifier.has("position")) {
                    /* identifier - KeyValuePairStructureIdentifier */

                    StringLocation cfgMetadataObjIdentifierPosition = StringLocation.valueOf(cfgMetadataObjIdentifier.getString("position"));
                    Boolean cfgMetadataObjIdentifierConnected = cfgMetadataObjIdentifier.getBoolean("connected");
                    String cfgMetadataObjIdentifierLiteral = cfgMetadataObjIdentifier.getString("literal");

                    // add idx
                    if (cfgMetadataObjIdentifier.has("idx")) {
                        AbstractLineIdentifier metadataObjIdentifierIdx = parseAbstractLineIdentifier(cfgMetadataObjIdentifier);
                        metadataObjIdentifier = new KeyValuePairStructureIdentifier(cfgMetadataObjIdentifierPosition, cfgMetadataObjIdentifierConnected, cfgMetadataObjIdentifierLiteral, metadataObjIdentifierIdx);
                    } else {
                        metadataObjIdentifier = new KeyValuePairStructureIdentifier(cfgMetadataObjIdentifierPosition, cfgMetadataObjIdentifierConnected, cfgMetadataObjIdentifierLiteral);
                    }
                    // TODO later - add object ignore to KeyValuePairStructureIdentifier

                } else {
                    /* identifier - SimpleStructureIdentifier */
                    AbstractLineIdentifier metadataObjIdentifierIdx = parseAbstractLineIdentifier(cfgMetadataObjIdentifier);
                    metadataObjIdentifier = new SimpleStructureIdentifier(metadataObjIdentifierIdx);
                }
                
                /* SimpleFieldStructure - lines */
                JSONArray cfgMetadataLinesObjects = cfgMetadataObj.getJSONArray("lines");
                List<AbstractLinesParent> linesList = new ArrayList<AbstractLinesParent>();
                Iterator<Object> cfgMetadataLinesObjectsIterator = cfgMetadataLinesObjects.iterator();

                while (cfgMetadataLinesObjectsIterator.hasNext()) {
                    JSONObject cfgMetadataLine = (JSONObject) cfgMetadataLinesObjectsIterator.next();
                    /* SkipLine ? */
                    if (cfgMetadataLine.getBoolean("skip")) {
                        /* SkipLine */
                        SkipLine skipLine = new SkipLine();
                        linesList.add(skipLine);
                    } else {
                        Boolean lineObjFixedPosition = cfgMetadataLine.getBoolean("fixedPosition");
                        String lineObjSeparator = cfgMetadataLine.getString("separator");
                        Boolean lineObjHeader = cfgMetadataLine.getBoolean("header");
                        String lineObjVocabulary = (cfgMetadataLine.has("vocabulary") ? cfgMetadataLine.getString("vocabulary") : null);

                        JSONObject cfgLineObjValueObj = cfgMetadataLine.getJSONObject("value");
                        Value lineObjValue;
                        if (cfgLineObjValueObj.has("format")) {
                            JSONObject cfgLineObjFormatObj = cfgLineObjValueObj.getJSONObject("format");
                            // TODO later - add format to value
                        }

                        if (cfgLineObjValueObj.has("structureGroup")) {
                            JSONArray cfgLineObjValueObjStructureGroup = cfgLineObjValueObj.getJSONArray("structureGroup");
                            SimpleStructureList[] lineObjValueStructureGroup = parseSimpleStructureList(cfgLineObjValueObjStructureGroup);
    
                            lineObjValue = new Value(lineObjValueStructureGroup); // new Value(lineObjValueStructureGroup, cfgLineObjFormatObj);
                        } else {
                            lineObjValue = new Value();
                        }


                        JSONArray cfgLineObjVocabularyTerms = cfgMetadataLine.getJSONArray("vocabularyTerms");
                        JSONArray cfgLineObjCategory = cfgMetadataLine.getJSONArray("category");
                        JSONArray cfgLineObjClassification = cfgMetadataLine.getJSONArray("classification");

                        VocabularyTerm[] lineObjVocabularyTerms = parseVocabularyTerms(cfgLineObjVocabularyTerms);
                        MetadataCategory[] lineObjCategory = parseCategory(cfgLineObjCategory);
                        Classification[] lineObjClassification = parseClassification(cfgLineObjClassification);

                        /* KeyValuePairLine vs SimpleLine */
                        if (metadataObjKeyvalue) {
                            /* KeyValuePairLine */
                            /* parse Key object */
                            JSONObject cfgLineObjKeyObj = cfgMetadataLine.getJSONObject("key");
                            JSONObject cfgLineObjKeyObjBlock = cfgLineObjKeyObj.getJSONObject("block");
                            Boolean lineObjKeyObjBlockMultipleLines = cfgLineObjKeyObjBlock.getBoolean("multipleLines");
                            Block lineObjKeyObjBlock = new Block(lineObjKeyObjBlockMultipleLines, false);
                            if (cfgLineObjKeyObjBlock.has("connected")) {
                                Boolean lineObjKeyObjBlockConnected = cfgLineObjKeyObjBlock.getBoolean("connected");
                                lineObjKeyObjBlock = new Block(lineObjKeyObjBlockMultipleLines, lineObjKeyObjBlockConnected);
                            }

                            JSONArray cfgLineObjKeyObjStructureGroup = cfgLineObjKeyObj.getJSONArray("structureGroup");
                            IterationStructureList[] lineObjKeyObjStructureGroup = parseIterationStructureList(cfgLineObjKeyObjStructureGroup);

                            KeyFirstField lineObjKey = new KeyFirstField(lineObjKeyObjBlock, lineObjKeyObjStructureGroup);
                            
                            KeyValuePairLine kvpLine = new KeyValuePairLine(lineObjFixedPosition, lineObjSeparator, lineObjVocabulary, lineObjHeader, lineObjValue, lineObjCategory, lineObjClassification, lineObjVocabularyTerms, lineObjKey);
                            linesList.add(kvpLine);
                        } else {
                            /* SimpleLine */
                            /* parse FirstField object */
                            JSONObject cfgLineObjFirstFieldObj = cfgMetadataLine.getJSONObject("firstField");
                            Boolean lineObjFirstFieldObjDifferent = cfgLineObjFirstFieldObj.getBoolean("different");

                            FirstField lineObjFirstField = null;
                            if (lineObjFirstFieldObjDifferent) {
                                Boolean lineObjFirstFieldObjTitle = cfgLineObjFirstFieldObj.getBoolean("title");
                                LineType lineObjFirstFieldObjTitleScope = LineType.valueOf(cfgLineObjFirstFieldObj.getString("titleScope"));

                                JSONArray cfgLineObjFirstFieldObjStructureGroup = cfgLineObjFirstFieldObj.getJSONArray("structureGroup");
                                SimpleStructureList[] lineObjFirstFieldObjStructureGroup = parseSimpleStructureList(cfgLineObjFirstFieldObjStructureGroup);
            
                                lineObjFirstField = new FirstField(lineObjFirstFieldObjTitle, lineObjFirstFieldObjTitleScope, lineObjFirstFieldObjStructureGroup);
                            } else {
                                lineObjFirstField = new FirstField();
                            }

                            SimpleLine sLine = new SimpleLine(lineObjFixedPosition, lineObjSeparator, lineObjVocabulary, lineObjHeader, lineObjValue,  lineObjCategory, lineObjClassification, lineObjVocabularyTerms, lineObjFirstField);
                            linesList.add(sLine);
                        }
                    }
                }
                AbstractLinesParent[] metadataObjLines = linesList.toArray(new AbstractLinesParent[linesList.size()]);
                if (metadataObjKeyvalue) {
                    KeyValuePairStructure kvpStructure = new KeyValuePairStructure(metadataObjSeparator, metadataObjLine, metadataObjFixedPosition, metadataObjLines, metadataObjIdentifier);
                    metadataList.add(kvpStructure);
                } else {
                    SimpleFieldStructure sfStructure = new SimpleFieldStructure(metadataObjSeparator, metadataObjLine, metadataObjFixedPosition, metadataObjLines, metadataObjIdentifier);
                    metadataList.add(sfStructure);
                }
            }
            return metadataList.toArray(new AbstractMetadataStructure[metadataList.size()]);
        } else {
            return new AbstractMetadataStructure[0];
        }
    }

    /**
     * Parse line identifier.
     * @param cfgMetadataObjIdentifier
     * @return
     */
    private static AbstractLineIdentifier parseAbstractLineIdentifier(JSONObject cfgMetadataObjIdentifier) {
        JSONObject cfgMetadataObjIdentifierIdx = cfgMetadataObjIdentifier.getJSONObject("idx");
        if (cfgMetadataObjIdentifierIdx.has("start") && cfgMetadataObjIdentifierIdx.has("end")) {
            Integer cfgMetadataObjIdentifierIdxStart = cfgMetadataObjIdentifierIdx.getInt("start");
            Integer cfgMetadataObjIdentifierIdxEnd = cfgMetadataObjIdentifierIdx.getInt("end");
            return new StartEndIdentifier(cfgMetadataObjIdentifierIdxStart, cfgMetadataObjIdentifierIdxEnd);
        } else if (cfgMetadataObjIdentifierIdx.has("start")) {
            Integer cfgMetadataObjIdentifierIdxStart = cfgMetadataObjIdentifierIdx.getInt("start");
            return new StartIdentifier(cfgMetadataObjIdentifierIdxStart);
        } else if (cfgMetadataObjIdentifierIdx.has("end")) {
            Integer cfgMetadataObjIdentifierIdxEnd = cfgMetadataObjIdentifierIdx.getInt("end");
            return new EndIdentifier(cfgMetadataObjIdentifierIdxEnd);
        } else {
            return null;
        }
    }

    /**
     * Parse VocabularyTerms object.
     * @param jsonArray
     * @return
     */
    private static VocabularyTerm[] parseVocabularyTerms(JSONArray jsonArray) {
        if (jsonArray.length() > 0) {
            List<VocabularyTerm> vocabularyTermList = new ArrayList<VocabularyTerm>();

            Iterator<Object> cfgVocabTermIterator = jsonArray.iterator();
            while (cfgVocabTermIterator.hasNext()) {
                JSONObject cfgVocabTermObj = (JSONObject) cfgVocabTermIterator.next();

                String vocabTermLiteral = cfgVocabTermObj.getString("literal");
                String vocabTermVocabulary = cfgVocabTermObj.getString("vocabulary");
                String vocabTermVocabularyTerm = cfgVocabTermObj.getString("vocabularyTerm");

                if (cfgVocabTermObj.has("data")) {
                    JSONObject cfgVocabTermData = cfgVocabTermObj.getJSONObject("data");
                    Boolean vocabTermDataHeader = cfgVocabTermData.getBoolean("header");
                    DataStructure vocabTermData = new DataStructure(vocabTermDataHeader);
                    // TODO later - add classification, category, format to DataStructure (use functions parseClassification, parseCategory)

                    VocabularyTerm vocabTerm = new VocabularyTerm(vocabTermVocabularyTerm, vocabTermVocabulary, vocabTermLiteral, vocabTermData);
                    vocabularyTermList.add(vocabTerm);
                } else {
                    VocabularyTerm vocabTerm = new VocabularyTerm(vocabTermVocabularyTerm, vocabTermVocabulary, vocabTermLiteral);
                    vocabularyTermList.add(vocabTerm);
                }
            }
            return vocabularyTermList.toArray(new VocabularyTerm[vocabularyTermList.size()]);
        } else {
            return new VocabularyTerm[0];
        }
    }
    
    /**
     * Parse Category object.
     * @param jsonArray
     * @return
     */
    private static MetadataCategory[] parseCategory(JSONArray jsonArray) {
        if (jsonArray.length() > 0) {
            List<MetadataCategory> categoryList = new ArrayList<MetadataCategory>();
            Iterator<Object> cfgCategoryIterator = jsonArray.iterator();
            while (cfgCategoryIterator.hasNext()) {
                JSONObject cfgCategoryObj = (JSONObject) cfgCategoryIterator.next();

                String categoryLiteral = cfgCategoryObj.getString("literal");
                String categoryContent = cfgCategoryObj.getString("content");
                if (cfgCategoryObj.has("vocabularyTerm")) {
                    String categoryVocabularyTerm = cfgCategoryObj.getString("vocabularyTerm");
                    MetadataCategory category = new MetadataCategory(categoryLiteral, categoryContent, categoryVocabularyTerm);
                    categoryList.add(category);
                } else {
                    MetadataCategory category = new MetadataCategory(categoryLiteral, categoryContent);
                    categoryList.add(category);
                }
            }
            return categoryList.toArray(new MetadataCategory[categoryList.size()]);
        } else {
            return new MetadataCategory[0];
        }
    }

    /**
     * Parse Classification object.
     * @param jsonArray
     * @return
     */
    private static Classification[] parseClassification(JSONArray jsonArray) {
        if (jsonArray.length() > 0) {
            List<Classification> classificationList = new ArrayList<Classification>();
            Iterator<Object> cfgClassificationIterator = jsonArray.iterator();
            while (cfgClassificationIterator.hasNext()) {
                JSONObject cfgClassificationObj = (JSONObject) cfgClassificationIterator.next();

                String classificationLiteral = cfgClassificationObj.getString("literal");
                String classificationVocabularyTerm = cfgClassificationObj.getString("vocabularyTerm");
                String classificationVocabulary = cfgClassificationObj.getString("vocabulary");
                Classification classification = new Classification(classificationLiteral, classificationVocabularyTerm, classificationVocabulary);
                classificationList.add(classification);
            }
            return classificationList.toArray(new Classification[classificationList.size()]);
        } else {
            return new Classification[0];
        }
    }

    /**
     * Parse SimpleStructureList object.
     * @param jsonArray
     * @return
     */
    private static SimpleStructureList[] parseSimpleStructureList(JSONArray jsonArray) {
        try {
            ArrayList<SimpleStructureList> list = new ArrayList<SimpleStructureList>();
            Iterator<Object> cfgIterator = jsonArray.iterator();
            while (cfgIterator.hasNext()) {
                JSONObject cfgObj = (JSONObject) cfgIterator.next();
                Boolean objOptional = cfgObj.getBoolean("optional");
                JSONArray cfgSGArray = cfgObj.getJSONArray("structure");
                SimpleStructure[] simpleSArray = parseSimpleStructure(cfgSGArray);
                list.add(new SimpleStructureList(objOptional, simpleSArray));
            }
            SimpleStructureList[] simpleSListArray = list.toArray(new SimpleStructureList[list.size()]);
            return simpleSListArray;

        } catch (Exception e) {
            System.out.println("Error in parseSimpleStructureList");
            e.printStackTrace();
        }

        return new SimpleStructureList[0];
    }

    /**
     * Parse IterationStructureList object.
     * @param jsonArray
     * @return
     */
    private static IterationStructureList[] parseIterationStructureList(JSONArray jsonArray) {
        try {
            ArrayList<IterationStructureList> list = new ArrayList<IterationStructureList>();
            Iterator<Object> cfgIterator = jsonArray.iterator();
            while (cfgIterator.hasNext()) {
                JSONObject cfgObj = (JSONObject) cfgIterator.next();
                Boolean objOptional = cfgObj.getBoolean("optional");
                JSONArray cfgSGArray = cfgObj.getJSONArray("structure");

                IStructure[] iterationSArray = parseIStructure(cfgSGArray);
                list.add(new IterationStructureList(objOptional, iterationSArray));
            }
            IterationStructureList[] iterationSListArray = list.toArray(new IterationStructureList[list.size()]);
            return iterationSListArray;
        } catch (Exception e) {
            System.out.println("Error in parseIterationStructureList");
            e.printStackTrace();
        }
        return new IterationStructureList[0];
    }


    /**
     * Parse SimpleStructure object.
     * @param jsonArray
     * @return
     */
    private static SimpleStructure[] parseSimpleStructure(JSONArray jsonArray) {
        ArrayList<SimpleStructure> simpleSList = new ArrayList<SimpleStructure>();
        Iterator<Object> cfgSimpleStructureIterator = jsonArray.iterator();
        while (cfgSimpleStructureIterator.hasNext()) {
            JSONObject cfgSimpleStructureObj = (JSONObject) cfgSimpleStructureIterator.next();
            String simpleStructureString = cfgSimpleStructureObj.getString("string");
            StringType simpleStructureType = StringType.valueOf(cfgSimpleStructureObj.getString("type"));

            SimpleStructure simpleStructure = new SimpleStructure(simpleStructureString, simpleStructureType);
            simpleSList.add(simpleStructure);
        }
        SimpleStructure[] simpleSArray = simpleSList.toArray(new SimpleStructure[simpleSList.size()]);
        return simpleSArray;
    }

    /**
     * Parse IStructure object.
     * @param jsonArray
     * @return
     */
    private static IStructure[] parseIStructure(JSONArray jsonArray) {
        ArrayList<IStructure> sList = new ArrayList<IStructure>();
        Iterator<Object> cfgStructureIterator = jsonArray.iterator();
        while (cfgStructureIterator.hasNext()) {
            JSONObject cfgStructureObj = (JSONObject) cfgStructureIterator.next();
            if (cfgStructureObj.has("string")) {
                String simpleStructureString = cfgStructureObj.getString("string");
                StringType simpleStructureType = StringType.valueOf(cfgStructureObj.getString("type"));
    
                SimpleStructure simpleStructure = new SimpleStructure(simpleStructureString, simpleStructureType);
                sList.add(simpleStructure);
            } else if (cfgStructureObj.has("start")) {
                Integer iterationStructureStart = cfgStructureObj.getInt("start");
                LinkNumberType iterationStructureLink = LinkNumberType.valueOf(cfgStructureObj.getString("link"));
    
                IterationStructure iterationStructure = new IterationStructure(iterationStructureStart, iterationStructureLink);
                sList.add(iterationStructure);
            }

        }
        IStructure[] sArray = sList.toArray(new IStructure[sList.size()]);
        return sArray;
    }


    // private <T> ArrayList<T> parseStructureGroup(JSONArray jsonArray, Class<T> classKey) throws Exception {

    //     try {
    //         ArrayList<T> list = new ArrayList<T>();
    //         list.add(classKey.getConstructor().newInstance());
    //         return list;
    //     } catch (Exception e) {
    //         System.out.println("Error in parseStructureGroup");
    //         e.printStackTrace();
    //     }
    //     return new ArrayList<T>();
    // }
    
}

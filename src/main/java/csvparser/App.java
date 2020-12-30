package csvparser;

import java.util.ArrayList;

import csvparser.fileloader.FileLoader;
import csvparser.metadataProfile.DataConfiguration;
import csvparser.output.OutputKeyValue;
import csvparser.output.OutputMetadata;
import csvparser.util.JsonMapper;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {

        try {


            String nameDataFile = args[0];
            String nameConfigFile = args[1];
            int numberRow = Integer.parseInt(args[2]);
            int numberCol = Integer.parseInt(args[3]);

            System.out.println("Run fileparser with following inputs ...");
            System.out.println("data file:      " + nameDataFile);
            System.out.println("config file:    " + nameConfigFile);
            System.out.println("row nr:         " + numberRow);
            System.out.println("column nr:      " + numberCol);

            /* Read dataset as lines - String array */
            /* Read config file as single string - String */
            FileLoader loadedFiles = new FileLoader(nameDataFile, nameConfigFile);
            System.out.println("Files loaded.");
            // System.out.println(loadedFiles.contentDataFile);
            // System.out.println(loadedFiles.contentConfigFile);

            // ProfileJsonParser cfgContentString = new ProfileJsonParser(loadedFiles.contentConfigFile);

            DataConfiguration cfgContent = JsonMapper.mapConfigFromJson(loadedFiles.contentConfigFile, nameDataFile);
            // DataConfiguration cfgContent = new DataConfiguration(loadedFiles.contentConfigFile, nameDataFile);
            System.out.println("Configfile prepared.");

            /* parse filename */
            OutputKeyValue[] outputMetadataFile = cfgContent.fileMetadata.parseOutputFileNameStructure();


            /* separate fields in each line */
            // done by reading dataset
            String dataSeparator = cfgContent.separatorSymbol;

            /* start trimming by each trim object */
            String[] dataContent = cfgContent.trimDataString(loadedFiles.contentDataFile);

            /* identify metadata blocks */


            /* parse metadata blocks */
            /* parse data blocks */
            /* submit numbers - get measurement with metadata */


            /* Run parsing */
            // OutputMetadata[] output = parser.parse(contentDataset, contentConfig);

            OutputKeyValue[] outputMetadataColumn = new OutputKeyValue[0];
            OutputKeyValue[] outputMetadataGlobal = new OutputKeyValue[0];

            OutputMetadata outputSelectedMeasurement = new OutputMetadata(outputMetadataFile, outputMetadataColumn, outputMetadataGlobal);

            System.out.println("Output generated.");
            System.out.println(outputSelectedMeasurement);

        
        } catch (Exception e) {
            System.out.println("Parser failed.");
            System.out.println(e);
        }

    }
    
    public static <T> ArrayList<T> cloneArrayOfObjects(Class<T> classKey, T[] arrayOfObjects) {
        try {
            ArrayList<T> list = new ArrayList<T>();
            for (T el: arrayOfObjects) {
                list.add(classKey.getConstructor().newInstance(el));
            }
            return list;
        } catch (Exception e) {
            System.out.println("Error in clone array of objects");
            e.printStackTrace();
        }
        return new ArrayList<T>();
    }

}

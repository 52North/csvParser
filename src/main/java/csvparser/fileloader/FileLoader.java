package csvparser.fileloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileLoader {

    public String filepathDataset;
    public String filepathConfigFile;
    public String[] contentDataFile;
    public String contentConfigFile;

    private String staticPath =  "./examplefiles/";

    public FileLoader(String filenameDataset, String filenameConfigFile) {
        // add static path to filename
        this.filepathDataset = staticPath + filenameDataset;
        this.filepathConfigFile = staticPath + filenameConfigFile;
        this.contentDataFile = readCSV(this.filepathDataset);
        this.contentConfigFile = readJSON(this.filepathConfigFile);
    }

    /**
     * Load CSV file
     * 
     * @param filename
     * @return
     */
    private static String[] readCSV(String filename) {

        try {
            File myObj = new File(filename);

            if (myObj.exists()) {
                System.out.println("File name: " + myObj.getName());
                // System.out.println("Absolute path: " + myObj.getAbsolutePath());
                // System.out.println("Writeable: " + myObj.canWrite());
                // System.out.println("Readable " + myObj.canRead());
                // System.out.println("File size in bytes " + myObj.length());
            } else {
                System.out.println("The file does not exist.");
            }

            Scanner myReader = new Scanner(myObj);
            List<String> fileContentList = new ArrayList<String>();

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                fileContentList.add(data);
            }
            myReader.close();

            String[] fileContent = fileContentList.toArray(new String[fileContentList.size()]);

            return fileContent;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return new String[0];
    }

    /**
     * Load JSON file
     * 
     * @param filename
     * @return
     */
    private static String readJSON(String filename) {

        try {
            File myObj = new File(filename);

            if (myObj.exists()) {
                System.out.println("File name: " + myObj.getName());
                // System.out.println("Absolute path: " + myObj.getAbsolutePath());
                // System.out.println("Writeable: " + myObj.canWrite());
                // System.out.println("Readable " + myObj.canRead());
                // System.out.println("File size in bytes " + myObj.length());
            } else {
                System.out.println("The file does not exist.");
            }
        } catch (Exception e) {
            System.out.println("Error: While loading file.");
            e.printStackTrace();
        }

        String jsonData = "";
        BufferedReader br = null;

        try {
            String line;
            br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null) {
                jsonData += line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return jsonData;
    }
}

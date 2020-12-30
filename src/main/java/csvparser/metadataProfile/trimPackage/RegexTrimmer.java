package csvparser.metadataProfile.trimPackage;

import csvparser.metadataProfile.types.TrimValueType;

public class RegexTrimmer extends AbstractTrimmer {
    public String regexCmd;

    public RegexTrimmer(TrimValueType what,
            String regexCmd) {
        super(what);
        this.regexCmd = regexCmd;
    }

    public String[] trimString(String[] input, Integer counter) {
        System.out.println("TODO - RegexTrimmer");
        return input;
    }

}

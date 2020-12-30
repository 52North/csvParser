package csvparser.metadataProfile.trimPackage;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;

import csvparser.metadataProfile.types.TrimValueType;

public class LineyByLiteralTrimmer extends AbstractTrimmer {
    public String literal;

    public LineyByLiteralTrimmer(JSONObject jsonObj) {
        super(TrimValueType.valueOf(jsonObj.getString("what")));
        this.literal = jsonObj.getString("literal");
    }

    public LineyByLiteralTrimmer(TrimValueType what, String literal) {
        super(what);
        this.literal = literal;
    }

    public String[] trimString(String[] input, Integer counter) {
        if (this.what.equals(TrimValueType.Row)) {
            Integer idx = ArrayUtils.indexOf(input, this.literal);
            String[] newRes = ArrayUtils.remove(input, idx);
            return newRes;
        }
        // TODO for TrimvalueType.Column
        return input;
    }
}

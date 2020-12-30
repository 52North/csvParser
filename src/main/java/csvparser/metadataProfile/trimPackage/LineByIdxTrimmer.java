package csvparser.metadataProfile.trimPackage;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;

import csvparser.metadataProfile.types.TrimValueType;

public class LineByIdxTrimmer extends AbstractTrimmer {
    public Integer idx;

    public LineByIdxTrimmer(JSONObject jsonObj) {
        super(TrimValueType.valueOf(jsonObj.getString("what")));
        this.idx = jsonObj.getInt("idx");
    }

    public LineByIdxTrimmer(TrimValueType what, Integer idx) {
        super(what);
        this.idx = idx;
    }

    public String[] trimString(String[] input, Integer counter) {
        if (this.what.equals(TrimValueType.Row)) {
            String[] newRes = ArrayUtils.remove(input, this.idx - counter);
            return newRes;
        }
        // TODO for TrimvalueType.Column
        return input;
    }
}

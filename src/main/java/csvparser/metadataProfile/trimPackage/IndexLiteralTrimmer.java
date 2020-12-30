package csvparser.metadataProfile.trimPackage;

import org.json.JSONObject;

import csvparser.metadataProfile.types.LineType;
import csvparser.metadataProfile.types.TrimValueType;

/**
 * Trim a value (literal) in a line (where) specified by index (idx).
 */
public class IndexLiteralTrimmer extends AbstractTrimmer {
    public LineType where;
    public Integer idx;
    public String literal;

    public IndexLiteralTrimmer(JSONObject jsonObj) {
        super(TrimValueType.valueOf(jsonObj.getString("what")));
        this.where = LineType.valueOf(jsonObj.getString("where"));
        this.idx = jsonObj.getInt("idx");
        this.literal = jsonObj.getString("literal");
    }

    public IndexLiteralTrimmer(TrimValueType what, LineType where, Integer idx, String literal) {
        super(what);
        this.where = where;
        this.idx = idx;
        this.literal = literal;
    }

    public String[] trimString(String[] input, Integer counter) {
        if (this.where.equals(LineType.Row)) {
            if (this.what.equals(TrimValueType.Literal))
            input[this.idx - counter] = input[this.idx - counter].replace(this.literal, "").trim();
        } 
        return input;
    }

}

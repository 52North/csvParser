package csvparser.metadataProfile.trimPackage;

import csvparser.metadataProfile.types.TrimValueType;

public abstract class AbstractTrimmer {
    public TrimValueType what;

    public AbstractTrimmer(TrimValueType what) {
        this.what = what;
    }

    public abstract String[] trimString(String[] input, Integer counter);
}

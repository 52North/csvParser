package csvparser.metadataProfile.structureGroupPackage;

import java.util.ArrayList;

public class IterationStructureList extends AbstractStructureList {
    public IStructure[] structure;

    public IterationStructureList(Boolean optional, IStructure[] structure) {
        super(optional);
        this.structure = structure;
    }

    public IterationStructureList(IterationStructureList that) {
        super(that.optional);
        ArrayList<IStructure> cloned = this.cloneArrayOfObjects(that.structure);
        this.structure = cloned.toArray(new IStructure[cloned.size()]);
    }

    public ArrayList<IStructure> cloneArrayOfObjects(IStructure[] arrayOfObjects) {
        try {
            ArrayList<IStructure> list = new ArrayList<IStructure>();
            for (IStructure el: arrayOfObjects) {

                if (el.getClass() == SimpleStructure.class) {
                    list.add(new SimpleStructure((SimpleStructure) el));
                } else {
                    list.add(new IterationStructure((IterationStructure) el));
                }
            }
            return list;
        } catch (Exception e) {
            System.out.println("Error in parseStructureGroup");
            e.printStackTrace();
        }
        return new ArrayList<IStructure>();
    }

    public IStructure[] getStructure() {
        return structure;
    }

    public void setStructure(IStructure[] structure) {
        this.structure = structure;
    }

}

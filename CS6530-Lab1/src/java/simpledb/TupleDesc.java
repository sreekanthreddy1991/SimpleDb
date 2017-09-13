package simpledb;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {

    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        public final Type fieldType;

        /**
         * The name of the field
         * */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldType + "(" + fieldName + ")";
        }
    }

    /**
     * list to hold all the fields of a single tuple desc
     * */
    private TDItem[] tdItemsList;

    /**
     * @return
     *        An iterator which iterates over all the field TDItems
     *        that are included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {
        // some code goes here
        return Arrays.asList(tdItemsList).iterator();
    }

    private static final long serialVersionUID = 1L;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     *
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        if(typeAr.length == 0 || typeAr.length != fieldAr.length){
            System.out.println("type and field array do not have same number of fields");
        }
        // some code goes here
        tdItemsList = new TDItem[typeAr.length];
        for(int i=0; i< tdItemsList.length; i++){
            tdItemsList[i] = new TDItem(typeAr[i], fieldAr[i]);
        }
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     *
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here
        tdItemsList = new TDItem[typeAr.length];
            for(int i=0; i< tdItemsList.length; i++){
                tdItemsList[i] = new TDItem(typeAr[i], "");
            }
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // some code goes here
        return tdItemsList.length;
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     *
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // some code goes here
        if(i >= tdItemsList.length){
            throw new NoSuchElementException("No field exists with index " + i);
        }
        return tdItemsList[i].fieldName;
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     *
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        // some code goes here
        if(i >= tdItemsList.length){
            throw new NoSuchElementException("No field exists with index " + i);
        }
        return tdItemsList[i].fieldType;
    }

    /**
     * Find the index of the field with a given name.
     *
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
        // some code goes here
        Iterator<TDItem> iterator = iterator();
        int index = 0;
        while(iterator.hasNext()){
            if(iterator.next().fieldName.equals(name)){
                return index;
            }
            index++;
        }
        throw new NoSuchElementException("can not find index with the name " + name);
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
        int size = 0;
        Iterator<TDItem> iterator = iterator();
        while(iterator.hasNext()){
            size = size + iterator.next().fieldType.getLen();
        }
        return size;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     *
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        // some code goes here
        int totalFields = td1.numFields() + td2.numFields();
        Type[] typeList = new Type[totalFields];
        String[] namesList = new String[totalFields];
        for(int i=0; i< totalFields; i++){
            if(i<td1.numFields()){
                typeList[i] = td1.getFieldType(i);
                namesList[i] = td1.getFieldName(i);
            } else {
                typeList[i] = td2.getFieldType(i - td1.numFields());
                namesList[i] = td2.getFieldName(i - td1.numFields());
            }
        }
        TupleDesc newTupleDesc = new TupleDesc(typeList, namesList);
        return newTupleDesc;
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they are the same size and if the n-th
     * type in this TupleDesc is equal to the n-th type in td.
     *
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */
    public boolean equals(Object o) {
        // some code goes here
        boolean isEqual = false;
        if(o == null)
            isEqual = false;
        if(o instanceof TupleDesc){
            TupleDesc newTupleDesc = (TupleDesc) o;
            if(newTupleDesc.numFields()!= this.numFields()){
                isEqual = false;
            } else {
                isEqual = true;
                for(int i=0; i<newTupleDesc.numFields(); i++){
                    if(newTupleDesc.getFieldType(i) != this.getFieldType(i)){
                        isEqual = false;
                        break;
                    }
                }
            }
        } else {
            isEqual = false;
        }
        return isEqual;
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     *
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
        String description = "";
        Iterator<TDItem> iterator = iterator();
        while (iterator.hasNext()){
            TDItem tdItem = iterator.next();
            description = description+", "+tdItem.toString();
        }
        return description;
    }
}

package it.cnr.contab.generator.artifacts;

/**
 * Created on 23-nov-04
 * Implementation of the Impostor Type bug Design Pattern
 *
 * @author Marco Spasiano
 * @version 2.0 - progetto eclipse wizard
 * [14-aug-2006] utilizzo di GeneratorBean
 * [22-aug-2006] refactoring
 */
public interface Tags {

    /**
     * Tags and attribute names
     */
    public static final String
            BULK_INFO = "bulkInfo",
            SHORT_DESCRIPTION = "shortDescription",
            LONG_DESCRIPTION = "longDescription",
            FIELD_PROPERTY = "fieldProperty",
            PROPERTY = "property",
            INPUT_TYPE = "inputType",
            INPUT_SIZE = "inputSize",
            FORMAT_NAME = "formatName",
            MAX_LENGTH = "maxLength",
            LABEL = "label",
            ENABLED_ON_SEARCH = "enabledOnSearch",
            ENABLED_ON_EDIT = "enabledOnEdit",
            FORM = "form",
            SEARCHTOOL = "searchtool",
            FORM_FIELD_PROPERTY = "formFieldProperty",
            FIND_FIELD_PROPERTY = "findFieldProperty",
            COLUMN_FIELD_PROPERTY = "columnFieldProperty",
            SQL_PERSISTENT_INFO = "SQLPersistentInfo",
            PERSISTENT_CLASS_NAME = "persistentClassName",
            KEY_CLASS_NAME = "keyClassName",
            HOME_CLASS_NAME = "homeClassName",
            DEFAULT_COLUMN_MAP = "defaultColumnMap",
            TABLE_NAME = "tableName",
            COLUMN_MAPPING = "columnMapping",
            COLUMN_NAME = "columnName",
            PROPERTY_NAME = "propertyName",
            SQL_TYPE_NAME = "sqlTypeName",
            COLUMN_SIZE = "columnSize",
            COLUMN_SCALE = "columnScale",
            NULLABLE = "nullable",
            PRIMARY = "primary",
            PERSISTENT_PROPERTY = "persistentProperty",
            DATE_SHORT = "date_short",
            EURO_FORMAT = "it.jada.stone.util.EuroFormat",
            NAME = "name",
            FORMNAME = "formName",
            PART_OF_ID = "partOfOid",
            CONVERTER_CLASS_NAME = "converterClassName";

    /**
     * Other constants for XML generation
     */
    public static final String
            COLS = "cols",
            ROWS = "rows",

    TYPE_CHECKBOX = "CHECKBOX",
            TYPE_TEXT = "TEXT",
            TYPE_TEXTAREA = "TEXTAREA";

    /**
     * MetaData columns names
     */
    public static final String
            MD_FK_NAME = "FK_NAME",
            MD_TABLE_NAME = "TABLE_NAME",
            MD_FKTABLE_NAME = "FKTABLE_NAME",
            MD_FKCOLUMN_NAME = "FKCOLUMN_NAME",
            MD_COLUMN_NAME = "COLUMN_NAME",
            MD_DATA_TYPE = "DATA_TYPE",
            MD_TYPE_NAME = "TYPE_NAME",
            MD_COLUMN_SIZE = "COLUMN_SIZE",
            MD_DECIMAL_DIGITS = "DECIMAL_DIGITS",
            MD_IS_NULLABLE = "IS_NULLABLE",
            MD_REMARKS = "REMARKS",
            MD_KEY_SEQ = "KEY_SEQ",
            MD_ORDINAL_POSITION = "ORDINAL_POSITION",
            MD_PKTABLE_NAME = "PKTABLE_NAME",
            MD_PKCOLUMN_NAME = "PKCOLUMN_NAME";

    /**
     * Other commonm names
     */
    public static final String
            CHAR = "CHAR",
            TIMESTAMP = "TIMESTAMP",
            FLAG = "FL_";
}

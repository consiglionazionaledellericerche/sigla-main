package it.cnr.contab.generator.properties;

/**
 * Nomi chiave per la gestione delle preferenze
 * <p>
 * Created on 18-nov-04
 * Implementation of the Impostor Type bug Pattern
 *
 * @author Marco Spasiano
 * @version 2.0
 * [31-Jul-2006] adattamento a plugin
 * [23-Aug-2006] refactoring e pulizia nomi non usati
 * [23-Aug-2006] classe rinominata (ex Names)
 */
public interface Preferences {

    /**
     * Nomi per la gestione delle preferenze
     */
    public static final String
            AUTHOR = "author",
            VERSION = "version",
            PROJECT = "project",
            DB_DRIVER = "db.driver",
            DB_URL = "db.url",
            DB_USER = "db.user",
            DB_PASSWORD = "db.password",
            DB_SCHEMA = "gen.schema",
            LIST_TABLES = "gen.listTables",
            XML_HEADER = "xml.header",
            XML_PATH = "xml.path",
            SQL_PREFIX = "sql.",
            SQL_ITEMS = SQL_PREFIX + "items";

    /**
     * Types
     **/
    public static final String
            TYPE_PREFIX = "tipo.",
            TYPE_ITEMS = TYPE_PREFIX + "items",
            TYPE_CHAR = TYPE_PREFIX + "char",
            TYPE_CHAR_BOOLEAN = TYPE_PREFIX + "char.boolean",
            TYPE_CHAR_FLAG = TYPE_PREFIX + "char.flag",
            TYPE_VARCHAR = TYPE_PREFIX + "varchar",
            TYPE_TIMESTAMP = TYPE_PREFIX + "timestamp",
            TYPE_DECIMAL = TYPE_PREFIX + "decimal",
            TYPE_DECIMAL_SIZE = TYPE_PREFIX + "decimal.size",
            TYPE_DECIMAL_BIG = TYPE_PREFIX + "decimal.big",
            TYPE_DECIMAL_LONG = TYPE_PREFIX + "decimal.long",
            TYPE_DECIMAL_INT = TYPE_PREFIX + "decimal.int",
            TYPE_UKNOWN = TYPE_PREFIX + "uknown";


    /**
     * Text Area Definition
     */
    public static final String
            TEXTAREA_TEXT = "textarea.text",
            TEXTAREA_COLS = "textarea.cols",
            TEXTAREA_ROWS = "textarea.rows";

}
package it.cnr.contab.generator.properties;

/**
 * Gestione proprietà di default
 *
 * @author Marco Spasiano
 * @version 1.0
 * [22-Aug-2006] creazione
 * [24-Aug-2006] classe rinominata (ex BulkGeneratorDefaults)
 */

public final class Generation {

    private static final String BUNDLE_NAME = "it.cnr.contab.generator.properties.Generation";//$NON-NLS-1$
    // ----- java names -----
    public static String java_ext = ".java";
    public static String java_base = "Base";
    public static String java_key = "Key";
    public static String java_home = "Home";
    public static String java_bulk = "Bulk";
    // ----- xml names -----
    public static String bixml_ext = ".xml";
    public static String bpixml_ext = ".xml";
    public static String xml_base = "BasePersistentInfo";
    public static String xml_key = "KeyPersistentInfo";
    public static String xml_bulk = "BulkPersistentInfo";
    public static String xml_info = "BulkInfo";
    // ------ imports ------
    public static String base_Keyed = "it.cnr.jada.persistency.Keyed";
    public static String bulk_ActionContext = "it.cnr.jada.action.ActionContext";
    public static String bulk_OggettoBulk = "it.cnr.jada.bulk.OggettoBulk";
    public static String bulk_CRUDBP = "it.cnr.jada.util.action.CRUDBP";
    public static String home_BulkHome = "it.cnr.jada.bulk.BulkHome";
    public static String home_PersistentCache = "it.cnr.jada.persistency.PersistentCache";
    public static String home_Connection = "java.sql.Connection";
    public static String key_KeyedPersistent = "it.cnr.jada.persistency.KeyedPersistent";

}

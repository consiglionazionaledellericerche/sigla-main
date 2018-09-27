package it.cnr.contab.generator.util;

/**
 * Stabilisce i parametri necessari per aprire una connessione col database
 *
 * @author Marco Spasiano
 * @version 1.0
 * [28-Jul-2006]
 */
public interface DatabaseInfo {
    public String getDriver();

    public String getUrl();

    public String getUser();

    public String getPassword();
}

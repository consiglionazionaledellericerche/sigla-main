package it.cnr.contab.prevent00.bulk;

/**
 * Interfaccia che implementa i metodi comuni tra le classi dei Bulk delle 
 * Entrate e delle Spese adattando {@link it.cnr.jada.persistency.KeyedPersistent }
 */
public interface Pdg_aggregato_det extends it.cnr.jada.persistency.KeyedPersistent {

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_centro_responsabilita'
 *
 * @return Il valore della proprietà 'cd_centro_responsabilita'
 */
String getCd_centro_responsabilita();
it.cnr.contab.config00.sto.bulk.CdrBulk getCdr();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'crudStatus'
 *
 * @return Il valore della proprietà 'crudStatus'
 */
int getCrudStatus();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'esercizio'
 *
 * @return Il valore della proprietà 'esercizio'
 */
Integer getEsercizio();
String getTi_aggregato();
/**
 * Imposta il dettaglio in modalità creazione
 */
void setToBeCreated();
/**
 * Imposta il dettaglio in modalità modifica
 */
void setToBeUpdated();
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'user'
 *
 * @param user	Il valore da assegnare a 'user'
 */
void setUser(String user);
}

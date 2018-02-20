package it.cnr.contab.pdg00.comp;

/**
 * Eccezione utilizzata per notificare la duplicazione del codice fiscale e
 * rendere possibile il riutilizzo dell'oggetto anagrafico che l'ha generato.
 * Quest'ultimo viene al momento gestito per poter accorpare i terzi del nuovo
 * oggetto al vecchio.
 */

public class DiscrepanzeAggregatoException extends it.cnr.jada.comp.ApplicationException {

	private java.util.List lista;

/**
 * Costruttore standard.
 */

public DiscrepanzeAggregatoException() {
	super();
}
/**
 * Costruttore che notifica un messaggio libero ma non associa all'eccezione
 * l'oggetto anagrafico.
 *
 * @param s Messaggio da notificare
 */

public DiscrepanzeAggregatoException(String s) {
	super(s);
}
/**
 * Il costruttore genera l'eccezione associando un oggetto <code>AnagraficoBulk</code>.
 *
 * @param anag oggetto che ha generato l'eccezione.
 */

public DiscrepanzeAggregatoException(java.util.List listaAgr) {
	super("Sono presenti importi complessivi non compatibili con i dati impostati dall'ente. Visualizzarli?");
	lista = listaAgr;
}
	/**
	 * Restituisce l'oggetto anagrafico associato all'eccezione. Se non
	 * è stato associato nessun oggetto viene restituito un nuovo <code>AnagraficoBulk</code>.
	 *
	 * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
	 */

	public java.util.List getLista() {
		return lista == null? (new java.util.Vector()): lista;
	}

}

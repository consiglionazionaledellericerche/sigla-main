/*
* Created by Generator 1.0
* Date 17/05/2005
*/
package it.cnr.contab.coepcoan00.core.bulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Chiusura_coepBulk extends Chiusura_coepBase {
	private static final java.util.Dictionary ti_statoKeys = new it.cnr.jada.util.OrderedHashtable();

	final public static String STATO_PROVA_DI_CHIUSURA_ESEGUITA = "P";
	final public static String STATO_CHIUSO_DEFINITIVAMENTE = "C";
	final public static String STATO_PROVA_ANNULLATA = "A";

	static {
		ti_statoKeys.put(STATO_PROVA_DI_CHIUSURA_ESEGUITA,"Prova di chiusura eseguita");
		ti_statoKeys.put(STATO_CHIUSO_DEFINITIVAMENTE,"Chiuso definitivamente");
		ti_statoKeys.put(STATO_PROVA_ANNULLATA,"Prova annullata");
	}
    private EsercizioBulk esercizio_rif;
        
	public Chiusura_coepBulk() {
		super();
	}
	public Chiusura_coepBulk(java.lang.String cd_cds, java.lang.Integer esercizio) {
		super(cd_cds, esercizio);
		setEsercizio_rif(new EsercizioBulk(cd_cds,esercizio));
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/04/2005 12:34:48)
	 * @return java.util.Dictionary
	 */
	public final java.util.Dictionary getTi_statoKeys() {
		return ti_statoKeys;
	}
	
	/**
	 * @return
	 */
	public EsercizioBulk getEsercizio_rif() {
		return esercizio_rif;
	}

	/**
	 * @param bulk
	 */
	public void setEsercizio_rif(EsercizioBulk bulk) {
		esercizio_rif = bulk;
	}	
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.coepcoan00.core.bulk.Chiusura_coepKey#setCd_cds(java.lang.String)
	 */
	public void setCd_cds(java.lang.String cd_cds)  {
		getEsercizio_rif().setCd_cds(cd_cds);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.coepcoan00.core.bulk.Chiusura_coepKey#getCd_cds()
	 */
	public java.lang.String getCd_cds () {
		return getEsercizio_rif().getCd_cds();
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.coepcoan00.core.bulk.Chiusura_coepKey#setEsercizio(java.lang.Integer)
	 */
	public void setEsercizio(java.lang.Integer esercizio)  {
		getEsercizio_rif().setEsercizio(esercizio);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.coepcoan00.core.bulk.Chiusura_coepKey#getEsercizio()
	 */
	public java.lang.Integer getEsercizio () {
		return getEsercizio_rif().getEsercizio();
	}
}
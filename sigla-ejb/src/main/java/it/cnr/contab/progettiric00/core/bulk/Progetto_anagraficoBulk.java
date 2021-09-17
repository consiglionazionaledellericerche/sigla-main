/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/08/2021
 */
package it.cnr.contab.progettiric00.core.bulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

public class Progetto_anagraficoBulk extends Progetto_anagraficoBase {
	/**
	 * [ANAGRAFICO ]
	 **/
	private AnagraficoBulk anagrafico =  new AnagraficoBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: PROGETTO_ANAGRAFICO
	 **/
	public Progetto_anagraficoBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: PROGETTO_ANAGRAFICO
	 **/
	public Progetto_anagraficoBulk(Long idPrgAnagrafico) {
		super(idPrgAnagrafico);
	}
	public AnagraficoBulk getAnagrafico() {
		return anagrafico;
	}
	public void setAnagrafico(AnagraficoBulk anagrafico)  {
		this.anagrafico=anagrafico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Codice entità anagrafica di primo livello]
	 **/
	public Integer getCdAnag() {
		AnagraficoBulk anagrafico = this.getAnagrafico();
		if (anagrafico == null)
			return null;
		return getAnagrafico().getCd_anag();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Codice entità anagrafica di primo livello]
	 **/
	public void setCdAnag(Integer cdAnag)  {
		this.getAnagrafico().setCd_anag(cdAnag);
	}


	public java.lang.String getDs_anagrafico() {
		if ( getAnagrafico() != null && getAnagrafico() != null &&
				getAnagrafico().getCognome()!=null )
			return getAnagrafico().getCognome() + " " + getAnagrafico().getNome();
		return "";
	}
}
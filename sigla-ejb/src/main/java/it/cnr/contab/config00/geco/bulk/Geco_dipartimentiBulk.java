/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 29/11/2006
 */
package it.cnr.contab.config00.geco.bulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Geco_dipartimentiBulk extends Geco_dipartimentiBase implements Geco_dipartimentiIBulk{
	public Geco_dipartimentiBulk() {
		super();
	}
	public Geco_dipartimentiBulk(java.lang.String cod_dip,java.lang.Long esercizio) {
		super(cod_dip,esercizio);
	}
	public void aggiornaDipartimentoSIP(DipartimentoBulk dipartimento) {
		if (!getDescrizione().equals(dipartimento.getDs_dipartimento())){
			dipartimento.setDs_dipartimento(getDescrizione());
			dipartimento.setToBeUpdated();
		}
		if (getData_istituzione() != null && !getData_istituzione().equals(dipartimento.getDt_istituzione())){
			dipartimento.setDt_istituzione(getData_istituzione());
			dipartimento.setToBeUpdated();
		}
	}
}
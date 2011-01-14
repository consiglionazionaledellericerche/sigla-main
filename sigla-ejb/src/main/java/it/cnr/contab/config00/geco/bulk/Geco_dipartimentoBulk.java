/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 29/11/2006
 */
package it.cnr.contab.config00.geco.bulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Geco_dipartimentoBulk extends Geco_dipartimentoBase implements Geco_dipartimentoIBulk{
	public Geco_dipartimentoBulk() {
		super();
	}
	public Geco_dipartimentoBulk(java.lang.String cod_dip) {
		super(cod_dip);
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
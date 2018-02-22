/*
* Created by Generator 1.0
* Date 02/05/2005
*/
package it.cnr.contab.doccont00.core.bulk;
import java.math.BigDecimal;

import it.cnr.contab.pdg01.bulk.Pdg_modulo_entrate_gestBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class V_pdg_accertamento_etrBulk extends V_pdg_accertamento_etrBase {
	private BigDecimal prcImputazioneFin;	

	public V_pdg_accertamento_etrBulk() {
		super();
	}

	public BigDecimal getImporto() 
	{
		if ( it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk.CAT_SINGOLO.equals( getCategoria_dettaglio()) ||
		     Pdg_modulo_entrate_gestBulk.CAT_DIRETTA.equals( getCategoria_dettaglio()))
			return getIm_ra_rce().add( getIm_rc_esr());
		else if ( it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk.CAT_SCARICO.equals( getCategoria_dettaglio()))
			return getIm_rb_rse();
		return new BigDecimal(0);
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'prcImputazioneFin'
	 *
	 * @return Il valore della proprietà 'prcImputazioneFin'
	 */
	public BigDecimal getPrcImputazioneFin() {
		return prcImputazioneFin;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'prcImputazioneFin'
	 *
	 * @param newPrcImputazioneFin	Il valore da assegnare a 'prcImputazioneFin'
	 */
	public void setPrcImputazioneFin(BigDecimal newPrcImputazioneFin) {
		prcImputazioneFin = newPrcImputazioneFin;
	}

}
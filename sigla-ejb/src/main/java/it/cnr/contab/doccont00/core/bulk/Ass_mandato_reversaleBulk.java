package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_mandato_reversaleBulk extends Ass_mandato_reversaleBase {
	public static final String TIPO_ORIGINE_ENTRATA = "E";
	public static final String TIPO_ORIGINE_SPESA	= "S";
	protected ReversaleBulk reversale;
	protected MandatoBulk mandato;
public Ass_mandato_reversaleBulk() {
	super();
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param mandato	
 * @param reversale	
 */
public Ass_mandato_reversaleBulk( MandatoBulk mandato, ReversaleBulk reversale)
{
	setMandato( mandato );
	setReversale( reversale );
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.MandatoBulk
 */
public MandatoBulk getMandato() {
	return mandato;
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.ReversaleBulk
 */
public ReversaleBulk getReversale() {
	return reversale;
}
/**
 * @param newMandato it.cnr.contab.doccont00.core.bulk.MandatoBulk
 */
public void setMandato(MandatoBulk newMandato) {
	mandato = newMandato;
	setPg_mandato( mandato.getPg_mandato());
	setEsercizio_mandato( mandato.getEsercizio());
	setCd_cds_mandato( mandato.getCd_cds());
}
/**
 * @param newReversale it.cnr.contab.doccont00.core.bulk.ReversaleBulk
 */
public void setReversale(ReversaleBulk newReversale) {
	reversale = newReversale;
	setPg_reversale( reversale.getPg_reversale());
	setEsercizio_reversale( reversale.getEsercizio());
	setCd_cds_reversale( reversale.getCd_cds());
}
}

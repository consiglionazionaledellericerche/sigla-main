package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Lettera_pagam_esteroBulk extends Lettera_pagam_esteroBase {

	private it.cnr.contab.doccont00.core.bulk.SospesoBulk sospeso = null;
	private java.util.Vector sospesiCancellati = null;
	private boolean annoDiCompetenza = true;
public Lettera_pagam_esteroBulk() {
	super();
}
public Lettera_pagam_esteroBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_lettera) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_lettera);
}
public void addToSospesiCancellati(SospesoBulk sospeso) {

	if (getSospesiCancellati() == null)
		setSospesiCancellati(new java.util.Vector());
	if (!BulkCollections.containsByPrimaryKey(getSospesiCancellati(), sospeso))
		getSospesiCancellati().addElement(sospeso);
}

public void completeFrom(ActionContext context) 
	throws javax.ejb.EJBException, java.text.ParseException {

	java.sql.Timestamp date = it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp();
	int annoSolare = Fattura_passivaBulk.getDateCalendar(date).get(java.util.Calendar.YEAR);
	int esercizioInScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue();
	setAnnoDiCompetenza(esercizioInScrivania == getEsercizio().intValue());
	if (annoSolare != esercizioInScrivania)
		date = new java.sql.Timestamp(new java.text.SimpleDateFormat("dd/MM/yyyy").parse("31/12/" + esercizioInScrivania).getTime());
	setDt_registrazione(date);

	setIm_commissioni(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setIm_pagamento(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	SospesoBulk sospeso = new SospesoBulk();
	sospeso.setEsercizio(getEsercizio());
	sospeso.setCd_cds(getCd_cds());
	sospeso.setTi_entrata_spesa(sospeso.TIPO_SPESA);
	sospeso.setTi_sospeso_riscontro(sospeso.TI_SOSPESO);
	setSospeso(sospeso);
	setUser(context.getUserInfo().getUserid());
}
public it.cnr.jada.bulk.OggettoBulk[] getBulksForPersistentcy() {
	return new it.cnr.jada.bulk.OggettoBulk[] { getSospeso() };
}
/**
 * Insert the method's description here.
 * Creation date: (6/12/2002 5:18:43 PM)
 * @return it.cnr.jada.bulk.BulkList
 */
public java.util.Vector getSospesiCancellati() {
	return sospesiCancellati;
}
/**
 * Insert the method's description here.
 * Creation date: (6/7/2002 3:17:11 PM)
 * @return it.cnr.contab.doccont00.core.bulk.SospesoBulk
 */
public it.cnr.contab.doccont00.core.bulk.SospesoBulk getSospeso() {
	return sospeso;
}
/**
 * Insert the method's description here.
 * Creation date: (6/7/2002 3:17:11 PM)
 * @return it.cnr.contab.doccont00.core.bulk.SospesoBulk
 */
public java.util.Dictionary getTipo_sospesoKeys() {
	
	java.util.Dictionary tipi = new java.util.Hashtable();
	tipi.put("E", "Entrata");
	tipi.put("S", "Spesa");
	return tipi;
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2003 15.36.58)
 * @return boolean
 */
public boolean isAnnoDiCompetenza() {
	return annoDiCompetenza;
}
/**
 * Insert the method's description here.
 * Creation date: (6/11/2002 5:10:59 PM)
 */
public boolean isROSospeso() {

	return false;	
}
/**
 * Insert the method's description here.
 * Creation date: (6/11/2002 5:10:59 PM)
 */
public boolean isROSospesoSearchTool() {

	return !isAnnoDiCompetenza();	
}
public int removeFromSospesiCancellati(SospesoBulk sospeso) {

	if (getSospesiCancellati() == null)
		return -1;
	if (BulkCollections.containsByPrimaryKey(getSospesiCancellati(), sospeso))
		getSospesiCancellati().remove(BulkCollections.indexOfByPrimaryKey(getSospesiCancellati(), sospeso));
	return getSospesiCancellati().size()-1;
}

/**
 * Insert the method's description here.
 * Creation date: (03/07/2003 15.36.58)
 * @param newAnnoDiCompetenza boolean
 */
public void setAnnoDiCompetenza(boolean newAnnoDiCompetenza) {
	annoDiCompetenza = newAnnoDiCompetenza;
}
/**
 * Insert the method's description here.
 * Creation date: (6/12/2002 5:18:43 PM)
 * @param newSospesiCancellati it.cnr.jada.bulk.BulkList
 */
public void setSospesiCancellati(java.util.Vector newSospesiCancellati) {
	sospesiCancellati = newSospesiCancellati;
}
/**
 * Insert the method's description here.
 * Creation date: (6/7/2002 3:17:11 PM)
 * @param newSospeso it.cnr.contab.doccont00.core.bulk.SospesoBulk
 */
public void setSospeso(it.cnr.contab.doccont00.core.bulk.SospesoBulk newSospeso) {
	sospeso = newSospeso;
}
public void validate() throws ValidationException {

	if (getIm_commissioni() == null)
		throw new ValidationException("Specificare un importo per le commissioni della lettera di pagamento estero!");
	if (getIm_pagamento() != null && getIm_pagamento().compareTo(new java.math.BigDecimal(0)) != 0) {
		if (getIm_pagamento().compareTo(getIm_commissioni()) < 0)
			throw new ValidationException("L'importo delle commissioni della lettera di pagamento estero non puo' superare l'importo di pagamento!");
	}
}
}

package it.cnr.contab.doccont00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 16.02.55)
 * @author: Roberto Fantino
 */
public class Stampa_registro_obbligazioniBulk extends ObbligazioneBulk {
	private java.sql.Timestamp dataInizio;
	private java.sql.Timestamp dataFine;
	private java.lang.Long pgInizio;
	private java.lang.Long pgFine;

	// Stati
	public final static String STATO_OBB_TUTTI = "*";
	public final static java.util.Dictionary statoObbligazioneKeys;

	static 
	{
		statoObbligazioneKeys = new it.cnr.jada.util.OrderedHashtable();
		statoObbligazioneKeys.put(STATO_OBB_PROVVISORIO, "Provvisoria");
		statoObbligazioneKeys.put(STATO_OBB_DEFINITIVO, "Definitiva");
		statoObbligazioneKeys.put(STATO_OBB_STORNATO, "Stornata");
		statoObbligazioneKeys.put(STATO_OBB_TUTTI, "Tutti");
	}
	private boolean cdsUOInScrivania = false;
	
	//	Competenza - Residuo
	public final static String TIPO_TUTTI = "*";
	public final static java.util.Dictionary tipoCompetenzaResiduoKeys;
	
	static 
	{
		tipoCompetenzaResiduoKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoCompetenzaResiduoKeys.put(Numerazione_doc_contBulk.TIPO_OBB, "Competenza");
		tipoCompetenzaResiduoKeys.put(Numerazione_doc_contBulk.TIPO_OBB_RES, "Residuo Proprio");
		tipoCompetenzaResiduoKeys.put(Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA, "Residuo Improprio");
		tipoCompetenzaResiduoKeys.put("RES", "Residuo");
		tipoCompetenzaResiduoKeys.put(TIPO_TUTTI, "Tutti");
	};

/**
 * Stampa_obbligazioniBulk constructor comment.
 */
public Stampa_registro_obbligazioniBulk() {
	super();
}
/**
 * Stampa_obbligazioniBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param esercizio java.lang.Integer
 * @param esercizio_originale java.lang.Integer
 * @param pg_obbligazione java.lang.Long
 */
public Stampa_registro_obbligazioniBulk(String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_obbligazione) {
	super(cd_cds, esercizio, esercizio_originale, pg_obbligazione);
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdUoForPrint() {
	if (getUoForPrint()==null)
		return null;
	return getUoForPrint().getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdUoNullableForPrint() {

	String cd = getCdUoForPrint();
	return (cd == null) ? "*" : cd;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 16.22.06)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFine() {
	return dataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 16.22.06)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataInizio() {
	return dataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 16.22.06)
 * @return java.lang.Integer
 */
public java.lang.Long getPgFine() {
	return pgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 16.22.06)
 * @return java.lang.Integer
 */
public java.lang.Long getPgInizio() {
	return pgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 16.22.06)
 * @return java.util.Dictionary
 */
public final static java.util.Dictionary getStatoObbligazioneKeys() {
	return statoObbligazioneKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 12.00.24)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoForPrint() {
	return super.getUnita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (3/27/2003 12:20:20 PM)
 * @return boolean
 */
public boolean isCdsUOInScrivania() {
	return cdsUOInScrivania;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROUoForPrint() {
	return getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROUoForPrintSearchTool() {
	return !isCdsUOInScrivania();
}
/**
 * Insert the method's description here.
 * Creation date: (3/27/2003 12:20:20 PM)
 * @param newCdsUOInScrivania boolean
 */
public void setCdsUOInScrivania(boolean newCdsUOInScrivania) {
	cdsUOInScrivania = newCdsUOInScrivania;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 16.22.06)
 * @param newDataFine java.sql.Timestamp
 */
public void setDataFine(java.sql.Timestamp newDataFine) {
	dataFine = newDataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 16.22.06)
 * @param newDataInizio java.sql.Timestamp
 */
public void setDataInizio(java.sql.Timestamp newDataInizio) {
	dataInizio = newDataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 16.22.06)
 * @param newPgFine java.lang.Integer
 */
public void setPgFine(java.lang.Long newPgFine) {
	pgFine = newPgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 16.22.06)
 * @param newPgInizio java.lang.Integer
 */
public void setPgInizio(java.lang.Long newPgInizio) {
	pgInizio = newPgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 12.00.24)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unitaOrganizzativa) {
	super.setUnita_organizzativa(unitaOrganizzativa);
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {
}

public final static java.util.Dictionary getTipoCompetenzaResiduoKeys() {
	return tipoCompetenzaResiduoKeys;
}

}

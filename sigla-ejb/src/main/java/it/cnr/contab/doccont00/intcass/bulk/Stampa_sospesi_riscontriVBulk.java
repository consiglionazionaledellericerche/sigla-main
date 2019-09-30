/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.doccont00.intcass.bulk;

/**
 * Classe che sarà ereditata dalle classi che si occuperanno della stampa dei Sospesi e dei Riscontri.
 * Creation date: (03/07/2003 16.56.12)
 * @author: Gennaro Borriello
 */
public abstract class Stampa_sospesi_riscontriVBulk extends it.cnr.jada.bulk.OggettoBulk {

	// Esercizio di scrivania
	private Integer esercizio;

	// Il Cds di scrivania
	private String cd_cds;

	// Data da
	private java.sql.Timestamp dataInizio;

	// Data a
	private java.sql.Timestamp dataFine;

	// TI_ENTRATA_SPESA CHAR(1) NOT NULL (PK)
	private java.lang.String ti_entrata_spesa;

	public static final String TIPO_ENTRATA = "E";
	public static final String TIPO_SPESA = "S";
	public static final String TIPO_ENTRATA_SPESA = "*";
	
	public final static java.util.Dictionary ti_entrata_spesaKeys;
	static 
	{
		ti_entrata_spesaKeys = new java.util.Hashtable();
		ti_entrata_spesaKeys.put(TIPO_ENTRATA_SPESA, "Entrata/Spesa");
		ti_entrata_spesaKeys.put(TIPO_ENTRATA,	"Entrata");
		ti_entrata_spesaKeys.put(TIPO_SPESA,	"Spesa");
	};
	
/**
 * Stampa_sospesi_riscontriVBulk constructor comment.
 */
public Stampa_sospesi_riscontriVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2003 17.08.01)
 * @return java.lang.String
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2003 17.08.01)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFine() {
	return dataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2003 17.08.01)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataInizio() {
	return dataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (08/06/2004 10.01.15)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (21/10/2003 12.15.09)
 * @return java.lang.String
 */
public java.lang.String getTi_entrata_spesa() {
	return ti_entrata_spesa;
}
/**
 * Insert the method's description here.
 * Creation date: (21/10/2003 12.21.17)
 * @return java.util.Dictionary
 */
public final static java.util.Dictionary getTi_entrata_spesaKeys() {
	return ti_entrata_spesaKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2003 17.08.01)
 * @param newCd_cds java.lang.String
 */
public void setCd_cds(java.lang.String newCd_cds) {
	cd_cds = newCd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2003 17.08.01)
 * @param newDataFine java.sql.Timestamp
 */
public void setDataFine(java.sql.Timestamp newDataFine) {
	dataFine = newDataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2003 17.08.01)
 * @param newDataInizio java.sql.Timestamp
 */
public void setDataInizio(java.sql.Timestamp newDataInizio) {
	dataInizio = newDataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (08/06/2004 10.01.15)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (21/10/2003 12.15.09)
 * @param newTi_entrata_spesa java.lang.String
 */
public void setTi_entrata_spesa(java.lang.String newTi_entrata_spesa) {
	ti_entrata_spesa = newTi_entrata_spesa;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {
	super.validate();
	
	// controllo su campo PROGRESSIVO DOC.CONTABILE
	if ( getTi_entrata_spesa() == null )
		throw new it.cnr.jada.bulk.ValidationException( "Attenzione: indicare un tipo Entrate/Spese." );
		
}
}

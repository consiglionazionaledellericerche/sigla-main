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

package it.cnr.contab.compensi00.tabrif.bulk;

import java.util.Dictionary;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.OrderedHashtable;

public class Gruppo_crBulk extends Gruppo_crBase {
	private SimpleBulkList dettagli_col= new SimpleBulkList();
	
	public final static Dictionary TIPO_RIGA_F24;
	
	public final static String ERARIO = "F";
	public final static String INPS = "I";
	public final static String REGIONI = "R";
	public final static String ENTI_LOCALI = "S";
	public final static String INAIL = "N";
	public final static String INPDAP = "Q";
	public final static String INPGI = "P";
	public final static String FONDI_PENSIONE = "W";
	
	static
	{
		TIPO_RIGA_F24 = new OrderedHashtable();
		TIPO_RIGA_F24.put(ERARIO, "Erario");
		TIPO_RIGA_F24.put(INPS, "Inps");
		TIPO_RIGA_F24.put(REGIONI, "Regioni");		
		TIPO_RIGA_F24.put(ENTI_LOCALI, "Enti Locali");
		TIPO_RIGA_F24.put(INAIL, "Inail");
		TIPO_RIGA_F24.put(INPDAP, "Inpdap");
		TIPO_RIGA_F24.put(INPGI, "Inpgi");
		TIPO_RIGA_F24.put(FONDI_PENSIONE, "Contr. Fondi Pensione T.AA.");
	}
public Gruppo_crBulk() {
	super();
}
public BulkCollection[] getBulkLists() {
	return new it.cnr.jada.bulk.BulkCollection[] 
	{dettagli_col};
}

public Gruppo_crBulk(java.lang.Integer esercizio,java.lang.String cd_gruppo_cr) {
	super(esercizio,cd_gruppo_cr);
}


public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context){

	super.initialize(bp, context);
	setEsercizio( ((it.cnr.contab.utenze00.bp.CNRUserContext)context.getUserContext()).getEsercizio() );	
	return this;
}

public void validate() throws ValidationException {

	// controllo su campo GRUPPO_CR
	if ( getCd_gruppo_cr() == null )
		throw new ValidationException( "Il campo Codice deve essere valorizzato !" );
	if ( getCd_tipo_riga_f24() != null && getCd_tipo_riga_f24().compareTo(Gruppo_crBulk.INPS)==0)
		throw new ValidationException( "Il campo Matricola Inps deve essere valorizzato !" );
}
public Gruppo_cr_detBulk  removeFromDettagli_col( int indiceDiLinea ) {
	Gruppo_cr_detBulk  element = (Gruppo_cr_detBulk )dettagli_col.get(indiceDiLinea);
	return (Gruppo_cr_detBulk )dettagli_col.remove(indiceDiLinea);
}
public int addToDettagli_col (Gruppo_cr_detBulk nuovo)
{	
	nuovo.setGruppo(this);
	nuovo.setModalitaPagamento(new Rif_modalita_pagamentoBulk());
	nuovo.setRegione(new RegioneBulk());
	nuovo.setComune(new ComuneBulk());
	nuovo.setBanca(new BancaBulk());
	nuovo.setTerzo(new TerzoBulk());
	getDettagli_col().add(nuovo);
	return getDettagli_col().size()-1;
}
public SimpleBulkList getDettagli_col() {
	return dettagli_col;
}
public void setDettagli_col(SimpleBulkList dettagli) {
	dettagli_col = dettagli;
}
/**
 * Insert the method's description here.
 * Creation date: (27/11/2001 17.06.35)
 * @return java.util.Dictionary
 */
public java.util.Dictionary getTipo_riga_f24Keys() {
	return TIPO_RIGA_F24;
}
}

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

package it.cnr.contab.docamm00.comp;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

import java.io.Serializable;

/**
 * Insert the type's description here.
 * Creation date: (12/12/2001 1:14:21 PM)
 * @author: Roberto Peli
 */
public class BeneServizioComponent  extends it.cnr.jada.comp.CRUDComponent 
	implements IBeneServizioMgr,ICRUDMgr,Cloneable,Serializable {

/**
 * BeneServizioComponent constructor comment.
 */
public BeneServizioComponent() {
	super();
}
/**
 * Prepara un OggettoBulk per la presentazione all'utente per una possibile
 * operazione di creazione.
 *
 * Pre-post-conditions:
 *
 * Nome: Tutti i controlli superati
 * Pre: 
 * Post: l'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
 *			per una operazione di creazione
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk da preparare
 * @return	l'OggettoBulk preparato
 */	
public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	Bene_servizioBulk beneServizio = (Bene_servizioBulk) super.inizializzaBulkPerInserimento(userContext, bulk);
	
	Categoria_gruppo_inventBulk categoriaGruppo = beneServizio.getCategoria_gruppo();
	if (categoriaGruppo == null)
		categoriaGruppo = new Categoria_gruppo_inventBulk();
	//categoriaGruppo.setVoce_f(new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk());
	return beneServizio;
}
public it.cnr.jada.persistency.sql.SQLBuilder selectCategoria_gruppoByClause(UserContext aUC,Bene_servizioBulk bene, Categoria_gruppo_inventBulk cgi, it.cnr.jada.persistency.sql.CompoundFindClause clauses) 
	throws ComponentException {
	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,cgi).createSQLBuilder();

	sql.addSQLClause("AND","LIVELLO",sql.EQUALS,"1");

	sql.addClause(clauses);
	return sql;
}
public it.cnr.jada.persistency.sql.SQLBuilder selectVoce_ivaByClause(UserContext aUC,Bene_servizioBulk bene, it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk voce_iva, it.cnr.jada.persistency.sql.CompoundFindClause clauses) 
	throws ComponentException {
	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,voce_iva).createSQLBuilder();

	sql.addClause(clauses);
	sql.openParenthesis("AND");
	sql.addSQLClause("AND","TI_APPLICAZIONE",sql.EQUALS,voce_iva.ENTRAMBE);
	sql.addSQLClause("OR","TI_APPLICAZIONE",sql.EQUALS,voce_iva.ACQUISTI);
	sql.closeParenthesis();

	
	return sql;
}
public Bene_servizioBulk completaElementoVoceOf(UserContext param0,
		Bene_servizioBulk param1) throws ComponentException {
	// TODO Auto-generated method stub
	return param1;
}
}

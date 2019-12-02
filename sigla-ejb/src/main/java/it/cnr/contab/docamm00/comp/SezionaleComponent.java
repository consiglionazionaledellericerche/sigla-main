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

import java.io.Serializable;
import java.util.StringTokenizer;

import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoHome;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
public class SezionaleComponent
	extends it.cnr.jada.comp.CRUDComponent 
	implements ICRUDMgr,ISezionaleMgr,Cloneable,Serializable {

    public  SezionaleComponent()
    {

        /*Default constructor*/


    }
//^^@@
/** 
  *  normale
  *    PreCondition:
  *      Viene richiesta una possibile operazione di creazione.
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato per una operazione di creazione.
 */
//^^@@

public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext, OggettoBulk bulk)
    throws it.cnr.jada.comp.ComponentException {

    SezionaleBulk sezionale = (SezionaleBulk) bulk;
    try {
        SezionaleHome home = (SezionaleHome) getHome(userContext, sezionale);
        if (!home.verificaStatoEsercizio(sezionale))
            throw new it.cnr.jada.comp.ApplicationException(
                "Impossibile inserire un sezionale per un esercizio non aperto!");
    } catch (it.cnr.jada.persistency.PersistencyException e) {
        throw handleException(bulk, e);
    } catch (it.cnr.jada.persistency.IntrospectionException e) {
        throw handleException(bulk, e);
    }
    return super.inizializzaBulkPerInserimento(userContext,bulk);
}
public java.util.List findListaSezionaleWS(UserContext userContext,String tipo,String tipo_fattura,String uo, String query,String dominio,String tipoRicerca)throws ComponentException{
	try {		
		SezionaleHome home = (SezionaleHome)getHome(userContext,SezionaleBulk.class);
		//SQLBuilder sql=home.createSQLBuilder();
		SQLBuilder sql = (SQLBuilder)super.select( userContext,null,new SezionaleBulk());
		sql.addTableToHeader("TIPO_SEZIONALE");
		
		sql.addSQLJoin("SEZIONALE.CD_TIPO_SEZIONALE","TIPO_SEZIONALE.CD_TIPO_SEZIONALE");
		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,uo);
		sql.addSQLClause("AND","TI_ACQUISTI_VENDITE",sql.EQUALS,tipo);
		sql.addSQLClause("AND","TI_FATTURA",sql.EQUALS, tipo_fattura);
		sql.addSQLClause("AND","FL_AUTOFATTURA",sql.EQUALS,"N");
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		if (dominio.equalsIgnoreCase("codice"))
			sql.addSQLClause("AND","CD_TIPO_SEZIONALE",SQLBuilder.EQUALS,query);
		else if (dominio.equalsIgnoreCase("descrizione")){
			
				sql.openParenthesis("AND");
				for(StringTokenizer stringtokenizer = new StringTokenizer(query, " "); stringtokenizer.hasMoreElements();){
					String queryDetail = stringtokenizer.nextToken();
					if ((tipoRicerca != null && tipoRicerca.equalsIgnoreCase("selettiva"))|| tipoRicerca == null){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail)))
							sql.addSQLClause("AND","DS_TIPO_SEZIONALE",SQLBuilder.CONTAINS,queryDetail);
						else{
							sql.openParenthesis("AND");
							sql.addSQLClause("OR","DS_TIPO_SEZIONALE",SQLBuilder.CONTAINS,queryDetail);
							sql.addSQLClause("OR","DS_TIPO_SEZIONALE",SQLBuilder.CONTAINS,RemoveAccent.convert(queryDetail));
							sql.closeParenthesis();
}
					}else if (tipoRicerca.equalsIgnoreCase("puntuale")){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail))){
							sql.openParenthesis("AND");
							  sql.addSQLClause("AND","UPPER(DS_TIPO_SEZIONALE)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							  sql.addSQLClause("OR","DS_TIPO_SEZIONALE",SQLBuilder.STARTSWITH,queryDetail+" ");
							  sql.addSQLClause("OR","DS_TIPO_SEZIONALE",SQLBuilder.ENDSWITH," "+queryDetail);
							sql.closeParenthesis();  
						}else{
							sql.openParenthesis("AND");
							  sql.openParenthesis("AND");
							    sql.addSQLClause("OR","UPPER(DS_TIPO_SEZIONALE)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							    sql.addSQLClause("OR","UPPER(DS_TIPO_SEZIONALE)",SQLBuilder.EQUALS,RemoveAccent.convert(queryDetail).toUpperCase());
							  sql.closeParenthesis();
							  sql.openParenthesis("OR");							  
							    sql.addSQLClause("OR","DS_TIPO_SEZIONALE",SQLBuilder.STARTSWITH,queryDetail+" ");
							    sql.addSQLClause("OR","DS_TIPO_SEZIONALE",SQLBuilder.STARTSWITH,RemoveAccent.convert(queryDetail)+" ");
							  sql.closeParenthesis();  
							  sql.openParenthesis("OR");
							    sql.addSQLClause("OR","DS_TIPO_SEZIONALE",SQLBuilder.ENDSWITH," "+queryDetail);
							    sql.addSQLClause("OR","DS_TIPO_SEZIONALE",SQLBuilder.ENDSWITH," "+RemoveAccent.convert(queryDetail));
							  sql.closeParenthesis();  
							sql.closeParenthesis();  
						}
					}
				}
				sql.closeParenthesis();
				sql.addOrderBy("DS_TIPO_SEZIONALE");
			}
		
		return home.fetchAll(sql);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
}

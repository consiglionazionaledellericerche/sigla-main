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
import java.rmi.RemoteException;
import java.util.StringTokenizer;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.V_terzo_anagrafico_sipBulk;
import it.cnr.contab.anagraf00.core.bulk.V_terzo_anagrafico_sipHome;
import 	it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;

public class TariffarioComponent 
	extends it.cnr.jada.comp.CRUDComponent 
	implements ICRUDMgr,ITariffarioMgr,Cloneable,Serializable {



    public  TariffarioComponent()
    {

        /*Default constructor*/


    }
//^^@@
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      La tariffa inserita ha data inizio validità interna all'ultimo periodo preesistente (con data fine = infinito) OR è il primo record della validità della tariffa e ha fine = infinito.
  *    PostCondition:
  *      Consente l'inserimento della tariffa.
  *  Riscontrata condizione di errore.
  *    PreCondition:
  *      Si e verificato un errore.
  *      
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione,  si è verificato un errore".
 */
//^^@@
public OggettoBulk creaConBulk (UserContext aUC,OggettoBulk tariffario) throws ComponentException{

	try {
		if (tariffario == null || !validaTariffa(aUC, (it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk)tariffario))        	
	      throw new it.cnr.jada.comp.ApplicationException("Attenzione,  si è verificato un errore");
	   return super.creaConBulk(aUC, tariffario);
	} catch (it.cnr.jada.comp.ApplicationException e) {
		throw handleException(e);
	}
}
//^^@@
/** 
  *  Eliminazione periodo.
  *    PreCondition:
  *      Periodi presenti =1.
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione,  deve esistere almeno un periodo".
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessun errore rilevato.
  *    PostCondition:
  *      Viene consentita la camcellazione.
 */
//^^@@
public void eliminaConBulk(UserContext aUC, OggettoBulk tariffario) throws ComponentException {
    int rc = -1;

    try {
        it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC, tariffario).createSQLBuilder();
        sql.addSQLClause(
            "AND",
            "CD_UNITA_ORGANIZZATIVA",
            sql.EQUALS,
            ((it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk) tariffario).getCd_unita_organizzativa());
        sql.addSQLClause(
            "AND",
            "CD_TARIFFARIO",
            sql.EQUALS,
            ((it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk) tariffario).getCd_tariffario());
        rc = sql.executeCountQuery(getHomeCache(aUC).getConnection());

        if (rc == 1)
            throw new it.cnr.jada.comp.ApplicationException("Attenzione, deve esistere almeno un periodo");
        else {
            /* L'unico record eliminabile è l'ultimo, che presenta la data di fine validità infinita */
            it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk tariffarioBulk =
                (it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk) tariffario;
            //if (!tariffarioBulk.getDt_fine_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO))
            //throw new it.cnr.jada.comp.ApplicationException("Attenzione, l'unico intervallo che è possibile cancellare è l'ultimo");	  
            //else{ 
            // il record precedente all'ultimo deve presentare data_fine_validita infinita
            sql.addSQLClause(
                "AND",
                "DT_FINE_VALIDITA",
                sql.EQUALS,
                new java.sql.Timestamp(tariffarioBulk.getDt_ini_validita().getTime() - 1000 * 24 * 60 * 60));

            if (tariffarioBulk.getDt_fine_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO)) {
                it.cnr.contab.docamm00.tabrif.bulk.TariffarioHome tariffarioHome =
                    (it.cnr.contab.docamm00.tabrif.bulk.TariffarioHome) getHome(aUC,
                        it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk.class);

                it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk penultimoTariffario =
                    (it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk) tariffarioHome.fetchAll(tariffarioHome.createBroker(sql)).get(0);

                penultimoTariffario.setDt_fine_validita(it.cnr.contab.docamm00.tabrif.bulk.CambioHome.getFineinfinito());
                penultimoTariffario.setToBeUpdated();
                modificaConBulk(aUC, penultimoTariffario);
            }
            super.eliminaConBulk(aUC, tariffario);
            //}// fine else
        } // fine else
    } // fine try
    catch (Throwable e) {
        throw handleException(e);
    }
}
//^^@@
/** 
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessuna condizione di errore.
  *    PostCondition:
  *      Consente la modifica della tariffa.
  *  validazione tariffa non superata
  *    PreCondition:
  *      Sono state modificate le date di validita di un periodo esistente.
  *      
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione, la modifica di questi dati non è consentita".
 */
//^^@@
public OggettoBulk modificaConBulk (UserContext aUC,OggettoBulk tariffario) throws ComponentException{
   return super.modificaConBulk(aUC, tariffario);
}
public it.cnr.jada.persistency.sql.Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {

	SQLBuilder sql = (SQLBuilder)super.select(userContext, clauses, bulk);
	sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));

	sql.addClause(clauses);
	return sql;

}
public it.cnr.jada.persistency.sql.SQLBuilder selectVoce_ivaByClause(UserContext aUC,TariffarioBulk tariffario, it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk voce_iva, it.cnr.jada.persistency.sql.CompoundFindClause clauses) 
	throws ComponentException {
	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,voce_iva).createSQLBuilder();

	sql.addSQLClause("AND","CD_VOCE_IVA",sql.EQUALS,voce_iva.getCd_voce_iva());
	sql.addSQLClause("AND","TI_APPLICAZIONE",sql.NOT_EQUALS,voce_iva.ACQUISTI);


	sql.addClause(clauses);
	return sql;
}
//^^@@
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      Viene consentito il salvataggio della nuova tariffa
  *  Tariffa non valida.
  *    PreCondition:
  *      Si è verificato un errore.
  *    PostCondition:
  *      Viene inviato il messaggio : "Attenzione tariffa non valida"
 */
//^^@@
private boolean validaTariffa (UserContext aUC,it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk tariffa) throws ComponentException{

   boolean accepted = false;
	
	try{
	   it.cnr.contab.docamm00.tabrif.bulk.TariffarioHome tariffarioH = (it.cnr.contab.docamm00.tabrif.bulk.TariffarioHome)getHome(aUC,it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk.class);
	   if (!tariffarioH.checkPeriodi(aUC, tariffa))
	      throw new it.cnr.jada.comp.ApplicationException("Attenzione tariffa non valida");
	   else
	      accepted = true;
	      
	   return accepted;
	}
	catch(it.cnr.jada.persistency.PersistencyException e){
		throw new it.cnr.jada.comp.ApplicationException(e);
	}
	catch(Throwable e){
		throw handleException(e);
	}
}
public java.util.List findListaTariffariWS(UserContext userContext,String uo,String query,String dominio,String tipoRicerca)throws ComponentException{
	try {	
		TariffarioHome home =(TariffarioHome)getHome(userContext, TariffarioBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		java.sql.Timestamp date = home.getServerDate();
		sql.addSQLClause("AND","DT_INI_VALIDITA",sql.LESS_EQUALS,date);
		sql.addSQLClause("AND","DT_FINE_VALIDITA",sql.GREATER_EQUALS,date);
		if (uo !=null)
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,uo);
		if (dominio.equalsIgnoreCase("codice"))
			sql.addSQLClause("AND","CD_TARIFFARIO",SQLBuilder.EQUALS,query);
		else if (dominio.equalsIgnoreCase("descrizione")){
			
				sql.openParenthesis("AND");
				for(StringTokenizer stringtokenizer = new StringTokenizer(query, " "); stringtokenizer.hasMoreElements();){
					String queryDetail = stringtokenizer.nextToken();
					if ((tipoRicerca != null && tipoRicerca.equalsIgnoreCase("selettiva"))|| tipoRicerca == null){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail)))
							sql.addSQLClause("AND","DS_TARIFFARIO",SQLBuilder.CONTAINS,queryDetail);
						else{
							sql.openParenthesis("AND");
							sql.addSQLClause("OR","DS_TARIFFARIO",SQLBuilder.CONTAINS,queryDetail);
							sql.addSQLClause("OR","DS_TARIFFARIO",SQLBuilder.CONTAINS,RemoveAccent.convert(queryDetail));
							sql.closeParenthesis();
}
					}else if (tipoRicerca.equalsIgnoreCase("puntuale")){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail))){
							sql.openParenthesis("AND");
							  sql.addSQLClause("AND","UPPER(DS_TARIFFARIO)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							  sql.addSQLClause("OR","DS_TARIFFARIO",SQLBuilder.STARTSWITH,queryDetail+" ");
							  sql.addSQLClause("OR","DS_TARIFFARIO",SQLBuilder.ENDSWITH," "+queryDetail);
							sql.closeParenthesis();  
						}else{
							sql.openParenthesis("AND");
							  sql.openParenthesis("AND");
							    sql.addSQLClause("OR","UPPER(DS_TARIFFARIO)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							    sql.addSQLClause("OR","UPPER(DS_TARIFFARIO)",SQLBuilder.EQUALS,RemoveAccent.convert(queryDetail).toUpperCase());
							  sql.closeParenthesis();
							  sql.openParenthesis("OR");							  
							    sql.addSQLClause("OR","DS_TARIFFARIO",SQLBuilder.STARTSWITH,queryDetail+" ");
							    sql.addSQLClause("OR","DS_TARIFFARIO",SQLBuilder.STARTSWITH,RemoveAccent.convert(queryDetail)+" ");
							  sql.closeParenthesis();  
							  sql.openParenthesis("OR");
							    sql.addSQLClause("OR","DS_TARIFFARIO",SQLBuilder.ENDSWITH," "+queryDetail);
							    sql.addSQLClause("OR","DS_TARIFFARIO",SQLBuilder.ENDSWITH," "+RemoveAccent.convert(queryDetail));
							  sql.closeParenthesis();  
							sql.closeParenthesis();  
						}
					}
				}
				sql.closeParenthesis();
				sql.addOrderBy("DS_TARIFFARIO");
			}
		
		return home.fetchAll(sql);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
}

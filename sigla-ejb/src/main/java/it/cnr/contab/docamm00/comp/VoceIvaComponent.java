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

import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaHome;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;

public class VoceIvaComponent 
	extends it.cnr.jada.comp.CRUDComponent 
	implements ICRUDMgr,IVoceIvaMgr,Cloneable,Serializable {

    public  VoceIvaComponent()
    {

        /*Default constructor*/


    }
//^^@@
/** 
  *  Voce iva non verificata.
  *    PreCondition:
  *      Una voce iva di default esiste gia.
  *    PostCondition:
  *      restituisce false come valore
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessuna voce iva di default preesistente.
  *    PostCondition:
  *      restituisce true
 */
//^^@@
public Voce_ivaBulk caricaVoceIvaDefault(UserContext aUC) throws ComponentException {

	try {
		Voce_ivaHome home = (Voce_ivaHome)getHome(aUC, Voce_ivaBulk.class);
		return home.loadDefault();
	}
	catch (Exception e)	{
		throw handleException(e);
	}   			 	    
}
//^^@@
/** 
  *  tutti i controlli superati
  *    PreCondition:
  *      validaVoceIva = true
  *    PostCondition:
  *      Consente la modifica della voce iva.
  *  validaVoceIva  non superata
  *    PreCondition:
  *      validaVoceIva = False.
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione, una voce iva di default è gia presente".
 */
//^^@@
public OggettoBulk creaConBulk(UserContext aUC, OggettoBulk voceIva)
    throws ComponentException {
	    
    try {
		
        Voce_ivaHome vocH = (Voce_ivaHome) getHome(aUC, Voce_ivaBulk.class);
        if (((Voce_ivaBulk) voceIva).isDefault_istituzionale())
            vocH.checkDefaultIstituzionale(voceIva);
        vocH.checkPercentuale(voceIva);
        //validaVoceIva(aUC,((Voce_ivaBulk) voceIva));
    } catch (Exception e) {
        throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
    }
    return super.creaConBulk(aUC, voceIva);
}
//^^@@
/** 
  *  tutti i controlli superati
  *    PreCondition:
  *      validaVoceIva = true
  *    PostCondition:
  *      Consente la modifica della voce iva.
  *  validaVoceIva  non superata
  *    PreCondition:
  *      validaVoceIva = False.
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione, una voce iva di default è gia presente".
 */
//^^@@
public OggettoBulk modificaConBulk(UserContext aUC, OggettoBulk voceIva)
    throws ComponentException {
	    
    try {
	    
        Voce_ivaHome vocH = (Voce_ivaHome) getHome(aUC, Voce_ivaBulk.class);
        if (((Voce_ivaBulk) voceIva).isDefault_istituzionale())
            vocH.checkDefaultIstituzionale(voceIva);
        vocH.checkPercentuale(voceIva);
        //validaVoceIva(aUC,((Voce_ivaBulk) voceIva));
    } catch (Exception e) {
        throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
    }
    return super.modificaConBulk(aUC, voceIva);
}
public it.cnr.jada.persistency.sql.SQLBuilder selectGruppo_ivaByClause(UserContext aUC,Voce_ivaBulk voce_iva, it.cnr.contab.docamm00.tabrif.bulk.Gruppo_ivaBulk gruppo_iva, it.cnr.jada.persistency.sql.CompoundFindClause clauses) 
	throws ComponentException {

	if (voce_iva.getPercentuale() == null)
		throw new it.cnr.jada.comp.ApplicationException("E' necessario specificare una percentuale");
		
	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,gruppo_iva).createSQLBuilder();

	it.cnr.jada.persistency.sql.SQLBuilder sql_voce_iva = getHome(aUC,Voce_ivaBulk.class).createSQLBuilder();
    sql_voce_iva.addSQLJoin("GRUPPO_IVA.CD_GRUPPO_IVA", "VOCE_IVA.CD_GRUPPO_IVA(+)");
	sql_voce_iva.addClause("AND","percentuale",sql.NOT_EQUALS,voce_iva.getPercentuale());

	sql.addSQLNotExistsClause("AND",sql_voce_iva);

	sql.addClause(clauses);
	
	return sql;
}
//^^@@
/** 
  *  Voce iva non verificata.
  *    PreCondition:
  *      Una voce iva di default esiste gia.
  *    PostCondition:
  *      restituisce false come valore
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessuna voce iva di default preesistente.
  *    PostCondition:
  *      restituisce true
 */
//^^@@
public boolean validaVoceIva(UserContext aUC, Voce_ivaBulk voceIva) {
    try {
        //controlla se esiste una voce iva di default
        //
		//Voce_ivaHome home= (Voce_ivaHome) getHome(aUC, Voce_ivaBulk.class);
        //it.cnr.jada.persistency.sql.SQLBuilder sql= home.createSQLBuilder();

        //sql.addTableToHeader("VOCE_IVA");
        ////imposto le chiavi
        //sql.addSQLJoin("VOCE_IVA.CD_GRUPPO_IVA", "GRUPPO_IVA.CD_GRUPPO_IVA");
		//sql.addSQLClause("AND","ACCERTAMENTO_SCADENZARIO.IM_SCADENZA",sql.EQUALS, voceIva.getPercentuale());


        Voce_ivaHome vocH= (Voce_ivaHome) getHome(aUC, Voce_ivaBulk.class);
        vocH.checkPercentuale(voceIva);

        return true;
    } catch (Exception e) {
        return false;
    }

}
public java.util.List findListaVoceIVAWS(UserContext userContext,String query,String dominio,String tipoRicerca)throws ComponentException{
	try {		
		
		Voce_ivaHome home = (Voce_ivaHome)getHome(userContext,Voce_ivaBulk.class);
		SQLBuilder sql = (SQLBuilder)super.select( userContext,null,new Voce_ivaBulk());
		sql.addSQLClause( "AND", "dt_cancellazione", sql.ISNULL,null);
		if (dominio.equalsIgnoreCase("codice"))
			sql.addSQLClause("AND","CD_VOCE_IVA",SQLBuilder.EQUALS,query);
		else if (dominio.equalsIgnoreCase("descrizione")){
			
				sql.openParenthesis("AND");
				for(StringTokenizer stringtokenizer = new StringTokenizer(query, " "); stringtokenizer.hasMoreElements();){
					String queryDetail = stringtokenizer.nextToken();
					if ((tipoRicerca != null && tipoRicerca.equalsIgnoreCase("selettiva"))|| tipoRicerca == null){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail)))
							sql.addSQLClause("AND","DS_VOCE_IVA",SQLBuilder.CONTAINS,queryDetail);
						else{
							sql.openParenthesis("AND");
							sql.addSQLClause("OR","DS_VOCE_IVA",SQLBuilder.CONTAINS,queryDetail);
							sql.addSQLClause("OR","DS_VOCE_IVA",SQLBuilder.CONTAINS,RemoveAccent.convert(queryDetail));
							sql.closeParenthesis();
}
					}else if (tipoRicerca.equalsIgnoreCase("puntuale")){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail))){
							sql.openParenthesis("AND");
							  sql.addSQLClause("AND","UPPER(DS_VOCE_IVA)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							  sql.addSQLClause("OR","DS_VOCE_IVA",SQLBuilder.STARTSWITH,queryDetail+" ");
							  sql.addSQLClause("OR","DS_VOCE_IVA",SQLBuilder.ENDSWITH," "+queryDetail);
							sql.closeParenthesis();  
						}else{
							sql.openParenthesis("AND");
							  sql.openParenthesis("AND");
							    sql.addSQLClause("OR","UPPER(DS_VOCE_IVA)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							    sql.addSQLClause("OR","UPPER(DS_VOCE_IVA)",SQLBuilder.EQUALS,RemoveAccent.convert(queryDetail).toUpperCase());
							  sql.closeParenthesis();
							  sql.openParenthesis("OR");							  
							    sql.addSQLClause("OR","DS_VOCE_IVA",SQLBuilder.STARTSWITH,queryDetail+" ");
							    sql.addSQLClause("OR","DS_VOCE_IVA",SQLBuilder.STARTSWITH,RemoveAccent.convert(queryDetail)+" ");
							  sql.closeParenthesis();  
							  sql.openParenthesis("OR");
							    sql.addSQLClause("OR","DS_VOCE_IVA",SQLBuilder.ENDSWITH," "+queryDetail);
							    sql.addSQLClause("OR","DS_VOCE_IVA",SQLBuilder.ENDSWITH," "+RemoveAccent.convert(queryDetail));
							  sql.closeParenthesis();  
							sql.closeParenthesis();  
						}
					}
				}
				sql.closeParenthesis();
				sql.addOrderBy("CD_VOCE_IVA,DS_VOCE_IVA");
			}
		
		return home.fetchAll(sql);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
} 
}

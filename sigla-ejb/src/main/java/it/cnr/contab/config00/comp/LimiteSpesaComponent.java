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

package it.cnr.contab.config00.comp;


import java.math.BigDecimal;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.LimiteSpesaBulk;
import it.cnr.contab.config00.pdcfin.bulk.LimiteSpesaDetBulk;
import it.cnr.contab.config00.pdcfin.bulk.LimiteSpesaDetHome;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class LimiteSpesaComponent extends it.cnr.jada.comp.CRUDComponent implements  java.io.Serializable, Cloneable {
public  LimiteSpesaComponent()
{
}

public OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
try 
	{
		validaFonte(userContext, bulk);
		return super.creaConBulk(userContext,bulk);
	} 
	catch (Throwable e){
		throw handleException(bulk,e);
	}
}

public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk)
	throws ComponentException
{
	oggettobulk = super.inizializzaBulkPerInserimento(usercontext,oggettobulk);
	LimiteSpesaBulk limite=(LimiteSpesaBulk) oggettobulk;
	limite.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(usercontext));
	limite.setImporto_assegnato(BigDecimal.ZERO);
	limite.setElementoVoce(new Elemento_voceBulk());
	return limite;
}	

public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try 
	{
		bulk = super.inizializzaBulkPerModifica(userContext,bulk);
		LimiteSpesaBulk limite=(LimiteSpesaBulk) bulk;
		LimiteSpesaDetHome dettHome = (LimiteSpesaDetHome)getHome(userContext,LimiteSpesaDetBulk.class);
		limite.setDettagli(new BulkList(dettHome.getDetailsFor(limite)));
		CdsHome cdsHome=(CdsHome)getHome(userContext,CdsBulk.class);
		for (java.util.Iterator i=limite.getDettagli().iterator();i.hasNext();){
			LimiteSpesaDetBulk det=(LimiteSpesaDetBulk)i.next();
			det.setCds((CdsBulk)cdsHome.findByPrimaryKey(new CdsBulk(det.getCd_cds())));
		}
		return limite;	
	} catch(Exception e) {
		throw handleException(e);
	}
}
 @Override
	public void eliminaConBulk(UserContext usercontext, OggettoBulk bulk)
			throws ComponentException {
	 if(bulk instanceof LimiteSpesaBulk){
         LimiteSpesaBulk lim= (LimiteSpesaBulk)bulk;
         if(lim.getDettagli()!=null && lim.getDettagli().size()!=0)
	        	 throw new ApplicationException("Eliminare prima i dettagli.");
         if (lim.getImporto_assegnato().compareTo(BigDecimal.ZERO)!=0)
        	 throw new ApplicationException("Non è possibile eliminare questo limite, è già stato assegnato ai Cds.");
	 
		super.eliminaConBulk(usercontext, bulk);
	 }
	}
public OggettoBulk modificaConBulk(UserContext userContext, OggettoBulk bulk)
	throws it.cnr.jada.comp.ComponentException {
	try 
	{
		validaFonte(userContext, bulk);
		super.modificaConBulk(userContext, bulk);
	}catch (Throwable e) 
	{
		throw handleException(bulk,e);
	}
	return bulk;
}

public SQLBuilder selectElementoVoceByClause (UserContext userContext,LimiteSpesaBulk limite, Elemento_voceBulk voce, CompoundFindClause clause) throws ComponentException{
	SQLBuilder sql = getHome(userContext, Elemento_voceBulk.class).createSQLBuilder();
	
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));		
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS,"S");
	sql.addClause("AND", "fl_limite_spesa", SQLBuilder.EQUALS,Boolean.TRUE);
	sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS,  Elemento_voceHome.TIPO_CAPITOLO );
	sql.addClause( clause );
	return sql;
}
private void validaFonte(UserContext userContext,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
	try{
		LimiteSpesaBulk limite=(LimiteSpesaBulk)bulk;
		if(limite.isToBeCreated()){
			SQLBuilder sql = getHome(userContext, LimiteSpesaBulk.class).createSQLBuilder();
			sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS,limite.getEsercizio());
			sql.addSQLClause("AND", "TI_APPARTENENZA", SQLBuilder.EQUALS, limite.getTi_appartenenza());
			sql.addSQLClause("AND", "TI_GESTIONE", SQLBuilder.EQUALS, limite.getTi_gestione());
			sql.addSQLClause("AND", "CD_ELEMENTO_VOCE", SQLBuilder.EQUALS, limite.getCd_elemento_voce());
			sql.addSQLClause("AND", "FONTE", SQLBuilder.EQUALS, limite.getFonte());
			if (sql.executeCountQuery(getConnection(userContext))>0)
				throw new ApplicationException("Esiste già un limite definito per questa voce."); 
		}
		if(limite.getFonte().compareTo(LimiteSpesaBulk.FONTE_INTERNA_E_ESTERNA)==0){
				SQLBuilder sql = getHome(userContext, LimiteSpesaBulk.class).createSQLBuilder();
				sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS,limite.getEsercizio());
				sql.addSQLClause("AND", "TI_APPARTENENZA", SQLBuilder.EQUALS, limite.getTi_appartenenza());
				sql.addSQLClause("AND", "TI_GESTIONE", SQLBuilder.EQUALS, limite.getTi_gestione());
				sql.addSQLClause("AND", "CD_ELEMENTO_VOCE", SQLBuilder.EQUALS, limite.getCd_elemento_voce());
				sql.addSQLClause("AND", "FONTE", SQLBuilder.NOT_EQUALS, limite.getFonte());
				if (sql.executeCountQuery(getConnection(userContext))>0)
					throw new ApplicationException("Esiste già un limite definito per questa voce."); 
		}else if(limite.getFonte().compareTo(LimiteSpesaBulk.FONTE_INTERNA)==0||limite.getFonte().compareTo(LimiteSpesaBulk.FONTE_ESTERNA)==0){
				SQLBuilder sql = getHome(userContext, LimiteSpesaBulk.class).createSQLBuilder();
				sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS,limite.getEsercizio());
				sql.addSQLClause("AND", "TI_APPARTENENZA", SQLBuilder.EQUALS, limite.getTi_appartenenza());
				sql.addSQLClause("AND", "TI_GESTIONE", SQLBuilder.EQUALS, limite.getTi_gestione());
				sql.addSQLClause("AND", "CD_ELEMENTO_VOCE", SQLBuilder.EQUALS, limite.getCd_elemento_voce());
				sql.addSQLClause("AND", "FONTE", SQLBuilder.EQUALS, LimiteSpesaBulk.FONTE_INTERNA_E_ESTERNA);
				if (sql.executeCountQuery(getConnection(userContext))>0)
					throw new ApplicationException("Esiste già un limite definito per questa voce."); 
		 }
		
	}catch (Throwable e) {
		throw handleException(bulk,e);
	}
}
public void validaCds(UserContext userContext,OggettoBulk bulk) throws ComponentException {
	try{
		
		LimiteSpesaDetBulk limite=(LimiteSpesaDetBulk)bulk;
		if(limite.isToBeCreated()){
			SQLBuilder sql = getHome(userContext, LimiteSpesaDetBulk.class).createSQLBuilder();
			sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS,limite.getEsercizio());
			sql.addSQLClause("AND", "TI_APPARTENENZA", SQLBuilder.EQUALS, limite.getTi_appartenenza());
			sql.addSQLClause("AND", "TI_GESTIONE", SQLBuilder.EQUALS, limite.getTi_gestione());
			sql.addSQLClause("AND", "CD_ELEMENTO_VOCE", SQLBuilder.EQUALS, limite.getCd_elemento_voce());
			sql.addSQLClause("AND", "FONTE", SQLBuilder.EQUALS, limite.getFonte());
			sql.addSQLClause("AND", "CD_CDS", SQLBuilder.EQUALS, limite.getCd_cds());
			if (sql.executeCountQuery(getConnection(userContext))>0)
				throw new ApplicationException("Esiste già un limite definito per questa voce e per il Cds: "+limite.getCd_cds());
		} 
	}catch (Throwable e) {
		throw handleException(bulk,e);
	}
}

}

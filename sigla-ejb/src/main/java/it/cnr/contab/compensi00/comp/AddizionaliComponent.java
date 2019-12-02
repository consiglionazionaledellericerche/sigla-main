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

package it.cnr.contab.compensi00.comp;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneHome;
import it.cnr.contab.compensi00.tabrif.bulk.AddizionaliBulk;
import it.cnr.contab.compensi00.tabrif.bulk.AddizionaliHome;
import it.cnr.contab.compensi00.tabrif.bulk.ScaglioneBulk;
import it.cnr.contab.compensi00.tabrif.bulk.ScaglioneHome;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class AddizionaliComponent extends it.cnr.jada.comp.CRUDComponent{

	public AddizionaliComponent() {
		super();
	}

 public AddizionaliBulk verifica_aggiornamento(UserContext usercontext, AddizionaliBulk addizionale) throws ComponentException {
	ScaglioneBulk scaglione=null;
	try{
		ComuneHome home = (ComuneHome)getHome(usercontext, ComuneBulk.class);
		ComuneBulk comune = home.findComune(usercontext, addizionale.getCd_catastale());
		if(comune!=null && comune.getPg_comune()!=null){
			ScaglioneHome home_scaglione =(ScaglioneHome)getHome(usercontext,ScaglioneBulk.class);
			scaglione =home_scaglione.findScaglione(usercontext, addizionale, comune);
		}else{
			addizionale.setNota("Comune non trovato - Codice Catastale: "+addizionale.getCd_catastale()+" - "+addizionale.getDs_comune());
		}
		if(comune.getDs_comune()!=null && comune.getDs_comune().compareTo(addizionale.getDs_comune())!=0)
			addizionale.setNota("Descrizione Comune non verificata - Codice Catastale: "+addizionale.getCd_catastale()+" - "+addizionale.getDs_comune()+" - in Archivio - "+comune.getDs_comune());
		
		if (scaglione != null && scaglione.getAliquota()!=null && scaglione.getAliquota().compareTo(addizionale.getAliquota())!=0){
			addizionale.setOld_aliquota(scaglione.getAliquota());
			addizionale.setToBeCreated();
			creaConBulk(usercontext, addizionale);
			return addizionale;
		}else if (addizionale.getNota()== null && scaglione == null)
			addizionale.setNota("Non esistono scaglioni da aggiornare o esistono più scaglioni validi - Codice Catastale: "+addizionale.getCd_catastale()+" - "+addizionale.getDs_comune());
			if(addizionale.getNota()!=null){
				addizionale.setOld_aliquota(null);
				addizionale.setToBeCreated();
				creaConBulk(usercontext, addizionale);
				return addizionale;
			}
		else
			return null;
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}	
 }
 public void Aggiornamento_scaglione(UserContext usercontext, AddizionaliBulk addizionale) throws ComponentException {
	 try{
		ScaglioneBulk scaglione=null;
		ScaglioneBulk nuovo_scaglione=null;
		for(Iterator i=addizionale.getDettagli().iterator();i.hasNext();){
			AddizionaliBulk nuova=(AddizionaliBulk)i.next();
			ComuneHome home = (ComuneHome)getHome(usercontext, ComuneBulk.class);
			ComuneBulk comune = home.findComune(usercontext, nuova.getCd_catastale());
			if(comune!=null){
				ScaglioneHome home_scaglione =(ScaglioneHome)getHome(usercontext,ScaglioneBulk.class);
				scaglione =home_scaglione.findScaglione(usercontext, nuova, comune);
			}
			nuovo_scaglione=scaglione;
			scaglione.setToBeUpdated();
			scaglione.setDt_fine_validita(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
			modificaConBulk(usercontext,scaglione);
			java.util.GregorianCalendar gc = getGregorianCalendar();
			gc.setTime(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
			gc.add(java.util.Calendar.DAY_OF_YEAR,+1);
			nuovo_scaglione.setDt_inizio_validita(new Timestamp(gc.getTime().getTime()));
			nuovo_scaglione.setDt_fine_validita(EsercizioHome.DATA_INFINITO);
			nuovo_scaglione.setAliquota(nuova.getAliquota());
			nuovo_scaglione.setCrudStatus(OggettoBulk.TO_BE_CREATED);
			super.creaConBulk(usercontext,nuovo_scaglione);		
			nuova.setToBeDeleted();
			eliminaConBulk(usercontext,nuova);
		}
			
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}	

	}
 protected java.util.GregorianCalendar getGregorianCalendar() {

		java.util.GregorianCalendar gc = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
		
		gc.set(java.util.Calendar.HOUR, 0);
		gc.set(java.util.Calendar.MINUTE, 0);
		gc.set(java.util.Calendar.SECOND, 0);
		gc.set(java.util.Calendar.MILLISECOND, 0);
		gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
		
		return gc;
	}
 public void cancella_pendenti(UserContext usercontext) throws ComponentException{
	 AddizionaliHome home=(AddizionaliHome)getHome(usercontext, AddizionaliBulk.class);
	 SQLBuilder sql = home.createSQLBuilder();
	 try {
		List canc = home.fetchAll(sql);
		for (Iterator i=canc.iterator();i.hasNext();){
			AddizionaliBulk bulk=(AddizionaliBulk)i.next();
			bulk.setToBeDeleted();
			eliminaConBulk(usercontext,bulk);
		}
	} catch (PersistencyException e) {
		handleException(e);
	} 
 }
}
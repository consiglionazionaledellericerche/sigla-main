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

package it.cnr.contab.pdg00.comp;

import it.cnr.contab.config00.esercizio.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.pdg00.bulk.*;
import it.cnr.contab.prevent00.bulk.Pdg_aggregato_etr_detBulk;
import it.cnr.contab.prevent00.bulk.Pdg_aggregato_spe_detBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;

import java.io.Serializable;
import java.util.Vector;

/**
  * Componente contenitore dei metodi comuni di controllo della responsabilità per piani di gestione
  */


public abstract class PdGComponent extends it.cnr.jada.comp.CRUDComponent implements IPdGMgr {

	public static final int LV_AC   = 0; // Amministazione centrale
	public static final int LV_CDRI = 1; // CDR I 00
	public static final int LV_RUO  = 2; // CDR II responsabile
	public static final int LV_NRUO = 3; // CDR II non responsabile



//^^@@
/** 
  *  default
  *    PreCondition:
  *      Viene richiesto il cdr dell'utente connesso
  *    PostCondition:
  *      Restituisce il cdr a cui appartiene l'utente specificato:
  *      esercizio = esercizio selezionato in scrivania
  *      codice = UTENTE.CD_CDR
 */
//^^@@
	protected CdrBulk cdrFromUserContext(UserContext userContext) throws ComponentException {
		try {
			it.cnr.contab.utenze00.bulk.UtenteBulk user = new it.cnr.contab.utenze00.bulk.UtenteBulk( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser() );
			user = (it.cnr.contab.utenze00.bulk.UtenteBulk)getHome(userContext, user).findByPrimaryKey(user);

			CdrBulk cdr = new CdrBulk( it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext) );
			return (CdrBulk)getHome(userContext, cdr).findByPrimaryKey(cdr);

		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw new ComponentException(e);
		}
	}

//^^@@
/**
 *  PDG già chiuso
 *    PreCondition:
 *      Il pdg a cui appartiene il dettaglio specificato è chiuso (stato = A,B,D o E)
 *    PostCondition:
 *      Genera una ApplicationException con il messaggio "Il pdg è stato chiuso e non può essere modificato"
 *  Tutti i controlli superati
 *    PreCondition:
 *      Nessun'altra precondizione è verificata
 *    PostCondition:
 *      Esce senza nessuna eccezione
 */
//^^@@
protected Pdg_preventivoBulk checkChiusuraPdg(UserContext userContext,Pdg_preventivo_detBulk pdgDett) throws ComponentException {
	 try {
		return checkChiusuraPdg(
			userContext,
			(Pdg_preventivoBulk)getHome(userContext,Pdg_preventivoBulk.class).findByPrimaryKey(new Pdg_preventivoBulk(
				pdgDett.getEsercizio(), 
				pdgDett.getCd_centro_responsabilita())),
				pdgDett);
	 } catch(Throwable e) {
		throw handleException(e);
	 }
}
/** 
  *  PDG già chiuso
  *    PreCondition:
  *      Il pdg a cui appartiene il dettaglio specificato è chiuso (stato = A,B,Ci,D o E)
  *    PostCondition:
  *      Genera una ApplicationException con il messaggio "Il pdg è stato chiuso e non può essere modificato"
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessun'altra precondizione è verificata
  *    PostCondition:
  *      Esce senza nessuna eccezione
 */
protected Pdg_preventivoBulk checkChiusuraPdg(UserContext userContext,Pdg_preventivoBulk pdg,Pdg_preventivo_detBulk pdg_dett) throws ComponentException {
	try {
		lockBulk(userContext,pdg);
		
		if (pdg.getStato().equals(Pdg_preventivoBulk.ST_A_CREAZIONE) ||
			pdg.getStato().equals(Pdg_preventivoBulk.ST_B_MODIFICA) ||
			pdg.getStato().equals(Pdg_preventivoBulk.ST_C0_MODIFICA_AC) ||
			pdg.getStato().equals(Pdg_preventivoBulk.ST_C1_MODIFC_CDRI) ||
			pdg.getStato().equals(Pdg_preventivoBulk.ST_C2_MODIFIC_RUO) ||
			pdg.getStato().equals(Pdg_preventivoBulk.ST_D_CHIUSURA_I) ||
			pdg.getStato().equals(Pdg_preventivoBulk.ST_E_CHIUSO) ||
			pdg.getStato().equals(Pdg_preventivoBulk.ST_G_APERTURA_PER_VARIAZIONI) ||
			pdg.getStato().equals(Pdg_preventivoBulk.ST_H_PRECHIUSURA_PER_VARIAZIONI)||
		    (pdg_dett != null && pdg_dett.isDaVariazione()))

			return pdg;

		throw new it.cnr.jada.comp.ApplicationException("Il PdG del CdR \"" + pdg.getCentro_responsabilita().getCd_ds_cdr() + "\" è stato chiuso e non può essere modificato");
	} catch(it.cnr.jada.bulk.BusyResourceException e) {
		throw new it.cnr.jada.comp.ApplicationException("Risorsa occupata: PdG del CdR " + pdg.getCentro_responsabilita().getCd_ds_cdr());
	} catch(it.cnr.jada.bulk.OutdatedResourceException e) {
		throw new it.cnr.jada.comp.ApplicationException("Risorsa scaduta: PdG del CdR " + pdg.getCentro_responsabilita().getCd_ds_cdr());
	} catch(PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  PDG già chiuso
  *    PreCondition:
  *      Il pdg a cui appartiene il dettaglio specificato è chiuso (stato = A,B,Ci,D o E)
  *    PostCondition:
  *      Genera una ApplicationException con il messaggio "Il pdg è stato chiuso e non può essere modificato"
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessun'altra precondizione è verificata
  *    PostCondition:
  *      Esce senza nessuna eccezione
 */

protected Pdg_preventivoBulk checkChiusuraPdgPerVariazioni(UserContext userContext,Pdg_preventivoBulk pdg) throws ComponentException {
	try {
		lockBulk(userContext,pdg);
		
		if (pdg.getStato().equals(Pdg_preventivoBulk.ST_G_APERTURA_PER_VARIAZIONI) ||
			pdg.getStato().equals(Pdg_preventivoBulk.ST_H_PRECHIUSURA_PER_VARIAZIONI))

			return pdg;

		throw new it.cnr.jada.comp.ApplicationException("Il PdG del CdR \"" + pdg.getCentro_responsabilita().getCd_ds_cdr() + "\" è stato chiuso e non può essere modificato");
	} catch(it.cnr.jada.bulk.BusyResourceException e) {
		throw new it.cnr.jada.comp.ApplicationException("Risorsa occupata: PdG del CdR " + pdg.getCentro_responsabilita().getCd_ds_cdr());
	} catch(it.cnr.jada.bulk.OutdatedResourceException e) {
		throw new it.cnr.jada.comp.ApplicationException("Risorsa scaduta: PdG del CdR " + pdg.getCentro_responsabilita().getCd_ds_cdr());
	} catch(PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  PDG in stato A,B, utente non abilitato
  *    PreCondition:
  *      Lo stato del pdg è A o B e  confrontaLivelloResponsabilita(cdrFromUserContext(),pdg.cdr) != 0
  *    PostCondition:
  *      Genera un ApplicationException con il messaggio "PDG non modificabile o l'utente non ha diritti per la modifica"
  *  PDG in stato Ci, utente non abilitato
  *    PreCondition:
  *      Lo stato del pdg è Ci e getLivelloResponsabilitaCDR(cdrFromUserContext()) != i
  *    PostCondition:
  *      Genera un ApplicationException con il messaggio "PDG non modificabile o l'utente non ha diritti per la modifica"
  *  PDG in stato D,E,G,H utente non abilitato
  *    PreCondition:
  *      Lo stato del pdg è D,E,G o H e  confrontaLivelloResponsabilita(cdrFromUserContext(),pdg.cdr) < 0
  *    PostCondition:
  *      Genera un ApplicationException con il messaggio "PDG non modificabile o l'utente non ha diritti per la modifica"
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessun'altra precondizione è verificata
  *    PostCondition:
  *      Esce senza nessuna eccezione
 */
protected void checkLivelloResponsabilita(UserContext userContext,CdrBulk cdr, Pdg_preventivoBulk pdg) throws ComponentException {
		int livelloCdR = getLivelloResponsabilitaCDR(userContext, cdr);

		if (((pdg.getStato().equals(pdg.ST_A_CREAZIONE) ||
			  pdg.getStato().equals(pdg.ST_B_MODIFICA)) && 
			 confrontaLivelloResponsabilita(userContext, cdr, pdg.getCentro_responsabilita()) != 0) ||
		
			(pdg.getStato().equals(pdg.ST_C0_MODIFICA_AC) && livelloCdR != LV_AC) ||
			
			(pdg.getStato().equals(pdg.ST_C1_MODIFC_CDRI) && livelloCdR != LV_CDRI) ||
			
			(pdg.getStato().equals(pdg.ST_C2_MODIFIC_RUO) && livelloCdR != LV_RUO) ||
			
			((pdg.getStato().equals(pdg.ST_D_CHIUSURA_I) || 
			  pdg.getStato().equals(pdg.ST_E_CHIUSO) ||
			  pdg.getStato().equals(pdg.ST_G_APERTURA_PER_VARIAZIONI) ||
			  pdg.getStato().equals(pdg.ST_H_PRECHIUSURA_PER_VARIAZIONI)) &&
			 confrontaLivelloResponsabilita(userContext, cdr, pdg.getCentro_responsabilita()) < 0) ) {
				 
			throw new it.cnr.jada.comp.ApplicationException("PDG non modificabile o l'utente non ha diritti per la modifica");
		}
}
//^^@@
/** 
  *  cdr1 = cdr2
  *    PreCondition:
  *      Se il CDR dell'utente in UserContext coincide (per CD_CENTRO_RESPONSABILITA) al cdr specificato
  *    PostCondition:
  *      Restituisce 0
  *  cdr1con valore livello responsabilità maggiore uguale
  *    PreCondition:
  *      Se getLivelloResponsabilita(cdr1) >= getLivelloResponsabilita(cdr2)
  *    PostCondition:
  *      Restituisce -1
  *  non compatibili 1
  *    PreCondition:
  *      Se getLivelloResponsabilita(cdr1) < getLivelloResponsabilita(cdr2) e 
  *      getLivelloResponsabilita(cdr2) = 3 e 
  *      CD_UNITA_ORGANIZZATIVA di cdr1 != CD_UNITA_ORGANIZZATIVA di cdr2 e
  *      
  *    PostCondition:
  *      Restituisce -1
  *  non compatibili 2
  *    PreCondition:
  *      Se getLivelloResponsabilita(cdr1) < getLivelloResponsabilita(cdr2) e 
  *      getLivelloResponsabilita(cdr2) = 2 e 
  *      cdr1 è in UO_CDS
  *      
  *    PostCondition:
  *      Restituisce -1
  *  Tutti i controlli superati
  *    PreCondition:
  *      
  *    PostCondition:
  *      Restituisce 1
  *  cdr1 = AC
  *    PreCondition:
  *      getLivelloResponsabilita(cdr2) = 0
  *    PostCondition:
  *      Restituisce 1
 */
//^^@@

	protected int confrontaLivelloResponsabilita (UserContext userContext, CdrBulk cdr1,CdrBulk cdr2) throws ComponentException {
		return confrontaLivelloResponsabilita(
			userContext,
			cdr1,
			cdr2,
			getLivelloResponsabilitaCDR(userContext,cdr1),
			getLivelloResponsabilitaCDR(userContext,cdr2));
	}

//^^@@
/** 
  *  cdr1 = cdr2
  *    PreCondition:
  *      Se il CDR dell'utente in UserContext coincide (per CD_CENTRO_RESPONSABILITA) al cdr specificato
  *    PostCondition:
  *      Restituisce 0
  *  cdr1con valore livello responsabilità maggiore uguale
  *    PreCondition:
  *      Se getLivelloResponsabilita(cdr1) >= getLivelloResponsabilita(cdr2)
  *    PostCondition:
  *      Restituisce -1
  *  non compatibili 1
  *    PreCondition:
  *      Se getLivelloResponsabilita(cdr1) < getLivelloResponsabilita(cdr2) e 
  *      getLivelloResponsabilita(cdr2) = 3 e 
  *      CD_UNITA_ORGANIZZATIVA di cdr1 != CD_UNITA_ORGANIZZATIVA di cdr2 e
  *      
  *    PostCondition:
  *      Restituisce -1
  *  non compatibili 2
  *    PreCondition:
  *      Se getLivelloResponsabilita(cdr1) < getLivelloResponsabilita(cdr2) e 
  *      getLivelloResponsabilita(cdr2) = 2 e 
  *      cdr1 è in UO_CDS
  *      
  *    PostCondition:
  *      Restituisce -1
  *  Tutti i controlli superati
  *    PreCondition:
  *      
  *    PostCondition:
  *      Restituisce 1
  *  cdr1 = AC
  *    PreCondition:
  *      getLivelloResponsabilita(cdr2) = 0
  *    PostCondition:
  *      Restituisce 1
 */
//^^@@

	protected int confrontaLivelloResponsabilita (UserContext userContext,CdrBulk cdr1,CdrBulk cdr2,int livello1,int livello2) throws ComponentException {

		if(cdr1.getCd_centro_responsabilita().equals(cdr2.getCd_centro_responsabilita()))
			return 0;

		if(livello1 == LV_AC)
			return 1;

		if(livello1 >= livello2)
			return -1;

		return 1;
	}

/** 
  *  CDR NRUO
  *    PreCondition:
  *      se CD_PROPRIO != '00'
  *    PostCondition:
  *      Restituisce 3
  *  CDR RUO
  *    PreCondition:
  *      CD_PROPRIO = '00' e LIVELLO = 2
  *      
  *    PostCondition:
  *      Restituisce 2
  *  CDR I
  *    PreCondition:
  *      LIVELLO = 1
  *    PostCondition:
  *      Restituisce 1
  *  AC
  *    PreCondition:
  *      LIVELLO = 1 e UNITA_ORGANIZZATIVA.CD_TIPO_UNITA = 'ENTE'
  *    PostCondition:
  *      Restituisce 0
 */

protected int getLivelloResponsabilitaCDR(UserContext userContext, CdrBulk cdr) throws ComponentException {
	try {
		cdr = (CdrBulk)getHome(userContext, cdr).findByPrimaryKey(cdr);

		// Se il livello del CDR è 1
		if(cdr.getLivello().intValue() == 1) {
			// Se il codice proprio del cdr è 0
			if (Integer.parseInt(cdr.getCd_proprio_cdr()) == 0) {
				Unita_organizzativaBulk uo = new Unita_organizzativaBulk( cdr.getCd_unita_organizzativa() );
				uo = (Unita_organizzativaBulk)getHome(userContext, uo).findByPrimaryKey(uo);

				Unita_organizzativaBulk cds = new Unita_organizzativaBulk( uo.getCd_unita_padre() );
				cds = (Unita_organizzativaBulk)getHome(userContext, cds).findByPrimaryKey(cds);

				// e ha come padre il cds dell'ente
				if (uo.getFl_uo_cds().booleanValue() == true &&
					cds.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE))

					// AC
					return LV_AC;

			}

			return LV_CDRI;

		} else if( (cdr.getLivello().intValue() == 2 && Integer.parseInt(cdr.getCd_proprio_cdr()) == 0)) {
			// Sel cdr.livello == 2 e codice proprio = 0

			return LV_RUO;

		} else {
			// Ogni altro livello o combinazione è livello 3

			return LV_NRUO;

		}
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw new ComponentException(e);
	}
}

protected String getStatoAggregato(UserContext userContext,Integer esercizio,String cd_cdr) throws ComponentException {
	try {			
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ), "{? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+ "CNRCTB050.getStatoAggregato(?,?)}",false,this.getClass());
		try {
			cs.registerOutParameter( 1, java.sql.Types.CHAR );
			cs.setObject( 2, esercizio );
			cs.setString( 3, cd_cdr);
			cs.execute();
		    return cs.getString(1);
		} finally {
		    cs.close();
		}	
	} catch (Throwable e) {
		throw handleException(e);
	}
}
protected boolean isCdrArea(UserContext userContext,CdrBulk cdr) throws ComponentException {
	try {
		Unita_organizzativaBulk uo = cdr.getUnita_padre();
		if (uo.getCd_tipo_unita() == null)
			uo = (Unita_organizzativaBulk)getHome(userContext,uo).findByPrimaryKey(uo);
		return uo.isUoArea();
	} catch(Throwable e) {
		throw handleException(e);
	}
}
protected boolean isEsercizioChiuso(UserContext userContext,Pdg_preventivoBulk pdg) throws ComponentException {
	try {
		EsercizioHome home = (EsercizioHome)getHome(userContext,EsercizioBulk.class);
		CdrBulk cdr = (CdrBulk)getHome(userContext,pdg.getCentro_responsabilita()).findByPrimaryKey(pdg.getCentro_responsabilita());
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHome(userContext,cdr.getUnita_padre()).findByPrimaryKey(cdr.getUnita_padre());
		return home.isEsercizioChiuso(userContext,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),uo.getCd_unita_padre());
	} catch(PersistencyException e) {
		throw handleException(e);
	}
}
protected boolean isPdgApertoPerVariazioni(UserContext userContext,Pdg_preventivoBulk pdg) throws ComponentException {
	String stato = pdg.getStato();

	return 
		stato.equals(pdg.ST_G_APERTURA_PER_VARIAZIONI) ||
		stato.equals(pdg.ST_H_PRECHIUSURA_PER_VARIAZIONI);
}
/**
 *  PDG in stato A,B, utente non abilitato
 *    PreCondition:
 *      Lo stato del pdg è A o B e  confrontaLivelloResponsabilita(cdrFromUserContext(),pdg.cdr) != 0
 *    PostCondition:
 *      Restituisce true
 *  PDG in stato Ci, utente non abilitato
 *    PreCondition:
 *      Lo stato del pdg è Ci e getLivelloResponsabilitaCDR(cdrFromUserContext()) != i
 *    PostCondition:
 *      Restituisce true
 *  PDG in stato D,E,G,H utente non abilitato
 *    PreCondition:
 *      Lo stato del pdg è D,E,G o H e  confrontaLivelloResponsabilita(cdrFromUserContext(),pdg.cdr) < 0
 *    PostCondition:
 *      Restituisce true
 *  Tutti i controlli superati
 *    PreCondition:
 *      Nessun'altra precondizione è verificata
 *    PostCondition:
 *      Restituisce false
 */
protected boolean isPdgModificabile(UserContext userContext,CdrBulk cdrUtente,Pdg_preventivoBulk pdg) throws ComponentException {
	String stato = pdg.getStato();

	int livelloResponsabilitaUtente = getLivelloResponsabilitaCDR(userContext, cdrUtente);
	int livelloResponsabilitaPdg = getLivelloResponsabilitaCDR(userContext, cdrUtente);

	// In stato A e B solo il cdr responsabile del pdg può modificare il pdg
	if (stato.equals(pdg.ST_A_CREAZIONE) || 
		stato.equals(pdg.ST_B_MODIFICA))
		return confrontaLivelloResponsabilita(userContext, cdrUtente, pdg.getCentro_responsabilita()) == 0;

	// In stato C0 solo i cdr di livello 0 può modificare il pdg
	if (stato.equals(pdg.ST_C0_MODIFICA_AC))
		return livelloResponsabilitaUtente == LV_AC;

	// In stato C1 solo il cdr di livello 1 può modificare il pdg
	if (stato.equals(pdg.ST_C1_MODIFC_CDRI))
		return livelloResponsabilitaUtente == LV_CDRI;
	
	// In stato C2 solo il cdr di livello 2 può modificare il pdg
	if (stato.equals(pdg.ST_C2_MODIFIC_RUO))
		return livelloResponsabilitaUtente == LV_RUO;

	// In stato D,E,G,H solo i cdr padri e il cdr responsabile del pdg possono modificare il pdg
	if (stato.equals(pdg.ST_D_CHIUSURA_I) || 
		stato.equals(pdg.ST_E_CHIUSO) ||
		stato.equals(pdg.ST_G_APERTURA_PER_VARIAZIONI) ||
		stato.equals(pdg.ST_H_PRECHIUSURA_PER_VARIAZIONI))
		return confrontaLivelloResponsabilita(userContext, cdrUtente, pdg.getCentro_responsabilita()) >= 0;

	return false;
}
/** 
 *  PDG in stato A,B, utente non abilitato
 *    PreCondition:
 *      Lo stato del pdg è A o B e  confrontaLivelloResponsabilita(cdrFromUserContext(),pdg.cdr) != 0
 *    PostCondition:
 *      Restituisce true
 *  PDG in stato Ci, utente non abilitato
 *    PreCondition:
 *      Lo stato del pdg è Ci e getLivelloResponsabilitaCDR(cdrFromUserContext()) != i
 *    PostCondition:
 *      Restituisce true
 *  PDG in stato D,E,G,H utente non abilitato
 *    PreCondition:
 *      Lo stato del pdg è D,E,G o H e  confrontaLivelloResponsabilita(cdrFromUserContext(),pdg.cdr) < 0
 *    PostCondition:
 *      Restituisce true
 *  Tutti i controlli superati
 *    PreCondition:
 *      Nessun'altra precondizione è verificata
 *    PostCondition:
 *      Restituisce false
 */
protected boolean isPdgModificabile(UserContext userContext,Pdg_preventivoBulk pdg) throws ComponentException {
	return isPdgModificabile(userContext,cdrFromUserContext(userContext),pdg);
}
}

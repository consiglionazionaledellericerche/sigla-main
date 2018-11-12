package it.cnr.contab.pdg00.comp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaHome;
import it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_cdp_laBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_cdp_uoBulk;
import it.cnr.contab.pdg00.cdip.bulk.Costi_dipendenteVBulk;
import it.cnr.contab.pdg00.cdip.bulk.Costo_del_dipendenteBulk;
import it.cnr.contab.pdg00.cdip.bulk.Stampa_imponibili_dipendentiVBulk;
import it.cnr.contab.pdg00.cdip.bulk.Stampa_ripartizione_costiVBulk;
import it.cnr.contab.pdg00.cdip.bulk.Stipendi_coanBulk;
import it.cnr.contab.pdg00.cdip.bulk.Stipendi_cofiBulk;
import it.cnr.contab.pdg00.cdip.bulk.Stipendi_cofi_obb_scadBulk;
import it.cnr.contab.pdg00.cdip.bulk.V_cdp_matricolaBulk;
import it.cnr.contab.pdg00.cdip.bulk.V_cdp_stato_mensilitaBulk;
import it.cnr.contab.pdg00.cdip.bulk.V_dipendenteBulk;
import it.cnr.contab.pdg00.cdip.bulk.V_dipendenteHome;
import it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.prevent01.ejb.PdgAggregatoModuloComponentSession;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.bulk.ROWrapper;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.IPrintMgr;
import it.cnr.jada.comp.RicercaComponent;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class CostiDipendenteComponent extends RicercaComponent implements ICostiDipendenteMgr, IPrintMgr {
/**
 * CostiPersonaleComponent constructor comment.
 */
public CostiDipendenteComponent() {
	super();
}
/**
 *  Default
 *    PreCondition:
 *      L'utente richiede l'annullamento della scrittura analitica per i costi stipendiali mensili
 *    PostCondition:
 *      Viene invocata la procedura CNRCTB207.annullaScaricaCDPSuPdgGestione per
 *		l'unità organizzativa e il mese selezionato dall'utente
 */
public void annullaScritturaAnalitica(UserContext userContext,int mese) throws ComponentException {
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(userContext))
		throw new ApplicationException("Funzione non disponibile ad esercizio chiuso.");

	try {
		LoggableStatement stm = new LoggableStatement(getConnection(userContext),
				"{  call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+ "CNRCTB207.annullaScaricaCDPSuPdgGestione(?, ?, ?, ?)}",false,this.getClass());
		try {
			stm.setInt(1, CNRUserContext.getEsercizio(userContext).intValue());
			stm.setInt(2,mese);		
			stm.setString(3,CNRUserContext.getCd_unita_organizzativa(userContext));
			stm.setString(4,CNRUserContext.getUser(userContext));
			stm.execute();
		} finally {
		}
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	}
}
/** 
  *  Default
  *    PreCondition:
  *      Viene richiesto la visualizzazione dello stato di scarico dei costi dei dipendenti del CDS a cui l'utente appartiene.
  *		 Non è specificata alcuna matricola
  *    PostCondition:
  *      Restituisce un Costi_dipendenteVBulk che contiene un'elenco di V_cdp_matricolaBulk costituito da:
  *      - l'elenco di tutti i dipendenti relativi all'unità organizzativa a cui l'utente appartiene (provenienza interna).
  *      - l'elenco dei dipendenti di altre unità organizzative che hanno richiesto di scaricare parte dei costi sull'unità organizzativa dell'utente (provenienza esterna).
  *
  *    PreCondition:
  *      Viene richiesto la visualizzazione dello stato di scarico dei costi dei dipendenti del CDS a cui l'utente appartiene per un mese > 0
  *		 E' stata specificata una matricola di provenienza interna.
  *		 Il mese specificato è 0 o esiste almeno una riga di dettaglio ass_cdp_la o ass_cdp_uo.
  *    PostCondition:
  *      Per la matricola selezionata vengono caricati:
  *      - l'elenco degli Ass_cdp_la (percentuali di scarico per CDR e linea di attività) relativi all'unità organizzativa a cui l'utente appartiene;
  *      - l'elenco degli Ass_cdp_uo (percentuali di scarico su altre UO diverse da quella del dipendente);
  *      Per ognuno degli ass_cdp_la/ass_cdp_uo caricati viene calcolato il corrispondente in giorni lavorativi annui alla percentuale scaricata; se un ass_cdp_la proviene da una UO diversa da quella dell'utente (costo caricato da altra UO) il totale dei giorni lavorativi viene calcolato in proporzione alla percentuale caricata dall'UO a cui appartiene la matricola
  *
  *    PreCondition:
  *      Viene richiesto la visualizzazione dello stato di scarico dei costi dei dipendenti del CDS a cui l'utente appartiene per un mese > 0
  *		 E' stata specificata una matricola di provenienza esterna.
  *		 Il mese specificato è 0 o esiste almeno una riga di dettaglio ass_cdp_la o ass_cdp_uo.
  *    PostCondition:
  *      Per la matricola selezionata vengono caricati:
  *      - l'elenco degli Ass_cdp_la (percentuali di scarico per CDR e linea di attività) relativi all'unità organizzativa a cui l'utente appartiene;
  *      Per ognuno degli ass_cdp_la caricati viene calcolato il corrispondente in giorni lavorativi annui alla percentuale scaricata; se un ass_cdp_la proviene da una UO diversa da quella dell'utente (costo caricato da altra UO) il totale dei giorni lavorativi viene calcolato in proporzione alla percentuale caricata dall'UO a cui appartiene la matricola,
  *		  pesata sulla percentuale dell'ass_cdp_uo da cui proviene.
  *
  *    PreCondition:
  *      Viene richiesto la visualizzazione dello stato di scarico dei costi dei dipendenti del CDS a cui l'utente appartiene per un mese > 0
  *		 E' stata specificata una matricola di provenienza interna.
  *		 Il mese specificato è > 0 e non esiste nessuna riga di dettaglio ass_cdp_la o ass_cdp_uo.
  *    PostCondition:
  *		 Viene generato un'elenco di dettagli ass_cdp_la copiandoli dalla ripartizione del mese precedente.
  *		 Viene generato un'elenco di dettagli ass_cdp_uo copiandoli dalla ripartizione del mese precedente filtrando solo quelli
  *		  in stato 'accettato' o 'iniziale' e relativi ad una UO per cui non è stata ancora generata la
  *		  scrittura analitica per il mese selezionato.
  *		 Viene impostato il flag RipartizioneGenerata a true.
 */
public OggettoBulk caricaCosto_dipendente(UserContext userContext,Costi_dipendenteVBulk costi_dipendente,V_cdp_matricolaBulk cdp) throws it.cnr.jada.comp.ComponentException {
	try {
		Integer esercizio = costi_dipendente.getEsercizio();
		String cd_unita_organizzativa = costi_dipendente.getUnita_organizzativa_filter().getCd_unita_organizzativa();
		int mese = costi_dipendente.getMese();

		if (cdp!=null && cdp.getId_matricola()!= null &&  
			(cdp.getCostiScaricati()==null || cdp.getCostiScaricati().isEmpty()) && 
			(cdp.getCostiScaricati()==null || cdp.getCostiScaricatiAltraUO().isEmpty())) {
			cdp.setGiorni_lavorativi_a1(costi_dipendente.getGiorni_lavorativi());
			cdp.setGiorni_lavorativi_a2(costi_dipendente.getGiorni_lavorativi());
			cdp.setGiorni_lavorativi_a3(costi_dipendente.getGiorni_lavorativi());			

			// Estraggo l'elenco degli Ass_cdp_laBulk per la matricola selezionata
			BulkHome home = getHome(userContext,Ass_cdp_laBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			// Estraggo solo le righe appartenenti alla mia UO
			sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,esercizio);
			sql.addClause(FindClause.AND,"id_matricola",SQLBuilder.EQUALS,cdp.getId_matricola());
			sql.addClause(FindClause.AND,"mese",SQLBuilder.EQUALS,mese);
			sql.addTableToHeader("CDR");
			sql.addSQLJoin("CDR.CD_CENTRO_RESPONSABILITA","ASS_CDP_LA.CD_CENTRO_RESPONSABILITA");
			sql.addSQLClause(FindClause.AND,"CDR.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,cd_unita_organizzativa);
				
			cdp.setCostiScaricati(new BulkList(home.fetchAll(sql)));

			// Estraggo l'elenco degli Ass_cdp_uoBulk per la matricola selezionata
			home = getHome(userContext,Ass_cdp_uoBulk.class);			
			sql = home.createSQLBuilder();
			sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,esercizio);
			sql.addClause(FindClause.AND,"id_matricola",SQLBuilder.EQUALS,cdp.getId_matricola());
			sql.addClause(FindClause.AND,"mese",SQLBuilder.EQUALS,new Integer(mese));
			if (!cdp.isProvenienzaInterna())
				sql.addClause(FindClause.AND,"cd_unita_organizzativa",SQLBuilder.EQUALS,cd_unita_organizzativa);				
			
			java.util.List ass_cdp_uo_list = home.fetchAll(sql);

			if (cdp.isProvenienzaInterna()) {
				// Se la provenienza è interna (i.e. matricola appartenente al CDS
				// dell'utente) imposto l'elenco dei costi scaricati su altra UO
				cdp.setCostiScaricatiAltraUO(new BulkList(ass_cdp_uo_list));
			} else {
				// Altrimenti imposto la provenienza del costo caricato
				Ass_cdp_uoBulk ass_cdp_uo = (Ass_cdp_uoBulk)ass_cdp_uo_list.get(0);
				cdp.setCostiScaricatiAltraUO(new BulkList());
				cdp.setCostoCaricato(ass_cdp_uo);
				// e ricalcolo la base del totale dei gg lavorativi da usare per
				// le conversioni da/per prc a giorni
				cdp.calcolaGiorni_lavorativi(1,ass_cdp_uo.getPrc_uo_a1());
				cdp.calcolaGiorni_lavorativi(2,ass_cdp_uo.getPrc_uo_a2());
				cdp.calcolaGiorni_lavorativi(3,ass_cdp_uo.getPrc_uo_a3());
			}

			// Calcolo i giorni lavorativi per i costi scaricati verso altra UO corrispondenti
			// alle percentuali
			// Se la provenienza NON è interna la lista è vuota
			if (cdp.getCostiScaricatiAltraUO() != null)
				for (java.util.Iterator<Ass_cdp_uoBulk> i = cdp.getCostiScaricatiAltraUO().iterator();i.hasNext();)
					(i.next()).calcolaGiorni_uo(costi_dipendente.getGiorni_lavorativi());
	
			boolean modificabile = isCosto_del_dipendenteValidoPerModifica(userContext,cdp);
	
			for (java.util.Iterator<Ass_cdp_laBulk> i = cdp.getCostiScaricati().iterator();i.hasNext();) {
				// Calcolo i giorni lavorativi corrispondenti alle percentuali
				Ass_cdp_laBulk ass_cdp_la = i.next();
				ass_cdp_la.calcolaGiorni_la(1,cdp.getGiorni_lavorativi_a1());
				ass_cdp_la.calcolaGiorni_la(2,cdp.getGiorni_lavorativi_a2());
				ass_cdp_la.calcolaGiorni_la(3,cdp.getGiorni_lavorativi_a3());
				if (mese == 0) {
					// Controllo che il pdgP del cdr sia aperto
					CdrBulk cdrPdgP = getCdrPdgP(userContext);
					Pdg_esercizioBulk pdgp = (Pdg_esercizioBulk)getHome(userContext,Pdg_esercizioBulk.class).findByPrimaryKey(new Pdg_esercizioBulk(esercizio, cdrPdgP.getCd_centro_responsabilita()));
					ass_cdp_la.setReadonly(!modificabile || !isStatoPdgPValidoPerModificaCDP(pdgp));
				} else {
					ass_cdp_la.setReadonly(!modificabile);
				}
			}
			// 05/09/2003
			// Aggiunto controllo sulla chiusura dell'esercizio
			if (isEsercizioChiuso(userContext))
				return asRO(costi_dipendente,"Ripartizione non modificabile ad esercizio chiuso.");
			else if (!modificabile)
				return asRO(costi_dipendente,"Ripartizione non modificabile per questa matricola");
		}

		return cdp;
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	} catch(javax.ejb.EJBException e) {
		throw handleException(e);
	}
}
/**
 *  Default
 *    PreCondition:
 *      L'utente richiede la generazione della contabilizzazione dei flussi stipendiali mensili
 *    PostCondition:
 *      Viene invocata la procedura CNRCTB680.contabilFlussoStipCOFI per
 *		il mese selezionato dall'utente
 */
public void contabilizzaFlussoStipendialeMensile(UserContext userContext,int mese) throws ComponentException {
	try {
		
		LoggableStatement stm = new LoggableStatement(getConnection(userContext),
				"{  call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+ "CNRCTB680.contabilFlussoStipCOFI(?, ?, ?)}",false,this.getClass());
		try {
			stm.setInt(1, CNRUserContext.getEsercizio(userContext).intValue());
			stm.setInt(2,mese);		
			stm.setString(3,CNRUserContext.getUser(userContext));
			stm.execute();
		} finally {
		}
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	}
}
/*
 * Pre-post-conditions:
 *
 * Nome: Dipendente non modificabile
 * Pre: Viene la copia della ripartizione dei costi da una matricola in un'altra matricola.
 *		Per la matricola specificata come destinazione esiste già una ripartizione dei costi.
 * Post: Viene generata una ApplicationException con il messaggio: "La matricola xyz possiede già una ripartizione"
 * Nome: Tutti i controlli superati
 * Pre:	Viene la copia della ripartizione dei costi da una matricola in un'altra matricola.
 *		Nessuna delle altre pre-condizioni è verificata.
 * Post: Viene copiata la configurazione della ripartizione dei costi del dipendente sulla
 *		matricola di destinazione (nella tabella ASS_CDP_LA)
 */
public OggettoBulk copiaRipartizione(it.cnr.jada.UserContext userContext,Costi_dipendenteVBulk cdp, V_cdp_matricolaBulk matricola_src,V_cdp_matricolaBulk matricola_dest) throws it.cnr.jada.comp.ComponentException {
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(userContext))
		throw new ApplicationException("Funzione non disponibile ad esercizio chiuso.");

	try {
		// Lock della matricola
		lockMatricola(userContext,matricola_dest.getId_matricola(),cdp.getMese());

		for (java.util.Iterator<V_cdp_matricolaBulk> i = cdp.getCosti_dipendenti().iterator();i.hasNext();) {
			V_cdp_matricolaBulk matricola = i.next();
			if (matricola.getEsercizio().equals(matricola_dest.getEsercizio()) &&
				matricola.getMese().equals(matricola_dest.getMese()) &&
				matricola.getId_matricola().equals(matricola_dest.getId_matricola()) &&
				matricola.getTi_prev_cons().equals(matricola_dest.getTi_prev_cons()) &&
				matricola.getTi_appartenenza().equals(matricola_dest.getTi_appartenenza()) &&
				matricola.getTi_gestione().equals(matricola_dest.getTi_gestione())) {
				matricola_dest = matricola;
				break;
			}
		}

		if (matricola_src==null || matricola_dest==null) 
			return cdp;

		if (!matricola_dest.getCostiScaricati().isEmpty())
			throw new ApplicationException("La matricola "+matricola_dest.getId_matricola()+" possiede già una ripartizione");
		if (!matricola_dest.getCostiScaricatiAltraUO().isEmpty())
			throw new ApplicationException("La matricola "+matricola_dest.getId_matricola()+" possiede già una ripartizione");

		for (java.util.Iterator<Ass_cdp_laBulk> i = matricola_src.getCostiScaricati().iterator();i.hasNext();) {
			Ass_cdp_laBulk ass_cdp_la = i.next();
			ass_cdp_la.setId_matricola(matricola_dest.getId_matricola());
			ass_cdp_la.setUser(userContext.getUser());
			ass_cdp_la.setFl_dip_altra_uo(new Boolean(matricola_dest.isProvenienzaCaricato()));
			matricola_dest.addToCostiScaricati(ass_cdp_la);
		}
		return cdp;
	} catch(Throwable e) {
		throw handleException(e);
	}
}
public V_cdp_matricolaBulk generaDaUltimaRipartizione(UserContext userContext,V_cdp_matricolaBulk cdp) throws it.cnr.jada.comp.ComponentException {
	try {
		String id_matricola = cdp.getId_matricola();
		Integer mese = cdp.getMese();
		Integer esercizio = cdp.getEsercizio();

		LoggableStatement stm = new LoggableStatement(getConnection(userContext), 
				                "SELECT MAX(MESE) FROM "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+"COSTO_DEL_DIPENDENTE " +
								"WHERE ID_MATRICOLA = ? " +
								"AND ESERCIZIO = ? " +
								"AND CD_UNITA_ORGANIZZATIVA = ? " +
								"AND MESE < ? " +
								"AND ( EXISTS ( SELECT 1 FROM "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+"ASS_CDP_UO " +
												"WHERE ASS_CDP_UO.ESERCIZIO = COSTO_DEL_DIPENDENTE.ESERCIZIO " +
												"AND ASS_CDP_UO.ID_MATRICOLA = COSTO_DEL_DIPENDENTE.ID_MATRICOLA " +
												"AND ASS_CDP_UO.MESE = COSTO_DEL_DIPENDENTE.MESE " +
												"AND ASS_CDP_UO.STATO <> 'N') " +
												"OR EXISTS ( SELECT 1 FROM "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+"ASS_CDP_LA " +
															"WHERE ASS_CDP_LA.ESERCIZIO = COSTO_DEL_DIPENDENTE.ESERCIZIO " +
															"AND ASS_CDP_LA.ID_MATRICOLA = COSTO_DEL_DIPENDENTE.ID_MATRICOLA " +
															"AND ASS_CDP_LA.MESE = COSTO_DEL_DIPENDENTE.MESE ) )",true,this.getClass());
		try {
			stm.setString(1,id_matricola);
			stm.setInt(2,cdp.getEsercizio().intValue());
			stm.setString(3,cdp.getCd_unita_organizzativa());
			stm.setInt(4,mese);
			java.sql.ResultSet rs = stm.executeQuery();
			try {
				if (!rs.next()) return cdp;
				int ultimo_mese = rs.getInt(1);
				if (ultimo_mese != mese.intValue()-1)
					return cdp;

				String cd_unita_organizzativa = ((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext)))).getCd_unita_organizzativa(); 
				
				SQLBuilder sql = getHome(userContext,Ass_cdp_laBulk.class).createSQLBuilder();
				// Estraggo solo le righe appartenenti alla mia UO
				sql.addTableToHeader("CDR");
				sql.addSQLJoin("CDR.CD_CENTRO_RESPONSABILITA","ASS_CDP_LA.CD_CENTRO_RESPONSABILITA");
				sql.addSQLClause("AND","CDR.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,cd_unita_organizzativa);
				sql.addClause("AND","esercizio",SQLBuilder.EQUALS,esercizio);
				sql.addClause("AND","id_matricola",SQLBuilder.EQUALS,id_matricola);
				sql.addClause("AND","mese",SQLBuilder.EQUALS,new Integer(ultimo_mese));
				cdp.setCostiScaricati(new BulkList(getHome(userContext,Ass_cdp_laBulk.class).fetchAll(sql)));

				sql = getHome(userContext,Ass_cdp_uoBulk.class).createSQLBuilder();
				sql.addClause("AND","esercizio",SQLBuilder.EQUALS,esercizio);
				sql.addClause("AND","id_matricola",SQLBuilder.EQUALS,id_matricola);
				sql.addClause("AND","mese",SQLBuilder.EQUALS,new Integer(ultimo_mese));
				sql.addClause("AND","stato",SQLBuilder.NOT_EQUALS,Ass_cdp_uoBulk.STATO_NON_ACCETTATO);
				cdp.setCostiScaricatiAltraUO(new BulkList(getHome(userContext,Ass_cdp_uoBulk.class).fetchAll(sql)));

				for (java.util.Iterator i = cdp.getCostiScaricati().iterator();i.hasNext();) {
					Ass_cdp_laBulk ass_cdp_la = (Ass_cdp_laBulk)i.next();
					ass_cdp_la.setMese(mese);
					// (1/21/2004 11:04:29 AM) CNRADM
					// Aggiunto modifica stato ass_cdp_la a non scaricato:
					// le ripartizione copiate non sono state ancora scaricate.
					ass_cdp_la.setStato(Ass_cdp_laBulk.STATO_NON_SCARICATO);
					ass_cdp_la.setCrudStatus(OggettoBulk.TO_BE_CREATED);
				}

				for (java.util.Iterator i = cdp.getCostiScaricatiAltraUO().iterator();i.hasNext();) {
					Ass_cdp_uoBulk ass_cdp_uo = (Ass_cdp_uoBulk)i.next();
					if (isStatoAnaliticaValidoPerModificaCDP(
						userContext,
						ass_cdp_uo.getCd_unita_organizzativa(),
						ass_cdp_uo.getEsercizio(),
						ass_cdp_uo.getMese().intValue())) {
						ass_cdp_uo.setMese(mese);
						ass_cdp_uo.setStato(Ass_cdp_uoBulk.STATO_INIZIALE);
						ass_cdp_uo.setCrudStatus(OggettoBulk.TO_BE_CREATED);
					} else {
						i.remove();
					}
				}

			} finally {
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		} finally {
			try{stm.close();}catch( java.sql.SQLException e ){};
		}
		return cdp;
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	}
}
/**
 *  Default
 *    PreCondition:
 *      L'utente richiede la generazione della scrittura analitica per i costi stipendiali mensili
 *    PostCondition:
 *      Viene invocata la procedura CNRCTB207.scaricaCDPSuPdgGestione per
 *		l'unità organizzativa e il mese selezionato dall'utente
 */
public void generaScritturaAnalitica(UserContext userContext,int mese) throws ComponentException {
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(userContext))
		throw new ApplicationException("Funzione non disponibile ad esercizio chiuso.");

	try {
		
		LoggableStatement stm = new LoggableStatement(getConnection(userContext),
				"{  call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+ "CNRCTB207.scaricaCDPSuPdgGestione(?, ?, ?, ?)}",false,this.getClass());
		try {
			stm.setInt(1, CNRUserContext.getEsercizio(userContext).intValue());
			stm.setInt(2,mese);		
			stm.setString(3,CNRUserContext.getCd_unita_organizzativa(userContext));
			stm.setString(4,CNRUserContext.getUser(userContext));
			stm.execute();
		} finally {
		}
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	}
}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_imponibili_dipendentiVBulk stampa) throws it.cnr.jada.comp.ComponentException {

	stampa.setCd_cds(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
	stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));

	try{
	
		String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		Unita_organizzativaHome uoHome = (it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome)getHome(userContext, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class);
		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = (it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk)uoHome.findByPrimaryKey(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk(cd_uo_scrivania));
		
		if (!uo.isUoCds()){
			stampa.setUoForPrint(uo);
			stampa.setIsUOForPrintEnabled(false);
		} else {
			stampa.setUoForPrint(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk());
			stampa.setIsUOForPrintEnabled(true);
		}
			
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}
}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_ripartizione_costiVBulk stampa) throws it.cnr.jada.comp.ComponentException {

	stampa.setCd_cds(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
	stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));
	
	stampa.setCommessaForPrint(new ProgettoBulk());
	stampa.setModuloForPrint(new ProgettoBulk());
	stampa.setDipendenteForPrint(new V_dipendenteBulk());
	try{	    
		String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		Unita_organizzativaHome uoHome = (it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome)getHome(userContext, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class);
		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = (it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk)uoHome.findByPrimaryKey(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk(cd_uo_scrivania));
		
		if (!uo.isUoCds()){
			stampa.setUoForPrint(uo);
			stampa.setIsUOForPrintEnabled(false);
		} else {
			stampa.setUoForPrint(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk());
			stampa.setIsUOForPrintEnabled(true);
		}
			
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}	
}
/**
 * inizializzaBulkPerStampa method comment.
 */
public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	
	if (bulk instanceof Stampa_imponibili_dipendentiVBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_imponibili_dipendentiVBulk)bulk);
	else
	if(bulk instanceof Stampa_ripartizione_costiVBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_ripartizione_costiVBulk)bulk);
		
	return bulk;
}
private boolean isCosto_del_dipendenteValidoPerModifica(UserContext userContext,V_cdp_matricolaBulk costo_del_dipendente) throws ComponentException {
	return
		costo_del_dipendente.getMese().intValue() == 0 ||
		isStatoAnaliticaValidoPerModificaCDP(
			userContext,
			costo_del_dipendente.getCd_unita_organizzativa(),
			costo_del_dipendente.getEsercizio(),
			costo_del_dipendente.getMese().intValue());
}
public boolean isEsercizioChiuso(UserContext userContext) throws ComponentException {
	try {
		EsercizioHome home = (EsercizioHome)getHome(userContext,EsercizioBulk.class);
		return home.isEsercizioChiuso(userContext);
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
protected boolean isEsercizioChiuso(UserContext userContext,Unita_organizzativaBulk uo) throws ComponentException {
	try {
		EsercizioHome home = (EsercizioHome)getHome(userContext,EsercizioBulk.class);
		return home.isEsercizioChiuso(userContext,CNRUserContext.getEsercizio(userContext),uo.getCd_unita_padre());
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
private boolean isStatoAnaliticaValidoPerModificaCDP(UserContext userContext,String cd_unita_organizzativa,Integer esercizio,int mese) throws ComponentException {
	try {
		LoggableStatement stm = new LoggableStatement(getConnection(userContext),
				"SELECT 1 FROM "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+"STIPENDI_COAN WHERE ESERCIZIO = ? AND CD_UO = ? AND MESE = ? AND PG_SCRITTURA_AN IS NOT NULL",true,this.getClass());
		try {
			stm.setInt(1,CNRUserContext.getEsercizio(userContext).intValue());
			stm.setString(2,cd_unita_organizzativa);
			stm.setInt(3,mese);
			java.sql.ResultSet rs = stm.executeQuery();
			try {
				return !rs.next();
			} finally {
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		} finally {
			try{stm.close();}catch( java.sql.SQLException e ){};
		}
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	} 
}
private boolean isStatoPdgValidoPerModificaCDP(Pdg_preventivoBulk pdg) {
	return 
		pdg.getStato().equalsIgnoreCase(pdg.ST_A_CREAZIONE) || 
		pdg.getStato().equalsIgnoreCase(pdg.ST_B_MODIFICA) ||
		pdg.getStato().equalsIgnoreCase(pdg.ST_D_CHIUSURA_I) || 
		pdg.getStato().equalsIgnoreCase(pdg.ST_E_CHIUSO); 
}
private boolean isStatoPdgPValidoPerModificaCDP(Pdg_esercizioBulk pdgP) {
	if (pdgP==null)
		return false;
	else
	return 
		(pdgP.getStato().equalsIgnoreCase(pdgP.STATO_APERTURA_CDR) || 
		 pdgP.getStato().equalsIgnoreCase(pdgP.STATO_ESAMINATO_CDR)); 
}
/**
 *  Default
 *    PreCondition:
 *      Viene richiesto l'elenco delle mensilità relative alla ripartizione
 *		 dei costi stipendiali mensili.
 *    PostCondition:
 *      Viene effettuata una query sulla vista V_CDP_STATO_MENSILITA 
 *		 per l'UO a cui appartiene l'utente e con la condizione 'stato_scarico = 'P'
 *		 (mensilità già contabilizzate)
 */
public it.cnr.jada.util.RemoteIterator listaCdp_analitica(UserContext userContext) throws ComponentException {
	SQLBuilder sql = getHome(userContext,V_cdp_stato_mensilitaBulk.class).createSQLBuilder();
	sql.addClause("AND","esercizio",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	sql.addClause("AND","cd_unita_organizzativa",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
	sql.addClause("AND","stato_carico",sql.EQUALS,"P");
	return iterator(userContext,sql,V_cdp_stato_mensilitaBulk.class,null);
}
/** 
  *  Costi del dipendente
  *    PreCondition:
  *      Viene richiesto l'elenco dei CDR utilizzabili per effettuare uno scarico dei costi di un dipendente su CDR
  *		 Il mese è 0
  *    PostCondition:
  *      Viene restituita una query sulla tabella dei cdr con le clausole specificate più
  *      - il pdg associato al cdr deve essere in stato A,B,D o E    
  *      - il cdr deve appartenere alla unità organizzativa dell'utente per l'esercizio di scrivania
  *
  *  Costi stipendiali mensili
  *    PreCondition:
  *      Viene richiesto l'elenco dei CDR utilizzabili per effettuare uno scarico dei costi di un dipendente su CDR
  *		 Il mese è > 0
  *    PostCondition:
  *      Viene restituita una query sulla tabella dei cdr con le clausole specificate più
  *      - il cdr deve appartenere alla unità organizzativa dell'utente per l'esercizio di scrivania
 */
public it.cnr.jada.util.RemoteIterator listaCdr(UserContext userContext,String cd_unita_organizzativa,int mese) throws ComponentException {
	SQLBuilder sql = getHome(userContext,CdrBulk.class).createSQLBuilder();
	if (mese == 0) {
		SQLBuilder sqlPdgP = getHome(userContext, Pdg_moduloBulk.class).createSQLBuilder();
		
		sqlPdgP.addSQLClause("AND","PDG_MODULO.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		sqlPdgP.addSQLClause("AND","PDG_MODULO.CD_CENTRO_RESPONSABILITA",sql.EQUALS,getCdrPdgP(userContext).getCd_centro_responsabilita());
		sqlPdgP.addSQLClause("AND","PDG_MODULO.STATO", sql.NOT_EQUALS, Pdg_moduloBulk.STATO_AC);
		sqlPdgP.addSQLClause("AND","PDG_MODULO.STATO", sql.NOT_EQUALS, Pdg_moduloBulk.STATO_AD);

		sql.addSQLNotExistsClause("AND", sqlPdgP);
	}
	sql.addSQLClause("AND","CDR.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,cd_unita_organizzativa);
//	sql.addSQLClause("AND","CDR.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,CNRUserContext.getCd_unita_organizzativa(userContext));
	return iterator(userContext,sql,CdrBulk.class,null);
}
/** 
  *  Costi del dipendente
  *    PreCondition:
  *      Viene richiesto l'elenco delle linee di attività utilizzabili per effettuare uno scarico dei costi di un dipendente su CDR
  *		 Il mese è 0
  *    PostCondition:
  *      Viene restituita una query sulla tabella delle linee di attività con le clausole specificate più
  *      - il pdgp associato al cdr della linea di attività deve essere in stato AC, o AP    
  *      - la linea di attività deve appartenere al cdr specificato
  *		 - la linea di attività deve essere di spesa
  *		 - la linea di attività deve essere valida nell'esercizio di scrivania
  *
  *  Costi stipendiali mensili
  *    PreCondition:
  *      Viene richiesto l'elenco delle linee di attività utilizzabili per effettuare uno scarico dei costi di un dipendente su CDR
  *		 Il mese è > 0
  *    PostCondition:
  *      Viene restituita una query sulla tabella delle linee di attività con le clausole specificate più
  *      - la linea di attività deve appartenere al cdr specificato
  *		 - la linea di attività deve essere di spesa
  *		 - la linea di attività deve essere valida nell'esercizio di scrivania
  */
public it.cnr.jada.util.RemoteIterator listaLinea_attivitaPerCdr(UserContext userContext,CdrBulk cdr,int mese, String tipo_rapporto, boolean isRapporto13) throws ComponentException {
	it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
	SQLBuilder sql = getHome(userContext,it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();
try {	
	if (mese == 0) {
		sql.addTableToHeader("PDG_MODULO");
		sql.addSQLJoin("PDG_MODULO.ESERCIZIO","V_LINEA_ATTIVITA_VALIDA.ESERCIZIO");
		sql.addSQLClause(FindClause.AND, "PDG_MODULO.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS, getCdrPdgP(userContext).getCd_centro_responsabilita());
		sql.addSQLJoin("PDG_MODULO.PG_PROGETTO","V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO");
		sql.openParenthesis(FindClause.AND);
		sql.addSQLClause(FindClause.AND,"PDG_MODULO.STATO", SQLBuilder.EQUALS, Pdg_moduloBulk.STATO_AC);
		sql.addSQLClause(FindClause.OR,"PDG_MODULO.STATO", SQLBuilder.EQUALS, Pdg_moduloBulk.STATO_AD);
		sql.closeParenthesis();
	}
	// Obbligatorio cofog sulle GAE
	Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext)); 
	if (parCnr.isCofogObbligatorio())
		sql.addSQLClause("AND","CD_COFOG",SQLBuilder.ISNOTNULL,null);
	if (parCnr.getFl_nuovo_pdg()) {
		sql.addSQLClause(FindClause.AND,"CD_PROGRAMMA",SQLBuilder.ISNOTNULL,null);
		sql.addSQLClause(FindClause.AND,"CD_MISSIONE",SQLBuilder.ISNOTNULL,null);
	}
//Filtro che estrae solo le linee di attività di spesa: 25/02/2002
	sql.openParenthesis(FindClause.AND);
	sql.addSQLClause(FindClause.OR, "V_LINEA_ATTIVITA_VALIDA.TI_GESTIONE", SQLBuilder.EQUALS, WorkpackageBulk.TI_GESTIONE_SPESE);
	sql.addSQLClause(FindClause.OR, "V_LINEA_ATTIVITA_VALIDA.TI_GESTIONE", SQLBuilder.EQUALS, WorkpackageBulk.TI_GESTIONE_ENTRAMBE);
	sql.closeParenthesis();

	sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
	sql.addClause(FindClause.AND,"cd_centro_responsabilita",SQLBuilder.EQUALS,cdr.getCd_centro_responsabilita());

//	sql.addSQLClause("AND","V_LINEA_ATTIVITA_VALIDA.CD_NATURA",sql.NOT_EQUALS,new Integer(5));

	sql.addTableToHeader("NATURA");
	sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_NATURA","NATURA.CD_NATURA");
	sql.addSQLClause(FindClause.AND, "NATURA.FL_SPESA",SQLBuilder.EQUALS,"Y");
	//Nel gestionale devono vedere tutte le GAE va tolto il controllo della natura per il tempo determiniato/indeterminato
	if (mese==0 && tipo_rapporto!=null &&
		(tipo_rapporto.equalsIgnoreCase(Costo_del_dipendenteBulk.TI_RAPPORTO_INDETERMINATO) ||
		 (tipo_rapporto.equalsIgnoreCase(Costo_del_dipendenteBulk.TI_RAPPORTO_DETERMINATO) && isRapporto13)))
		sql.addSQLClause(FindClause.AND, "NATURA.TIPO",SQLBuilder.EQUALS,NaturaBulk.TIPO_NATURA_FONTI_INTERNE);
	
	sql.addTableToHeader("FUNZIONE");
	sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_FUNZIONE","FUNZIONE.CD_FUNZIONE");
	sql.addSQLClause(FindClause.AND, "FUNZIONE.FL_UTILIZZABILE",SQLBuilder.EQUALS,"Y");

	/**
	 * Escludo la linea di attività dell'IVA C20
	 */
			config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LINEA_ATTIVITA_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_LINEA_COMUNE_VERSAMENTO_IVA);
	} catch (RemoteException e) {
		throw new ComponentException(e);
	} catch (EJBException e) {
		throw new ComponentException(e);
	}
	if (config != null){
		sql.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA",  SQLBuilder.NOT_EQUALS, config.getVal01());
	}

	sql.addOrderBy("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA");
	return iterator(userContext,sql,it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class,null);
}
/** 
  *  Costi del dipendente
  *    PreCondition:
  *      Viene richiesto l'elenco delle linee di attività utilizzabili per effettuare la ripartizione dei residui delle percentuali di scarico di un dipendente
  *		 Il mese è 0
  *    PostCondition:
  *      Viene restituita una query sulla tabella delle linee di attività tali che
  *      - il pdgp associato al cdr della linea di attività deve essere in stato AC, o AP    
  *      - la linea di attività deve appartenere ad un cdr dell'unità organizzativa dell'utente per l'esercizio di scrivania
  *
  *  Costi stipendiali mensili
  *    PreCondition:
  *      Viene richiesto l'elenco delle linee di attività utilizzabili per effettuare la ripartizione dei residui delle percentuali di scarico di un dipendente
  *		 Il mese è 0
  *    PostCondition:
  *      Viene restituita una query sulla tabella delle linee di attività tali che
  *      - il pdg associato al cdr della linea di attività deve essere in stato A,B,D o E    
  *      - la linea di attività deve appartenere ad un cdr dell'unità organizzativa dell'utente per l'esercizio di scrivania
  */
public java.util.List listaLinea_attivitaPerRipartizioneResidui(UserContext userContext,String id_matricola,String cd_unita_organizzativa,int mese, String tipo_rapporto, boolean isRapporto13) throws ComponentException {
	try {
		BulkHome home = getHome(userContext,it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class,"V_LINEA_ATTIVITA_VALIDA");
		SQLBuilder sql = home.createSQLBuilder();
		sql.addTableToHeader("CDR");
		sql.addSQLJoin("CDR.CD_CENTRO_RESPONSABILITA","V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA");
		sql.addSQLClause(FindClause.AND,"CDR.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,cd_unita_organizzativa);
		sql.addClause(FindClause.AND,"ti_gestione",SQLBuilder.EQUALS,it.cnr.contab.config00.latt.bulk.WorkpackageBulk.TI_GESTIONE_SPESE);
		sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		
		// Obbligatorio cofog sulle GAE
		Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext)); 
		if (parCnr.isCofogObbligatorio())
			sql.addSQLClause("AND","CD_COFOG",SQLBuilder.ISNOTNULL,null);
		if (parCnr.getFl_nuovo_pdg()) {
			sql.addSQLClause(FindClause.AND,"CD_PROGRAMMA",SQLBuilder.ISNOTNULL,null);
			sql.addSQLClause(FindClause.AND,"CD_MISSIONE",SQLBuilder.ISNOTNULL,null);
		}
// Tolta perchè voglio vedere tutte le linee di attività anche se sono già
// state scaricate
//		sql.addSQLClause("AND","NOT EXISTS ( SELECT 1 FROM ASS_CDP_LA WHERE ASS_CDP_LA.ESERCIZIO = LINEA_ATTIVITA.ESERCIZIO AND ASS_CDP_LA.CD_CENTRO_RESPONSABILITA = LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA AND ASS_CDP_LA.CD_LINEA_ATTIVITA = LINEA_ATTIVITA.CD_LINEA_ATTIVITA )");

		if (mese == 0) {
			sql.addTableToHeader("PDG_MODULO");
			sql.addSQLJoin("PDG_MODULO.ESERCIZIO","V_LINEA_ATTIVITA_VALIDA.ESERCIZIO");
			sql.addSQLClause(FindClause.AND, "PDG_MODULO.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS, getCdrPdgP(userContext).getCd_centro_responsabilita());
			sql.addSQLJoin("PDG_MODULO.PG_PROGETTO","V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO");
			sql.openParenthesis(FindClause.AND);
			sql.addSQLClause(FindClause.AND,"PDG_MODULO.STATO", SQLBuilder.EQUALS, Pdg_moduloBulk.STATO_AC);
			sql.addSQLClause(FindClause.OR,"PDG_MODULO.STATO", SQLBuilder.EQUALS, Pdg_moduloBulk.STATO_AD);
			sql.closeParenthesis();
		}
		
		sql.addTableToHeader("NATURA");
		sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_NATURA","NATURA.CD_NATURA");

		sql.addSQLClause(FindClause.AND, "NATURA.FL_SPESA",SQLBuilder.EQUALS,"Y");
		//Nel gestionale devono vedere tutte le GAE va tolto il controllo della natura per il tempo determiniato/indeterminato
		if (mese==0 && tipo_rapporto!=null &&
				(tipo_rapporto.equalsIgnoreCase(Costo_del_dipendenteBulk.TI_RAPPORTO_INDETERMINATO) ||
				 (tipo_rapporto.equalsIgnoreCase(Costo_del_dipendenteBulk.TI_RAPPORTO_DETERMINATO) && isRapporto13)))
			sql.addSQLClause(FindClause.AND, "NATURA.TIPO",SQLBuilder.EQUALS,NaturaBulk.TIPO_NATURA_FONTI_INTERNE);

		sql.addTableToHeader("FUNZIONE");
		sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_FUNZIONE","FUNZIONE.CD_FUNZIONE");
		sql.addSQLClause(FindClause.AND, "FUNZIONE.FL_UTILIZZABILE",SQLBuilder.EQUALS,"Y");

		/**
		 * Escludo la linea di attività dell'IVA C20
		 */
		it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
		try {
			config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LINEA_ATTIVITA_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_LINEA_COMUNE_VERSAMENTO_IVA);
		} catch (RemoteException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);
		}
		if (config != null){
			sql.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA",  SQLBuilder.NOT_EQUALS, config.getVal01());
		}

		java.util.List list = home.fetchAll(sql);
		getHomeCache(userContext).fetchAll(userContext,home);
		return list;
	} catch(Throwable e) {
		throw handleException(e);
	}
}
public it.cnr.jada.util.RemoteIterator listaStipendi_cofi(UserContext userContext) throws ComponentException {
	SQLBuilder sql = getHome(userContext,Stipendi_cofiBulk.class).createSQLBuilder();
	sql.addClause("AND","esercizio",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	return iterator(userContext,sql,Stipendi_cofiBulk.class,null);
}
/** 
  *  Costi del dipendente
  *    PreCondition:
  *      Viene richiesto l'elenco delle Unità organizzative utilizzabili per effettuare uno scarico dei costi di un dipendente su altra UO
  *		 Il mese è 0
  *    PostCondition:
  *      Viene restituita una query sulla tabella delle UO con le clausole specificate più
  *      - il pdgP associato al cds responsabile dell'UO deve essere in stato AC, o EC    
  *      - l'uo deve essere diversa da quella a cui appartiene la matricola
  *      - l'uo cui imputare i costi di un dipendente non deve aver già effettuato lo scarico dei costi 
  *
  *  Costi stipendiali mensili
  *    PreCondition:
  *      Viene richiesto l'elenco delle Unità organizzative utilizzabili per effettuare uno scarico dei costi di un dipendente su altra UO
  *		 Il mese è > 0
  *    PostCondition:
  *      Viene restituita una query sulla tabella delle UO con le clausole specificate più
  *      - l'uo deve essere diversa da quella a cui appartiene la matricola
  *		 - non deve essere ancora stata generata una ripartizione analitica per l'uo nel mese selezionato
  */
public it.cnr.jada.util.RemoteIterator listaUnita_organizzativa(UserContext userContext,String cd_unita_organizzativa,int mese) throws ComponentException {
	//nel caso della ripartizione mensile occorre far vedere ance le UO chiuse
	BulkHome home = getHome(userContext,Unita_organizzativaBulk.class,"V_UNITA_ORGANIZZATIVA_VALIDA");
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause(FindClause.AND,"cd_unita_organizzativa",SQLBuilder.NOT_EQUALS,cd_unita_organizzativa);
	sql.addSQLClause(FindClause.AND,"V_UNITA_ORGANIZZATIVA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));

	if (mese == 0) {
		sql.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "A");
		sql.addSQLJoin("V_UNITA_ORGANIZZATIVA_VALIDA.ESERCIZIO","A.ESERCIZIO");
		sql.addSQLJoin("V_UNITA_ORGANIZZATIVA_VALIDA.CD_UNITA_ORGANIZZATIVA","A.CD_ROOT");

		sql.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "B");
		sql.addSQLJoin("A.ESERCIZIO","B.ESERCIZIO");
		sql.addSQLJoin("A.CD_CDS","B.CD_CDS");
		sql.addSQLClause(FindClause.AND,"B.CD_TIPO_LIVELLO", SQLBuilder.EQUALS, V_struttura_organizzativaHome.LIVELLO_CDR);
		sql.addSQLClause(FindClause.AND,"B.FL_CDR_UO", SQLBuilder.EQUALS, "Y");
		sql.addSQLClause(FindClause.AND,"B.FL_UO_CDS", SQLBuilder.EQUALS, "Y");

		sql.addTableToHeader("PDG_ESERCIZIO");
		sql.addSQLJoin("PDG_ESERCIZIO.ESERCIZIO","V_UNITA_ORGANIZZATIVA_VALIDA.ESERCIZIO");
		sql.addSQLJoin("PDG_ESERCIZIO.ESERCIZIO","B.ESERCIZIO");
		sql.addSQLJoin("PDG_ESERCIZIO.CD_CENTRO_RESPONSABILITA","B.CD_ROOT");

		sql.openParenthesis(FindClause.AND);
		sql.addSQLClause(FindClause.AND,"PDG_ESERCIZIO.STATO", SQLBuilder.EQUALS, Pdg_esercizioBulk.STATO_APERTURA_CDR);
		sql.addSQLClause(FindClause.OR,"PDG_ESERCIZIO.STATO", SQLBuilder.EQUALS, Pdg_esercizioBulk.STATO_ESAMINATO_CDR);
		sql.closeParenthesis();
		
		BulkHome homeAss = getHome(userContext,Ass_cdp_laBulk.class);
		SQLBuilder sqlAss = homeAss.createSQLBuilder();
		sqlAss.resetColumns();
		sqlAss.addColumn("1");

		sqlAss.addSQLJoin("ASS_CDP_LA.ESERCIZIO","V_UNITA_ORGANIZZATIVA_VALIDA.ESERCIZIO");

		sqlAss.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "C");
		sqlAss.addSQLJoin("ASS_CDP_LA.ESERCIZIO","C.ESERCIZIO");
		sqlAss.addSQLJoin("ASS_CDP_LA.CD_CENTRO_RESPONSABILITA","C.CD_ROOT");
		sqlAss.addSQLJoin("C.CD_CDS","V_UNITA_ORGANIZZATIVA_VALIDA.CD_UNITA_PADRE");

		sqlAss.openParenthesis(FindClause.AND);
		sqlAss.addSQLClause(FindClause.AND,"ASS_CDP_LA.STATO", SQLBuilder.EQUALS, Ass_cdp_laBulk.STATO_SCARICATO);
		sqlAss.addSQLClause(FindClause.OR,"ASS_CDP_LA.STATO", SQLBuilder.EQUALS, Ass_cdp_laBulk.STATO_SCARICATO_PDGP);
		sqlAss.closeParenthesis();
		
		sql.addSQLNotExistsClause(FindClause.AND, sqlAss);
	} else {
		BulkHome homeAss = getHome(userContext,Ass_cdp_laBulk.class);
		SQLBuilder sqlAss = homeAss.createSQLBuilder();
		sqlAss.resetColumns();
		sqlAss.addColumn("1");

		sqlAss.addSQLJoin("ASS_CDP_LA.ESERCIZIO","V_UNITA_ORGANIZZATIVA_VALIDA.ESERCIZIO");

		sqlAss.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "C");
		sqlAss.addSQLJoin("ASS_CDP_LA.ESERCIZIO","C.ESERCIZIO");
		sqlAss.addSQLJoin("ASS_CDP_LA.CD_CENTRO_RESPONSABILITA","C.CD_ROOT");
		sqlAss.addSQLJoin("C.CD_CDS","V_UNITA_ORGANIZZATIVA_VALIDA.CD_UNITA_PADRE");

		sqlAss.openParenthesis(FindClause.AND);
		sqlAss.addSQLClause(FindClause.AND,"ASS_CDP_LA.STATO", SQLBuilder.EQUALS, Ass_cdp_laBulk.STATO_SCARICATO_DEFINITIVO);
		sqlAss.closeParenthesis();
		
		sql.addSQLNotExistsClause(FindClause.AND, sqlAss);

		BulkHome homeCoan = getHome(userContext,Stipendi_coanBulk.class);
		SQLBuilder sqlCoan = homeCoan.createSQLBuilder();
		sqlCoan.resetColumns();
		sqlCoan.addColumn("1");

		sqlCoan.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		sqlCoan.addSQLJoin("STIPENDI_COAN.CD_UO","V_UNITA_ORGANIZZATIVA_VALIDA.CD_UNITA_ORGANIZZATIVA");
		sqlCoan.addClause(FindClause.AND,"pg_scrittura_an",SQLBuilder.ISNOTNULL,null);
		
		sql.addSQLNotExistsClause(FindClause.AND, sqlCoan);
		//sql.addSQLClause("AND","NOT EXISTS ( SELECT 1 FROM "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+"STIPENDI_COAN WHERE STIPENDI_COAN.PG_SCRITTURA_AN IS NOT NULL AND STIPENDI_COAN.ESERCIZIO = UNITA_ORGANIZZATIVA.ESERCIZIO AND STIPENDI_COAN.CD_UO = UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA )");
	}

	return iterator(userContext,sql,Unita_organizzativaBulk.class,null);
}
public void lockMatricola(UserContext userContext,String id_matricola,int mese) throws ComponentException, BusyResourceException {
	try {
		SQLBuilder sql = getHome(userContext,Costo_del_dipendenteBulk.class).createSQLBuilder();
		sql.addSQLClause(FindClause.AND,"ESERCIZIO",SQLBuilder.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause(FindClause.AND,"ID_MATRICOLA",SQLBuilder.EQUALS,id_matricola);
		sql.addSQLClause(FindClause.AND,"MESE",SQLBuilder.EQUALS,new Integer(mese));
		sql.setForUpdate(true);
		LoggableStatement stm = sql.prepareStatement(getConnection(userContext));
		try {
			java.sql.ResultSet rs = stm.executeQuery();
			while (rs.next());
		} finally {
			try{stm.close();}catch( java.sql.SQLException e ){};
		}
	} catch(java.sql.SQLException e) {
		throw new BusyResourceException();
	}
}
/** 
  *  default
  *    PreCondition:
  *      Viene richiesta la ripartizione dei residui delle percentuali di scarico di un dipendente su un elenco di linee di attività.
  *    PostCondition:
  *      Detto R il residuo del totale dei costi già scaricati per calcolato sottraendo a 100%:
  *      - la somma degli ass_cdp_la già scaricati sui cdr dell'uo dell'utente 
  *      - se l'uo dell'utente è quella della matricola la somma degli ass_cdp_uo accettati o in stato iniziale;
  *      
  *      Vengono create nuove righe ass_cdp_la, una per ognli linea di attività specificata, con le seguenti condizioni:
  *      - le percentuali per ogni anno sono calcolate dividendo R per il numero di linee di attività scelte; sull'ultima linea di attività viene aggiunta l'eventuale resto della divisione (per arrivare a 100 con 2 cifre decimali)
  *      - se una linea di attività è già stata scaricata per quella matricola le percentuali cacolate al precedente punto vengono sommate a quelle già esistenti.
  *  Costi scaricati per linea di attivita non validi
  *    PreCondition:
  *      Viene richiesta la ripartizione dei residui delle percentuali di scarico di un dipendente su un elenco di linee di attività e qualcuno degli ass_cdp_la generati non è valido (validaAss_cdp_la genera un'eccezione)
  *    PostCondition:
  *      Viene lasciata uscire l'eccezione di validazione
 */
public void ripartizioneResidui(it.cnr.jada.UserContext userContext, java.lang.String id_matricola,String cd_unita_organizzativa,int mese,java.util.Collection linee_attivita) throws it.cnr.jada.comp.ComponentException {
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(userContext))
		throw new ApplicationException("Funzione non disponibile ad esercizio chiuso.");

	try {
		// Lock della matricola
		lockMatricola(userContext,id_matricola,mese);

		// Cerco la riga di ass_cdp_uo da cui proviene la matricola
		// Se non esiste vuol dire che la matricola appartiene alla UO dell'utente
		Ass_cdp_uoBulk ass_cdp_uo = (Ass_cdp_uoBulk)getHome(userContext,Ass_cdp_uoBulk.class).findByPrimaryKey(new Ass_cdp_uoBulk(
			cd_unita_organizzativa,
			CNRUserContext.getEsercizio(userContext),
			id_matricola,
			new Integer(mese)));

		if (ass_cdp_uo != null && ass_cdp_uo.isStato_iniziale())
			throw new ApplicationException("Per poter effettuare lo scarico di una matricola proveniente da un altra unità organizzativa è necessario prima accettare la contrattazione.");

		// Eseguo query per il calcolo dei totali già scaricati
		LoggableStatement stm = new LoggableStatement(getHomeCache(userContext).getConnection(),
				"SELECT PRC_A1,PRC_A2,PRC_A3 FROM "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+"V_CDP_TOTALI WHERE ESERCIZIO = ? AND ID_MATRICOLA = ? AND MESE = ? AND "
				+" CD_UNITA_ORGANIZZATIVA = ?",true,this.getClass());
		stm.setInt(1,CNRUserContext.getEsercizio(userContext).intValue());
		stm.setString(2,id_matricola);
		stm.setInt(3,mese);
		stm.setString(4,cd_unita_organizzativa);
		java.sql.ResultSet rs = stm.executeQuery();

		// Costanti
		final java.math.BigDecimal BD_100 = java.math.BigDecimal.valueOf(100);
		final java.math.BigDecimal BD_LATT_S = java.math.BigDecimal.valueOf(linee_attivita.size());
		
		// Calcolo del residuo per ogni anno
		java.math.BigDecimal prc_a1 = BD_100;
		java.math.BigDecimal prc_a2 = BD_100;
		java.math.BigDecimal prc_a3 = BD_100;
		if (rs.next()) {
			prc_a1 = prc_a1.subtract(rs.getBigDecimal(1));
			prc_a2 = prc_a2.subtract(rs.getBigDecimal(2));
			prc_a3 = prc_a3.subtract(rs.getBigDecimal(3));
		} 
		try{rs.close();}catch( java.sql.SQLException e ){};

		// Calcolo della percentuale ripartita per ogni anno
		prc_a1 = prc_a1.divide(BD_LATT_S,2,java.math.BigDecimal.ROUND_HALF_UP);
		prc_a2 = prc_a2.divide(BD_LATT_S,2,java.math.BigDecimal.ROUND_HALF_UP);
		prc_a3 = prc_a3.divide(BD_LATT_S,2,java.math.BigDecimal.ROUND_HALF_UP);

		Ass_cdp_laBulk ass_cdp_la = null;
		// Creazione/modifica delle Ass_cdp_la
		for (java.util.Iterator i = linee_attivita.iterator();i.hasNext();) {
			it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea = (it.cnr.contab.config00.latt.bulk.WorkpackageBulk)i.next();

			// Cerco una ass_cdp_la per la linea attivita corrente
			Ass_cdp_laBulk ass_cdp_la_pk = new Ass_cdp_laBulk(
				linea.getCd_centro_responsabilita(),
				linea.getCd_linea_attivita(),
				CNRUserContext.getEsercizio(userContext),
				id_matricola,
				new Integer(mese));
			ass_cdp_la = (Ass_cdp_laBulk)getHome(userContext,ass_cdp_la_pk).findByPrimaryKey(ass_cdp_la_pk);
			
			if (ass_cdp_la != null) {
				// Se esiste aggiungo alla percentuale già scaricata la
				// percentuale residua ripartita
				ass_cdp_la.setPrc_la_a1(ass_cdp_la.getPrc_la_a1().add(prc_a1));
				ass_cdp_la.setPrc_la_a2(ass_cdp_la.getPrc_la_a2().add(prc_a2));
				ass_cdp_la.setPrc_la_a3(ass_cdp_la.getPrc_la_a3().add(prc_a3));
				ass_cdp_la.setToBeUpdated();
			} else {
				// Se non esiste creo una nuova ass_cdp_la con la
				// percentuale residua ripartita
				ass_cdp_la = ass_cdp_la_pk;
				ass_cdp_la.setToBeCreated();
				ass_cdp_la.setFl_dip_altra_uo(ass_cdp_uo == null ? Boolean.FALSE : Boolean.TRUE);
				ass_cdp_la.setStato(ass_cdp_la.STATO_NON_SCARICATO);
				ass_cdp_la.setPrc_la_a1(prc_a1);
				ass_cdp_la.setPrc_la_a2(prc_a2);
				ass_cdp_la.setPrc_la_a3(prc_a3);
			}
			ass_cdp_la.setUser(userContext.getUser());

			// Controllo di validità della ass_cdp_la creata/modificata
			validaAss_cdp_la(userContext,ass_cdp_la);

			// Salvo la ass_cpd_la
			makeBulkPersistent(userContext,ass_cdp_la);
		}

		if (ass_cdp_la != null) {
			// Se è l'ultima linea di attività aggiungo il resto della divisione
			rs = stm.executeQuery();
			rs.next();
			ass_cdp_la.setPrc_la_a1(ass_cdp_la.getPrc_la_a1().add(BD_100.subtract(rs.getBigDecimal(1))));
			ass_cdp_la.setPrc_la_a2(ass_cdp_la.getPrc_la_a2().add(BD_100.subtract(rs.getBigDecimal(2))));
			ass_cdp_la.setPrc_la_a3(ass_cdp_la.getPrc_la_a3().add(BD_100.subtract(rs.getBigDecimal(3))));
			ass_cdp_la.setToBeUpdated();
			try{rs.close();}catch( java.sql.SQLException e ){};
			makeBulkPersistent(userContext,ass_cdp_la);
		}

	} catch(Throwable e) {
		throw handleException(e);
	}
}
/** 
  *  default
  *    PreCondition:
  *      Viene richiesto il salvatggio dello stato di scarico dei costi di un dipendente
  *    PostCondition:
  *      Tutti gli oggetti (Ass_cdp_laBulk e Ass_cdp_uoBulk) vengono resi persistenti; Viene restituito un oggetto  Costi_dipendenteVBulk conforme al valore di ritorno del metodo "caricaCosti_dipendente"
  *  Matricola modificata in concorrenza con altro utente
  *    PreCondition:
  *      Viene richiesto il salvatggio dello stato di scarico dei costi di un dipendente e qualche altro utente sta effettuando la medesima operazione sulla stessa matricola
  *    PostCondition:
  *      Viene generata una BusyResourceException
  *  Risorsa occupata per i Costi stipendiali mensili 
  *    PreCondition:
  *      Viene richiesto il salvatggio dello stato di scarico dei costi di un dipendente e qualche altro utente sta tentando di generare la ripartizione analitica
  *    PostCondition:
  *      Viene generata una BusyResourceException
  *  Costi scaricati per linea di attivita non validi
  *    PreCondition:
  *      Viene richiesto il salvatggio dello stato di scarico dei costi di un dipendente e qualcuno degli ass_cdp_la modificati non è valido (validaAss_cdp_la genera un'eccezione)
  *    PostCondition:
  *      Viene lasciata uscire l'eccezione di validazione
  *  Costi scaricati su altra unità organizzativa non validi
  *    PreCondition:
  *      Viene richiesto il salvatggio dello stato di scarico dei costi di un dipendente e qualcuno degli ass_cdp_uo modificati non è valido (validaAss_cdp_uo genera un'eccezione)
  *    PostCondition:
  *      Viene lasciata uscire l'eccezione di validazione
  *  Somma delle percentuali scaricate superiore a 100%
  *    PreCondition:
  *      Viene richiesto il salvataggio dello stato di scarico dei costi di un dipendente e la somma delle percentuali scaricate sui cdr dell'unità organizzativa dell'utente più la somma delle percentuali scaricate su altre UO è superiore a 100 in qualche anno
  *    PostCondition:
  *      Viene generata una eccezione con il messaggio "La somma delle percentuali scaricate è superiore a 100%"
 */
public OggettoBulk salvaCosti_dipendente(UserContext userContext,Costi_dipendenteVBulk costi_dipendente) throws ComponentException {
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(userContext))
		throw new ApplicationException("Importi non modificabili ad esercizio chiuso.");

	for (Iterator<V_cdp_matricolaBulk> iterator = costi_dipendente.getCosti_dipendenti().iterator(); iterator.hasNext();) {
		V_cdp_matricolaBulk cdp = iterator.next();
		cdp = salvaCosti_dipendente(userContext, cdp);
	}
	return costi_dipendente;
}
public V_cdp_matricolaBulk salvaCosti_dipendente(UserContext userContext,V_cdp_matricolaBulk cdp) throws ComponentException {
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(userContext))
		throw new ApplicationException("Importi non modificabili ad esercizio chiuso.");

	try {
		// Lock della matricola
		lockMatricola(userContext,cdp.getId_matricola(),cdp.getMese());

		// Controllo dei constraint SQL (campi not null, lunghezza...)
		for (java.util.Iterator j = cdp.getCostiScaricati().iterator();j.hasNext();)
			checkSQLConstraints(userContext,(OggettoBulk)j.next());

		// Controllo di validità degli ass_cdp_la modificati o cancellati
		for (java.util.Iterator i = cdp.getCostiScaricati().iterator();i.hasNext();) {
			Ass_cdp_laBulk assCdpLa = (Ass_cdp_laBulk)i.next();
			validaAss_cdp_la(userContext,assCdpLa);
			if (cdp.getMese()!=0)
				assCdpLa.setStato(Ass_cdp_laBulk.STATO_SCARICATO_PROVVISORIO);
		}
		for (java.util.Iterator i = cdp.getCostiScaricati().deleteIterator();i.hasNext();)
			validaAss_cdp_la(userContext,(Ass_cdp_laBulk)i.next());

		// Controllo di validità degli ass_cdp_uo modificati o cancellati
		if (cdp.getCostiScaricatiAltraUO() != null) {
			for (java.util.Iterator i = cdp.getCostiScaricatiAltraUO().iterator();i.hasNext();) {
				Ass_cdp_uoBulk assCdpUo = (Ass_cdp_uoBulk)i.next();
				validaAss_cdp_uo(userContext,assCdpUo);
			}
			for (java.util.Iterator i = ((BulkList)cdp.getCostiScaricatiAltraUO()).deleteIterator();i.hasNext();)
				validaAss_cdp_uo(userContext,(Ass_cdp_uoBulk)i.next());
		}

		// Rendo persistenti gli ass_cdp_la e gli ass_cdp_uo modificati
		makeBulkListPersistent(userContext,(BulkList)cdp.getCostiScaricati());
		makeBulkListPersistent(userContext,(BulkList)cdp.getCostiScaricatiAltraUO());
		// Rendo persistente l'ass_cdp_uo che rappresenta il costo caricato
		// (se l'utente ha modificato lo stato di scarico)
		if (cdp.getCostoCaricato() != null)
			makeBulkPersistent(userContext,cdp.getCostoCaricato());

		// Eseguo una query per controllare che il totale dei costi scaricati
		// non sia superiore a 100%
		LoggableStatement stm = new LoggableStatement(getHomeCache(userContext).getConnection(),
				"SELECT 0 FROM "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+"V_CDP_TOTALI WHERE ESERCIZIO = ? AND ID_MATRICOLA = ? AND MESE = ? AND "
				+" CD_UNITA_ORGANIZZATIVA = ? AND ( PRC_A1 > 100 OR PRC_A2 > 100 OR PRC_A3 > 100 )",true,this.getClass());
		stm.setInt(1,cdp.getEsercizio());
		stm.setString(2,cdp.getId_matricola());
		stm.setInt(3,cdp.getMese());
		stm.setString(4,cdp.getCd_unita_organizzativa());
		java.sql.ResultSet rs = stm.executeQuery();
		if (rs.next())
			throw new ApplicationException("La somma delle percentuali scaricate è superiore a 100%");
		try{rs.close();}catch( java.sql.SQLException e ){};
		try{stm.close();}catch( java.sql.SQLException e ){};

		return cdp;
	} catch(Throwable e) {
		throw handleException(e);
	}
}
/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
  *
  * Nome: Richiesta di ricerca di una Unita Organizzativa per la Stampa dei degli Imponibili Dipendenti
  * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
  * Post: Viene restituito l'SQLBuilder per filtrare le UO
  *		  in base al cds di scrivania
  *
  * @param userContext	lo userContext che ha generato la richiesta
  * @param stampa		l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param uo			l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param				clauses L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  *			della query.
  *
**/
public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_imponibili_dipendentiVBulk stampa, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {

	Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class);
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "cd_unita_padre", sql.EQUALS, stampa.getCd_cds());
	sql.addClause(clauses);
	return sql;
}

/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
  *
  * Nome: Richiesta di ricerca di una Unita Organizzativa per la Stampa dei degli Imponibili Dipendenti
  * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
  * Post: Viene restituito l'SQLBuilder per filtrare le UO
  *		  in base al cds di scrivania
  *
  * @param userContext	lo userContext che ha generato la richiesta
  * @param stampa		l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param uo			l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param				clauses L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  *			della query.
  *
**/
public SQLBuilder selectUoForPrintByClause(UserContext usercontext, Stampa_ripartizione_costiVBulk stampa_ripartizione_costivbulk, Unita_organizzativaBulk unita_organizzativabulk, CompoundFindClause compoundfindclause)
	throws ComponentException
{
	Unita_organizzativaHome unita_organizzativahome = (Unita_organizzativaHome)getHome(usercontext, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class);
	SQLBuilder sqlbuilder = unita_organizzativahome.createSQLBuilder();
	sqlbuilder.addClause("AND", "cd_unita_padre", sqlbuilder.EQUALS, stampa_ripartizione_costivbulk.getCd_cds());
	sqlbuilder.addClause(compoundfindclause);
	return sqlbuilder;
}
public SQLBuilder selectDipendenteForPrintByClause(UserContext usercontext, Stampa_ripartizione_costiVBulk stampa_ripartizione_costivbulk, V_dipendenteBulk dipendenteBulk, CompoundFindClause compoundfindclause)
	throws ComponentException, it.cnr.jada.persistency.PersistencyException
{
	V_dipendenteHome dipendentehome = (V_dipendenteHome)getHome(usercontext, V_dipendenteBulk.class,"V_DIPENDENTE_RID");
	SQLBuilder sqlbuilder = dipendentehome.createSQLBuilder();
	sqlbuilder.addSQLClause("AND", "ESERCIZIO", sqlbuilder.EQUALS, stampa_ripartizione_costivbulk.getEsercizio());
	// Se uo 999.000 in scrivania: visualizza tutti i dipendenti
	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( usercontext, Unita_organizzativa_enteBulk.class).findAll().get(0);
	if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
		sqlbuilder.addSQLClause("AND","cd_unita_organizzativa",sqlbuilder.EQUALS,CNRUserContext.getCd_unita_organizzativa(usercontext));	
	}
	sqlbuilder.setDistinctClause(Boolean.TRUE);
	sqlbuilder.addClause(compoundfindclause);
	sqlbuilder.setOrderBy("nominativo",it.cnr.jada.util.OrderConstants.ORDER_ASC);
	return sqlbuilder;
}
public SQLBuilder selectCommessaForPrintByClause(UserContext usercontext, Stampa_ripartizione_costiVBulk stampa_ripartizione_costivbulk, ProgettoBulk progettoBulk, CompoundFindClause compoundfindclause)
	throws ComponentException, it.cnr.jada.persistency.PersistencyException, RemoteException, EJBException
{
	Parametri_cnrBulk par=Utility.createParametriCnrComponentSession().getParametriCnr(usercontext, CNRUserContext.getEsercizio(usercontext));
	ProgettoHome progettohome = (ProgettoHome)getHome(usercontext, ProgettoBulk.class,"V_PROGETTO_PADRE");
	SQLBuilder sqlbuilder = progettohome.createSQLBuilder();
	if (par.getFl_nuovo_pdg())
		sqlbuilder.addSQLClause("AND", "LIVELLO", sqlbuilder.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_PRIMO);
	else
		sqlbuilder.addSQLClause("AND", "LIVELLO", sqlbuilder.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
	sqlbuilder.addSQLClause("AND", "TIPO_FASE", sqlbuilder.EQUALS, ProgettoBulk.TIPO_FASE_PREVISIONE);
	sqlbuilder.addSQLClause("AND", "ESERCIZIO", sqlbuilder.EQUALS, stampa_ripartizione_costivbulk.getEsercizio());
	// Se uo 999.000 in scrivania: visualizza tutti i progetti
	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( usercontext, Unita_organizzativa_enteBulk.class).findAll().get(0);
	if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
		if (par.getFl_nuovo_pdg())
			sqlbuilder.addSQLExistsClause("AND",progettohome.abilitazioniProgetti(usercontext));
		else
			sqlbuilder.addSQLExistsClause("AND",progettohome.abilitazioniCommesse(usercontext));
	}	
	sqlbuilder.addClause(compoundfindclause);
	return sqlbuilder;
}
public SQLBuilder selectModuloForPrintByClause(UserContext usercontext, Stampa_ripartizione_costiVBulk stampa_ripartizione_costivbulk, ProgettoBulk progettoBulk, CompoundFindClause compoundfindclause)
	throws ComponentException, it.cnr.jada.persistency.PersistencyException, RemoteException, EJBException
{
	Parametri_cnrBulk par=Utility.createParametriCnrComponentSession().getParametriCnr(usercontext, CNRUserContext.getEsercizio(usercontext));
	ProgettoHome progettohome = (ProgettoHome)getHome(usercontext, ProgettoBulk.class,"V_PROGETTO_PADRE");
	SQLBuilder sqlbuilder = progettohome.createSQLBuilder();
	if (par.getFl_nuovo_pdg())
		sqlbuilder.addSQLClause("AND", "LIVELLO", sqlbuilder.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
	else
		sqlbuilder.addSQLClause("AND", "LIVELLO", sqlbuilder.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO);

	sqlbuilder.addSQLClause("AND", "TIPO_FASE", sqlbuilder.EQUALS, ProgettoBulk.TIPO_FASE_PREVISIONE);
	sqlbuilder.addSQLClause("AND", "ESERCIZIO", sqlbuilder.EQUALS, stampa_ripartizione_costivbulk.getEsercizio());
	if(stampa_ripartizione_costivbulk.getCommessaForPrint()!= null && stampa_ripartizione_costivbulk.getCommessaForPrint().getPg_progetto()!=null)
	  sqlbuilder.addClause("AND", "pg_progetto_padre", sqlbuilder.EQUALS, stampa_ripartizione_costivbulk.getCommessaForPrint().getPg_progetto());
	// Se uo 999.000 in scrivania: visualizza tutti i progetti
	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( usercontext, Unita_organizzativa_enteBulk.class).findAll().get(0);
	if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
		if (par.getFl_nuovo_pdg())
			sqlbuilder.addSQLExistsClause("AND",progettohome.abilitazioniCommesse(usercontext));
		else
			sqlbuilder.addSQLExistsClause("AND",progettohome.abilitazioniModuli(usercontext));
	}	  
	sqlbuilder.addClause(compoundfindclause);
	return sqlbuilder;
}
/**
 * stampaConBulk method comment.
 */
public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	return bulk;
}
/** 
  *  Costi del dipendente, PDG chiuso
  *    PreCondition:
  *		 L'Ass_cdp_la ha mese = 0
  *      Viene richiesta una operazione di persistenza su un Ass_cdp_la e il PDG del cdr associato è in stato diverso da A, B, D o E
  *    PostCondition:
  *      Viene generata una ApplicationException con il messaggio "Il pdg relativo del cdr xxx.yy.zzz è già chiuso. Non è possibile scaricarvi costi."
  */
private void validaAss_cdp_la(UserContext userContext,Ass_cdp_laBulk ass_cdp_la) throws ComponentException {
	try {
		// Controllo che tutti i cdr su cui scarico i costi dei dipendenti
		// non siano già chiusi.
		if (ass_cdp_la.getCrudStatus() != ass_cdp_la.NORMAL) {
			if (ass_cdp_la.getMese().intValue() == 0) {
				CdrBulk cdrPdgP = getCdrPdgP(userContext);
				Pdg_esercizioBulk pdgp =
					(Pdg_esercizioBulk)getHome(userContext,Pdg_esercizioBulk.class).findAndLock(
								new Pdg_esercizioBulk(
									it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),
							        cdrPdgP.getCd_centro_responsabilita()
								)
					);
				if (!isStatoPdgPValidoPerModificaCDP(pdgp))
					throw new ApplicationException("Il pdgP relativo del cdr "+getCdrPdgP(userContext).getCd_centro_responsabilita()+" è già chiuso. Non è possibile scaricarvi costi.");
			}
		}
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	} catch(OutdatedResourceException e) {
		throw handleException(e);
	} catch(BusyResourceException e) {
		throw handleException(e);
	}
}
/** 
  *  Costi del dipendente, PDG chiuso
  *    PreCondition:
  *		 L'Ass_cdp_uo ha mese = 0
  *      Viene richiesta una operazione di persistenza su un Ass_cdp_uo e il PDG del cdr responsabile dell'UO associata è in stato diverso da A, B, D o E
  *    PostCondition:
  *      Viene generata una ApplicationException con il messaggio "Il pdg relativo del cdr responsabile dell'uo xxx.yy è già chiuso. Non è possibile scaricarvi costi."
  *
  *  Costi stipendiali mensili, ripartizione analitica già generata
  *    PreCondition:
  *		 L'Ass_cdp_uo ha mese > 0
  *      Viene richiesta una operazione di persistenza su un Ass_cdp_uo ed è già stata generata la scrittura analitica per il mese selezionato
  *    PostCondition:
  *      Viene generata una ApplicationException con il messaggio "Non è possibile modificare la ripartizione perchè è già stato effettuato lo scarico in analitica per l'unità organizzativa xyz.abc"
  */
private void validaAss_cdp_uo(UserContext userContext,Ass_cdp_uoBulk ass_cdp_uo) throws ComponentException {
	try {
		if (ass_cdp_uo.getCrudStatus() != ass_cdp_uo.NORMAL) {
			// 05/09/2003
			// Aggiunto controllo sulla chiusura dell'esercizio
			if (isEsercizioChiuso(userContext,ass_cdp_uo.getUnita_organizzativa()))
				throw new ApplicationException("Non è possibile modificare la ripartizione verso l'uo "+ass_cdp_uo.getCd_unita_organizzativa()+" ad esercizio chiuso.");

			if (ass_cdp_uo.getMese().intValue() == 0) {
				// Controllo che il cdr responsabilie dell'uo su cui scarico i costi dei dipendenti
				// non abbia già il pdg chiuso
				BulkHome home = getHome(userContext,Pdg_esercizioBulk.class);
				SQLBuilder sql = home.createSQLBuilder();
				sql.addSQLClause("AND","PDG_ESERCIZIO.ESERCIZIO",sql.EQUALS,ass_cdp_uo.getEsercizio());

				V_struttura_organizzativaHome struttHome = (V_struttura_organizzativaHome)getHome(userContext,V_struttura_organizzativaBulk.class);
				CdrBulk cdr = struttHome.findCDRBaseCDS(ass_cdp_uo.getUnita_organizzativa(), ass_cdp_uo.getEsercizio());

				sql.addSQLClause("AND","PDG_ESERCIZIO.CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdr.getCd_centro_responsabilita());

				sql.setForUpdate(true);
				Pdg_esercizioBulk pdgp = (Pdg_esercizioBulk)home.fetchAndLock(sql);
				if (!isStatoPdgPValidoPerModificaCDP(pdgp))
					throw new ApplicationException("Il pdgP relativo del cdr responsabile dell'uo"+ass_cdp_uo.getCd_unita_organizzativa()+" è già chiuso. Non è possibile scaricarvi costi.");
			} else {
				if (!isStatoAnaliticaValidoPerModificaCDP(userContext,ass_cdp_uo.getCd_unita_organizzativa(),ass_cdp_uo.getEsercizio(),ass_cdp_uo.getMese().intValue()))
					throw new ApplicationException("Non è possibile modificare la ripartizione perchè è già stato effettuato lo scarico in analitica per l'unità organizzativa "+ass_cdp_uo.getCd_unita_organizzativa()+".");
			}
		}
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	} catch(OutdatedResourceException e) {
		throw handleException(e);
	} catch(BusyResourceException e) {
		throw handleException(e);
	}
}
/** 
 *  Centro di responsabilità del Piano di Gestione Preventivo
 *    PreCondition:
 *      Viene richiesto il CDR utilizzabile per il caricamento del Piano di Gestione Preventivo
 *    PostCondition:
 *      Viene restituita un CdrBulk
*/
private CdrBulk getCdrPdgP (UserContext userContext)  throws ComponentException {
	try
	{
		PdgAggregatoModuloComponentSession session = (PdgAggregatoModuloComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
				  "CNRPREVENT01_EJB_PdgAggregatoModuloComponentSession", 
				  PdgAggregatoModuloComponentSession.class);

		CdrBulk cdrUtente = session.cdrFromUserContext(userContext);
		return getCdrPdgP(userContext, cdrUtente);
	}
	catch( Exception e )
	{
		throw handleException( e );
	}
}
/** 
  *  Centro di responsabilità del Piano di Gestione Preventivo
  *    PreCondition:
  *      Viene richiesto il CDR utilizzabile per il caricamento del Piano di Gestione Preventivo
  *    PostCondition:
  *      Viene restituita un CdrBulk
 */
private CdrBulk getCdrPdgP (UserContext userContext, CdrBulk cdrUtente)  throws ComponentException {
	try
	{
		SQLBuilder sql = getHome(userContext, CdrBulk.class, "V_CDR_VALIDO").createSQLBuilder();
		
		sql.addSQLClause("AND", "esercizio", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		
		if (cdrUtente == null) 
			throw new ApplicationException("L'utente non è configurato correttamente per l'utilizzo del pdg preliminare");
		
		// riempiamo i dati di cdrUtente.getUnita_padre() dato che ci servono
		cdrUtente.setUnita_padre((Unita_organizzativaBulk)getHome(userContext,cdrUtente.getUnita_padre()).findByPrimaryKey(cdrUtente.getUnita_padre()));
		
		if (cdrUtente.getLivello().equals(new Integer(1)) || cdrUtente.getUnita_padre().isUoArea() ||
			isCdrSAC(userContext, cdrUtente)) {
			sql.addSQLClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, cdrUtente.getCd_centro_responsabilita());
		}
		else {
			sql.addSQLClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, cdrUtente.getCd_cdr_afferenza());
		}
	
		List result = getHome( userContext, CdrBulk.class ).fetchAll( sql );
		if ( result.size() > 1 || result.size() == 0)
			throw new ApplicationException("Impossibile determinare il CdR legato alla UO in scrivania.");

		return (CdrBulk) result.get(0);	
	}
	catch( Exception e )
	{
		throw handleException( e );
	}
}
/** 
  *  Metodo che ritorna se è possibile o meno effettuare altre ripartizioni dei costi
  *    PreCondition:
  *      Viene richiesto se è possibile effettuare altre ripartizioni di costi
  *    PostCondition:
  *      Viene restituita un boolean
 */
public boolean isPdgPrevisionaleEnabled (UserContext userContext)  throws ComponentException {
	try {
		SQLBuilder sql = getHome(userContext, Pdg_moduloBulk.class).createSQLBuilder();
			
		sql.addSQLClause("AND","PDG_MODULO.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND","PDG_MODULO.CD_CENTRO_RESPONSABILITA",sql.EQUALS,getCdrPdgP(userContext).getCd_centro_responsabilita());
		sql.addSQLClause("AND","PDG_MODULO.STATO", sql.NOT_EQUALS, Pdg_moduloBulk.STATO_AC);
		sql.addSQLClause("AND","PDG_MODULO.STATO", sql.NOT_EQUALS, Pdg_moduloBulk.STATO_AD);
	
		List result = getHome( userContext, Pdg_moduloBulk.class ).fetchAll( sql );
		if ( result.size() > 0 )
			return false;
		return true;	
	}
	catch( Exception e )
	{
		throw handleException( e );
	}
}
/** 
  *  Metodo che ritorna se è possibile o meno effettuare altre ripartizioni dei costi per il 
  *  CDR di scrivania
  *    PreCondition:
  *      Viene richiesto se è possibile effettuare altre ripartizioni di costi
  *    PostCondition:
  *      Viene restituita un boolean
 */
public boolean isCostiDipendenteRipartiti (UserContext userContext)  throws ComponentException {
	return isCostiDipendenteRipartiti(userContext, getCdrPdgP(userContext)); 		
}
/** 
  *  Metodo che ritorna se è possibile o meno effettuare altre ripartizioni dei costi per il 
  *  CDR indicato
  *    PreCondition:
  *      Viene richiesto se è possibile effettuare altre ripartizioni di costi per il CDR <CdrBulk>
  *    PostCondition:
  *      Viene restituita un boolean
 */
public boolean isCostiDipendenteRipartiti (UserContext userContext, CdrBulk cdr)  throws ComponentException {
	try {
		SQLBuilder sql = getHome(userContext, Ass_cdp_laBulk.class).createSQLBuilder();
			
		// se il CdR è della SAC deve esser controllato direttamente
		// altrimenti si vede l'afferenza
		sql.addSQLClause("AND","ASS_CDP_LA.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND","ASS_CDP_LA.MESE",sql.EQUALS,BigDecimal.ZERO);
		
		if (isCdrSAC(userContext, cdr)) {
			sql.addSQLClause("AND","ASS_CDP_LA.CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdr.getCd_centro_responsabilita());
		}
		else {
			sql.addToHeader("V_STRUTTURA_ORGANIZZATIVA");
			sql.addSQLJoin("ASS_CDP_LA.ESERCIZIO", "V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO");
			sql.addSQLJoin("ASS_CDP_LA.CD_CENTRO_RESPONSABILITA", "V_STRUTTURA_ORGANIZZATIVA.CD_ROOT");
			sql.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.CD_CDR_AFFERENZA",sql.EQUALS,cdr.getCd_centro_responsabilita());
		}

		// controlliamo prima che esistano dati per cui effettuare la ripartizione
		List result = getHome( userContext, Ass_cdp_laBulk.class ).fetchAll( sql );
		if ( result.isEmpty() )
			return false;

		sql.openParenthesis("AND");
		sql.addSQLClause("AND","ASS_CDP_LA.STATO", sql.EQUALS, Ass_cdp_laBulk.STATO_SCARICATO);
		sql.addSQLClause("OR","ASS_CDP_LA.STATO", sql.EQUALS, Ass_cdp_laBulk.STATO_SCARICATO_PDGP);
		sql.closeParenthesis();
	
		result = getHome( userContext, Ass_cdp_laBulk.class ).fetchAll( sql );
		if ( result.size() > 0 )
			return true;
		return false;	
	}
	catch( Exception e )
	{
		throw handleException( e );
	}
}
/** 
  *  Metodo che ritorna se è possibile o meno effettuare altre ripartizioni dei costi per il 
  *  CDR e il Modulo indicati
  *    PreCondition:
  *      Viene richiesto se è possibile effettuare altre ripartizioni di costi per il CDR <CdrBulk>, Modulo <ProgettoBulk>
  *    PostCondition:
  *      Viene restituita un boolean
 */
public boolean isCostiDipendenteRipartiti (UserContext userContext, CdrBulk cdr, Progetto_sipBulk modulo)  throws ComponentException {
	try {
		SQLBuilder sql = getHome(userContext, Ass_cdp_laBulk.class).createSQLBuilder();
			
		// se il CdR è della SAC deve esser controllato direttamente
		// altrimenti si vede l'afferenza
		sql.addSQLClause("AND","ASS_CDP_LA.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND","ASS_CDP_LA.MESE",sql.EQUALS,BigDecimal.ZERO);
		
		if (isCdrSAC(userContext, cdr)) {
			sql.addToHeader("LINEA_ATTIVITA");
			sql.addSQLClause("AND","LINEA_ATTIVITA.PG_PROGETTO",sql.EQUALS,modulo.getPg_progetto());
			sql.addSQLJoin("ASS_CDP_LA.CD_CENTRO_RESPONSABILITA", "LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA");
			sql.addSQLJoin("ASS_CDP_LA.CD_LINEA_ATTIVITA", "LINEA_ATTIVITA.CD_LINEA_ATTIVITA");		
			sql.addSQLClause("AND","ASS_CDP_LA.CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdr.getCd_centro_responsabilita());
		}
		else {
			sql.addToHeader("V_STRUTTURA_ORGANIZZATIVA");
			sql.addToHeader("LINEA_ATTIVITA");
			sql.addSQLClause("AND","LINEA_ATTIVITA.PG_PROGETTO",sql.EQUALS,modulo.getPg_progetto());
			sql.addSQLJoin("ASS_CDP_LA.CD_CENTRO_RESPONSABILITA", "LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA");
			sql.addSQLJoin("ASS_CDP_LA.CD_LINEA_ATTIVITA", "LINEA_ATTIVITA.CD_LINEA_ATTIVITA");		
			sql.addSQLJoin("ASS_CDP_LA.ESERCIZIO", "V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO");
			sql.addSQLJoin("ASS_CDP_LA.CD_CENTRO_RESPONSABILITA", "V_STRUTTURA_ORGANIZZATIVA.CD_ROOT");
			sql.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.CD_CDR_AFFERENZA",sql.EQUALS,cdr.getCd_centro_responsabilita());
		}
		
		// controlliamo prima che esistano dati per cui effettuare la ripartizione
		List result = getHome( userContext, Ass_cdp_laBulk.class ).fetchAll( sql );
		if ( result.isEmpty() )
			return true;

		sql.openParenthesis("AND");
		sql.addSQLClause("AND","ASS_CDP_LA.STATO", sql.EQUALS, Ass_cdp_laBulk.STATO_SCARICATO);
		sql.addSQLClause("OR","ASS_CDP_LA.STATO", sql.EQUALS, Ass_cdp_laBulk.STATO_SCARICATO_PDGP);
		sql.closeParenthesis();
	
		result = getHome( userContext, Ass_cdp_laBulk.class ).fetchAll( sql );
		if ( result.size() > 0 )
			return true;
		return false;	
	}
	catch( Exception e )
	{
		throw handleException( e );
	}
}
/** 
  *  Metodo che ritorna se è possibile o meno effettuare altre ripartizioni dei costi per il 
  *  CDR e il Modulo indicati
  *    PreCondition:
  *      Viene richiesto se è possibile effettuare altre ripartizioni di costi per il CDR <CdrBulk>, Modulo <ProgettoBulk>
  *    PostCondition:
  *      Viene restituita un boolean
 */
public boolean isModuloUtilizzato (UserContext userContext, CdrBulk cdr, Progetto_sipBulk modulo)  throws ComponentException {
	try {
		int esercizio = CNRUserContext.getEsercizio(userContext);

		SQLBuilder sql = getHome(userContext, Ass_cdp_laBulk.class).createSQLBuilder();
		sql.addToHeader("V_STRUTTURA_ORGANIZZATIVA");
		sql.addToHeader("V_LINEA_ATTIVITA_VALIDA");
		sql.addSQLClause("AND","V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,esercizio);
		sql.addSQLClause("AND","V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO",SQLBuilder.EQUALS,modulo.getPg_progetto());
		sql.addSQLJoin("ASS_CDP_LA.ESERCIZIO", "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO");
		sql.addSQLJoin("ASS_CDP_LA.CD_CENTRO_RESPONSABILITA", "V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA");
		sql.addSQLJoin("ASS_CDP_LA.CD_LINEA_ATTIVITA", "V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA");		
		sql.addSQLJoin("ASS_CDP_LA.ESERCIZIO", "V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO");
		sql.addSQLJoin("ASS_CDP_LA.CD_CENTRO_RESPONSABILITA", "V_STRUTTURA_ORGANIZZATIVA.CD_ROOT");
		sql.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.CD_CDR_AFFERENZA",SQLBuilder.EQUALS,cdr.getCd_centro_responsabilita());
	
		List result = getHome( userContext, Ass_cdp_laBulk.class ).fetchAll( sql );
		if ( result.size() > 0 )
			return true;
		return false;	
	}
	catch( Exception e )
	{
		throw handleException( e );
	}
}
/** 
  *  Metodo che ritorna se la riga delle spese proviene dallo scarico dei dipendenti 
  *  Spese indicate
  *    PreCondition:
  *      Viene richiesto se la riga delle spese proviene da una ripartizione dei costi del personale
  *    PostCondition:
  *      Viene restituita un boolean
 */
public boolean isSpeseFromScaricoDipendente(UserContext userContext, Pdg_modulo_speseBulk pdg_modulo_spese)  throws ComponentException {
	try {
		SQLBuilder sql = new SQLBuilder();
		sql.setHeader("SELECT 1");
		sql.addTableToHeader("ASS_CDP_PDGP");
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,pdg_modulo_spese.getEsercizio());
		sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",sql.EQUALS,pdg_modulo_spese.getCd_centro_responsabilita());
		sql.addSQLClause("AND","PG_PROGETTO_SPESE",sql.EQUALS,pdg_modulo_spese.getPg_progetto());
		sql.addSQLClause("AND","ID_CLASSIFICAZIONE",sql.EQUALS,pdg_modulo_spese.getId_classificazione());
		sql.addSQLClause("AND","CD_CDS_AREA",sql.EQUALS,pdg_modulo_spese.getCd_cds_area());
		//sql.addSQLClause("AND","PG_DETTAGLIO",sql.EQUALS,pdg_modulo_spese.getPg_dettaglio());
		
		LoggableStatement stm = sql.prepareStatement(getConnection(userContext));
		try {
			java.sql.ResultSet rs = stm.executeQuery();
			if (rs.next())
			  return true;
		} finally {
			try{stm.close();}catch( java.sql.SQLException e ){};
		}
	return false;
	}
	catch( Exception e )
	{
		throw handleException( e );
	}
}

/** 
  *  Metodo che ritorna se sono stati caricati i costi del dipendente da ripartire per almeno una UO
  *  appartenente al CDS del CDR di scrivania 
  *    PreCondition:
  *      Viene richiesto se sono stati caricati record nella tabella COSTO_DEL_DIPENDENTE per almeno una 
  *      Uo appartenente al CDS del CDR di scrivania 
  *    PostCondition:
  *      Viene restituita un boolean
 */
public boolean isCostiDipendenteCaricati (UserContext userContext)  throws ComponentException {
	return isCostiDipendenteCaricati(userContext, getCdrPdgP(userContext)); 		
}
/** 
  *  Metodo che ritorna se sono stati caricati i costi del dipendente da ripartire per almeno una UO
  *  appartenente al CDS del CDR indicato come parametro 
  *    PreCondition:
  *      Viene richiesto se sono stati caricati record nella tabella COSTO_DEL_DIPENDENTE per almeno una 
  *      Uo appartenente al CDS del CDR <CdrBulk> indicato come parametro 
  *    PostCondition:
  *      Viene restituita un boolean
 */
public boolean isCostiDipendenteCaricati (UserContext userContext, CdrBulk cdr)  throws ComponentException {
	try {
		// Estraggo tutti i V_cdp_matricolaBulk che competono all'utente
		BulkHome home = getHome(userContext,V_cdp_matricolaBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND","MESE",sql.EQUALS,BigDecimal.ZERO);
		if (isCdrSAC(userContext, cdr) && cdr.getUnita_padre()!=null) {
			sql.addSQLClause("AND","CD_UO_CARICO",sql.EQUALS,cdr.getUnita_padre().getCd_unita_organizzativa());
		}
		else{
			V_struttura_organizzativaHome homeStrutt = (V_struttura_organizzativaHome)getHome(userContext,V_struttura_organizzativaBulk.class);
			List uoList = (List)homeStrutt.findUoCollegateCDS(cdr.getUnita_padre(), CNRUserContext.getEsercizio(userContext));
			Unita_organizzativaBulk uoBulk;
			
			sql.openParenthesis("AND");
			for ( Iterator uoIterator = uoList.iterator(); uoIterator.hasNext();) {
				uoBulk = (Unita_organizzativaBulk) uoIterator.next();
				sql.addSQLClause("OR", "CD_UO_CARICO",sql.EQUALS,uoBulk.getCd_unita_organizzativa());
			}
			sql.closeParenthesis();
		}
		List result = getHome( userContext, V_cdp_matricolaBulk.class ).fetchAll( sql );
		if ( result.size() > 0 )
			return true;
		return false;	
	}
	catch( Exception e )
	{
		throw handleException( e );
	}
}
public SQLBuilder selectUnita_organizzativa_filterByClause(UserContext userContext, Costi_dipendenteVBulk costiDipendente, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException
{				  
	try {
		Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext);
  		String cd_unita_organizzativa = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk) getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cd_unita_organizzativa));

		SQLBuilder sql = getHome(userContext, Unita_organizzativaBulk.class).createSQLBuilder();
		sql.addClause( clauses );

 		String from = sql.getFromClause().toString() + ".";
	
		if (uoScrivania.getFl_uo_cds().booleanValue()){
			sql.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA");
			sql.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO",sql.EQUALS,esercizio);
			sql.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.CD_CDS",sql.EQUALS,uoScrivania.getCd_cds());
			sql.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.CD_TIPO_LIVELLO",sql.EQUALS,V_struttura_organizzativaHome.LIVELLO_UO);
			sql.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA",from +"CD_UNITA_ORGANIZZATIVA");
		}else{
			sql.addSQLClause("AND",from +"CD_UNITA_ORGANIZZATIVA",sql.EQUALS,cd_unita_organizzativa);				        					
		}
		return sql;		
	} catch (PersistencyException e) {
		throw handleException( e );
	} catch (ComponentException e) {
		throw handleException( e );
	}		
}
public SQLBuilder selectUnita_organizzativa_scaricoByClause(UserContext userContext, Costi_dipendenteVBulk costiDipendente, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {
	//nel caso della ripartizione mensile occorre far vedere ance le UO chiuse
	BulkHome home = getHome(userContext,Unita_organizzativaBulk.class,"V_UNITA_ORGANIZZATIVA_VALIDA");
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause(FindClause.AND,"cd_unita_organizzativa",SQLBuilder.NOT_EQUALS,costiDipendente.getUnita_organizzativa_filter().getCd_unita_organizzativa());
	sql.addSQLClause(FindClause.AND,"V_UNITA_ORGANIZZATIVA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));

	if (costiDipendente.getMese() == 0) {
		sql.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "A");
		sql.addSQLJoin("V_UNITA_ORGANIZZATIVA_VALIDA.ESERCIZIO","A.ESERCIZIO");
		sql.addSQLJoin("V_UNITA_ORGANIZZATIVA_VALIDA.CD_UNITA_ORGANIZZATIVA","A.CD_ROOT");

		sql.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "B");
		sql.addSQLJoin("A.ESERCIZIO","B.ESERCIZIO");
		sql.addSQLJoin("A.CD_CDS","B.CD_CDS");
		sql.addSQLClause(FindClause.AND,"B.CD_TIPO_LIVELLO", SQLBuilder.EQUALS, V_struttura_organizzativaHome.LIVELLO_CDR);
		sql.addSQLClause(FindClause.AND,"B.FL_CDR_UO", SQLBuilder.EQUALS, "Y");
		sql.addSQLClause(FindClause.AND,"B.FL_UO_CDS", SQLBuilder.EQUALS, "Y");

		sql.addTableToHeader("PDG_ESERCIZIO");
		sql.addSQLJoin("PDG_ESERCIZIO.ESERCIZIO","V_UNITA_ORGANIZZATIVA_VALIDA.ESERCIZIO");
		sql.addSQLJoin("PDG_ESERCIZIO.ESERCIZIO","B.ESERCIZIO");
		sql.addSQLJoin("PDG_ESERCIZIO.CD_CENTRO_RESPONSABILITA","B.CD_ROOT");

		sql.openParenthesis(FindClause.AND);
		sql.addSQLClause(FindClause.AND,"PDG_ESERCIZIO.STATO", SQLBuilder.EQUALS, Pdg_esercizioBulk.STATO_APERTURA_CDR);
		sql.addSQLClause(FindClause.OR,"PDG_ESERCIZIO.STATO", SQLBuilder.EQUALS, Pdg_esercizioBulk.STATO_ESAMINATO_CDR);
		sql.closeParenthesis();
		
		BulkHome homeAss = getHome(userContext,Ass_cdp_laBulk.class);
		SQLBuilder sqlAss = homeAss.createSQLBuilder();
		sqlAss.resetColumns();
		sqlAss.addColumn("1");

		sqlAss.addSQLJoin("ASS_CDP_LA.ESERCIZIO","V_UNITA_ORGANIZZATIVA_VALIDA.ESERCIZIO");

		sqlAss.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "C");
		sqlAss.addSQLJoin("ASS_CDP_LA.ESERCIZIO","C.ESERCIZIO");
		sqlAss.addSQLJoin("ASS_CDP_LA.CD_CENTRO_RESPONSABILITA","C.CD_ROOT");
		sqlAss.addSQLJoin("C.CD_CDS","V_UNITA_ORGANIZZATIVA_VALIDA.CD_UNITA_PADRE");
		sqlAss.addSQLClause(FindClause.AND,"V_UNITA_ORGANIZZATIVA_VALIDA.CD_TIPO_UNITA", SQLBuilder.NOT_EQUALS,Tipo_unita_organizzativaHome.TIPO_UO_SAC);		

		sqlAss.openParenthesis(FindClause.AND);
		sqlAss.addSQLClause(FindClause.AND,"ASS_CDP_LA.STATO", SQLBuilder.EQUALS, Ass_cdp_laBulk.STATO_SCARICATO);
		sqlAss.addSQLClause(FindClause.OR,"ASS_CDP_LA.STATO", SQLBuilder.EQUALS, Ass_cdp_laBulk.STATO_SCARICATO_PDGP);
		sqlAss.closeParenthesis();
		
		sql.addSQLNotExistsClause(FindClause.AND, sqlAss);
		SQLBuilder sqlAssSac = homeAss.createSQLBuilder();
		sqlAssSac.resetColumns();
		sqlAssSac.addColumn("1");

		sqlAssSac.addSQLJoin("ASS_CDP_LA.ESERCIZIO","V_UNITA_ORGANIZZATIVA_VALIDA.ESERCIZIO");

		sqlAssSac.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "C");
		sqlAssSac.addSQLJoin("ASS_CDP_LA.ESERCIZIO","C.ESERCIZIO");
		sqlAssSac.addSQLJoin("ASS_CDP_LA.CD_CENTRO_RESPONSABILITA","C.CD_ROOT");
		sqlAssSac.addSQLJoin("C.CD_UNITA_ORGANIZZATIVA","V_UNITA_ORGANIZZATIVA_VALIDA.CD_UNITA_ORGANIZZATIVA");
		sqlAssSac.addSQLClause(FindClause.AND,"V_UNITA_ORGANIZZATIVA_VALIDA.CD_TIPO_UNITA", SQLBuilder.EQUALS,Tipo_unita_organizzativaHome.TIPO_UO_SAC);		

		sqlAssSac.openParenthesis(FindClause.AND);
		sqlAssSac.addSQLClause(FindClause.AND,"ASS_CDP_LA.STATO", SQLBuilder.EQUALS, Ass_cdp_laBulk.STATO_SCARICATO);
		sqlAssSac.addSQLClause(FindClause.OR,"ASS_CDP_LA.STATO", SQLBuilder.EQUALS, Ass_cdp_laBulk.STATO_SCARICATO_PDGP);
		sqlAssSac.closeParenthesis();
		sql.addSQLNotExistsClause(FindClause.AND, sqlAssSac);

	} else {
		BulkHome homeAss = getHome(userContext,Ass_cdp_laBulk.class);
		SQLBuilder sqlAss = homeAss.createSQLBuilder();
		sqlAss.resetColumns();
		sqlAss.addColumn("1");

		sqlAss.addSQLJoin("ASS_CDP_LA.ESERCIZIO","V_UNITA_ORGANIZZATIVA_VALIDA.ESERCIZIO");
		sqlAss.addClause(FindClause.AND, "mese", SQLBuilder.EQUALS, costiDipendente.getMese());

		sqlAss.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "C");
		sqlAss.addSQLJoin("ASS_CDP_LA.ESERCIZIO","C.ESERCIZIO");
		sqlAss.addSQLJoin("ASS_CDP_LA.CD_CENTRO_RESPONSABILITA","C.CD_ROOT");
		sqlAss.addSQLJoin("C.CD_CDS","V_UNITA_ORGANIZZATIVA_VALIDA.CD_UNITA_PADRE");
		sqlAss.addSQLClause(FindClause.AND,"V_UNITA_ORGANIZZATIVA_VALIDA.CD_TIPO_UNITA", SQLBuilder.NOT_EQUALS,Tipo_unita_organizzativaHome.TIPO_UO_SAC);

		sqlAss.openParenthesis(FindClause.AND);
		sqlAss.addSQLClause(FindClause.AND,"ASS_CDP_LA.STATO", SQLBuilder.EQUALS, Ass_cdp_laBulk.STATO_SCARICATO_DEFINITIVO);
		sqlAss.closeParenthesis();
		
		sql.addSQLNotExistsClause(FindClause.AND, sqlAss);
		
		SQLBuilder sqlAssSac = homeAss.createSQLBuilder();
		sqlAssSac.resetColumns();
		sqlAssSac.addColumn("1");

		sqlAssSac.addSQLJoin("ASS_CDP_LA.ESERCIZIO","V_UNITA_ORGANIZZATIVA_VALIDA.ESERCIZIO");

		sqlAssSac.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "C");
		sqlAssSac.addSQLJoin("ASS_CDP_LA.ESERCIZIO","C.ESERCIZIO");
		sqlAssSac.addSQLJoin("ASS_CDP_LA.CD_CENTRO_RESPONSABILITA","C.CD_ROOT");
		sqlAssSac.addSQLJoin("C.CD_UNITA_ORGANIZZATIVA","V_UNITA_ORGANIZZATIVA_VALIDA.CD_UNITA_ORGANIZZATIVA");
		sqlAssSac.addSQLClause(FindClause.AND,"V_UNITA_ORGANIZZATIVA_VALIDA.CD_TIPO_UNITA", SQLBuilder.EQUALS,Tipo_unita_organizzativaHome.TIPO_UO_SAC);		

		sqlAssSac.openParenthesis(FindClause.AND);
		sqlAssSac.addSQLClause(FindClause.AND,"ASS_CDP_LA.STATO", SQLBuilder.EQUALS, Ass_cdp_laBulk.STATO_SCARICATO_DEFINITIVO);
		sqlAssSac.closeParenthesis();
		sql.addSQLNotExistsClause(FindClause.AND, sqlAssSac);

		BulkHome homeCoan = getHome(userContext,Stipendi_coanBulk.class);
		SQLBuilder sqlCoan = homeCoan.createSQLBuilder();
		sqlCoan.resetColumns();
		sqlCoan.addColumn("1");

		sqlCoan.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		sqlCoan.addSQLJoin("STIPENDI_COAN.CD_UO","V_UNITA_ORGANIZZATIVA_VALIDA.CD_UNITA_ORGANIZZATIVA");
		sqlCoan.addClause(FindClause.AND,"pg_scrittura_an",SQLBuilder.ISNOTNULL,null);
		
		sql.addSQLNotExistsClause(FindClause.AND, sqlCoan);
		//sql.addSQLClause("AND","NOT EXISTS ( SELECT 1 FROM "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+"STIPENDI_COAN WHERE STIPENDI_COAN.PG_SCRITTURA_AN IS NOT NULL AND STIPENDI_COAN.ESERCIZIO = UNITA_ORGANIZZATIVA.ESERCIZIO AND STIPENDI_COAN.CD_UO = UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA )");
	}

	if (clauses!=null)
		sql.addClause(clauses);
	return sql;
}
/**
 * verifica se il cdr è di tipo SAC,
 * può essere utilizzato anche in altri contesti
 * 
 * @param userContext
 * @return
 * @throws ComponentException
 */
private boolean isCdrSAC(UserContext userContext, CdrBulk cdr) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
	if (cdr==null)
		return false;
	// cerchiamo l'uo legata al cdr per capirne il tipo
	Unita_organizzativaBulk aUO = (Unita_organizzativaBulk)getHome(userContext,Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdr.getCd_unita_organizzativa()));

	if (aUO.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC))
		return true;
	else
		return false;		 	
}
public SQLBuilder selectStipendiObbByClause(UserContext userContext, Stipendi_cofi_obb_scadBulk dett,  Stipendi_cofi_obb_scadBulk bulkClass, CompoundFindClause clauses)  throws ComponentException 
{
	//if (dett == null)
	//	return null;
	 SQLBuilder sql;
	 sql = getHome(userContext,Stipendi_cofi_obb_scadBulk.class).createSQLBuilder();
	
	 sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	 //sql.addSQLClause("AND", "MESE", sql.EQUALS, dett.getMese());
	 return sql;
}
/** 
 *  Metodo che ritorna se è possibile o meno effettuare, per il mese in oggetto, altre ripartizioni dei costi per il 
 *  CDR di scrivania
 *    PreCondition:
 *      Viene richiesto se è possibile effettuare altre ripartizioni di costi
 *    PostCondition:
 *      Viene restituita un boolean
*/
public boolean isCostiDipendenteDefinitivi (UserContext userContext, int mese)  throws ComponentException {
	return isCostiDipendenteDefinitivi(userContext, mese, getCdrPdgP(userContext)); 		
}
/** 
 *  Metodo che ritorna se è possibile o meno effettuare, per il mese in oggetto, altre ripartizioni dei costi per il 
 *  CDR indicato
 *    PreCondition:
 *      Viene richiesto se è possibile effettuare altre ripartizioni di costi per il CDR <CdrBulk>
 *    PostCondition:
 *      Viene restituita un boolean
*/
public boolean isCostiDipendenteDefinitivi (UserContext userContext, int mese, CdrBulk cdr)  throws ComponentException {
	try {
		SQLBuilder sql = getHome(userContext, Ass_cdp_laBulk.class).createSQLBuilder();
			
		// se il CdR è della SAC deve esser controllato direttamente
		// altrimenti si vede l'afferenza
//		if (isCdrSAC(userContext, cdr)) {
			sql.addSQLClause(FindClause.AND,"ASS_CDP_LA.ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
			sql.addSQLClause(FindClause.AND,"ASS_CDP_LA.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,cdr.getCd_centro_responsabilita());
//		}
//		else {
//			sql.addToHeader("V_STRUTTURA_ORGANIZZATIVA");
//			sql.addSQLClause(FindClause.AND,"ASS_CDP_LA.ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
//			sql.addSQLJoin("ASS_CDP_LA.ESERCIZIO", "V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO");
//			sql.addSQLJoin("ASS_CDP_LA.CD_CENTRO_RESPONSABILITA", "V_STRUTTURA_ORGANIZZATIVA.CD_ROOT");
//			sql.addSQLClause(FindClause.AND,"V_STRUTTURA_ORGANIZZATIVA.CD_CDR_AFFERENZA",SQLBuilder.EQUALS,cdr.getCd_centro_responsabilita());
//		}

		// controlliamo prima che esistano dati per cui effettuare la ripartizione
		List result = getHome( userContext, Ass_cdp_laBulk.class ).fetchAll( sql );
		if ( result.isEmpty() )
			return false;

		sql.addClause(FindClause.AND,"mese", SQLBuilder.EQUALS, mese);
		sql.addClause(FindClause.AND,"stato", SQLBuilder.EQUALS, Ass_cdp_laBulk.STATO_SCARICATO_DEFINITIVO);
	
		result = getHome( userContext, Ass_cdp_laBulk.class ).fetchAll( sql );
		if ( result.size() > 0 )
			return true;
		return false;	
	}
	catch( Exception e )
	{
		throw handleException( e );
	}
}

/** 
 *  Metodo che ritorna se sono stati caricati i costi del dipendente da ripartire per almeno una UO
 *  appartenente al CDS del CDR indicato come parametro 
 *    PreCondition:
 *      Viene richiesto se sono stati caricati record nella tabella COSTO_DEL_DIPENDENTE per almeno una 
 *      Uo appartenente al CDS del CDR <CdrBulk> indicato come parametro 
 *    PostCondition:
 *      Viene restituita un boolean
*/
public boolean isCostiDipendenteDefinitivi (UserContext userContext, int mese, String cd_unita_organizzativa)  throws ComponentException {
	try {
		if (cd_unita_organizzativa==null) return false;
	
		CdrHome cdrHome = (CdrHome)getHome(userContext,CdrBulk.class,"V_CDR_VALIDO");
		SQLBuilder sqlCdr = cdrHome.createSQLBuilderEsteso();
		sqlCdr.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
		sqlCdr.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, cd_unita_organizzativa);
		sqlCdr.addClause("AND", "cd_proprio_cdr", SQLBuilder.EQUALS, "000");

		List result = getHome( userContext, CdrBulk.class ).fetchAll( sqlCdr );
		if ( result.size() > 1 || result.size() == 0)
			throw new ApplicationException("Impossibile determinare il CdR legato alla U.O. " + cd_unita_organizzativa + ".");
	
		CdrBulk cdrUo = (CdrBulk) result.get(0);	
	
		return isCostiDipendenteDefinitivi(userContext, mese, cdrUo);
	}
	catch( Exception e )
	{
		throw handleException( e );
	}
}

/** 
 *  Metodo che ritorna se è possibile o meno effettuare altre ripartizioni dei costi per il 
 *  CDR e il Modulo indicati
 *    PreCondition:
 *      Viene richiesto se è possibile effettuare altre ripartizioni di costi per il CDR <CdrBulk>, Modulo <ProgettoBulk>
 *    PostCondition:
 *      Viene restituita un boolean
*/
public boolean isCostiDipendenteDefinitivi (UserContext userContext, int mese, CdrBulk cdr, Progetto_sipBulk modulo)  throws ComponentException {
	try {
		SQLBuilder sql = getHome(userContext, Ass_cdp_laBulk.class).createSQLBuilder();
			
		// se il CdR è della SAC deve esser controllato direttamente
		// altrimenti si vede l'afferenza
//		if (isCdrSAC(userContext, cdr)) {
			sql.addToHeader("LINEA_ATTIVITA");
			sql.addSQLClause(FindClause.AND,"LINEA_ATTIVITA.PG_PROGETTO",SQLBuilder.EQUALS,modulo.getPg_progetto());
			sql.addSQLJoin("ASS_CDP_LA.CD_CENTRO_RESPONSABILITA", "LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA");
			sql.addSQLJoin("ASS_CDP_LA.CD_LINEA_ATTIVITA", "LINEA_ATTIVITA.CD_LINEA_ATTIVITA");		
			sql.addSQLClause(FindClause.AND,"ASS_CDP_LA.ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
			sql.addSQLClause(FindClause.AND,"ASS_CDP_LA.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,cdr.getCd_centro_responsabilita());
//		}
//		else {
//			sql.addToHeader("V_STRUTTURA_ORGANIZZATIVA");
//			sql.addToHeader("LINEA_ATTIVITA");
//			sql.addSQLClause(FindClause.AND,"LINEA_ATTIVITA.PG_PROGETTO",SQLBuilder.EQUALS,modulo.getPg_progetto());
//			sql.addSQLJoin("ASS_CDP_LA.CD_CENTRO_RESPONSABILITA", "LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA");
//			sql.addSQLJoin("ASS_CDP_LA.CD_LINEA_ATTIVITA", "LINEA_ATTIVITA.CD_LINEA_ATTIVITA");		
//			sql.addSQLClause(FindClause.AND,"ASS_CDP_LA.ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
//			sql.addSQLJoin("ASS_CDP_LA.ESERCIZIO", "V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO");
//			sql.addSQLJoin("ASS_CDP_LA.CD_CENTRO_RESPONSABILITA", "V_STRUTTURA_ORGANIZZATIVA.CD_ROOT");
//			sql.addSQLClause(FindClause.AND,"V_STRUTTURA_ORGANIZZATIVA.CD_CDR_AFFERENZA",SQLBuilder.EQUALS,cdr.getCd_centro_responsabilita());
//		}
		
		// controlliamo prima che esistano dati per cui effettuare la ripartizione
		List result = getHome( userContext, Ass_cdp_laBulk.class ).fetchAll( sql );
		if ( result.isEmpty() )
			return true;

		sql.addClause(FindClause.AND,"mese", SQLBuilder.EQUALS, mese);
		sql.addClause(FindClause.AND,"stato", SQLBuilder.EQUALS, Ass_cdp_laBulk.STATO_SCARICATO_DEFINITIVO);
	
		result = getHome( userContext, Ass_cdp_laBulk.class ).fetchAll( sql );
		if ( result.size() > 0 )
			return true;
		return false;	
	}
	catch( Exception e )
	{
		throw handleException( e );
	}
}
public OggettoBulk salvaDefinitivoCosti_dipendente(UserContext userContext,Costi_dipendenteVBulk costi_dipendente) throws ComponentException {
	try {
		if (isEsercizioChiuso(userContext))
			throw new ApplicationException("Operazione non consentita ad esercizio chiuso.");
		
		for (Iterator<V_cdp_matricolaBulk> iterator = costi_dipendente.getCosti_dipendenti().iterator(); iterator.hasNext();) {
			V_cdp_matricolaBulk cdp = iterator.next();
	
			if (cdp.getMese()==0)
				throw new ApplicationException("Operazione non consentita per i dati previsionali.");
			
			if (cdp.getIm_a1().compareTo(BigDecimal.ZERO)==1) {
				BigDecimal totPerc=BigDecimal.ZERO;
				BigDecimal totStatoIniziale=BigDecimal.ZERO;
				if (cdp.getCostiScaricati()!=null) {
					for (Iterator<Ass_cdp_laBulk> iterator2 = cdp.getCostiScaricati().iterator(); iterator2.hasNext();) {
						Ass_cdp_laBulk assCdpLa = iterator2.next();
						assCdpLa.setStato(Ass_cdp_laBulk.STATO_SCARICATO_DEFINITIVO);
						assCdpLa.setToBeUpdated();
						updateBulk(userContext, assCdpLa);
						totPerc=totPerc.add(assCdpLa.getPrc_la_a1());
					}
				}
				if (cdp.getCostiScaricatiAltraUO()!=null) {
					for (Iterator<Ass_cdp_uoBulk> iterator2 = cdp.getCostiScaricatiAltraUO().iterator(); iterator2.hasNext();) {
						Ass_cdp_uoBulk assCdpUo = iterator2.next();
						if (!assCdpUo.getStato().equals(Ass_cdp_uoBulk.STATO_NON_ACCETTATO))
							totPerc=totPerc.add(assCdpUo.getPrc_uo_a1());
						if (assCdpUo.getStato().equals(Ass_cdp_uoBulk.STATO_INIZIALE))
							totStatoIniziale=totStatoIniziale.add(assCdpUo.getPrc_uo_a1());
					}
				}
				if (totPerc.compareTo(BigDecimal.TEN.multiply(BigDecimal.TEN))!=0)
					throw new ApplicationException("Operazione non consentita! La ripartizione della matricola " + cdp.getId_matricola() + " non è completa.");
				if (totStatoIniziale.compareTo(BigDecimal.ZERO)!=0)
					throw new ApplicationException("Operazione non consentita! La matricola " + cdp.getId_matricola() + " non è stata ancora accettata dalla UO alla quale è stata assegnata.");
			} else {
				if (!cdp.getCostiScaricati().isEmpty() || !cdp.getCostiScaricatiAltraUO().isEmpty())
					throw new ApplicationException("Operazione non consentita! Esiste una ripartizione per la matricola " + cdp.getId_matricola() + " anche se il suo costo è nullo.");
			}

		}
		return costi_dipendente;
	} catch (PersistencyException e) {
		throw handleException( e );
	} catch (ComponentException e) {
		throw handleException( e );
	}		
}
public OggettoBulk annullaDefinitivoCosti_dipendente(UserContext userContext,Costi_dipendenteVBulk costi_dipendente) throws ComponentException {
	try {
		if (isEsercizioChiuso(userContext))
			throw new ApplicationException("Operazione non consentita ad esercizio chiuso.");
		
		for (Iterator<V_cdp_matricolaBulk> iterator = costi_dipendente.getCosti_dipendenti().iterator(); iterator.hasNext();) {
			V_cdp_matricolaBulk cdp = iterator.next();
	
			if (cdp.getMese()==0)
				throw new ApplicationException("Operazione non consentita per i dati previsionali.");
			
			if (cdp.getCostiScaricati()!=null) {
				for (Iterator<Ass_cdp_laBulk> iterator2 = cdp.getCostiScaricati().iterator(); iterator2.hasNext();) {
					Ass_cdp_laBulk assCdpLa = iterator2.next();
					assCdpLa.setStato(Ass_cdp_laBulk.STATO_SCARICATO_PROVVISORIO);
					assCdpLa.setToBeUpdated();
					updateBulk(userContext, assCdpLa);
				}
			}
		}
		return costi_dipendente;
	} catch (PersistencyException e) {
		throw handleException( e );
	} catch (ComponentException e) {
		throw handleException( e );
	}		
}
public OggettoBulk caricaCosti_dipendente(UserContext userContext,int mese) throws it.cnr.jada.comp.ComponentException {
	try {
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext)));
		return caricaCosti_dipendente(userContext,uo,mese);
	} catch (PersistencyException e) {
		throw handleException( e );
	}
}
public OggettoBulk caricaCosti_dipendente(UserContext userContext,Unita_organizzativaBulk uo,int mese) throws it.cnr.jada.comp.ComponentException {
	try {
		Costi_dipendenteVBulk costi_dipendente = new Costi_dipendenteVBulk();
	
		costi_dipendente.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		costi_dipendente.setMese(mese);
	
		// Imposto il numero di giorni lavorativi
		Configurazione_cnrBulk config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, costi_dipendente.getEsercizio(), "*", Configurazione_cnrBulk.PK_COSTANTI, Configurazione_cnrBulk.SK_TOTALE_GIORNI_LAVORATIVI_COSTI_PERSONALE);
		java.math.BigDecimal giorni_lavorativi = config.getIm01();
	
		if(giorni_lavorativi == null)
			 throw new ApplicationException("Totale giorni lavorativi non trovato in CONFIGURAZIONE CNR!");

		costi_dipendente.setGiorni_lavorativi(giorni_lavorativi);
		costi_dipendente.setUnita_organizzativa_filter(uo);
	
		if (costi_dipendente.getUnita_organizzativa_filter() != null &&
			costi_dipendente.getUnita_organizzativa_filter().getCd_unita_organizzativa() != null) {
			// Estraggo tutti i V_cdp_matricolaBulk che competono all'utente della UO selezionata
			BulkHome home = getHome(userContext,V_cdp_matricolaBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,costi_dipendente.getEsercizio());
			sql.addClause(FindClause.AND,"cd_uo_carico",SQLBuilder.EQUALS,costi_dipendente.getUnita_organizzativa_filter().getCd_unita_organizzativa());
			sql.addClause(FindClause.AND,"mese",SQLBuilder.EQUALS,costi_dipendente.getMese());
	
			costi_dipendente.setCosti_dipendenti(new BulkList(home.fetchAll(sql)));
			
			ROWrapper roWrapper = null;
			for (Iterator iterator = costi_dipendente.getCosti_dipendenti().iterator(); iterator.hasNext();) {
				V_cdp_matricolaBulk cdp = (V_cdp_matricolaBulk) iterator.next();
				OggettoBulk bulk = (OggettoBulk)caricaCosto_dipendente(userContext, costi_dipendente, cdp);
				if (bulk instanceof ROWrapper)
					roWrapper = (ROWrapper)bulk; 
			}
			if (roWrapper != null) return roWrapper;
		}

		return costi_dipendente; 
	} catch (PersistencyException e) {
		throw handleException( e );
	} catch (RemoteException e) {
		throw handleException( e );
	}
}

/** 
 *  Metodo che ritorna se sono stati caricati i costi del dipendente da ripartire per almeno una UO
 *  appartenente al CDS del CDR indicato come parametro 
 *    PreCondition:
 *      Viene richiesto se sono stati caricati record nella tabella COSTO_DEL_DIPENDENTE per almeno una 
 *      Uo appartenente al CDS del CDR <CdrBulk> indicato come parametro 
 *    PostCondition:
 *      Viene restituita un boolean
*/
public boolean isCostiDipendenteRipartiti (UserContext userContext, String cd_unita_organizzativa)  throws ComponentException {
	try {
		if (cd_unita_organizzativa==null) return false;
	
		CdrHome cdrHome = (CdrHome)getHome(userContext,CdrBulk.class,"V_CDR_VALIDO");
		SQLBuilder sqlCdr = cdrHome.createSQLBuilderEsteso();
		sqlCdr.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
		sqlCdr.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, cd_unita_organizzativa);
		sqlCdr.addClause("AND", "cd_proprio_cdr", SQLBuilder.EQUALS, "000");
	
		List result = getHome( userContext, CdrBulk.class ).fetchAll( sqlCdr );
		if ( result.size() > 1 || result.size() == 0)
			throw new ApplicationException("Impossibile determinare il CdR legato alla U.O. " + cd_unita_organizzativa + ".");
	
		CdrBulk cdrUo = (CdrBulk) result.get(0);	
	
		return isCostiDipendenteRipartiti(userContext, getCdrPdgP(userContext, cdrUo));
	}
	catch( Exception e )
	{
		throw handleException( e );
	}
}
}

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

package it.cnr.contab.doccont00.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Iterator;

import javax.ejb.EJBException;

import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voceBulk;
import it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession;
import it.cnr.contab.pdg01.bp.SelezionatoreAssestatoBP;
import it.cnr.contab.prevent00.bulk.V_assestatoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;

/**
* @author rpagano
*
* To change the template for this generated type comment go to
* Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
*/
public class SelezionatoreAssestatoDocContBP extends SelezionatoreAssestatoBP{
	public SelezionatoreAssestatoDocContBP() 
	{
		super();
	}
	
	public SelezionatoreAssestatoDocContBP( String function ) 
	{
		super(function);
	}

	public SelezionatoreAssestatoDocContBP( String function , OggettoBulk bulk, BigDecimal importoDaRipartire, String tipoGestione) 
	{
		super(function, bulk, importoDaRipartire, tipoGestione);
	}

	/**
	 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
	 */
	public CRUDComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (ObbligazioneComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ObbligazioneComponentSession",ObbligazioneComponentSession.class);
	}

	/** 
	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>OggettoBulk</code>
	 *
	 * @exception <code>BusinessProcessException</code>
	 */
	public OggettoBulk createEmptyModelForFreeSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			return createComponentSession().inizializzaBulkPerRicercaLibera(context.getUserContext(),createModel( context));
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	/**
	 *  Il metodo aggiunge alla normale toolbar del CRUD i bottoni per gestire le obbligazioni.
	 */
	public it.cnr.jada.util.jsp.Button[] createToolbar() 
	{
		Button[] toolbar = super.createToolbar();
		Button[] newToolbar = new Button[ toolbar.length + 2];
		int i;
		for ( i = 0; i < toolbar.length; i++ )
			newToolbar[i] = toolbar[i];

		newToolbar[ i++ ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.assegnaPercBilancio");
		newToolbar[ i++ ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.assegnaPercPdg");

		return newToolbar;
	}

	public void caricaSelezioniEffettuate(ActionContext actioncontext, ObbligazioneBulk obbligazione) throws it.cnr.jada.action.BusinessProcessException{
		try{
			PrimaryKeyHashtable hashRipartizione = obbligazione.getRipartizioneCdrVoceLinea();
			Obbligazione_scad_voceBulk key, oldKey;

			// recupero le percentuali di imputazione finanziaria per le linee di attivita da pdg
			// 100 - percentuali specificate x linee att non da PDG
			PrimaryKeyHashtable oldHashRipartizione = ((ObbligazioneComponentSession)createComponentSession()).getOldRipartizioneCdrVoceLinea(actioncontext.getUserContext(), obbligazione); 

			for ( Enumeration e = hashRipartizione.keys(); e.hasMoreElements(); ) 
			{
				key = (Obbligazione_scad_voceBulk)e.nextElement();
				V_assestatoBulk assestato=null;
				
				CompoundFindClause clause = new CompoundFindClause();
				clause.addClause("AND", "esercizio", SQLBuilder.EQUALS, key.getEsercizio());
				clause.addClause("AND", "esercizio_res", SQLBuilder.EQUALS, key.getObbligazione_scadenzario().getObbligazione().getEsercizio_originale());
				clause.addClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, key.getCd_centro_responsabilita());
				clause.addClause("AND", "cd_linea_attivita", SQLBuilder.EQUALS, key.getCd_linea_attivita());
				clause.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, key.getTi_gestione());		
				clause.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, key.getTi_appartenenza());
				clause.addClause("AND", "cd_elemento_voce", SQLBuilder.EQUALS, key.getObbligazione_scadenzario().getObbligazione().getCd_elemento_voce());

				RemoteIterator ri = find(actioncontext,clause,new V_assestatoBulk());
				if (ri.countElements()==1) {
					assestato = (V_assestatoBulk)ri.nextElement();
				}
				EJBCommonServices.closeRemoteIterator(actioncontext, ri);
				if (assestato != null) {
					assestato.setImp_da_assegnare((BigDecimal) hashRipartizione.get( key ));
					for ( Enumeration old = oldHashRipartizione.keys(); old.hasMoreElements(); ) 
					{
						oldKey = (Obbligazione_scad_voceBulk)old.nextElement();
						if (oldKey.getCd_centro_responsabilita().equals( key.getCd_centro_responsabilita() ) &&
							oldKey.getCd_linea_attivita().equals( key.getCd_linea_attivita() ) &&
							oldKey.getCd_voce().equals( key.getCd_voce() )) 
						{
							assestato.setDb_imp_utilizzato( (BigDecimal)oldHashRipartizione.get( oldKey ) );
							break;
						}
					}
					getAssestatoReplacer().put(assestato,assestato);
				}
			}
			refreshList( actioncontext );

			// individuo e seleziono automaticamente le combinazioni CDR/VOCE/LINEA già utilizzati
			// nell'obbligazione 
			it.cnr.jada.util.action.Selection models = getSelection();
			V_assestatoBulk assestato;
			for (int i=0;i<getElementsCount();i++) {
				assestato = (V_assestatoBulk) getElementAt(actioncontext,i);
				if (Utility.nvl(assestato.getImp_da_assegnare()).compareTo(new BigDecimal(0))>0) {
					models.setSelected(i);
				}
			}
			setSelection(models);
			allineaPercentualiSuImporti(actioncontext);
		} catch (EJBException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		} catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
   
	public void impostaModalitaMappa(ActionContext actioncontext, String modalitaMappa) throws it.cnr.jada.action.BusinessProcessException {
		if (modalitaMappa.equals(this.MODALITA_INSERIMENTO_PERCENTUALI))
		{
			BigDecimal totaleSelVoci = new BigDecimal(0);
			for (Iterator s = getSelectedElements(actioncontext).iterator();s.hasNext();) 
			{
				V_assestatoBulk voceSel = (V_assestatoBulk) s.next();
				if (Utility.nvl(voceSel.getImp_da_assegnare()).compareTo(new BigDecimal(0))>0)
					totaleSelVoci = totaleSelVoci.add( Utility.nvl(voceSel.getImp_da_assegnare()) );
			}
			if (totaleSelVoci.compareTo(((ObbligazioneBulk)getBulkCaller()).getIm_obbligazione()) > 0)
				throw new BusinessProcessException("Per passare alla modalità di selezione percentuale il totale importo selezionato " 
												 + "non deve essere superiore all'importo dell'obbligazione.");
		}
		super.impostaModalitaMappa(actioncontext, modalitaMappa);
	}

	public void assegnaPercentualiBilancio(ActionContext actioncontext) throws BusinessProcessException {
		for (int i = 0; i<getElementsCount(); i++) {
			V_assestatoBulk bulk = (V_assestatoBulk)getElementAt(actioncontext, i);
			bulk.setPrc_da_assegnare(null);
			bulk.setImp_da_assegnare(null);
		}

		//trovo il totale assestato di tutte le selezioni effettuate
		BigDecimal totAssestato = new BigDecimal(0), totalePrcVoci = new BigDecimal(0);
		for (Iterator s = getSelectedElements(actioncontext).iterator();s.hasNext();)
			totAssestato = totAssestato.add(Utility.nvl(((V_assestatoBulk) s.next()).getAssestato_iniziale())); 
		
		for (Iterator s = getSelectedElements(actioncontext).iterator();s.hasNext();) { 
			V_assestatoBulk voceSel = (V_assestatoBulk) s.next();

			if (!s.hasNext())
			{
				voceSel.setPrc_da_assegnare( new BigDecimal(100).subtract(totalePrcVoci));
			}
			else
			{
				voceSel.setPrc_da_assegnare(Utility.nvl(voceSel.getAssestato_iniziale()).divide(totAssestato, 16, java.math.BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
				totalePrcVoci = totalePrcVoci.add( voceSel.getPrc_da_assegnare() );
			}
			getAssestatoReplacer().put(voceSel,voceSel);
		}

		allineaImportiSuPercentuali(actioncontext);
	}

	public boolean isAssegnaPercentualiBilancioButtonHidden() {
		return isModalitaConsultazione() || isModalitaInserimentoSemplice();
	}

	public boolean isAssegnaPercentualiPdgButtonHidden() {
		return isModalitaConsultazione() || isModalitaInserimentoSemplice();
	}

	public void assegnaPercentualiPdg(ActionContext actioncontext) throws BusinessProcessException {
		for (int i = 0; i<getElementsCount(); i++) {
			V_assestatoBulk bulk = (V_assestatoBulk)getElementAt(actioncontext, i);
			bulk.setPrc_da_assegnare(null);
			bulk.setImp_da_assegnare(null);
		}

		//trovo il totale assestato di tutte le selezioni effettuate
		BigDecimal totPdg = new BigDecimal(0), totalePrcVoci = new BigDecimal(0);
		for (Iterator s = getSelectedElements(actioncontext).iterator();s.hasNext();)
			totPdg = totPdg.add(Utility.nvl(((V_assestatoBulk) s.next()).getStanziamento_iniziale())); 
		
		if (totPdg.compareTo(Utility.ZERO) <= 0)
			throw new BusinessProcessException("Sono state selezionati dettagli senza stanziamenti iniziali "
											 + "nel Piano di Gestione. Impossibile determinare le percentuali "
											 + "da applicare.");

		for (Iterator s = getSelectedElements(actioncontext).iterator();s.hasNext();) { 
			V_assestatoBulk voceSel = (V_assestatoBulk) s.next();

			if (!s.hasNext())
			{
				voceSel.setPrc_da_assegnare( new BigDecimal(100).subtract(totalePrcVoci));
			}
			else
			{
				voceSel.setPrc_da_assegnare(Utility.nvl(voceSel.getStanziamento_iniziale()).divide(totPdg, 4, java.math.BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
				totalePrcVoci = totalePrcVoci.add( voceSel.getPrc_da_assegnare() );
			}
			getAssestatoReplacer().put(voceSel,voceSel);
		}

		allineaImportiSuPercentuali(actioncontext);
	}
	public Button[] createNavigatorToolbar()
	{
		Button abutton[] = new Button[4];
		int i = 0;
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Navigator.previousFrame");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Navigator.previous");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Navigator.next");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Navigator.nextFrame");
		return abutton;
	}
}

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

package it.cnr.contab.ordmag.richieste.comp;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.ordmag.anag00.LuogoConsegnaMagBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaHome;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
import it.cnr.contab.ordmag.ordini.comp.OrdineAcqComponent;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk;
import it.cnr.contab.ordmag.richieste.bulk.VRichiestaPerOrdiniBulk;
import it.cnr.contab.ordmag.richieste.bulk.VRichiestaPerOrdiniHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class GenerazioneOrdiniDaRichiesteComponent
	extends OrdineAcqComponent
	implements ICRUDMgr,Cloneable,Serializable {

	public final static String TIPO_TOTALE_COMPLETO = "C";
	public final static String TIPO_TOTALE_PARZIALE = "P";
	
    public  GenerazioneOrdiniDaRichiesteComponent()
    {

        /*Default constructor*/


    }
    
    public OrdineAcqBulk cercaRichieste(UserContext context, OrdineAcqBulk filtro)
    		throws ComponentException {

    		VRichiestaPerOrdiniHome home = (VRichiestaPerOrdiniHome)getHome(context, VRichiestaPerOrdiniBulk.class);
    		it.cnr.jada.persistency.sql.SQLBuilder sql = ricercaRichieste(context, filtro, home);

    		try {
				Collection richieste = home.fetchAll(sql);
				for (Iterator j = richieste.iterator(); j.hasNext();) {
					VRichiestaPerOrdiniBulk richiesta = (VRichiestaPerOrdiniBulk) j.next();
					filtro.addToRichiesteDaTrasformareInOrdineColl(richiesta);
				}
				return filtro;
			} catch (PersistencyException e) {
				throw new ComponentException(e);
			}
    	}

    private it.cnr.jada.persistency.sql.SQLBuilder ricercaRichieste(UserContext context,
    		OrdineAcqBulk filtro, VRichiestaPerOrdiniHome home) throws ApplicationException {
    	
    	it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();

    	if (filtro.getUnitaOperativaOrd() == null || filtro.getUnitaOperativaOrd().getCdUnitaOperativa() == null){
    		throw new it.cnr.jada.comp.ApplicationException("E' necessario valorizzare l'unità operativa.");    	
    	} else {
            sql.addSQLClause(FindClause.AND, "V_RICHIESTA_PER_ORDINI.CD_UNITA_OPERATIVA_DEST", SQLBuilder.EQUALS, filtro.getUnitaOperativaOrd().getCdUnitaOperativa());
    	}

    	return sql;
    } 

public RichiestaUopRigaBulk selezionaRichiestaPerOrdine (UserContext aUC,VRichiestaPerOrdiniBulk richiesta) throws ComponentException
{
	try
	{

		RichiestaUopRigaBulk richiestaRiga = (RichiestaUopRigaBulk) getHome( aUC, RichiestaUopRigaBulk.class).findByPrimaryKey( new RichiestaUopRigaBulk( richiesta.getCdCds(), richiesta.getCdUnitaOperativa(), richiesta.getEsercizio(), richiesta.getCdNumeratore(), richiesta.getNumero(), richiesta.getRiga() ));

		if ( richiestaRiga == null )
			throw new ApplicationException( "Richiesta non esistente" );

		lockBulk( aUC, richiestaRiga );
		if ( richiestaRiga.getStato().equals(RichiestaUopRigaBulk.STATO_ANNULLATO))
			throw new ApplicationException("La richiesta è stata annullata.");
		if ( richiestaRiga.getStato().equals(RichiestaUopRigaBulk.STATO_TRASFORMATA_ORDINE))
			throw new ApplicationException("La richiesta è già stata trasformata in ordine.");
		if ( !richiestaRiga.getRichiestaUop().getStato().equals(RichiestaUopBulk.STATO_INVIATA_ORDINE))
			throw new ApplicationException("La richiesta non è stata inviata in ordine");
		richiestaRiga.setStato(RichiestaUopRigaBulk.STATO_TRASFORMATA_ORDINE);
		richiestaRiga.setUser( aUC.getUser());
		updateBulk( aUC, richiestaRiga );
		return richiestaRiga;
	}
	catch ( Exception e )
	{
		throw handleException( richiesta, e )	;
	}
}
public OrdineAcqBulk generaOrdine(it.cnr.jada.UserContext userContext, OrdineAcqBulk ordine) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	List<VRichiestaPerOrdiniBulk> lista = ordine.getRichiesteDaTrasformareInOrdineColl();
	for (VRichiestaPerOrdiniBulk richiestaPerOrdini : lista){
		RichiestaUopRigaBulk rigaRichiesta = selezionaRichiestaPerOrdine(userContext, richiestaPerOrdini);
		creaRigaOrdine(userContext,ordine, rigaRichiesta);
	}
	for (Object riga : ordine.getRigheOrdineColl()){
		OrdineAcqRigaBulk rigaOrdine = (OrdineAcqRigaBulk)riga;
		impostaCampiDspRiga(userContext, rigaOrdine);
	}
	return ordine;
}
private void creaRigaOrdine(it.cnr.jada.UserContext userContext, OrdineAcqBulk ordine, RichiestaUopRigaBulk rigaRichiesta) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	boolean trovataRiga = false;
	OrdineAcqRigaBulk rigaOrdine = null;
	for (Object objectRiga : ordine.getRigheOrdineColl()){
		OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk)objectRiga;
		if (riga.getBeneServizio().equalsByPrimaryKey(rigaRichiesta.getBeneServizio())){
			rigaOrdine = riga;
		}
	}
	if (!trovataRiga){
		rigaOrdine = new OrdineAcqRigaBulk();
		rigaOrdine = (OrdineAcqRigaBulk)rigaOrdine.inizializzaPerInserimento(userContext);
		rigaOrdine.setBeneServizio(rigaRichiesta.getBeneServizio());
		rigaOrdine.setDsBeneServizio(rigaRichiesta.getDsBeneServizio());
		rigaOrdine.setCdBeneServizio(rigaRichiesta.getCdBeneServizio());
		Bene_servizioBulk bene;
		try {
			bene = recuperoBeneServizio(userContext, rigaRichiesta.getCdBeneServizio());
			if (bene != null){
				rigaOrdine.setVoceIva(bene.getVoce_iva());
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		rigaOrdine.setUnitaMisura(rigaRichiesta.getUnitaMisura());
		rigaOrdine.setCoefConv(rigaRichiesta.getCoefConv());
		rigaOrdine.setNotaRiga(rigaRichiesta.getNotaRiga());
		rigaOrdine.setOrdineAcq(ordine);
	}
	OrdineAcqConsegnaBulk consegna = new OrdineAcqConsegnaBulk();
	consegna.inizializzaConsegnaNuovaRiga();
	consegna.setTipoConsegna(Bene_servizioBulk.TIPO_CONSEGNA_TRANSITO);
	consegna.setUnitaOperativaOrd(rigaRichiesta.getRichiestaUop().getUnitaOperativaOrd());
	consegna.setQuantita(rigaRichiesta.getQuantitaAutorizzata());
	consegna.setDtPrevConsegna(rigaOrdine.getDspDtPrevConsegna());
	consegna.getRigheRichiestaCollegate().add(rigaRichiesta);
	rigaOrdine.addToRigheConsegnaColl(consegna);
	ordine.addToRigheOrdineColl(rigaOrdine);
}
}

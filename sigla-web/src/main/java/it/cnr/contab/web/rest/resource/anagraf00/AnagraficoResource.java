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

package it.cnr.contab.web.rest.resource.anagraf00;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBase;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TelefonoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.ejb.AnagraficoComponentSession;
import it.cnr.contab.anagraf00.ejb.TerzoComponentSession;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.util.CodiceFiscaleControllo;
import it.cnr.contab.config00.ejb.Unita_organizzativaComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.web.rest.exception.RestException;
import it.cnr.contab.web.rest.local.anagraf00.AnagraficoLocal;
import it.cnr.contab.web.rest.local.anagraf00.TerzoLocal;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.PersistencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import java.rmi.RemoteException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

@Stateless
public class AnagraficoResource implements AnagraficoLocal {
    private final Logger LOGGER = LoggerFactory.getLogger(AnagraficoResource.class);
	@Context SecurityContext securityContext;
	@EJB CRUDComponentSession crudComponentSession;
	@EJB TerzoComponentSession terzoComponentSession;
	@EJB AnagraficoComponentSession anagraficoComponentSession;
	@EJB Unita_organizzativaComponentSession unita_organizzativaComponentSession;
	
    public Response insert(@Context HttpServletRequest request, AnagraficoBulk anagraficoBulk) throws Exception {
    	CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
		Optional.ofNullable(anagraficoBulk.getCognome()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, indicare il cognome."));
		Optional.ofNullable(anagraficoBulk.getNome()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, indicare il nome."));
		Optional.ofNullable(anagraficoBulk.getDt_nascita()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, indicare la data di nascita."));
		Optional.ofNullable(anagraficoBulk.getPg_comune_nascita()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, indicare il comune di nascita."));
		Optional.ofNullable(anagraficoBulk.getPg_comune_fiscale()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, indicare il comune di residenza."));
		Optional.ofNullable(anagraficoBulk.getVia_fiscale()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, indicare l'indirizzo di residenza."));

  		anagraficoBulk = (AnagraficoBulk) anagraficoComponentSession.inizializzaBulkPerInserimento(userContext, anagraficoBulk);

		ComuneBulk comuneNascita = getComune(userContext, anagraficoBulk.getPg_comune_nascita());
		if (comuneNascita == null){
			throw  new RestException(Status.BAD_REQUEST, "Errore, comune di nascita non corretto.");
		} else {
			anagraficoBulk.setComune_nascita(comuneNascita);
		}
		ComuneBulk comuneResidenza = getComune(userContext, anagraficoBulk.getPg_comune_fiscale());
		if (comuneResidenza == null){
			throw  new RestException(Status.BAD_REQUEST, "Errore, comune di residenza non corretto.");
		} else {
			NazioneBulk nazioneResidenza = getNazione(userContext, comuneResidenza.getPg_nazione());
			if (nazioneResidenza != null){
				comuneResidenza.setNazione(nazioneResidenza);
			}
			anagraficoBulk.setComune_fiscale(comuneResidenza);
		}
		anagraficoBulk.setPg_nazione_fiscale(comuneResidenza.getPg_nazione());

		NazioneBulk nazioneNascita = getNazione(userContext, comuneNascita.getPg_nazione());
		if (nazioneNascita != null){
			anagraficoBulk.getComune_nascita().setNazione(nazioneNascita);
			anagraficoBulk.setNazionalita(nazioneNascita);
			anagraficoBulk.setPg_nazione_nazionalita(comuneNascita.getPg_nazione());
		}
		if (anagraficoBulk.getCodice_fiscale() == null){
			anagraficoBulk.setCodice_fiscale(getCodiceFiscale(anagraficoBulk));
		}
		anagraficoBulk.setToBeCreated();
		anagraficoBulk = (AnagraficoBulk) anagraficoComponentSession.creaConBulk(userContext, anagraficoBulk);

    	TerzoBulk terzoBulk = new TerzoBulk();
		terzoBulk = (TerzoBulk)terzoComponentSession.inizializzaBulkPerInserimento(userContext, terzoBulk);
		terzoBulk.setAnagrafico(anagraficoBulk);
		terzoBulk.setDenominazione_sede(anagraficoBulk.getCognome()+' '+anagraficoBulk.getNome());
		terzoBulk.setVia_sede(anagraficoBulk.getVia_fiscale());
		terzoBulk.setNumero_civico_sede(anagraficoBulk.getNum_civico_fiscale());
		terzoBulk.setComune_sede(anagraficoBulk.getComune_fiscale());
		terzoBulk.setCap_comune_sede(anagraficoBulk.getCap_comune_fiscale());

    	terzoBulk.setToBeCreated();
    	
    	terzoBulk = (TerzoBulk)terzoComponentSession.creaConBulk(userContext, terzoBulk);
    	return Response.status(Status.OK).entity(terzoBulk).build();
    }

	private ComuneBulk getComune(UserContext userContext, Long pg_comune) throws PersistencyException, ComponentException, RemoteException, EJBException {
		ComuneBulk comuneBulk = new ComuneBulk();
		comuneBulk.setPg_comune(pg_comune);
		comuneBulk = (ComuneBulk) crudComponentSession.findByPrimaryKey(userContext, comuneBulk);
		return comuneBulk;
	}

	private NazioneBulk getNazione(UserContext userContext, Long pg_nazione) throws PersistencyException, ComponentException, RemoteException, EJBException {
		NazioneBulk nazioneBulk = new NazioneBulk();
		nazioneBulk.setPg_nazione(pg_nazione);
		nazioneBulk = (NazioneBulk) crudComponentSession.findByPrimaryKey(userContext, nazioneBulk);
		return nazioneBulk;
	}
	private String getCodiceFiscale(AnagraficoBulk anagBulk) throws PersistencyException, ComponentException, RemoteException, EJBException {
		GregorianCalendar dataNascita = new GregorianCalendar();
		dataNascita.setTime(new java.util.Date(anagBulk.getDt_nascita().getTime()));
		String cd_catastale = null;
		if (anagBulk.getComune_nascita() != null) {
			if (NazioneBulk.ITALIA.equals(anagBulk.getComune_nascita().getTi_italiano_estero()))
				cd_catastale = anagBulk.getComune_nascita().getCd_catastale();
			else if (anagBulk.getComune_nascita().getNazione() != null)
				cd_catastale = anagBulk.getComune_nascita().getNazione().getCd_catastale();
		}

			return CodiceFiscaleControllo.makeCodiceFiscale(
					anagBulk.getCognome(),
					anagBulk.getNome(),
					"" + (dataNascita.get(GregorianCalendar.YEAR) % 100),
				"" + dataNascita.get(GregorianCalendar.MONTH),
				"" + dataNascita.get(GregorianCalendar.DAY_OF_MONTH),
				anagBulk.getTi_sesso(),
		cd_catastale);
	}

}
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

package it.cnr.contab.incarichi00.bp;

import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Incarichi_parametriBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.upload.UploadedFile;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 11:32:54 AM)
 * @author: Roberto Peli
 */
public class Incarichi_archivioCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
	private static final long LUNGHEZZA_MAX=0x1000000;
	private static final long LUNGHEZZA_MAX_PERLA=Long.valueOf(1000000);

	public Incarichi_archivioCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
		super(name, modelClass, listPropertyName, parent);
	}
	protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
		// TODO Auto-generated method stub
		Incarichi_archivioBulk allegato = (Incarichi_archivioBulk)oggettobulk;
		UploadedFile file = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter(getInputPrefix()+".blob");

		if ( allegato.getNome_file() == null && allegato.isFileRequired()) {
			if (file == null || file.getName().equals(""))
				throw new ValidationException("Attenzione: selezionare un File da caricare.");
		}

		if (!(file == null || file.getName().equals(""))) {
			if (file.length() > LUNGHEZZA_MAX)
				throw new ValidationException("Attenzione: la dimensione del file è superiore alla massima consentita (10 Mb).");

			if (allegato.isCurriculumVincitore()||allegato.isAggiornamentoCurriculumVincitore()||allegato.isConflittoInteressi()) {
				try {
					if (allegato instanceof Incarichi_repertorio_archivioBulk) {
						Incarichi_parametriBulk parametri = Utility.createIncarichiProceduraComponentSession().getIncarichiParametri(actioncontext.getUserContext(),
								(((Incarichi_repertorio_archivioBulk) allegato).getIncarichi_repertorio().getIncarichi_procedura()));
						if (parametri != null && parametri.getFl_invio_fp().equals("Y")) {
							if (file.length() > LUNGHEZZA_MAX_PERLA)
								throw new ValidationException("Attenzione: la dimensione del file è superiore alla massima consentita (1 Mb).");
						}
					}
				} catch (Exception e) {
				}
				if (!file.getContentType().equals("application/pdf"))
					throw new ValidationException("File non valido! Il formato del file consentito è il pdf.");
			}

			allegato.setFile(file.getFile());
			allegato.setNome_file(allegato.parseFilename(file.getName()));
			allegato.setContentType(file.getContentType());
			((OggettoBulk)allegato).setToBeUpdated();
			getParentController().setDirty(true);
		}
		
		if ( allegato.getUrl_file() == null && allegato.isUrlRequired())
			throw new ValidationException("Attenzione: indicare un indirizzo URL da cui reperire l'allegato.");

		super.validate(actioncontext, oggettobulk);
	}
	public void validateForDelete(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
		Incarichi_archivioBulk archivio = (Incarichi_archivioBulk)oggettobulk;
		if (archivio !=  null)
			if (!((OggettoBulk)archivio).isToBeCreated()) {
				if (archivio.getFaseProcesso() != null) {
					if (archivio.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_PUBBLICAZIONE)==1 &&
						archivio.isBando())
						throw new ValidationException("Eliminazione non possibile!\nLa procedura di conferimento incarico e' gia' stata pubblicata.");
					if (archivio.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INVIO_CORTE_CONTI)!=-1) {
						if (/*archivio.isContratto()||*/archivio.isDecisioneAContrattare()||archivio.isDecretoDiNomina()) {
							if (archivio.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INVIO_CORTE_CONTI)==0)
								throw new ValidationException("Eliminazione non possibile!\nGli atti sono gia' stata inviati alla Corte dei Conti per il controllo di legittimita'.");
							else
								throw new ValidationException("Eliminazione non possibile!\nLa procedura di conferimento incarico e' gia' stata resa definitiva.");
						}
					}
				}
			}
		super.validateForDelete(actioncontext, oggettobulk);
	}
	public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
		if (oggettobulk instanceof Incarichi_archivioBulk) {
			Incarichi_archivioBulk archivio = (Incarichi_archivioBulk)oggettobulk;
			if (!((OggettoBulk)archivio).isToBeCreated() && !((OggettoBulk)archivio).isToBeDeleted() &&
				((OggettoBulk)archivio).getCrudStatus()!=OggettoBulk.UNDEFINED) {
				archivio.setStato(Incarichi_archivioBulk.STATO_ANNULLATO);
				((OggettoBulk)archivio).setToBeUpdated();
				return (OggettoBulk)archivio;
			}
		}
		return super.removeDetail(oggettobulk, i);
	}
	public void addBando(ActionContext actioncontext) throws BusinessProcessException {
		add(actioncontext);
		((Incarichi_archivioBulk)getModel()).setTipo_archivio(Incarichi_archivioBulk.TIPO_BANDO);
	}		
	public void addDecisioneAContrattare(ActionContext actioncontext) throws BusinessProcessException {
		add(actioncontext);
		((Incarichi_archivioBulk)getModel()).setTipo_archivio(Incarichi_archivioBulk.TIPO_DECISIONE_A_CONTRATTARE);
	}		
	public void addContratto(ActionContext actioncontext) throws BusinessProcessException {
		add(actioncontext);
		((Incarichi_archivioBulk)getModel()).setTipo_archivio(Incarichi_archivioBulk.TIPO_CONTRATTO);
	}		
	public void addCurriculumVincitore(ActionContext actioncontext) throws BusinessProcessException {
		add(actioncontext);
		((Incarichi_archivioBulk)getModel()).setTipo_archivio(Incarichi_archivioBulk.TIPO_CURRICULUM_VINCITORE);
	}		
	public void addAggiornamentoCurriculumVincitore(ActionContext actioncontext) throws BusinessProcessException {
		add(actioncontext);
		((Incarichi_archivioBulk)getModel()).setTipo_archivio(Incarichi_archivioBulk.TIPO_AGGIORNAMENTO_CURRICULUM_VINCITORE);
	}
	public void addProgetto(ActionContext actioncontext) throws BusinessProcessException {
		add(actioncontext);
		((Incarichi_archivioBulk)getModel()).setTipo_archivio(Incarichi_archivioBulk.TIPO_PROGETTO);
	}		
	public void addAllegatoGenerico(ActionContext actioncontext) throws BusinessProcessException {
		add(actioncontext);
		((Incarichi_archivioBulk)getModel()).setTipo_archivio(Incarichi_archivioBulk.TIPO_GENERICO);
	}		
	public void addAllegatoContratto(ActionContext actioncontext) throws BusinessProcessException {
		add(actioncontext);
		((Incarichi_archivioBulk)getModel()).setTipo_archivio(Incarichi_archivioBulk.TIPO_ALLEGATO_CONTRATTO);
	}		
	public void addDecretoDiNomina(ActionContext actioncontext) throws BusinessProcessException {
		add(actioncontext);
		((Incarichi_archivioBulk)getModel()).setTipo_archivio(Incarichi_archivioBulk.TIPO_DECRETO_DI_NOMINA);
	}		
	public void addAttoEsitoControllo(ActionContext actioncontext) throws BusinessProcessException {
		add(actioncontext);
		((Incarichi_archivioBulk)getModel()).setTipo_archivio(Incarichi_archivioBulk.TIPO_ATTO_ESITO_CONTROLLO);
	}
	public void addConflittoInteresse(ActionContext actioncontext) throws BusinessProcessException {
		add(actioncontext);
		((Incarichi_archivioBulk)getModel()).setTipo_archivio(Incarichi_archivioBulk.TIPO_CONFLITTO_INTERESSI);
	}
	public void addAttestazioneDirettore(ActionContext actioncontext) throws BusinessProcessException {
		add(actioncontext);
		((Incarichi_archivioBulk)getModel()).setTipo_archivio(Incarichi_archivioBulk.TIPO_ATTESTAZIONE_DIRETTORE);
	}
}

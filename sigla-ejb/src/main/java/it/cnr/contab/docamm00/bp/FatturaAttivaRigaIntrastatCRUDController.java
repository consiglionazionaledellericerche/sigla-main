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

package it.cnr.contab.docamm00.bp;

import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk;
import it.cnr.contab.docamm00.intrastat.bulk.Fattura_attiva_intraBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 11:32:54 AM)
 *
 * @author: Roberto Peli
 */
public class FatturaAttivaRigaIntrastatCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
    /**
     * FatturaPassivaRigaCRUDController constructor comment.
     *
     * @param name             java.lang.String
     * @param modelClass       java.lang.Class
     * @param listPropertyName java.lang.String
     * @param parent           it.cnr.jada.util.action.FormController
     */
    public FatturaAttivaRigaIntrastatCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
        super(name, modelClass, listPropertyName, parent);
    }

    /**
     * Restituisce true se è possibile aggiungere nuovi elementi
     */
    public boolean isGrowable() {

        Fattura_attivaBulk fattura = (Fattura_attivaBulk) getParentModel();
        Boolean trovato = false;
        if (fattura.getFl_intra_ue() != null && fattura.getFl_intra_ue().booleanValue() &&
                fattura.getCliente() != null && fattura.getCliente().getAnagrafico() != null && fattura.getCliente().getAnagrafico().getPartita_iva() != null &&
                fattura.getFattura_attiva_dettColl() != null && !fattura.getFattura_attiva_dettColl().isEmpty()) {
            for (Iterator i = fattura.getFattura_attiva_dettColl().iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaBulk dettaglio = (Fattura_attiva_rigaBulk) i.next();
                if (dettaglio.getBene_servizio() != null && dettaglio.getBene_servizio().getFl_obb_intrastat_ven().booleanValue())
                    trovato = true;
            }
        }
        return (//super.isGrowable() &&
                !((it.cnr.jada.util.action.CRUDBP) getParentController()).isSearching() && trovato);
    }

    public boolean isInputReadonly() {

        return isReadonly() || isParentControllerReadonly();
    }

    public boolean isParentControllerReadonly() {

        CRUDFatturaAttivaBP parentC = (CRUDFatturaAttivaBP) getParentController();
        return (!parentC.isEditable() && !parentC.isSearching()) ||
                parentC.isDeleting() ||
                parentC.isModelVoided();
    }

    /**
     * Restituisce true se è possibile aggiungere nuovi elementi
     */
    public boolean isShrinkable() {
        Fattura_attivaBulk fattura = (Fattura_attivaBulk) getParentModel();
        return    //super.isShrinkable() &&
                !((it.cnr.jada.util.action.CRUDBP) getParentController()).isSearching() &&
                        fattura.getFattura_attiva_intrastatColl() != null &&
                        !fattura.getFattura_attiva_intrastatColl().isEmpty();
    }

    public void validate(ActionContext context, OggettoBulk model) throws ValidationException {

        Fattura_attiva_intraBulk fpi = (Fattura_attiva_intraBulk) model;
        String descr = (fpi.getDs_bene() == null) ? "selezionato" : fpi.getDs_bene();
        if (fpi.getAmmontare_euro() == null)
            throw new ValidationException("Specificare una ammontare in euro per il dettaglio " + descr + "!");

        if (fpi.getFattura_attiva() != null && fpi.getFattura_attiva().getTi_bene_servizio().compareTo(Bene_servizioBulk.BENE) == 0) {
            //if (fpi.getDs_bene() == null)
            //throw new ValidationException("Specificare una descrizione per il dettaglio intrastat!");

            //if (fpi.getAmmontare_divisa() == null)
            //throw new ValidationException("Specificare una ammontare in divisa per il dettaglio " + descr + "!");
            //if (fpi.getMassa_netta() == null)
            //throw new ValidationException("Specificare una massa netta per il dettaglio " + descr + "!");
            //if (fpi.getUnita_supplementari() == null )
            //throw new ValidationException("Specificare le unità supplementari per il dettaglio " + descr + "!");
            if (fpi.getValore_statistico() == null || (fpi.getValore_statistico().compareTo(java.math.BigDecimal.ZERO) == 0 && fpi.getAmmontare_euro().compareTo(java.math.BigDecimal.ZERO) != 0))
                throw new ValidationException("Specificare un valore statistico per il dettaglio " + descr + "!");
            if (fpi.getValore_statistico().compareTo(fpi.getAmmontare_euro()) > 0)
                throw new ValidationException("L'importo del valore statistico non può essere superiore all'ammontare in euro per il dettaglio " + descr + "!");
            if (fpi.getCondizione_consegna() == null || fpi.getCondizione_consegna().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una condizione di consegna per il dettaglio " + descr + "!");
            if (fpi.getModalita_trasporto() == null || fpi.getModalita_trasporto().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una modalità di trasporto per il dettaglio " + descr + "!");
            if (fpi.getNatura_transazione() == null || fpi.getNatura_transazione().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una natura transazione per il dettaglio " + descr + "!");
            if (fpi.getNomenclatura_combinata() == null || fpi.getNomenclatura_combinata().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una nomenclatura combinata per il dettaglio " + descr + "!");
            if (fpi.getNazione_destinazione() == null || fpi.getNazione_destinazione().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una nazione di destinazione per il dettaglio " + descr + "!");
            if (fpi.getProvincia_origine() == null || fpi.getProvincia_origine().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una provincia di origine per il dettaglio " + descr + "!");
            if (fpi.getNomenclatura_combinata() != null && fpi.getNomenclatura_combinata().getUnita_supplementari() != null && (fpi.getUnita_supplementari() == null || new BigDecimal(fpi.getUnita_supplementari()).compareTo(BigDecimal.ZERO) == 0))
                throw new ValidationException("Specificare le unità supplementari per il dettaglio " + descr + "!");
            if (fpi.getNomenclatura_combinata() != null && fpi.getNomenclatura_combinata().getUnita_supplementari() == null && (fpi.getMassa_netta() == null || fpi.getMassa_netta().compareTo(BigDecimal.ZERO) == 0))
                throw new ValidationException("Specificare una massa netta per il dettaglio " + descr + "!");

        } else {
            if (fpi.getModalita_incasso() == null || fpi.getModalita_incasso().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una modalita di incasso per il dettaglio " + descr + "!");
            if (fpi.getModalita_erogazione() == null || fpi.getModalita_erogazione().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una modalita di erogazione per il dettaglio " + descr + "!");
            if (fpi.getCodici_cpa() == null || fpi.getCodici_cpa().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare il codice servizio (cpa) per il dettaglio " + descr + "!");
            if (fpi.getNazione_destinazione() == null || fpi.getNazione_destinazione().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una nazione di pagamento per il dettaglio " + descr + "!");

        }

    }

    public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {

    }
}

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

import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.intrastat.bulk.Fattura_passiva_intraBulk;
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
public class FatturaPassivaRigaIntrastatCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
    /**
     * FatturaPassivaRigaCRUDController constructor comment.
     *
     * @param name             java.lang.String
     * @param modelClass       java.lang.Class
     * @param listPropertyName java.lang.String
     * @param parent           it.cnr.jada.util.action.FormController
     */
    public FatturaPassivaRigaIntrastatCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
        super(name, modelClass, listPropertyName, parent);
    }

    /**
     * Restituisce true se è possibile aggiungere nuovi elementi
     */
    public boolean isGrowable() {

        Fattura_passivaBulk fattura = (Fattura_passivaBulk) getParentModel();
        Boolean trovato = false;
        if (fattura.getFl_intra_ue() != null && fattura.getFl_intra_ue().booleanValue() &&
                fattura.getFornitore() != null && fattura.getFornitore().getAnagrafico() != null && fattura.getFornitore().getAnagrafico().getPartita_iva() != null &&
                fattura.getFattura_passiva_dettColl() != null && !fattura.getFattura_passiva_dettColl().isEmpty()) {
            for (Iterator i = fattura.getFattura_passiva_dettColl().iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk) i.next();
                if (dettaglio.getBene_servizio() != null && dettaglio.getBene_servizio().getFl_obb_intrastat_acq().booleanValue())
                    trovato = true;
            }
        }
        return (//super.isGrowable() &&
                !((it.cnr.jada.util.action.CRUDBP) getParentController()).isSearching() &&
                        trovato);
    }

    public boolean isInputReadonly() {

        return isReadonly() || isParentControllerReadonly();
    }

    public boolean isParentControllerReadonly() {

        CRUDFatturaPassivaBP parentC = (CRUDFatturaPassivaBP) getParentController();
        return (!parentC.isEditable() && !parentC.isSearching()) ||
                parentC.isDeleting() ||
                parentC.isModelVoided();
    }

    /**
     * Restituisce true se è possibile eliminare elementi
     */
    public boolean isShrinkable() {
        Fattura_passivaBulk fatturaP = (Fattura_passivaBulk) getParentModel();
        return    //super.isShrinkable() &&
                !((it.cnr.jada.util.action.CRUDBP) getParentController()).isSearching() &&
                        fatturaP.getFattura_passiva_intrastatColl() != null &&
                        !fatturaP.getFattura_passiva_intrastatColl().isEmpty();
    }

    public void validate(ActionContext context, OggettoBulk model) throws ValidationException {

        Fattura_passiva_intraBulk fpi = (Fattura_passiva_intraBulk) model;
        String descr = (fpi.getDs_bene() == null) ? "selezionato" : fpi.getDs_bene();
        if (fpi.getAmmontare_euro() == null)
            throw new ValidationException("Specificare un ammontare in euro per il dettaglio " + descr + "!");

        if ((fpi.getFattura_passiva() != null && ((Fattura_passivaBulk) fpi.getFattura_passiva()).getFornitore() != null &&
                ((Fattura_passivaBulk) fpi.getFattura_passiva()).getFornitore().getAnagrafico() != null &&
                (((Fattura_passivaBulk) fpi.getFattura_passiva()).getFornitore().getAnagrafico().getNazionalita() != null &&
                        ((Fattura_passivaBulk) fpi.getFattura_passiva()).getFornitore().getAnagrafico().getNazionalita().getDivisa() != null) ||
                (((Fattura_passivaBulk) fpi.getFattura_passiva()).getFornitore().getAnagrafico().getComune_fiscale() != null &&
                        ((Fattura_passivaBulk) fpi.getFattura_passiva()).getFornitore().getAnagrafico().getComune_fiscale().getNazione() != null &&
                        ((Fattura_passivaBulk) fpi.getFattura_passiva()).getFornitore().getAnagrafico().getComune_fiscale().getNazione().getDivisa() != null)) &&
                fpi.getAmmontare_euro().compareTo(BigDecimal.ZERO) != 0 && (fpi.getAmmontare_divisa() == null || fpi.getAmmontare_divisa().compareTo(BigDecimal.ZERO) == 0)) {
            throw new ValidationException("Specificare un ammontare in divisa per il dettaglio " + descr + "!");
        } else if ((fpi.getFattura_passiva() != null && ((Fattura_passivaBulk) fpi.getFattura_passiva()).getFornitore() != null &&
                ((Fattura_passivaBulk) fpi.getFattura_passiva()).getFornitore().getAnagrafico() != null &&
                (((Fattura_passivaBulk) fpi.getFattura_passiva()).getFornitore().getAnagrafico().getNazionalita() != null &&
                        ((Fattura_passivaBulk) fpi.getFattura_passiva()).getFornitore().getAnagrafico().getNazionalita().getDivisa() == null) ||
                (((Fattura_passivaBulk) fpi.getFattura_passiva()).getFornitore().getAnagrafico().getComune_fiscale() != null &&
                        ((Fattura_passivaBulk) fpi.getFattura_passiva()).getFornitore().getAnagrafico().getComune_fiscale().getNazione() != null &&
                        ((Fattura_passivaBulk) fpi.getFattura_passiva()).getFornitore().getAnagrafico().getComune_fiscale().getNazione().getDivisa() == null)) &&
                (fpi.getAmmontare_divisa() != null && fpi.getAmmontare_divisa().compareTo(BigDecimal.ZERO) != 0)) {
            throw new ValidationException("Non specificare un ammontare in divisa per il dettaglio " + descr + "!");
        }


        if (fpi.getFattura_passiva() != null && fpi.getFattura_passiva().getTi_bene_servizio().compareTo(Fattura_passivaBulk.FATTURA_DI_BENI) == 0) {
            if (fpi.getValore_statistico() == null || (fpi.getValore_statistico().compareTo(java.math.BigDecimal.ZERO) == 0 && fpi.getAmmontare_euro().compareTo(java.math.BigDecimal.ZERO) != 0))
                throw new ValidationException("Specificare un valore statistico per il dettaglio " + descr + "!");
            if (fpi.getValore_statistico().compareTo(fpi.getAmmontare_euro()) > 0)
                throw new ValidationException("L'importo del valore statistico non può essere superiore all'ammontare in euro per il dettaglio " + descr + "!");
            if (fpi.getNatura_transazione() == null || fpi.getNatura_transazione().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una natura transazione per il dettaglio " + descr + "!");
            if (fpi.getModalita_trasporto() == null || fpi.getModalita_trasporto().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una modalità di trasporto per il dettaglio " + descr + "!");
            if (fpi.getNazione_origine() == null || fpi.getNazione_origine().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una nazione di origine per il dettaglio " + descr + "!");
            if (fpi.getCondizione_consegna() == null || fpi.getCondizione_consegna().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una condizione di consegna per il dettaglio " + descr + "!");
            if (fpi.getNazione_provenienza() == null || fpi.getNazione_provenienza().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una nazione di provenienza per il dettaglio " + descr + "!");
            if (fpi.getProvincia_destinazione() == null || fpi.getProvincia_destinazione().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una provincia di destinazione per il dettaglio " + descr + "!");
            if (fpi.getNomenclatura_combinata() == null || fpi.getNomenclatura_combinata().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una nomenclatura combinata per il dettaglio " + descr + "!");
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
            if (fpi.getNazione_provenienza() == null || fpi.getNazione_provenienza().getCrudStatus() != OggettoBulk.NORMAL)
                throw new ValidationException("Specificare una nazione di pagamento per il dettaglio " + descr + "!");

        }

    }

    public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {

    }
}

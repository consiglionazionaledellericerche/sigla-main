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

package it.cnr.contab.anagraf00.core.bulk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.StrServ;

import java.util.Dictionary;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Gestione dei dati relativi alla tabella Banca
 */
@JsonInclude(value = Include.NON_NULL)
public class BancaBulk extends BancaBase {

    public static String ORIGINE_ON_LINE = "O";
    public static String ORIGINE_STIPENDI = "S";
    @JsonIgnore
    private TerzoBulk terzo;
    @JsonIgnore
    private it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk abi_cab;
    @JsonIgnore
    private java.util.Collection nazioniIban;
    @JsonIgnore
    private TerzoBulk terzo_delegato;
    private String chiave;
    @JsonIgnore
    private NazioneBulk nazione_iban;

    // CODICE_IBAN_PARTE1 VARCHAR(34)
    @JsonIgnore
    private java.lang.String codice_iban_parte1;

    // CODICE_IBAN_PARTE2 VARCHAR(34)
    @JsonIgnore
    private java.lang.String codice_iban_parte2;

    // CODICE_IBAN_PARTE3 VARCHAR(34)
    @JsonIgnore
    private java.lang.String codice_iban_parte3;

    // CODICE_IBAN_PARTE4 VARCHAR(34)
    @JsonIgnore
    private java.lang.String codice_iban_parte4;

    // CODICE_IBAN_PARTE5 VARCHAR(34)
    @JsonIgnore
    private java.lang.String codice_iban_parte5;

    // CODICE_IBAN_PARTE6 VARCHAR(34)
    @JsonIgnore
    private java.lang.String codice_iban_parte6;

    public final static Dictionary tipo_PostalizzazioneKeys = new OrderedHashtable();

    static {
        for (TipoPostalizzazione tipoPostalizzazione : TipoPostalizzazione.values()) {
            tipo_PostalizzazioneKeys.put(tipoPostalizzazione.value, tipoPostalizzazione.value);
        }
    }

    /**
     * Costruttore di default.
     */

    public BancaBulk() {
        super();
    }

    /**
     * Costruttore di default.
     */

    public BancaBulk(it.cnr.jada.util.action.CRUDController crud) {

        Modalita_pagamentoBulk mod_pagamento = (Modalita_pagamentoBulk) crud.getParentController().getModel();

        String rifModalita = mod_pagamento.getRif_modalita_pagamento().getTi_pagamento();
        String perCessione = "N";
        String cdTerzoDelegato = "XXX";

        setTi_pagamento(mod_pagamento.getRif_modalita_pagamento().getTi_pagamento());
        setTipo_pagamento_siope(mod_pagamento.getRif_modalita_pagamento().getTipo_pagamento_siope());
        //  	if (mod_pagamento.isPerCessione())

        if (mod_pagamento.isPerCessione()) {
            setCd_terzo_delegato(getTerzo_delegato().getCd_terzo());
            perCessione = "Y";
            //cdTerzoDelegato = Integer.toString(getCd_terzo_delegato().intValue());
        }

        setChiave(rifModalita + "-" + perCessione + "-" + cdTerzoDelegato);
    }

    public BancaBulk(java.lang.Integer cd_terzo, java.lang.Long pg_banca) {
        super(cd_terzo, pg_banca);
        setTerzo(new it.cnr.contab.anagraf00.core.bulk.TerzoBulk(cd_terzo));
    }

    public java.lang.String getAbi() {
        it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk abi_cab = this.getAbi_cab();
        if (abi_cab == null)
            return null;
        return abi_cab.getAbi();
    }

    public void setAbi(java.lang.String abi) {
        this.getAbi_cab().setAbi(abi);
    }

    /**
     * Restituisce l'<code>AbicabBulk</code> relativo all'oggetto banca.
     *
     * @return it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk
     *
     */

    public it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk getAbi_cab() {
        return abi_cab;
    }

    /**
     * Imposta l'<code>AbicabBulk</code> relativo all'oggetto banca.
     *
     * @return newAbi_cab Abi e cab da associare.
     *
     */

    public void setAbi_cab(it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk newAbi_cab) {
        abi_cab = newAbi_cab;
    }

    public java.lang.String getCab() {
        it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk abi_cab = this.getAbi_cab();
        if (abi_cab == null)
            return null;
        return abi_cab.getCab();
    }

    public void setCab(java.lang.String cab) {
        this.getAbi_cab().setCab(cab);
    }

    public String getCd_ds_banca() {
        return (getPg_banca() + " - " + getIntestazione() + " (" + getTi_pagamento() + ")");
    }

    public java.lang.Integer getCd_terzo() {
        it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo = this.getTerzo();
        if (terzo == null)
            return null;
        return terzo.getCd_terzo();
    }

    public void setCd_terzo(java.lang.Integer cd_terzo) {
        this.getTerzo().setCd_terzo(cd_terzo);
    }

    /**
     * Insert the method's description here.
     * Creation date: (05/11/2002 16.44.37)
     *
     * @return java.lang.String
     */
    public java.lang.String getChiave() {

        if (chiave == null/* && this.getCrudStatus() == OggettoBulk.NORMAL*/) {
            String perCessione = "N";
            String cdTerzoDelegato = "XXX";

            if (getCd_terzo_delegato() != null) {
                perCessione = "Y";
                //cdTerzoDelegato = Integer.toString(getCd_terzo_delegato().intValue());
            }

            this.setChiave(getTi_pagamento() + "-" + perCessione + "-" + cdTerzoDelegato);
        }
        return chiave;
    }

    /**
     * Insert the method's description here.
     * Creation date: (05/11/2002 16.44.37)
     *
     * @param newChiave java.lang.String
     */
    public void setChiave(java.lang.String newChiave) {
        chiave = newChiave;
    }

    public String getDs_estesa() {

        StringBuffer ds = new StringBuffer();

        if (getNumero_conto() != null)
            ds.append(" Nr: " + getNumero_conto());
        if (getIntestazione() != null)
            ds.append(" Intestazione: " + getIntestazione());
        if (getAbi() != null)
            ds.append(" ABI: " + getAbi());
        if (getCab() != null)
            ds.append(" CAB: " + getCab());
        return new String(ds);
    }

    /**
     * Restituisce il <code>Dictionary</code> per la gestione della descrizione dei tipi banche.
     *
     * @return java.util.Dictionary
     */

    public java.util.Dictionary getDs_lista_bancheKeys() {
        return it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk.DS_LISTA_PAGAMENTI_KEYS;
    }

    /**
     * Restituisce l'<code>TerzoBulk</code> a cui è associato l'oggetto banca.
     *
     * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
     *
     */

    public TerzoBulk getTerzo() {
        return terzo;
    }

    /**
     * Imposta l'<code>TerzoBulk</code> a cui è associato l'oggetto banca.
     *
     * @param newTerzo Terzo da associare.
     *
     */

    public void setTerzo(TerzoBulk newTerzo) {
        terzo = newTerzo;
    }

    /**
     * Restituisce il <code>Dictionary</code> per la gestione delle modalità di pagamento.
     *
     * @return java.util.Dictionary
     * @see <code>Rif_modalita_pagamentoBulk</code>.<code>TI_PAGAMENTO_KEYS</code>
     */

    public java.util.Dictionary getTi_pagamentoKeys() {
        return it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk.TI_PAGAMENTO_KEYS;
    }

    public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {

        setFlagsToFalse();
        setOrigine(BancaBulk.ORIGINE_ON_LINE);
        setFl_cc_cds(new Boolean(false));
        return super.initializeForInsert(bp, context);
    }

    public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {

        setFl_cancellato(null);
        return super.initializeForSearch(bp, context);
    }

    /**
     * Insert the method's description here.
     * Creation date: (26/04/2002 15.16.36)
     *
     * @return boolean
     */
    public boolean isOrigineStipendi() {

        if (getOrigine() != null)
            return (getOrigine().compareTo(ORIGINE_STIPENDI) == 0);

        return false;
    }

    /**
     * Restituisce TRUE se la chiave contiene il flag per cessione == "Y"
     *
     * @return boolean
     */

    public boolean isPerCessione() {

        java.util.StringTokenizer st = new java.util.StringTokenizer(getChiave(), "-", false);
        int count = 0;
        boolean perCessione = false;

        while (st.hasMoreTokens()) {
            count++;
            String tok = st.nextToken();
            if (count == 2) {
                perCessione = tok.equals("Y");
            }
        }


        return perCessione;
    }

    /**
     * Insert the method's description here.
     * Creation date: (26/04/2002 15.16.36)
     *
     * @return boolean
     */
    public boolean isROAbiCab() {

        return isROBanca() || (abi_cab == null || abi_cab.getCrudStatus() == OggettoBulk.NORMAL);
        //return cd_ente_app == null || cd_ente_app.getCrudStatus() == OggettoBulk.NORMAL;

    }

    /**
     * Insert the method's description here.
     * Creation date: (26/04/2002 15.16.36)
     *
     * @return boolean
     */
    public boolean isROBanca() {

        if (isPerCessione() || isOrigineStipendi() || getPg_banca() != null)
            return true;
        if (getFl_cancellato() == null)
            return false;
        return getFl_cancellato().booleanValue();

    }

    /**
     * Insert the method's description here.
     * Creation date: (26/04/2002 15.16.36)
     *
     * @return boolean
     */
    public boolean isROCcd() {

        if (this.getTerzo() != null && this.getTerzo().getUnita_organizzativa() == null)
            return true;
        else
            return isROBanca();
    }

    /**
     * Insert the method's description here.
     * Creation date: (26/04/2002 15.16.36)
     *
     * @return boolean
     */
    public boolean isROCin() {

        return isROBanca();
    }

    public void setFlagsToFalse() {

        this.setFl_cancellato(new Boolean(false));
    }

    public void validate(OggettoBulk parent) throws ValidationException {
        if (Rif_modalita_pagamentoBulk.BANCARIO.equals(getTi_pagamento()) ||
                Optional.ofNullable(getTipo_pagamento_siope())
                        .map(s -> s.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ACCREDITOTESORERIAPROVINCIALESTATOPERTABB.value()))
                        .orElse(Boolean.FALSE)) {
            // SE è BANCARIO
            if (!isROBanca()) validaIban();
            if (this.getNazione_iban() != null && this.getNazione_iban().getCd_iso().equals("IT")) {
                if (getAbi_cab() == null ||
                        getAbi_cab().getAbi() == null ||
                        getAbi_cab().getCab() == null ||
                        getNumero_conto() == null)
                    throw new ValidationException("Modalità di pagamento: ABI, CAB, e Numero Conto sono obbligatori.");
            }
        } else if (Rif_modalita_pagamentoBulk.BANCA_ITALIA.equals(getTi_pagamento()) && (isTABA() || isRegolarizzazione())) {
            // SE è BANCA_ITALIA
            final String regex = "^[0-9]{7}$";
            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            Optional.ofNullable(getNumero_conto())
                    .filter(s -> pattern.matcher(s).find())
                    .orElseThrow(() ->  new ValidationException("Modalità di pagamento: Numero Conto è obbligatorio e la sua lunghezza " +
                            "deve essere di 7 caratteri numerici!"));
        } else if (Rif_modalita_pagamentoBulk.POSTALE.equals(getTi_pagamento())) {
            // SE è POSTALE
            if (getNumero_conto() == null)
                throw new ValidationException("Modalità di pagamento: Numero Conto è obbligatorio.");
        } else if (Rif_modalita_pagamentoBulk.ALTRO.equals(getTi_pagamento())) {
            // SE è ALTRO
            if (getIntestazione() == null)
                throw new ValidationException("Modalità di pagamento: Intestazione è obbligatoria.");
        } else if (Rif_modalita_pagamentoBulk.QUIETANZA.equals(getTi_pagamento())) {
            // SE è QUIETANZA
            if (getQuietanza() == null)
                throw new ValidationException("Modalità di pagamento: Quietanza è obbligatoria.");
        } else if (Rif_modalita_pagamentoBulk.IBAN.equals(getTi_pagamento())) {
            // SE è ALTRO CON IBAN OBBLIGATORIO
            if (getIntestazione() == null)
                throw new ValidationException("Modalità di pagamento: Intestazione è obbligatoria.");
            if (this.getNazione_iban() != null)
                validaIban();
            else
                setCodice_iban(null);
        }
        if ((this.getFl_cancellato() == null || !this.getFl_cancellato().booleanValue()) &&
                ((Rif_modalita_pagamentoBulk.IBAN.equals(getTi_pagamento()) || Rif_modalita_pagamentoBulk.BANCARIO.equals(getTi_pagamento())))) {
            if (this.getNazione_iban() != null && this.getNazione_iban().getFl_iban()) {
                if ((this.getNazione_iban() == null) || (this.getNazione_iban() != null && this.getNazione_iban().getCd_iso().compareTo("IT") != 0)) {
                    if (this.getCodice_swift() == null) {
                        throw new ValidationException("Il codice swift è obbligatorio.");
                    } else {
                        if (!(this.getCodice_swift().length() == 8 || this.getCodice_swift().length() == 11) || this.getCodice_swift().contains(" "))
                            throw new ValidationException("Formato del codice swift/bic non valido, non può contenere spazi e deve essere di 8 o 11 caratteri.");
                        for (int i = 0; i < getCodice_swift().length(); i++)
                            if (!Character.isLetterOrDigit(getCodice_swift().charAt(i)))
                                throw new ValidationException("Formato del codice swift/bic non valido può essere composto solo da lettere e cifre.");

                    }
                }
            }
        }
    }

    public NazioneBulk getNazione_iban() {
        return nazione_iban;
    }

    public void setNazione_iban(NazioneBulk nazione_iban) {
        this.nazione_iban = nazione_iban;
    }

    public java.util.Collection getNazioniIban() {
        return nazioniIban;
    }

    public void setNazioniIban(java.util.Collection nazioniIban) {
        this.nazioniIban = nazioniIban;
    }

    @JsonIgnore
    public java.lang.String getCodice_iban_parte1() {
        return codice_iban_parte1;
    }

    public void setCodice_iban_parte1(java.lang.String codice_iban_parte1) {
        this.codice_iban_parte1 = codice_iban_parte1;
    }

    @JsonIgnore
    public java.lang.String getCodice_iban_parte2() {
        return codice_iban_parte2;
    }

    public void setCodice_iban_parte2(java.lang.String codice_iban_parte2) {
        this.codice_iban_parte2 = codice_iban_parte2;
    }

    @JsonIgnore
    public java.lang.String getCodice_iban_parte3() {
        return codice_iban_parte3;
    }

    public void setCodice_iban_parte3(java.lang.String codice_iban_parte3) {
        this.codice_iban_parte3 = codice_iban_parte3;
    }

    @JsonIgnore
    public java.lang.String getCodice_iban_parte4() {
        return codice_iban_parte4;
    }

    public void setCodice_iban_parte4(java.lang.String codice_iban_parte4) {
        this.codice_iban_parte4 = codice_iban_parte4;
    }

    @JsonIgnore
    public java.lang.String getCodice_iban_parte5() {
        return codice_iban_parte5;
    }

    public void setCodice_iban_parte5(java.lang.String codice_iban_parte5) {
        this.codice_iban_parte5 = codice_iban_parte5;
    }

    @JsonIgnore
    public java.lang.String getCodice_iban_parte6() {
        return codice_iban_parte6;
    }

    public void setCodice_iban_parte6(java.lang.String codice_iban_parte6) {
        this.codice_iban_parte6 = codice_iban_parte6;
    }

    @JsonIgnore
    public int getCodice_iban_parte1MaxLength() {
        if (getNazione_iban() == null) return 0;
        return getNazione_iban().getStruttura_iban_parte1MaxLength();
    }

    @JsonIgnore
    public int getCodice_iban_parte2MaxLength() {
        if (getNazione_iban() == null) return 0;
        return getNazione_iban().getStruttura_iban_parte2MaxLength();
    }

    @JsonIgnore
    public int getCodice_iban_parte3MaxLength() {
        if (getNazione_iban() == null) return 0;
        return getNazione_iban().getStruttura_iban_parte3MaxLength();
    }

    @JsonIgnore
    public int getCodice_iban_parte4MaxLength() {
        if (getNazione_iban() == null) return 0;
        return getNazione_iban().getStruttura_iban_parte4MaxLength();
    }

    @JsonIgnore
    public int getCodice_iban_parte5MaxLength() {
        if (getNazione_iban() == null) return 0;
        return getNazione_iban().getStruttura_iban_parte5MaxLength();
    }

    @JsonIgnore
    public int getCodice_iban_parte6MaxLength() {
        if (getNazione_iban() == null) return 0;
        return getNazione_iban().getStruttura_iban_parte6MaxLength();
    }

    @JsonIgnore
    public int getCodice_iban_parte1InputSize() {
        if (getNazione_iban() == null) return 0;
        return getNazione_iban().getStruttura_iban_parte1InputSize();
    }

    @JsonIgnore
    public int getCodice_iban_parte2InputSize() {
        if (getNazione_iban() == null) return 0;
        return getNazione_iban().getStruttura_iban_parte2InputSize();
    }

    @JsonIgnore
    public int getCodice_iban_parte3InputSize() {
        if (getNazione_iban() == null) return 0;
        return getNazione_iban().getStruttura_iban_parte3InputSize();
    }

    @JsonIgnore
    public int getCodice_iban_parte4InputSize() {
        if (getNazione_iban() == null) return 0;
        return getNazione_iban().getStruttura_iban_parte4InputSize();
    }

    @JsonIgnore
    public int getCodice_iban_parte5InputSize() {
        if (getNazione_iban() == null) return 0;
        return getNazione_iban().getStruttura_iban_parte5InputSize();
    }

    @JsonIgnore
    public int getCodice_iban_parte6InputSize() {
        if (getNazione_iban() == null) return 0;
        return getNazione_iban().getStruttura_iban_parte6InputSize();
    }

    @JsonIgnore
    public String getCodice_iban_calcolato() {
        if (this.getNazione_iban() == null) return null;
        String iban = this.getNazione_iban().getCd_iso();
        for (int i = 0; i < this.getNazione_iban().getStrutturaIbanNrLivelli(); i++) {
            if (i == 0) iban = iban + getCodice_iban_parte1();
            if (i == 1) iban = iban + getCodice_iban_parte2();
            if (i == 2) iban = iban + getCodice_iban_parte3();
            if (i == 3) iban = iban + getCodice_iban_parte4();
            if (i == 4) iban = iban + getCodice_iban_parte5();
            if (i == 5) iban = iban + getCodice_iban_parte6();
        }
        return iban;
    }

    public void validaIban() throws ValidationException {
        if (this.getNazione_iban() == null)
            throw new ValidationException("Codice IBAN obbligatorio.");
        else if (this.getNazione_iban() != null) {
            allineaIbanDaContoIT();
            String codiceParteIban = null;
            for (int i = 0; i < this.getNazione_iban().getStrutturaIbanNrLivelli(); i++) {
                String strutturaParteIban = this.getNazione_iban().getStrutturaIbanLivello(i + 1);
                codiceParteIban = null;
                if (i == 0) codiceParteIban = this.getCodice_iban_parte1();
                if (i == 1) codiceParteIban = this.getCodice_iban_parte2();
                if (i == 2) codiceParteIban = this.getCodice_iban_parte3();
                if (i == 3) codiceParteIban = this.getCodice_iban_parte4();
                if (i == 4) codiceParteIban = this.getCodice_iban_parte5();
                if (i == 5) codiceParteIban = this.getCodice_iban_parte6();

                if (codiceParteIban == null)
                    throw new ValidationException("Inserire tutti i campi del codice Iban.");

                if (codiceParteIban.length() != strutturaParteIban.length())
                    throw new ValidationException("La lunghezza della " + (i + 2) + "^ parte del codice Iban deve essere di " + strutturaParteIban.length() + " caratteri.");

                for (int y = 0; y < strutturaParteIban.length(); y++) {
                    if (strutturaParteIban.charAt(y) != NazioneBulk.IBAN_TIPO_ALFANUMERICO.charAt(0)) {
                        try {
                            char[] data = {codiceParteIban.charAt(y)};
                            int appo = Integer.parseInt(new String(data));
                            if (strutturaParteIban.charAt(y) == NazioneBulk.IBAN_TIPO_CARATTERE.charAt(0))
                                throw new ValidationException("Il " + (y + 1) + "° carattere della " + (i + 2) + "^ parte del codice Iban non deve essere un numero.");
                        } catch (ValidationException e) {
                            throw e;
                        } catch (Exception e) {
                            if (strutturaParteIban.charAt(y) == NazioneBulk.IBAN_TIPO_NUMERICO.charAt(0))
                                throw new ValidationException("Il " + (y + 1) + "° carattere della " + (i + 2) + "^ parte del codice Iban deve essere un numero.");
                        }
                    }
                }
            }
            setCodice_iban(getCodice_iban_calcolato());
            if (this.getTi_pagamento() == null || !this.getTi_pagamento().equals(Rif_modalita_pagamentoBulk.IBAN))
                allineaContoDaIbanIT();
        }
    }

    public void allineaIbanDaContoIT() throws ValidationException {
        if (this.getNazione_iban() != null && this.getNazione_iban().getCd_iso().equals("IT")) {
            if (getCodice_iban_parte2() == null && getCin() != null)
                setCodice_iban_parte2(getCin());
            else if (getCodice_iban_parte2() != null && getCin() != null && !getCodice_iban_parte2().equals(getCin()))
                throw new ValidationException("Attenzione! La 3^ parte del codice Iban è diversa dal codice CIN indicato.");

            // Cod. ABI
            if (getCodice_iban_parte3() == null && getAbi() != null)
                setCodice_iban_parte3(StrServ.lpad(getAbi(), this.getNazione_iban().getStrutturaIbanLivello(3).length(), "0"));
            else if (getCodice_iban_parte3() != null && getAbi() != null && !getCodice_iban_parte3().equals(getAbi()))
                throw new ValidationException("Attenzione! La 4^ parte del codice Iban è diversa dal codice ABI indicato.");

            // Cod. CAB
            if (getCodice_iban_parte4() == null && getCab() != null)
                setCodice_iban_parte4(StrServ.lpad(getCab(), this.getNazione_iban().getStrutturaIbanLivello(4).length(), "0"));
            else if (getCodice_iban_parte4() != null && getCab() != null && !getCodice_iban_parte4().equals(getCab()))
                throw new ValidationException("Attenzione! La 5^ parte del codice Iban è diversa dal codice CAB indicato.");

            // Cod. C/C
            if (getCodice_iban_parte5() == null && getNumero_conto() != null)
                setCodice_iban_parte5(StrServ.lpad(getNumero_conto(), this.getNazione_iban().getStrutturaIbanLivello(5).length(), "0"));
            else if (getCodice_iban_parte5() != null && getNumero_conto() != null && !getNumero_conto().equals(getCodice_iban_parte5()))
                throw new ValidationException("Attenzione! La 6^ parte del codice Iban è diversa dal Numero Conto indicato.");
        }
    }

    public void allineaContoDaIbanIT() throws ValidationException {
        if (this.getNazione_iban() != null && this.getNazione_iban().getCd_iso().equals("IT")) {
            if (getCodice_iban_parte2() != null && getCin() == null)
                setCin(getCodice_iban_parte2());
            else if (getCodice_iban_parte2() != null && getCin() != null && !getCodice_iban_parte2().equals(getCin()))
                throw new ValidationException("Attenzione! La 3^ parte del codice Iban è diversa dal codice CIN indicato.");

            // Cod. ABI
            if (getCodice_iban_parte3() != null && getAbi() == null) {
                if (getAbi_cab() == null) setAbi_cab(new AbicabBulk());
                setAbi(getCodice_iban_parte3());
            } else if (getCodice_iban_parte3() != null && getAbi() != null && !getCodice_iban_parte3().equals(getAbi()))
                throw new ValidationException("Attenzione! La 4^ parte del codice Iban è diversa dal codice ABI indicato.");

            // Cod. CAB
            if (getCodice_iban_parte4() != null && getCab() == null) {
                if (getAbi_cab() == null) setAbi_cab(new AbicabBulk());
                setCab(getCodice_iban_parte4());
            } else if (getCodice_iban_parte4() != null && getCab() != null && !getCodice_iban_parte4().equals(getCab()))
                throw new ValidationException("Attenzione! La 5^ parte del codice Iban è diversa dal codice CAB indicato.");

            // Cod. C/C
            if (getCodice_iban_parte5() != null && getNumero_conto() == null)
                setNumero_conto(getCodice_iban_parte5());
            else if (getCodice_iban_parte5() != null && getNumero_conto() != null && !getNumero_conto().equals(getCodice_iban_parte5()))
                throw new ValidationException("Attenzione! La 6^ parte del codice Iban è diversa dal Numero Conto indicato.");
        }
    }

    public TerzoBulk getTerzo_delegato() {
        return terzo_delegato;
    }

    public void setTerzo_delegato(TerzoBulk terzo_delegato) {
        this.terzo_delegato = terzo_delegato;
    }

    public boolean isROterzo_delegato() {
        return terzo_delegato == null || terzo_delegato.getCrudStatus() == OggettoBulk.NORMAL;
    }

    public java.lang.Integer getCd_terzo_delegato() {
        it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo_delegato = this.getTerzo_delegato();
        if (terzo_delegato == null)
            return null;
        return terzo_delegato.getCd_terzo();
    }

    public void setCd_terzo_delegato(java.lang.Integer cd_terzo_delegato) {
        this.getTerzo_delegato().setCd_terzo(cd_terzo_delegato);
    }

    public boolean isTABA() {
        return Optional.ofNullable(getTipo_pagamento_siope())
                .map(s -> s.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ACCREDITOTESORERIAPROVINCIALESTATOPERTABA.value()))
                .orElse(Boolean.FALSE);
    }

    public boolean isRegolarizzazione() {
        return Optional.ofNullable(getTipo_pagamento_siope())
                .map(s -> s.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.REGOLARIZZAZIONE.value()))
                .orElse(Boolean.FALSE);
    }

    public boolean isTABB() {
        return Optional.ofNullable(getTipo_pagamento_siope())
                .map(s -> s.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ACCREDITOTESORERIAPROVINCIALESTATOPERTABB.value()))
                .orElse(Boolean.FALSE);
    }

    public static Dictionary getTipo_PostalizzazioneKeys() {
        return tipo_PostalizzazioneKeys;
    }

    public enum TipoPostalizzazione {
        COME_DA_CONVENZIONE("COME DA CONVENZIONE"),
        CONSEGNA_ALL_ENTE("CONSEGNA ALL'ENTE"),
        POSTA_PRIORITARIA("POSTA PRIORITARIA"),
        RACCOMANDATA("RACCOMANDATA"),
        RACCOMANDATA_A_R("RACCOMANDATA A.R."),
        ASSICURATA("ASSICURATA");

        private final String value;

        private TipoPostalizzazione(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public static TipoPostalizzazione getValueFrom(String value) {
            for (TipoPostalizzazione tipoPostalizzazione : TipoPostalizzazione.values()) {
                if (tipoPostalizzazione.value.equals(value))
                    return tipoPostalizzazione;
            }
            throw new IllegalArgumentException("Esito no found for value: " + value);
        }
    }
}

package it.cnr.contab.anagraf00.tabrif.bulk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.jada.util.OrderedHashtable;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.ModalitaPagamentoType;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Optional;

/**
 * Gestione dei dati relativi alla tabella Progressione
 */
@JsonInclude(value = Include.NON_NULL)
public class Rif_modalita_pagamentoBulk extends Rif_modalita_pagamentoBase {
    public final static java.util.Dictionary TI_PAGAMENTO_KEYS;
    public final static java.util.Dictionary TIPO_PAGAMENTO_SDI_KEYS;
    public final static java.util.Dictionary DS_LISTA_PAGAMENTI_KEYS;

    public final static String BANCARIO = "B";
    public final static String POSTALE = "P";
    public final static String ALTRO = "A";
    public final static String QUIETANZA = "Q";
    public final static String BANCA_ITALIA = "I";
    public final static String IBAN = "N";
    public final static String ASSEGNO = "S";

    public final static String TABA = "TABA";

    static {
        TI_PAGAMENTO_KEYS = new it.cnr.jada.util.OrderedHashtable();
        TI_PAGAMENTO_KEYS.put(BANCARIO, "Bancario");
        TI_PAGAMENTO_KEYS.put(POSTALE, "Postale");
        TI_PAGAMENTO_KEYS.put(QUIETANZA, "Quietanza");
        TI_PAGAMENTO_KEYS.put(BANCA_ITALIA, "Banca d'Italia");
        TI_PAGAMENTO_KEYS.put(ALTRO, "Altro");
        TI_PAGAMENTO_KEYS.put(IBAN, "Altro con Iban Obbligatorio");

        DS_LISTA_PAGAMENTI_KEYS = new it.cnr.jada.util.OrderedHashtable();
        DS_LISTA_PAGAMENTI_KEYS.put(BANCARIO, "Conti correnti bancari");
        DS_LISTA_PAGAMENTI_KEYS.put(POSTALE, "Conti correnti postali");
        DS_LISTA_PAGAMENTI_KEYS.put(QUIETANZA, "Quietanza");
        DS_LISTA_PAGAMENTI_KEYS.put(BANCA_ITALIA, "Banca d'Italia");
        DS_LISTA_PAGAMENTI_KEYS.put(ALTRO, "Altre modalità di pagamento");
        DS_LISTA_PAGAMENTI_KEYS.put(IBAN, "Altre modalità di pagamento con Iban obbligatorio");

        TIPO_PAGAMENTO_SDI_KEYS = new it.cnr.jada.util.OrderedHashtable();
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_01.value(), "Contanti");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_02.value(), "Assegno");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_03.value(), "Assegno Circolare");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_04.value(), "Contanti presso Tesoreria");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_05.value(), "Bonifico");
//		TIPO_PAGAMENTO_SDI_KEYS.put( "MP_05",    "Bonifico"    );
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_06.value(), "Vaglia Cambiario");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_07.value(), "Bollettino Bancario");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_08.value(), "Carta di Credito");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_09.value(), "RID");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_10.value(), "RID utenze");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_11.value(), "RID veloce");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_12.value(), "RIBA");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_13.value(), "MAV");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_14.value(), "Quietanza Erario");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_15.value(), "Giroconto su Conti di Contabilità Speciale");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_16.value(), "Domiciliazione Bancaria");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_17.value(), "Domiciliazione Postale");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_18.value(), "Bollettino di c/c postale");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_19.value(), "SEPA Direct Debit");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_20.value(), "SEPA Direct Debit CORE");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_21.value(), "SEPA Direct Debit B2B");
        TIPO_PAGAMENTO_SDI_KEYS.put(ModalitaPagamentoType.MP_22.value(), "Trattenuta su somme già riscosse");
    }
    public final static Dictionary tipoPagamentoSiopePlusKeys = new OrderedHashtable();

    static {
        for (TipoPagamentoSiopePlus tipoPagamentoSiopePlus : TipoPagamentoSiopePlus.values()) {
            tipoPagamentoSiopePlusKeys.put(tipoPagamentoSiopePlus.value, tipoPagamentoSiopePlus.value);
        }
    }
    /**
     *
     */
    public Rif_modalita_pagamentoBulk() {
    }

    public Rif_modalita_pagamentoBulk(java.lang.String cd_modalita_pag) {
        super(cd_modalita_pag);
    }

    public String getCd_ds_modalita_pagamento() {

        String result = "";
        result += ((getCd_modalita_pag() == null) ? "n.n." : getCd_modalita_pag()) + " - ";
        result += ((getDs_modalita_pag() == null) ? "n.n." : getDs_modalita_pag());
        return result;
    }

    /**
     * Restituisce il <code>java.util.Dictionary</code> per la gestione delle
     * descrizioni delle banche, contenute in <code>BancaBulk</code>.
     *
     * @return L'elenco.
     */

    public java.util.Dictionary getDs_lista_bancheKeys() {
        return DS_LISTA_PAGAMENTI_KEYS;
    }

    /**
     * Restituisce il <code>java.util.Dictionary</code> per la gestione dei tipi di pagamento.
     *
     * @return L'elenco.
     */

    public java.util.Dictionary getTi_pagamentoKeys() {
        return TI_PAGAMENTO_KEYS;
    }

    public java.util.Dictionary getTipoPagamentoSdiKeys() {
        return TIPO_PAGAMENTO_SDI_KEYS;
    }

    /**
     * Indica quando terzo_delegato deve essere read only.
     *
     * @return boolean
     */

    public boolean isROfl_per_cessione() {

        if (getTi_pagamento() != null && BANCA_ITALIA.equals(getTi_pagamento())) {
            setFl_per_cessione(new Boolean(false));
            return true;
        }
        return false;
    }

    public boolean isModalitaBancaItalia() {
        return getTi_pagamento() != null && getTi_pagamento().equals(Rif_modalita_pagamentoBulk.BANCA_ITALIA);
    }

    public boolean isMandatoRegSospeso() {
        return getTi_mandato() != null && getTi_mandato().compareTo(MandatoBulk.TIPO_REGOLAM_SOSPESO) == 0;
    }

    public String getTiPagamentoColumnSet() {
        return Optional.ofNullable(this)
                .map(rif_modalita_pagamentoBulk -> {
                    if (Optional.ofNullable(rif_modalita_pagamentoBulk.getTipo_pagamento_siope()).isPresent()){
                            if (Arrays.asList(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ASSEGNOCIRCOLARE.value(),
                                    Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ASSEGNOBANCARIOEPOSTALE.value())
                                    .contains(rif_modalita_pagamentoBulk.getTipo_pagamento_siope())) {
                                return Rif_modalita_pagamentoBulk.ASSEGNO;
                            } else if (rif_modalita_pagamentoBulk.getTipo_pagamento_siope().equals(
                                    TipoPagamentoSiopePlus.ACCREDITOTESORERIAPROVINCIALESTATOPERTABA.value()
                            )){
                                return Rif_modalita_pagamentoBulk.TABA;
                            } else if (rif_modalita_pagamentoBulk.getTipo_pagamento_siope().equals(
                                    TipoPagamentoSiopePlus.ACCREDITOTESORERIAPROVINCIALESTATOPERTABB.value()
                            )){
                                return Rif_modalita_pagamentoBulk.BANCARIO;
                            }

                    }
                    return rif_modalita_pagamentoBulk.getTi_pagamento();
                })
                .orElse(Rif_modalita_pagamentoBulk.ALTRO);
    }

    public static Dictionary getTipoPagamentoSiopePlusKeys() {
        return tipoPagamentoSiopePlusKeys;
    }

    public enum TipoPagamentoSiopePlus {
        CASSA("CASSA"),
        SEPACREDITTRANSFER("SEPA CREDIT TRANSFER"),
        ASSEGNOBANCARIOEPOSTALE("ASSEGNO BANCARIO E POSTALE"),
        ASSEGNOCIRCOLARE("ASSEGNO CIRCOLARE"),
        ACCREDITOCONTOCORRENTEPOSTALE("ACCREDITO CONTO CORRENTE POSTALE"),
        ACCREDITOTESORERIAPROVINCIALESTATOPERTABA("ACCREDITO TESORERIA PROVINCIALE STATO PER TAB A"),
        ACCREDITOTESORERIAPROVINCIALESTATOPERTABB("ACCREDITO TESORERIA PROVINCIALE STATO PER TAB B"),
        F24EP("F24EP"),
        VAGLIAPOSTALE("VAGLIA POSTALE"),
        VAGLIATESORO("VAGLIA TESORO"),
        REGOLARIZZAZIONE("REGOLARIZZAZIONE"),
        REGOLARIZZAZIONEACCREDITOTESORERIAPROVINCIALESTATOPERTABA("REGOLARIZZAZIONE ACCREDITO TESORERIA PROVINCIALE STATO PER TAB A"),
        REGOLARIZZAZIONEACCREDITOTESORERIAPROVINCIALESTATOPERTABB("REGOLARIZZAZIONE ACCREDITO TESORERIA PROVINCIALE STATO PER TAB B"),
        ADDEBITOPREAUTORIZZATO("ADDEBITO PREAUTORIZZATO"),
        DISPOSIZIONEDOCUMENTOESTERNO("DISPOSIZIONE DOCUMENTO ESTERNO"),
        COMPENSAZIONE("COMPENSAZIONE"),
        BONIFICOESTEROEURO("BONIFICO ESTERO EURO");

        private final String value;

        private TipoPagamentoSiopePlus(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public static TipoPagamentoSiopePlus getValueFrom(String value) {
            for (TipoPagamentoSiopePlus tipoPagamentoSiopPlus : TipoPagamentoSiopePlus.values()) {
                if (tipoPagamentoSiopPlus.value.equals(value))
                    return tipoPagamentoSiopPlus;
            }
            throw new IllegalArgumentException("Tipo Pagamento no found for value: " + value);
        }
    }
}

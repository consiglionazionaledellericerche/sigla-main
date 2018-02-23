package it.cnr.contab.anagraf00.tabrif.bulk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.ModalitaPagamentoType;

/**
 * Gestione dei dati relativi alla tabella Progressione
 */
@JsonInclude(value=Include.NON_NULL)
public class Rif_modalita_pagamentoBulk extends Rif_modalita_pagamentoBase {
	public final static java.util.Dictionary TI_PAGAMENTO_KEYS;
	public final static java.util.Dictionary TIPO_PAGAMENTO_SDI_KEYS;
	public final static java.util.Dictionary DS_LISTA_PAGAMENTI_KEYS;

	public final static String BANCARIO  = "B";
	public final static String POSTALE   = "P";
	public final static String ALTRO     = "A";
	public final static String QUIETANZA = "Q";
	public final static String BANCA_ITALIA	= "I";
	public final static String IBAN	= "N";

	static {
		TI_PAGAMENTO_KEYS = new it.cnr.jada.util.OrderedHashtable();
		TI_PAGAMENTO_KEYS.put( BANCARIO, "Bancario" );
		TI_PAGAMENTO_KEYS.put( POSTALE,  "Postale"  );
		TI_PAGAMENTO_KEYS.put( QUIETANZA,"Quietanza");
		TI_PAGAMENTO_KEYS.put( BANCA_ITALIA, "Banca d'Italia");
		TI_PAGAMENTO_KEYS.put( ALTRO,    "Altro"    );
		TI_PAGAMENTO_KEYS.put( IBAN,    "Altro con Iban Obbligatorio"    );

		DS_LISTA_PAGAMENTI_KEYS = new it.cnr.jada.util.OrderedHashtable();
		DS_LISTA_PAGAMENTI_KEYS.put(BANCARIO,"Conti correnti bancari");
		DS_LISTA_PAGAMENTI_KEYS.put(POSTALE,"Conti correnti postali");
		DS_LISTA_PAGAMENTI_KEYS.put(QUIETANZA,"Quietanza");
		DS_LISTA_PAGAMENTI_KEYS.put(BANCA_ITALIA,"Banca d'Italia");
		DS_LISTA_PAGAMENTI_KEYS.put(ALTRO,"Altre modalità di pagamento");
		DS_LISTA_PAGAMENTI_KEYS.put(IBAN,"Altre modalità di pagamento con Iban obbligatorio");

		TIPO_PAGAMENTO_SDI_KEYS = new it.cnr.jada.util.OrderedHashtable();
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_01.value(), "Contanti" );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_02.value(),  "Assegno"  );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_03.value(),"Assegno Circolare");
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_04.value(), "Contanti presso Tesoreria");
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_05.value(),    "Bonifico"    );
//		TIPO_PAGAMENTO_SDI_KEYS.put( "MP_05",    "Bonifico"    );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_06.value(),    "Vaglia Cambiario"    );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_07.value(), "Bollettino Bancario" );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_08.value(),  "Carta di Credito"  );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_09.value(),"RID");
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_10.value(), "RID utenze");
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_11.value(),    "RID veloce"    );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_12.value(),    "RIBA"    );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_13.value(),    "MAV"    );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_14.value(),    "Quietanza Erario"    );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_15.value(),    "Giroconto su Conti di Contabilità Speciale"    );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_16.value(),    "Domiciliazione Bancaria"    );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_17.value(),    "Domiciliazione Postale"    );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_18.value(),    "Bollettino di c/c postale"    );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_19.value(),    "SEPA Direct Debit"    );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_20.value(),    "SEPA Direct Debit CORE"    );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_21.value(),    "SEPA Direct Debit B2B"    );
		TIPO_PAGAMENTO_SDI_KEYS.put( ModalitaPagamentoType.MP_22.value(),    "Trattenuta su somme già riscosse"    );
	}
/**
 * 
 */
public Rif_modalita_pagamentoBulk() {}
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

	if (getTi_pagamento() != null && BANCA_ITALIA.equals(getTi_pagamento())){
		setFl_per_cessione(new Boolean(false));
		return true;
	}
	return false;
}

public boolean isModalitaBancaItalia() {
	if (getTi_pagamento() != null && getTi_pagamento().equals(Rif_modalita_pagamentoBulk.BANCA_ITALIA)){
		return true;
	}
	return false;
}
public boolean isMandatoRegSospeso(){
	if(getTi_mandato()!=null && getTi_mandato().compareTo(MandatoBulk.TIPO_REGOLAM_SOSPESO)==0)
	  return true;
	return false; 
}
}

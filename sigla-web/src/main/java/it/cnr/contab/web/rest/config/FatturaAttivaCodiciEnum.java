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

package it.cnr.contab.web.rest.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum FatturaAttivaCodiciEnum {
	ERRORE_FA_98(98,"Parametro necessario alla generazione della Fattura non inserito o non valido, righe Scadenza inserite non valide."),
	ERRORE_FA_99(99,"Tipologia fattura non valida"),
	ERRORE_FA_100(100,"Errore generico applicativo"),
	ERRORE_FA_101(101,"Parametro necessario alla generazione della Fattura non inserito o non valido."),
	ERRORE_FA_102(102,"Fattura o Nota Credito già inserita"),
	ERRORE_FA_103(103,"Fattura di tipo tariffario con codice tariffario non valorizzato"),
	ERRORE_FA_104(104,"Estremi del contratto non presenti"),
	ERRORE_FA_105(105,"Estremi del cliente non presenti"),
	ERRORE_FA_106(106,"Estremi della voce iva non presenti o non validi"),
	ERRORE_FA_107(107,"Estremi della voce non presenti o non coerenti"),
	ERRORE_FA_108(108,"Estremi del contratto non completi"),
	ERRORE_FA_109(109,"Estremi riga fattura di riferimento non presenti"),
	ERRORE_FA_110(110,"Estremi banca cliente non inseriti"),
	ERRORE_FA_111(111,"Estremi modalita' pagamento cliente non inseriti o non validi"),
	ERRORE_FA_112(112,"Formato data registrazione errato; usare dd/mm/yyyy"),
	ERRORE_FA_113(113,"Formato numerico errato prezzo unitario"),
	ERRORE_FA_114(114,"Formato numerico errato quantita"),
	ERRORE_FA_115(115,"Formato numerico errato codice cliente"),
	ERRORE_FA_116(116,"Il codice cliente non corrisponde a quello della riga della fattura di riferimento"),
	ERRORE_FA_117(117,"L'importo ancora disponibile da stornare e' minore dell'importo della nota credito"),
	ERRORE_FA_118(118,"Estremi banca cliente non presenti"),
	ERRORE_FA_119(119,"Formato numerico errato"),
	ERRORE_FA_120(120,"Il codice IVA non corrisponde a quello della riga della fattura di riferimento"),
	ERRORE_FA_121(121,"La causale emissione non corrisponde a quella della riga della fattura di riferimento"),
	ERRORE_FA_122(122,"Il tipo sezionale non corrisponde a quello della riga della fattura di riferimento"),
	ERRORE_FA_123(123,"Il codice tariffario non corrisponde a quello della riga della fattura di riferimento"),
	ERRORE_FA_124(124,"Cliente selezionato NON coerente con il valore liquidazione differita"),
	ERRORE_FA_125(125,"Cliente selezionato NON valido"),
	ERRORE_FA_126(126,"Valore dei campi intra_ue e/o extra_ue e/o san_marino e/o liquidazione differita non corrispondenti a quelli della riga della fattura di riferimento"),
	ERRORE_FA_127(127,"Tipo sezionale non valido"),
	ERRORE_FA_128(128,"Tariffario non valido"),
	ERRORE_FA_129(129,"Valore dei campi intra_ue e/o extra_ue e/o san_marino non coerenti tra loro"),
	ERRORE_FA_130(130,"Cds non valido"),
	ERRORE_FA_131(131,"Unita' organizzativa non valida"),
	ERRORE_FA_132(132,"Modalita' di pagamento del terzo uo non valida"),
	ERRORE_FA_133(133,"Estremi del terzo uo non presenti"),
	ERRORE_FA_134(134,"Estremi della banca del terzo uo non presenti"),
	ERRORE_FA_135(135,"Codice Uo non coerente con il codice Cds"),
	ERRORE_FA_136(136,"Estremi CDR non presenti"),
	ERRORE_FA_137(137,"Estremi G.a.e. non presenti"),
	ERRORE_FA_138(138,"Tipo G.a.e. non coerente con la voce"),
	ERRORE_FA_139(139,"Estremi bene/servizio non presenti"),
	ERRORE_FA_140(140,"Cliente selezionato NON coerente con i campi intra_ue e/o extra_ue e/o san_marino"),
	ERRORE_FA_141(141,"Tipologia bene/servizio non coerente con il bene/servizio"),
	ERRORE_FA_142(142,"Tipologia bene/servizio non coerente con i campi intra_ue e/o extra_ue"),
	ERRORE_FA_143(143,"Estremi Modalità erogazione non presenti"),
	ERRORE_FA_144(144,"Estremi Modalità incasso non presenti"),
	ERRORE_FA_145(145,"Estremi Codici Cpa non presenti"),
	ERRORE_FA_146(146,"Estremi Nazione non presenti"),
	ERRORE_FA_147(147,"Estremi Nomenclatura Combinata non presenti"),
	ERRORE_FA_148(148,"Estremi Natura transazione non presenti"),
	ERRORE_FA_149(149,"Estremi Condizione consegna non presenti"),
	ERRORE_FA_150(150,"Estremi Modalita trasporto non presenti"),
	ERRORE_FA_151(151,"Estremi Provincia non presenti"),
	ERRORE_FA_152(152,"Data di registrazione non valorizzata!"),
	ERRORE_FA_153(153,"Descrizione dell'accertamento non presente!"),
	ERRORE_FA_999(999,"Errore generico");
	
	private final Integer codice;
	private final String message;
	
	private FatturaAttivaCodiciEnum(Integer codice, String message) {
		this.codice = codice;
		this.message = message;
	}

	public Integer getCodice() {
		return codice;
	}

	public String getMessage() {
		return message;
	}
	
	public Map<String, Serializable> getErrorMap() {
		Map<String, Serializable> result = new HashMap<String, Serializable>();
		result.put("codice", codice);
		result.put("message", message);
		return result;
	}	
}

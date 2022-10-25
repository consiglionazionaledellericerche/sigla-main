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

package it.cnr.contab.coepcoan00.comp;

import it.cnr.contab.anagraf00.core.bulk.Anagrafico_esercizioBulk;
import it.cnr.contab.anagraf00.core.bulk.Anagrafico_esercizioHome;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.coepcoan00.core.bulk.*;
import it.cnr.contab.coepcoan00.tabrif.bulk.Ass_anag_voce_epBulk;
import it.cnr.contab.coepcoan00.tabrif.bulk.Ass_anag_voce_epHome;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.contab.compensi00.tabrif.bulk.Ass_tipo_cori_voce_epBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Ass_tipo_cori_voce_epHome;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.pdcep.bulk.*;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.cori00.docs.bulk.*;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.SezionaleBulk;
import it.cnr.contab.docamm00.tabrif.bulk.SezionaleHome;
import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk;
import it.cnr.contab.fondecon00.core.bulk.Fondo_economaleHome;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaHome;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaVBulk;
import it.cnr.contab.gestiva00.core.bulk.Stampa_registri_ivaVBulk;
import it.cnr.contab.missioni00.docs.bulk.*;
import it.cnr.contab.ordmag.ordini.bulk.FatturaOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.pdg00.cdip.bulk.Stipendi_cofiBulk;
import it.cnr.contab.pdg00.cdip.bulk.Stipendi_cofiHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ScritturaPartitaDoppiaComponent extends it.cnr.jada.comp.CRUDComponent implements IScritturaPartitaDoppiaMgr,ICRUDMgr, Serializable,IPrintMgr{
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ScritturaPartitaDoppiaComponent.class);

	private static class DettaglioFinanziario {
		public DettaglioFinanziario(IDocumentoAmministrativoBulk docamm, Integer cdTerzo, Elemento_voceBulk elementoVoce, java.sql.Timestamp dtDaCompetenzaCoge, java.sql.Timestamp dtACompetenzaCoge, BigDecimal imImponibile, BigDecimal imImposta) {
			super();
			this.docamm = docamm;
			this.cdTerzo = cdTerzo;
			this.elementoVoce = elementoVoce;
			this.dtDaCompetenzaCoge = dtDaCompetenzaCoge;
			this.dtACompetenzaCoge = dtACompetenzaCoge;
			this.imImponibile = imImponibile;
			this.imImposta = imImposta;
		}

		public DettaglioFinanziario(IDocumentoAmministrativoBulk docamm, IDocumentoAmministrativoBulk partita, Integer cdTerzo, Elemento_voceBulk elementoVoce, java.sql.Timestamp dtDaCompetenzaCoge, java.sql.Timestamp dtACompetenzaCoge, BigDecimal imImponibile, BigDecimal imImposta) {
			this(docamm, cdTerzo, elementoVoce, dtDaCompetenzaCoge, dtACompetenzaCoge, imImponibile, imImposta);
			this.partita = partita;
		}

		private final IDocumentoAmministrativoBulk docamm;

		private IDocumentoAmministrativoBulk partita;

		private final Integer cdTerzo;

		private final Elemento_voceBulk elementoVoce;

		private final java.sql.Timestamp dtDaCompetenzaCoge;

		private final java.sql.Timestamp dtACompetenzaCoge;

		private final BigDecimal imImponibile;

		private final BigDecimal imImposta;

		public IDocumentoAmministrativoBulk getDocamm() {
			return docamm;
		}

		public IDocumentoAmministrativoBulk getPartita() {
			return partita;
		}

		protected Integer getCdTerzo() {
			return cdTerzo;
		}

		protected Elemento_voceBulk getElementoVoce() {
			return elementoVoce;
		}

		protected Timestamp getDtDaCompetenzaCoge() {
			return dtDaCompetenzaCoge;
		}

		protected Timestamp getDtACompetenzaCoge() {
			return dtACompetenzaCoge;
		}

		protected BigDecimal getImImponibile() {
			return imImponibile;
		}

		protected BigDecimal getImImposta() {
			return imImposta;
		}
	}

	private static class MandatoRigaComplete {
		public MandatoRigaComplete(IDocumentoAmministrativoBulk docamm, Mandato_rigaBulk mandatoRiga, List<IDocumentoAmministrativoRigaBulk> docammRighe, Integer cdTerzo) {
			super();
			this.docamm = docamm;
			this.mandatoRiga = mandatoRiga;
			this.docammRighe = docammRighe;
			this.cdTerzo = cdTerzo;
		}

		private final IDocumentoAmministrativoBulk docamm;
		private final Mandato_rigaBulk mandatoRiga;
		private final List<IDocumentoAmministrativoRigaBulk> docammRighe;
		private final Integer cdTerzo;
		public IDocumentoAmministrativoBulk getDocamm() {
			return docamm;
		}

		public Mandato_rigaBulk getMandatoRiga() {
			return mandatoRiga;
		}

		public List<IDocumentoAmministrativoRigaBulk> getDocammRighe() {
			return docammRighe;
		}

		protected Integer getCdTerzo() {
			return cdTerzo;
		}
	}

	private static class TestataPrimaNota {
		public TestataPrimaNota() {
		}

		public TestataPrimaNota(Timestamp dtDa, Timestamp dtA) {
			super();
			this.dtDa = dtDa;
			this.dtA = dtA;
		}

		private Timestamp dtDa;

		private Timestamp dtA;

		private List<DettaglioPrimaNota> dett = new ArrayList<>();

		public Timestamp getDtDa() {
			return dtDa;
		}

		public void setDtDa(Timestamp dtDa) {
			this.dtDa = dtDa;
		}

		public Timestamp getDtA() {
			return dtA;
		}

		public void setDtA(Timestamp dtA) {
			this.dtA = dtA;
		}

		public List<DettaglioPrimaNota> getDett() {
			return dett;
		}

		public void setDett(List<DettaglioPrimaNota> dett) {
			this.dett = dett;
		}

		public void openDettaglioIva(IDocumentoAmministrativoBulk docamm, String cdConto, BigDecimal importo, Integer cdTerzo, String cdCori) {
			this.addDettaglioIva(docamm, cdConto, importo, cdTerzo, cdCori,true);
		}

		public void openDettaglioIva(IDocumentoAmministrativoBulk docamm, IDocumentoAmministrativoBulk partita, String cdConto, BigDecimal importo, Integer cdTerzo, String cdCori) {
			DettaglioPrimaNota dettPN = this.addDettaglioIva(docamm, cdConto, importo, cdTerzo, cdCori,true);
			dettPN.setPartita(partita);
		}

		public void openDettaglioIvaSplit(IDocumentoAmministrativoBulk docamm, String cdConto, BigDecimal importo, Integer cdTerzo, String cdCori) {
			this.addDettaglioIvaSplit(docamm, cdConto, importo, cdTerzo, cdCori,true);
		}

		public void closeDettaglioIva(IDocumentoAmministrativoBulk docamm, String cdConto, BigDecimal importo, Integer cdTerzo, String cdCori) {
			this.addDettaglioIva(docamm, cdConto, importo, cdTerzo, cdCori,false);
		}

		public void closeDettaglioIva(IDocumentoAmministrativoBulk docamm, IDocumentoAmministrativoBulk partita, String cdConto, BigDecimal importo, Integer cdTerzo, String cdCori) {
			DettaglioPrimaNota dettPN = this.addDettaglioIva(docamm, cdConto, importo, cdTerzo, cdCori, false);
			dettPN.setPartita(partita);
		}

		public void closeDettaglioIvaSplit(IDocumentoAmministrativoBulk docamm, String cdConto, BigDecimal importo, Integer cdTerzo, String cdCori) {
			this.addDettaglioIvaSplit(docamm, cdConto, importo, cdTerzo, cdCori,false);
		}

		public void closeDettaglioIvaSplit(IDocumentoAmministrativoBulk docamm, IDocumentoAmministrativoBulk partita, String cdConto, BigDecimal importo, Integer cdTerzo, String cdCori) {
			DettaglioPrimaNota dettPN = this.addDettaglioIvaSplit(docamm, cdConto, importo, cdTerzo, cdCori,false);
			dettPN.setPartita(partita);
		}

		private DettaglioPrimaNota addDettaglioIva(IDocumentoAmministrativoBulk docamm, String cdConto, BigDecimal importo, Integer cdTerzo, String cdCori, boolean isOpen) {
			String tipoDettaglio;
			if (docamm instanceof Fattura_attivaBulk)
				tipoDettaglio = Movimento_cogeBulk.TipoRiga.IVA_VENDITE.value();
			else
				tipoDettaglio = Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO.value();

			if (isOpen)
				return this.addDettaglio(tipoDettaglio, docamm.getTipoDocumentoEnum().getSezioneIva(), cdConto, importo, docamm, cdTerzo, cdCori);
			else
				return this.addDettaglio(tipoDettaglio, Movimento_cogeBulk.getControSezione(docamm.getTipoDocumentoEnum().getSezioneIva()), cdConto, importo, docamm, cdTerzo, cdCori);
		}

		private DettaglioPrimaNota addDettaglioIvaSplit(IDocumentoAmministrativoBulk docamm, String cdConto, BigDecimal importo, Integer cdTerzo, String cdCori, boolean isOpen) {
			String tipoDettaglio;
			if (docamm instanceof Fattura_attivaBulk)
				tipoDettaglio = Movimento_cogeBulk.TipoRiga.IVA_VENDITE_SPLIT.value();
			else
				tipoDettaglio = Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO_SPLIT.value();

			if (isOpen)
				return this.addDettaglio(tipoDettaglio, docamm.getTipoDocumentoEnum().getSezioneIva(), cdConto, importo, docamm, cdTerzo, cdCori);
			else
				return this.addDettaglio(tipoDettaglio, Movimento_cogeBulk.getControSezione(docamm.getTipoDocumentoEnum().getSezioneIva()), cdConto, importo, docamm, cdTerzo, cdCori);
		}

		public void openDettaglioCostoRicavo(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo) {
			openDettaglioCostoRicavo(docamm, cdConto, importo, false, null, null);
		}

		public void openDettaglioCostoRicavoPartita(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, Integer cdTerzo) {
			openDettaglioCostoRicavo(docamm, cdConto, importo, true, cdTerzo, null);
		}

		public void openDettaglioCostoRicavo(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, boolean registraPartita, Integer cdTerzo, String cdCori) {
			DettaglioPrimaNota dettPN = this.addDettaglioCostoRicavo(docamm, cdConto, importo, registraPartita, cdTerzo, cdCori, true);
			dettPN.setModificabile(Boolean.TRUE);
		}

		public void closeDettaglioCostoRicavo(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo) {
			this.closeDettaglioCostoRicavo(docamm, cdConto, importo, false, null, null, false);
		}

		public void closeDettaglioCostoRicavoPartita(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, Integer cdTerzo) {
			this.closeDettaglioCostoRicavo(docamm, cdConto, importo, true, cdTerzo, null, false);
		}

		public void closeDettaglioCostoRicavo(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, boolean registraPartita, Integer cdTerzo, String cdCori, boolean isModificabile) {
			DettaglioPrimaNota dettPN = this.addDettaglioCostoRicavo(docamm, cdConto, importo, registraPartita, cdTerzo, cdCori,false);
			dettPN.setModificabile(isModificabile);
		}

		private DettaglioPrimaNota addDettaglioCostoRicavo(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, boolean registraPartita, Integer cdTerzo, String cdCori, boolean isOpen) {
			if (isOpen)
				return this.addDettaglio(docamm.getTipoDocumentoEnum().getTipoEconomica(), docamm.getTipoDocumentoEnum().getSezioneEconomica(), cdConto, importo, registraPartita?docamm:null, cdTerzo, cdCori);
			return this.addDettaglio(docamm.getTipoDocumentoEnum().getTipoEconomica(), Movimento_cogeBulk.getControSezione(docamm.getTipoDocumentoEnum().getSezioneEconomica()), cdConto, importo, registraPartita?docamm:null, cdTerzo, cdCori);
		}

		public void openDettaglioPatrimoniale(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo) {
			openDettaglioPatrimoniale(docamm, cdConto, importo, false, null, null);
		}

		public void openDettaglioPatrimonialePartita(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, Integer cdTerzo) {
			openDettaglioPatrimoniale(docamm, cdConto, importo, true, cdTerzo, null);
		}

		public void openDettaglioPatrimonialePartita(IDocumentoCogeBulk docamm, IDocumentoCogeBulk partita, String cdConto, BigDecimal importo, Integer cdTerzo) {
			DettaglioPrimaNota dettPN = openDettaglioPatrimoniale(docamm, cdConto, importo, true, cdTerzo, null);
			dettPN.setPartita(partita);
		}

		public void openDettaglioPatrimonialeCori(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, Integer cdTerzo, String cdCori) {
			openDettaglioPatrimoniale(docamm, cdConto, importo, true, cdTerzo, cdCori);
		}

		public DettaglioPrimaNota openDettaglioPatrimoniale(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, boolean registraPartita, Integer cdTerzo, String cdCori) {
			DettaglioPrimaNota dettPN = this.addDettaglioPatrimoniale(docamm, cdConto, importo, registraPartita, cdTerzo, cdCori, true);
			dettPN.setModificabile(Boolean.TRUE);
			return dettPN;
		}

		public void closeDettaglioPatrimoniale(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo) {
			this.closeDettaglioPatrimoniale(docamm, cdConto, importo, false, null, null, false);
		}

		public void closeDettaglioPatrimonialePartita(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, Integer cdTerzo) {
			this.closeDettaglioPatrimonialePartita(docamm, cdConto, importo, cdTerzo, false);
		}

		public void closeDettaglioPatrimonialePartita(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, Integer cdTerzo, boolean isModificabile) {
			this.closeDettaglioPatrimoniale(docamm, cdConto, importo, true, cdTerzo, null, isModificabile);
		}

		public void closeDettaglioPatrimonialePartita(IDocumentoCogeBulk docamm, IDocumentoCogeBulk partita, String cdConto, BigDecimal importo, Integer cdTerzo, boolean isModificabile) {
			DettaglioPrimaNota dettPN = this.closeDettaglioPatrimoniale(docamm, cdConto, importo, true, cdTerzo, null, isModificabile);
			dettPN.setPartita(partita);
		}

		public void closeDettaglioPatrimonialeCori(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, Integer cdTerzo, String cdCori) {
			this.closeDettaglioPatrimoniale(docamm, cdConto, importo, true, cdTerzo, cdCori, false);
		}

		public DettaglioPrimaNota closeDettaglioPatrimoniale(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, boolean registraPartita, Integer cdTerzo, String cdCori, boolean isModificabile) {
			DettaglioPrimaNota dettPN = this.addDettaglioPatrimoniale(docamm, cdConto, importo, registraPartita, cdTerzo, cdCori,false);
			dettPN.setModificabile(isModificabile);
			return dettPN;
		}

		public DettaglioPrimaNota addDettaglioPatrimoniale(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, boolean registraPartita, Integer cdTerzo, String cdCori, boolean isOpen) {
			if (isOpen)
				return this.addDettaglio(docamm.getTipoDocumentoEnum().getTipoPatrimoniale(), docamm.getTipoDocumentoEnum().getSezionePatrimoniale(), cdConto, importo, registraPartita?docamm:null, cdTerzo, cdCori);
			else
				return this.addDettaglio(docamm.getTipoDocumentoEnum().getTipoPatrimoniale(), Movimento_cogeBulk.getControSezione(docamm.getTipoDocumentoEnum().getSezionePatrimoniale()), cdConto, importo, registraPartita?docamm:null, cdTerzo, cdCori);
		}

		public void addDettaglio(String tipoDettaglio, String sezione, String cdConto, BigDecimal importo) {
			this.addDettaglio(tipoDettaglio, sezione, cdConto, importo, null, null, null);
		}

		public DettaglioPrimaNota addDettaglio(String tipoDettaglio, String sezione, String cdConto, BigDecimal importo, IDocumentoCogeBulk partita, Integer cdTerzo, String cdCori) {
			String mySezione = importo.compareTo(BigDecimal.ZERO)<0?Movimento_cogeBulk.getControSezione(sezione):sezione;
			DettaglioPrimaNota dettPN = new DettaglioPrimaNota(tipoDettaglio, mySezione, cdConto, importo.abs(), partita, cdTerzo, cdCori);
			dettPN.setModificabile(Boolean.FALSE);
			dett.add(dettPN);
			return dettPN;
		}
	}

	private static class DettaglioPrimaNota {
		public DettaglioPrimaNota(String tipoDett, String sezione, String cdConto, BigDecimal importo, IDocumentoCogeBulk partita, Integer cdTerzo, String cdCori) {
			super();
			this.tipoDett = tipoDett;
			this.cdConto = cdConto;
			this.sezione = sezione;
			this.importo = importo;
			this.partita = partita;
			this.cdTerzo = cdTerzo;
			this.cdCori = cdCori;
		}

		private String tipoDett;
		private String sezione;
		private String cdConto;
		private BigDecimal importo;
		private IDocumentoCogeBulk partita;
		private Integer cdTerzo;
		private String cdCori;
		private boolean modificabile;

		public String getTipoDett() {
			return tipoDett;
		}

		public void setTipoDett(String tipoDett) {
			this.tipoDett = tipoDett;
		}

		public String getSezione() {
			return sezione;
		}

		public void setSezione(String sezione) {
			this.sezione = sezione;
		}

		public String getCdConto() {
			return cdConto;
		}

		public void setCdConto(String cdConto) {
			this.cdConto = cdConto;
		}

		public BigDecimal getImporto() {
			return importo;
		}

		public void setImporto(BigDecimal importo) {
			this.importo = importo;
		}

		public IDocumentoCogeBulk getPartita() {
			return partita;
		}

		public void setPartita(IDocumentoCogeBulk partita) {
			this.partita = partita;
		}

		public Integer getCdTerzo() {
			return cdTerzo;
		}

		public void setCdTerzo(Integer cdTerzo) {
			this.cdTerzo = cdTerzo;
		}

		public String getCdCori() {
			return cdCori;
		}

		public void setCdCori(String cdCori) {
			this.cdCori = cdCori;
		}

		public boolean isModificabile() {
			return modificabile;
		}

		public void setModificabile(Boolean modificabile) {
			this.modificabile = modificabile;
		}

		public boolean isDettaglioIva() {
			return Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO.value().equals(this.getTipoDett()) ||
					Movimento_cogeBulk.TipoRiga.IVA_VENDITE.value().equals(this.getTipoDett()) ||
					Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO_SPLIT.value().equals(this.getTipoDett()) ||
					Movimento_cogeBulk.TipoRiga.IVA_VENDITE_SPLIT.value().equals(this.getTipoDett());
		}

		public boolean isDettaglioCostoRicavo() {
			return Movimento_cogeBulk.TipoRiga.COSTO.value().equals(this.getTipoDett()) ||
					Movimento_cogeBulk.TipoRiga.RICAVO.value().equals(this.getTipoDett());
		}

		public boolean isDettaglioPatrimoniale() {
			return Movimento_cogeBulk.TipoRiga.DEBITO.value().equals(this.getTipoDett()) ||
					Movimento_cogeBulk.TipoRiga.CREDITO.value().equals(this.getTipoDett()) ||
					Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO.value().equals(this.getTipoDett()) ||
					Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO_SPLIT.value().equals(this.getTipoDett()) ||
					Movimento_cogeBulk.TipoRiga.IVA_VENDITE.value().equals(this.getTipoDett()) ||
					Movimento_cogeBulk.TipoRiga.IVA_VENDITE_SPLIT.value().equals(this.getTipoDett());
		}

		public boolean isSezioneDare() {
			return Movimento_cogeBulk.SEZIONE_DARE.equals(this.getSezione());
		}

		public boolean isSezioneAvere() {
			return Movimento_cogeBulk.SEZIONE_AVERE.equals(this.getSezione());
		}
	}

	private static class Partita implements IDocumentoCogeBulk {
		public Partita(Movimento_cogeBulk movimentoCoge) {
			super();
			this.cd_tipo_doc = movimentoCoge.getCd_tipo_documento();
			this.cd_cds = movimentoCoge.getCd_cds_documento();
			this.cd_uo = movimentoCoge.getCd_uo_documento();
			this.esercizio = movimentoCoge.getEsercizio_documento();
			this.pg_doc = movimentoCoge.getPg_numero_documento();
			this.cd_terzo = movimentoCoge.getCd_terzo();
			this.dt_contabilizzazione = movimentoCoge.getScrittura().getDt_contabilizzazione();
			this.tipoDocumentoEnum = TipoDocumentoEnum.fromValue(movimentoCoge.getCd_tipo_documento());
		}

		public Partita(String cd_tipo_doc, String cd_cds, String cd_uo, Integer esercizio, Long pg_doc, Integer cd_terzo, TipoDocumentoEnum tipoDocumentoEnum) {
			super();
			this.cd_tipo_doc = cd_tipo_doc;
			this.cd_cds = cd_cds;
			this.cd_uo = cd_uo;
			this.esercizio = esercizio;
			this.pg_doc = pg_doc;
			this.cd_terzo = cd_terzo;
			this.tipoDocumentoEnum = tipoDocumentoEnum;
		}

		String cd_tipo_doc;

		String cd_cds;

		String cd_uo;

		Integer esercizio;

		Long pg_doc;

		Integer cd_terzo;

		TipoDocumentoEnum tipoDocumentoEnum;

		Scrittura_partita_doppiaBulk scrittura_partita_doppia;

		Timestamp dt_contabilizzazione;

		Timestamp dtInizioLiquid;

		Timestamp dtFineLiquid;

		String tipoLiquid;

		Long reportIdLiquid;

		@Override
		public String getCd_tipo_doc() {
			return cd_tipo_doc;
		}

		public void setCd_tipo_doc(String cd_tipo_doc) {
			this.cd_tipo_doc = cd_tipo_doc;
		}

		@Override
		public String getCd_cds() {
			return cd_cds;
		}

		public void setCd_cds(String cd_cds) {
			this.cd_cds = cd_cds;
		}

		@Override
		public String getCd_uo() {
			return cd_uo;
		}

		public void setCd_uo(String cd_uo) {
			this.cd_uo = cd_uo;
		}

		@Override
		public Integer getEsercizio() {
			return esercizio;
		}

		public void setEsercizio(Integer esercizio) {
			this.esercizio = esercizio;
		}

		@Override
		public Long getPg_doc() {
			return pg_doc;
		}

		public void setPg_doc(Long pg_doc) {
			this.pg_doc = pg_doc;
		}

		public Integer getCd_terzo() {
			return cd_terzo;
		}

		public void setCd_terzo(Integer cd_terzo) {
			this.cd_terzo = cd_terzo;
		}

		@Override
		public TipoDocumentoEnum getTipoDocumentoEnum() {
			return tipoDocumentoEnum;
		}

		public void setTipoDocumentoEnum(TipoDocumentoEnum tipoDocumentoEnum) {
			this.tipoDocumentoEnum = tipoDocumentoEnum;
		}

		public Scrittura_partita_doppiaBulk getScrittura_partita_doppia() {
			return scrittura_partita_doppia;
		}

		public void setScrittura_partita_doppia(Scrittura_partita_doppiaBulk scrittura_partita_doppia) {
			this.scrittura_partita_doppia = scrittura_partita_doppia;
		}

		@Override
		public Timestamp getDt_contabilizzazione() {
			return dt_contabilizzazione;
		}

		public void setDt_contabilizzazione(Timestamp dt_contabilizzazione) {
			this.dt_contabilizzazione = dt_contabilizzazione;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Partita partita = (Partita) o;
			return Objects.equals(cd_tipo_doc, partita.cd_tipo_doc) && Objects.equals(cd_cds, partita.cd_cds) && Objects.equals(cd_uo, partita.cd_uo) && Objects.equals(esercizio, partita.esercizio) && Objects.equals(pg_doc, partita.pg_doc) && tipoDocumentoEnum == partita.tipoDocumentoEnum;
		}

		@Override
		public int hashCode() {
			return Objects.hash(cd_tipo_doc, cd_cds, cd_uo, esercizio, pg_doc, tipoDocumentoEnum);
		}

		@Override
		public void setStato_coge(String stato_coge) {
		}

		@Override
		public String getStato_coge() {
			return null;
		}

		@Override
		public Timestamp getDtInizioLiquid() {
			return dtInizioLiquid;
		}

		public void setDtInizioLiquid(Timestamp dtInizioLiquid) {
			this.dtInizioLiquid = dtInizioLiquid;
		}

		@Override
		public Timestamp getDtFineLiquid() {
			return dtFineLiquid;
		}

		public void setDtFineLiquid(Timestamp dtFineLiquid) {
			this.dtFineLiquid = dtFineLiquid;
		}

		@Override
		public String getTipoLiquid() {
			return tipoLiquid;
		}

		public void setTipoLiquid(String tipoLiquid) {
			this.tipoLiquid = tipoLiquid;
		}

		@Override
		public Long getReportIdLiquid() {
			return reportIdLiquid;
		}

		public void setReportIdLiquid(Long reportIdLiquid) {
			this.reportIdLiquid = reportIdLiquid;
		}
	}
	/**
	 * ScritturaPartitaDoppiaComponent constructor comment.
	 */
	public ScritturaPartitaDoppiaComponent() {
		super();
	}
	/**
	 *
	 * Nome: Aggiornamento saldi
	 * Pre: È stata richiesto l'aggiornamento dei saldi coge come conseguenza di un inserimento di
	 *       una nuova scrittura in partita doppia
	 * Post: È stata richiamata la stored procedure che esegue l'aggiornamento dei saldi coge relativi ai
	 *       conti di tutti i movimenti dare e avere della scrittura
	 *
	 * @param userContext <code>UserContext</code>
	 * @param scrittura <code>Scrittura_partita_doppiaBulk</code>  che deve essere inserita
	 *
	 */

	private void aggiornaSaldiCoge (UserContext userContext, Scrittura_partita_doppiaBulk scrittura ) throws ComponentException
	{
		try
		{
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ),
					"{ call " +
							it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
							"CNRCTB200.aggiornaSaldoCoge(?, ?, ?, ?, ?)}",false,this.getClass());
			try
			{
				cs.setString( 1, scrittura.getCd_cds() );
				cs.setObject( 2, scrittura.getEsercizio() );
				cs.setString( 3, scrittura.getCd_unita_organizzativa() );
				cs.setObject( 4, scrittura.getPg_scrittura() );
				cs.setString( 5, scrittura.getUser());
				cs.executeQuery();
			}
			catch ( java.lang.Exception e )
			{
				throw new ComponentException( e );
			}
			finally
			{
				cs.close();
			}
		}
		catch ( SQLException e )
		{
			throw handleException(e);
		}
	}
	/**
	 * Nome: Ricerca dell'attributo relativo al terzo con codice '0'
	 * Pre: E' stata richiesta la ricerca di un terzo e l'utente ha specificato come codice
	 *       il valore '0' ad indicare tutte le classificazioni anagrafiche
	 * Post: Viene restituito un RemoteIterator contenente solamente l'oggetto fittizio (con codice '0') che rappresenta
	 *       tutte le classificazioni anagrafiche
	 * <p>
	 * Nome: Ricerca dell'attributo relativo al terzo con codice '0'
	 * Pre: È stata richiesta la ricerca di un terzo e l'utente ha specificato come codice
	 *       un valore diverso da '0'
	 * Post: Viene restituito un RemoteIterator contenente la lista di oggetti di tipo TerzoBulk
	 *       risultante dall'esecuzione della query sul database
	 * <p>
	 * Nome: Ricerca di un attributo diverso da terzo
	 * Pre: È stata richiesta la ricerca di un attributo diverso da 'terzo'
	 * Post: Viene restituito un RemoteIterator contenente la lista degli oggettiBulk
	 *       risultante dall'esecuzione della query sul database
	 * <p>
	 *
	 * @param userContext <code>UserContext</code>
	 * @param clausole <code>CompoundFindClause</code>  clausole specificate dall'utente
	 * @param bulk <code>OggettoBulk</code>  oggettoBulk da ricercare
	 * @param contesto <code>Scrittura_partita_doppiaBulk</code>  contesto della ricerca
	 * @param attributo nome dell'attributo del contesto che deve essere ricercato
	 * @return <code>RemoteIterator</code>  elenco di oggetti trovati
	 *
	 */

	public it.cnr.jada.util.RemoteIterator cerca(UserContext userContext,it.cnr.jada.persistency.sql.CompoundFindClause clausole,OggettoBulk bulk,OggettoBulk contesto,String attributo) throws it.cnr.jada.comp.ComponentException {
		if ( attributo.equals("terzo") )
		{
			if ( (((TerzoBulk) bulk).getCd_terzo() != null && ((TerzoBulk) bulk).getCd_terzo().equals(TerzoBulk.TERZO_NULLO)))
			{
				TerzoBulk terzo = getTerzoNullo();
				return new it.cnr.jada.util.ArrayRemoteIterator(new TerzoBulk[] { terzo });
			}
		}
		return super.cerca(userContext,clausole,bulk,contesto,attributo);
	}
	/**
	 *  Scrittura in Partita Doppia - Esercizio COEP/COAN chiuso
	 *    PreCondition:
	 *      L'esrcizio COEP/COAN risulta chiuso per il CdS di scrivania
	 *    PostCondition:
	 *      Non viene consentita il salvataggio.
	 * <p>
	 *  Tutti i controlli superati.
	 *    PreCondition:
	 *      Nessun errore rilevato.
	 *    PostCondition:
	 *      Viene consentito il salvataggio.
	 *
	 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	 * @param bulk <code>OggettoBulk</code> il Bulk da creare
	 *
	 * @return l'oggetto <code>OggettoBulk</code> creato
	 **/
	public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException{

		if (isEsercizioChiuso(userContext))
			throw new ApplicationException ("Attenzione: esercizio economico chiuso o in fase di chiusura.");


		return super.creaConBulk(userContext, bulk);
	}
	/**
	 *
	 * Nome: Inserimento di una scrittura
	 * Pre: È stata richiesta l'inserimento di una scrittura in partita doppia già validata
	 * Post: L'importo della scrittura viene impostato come la somma degli importi dei movimenti avere (che coincide con la
	 *       somma degli importi dei movimenti dare) e il saldo coge relativo ai conti econ-patr. dei movimenti viene aggiornato
	 *       (metodo aggiornaSaldiCoge)
	 *
	 * @param userContext <code>UserContext</code>
	 * @param bulk <code>Scrittura_partita_doppiaBulk</code>  che deve essere inserita
	 * @return <code>Scrittura_partita_doppiaBulk</code>  inserita
	 *
	 */

	protected OggettoBulk eseguiCreaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException,PersistencyException
	{
		Scrittura_partita_doppiaBulk scrittura = (Scrittura_partita_doppiaBulk) bulk;
		scrittura.setIm_scrittura( scrittura.getImTotaleAvere());
		if ( scrittura.getTerzo() == null || scrittura.getTerzo().getCd_terzo() == null ) {
            scrittura.setTerzo(getTerzoNullo());
            scrittura.getAllMovimentiColl().forEach(el->el.setTerzo(scrittura.getTerzo()));
        }
		makeBulkPersistent(userContext,scrittura);
		aggiornaSaldiCoge( userContext, scrittura );
		return bulk;
	}

	@Override
	protected OggettoBulk eseguiModificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		Scrittura_partita_doppiaBulk scrittura = (Scrittura_partita_doppiaBulk) oggettobulk;
		scrittura.getAllMovimentiColl().forEach(el->el.setTerzo(scrittura.getTerzo()));
		return super.eseguiModificaConBulk(usercontext, oggettobulk);
	}

	/**
	 *
	 * Nome: Storno e riemissione di una scrittura
	 * Pre: È stata richiesta la cancellazione di una scrittura in partita doppia
	 * Post: È stata richiamata la stored procedure che esegue lo storno e la riemissione della partita doppia e
	 *       aggiorna i saldi coge
	 *
	 * @param userContext <code>UserContext</code>
	 * @param bulk <code>Scrittura_partita_doppiaBulk</code>  che deve essere inserita
	 *
	 */

	protected void eseguiEliminaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException
	{
		try
		{
			Scrittura_partita_doppiaBulk scrittura = (Scrittura_partita_doppiaBulk) bulk;

			LoggableStatement cs = new LoggableStatement(getConnection( userContext ),
					"{ call " +
							it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
							"CNRCTB200.creaScrittStornoCoge(?, ?, ?, ?, ?)}",false,this.getClass());
			try
			{
				cs.setString( 1, scrittura.getCd_cds() );
				cs.setObject( 2, scrittura.getEsercizio() );
				cs.setString( 3, scrittura.getCd_unita_organizzativa() );
				cs.setObject( 4, scrittura.getPg_scrittura() );
				cs.setString( 5, scrittura.getUser());
				cs.executeQuery();
			}
			catch ( java.lang.Exception e )
			{
				throw new ComponentException( e );
			}
			finally
			{
				cs.close();
			}
		}
		catch ( SQLException e )
		{
			throw handleException(e);
		}
	}
	/* restitusce un'istanza fittizia di TerzoBulk con codice = 0 a rappresentare
       la non presenza di un terzo */
	private TerzoBulk getTerzoNullo()
	{
		TerzoBulk terzo = new TerzoBulk();
		terzo.setCd_terzo( TerzoBulk.TERZO_NULLO);
		terzo.setDenominazione_sede("Terzo non definito");
		terzo.setCrudStatus( TerzoBulk.NORMAL);
		return terzo;
	}
	/**
	 *
	 * Nome: Inizializzazione di una scrittura
	 * Pre: È stata richiesta l'inizializzazione per inserimento di una scrittura in partita doppia
	 * Post: La scrittura viene restituita con inizializzata la data di contabilizzazione (metodo inizializzaDataContabilizzazione)
	 *       e il cds (con il cds di scrivania)
	 *
	 * @param userContext <code>UserContext</code>
	 * @param bulk <code>Scrittura_partita_doppiaBulk</code>  che deve essere inizializzato per inserimento
	 * @return <code>Scrittura_partita_doppiaBulk</code>  inizializzato per inserimento
	 *
	 */

	public OggettoBulk inizializzaBulkPerInserimento (UserContext userContext,OggettoBulk bulk) throws ComponentException
	{

		/* Gennaro Borriello - (30/09/2004 11.39.59)
		 *	In caso di esercizio COAN/COEP chiuso, blocca l'utente.
		 *	(Su indicazione di Massimo Bartolucci)
		 */
		if(isEsercizioChiuso(userContext))
			throw  new it.cnr.jada.comp.ApplicationException("Attenzione: esercizio economico chiuso o in fase di chiusura.");

		try
		{
			bulk = super.inizializzaBulkPerInserimento( userContext, bulk);
			if ( bulk instanceof Scrittura_partita_doppiaBulk )
			{
				Scrittura_partita_doppiaBulk scrittura = (Scrittura_partita_doppiaBulk) bulk;
				inizializzaDataContabilizzazione(userContext, scrittura);
				scrittura.setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( new CdsBulk( scrittura.getUo().getCd_unita_padre())));
				scrittura.setCd_cds_documento( scrittura.getCd_cds() );
				return scrittura;
			}

			return bulk;
		}
		catch ( Exception e )
		{
			throw handleException( bulk, e );
		}
	}
	/**
	 *
	 * Nome: Inizializzazione di una scrittura
	 * Pre: È stata richiesta l'inizializzazione per modifica di una scrittura in partita doppia
	 * Post: La scrittura viene restituita con inizializzata la collezione di movimenti dare e movimenti avere
	 * <p>
	 * Nome: Inizializzazione di un movimento
	 * Pre:  E' stata richiesta l'inizializzazione per visualizzazione di un movimento coge
	 * Post: Il movimento viene restituito con l'inizializzazione di default
	 * <p>
	 * Nome: Inizializzazione di un saldo
	 * Pre:  E' stata richiesta l'inizializzazione per visualizzazione di un saldo coge
	 * Post: Il saldo viene restituito con l'inizializzazione di default
	 *
	 * @param userContext <code>UserContext</code>
	 * @param bulk <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> che devono essere inizializzati per modifica
	 * @return <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> inizializzati per modifica
	 *
	 */
	public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws ComponentException {
		try
		{
			bulk = super.inizializzaBulkPerModifica( userContext, bulk );

			if ( bulk instanceof Scrittura_partita_doppiaBulk )
			{
				Scrittura_partita_doppiaBulk scrittura = (Scrittura_partita_doppiaBulk) bulk;
				scrittura.setMovimentiDareColl( new BulkList( ((Scrittura_partita_doppiaHome) getHome( userContext, scrittura.getClass())).findMovimentiDareColl( userContext, scrittura )));
				scrittura.setMovimentiAvereColl( new BulkList( ((Scrittura_partita_doppiaHome) getHome( userContext, scrittura.getClass())).findMovimentiAvereColl( userContext, scrittura )));
				if (Optional.ofNullable(scrittura.getCd_terzo()).isPresent()) {
					if ( scrittura.getCd_terzo().equals( TerzoBulk.TERZO_NULLO))
						scrittura.setTerzo( getTerzoNullo());
					else
						scrittura.setTerzo( (TerzoBulk) getHome( userContext, TerzoBulk.class).findByPrimaryKey( scrittura.getTerzo()));
				}
			}
			else if ( bulk instanceof Saldo_cogeBulk )
			{
				Saldo_cogeBulk saldo = (Saldo_cogeBulk) bulk;
				if ( saldo.getCd_terzo().equals( TerzoBulk.TERZO_NULLO))
					saldo.setTerzo( getTerzoNullo());
				else
					saldo.setTerzo( (TerzoBulk) getHome( userContext, TerzoBulk.class).findByPrimaryKey( saldo.getTerzo()));
			}
			else if ( bulk instanceof Movimento_cogeBulk )
			{
				Movimento_cogeBulk mov = (Movimento_cogeBulk) bulk;
				if ( mov.getCd_terzo().equals( TerzoBulk.TERZO_NULLO))
					mov.getScrittura().setTerzo( getTerzoNullo());
				else
					mov.getScrittura().setTerzo( (TerzoBulk) getHome( userContext, TerzoBulk.class).findByPrimaryKey( mov.getScrittura().getTerzo()));
			}

			if(isEsercizioChiuso(userContext))
				return asRO(bulk,"Attenzione: esercizio economico chiuso o in fase di chiusura.");

			return bulk;
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	/**
	 *
	 * Nome: Inizializzazione di una scrittura
	 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca di una scrittura in partita doppia
	 * Post: La scrittura viene restituita con inizializzato come Cds quello di scrivania
	 * <p>
	 * Nome: Inizializzazione di un movimento
	 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca di un movimento coge
	 * Post: Il movimento viene restituito con inizializzato come Cds quello di scrivania
	 * <p>
	 * Nome: Inizializzazione di un saldo
	 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca di un saldo coge
	 * Post: Il saldo viene restituito con inizializzato come Cds quello di scrivania
	 *
	 * @param userContext <code>UserContext</code>
	 * @param bulk <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> che devono essere inizializzati per ricerca
	 * @return <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> inizializzati per ricerca
	 *
	 */

	public OggettoBulk inizializzaBulkPerRicerca (UserContext userContext,OggettoBulk bulk) throws ComponentException
	{

		try
		{
			bulk = super.inizializzaBulkPerRicerca( userContext, bulk);
			if ( bulk instanceof Scrittura_partita_doppiaBulk )
			{
				Scrittura_partita_doppiaBulk scrittura = (Scrittura_partita_doppiaBulk) bulk;
				scrittura.setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( new CdsBulk( scrittura.getUo().getCd_unita_padre())));
				return scrittura;
			}
			else if ( bulk instanceof Saldo_cogeBulk )
			{
				((Saldo_cogeBulk)bulk).setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( ((Saldo_cogeBulk)bulk).getCds()));
			}
			else if ( bulk instanceof Movimento_cogeBulk )
			{
				((Movimento_cogeBulk)bulk).setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( ((Movimento_cogeBulk)bulk).getCds()));
			}


			return bulk;
		}
		catch ( Exception e )
		{
			throw handleException( bulk, e );
		}
	}
	/**
	 *
	 * Nome: Inizializzazione di una scrittura
	 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca libera di una scrittura in partita doppia
	 * Post: La scrittura viene restituita con inizializzato come Cds quello di scrivania
	 * <p>
	 * Nome: Inizializzazione di un movimento
	 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca libera di un movimento coge
	 * Post: Il movimento viene restituito con inizializzato come Cds quello di scrivania
	 * <p>
	 * Nome: Inizializzazione di un saldo
	 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca libera di un saldo coge
	 * Post: Il saldo viene restituito con inizializzato come Cds quello di scrivania
	 *
	 * @param userContext <code>UserContext</code>
	 * @param bulk <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> che devono essere inizializzati per ricerca
	 * @return <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> inizializzati per ricerca
	 *
	 */

	public OggettoBulk inizializzaBulkPerRicercaLibera (UserContext userContext,OggettoBulk bulk) throws ComponentException
	{

		try
		{
			bulk = super.inizializzaBulkPerRicercaLibera( userContext, bulk);
			if ( bulk instanceof Scrittura_partita_doppiaBulk )
			{
				Scrittura_partita_doppiaBulk scrittura = (Scrittura_partita_doppiaBulk) bulk;
				scrittura.setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( new CdsBulk( scrittura.getUo().getCd_unita_padre())));
				return scrittura;
			}
			else if ( bulk instanceof Saldo_cogeBulk )
			{
				((Saldo_cogeBulk)bulk).setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( ((Saldo_cogeBulk)bulk).getCds()));
			}
			else if ( bulk instanceof Movimento_cogeBulk )
			{
				((Movimento_cogeBulk)bulk).setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( ((Movimento_cogeBulk)bulk).getCds()));
			}


			return bulk;
		}
		catch ( Exception e )
		{
			throw handleException( bulk, e );
		}
	}
	/**
	 * Nome: Esercizio aperto
	 * Pre:  E' stata richiesta l'inizializzazione della data di contabilizzazione di una scrittura
	 * e l'esercizio contabile e' APERTO
	 * Post: La data di contabilizzzaione viene inizializzata con la data odierna
	 * <p>
	 * Nome: Esercizio chiuso priovvisoriamente
	 * Pre:  E' stata richiesta l'inizializzazione della data di contabilizzazione di una scrittura
	 * e l'esercizio contabile e' CHIUSO PROVVISORIO
	 * Post: La data di contabilizzzaione viene inizializzata con il 31/12/esercizio di scrivania
	 * <p>
	 * Nome: Esercizio in altro stato
	 * Pre:  E' stata richiesta l'inizializzazione della data di contabilizzazione di una scrittura
	 * e l'esercizio contabile ha stato diverso da APERTO o  CHIUSO PROVVISORIO
	 * Post: Una segnalazione di errore comunica all'utente l'impossibilità di creare una scrittura in partita doppia
	 *
	 * @param userContext <code>UserContext</code>
	 * @param scrittura   <code>Scrittura_partita_doppiaBulk</code>  la cui data deve essere inizializzata
	 */

	private void inizializzaDataContabilizzazione (UserContext userContext, Scrittura_partita_doppiaBulk scrittura ) throws PersistencyException, ComponentException, javax.ejb.EJBException
	{
		EsercizioBulk esercizio = (EsercizioBulk) getHome( userContext, EsercizioBulk.class ).findByPrimaryKey( new EsercizioBulk( ((CNRUserContext)userContext).getCd_cds(), ((CNRUserContext)userContext).getEsercizio()));
		if ( esercizio == null )
			throw new ApplicationException( "Attenzione esercizio non definito per il cds di scrivania!");
		/* Gennaro Borriello - (23/09/2004 10.33.19)
		 *	Err. 838 - Deve essere possibile poter registrare delle scritture coep/coan
		 *	anche se l'esercizio finanziario è chiuso.
		 */
		if ( !esercizio.getSt_apertura_chiusura().equals(EsercizioBulk.STATO_APERTO) &&
				!esercizio.getSt_apertura_chiusura().equals(EsercizioBulk.STATO_CHIUSO_DEF))
			throw new ApplicationException( "Attenzione esercizio non in stato aperto per il cds di scrivania!");
		scrittura.setDt_contabilizzazione( it.cnr.contab.doccont00.comp.DateServices.getDt_valida( userContext));
	}

	/**
	 *	Controllo se l'esercizio di scrivania e' aperto
	 * <p>
	 * Nome: Controllo chiusura esercizio
	 * Pre:  E' stata richiesta la creazione o modifica di una scrittura
	 * Post: Viene chiamata una stored procedure che restituisce
	 *		  -		'Y' se il campo stato della tabella CHIUSURA_COEP vale C
	 *		  -		'N' altrimenti
	 *		  Se l'esercizio e' chiuso e' impossibile proseguire
	 *
	 * @param  userContext <code>UserContext</code>

	 * @return boolean : TRUE se stato = C
	 *					  FALSE altrimenti
	 */
	private boolean isEsercizioChiuso(UserContext userContext) throws ComponentException
	{
		LoggableStatement cs;
		String status;

		try
		{
			cs = new LoggableStatement(getConnection(userContext), "{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
					+	"CNRCTB200.isChiusuraCoepDef(?,?)}",false,this.getClass());

			cs.registerOutParameter( 1, java.sql.Types.VARCHAR);
			cs.setObject( 2, CNRUserContext.getEsercizio(userContext)	);
			cs.setObject( 3, CNRUserContext.getCd_cds(userContext)		);

			cs.executeQuery();

			status = cs.getString(1);

			if(status.compareTo("Y")==0)
				return true;

		} catch (java.sql.SQLException ex) {
			throw handleException(ex);
		}

		return false;
	}
/* per le date salvate nel database come timestamp bisogna ridefinire la query nel modo seguente:
		TRUNC( dt_nel_db) operator 'GG/MM/YYYY'
*/

	protected void ridefinisciClausoleConTimestamp(UserContext userContext,CompoundFindClause clauses)
	{
		SimpleFindClause clause;
		for ( Iterator<SimpleFindClause> i = clauses.iterator(); i.hasNext(); )
		{
			clause = i.next();
			if ( clause.getPropertyName().equalsIgnoreCase( "dt_contabilizzazione" ) )
				if ( clause.getOperator() == SQLBuilder.ISNOTNULL || clause.getOperator() == SQLBuilder.ISNULL )
					clause.setSqlClause( "TRUNC( " + clause.getPropertyName() + ") " + SQLBuilder.getSQLOperator(clause.getOperator()) );
				else
					clause.setSqlClause( "TRUNC( " + clause.getPropertyName() + ") " + SQLBuilder.getSQLOperator(clause.getOperator()) + " ? ");

		}


	}
	/* per le date salvate nel database come timestamp bisogna ridefinire la query nel modo seguente:
            TRUNC( dt_nel_db) operator 'GG/MM/YYYY'
    */
	protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException
	{
		/* COMPORTAMENTO DI DEFAULT - INIZIO */
		if (clauses == null)
		{
			if (bulk != null)
				clauses = bulk.buildFindClauses(null);
		} else
			clauses = it.cnr.jada.persistency.sql.CompoundFindClause.and(clauses,bulk.buildFindClauses(Boolean.FALSE));
		/* COMPORTAMENTO DI DEFAULT - FINE */

		if (clauses != null)
			ridefinisciClausoleConTimestamp(userContext, clauses);

		if(bulk instanceof Movimento_cogeBulk)
		{
			for ( Iterator i = clauses.iterator(); i.hasNext(); )
			{
				SimpleFindClause clause = (SimpleFindClause) i.next();
				if ( clause.getPropertyName().equalsIgnoreCase( "scrittura.attiva" ))
					if (clause.getOperator() == SQLBuilder.ISNOTNULL || clause.getOperator() == SQLBuilder.ISNULL )
						clause.setSqlClause("SCRITTURA_PARTITA_DOPPIA.ATTIVA " + SQLBuilder.getSQLOperator(clause.getOperator()) );
					else
						clause.setSqlClause( "SCRITTURA_PARTITA_DOPPIA.ATTIVA " + SQLBuilder.getSQLOperator(clause.getOperator()) + "'" + clause.getValue() + "'");

				if ( clause.getPropertyName().equalsIgnoreCase( "scrittura.pg_scrittura_annullata" ))
					if (clause.getOperator() == SQLBuilder.ISNOTNULL || clause.getOperator() == SQLBuilder.ISNULL  )
						clause.setSqlClause("SCRITTURA_PARTITA_DOPPIA.PG_SCRITTURA_ANNULLATA " + SQLBuilder.getSQLOperator(clause.getOperator()) );
					else
						clause.setSqlClause( "SCRITTURA_PARTITA_DOPPIA.PG_SCRITTURA_ANNULLATA " + SQLBuilder.getSQLOperator(clause.getOperator()) + "'" + clause.getValue() + "'");
			}
		}

		return getHome(userContext,bulk).selectByClause(clauses);
	}
	/**
	 * esegue la seleziona un conto economico patrimoniale per un movimento coge
	 * <p>
	 * Nome: Seleziona conto per movimeto avere
	 * Pre:  E' stata richiesta la ricerca di un conto economico patrimoniale per un movimento avere
	 * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente ed inoltre le clausole che il conto
	 *       abbia esercizio uguale all'esercizio del movimento coge e il tipo sezione diverso da DARE
	 * <p>
	 * Nome: Seleziona conto per movimeto dare
	 * Pre:  E' stata richiesta la ricerca di un conto economico patrimoniale per un movimento dare
	 * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente ed inoltre le clausole che il conto
	 *       abbia esercizio uguale all'esercizio del movimento coge e il tipo sezione diverso da AVERE
	 *
	 * @param userContext <code>UserContext</code>
	 * @param movimento <code>Movimento_cogeBulk</code> per cui ricercare il conto
	 * @param conto <code>ContoBulk</code> conto econom.patrimoniale da ricercare
	 * @param clauses <code>CompoundFindClause</code> clausole specificate dall'utente
	 * @return SQLBuilder
	 *
	 */

	public SQLBuilder selectContoByClause (UserContext userContext, Movimento_cogeBulk movimento, ContoBulk conto, CompoundFindClause clauses ) throws ComponentException
	{
		SQLBuilder sql = getHome( userContext, conto.getClass()).createSQLBuilder();
		sql.addClause( clauses );
		if ( Movimento_cogeBulk.SEZIONE_AVERE.equals( movimento.getSezione()))
			sql.addClause( FindClause.AND, "ti_sezione", SQLBuilder.NOT_EQUALS, ContoHome.SEZIONE_DARE);
		else if ( Movimento_cogeBulk.SEZIONE_DARE.equals( movimento.getSezione()))
			sql.addClause( FindClause.AND, "ti_sezione", SQLBuilder.NOT_EQUALS, ContoHome.SEZIONE_AVERE);
		sql.addClause( FindClause.AND, "esercizio", SQLBuilder.EQUALS, movimento.getEsercizio());
		return sql;
	}
	/**
	 * esegue la seleziona un conto economico patrimoniale per un saldo coge
	 * <p>
	 * Nome: Seleziona conto per saldo coge
	 * Pre:  E' stata richiesta la ricerca di un conto economico patrimoniale per un saldo coge
	 * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente ed inoltre le clausole che il conto
	 *       abbia esercizio uguale all'esercizio del saldo coge
	 *
	 * @param userContext <code>UserContext</code>
	 * @param saldo <code>Saldo_cogeBulk</code> per cui ricercare il conto
	 * @param conto <code>ContoBulk</code> conto econom.patrimoniale da ricercare
	 * @param clauses <code>CompoundFindClause</code> clausole specificate dall'utente
	 * @return SQLBuilder
	 *
	 */

	public SQLBuilder selectContoByClause (UserContext userContext, Saldo_cogeBulk saldo, ContoBulk conto, CompoundFindClause clauses ) throws ComponentException
	{
		SQLBuilder sql = getHome( userContext, conto.getClass()).createSQLBuilder();
		sql.addClause( clauses );
		sql.addClause( FindClause.AND, "esercizio", SQLBuilder.EQUALS, saldo.getEsercizio());
		return sql;
	}


	/**
	 * esegue la seleziona un terzo per una scrittura
	 * <p>
	 * Nome: Seleziona terzo per scrittura
	 * Pre:  E' stata richiesta la ricerca di un terzo per una scrittura in partita doppia
	 * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente ed inoltre le clausole che il terzo
	 *       non abbia data di fine rapporto < della data odierna
	 *
	 * @param userContext <code>UserContext</code>
	 * @param scrittura <code>Scrittura_partita_doppiaBulk</code> per cui ricercare il terzo
	 * @param terzo <code>TerzoBulk</code> terzo da ricercare
	 * @param clauses <code>CompoundFindClause</code> clausole specificate dall'utente
	 * @return SQLBuilder
	 *
	 */

	public SQLBuilder selectTerzoByClause (UserContext userContext, Scrittura_partita_doppiaBulk scrittura, TerzoBulk terzo, CompoundFindClause clauses ) throws ComponentException
	{
		SQLBuilder sql = getHome( userContext, terzo.getClass(), "V_TERZO_CF_PI").createSQLBuilder();
		sql.addClause( clauses );
		sql.addSQLClause( "AND", "(DT_FINE_RAPPORTO >= SYSDATE OR DT_FINE_RAPPORTO IS NULL)");
		return sql;
	}
	/**
	 * valida la correttezza dell'associazione fra anagrafico e conto
	 * <p>
	 * Nome: Nessuna associzione fra anagrafico-conto
	 * Pre:  Per la scrittura in partita doppia e' stato selezionato un terzo le cui caratteristiche anagrafiche non
	 *       sono state messe in relazione con neanche un conto definito per la scrittura (sia in avere che in dare)
	 * Post: Una segnalazione di errore viene restituita all'utente
	 * <p>
	 * Nome: Almeno un'associzione fra anagrafico-conto
	 * Pre:  Per la scrittura in partita doppia e' stato selezionato un terzo le cui caratteristiche anagrafiche
	 *       sono state messe in relazione con un conto definito per la scrittura (in avere o in dare)
	 * Post: La scrittura supera la validazione anagrafica-conto
	 *
	 * @param userContext <code>UserContext</code>
	 * @param scrittura <code>Scrittura_partita_doppiaBulk</code> da validare
	 *
	 */

	private void validaAssociazioneAnagConto (UserContext userContext, Scrittura_partita_doppiaBulk scrittura ) throws ComponentException, PersistencyException
	{
		List<Ass_anag_voce_epBulk> result = ((Ass_anag_voce_epHome) getHome(userContext, Ass_anag_voce_epBulk.class)).findAssociazioniPerScrittura( scrittura );
		if ( result == null || result.size() == 0 )
			throw new ApplicationException( "Non esiste nessuna associazione fra l'anagrafica selezionata e i conti economico-patrimoniali" );
		Ass_anag_voce_epBulk ass = result.get(0);
		Movimento_cogeBulk movimento;
		for (Movimento_cogeBulk movimento_cogeBulk : scrittura.getMovimentiAvereColl()) {
			if (movimento_cogeBulk.getConto().equalsByPrimaryKey(ass.getConto()))
				return;
		}
		for (Movimento_cogeBulk movimento_cogeBulk : scrittura.getMovimentiDareColl()) {
			if (movimento_cogeBulk.getConto().equalsByPrimaryKey(ass.getConto()))
				return;
		}
		throw new ApplicationException( "Non esiste nessuna associazione fra l'anagrafica selezionata e i conti economico-patrimoniali" );
	}

	/**
	 * valida la correttezza di un oggetto di tipo <code>Scrittura_partita_doppiaBulk</code> passato in ingresso.
	 * <p>
	 * Nome: validazione superata
	 * Pre:  La scrittura supera la validazione ( metodo validaScrittura)
	 * Post: La scrittura può essere inserita nel database
	 * <p>
	 * Nome: validazione non superata
	 * Pre:  La scrittura non supera la validazione ( metodo validaScrittura)
	 * Post: Una segnalazione di errore viene restituita all'utente
	 * <p>
	 *
	 * @param userContext <code>UserContext</code>
	 * @param bulk <code>Scrittura_partita_doppiaBulk</code> da validare
	 *
	 */

	protected void validaCreaModificaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException
	{
		super.validaCreaModificaConBulk( userContext, bulk );
		try
		{
			validaScrittura( userContext, (Scrittura_partita_doppiaBulk) bulk );
		}
		catch ( Exception e )
		{
			throw handleException( e )	;
		}

	}
	/**
	 * valida la correttezza di un oggetto di tipo <code>Scrittura_partita_doppiaBulk</code> passato in ingresso.
	 * <p>
	 * Nome: Nessun movimento avere
	 * Pre:  Per la scrittura in partita doppia non e' stato definito nessun movimento avere
	 * Post: Una segnalazione di errore viene restituita all'utente
	 * <p>
	 * Nome: Nessun movimento dare
	 * Pre:  Per la scrittura in partita doppia non e' stato definito nessun movimento dare
	 * Post: Una segnalazione di errore viene restituita all'utente
	 * <p>
	 * Nome: Somma movimenti dare diversa da somma movimenti avere
	 * Pre:  La somma degli importi dei movimenti dare e' diversa dalla somma degli importi dei movimenti avere
	 * Post: Una segnalazione di errore viene restituita all'utente
	 * <p>
	 * Nome: Terzo
	 * Pre:  La scrittura e' stata definita per un terzo e la validazione terzo-conti (metodo 'validaAssociazioneAnagConto')
	 *       non e' stata superata
	 * Post: Una segnalazione di errore viene restituita all'utente

	 * Nome: Tutti i controlli superati
	 * Pre:  La scrittura ha almeno un movimento avere, ha almeno un movimento dare e la somma degli importi dei
	 *       movimenti avere e dare coincide. Inoltre non e' stato specificato un terzo oppure e' stato specificato un
	 *       terzo e tale terzo ha superato la validazione coi conti.
	 * Post: La scrittura supera la validazione e può pertanto essere salvata
	 *
	 * @param userContext <code>UserContext</code>
	 * @param scrittura <code>Scrittura_partita_doppiaBulk</code> da validare
	 *
	 */

	private void validaScrittura (UserContext userContext, Scrittura_partita_doppiaBulk scrittura ) throws ComponentException, PersistencyException
	{
		if ( scrittura.getMovimentiAvereColl().size() == 0 )
			throw new ApplicationException( "E' necessario definire almeno un movimento Avere");
		if ( scrittura.getMovimentiDareColl().size() == 0 )
			throw new ApplicationException( "E' necessario definire almeno un movimento Dare");
		if ( scrittura.getImTotaleAvere().compareTo( scrittura.getImTotaleDare()) != 0 )
			throw new ApplicationException( "Gli importi totali dei movimenti Dare e Avere devono essere uguali");

		if ( scrittura.getTerzo() != null && scrittura.getTerzo().getCd_terzo() != null && !scrittura.getTerzo().equalsByPrimaryKey( getTerzoNullo()))
			validaAssociazioneAnagConto( userContext, scrittura );



	}
	/* (non-Javadoc)
	 * @see it.cnr.jada.comp.IPrintMgr#inizializzaBulkPerStampa(it.cnr.jada.UserContext, it.cnr.jada.bulk.OggettoBulk)
	 */
	public OggettoBulk inizializzaBulkPerStampa(UserContext usercontext,OggettoBulk oggettobulk) throws ComponentException {
		if (oggettobulk instanceof Stampa_elenco_movimentiBulk)
			return  inizializzaBulkPerStampa(usercontext,(Stampa_elenco_movimentiBulk)oggettobulk);
		return oggettobulk;
	}

	public OggettoBulk inizializzaBulkPerStampa(UserContext usercontext,Stampa_elenco_movimentiBulk stampa) throws ComponentException {
		//	Imposta l'Esercizio come quello di scrivania
		stampa.setEsercizio(CNRUserContext.getEsercizio(usercontext));


		try{
			String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(usercontext);

			CdsHome cdsHome = (CdsHome)getHome(usercontext, CdsBulk.class);
			CdsBulk cds = (CdsBulk)cdsHome.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));
			Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( usercontext, Unita_organizzativa_enteBulk.class).findAll().get(0);

			if (!cds.getCd_unita_organizzativa().equals(ente.getCd_unita_padre())){
				stampa.setCdsForPrint(cds);
				stampa.setCdsForPrintEnabled(true);
			} else {
				stampa.setCdsForPrint(new CdsBulk());
				stampa.setCdsForPrintEnabled(false);
			}

		} catch (it.cnr.jada.persistency.PersistencyException pe){
			throw new ComponentException(pe);
		}

		try{
			String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(usercontext);

			Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHome(usercontext, Unita_organizzativaBulk.class);
			Unita_organizzativaBulk uo = (Unita_organizzativaBulk)uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo_scrivania));

			if (!uo.isUoCds()){
				stampa.setUoForPrint(uo);
				stampa.setUoForPrintEnabled(true);
			} else {
				stampa.setUoForPrint(new Unita_organizzativaBulk());
				stampa.setUoForPrintEnabled(false);
			}

		} catch (it.cnr.jada.persistency.PersistencyException pe){
			throw new ComponentException(pe);
		}

		return stampa;
	}

	public OggettoBulk stampaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		if (oggettobulk instanceof Stampa_elenco_movimentiBulk)
			return  stampaConBulk(usercontext,(Stampa_elenco_movimentiBulk)oggettobulk);
		return oggettobulk;
	}

	public OggettoBulk stampaConBulk(UserContext usercontext, Stampa_elenco_movimentiBulk stampa) throws ComponentException {
		if ( stampa.getContoForPrint()==null || stampa.getContoForPrint().getCd_voce_ep() == null)
			throw new ApplicationException( "E' necessario inserire un Conto");
		if ( stampa.getattiva()==null )
			throw new ApplicationException( "E' necessario valorizzare il campo 'Attiva'");
		if ( stampa.gettipologia()==null )
			throw new ApplicationException( "E' necessario valorizzare il campo 'Tipologia'");
		return stampa;
	}
	public SQLBuilder selectContoForPrintByClause (UserContext userContext, Stampa_elenco_movimentiBulk stampa, ContoBulk conto, CompoundFindClause clauses ) throws ComponentException
	{
		SQLBuilder sql = getHome( userContext, conto.getClass()).createSQLBuilder();
		sql.addSQLClause(FindClause.AND,"ESERCIZIO",SQLBuilder.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addClause( clauses );
		return sql;
	}
	public SQLBuilder selectTerzoForPrintByClause (UserContext userContext, Stampa_elenco_movimentiBulk stampa, TerzoBulk terzo, CompoundFindClause clauses ) throws ComponentException
	{
		SQLBuilder sql = getHome( userContext, terzo.getClass(),"V_TERZO_CF_PI").createSQLBuilder();
		sql.addClause( clauses );
		return sql;
	}
	public SQLBuilder selectUoForPrintByClause (UserContext userContext, Stampa_elenco_movimentiBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clauses ) throws ComponentException
	{
		SQLBuilder sql = getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
		sql.addSQLClause(FindClause.AND, "ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
		sql.addSQLClause(FindClause.AND, "CD_UNITA_PADRE", SQLBuilder.EQUALS, stampa.getCdsForPrint().getCd_unita_organizzativa());
		sql.addClause( clauses );
		return sql;
	}
	public SQLBuilder selectCdsForPrintByClause (UserContext userContext, Stampa_elenco_movimentiBulk stampa, CdsBulk cds, CompoundFindClause clauses ) throws ComponentException
	{
		SQLBuilder sql = getHome(userContext, cds.getClass(), "V_CDS_VALIDO").createSQLBuilder();
		sql.addClause( clauses );
		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		return sql;

	}

	public Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppia(UserContext userContext, IDocumentoCogeBulk doccoge) throws ComponentException, ScritturaPartitaDoppiaNotRequiredException {
		try {
			if (doccoge.getTipoDocumentoEnum().isGenericoStipendiSpesa())
				throw new ScritturaPartitaDoppiaNotRequiredException("Scrittura Economica non prevista per il documento generico di pagamento stipendi. " +
						"La scrittura principale viene eseguita sul compenso associato.");
			else if (doccoge.getTipoDocumentoEnum().isGenericoCoriVersamentoSpesa())
				throw new ScritturaPartitaDoppiaNotRequiredException("Scrittura Economica non prevista per il documento generico di versamento contributi/ritenute. " +
						"La scrittura principale viene eseguita sul compenso associato.");
			else if (doccoge.getTipoDocumentoEnum().isChiusuraFondo())
				throw new ScritturaPartitaDoppiaNotRequiredException("Scrittura Economica non prevista per il documento generico di chiusura fondo economale. " +
						"La scrittura principale viene eseguita sulla reversale associata.");
			else if (!doccoge.getTipoDocumentoEnum().isScritturaEconomicaRequired())
				throw new ScritturaPartitaDoppiaNotRequiredException("Scrittura Economica non prevista per la tipologia di documento selezionato.");
			else if (doccoge.getTipoDocumentoEnum().isAnticipo())
				return this.proposeScritturaPartitaDoppiaAnticipo(userContext, (AnticipoBulk) doccoge);
			else if (doccoge.getTipoDocumentoEnum().isRimborso())
				return this.proposeScritturaPartitaDoppiaRimborso(userContext, (RimborsoBulk) doccoge);
			else if (doccoge.getTipoDocumentoEnum().isMissione())
				return this.proposeScritturaPartitaDoppiaMissione(userContext, (MissioneBulk) doccoge);
			else if (doccoge.getTipoDocumentoEnum().isCompenso()) {
				if (((CompensoBulk) doccoge).getFl_compenso_stipendi())
					return this.proposeScritturaPartitaDoppiaCompensoStipendi(userContext, (CompensoBulk) doccoge);
				else
					return this.proposeScritturaPartitaDoppiaCompenso(userContext, (CompensoBulk) doccoge);
			} else if (doccoge.getTipoDocumentoEnum().isAperturaFondo())
				return this.proposeScritturaPartitaDoppiaAperturaFondo(userContext, (Documento_genericoBulk) doccoge);
			else if (doccoge.getTipoDocumentoEnum().isDocumentoPassivo() || doccoge.getTipoDocumentoEnum().isDocumentoAttivo())
				return this.proposeScritturaPartitaDoppiaDocumento(userContext, (IDocumentoAmministrativoBulk) doccoge);
			else if (doccoge.getTipoDocumentoEnum().isLiquidazioneIva())
				return this.proposeScritturaPartitaDoppiaLiquidazioneUo(userContext, (Liquidazione_ivaBulk) doccoge);
			else if (doccoge.getTipoDocumentoEnum().isMandato())
				return this.proposeScritturaPartitaDoppiaMandato(userContext, (MandatoBulk) doccoge);
			else if (doccoge.getTipoDocumentoEnum().isReversale())
				return this.proposeScritturaPartitaDoppiaReversale(userContext, (ReversaleBulk) doccoge);
			throw new ApplicationException("Scrittura Economica non gestita per la tipologia di documento "+doccoge.getCd_tipo_doc()+" selezionato.");
		} catch (ApplicationException|ApplicationRuntimeException e) {
			throw new NoRollbackException(e);
		}
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaDocumento(UserContext userContext, IDocumentoAmministrativoBulk docamm) throws ComponentException, ScritturaPartitaDoppiaNotRequiredException {
		List<TestataPrimaNota> testataPrimaNotaList = this.proposeTestataPrimaNotaDocumento(userContext, docamm);
		return Optional.of(testataPrimaNotaList).map(el->this.generaScrittura(userContext, docamm, el, true)).orElse(null);
	}

	private List<TestataPrimaNota> proposeTestataPrimaNotaDocumento(UserContext userContext, IDocumentoAmministrativoBulk docamm) throws ComponentException, ScritturaPartitaDoppiaNotRequiredException {
		List<TestataPrimaNota> testataPrimaNotaList = new ArrayList<>();
		final boolean isFatturaPassivaDaOrdini = Optional.of(docamm).filter(Fattura_passivaBulk.class::isInstance).map(Fattura_passivaBulk.class::cast).filter(Fattura_passivaBulk::isDaOrdini).isPresent();

		//Le fatture generate da compenso non creano scritture di prima nota in quanto create direttamente dal compenso stesso
		if (Optional.of(docamm).filter(Fattura_passivaBulk.class::isInstance).map(Fattura_passivaBulk.class::cast).map(Fattura_passivaBulk::isGenerataDaCompenso).orElse(Boolean.FALSE))
			throw new ScritturaPartitaDoppiaNotRequiredException("Scrittura Economica non necessaria in quanto fattura collegata a compenso. La scrittura di prima nota viene creata direttamente dal compenso stesso.");

		List<DettaglioFinanziario> righeDettFin = this.getRigheDocamm(userContext, docamm).stream()
				.map(rigaDocAmm-> {
					//Attenzione: recupero il terzo dal docamm perchè sulle righe potrebbe non essere valorizzato
					TerzoBulk terzo;
					IDocumentoAmministrativoBulk partita = docamm;
					if (docamm instanceof Fattura_passivaBulk) {
						terzo = ((Fattura_passivaBulk) docamm).getFornitore();
						if (rigaDocAmm instanceof Nota_di_credito_rigaBulk)
							partita = ((Nota_di_credito_rigaBulk)rigaDocAmm).getRiga_fattura_associata().getFather();
					} else if (docamm instanceof Fattura_attivaBulk)
						terzo = ((Fattura_attivaBulk) docamm).getCliente();
					else if (docamm instanceof OrdineAcqBulk)
						terzo = ((OrdineAcqBulk) docamm).getFornitore();
					else {
						terzo = rigaDocAmm.getTerzo();
						if (TipoDocumentoEnum.fromValue(docamm.getCd_tipo_doc()).isDocumentoGenericoAttivo()) {
							((Documento_genericoBulk) docamm).setTi_entrate_spese(Documento_genericoBulk.ENTRATE);
							((Documento_generico_rigaBulk) rigaDocAmm).setDocumento_generico((Documento_genericoBulk)docamm);
						} else if (TipoDocumentoEnum.fromValue(docamm.getCd_tipo_doc()).isDocumentoGenericoPassivo()) {
							((Documento_genericoBulk) docamm).setTi_entrate_spese(Documento_genericoBulk.SPESE);
							((Documento_generico_rigaBulk) rigaDocAmm).setDocumento_generico((Documento_genericoBulk)docamm);
						}
					}

					if (Optional.ofNullable(rigaDocAmm.getScadenzaDocumentoContabile()).filter(Obbligazione_scadenzarioBulk.class::isInstance).isPresent()) {
						ObbligazioneBulk obbligazioneDB = Optional.of(rigaDocAmm.getScadenzaDocumentoContabile().getFather()).filter(ObbligazioneBulk.class::isInstance).map(ObbligazioneBulk.class::cast)
								.filter(el -> el.getCd_elemento_voce() != null).orElseGet(() -> {
									try {
										ObbligazioneHome obbligazionehome = (ObbligazioneHome) getHome(userContext, ObbligazioneBulk.class);
										return (ObbligazioneBulk) obbligazionehome.findByPrimaryKey((ObbligazioneBulk) rigaDocAmm.getScadenzaDocumentoContabile().getFather());
									} catch (ComponentException | PersistencyException e) {
										throw new DetailedRuntimeException(e);
									}
								});
						return new DettaglioFinanziario(docamm, partita, rigaDocAmm.getCd_terzo(), obbligazioneDB.getElemento_voce(), rigaDocAmm.getDt_da_competenza_coge(), rigaDocAmm.getDt_a_competenza_coge(),
								rigaDocAmm.getIm_imponibile(), rigaDocAmm.getIm_iva());
					}
					if (Optional.ofNullable(rigaDocAmm.getScadenzaDocumentoContabile()).filter(Accertamento_scadenzarioBulk.class::isInstance).isPresent()) {
						AccertamentoBulk accertamentoDB = Optional.of(rigaDocAmm.getScadenzaDocumentoContabile().getFather()).filter(AccertamentoBulk.class::isInstance).map(AccertamentoBulk.class::cast)
								.filter(el -> el.getCd_elemento_voce() != null).orElseGet(() -> {
									try {
										AccertamentoHome home = (AccertamentoHome) getHome(userContext, AccertamentoBulk.class);
										return (AccertamentoBulk) home.findByPrimaryKey((AccertamentoBulk) rigaDocAmm.getScadenzaDocumentoContabile().getFather());
									} catch (ComponentException | PersistencyException e) {
										throw new DetailedRuntimeException(e);
									}
								});
						return new DettaglioFinanziario(docamm, partita, terzo.getCd_terzo(),
								new Elemento_voceBulk(accertamentoDB.getCd_elemento_voce(), accertamentoDB.getEsercizio(), accertamentoDB.getTi_appartenenza(), accertamentoDB.getTi_gestione()),
								rigaDocAmm.getDt_da_competenza_coge(), rigaDocAmm.getDt_a_competenza_coge(),
								rigaDocAmm.getIm_imponibile(), rigaDocAmm.getIm_iva());
					}
					if (!Optional.ofNullable(rigaDocAmm.getScadenzaDocumentoContabile()).isPresent() && docamm instanceof Documento_genericoBulk && docamm.getTipoDocumentoEnum().isGenericoEntrata() &&
							(((Documento_generico_rigaBulk)rigaDocAmm).getAccertamento_scadenziario()!=null)) {
						AccertamentoBulk accertamentoDB = Optional.of((((Documento_generico_rigaBulk)rigaDocAmm).getAccertamento_scadenziario().getAccertamento()))
								.filter(el -> el.getCd_elemento_voce() != null).orElseGet(() -> {
									try {
										AccertamentoHome home = (AccertamentoHome) getHome(userContext, AccertamentoBulk.class);
										return (AccertamentoBulk) home.findByPrimaryKey((((Documento_generico_rigaBulk)rigaDocAmm).getAccertamento_scadenziario().getAccertamento()));
									} catch (ComponentException | PersistencyException e) {
										throw new DetailedRuntimeException(e);
									}
								});
						return new DettaglioFinanziario(docamm, partita, terzo.getCd_terzo(),
								new Elemento_voceBulk(accertamentoDB.getCd_elemento_voce(), accertamentoDB.getEsercizio(), accertamentoDB.getTi_appartenenza(), accertamentoDB.getTi_gestione()),
								rigaDocAmm.getDt_da_competenza_coge(), rigaDocAmm.getDt_a_competenza_coge(),
								rigaDocAmm.getIm_imponibile(), rigaDocAmm.getIm_iva());
					}
					return null;
				}).collect(Collectors.toList());

		/*
		  Le fatture Commerciali con autofattura sono praticamente tutte le fatture Commerciali con Iva positiva
		  Quindi se isCommercialeWithAutofattura=true allora devo procedere a registrare IVA
		  Il flag registraIva viene quindi impostatao a true
		 */
		final boolean isCommercialeWithAutofattura = this.hasAutofattura(userContext, docamm);

		final boolean isSplitPayment;
		final boolean registraIva;
		if (docamm instanceof Fattura_passivaBulk) {
			isSplitPayment = ((Fattura_passivaBulk) docamm).getFl_split_payment();
			registraIva = Boolean.TRUE;
		} else if (docamm instanceof Fattura_attivaBulk) {
			isSplitPayment = ((Fattura_attivaBulk)docamm).getFl_liquidazione_differita();
			registraIva = !isSplitPayment;
		} else {
			isSplitPayment = Boolean.FALSE;
			registraIva = Boolean.TRUE;
		}

		final String cdCoriIva, cdCoriIvaSplit;
		final Voce_epBulk aContoIva, aContoIvaSplit;
		try {
			cdCoriIva = this.findCodiceTributoIva(userContext);
			cdCoriIvaSplit = isSplitPayment?this.findCodiceTributoIvaSplit(userContext):null;

			aContoIva = registraIva?this.findContoIva(userContext, docamm):null;
			aContoIvaSplit = registraIva && isSplitPayment?this.findContoIvaSplit(userContext, docamm):null;
		} catch (ComponentException|RemoteException|PersistencyException e) {
			throw new ApplicationRuntimeException(e);
		}

		Map<Integer, Map<Timestamp, Map<Timestamp, List<DettaglioFinanziario>>>> mapTerzo =
				righeDettFin.stream().collect(Collectors.groupingBy(DettaglioFinanziario::getCdTerzo,
						Collectors.groupingBy(DettaglioFinanziario::getDtDaCompetenzaCoge,
							Collectors.groupingBy(DettaglioFinanziario::getDtACompetenzaCoge))));

		mapTerzo.keySet().forEach(aCdTerzo -> mapTerzo.get(aCdTerzo).keySet().forEach(aDtDaCompCoge -> mapTerzo.get(aCdTerzo).get(aDtDaCompCoge).keySet().forEach(aDtACompCoge -> {
			List<DettaglioFinanziario> righeDettFinTerzo = mapTerzo.get(aCdTerzo).get(aDtDaCompCoge).get(aDtACompCoge);
			TestataPrimaNota testataPrimaNota = new TestataPrimaNota(aDtDaCompCoge, aDtACompCoge);
			testataPrimaNotaList.add(testataPrimaNota);

			Map<Integer, Map<String, Map<String, Map<String, List<DettaglioFinanziario>>>>> mapVoce =
					righeDettFinTerzo.stream().collect(Collectors.groupingBy(rigaDettFin->rigaDettFin.getElementoVoce().getEsercizio(),
							Collectors.groupingBy(rigaDettFin->rigaDettFin.getElementoVoce().getTi_appartenenza(),
									Collectors.groupingBy(rigaDettFin->rigaDettFin.getElementoVoce().getTi_gestione(),
											Collectors.groupingBy(rigaDettFin->rigaDettFin.getElementoVoce().getCd_elemento_voce())))));

			mapVoce.keySet().forEach(aEseVoce -> mapVoce.get(aEseVoce).keySet().forEach(aTiAppartenenza -> mapVoce.get(aEseVoce).get(aTiAppartenenza).keySet().forEach(aTiGestione -> mapVoce.get(aEseVoce).get(aTiAppartenenza).get(aTiGestione).keySet().forEach(aCdVoce -> {
				List<DettaglioFinanziario> righeDettFinVoce = mapVoce.get(aEseVoce).get(aTiAppartenenza).get(aTiGestione).get(aCdVoce);

				//suddivido per partita.... che potrebbe essere differente come nel caso di note credito/debito
				Map<String, Map<String, Map<String, Map<Integer, Map<Long, List<DettaglioFinanziario>>>>>> mapPartita =
					righeDettFinVoce.stream().collect(Collectors.groupingBy(rigaDettFin->rigaDettFin.getPartita().getCd_tipo_doc(),
						Collectors.groupingBy(rigaDettFin->rigaDettFin.getPartita().getCd_cds(),
							Collectors.groupingBy(rigaDettFin->rigaDettFin.getPartita().getCd_uo(),
								Collectors.groupingBy(rigaDettFin->rigaDettFin.getPartita().getEsercizio(),
									Collectors.groupingBy(rigaDettFin->rigaDettFin.getPartita().getPg_doc()))))));

				mapPartita.keySet().forEach(aCd_tipo_doc ->
					mapPartita.get(aCd_tipo_doc).keySet().forEach(aCd_cds ->
						mapPartita.get(aCd_tipo_doc).get(aCd_cds).keySet().forEach(aCd_uo ->
							mapPartita.get(aCd_tipo_doc).get(aCd_cds).get(aCd_uo).keySet().forEach(aEsercizioDoc ->
								mapPartita.get(aCd_tipo_doc).get(aCd_cds).get(aCd_uo).get(aEsercizioDoc).keySet().forEach(aPg_doc -> {
					try {
						List<DettaglioFinanziario> righeDettFinVocePartita = mapPartita.get(aCd_tipo_doc).get(aCd_cds).get(aCd_uo).get(aEsercizioDoc).get(aPg_doc);

						BigDecimal imImponibile = righeDettFinVocePartita.stream().map(DettaglioFinanziario::getImImponibile).reduce(BigDecimal.ZERO, BigDecimal::add);
						BigDecimal imIva = righeDettFinVocePartita.stream()
								.map(rigaDettFin -> Optional.ofNullable(rigaDettFin.getImImposta()).orElse(BigDecimal.ZERO))
								.reduce(BigDecimal.ZERO, BigDecimal::add);

						DettaglioFinanziario rigaDettFinVocePartita = righeDettFinVocePartita.stream().findAny().get();
						IDocumentoAmministrativoBulk partita = rigaDettFinVocePartita.getPartita();

						//Registro Imponibile Fattura
						Pair<Voce_epBulk, Voce_epBulk> pairContoCosto = this.findPairCosto(userContext, rigaDettFinVocePartita);
						if (!isFatturaPassivaDaOrdini)
							testataPrimaNota.openDettaglioCostoRicavo(docamm, pairContoCosto.getFirst().getCd_voce_ep(), imImponibile);
						testataPrimaNota.openDettaglioPatrimonialePartita(docamm, partita, pairContoCosto.getSecond().getCd_voce_ep(), imImponibile, aCdTerzo);

						//Il flag registraIva è sempre impostato a true se fattura istituzionale o isCommercialeWithAutofattura
						if (imIva.compareTo(BigDecimal.ZERO)!=0 && registraIva) {
							if (isCommercialeWithAutofattura || docamm instanceof Fattura_attivaBulk)
								testataPrimaNota.openDettaglioIva(docamm, partita, aContoIva.getCd_voce_ep(), imIva, aCdTerzo, cdCoriIva);
							else { //è una fattura istituzionale e quindi l'IVA va a costo
								if (!isFatturaPassivaDaOrdini)
									testataPrimaNota.openDettaglioCostoRicavo(docamm, pairContoCosto.getFirst().getCd_voce_ep(), imIva);
							}
							testataPrimaNota.openDettaglioPatrimonialePartita(docamm, partita, pairContoCosto.getSecond().getCd_voce_ep(), imIva, aCdTerzo);

							//Se intraUE o extraUE sposto l'IVA anzichè darla al Fornitore (quindi chiudo il debito) la rilevo come debito verso Erario
							if (Optional.of(docamm).filter(Fattura_passivaBulk.class::isInstance).map(Fattura_passivaBulk.class::cast)
									.map(el->el.getFl_intra_ue() || el.getFl_extra_ue()).orElse(Boolean.FALSE)) {
								testataPrimaNota.closeDettaglioPatrimonialePartita(docamm, partita, pairContoCosto.getSecond().getCd_voce_ep(), imIva, aCdTerzo, true);
								testataPrimaNota.closeDettaglioIva(docamm, partita, aContoIva.getCd_voce_ep(), imIva, aCdTerzo, cdCoriIva);
							}

							if (isSplitPayment) {
								//Rilevo il conto IVA Credito/Debito di tipo SPLIT (a secondo se doc attivo o passivo) e lo compenso con il debito verso il fornitore
								testataPrimaNota.closeDettaglioIvaSplit(docamm, partita, aContoIvaSplit.getCd_voce_ep(), imIva, aCdTerzo, cdCoriIvaSplit);
								testataPrimaNota.closeDettaglioPatrimonialePartita(docamm, partita, pairContoCosto.getSecond().getCd_voce_ep(), imIva, aCdTerzo, true);
							}
						}
					} catch (ComponentException|PersistencyException|RemoteException e) {
						throw new ApplicationRuntimeException(e);
					}
				})))));
			}))));

			if (isFatturaPassivaDaOrdini){
				Fattura_passivaBulk fattura = Optional.of(docamm).map(Fattura_passivaBulk.class::cast).get();
				final BulkList<FatturaOrdineBulk> listaFatturaOrdini = Optional.ofNullable(fattura.getFattura_passiva_ordini())
						.orElse(new BulkList());
				final List<OrdineAcqConsegnaBulk> listaConsegne = listaFatturaOrdini.stream().map(FatturaOrdineBulk::getOrdineAcqConsegna).collect(Collectors.toList());

				Map<Integer, Map<String, List<FatturaOrdineBulk>>> mapConto =
					listaFatturaOrdini.stream().collect(Collectors.groupingBy(fatturaOrdineBulk->fatturaOrdineBulk.getOrdineAcqConsegna().getContoBulk().getEsercizio(),
								Collectors.groupingBy(fatturaOrdineBulk2->fatturaOrdineBulk2.getOrdineAcqConsegna().getContoBulk().getCd_voce_ep())));

				mapConto.keySet().forEach(aEseConto -> mapConto.get(aEseConto).keySet().forEach(conto -> {
					List<FatturaOrdineBulk> righeFatturaOrdine = mapConto.get(aEseConto).get(conto);

					BigDecimal imImponibile = righeFatturaOrdine.stream().map(FatturaOrdineBulk::getImponibilePerRigaFattura)
							.reduce(BigDecimal.ZERO, BigDecimal::add);
					testataPrimaNota.openDettaglioCostoRicavo(docamm, conto, imImponibile);

					BigDecimal imIva = righeFatturaOrdine.stream().map(FatturaOrdineBulk::getIvaPerRigaFattura)
							.reduce(BigDecimal.ZERO, BigDecimal::add);
					if (imIva.compareTo(BigDecimal.ZERO)!=0)
						testataPrimaNota.openDettaglioCostoRicavo(docamm, conto, imIva);
				}));
			}
		})));
		return testataPrimaNotaList;
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaCompenso(UserContext userContext, CompensoBulk compenso) throws ComponentException, ScritturaPartitaDoppiaNotRequiredException {
		try {
			String descCompenso = compenso.getEsercizio() + "/" + compenso.getCd_cds() + "/" + compenso.getPg_compenso();

			List<Contributo_ritenutaBulk> righeCori = Optional.ofNullable(compenso.getChildren()).orElseGet(()->{
				try {
					Contributo_ritenutaHome home = (Contributo_ritenutaHome) getHome(userContext, Contributo_ritenutaBulk.class);
					return (java.util.List<Contributo_ritenutaBulk>)home.loadContributiRitenute(compenso);
				} catch (ComponentException | PersistencyException e) {
					throw new DetailedRuntimeException(e);
				}
			});

			Optional<MissioneBulk> optMissione = Optional.ofNullable(compenso.getMissione()).map(missione->
				Optional.of(missione).filter(el->el.getCrudStatus()!=OggettoBulk.UNDEFINED).orElseGet(()-> {
					try {
						MissioneHome home = (MissioneHome) getHome(userContext, MissioneBulk.class);
						return (MissioneBulk)home.findByPrimaryKey(compenso.getMissione());
					} catch (ComponentException | PersistencyException e) {
						throw new DetailedRuntimeException(e);
					}
				}));

			BigDecimal imContributiCaricoEnte = righeCori.stream().filter(Contributo_ritenutaBulk::isContributoEnte)
					.map(Contributo_ritenutaBulk::getAmmontare)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			if (compenso.getIm_lordo_percipiente().compareTo(BigDecimal.ZERO)==0 && imContributiCaricoEnte.compareTo(BigDecimal.ZERO)==0)
				throw new ScritturaPartitaDoppiaNotRequiredException("Scrittura Economica non necessaria in quanto sia il lordo percipiente che i contributi a carico ente sono nulli.");

			final Optional<Pair<Voce_epBulk, Voce_epBulk>> pairContoCostoCompenso;

			if (compenso.getObbligazioneScadenzario()==null) {
				if (compenso.getIm_lordo_percipiente().compareTo(BigDecimal.ZERO) > 0 || imContributiCaricoEnte.compareTo(BigDecimal.ZERO) > 0)
					throw new ApplicationException("Compenso " + descCompenso + " con lordo percipiente e/o contributi a carico ente senza indicazione dell'obbligazione. " +
							"Scrittura prima nota non possibile per impossibilità ad individuare il conto di costo!");
				pairContoCostoCompenso = Optional.empty();
			} else
				pairContoCostoCompenso = Optional.ofNullable(this.findPairCosto(userContext, compenso));

			TestataPrimaNota testataPrimaNota = new TestataPrimaNota(compenso.getDt_da_competenza_coge(), compenso.getDt_a_competenza_coge());

			//Nel caso dei compensi rilevo subito il costo prelevando le informazioni dalla riga del compenso stesso
			//Registrazione conto COSTO COMPENSO
			if (compenso.getIm_lordo_percipiente().compareTo(BigDecimal.ZERO)!=0) {
				if (!pairContoCostoCompenso.isPresent())
					throw new ApplicationException("Compenso " + descCompenso + " con lordo percipiente non nullo. Scrittura prima nota non possibile per impossibilità ad individuare il conto di costo!");
				testataPrimaNota.openDettaglioCostoRicavo(compenso, pairContoCostoCompenso.get().getFirst().getCd_voce_ep(), compenso.getIm_lordo_percipiente());
				testataPrimaNota.openDettaglioPatrimonialePartita(compenso, pairContoCostoCompenso.get().getSecond().getCd_voce_ep(), compenso.getIm_lordo_percipiente(), compenso.getCd_terzo());
			}

			//Registrazione conto CONTRIBUTI-RITENUTE
			//Vengono registrati tutti i CORI a carico Ente
			righeCori.stream().filter(el->el.getAmmontare().compareTo(BigDecimal.ZERO)!=0)
					.filter(el->el.isContributoEnte() || el.isTipoContributoIva())
					.forEach(cori->{
						try {
							BigDecimal imCostoCori = cori.getAmmontare();
							Pair<Voce_epBulk, Voce_epBulk> pairContoCostoCori;

							if (imCostoCori.compareTo(BigDecimal.ZERO)!=0) {
								if (cori.isTipoContributoRivalsa() || cori.isTipoContributoIva()) {
									if (!pairContoCostoCompenso.isPresent())
										throw new ApplicationException("Compenso " + descCompenso + " con contributo non nullo. Scrittura prima nota non possibile per impossibilità ad individuare il conto di costo!");
									if (cori.isTipoContributoRivalsa()) {
										// Se la tipologia di contributo ritenuta è RIVALSA va tutto sul costo principale del compenso
										testataPrimaNota.openDettaglioCostoRicavo(compenso, pairContoCostoCompenso.get().getFirst().getCd_voce_ep(), imCostoCori);
										testataPrimaNota.openDettaglioPatrimonialePartita(compenso, pairContoCostoCompenso.get().getSecond().getCd_voce_ep(), imCostoCori, compenso.getCd_terzo());
									} else if (cori.isTipoContributoIva()) {
										//Il primo giro viene fatto solo per il contributo a carico ente.... nel caso di split esiste anche la riga carico
										//percipiente che, in questa fase, deve solo rilevare il credito verso il fornitore che sarà chiuso all'atto del pagamento
										if (cori.isContributoEnte()) {
											if (compenso.isIstituzionale())
												// Se la tipologia di contributo ritenuta è IVA ed è istituzionale va tutto sul costo principale del compenso
												testataPrimaNota.openDettaglioCostoRicavo(compenso, pairContoCostoCompenso.get().getFirst().getCd_voce_ep(), imCostoCori);
											else if (compenso.isCommerciale()) {
												// Se la tipologia di contributo ritenuta è IVA Commerciale il conto è del tributo
												Voce_epBulk voceCosto = this.findContoCosto(userContext, cori.getTipoContributoRitenuta(), cori.getEsercizio(), cori.getSezioneCostoRicavo(), cori.getTi_ente_percipiente());
												if (compenso.getFl_split_payment().equals(Boolean.TRUE))
													testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO_SPLIT.value(), compenso.getTipoDocumentoEnum().getSezioneEconomica(), voceCosto.getCd_voce_ep(), imCostoCori, compenso, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
												else
													testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO.value(), compenso.getTipoDocumentoEnum().getSezioneEconomica(), voceCosto.getCd_voce_ep(), imCostoCori, compenso, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
											}
											testataPrimaNota.openDettaglioPatrimonialePartita(compenso, pairContoCostoCompenso.get().getSecond().getCd_voce_ep(), imCostoCori, compenso.getCd_terzo());

											if (compenso.getFl_split_payment().equals(Boolean.TRUE)) {
												Ass_tipo_cori_voce_epBulk assTipoCori = this.findAssociazioneCoriVoceEp(userContext, cori.getTipoContributoRitenuta(), cori.getEsercizio(), compenso.getTipoDocumentoEnum().getSezionePatrimoniale(), cori.getTi_ente_percipiente());
												testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO_SPLIT.value(), compenso.getTipoDocumentoEnum().getSezionePatrimoniale(), assTipoCori.getCd_voce_ep_contr(), imCostoCori, compenso, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
												testataPrimaNota.closeDettaglioPatrimonialePartita(compenso, pairContoCostoCompenso.get().getSecond().getCd_voce_ep(), imCostoCori, compenso.getCd_terzo(), true);
											}
										} else { //Contributo Percipiente
											if (compenso.getFl_split_payment().equals(Boolean.TRUE)) {
												//Rilevo il conto IVA Credito/Debito di tipo SPLIT e apro il conto ritenute split
												Voce_epBulk aContoCreditoRitenuteSplit = this.findContoCreditoRitenuteSplitPayment(userContext, compenso.getEsercizio());
												testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.CREDITO.value(), compenso.getTipoDocumentoEnum().getSezioneEconomica(), aContoCreditoRitenuteSplit.getCd_voce_ep(), imCostoCori, compenso, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
												testataPrimaNota.openDettaglioPatrimonialePartita(compenso, pairContoCostoCompenso.get().getSecond().getCd_voce_ep(), imCostoCori, compenso.getCd_terzo());
											}
										}
									}
								} else {
									pairContoCostoCori = this.findPairCostoCompenso(userContext, cori);
									testataPrimaNota.openDettaglioCostoRicavo(compenso, pairContoCostoCori.getFirst().getCd_voce_ep(), imCostoCori);
									testataPrimaNota.openDettaglioPatrimonialeCori(compenso, pairContoCostoCori.getSecond().getCd_voce_ep(), imCostoCori, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
								}
							}
						} catch (ComponentException|PersistencyException|RemoteException e) {
							throw new ApplicationRuntimeException(e);
						}
					});

			return this.generaScrittura(userContext, compenso, Collections.singletonList(testataPrimaNota), true);
		} catch (PersistencyException|RemoteException e) {
			throw handleException(e);
		}
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaCompensoStipendi(UserContext userContext, CompensoBulk compenso) throws ComponentException, ScritturaPartitaDoppiaNotRequiredException {
		//la contabilizzazione del compenso stipendi prevede che nell'ambito del compenso confluisca la movimentazione generata sia dal documento passivo legato al mandato stipendi
		//sul quale la scrittura non viene proprio prodotta
		try {
			//recupero il documento generico passivo leggendolo dalla tabelle stipendiCofiBulk
			Stipendi_cofiBulk stipendiCofiBulk = ((Stipendi_cofiHome)getHome(userContext, Stipendi_cofiBulk.class)).findStipendiCofi(compenso);

			if (!Optional.ofNullable(stipendiCofiBulk).isPresent())
				throw new ApplicationException("Il compenso " + compenso.getEsercizio() + "/" + compenso.getCd_cds() + "/" + compenso.getPg_compenso() +
						" non risulta pagare uno stipendio. Proposta di prima nota non possibile.");

			Documento_genericoBulk documentoGenericoPassivoBulk = (Documento_genericoBulk)getHome(userContext, Documento_genericoBulk.class)
					.findByPrimaryKey(new Documento_genericoBulk(stipendiCofiBulk.getCd_cds_doc_gen(), stipendiCofiBulk.getCd_tipo_doc_gen(),
							stipendiCofiBulk.getCd_uo_doc_gen(), stipendiCofiBulk.getEsercizio_doc_gen(), stipendiCofiBulk.getPg_doc_gen()));

			//Quindi individuo la scrittura proposta sul documento
			TestataPrimaNota testataPrimaNotaDocumentoPassivo = proposeTestataPrimaNotaDocumento(userContext, documentoGenericoPassivoBulk).stream().findFirst().orElse(null);

			//E la copio identica sul compenso
			TestataPrimaNota testataPrimaNotaCompenso = new TestataPrimaNota(compenso.getDt_da_competenza_coge(), compenso.getDt_a_competenza_coge());
			testataPrimaNotaDocumentoPassivo.getDett().forEach(el-> testataPrimaNotaCompenso.addDettaglio(el.getTipoDett(), el.getSezione(), el.getCdConto(), el.getImporto(), el.getPartita(), el.getCdTerzo(), el.getCdCori()));

			return this.generaScrittura(userContext, compenso, Collections.singletonList(testataPrimaNotaCompenso), true);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaAnticipo(UserContext userContext, AnticipoBulk anticipo) throws ComponentException {
		try {
			TestataPrimaNota testataPrimaNota = new TestataPrimaNota(anticipo.getDt_da_competenza_coge(), anticipo.getDt_a_competenza_coge());

			//Registrazione conto COSTO ANTICIPO
			BigDecimal imCostoAnticipo = anticipo.getIm_anticipo();
			if (imCostoAnticipo.compareTo(BigDecimal.ZERO)!=0) {
				Voce_epBulk aContoCreditoAnticipo = this.findContoCreditoAnticipo(userContext, anticipo.getEsercizio());
				Voce_epBulk aContoDebitoAnticipo = this.findContoDebitoAnticipo(userContext, anticipo.getEsercizio());
				testataPrimaNota.openDettaglioCostoRicavoPartita(anticipo, aContoCreditoAnticipo.getCd_voce_ep(), imCostoAnticipo, anticipo.getCd_terzo());
				testataPrimaNota.openDettaglioPatrimonialePartita(anticipo, aContoDebitoAnticipo.getCd_voce_ep(), imCostoAnticipo, anticipo.getCd_terzo());
			}
			return this.generaScrittura(userContext, anticipo, Collections.singletonList(testataPrimaNota), false);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaAperturaFondo(UserContext userContext, Documento_genericoBulk documento) throws ComponentException {
		try {
			if (!documento.getTipoDocumentoEnum().isAperturaFondo())
				throw new ApplicationException("Il documento " + documento.getEsercizio() + "/" + documento.getCd_tipo_doc() + "/" +
						documento.getCd_uo()+"/"+documento.getPg_documento_generico() +
						" non risulta essere di tipo 'Apertura Fondo Econmale'. Proposta di prima nota non possibile.");

			List<IDocumentoAmministrativoRigaBulk> righeDocamm = this.getRigheDocamm(userContext, documento);

			if (righeDocamm.size() != 1)
				throw new ApplicationException("Il documento " + documento.getEsercizio() + "/" + documento.getCd_tipo_doc() + "/" +
						documento.getCd_uo()+"/"+documento.getPg_documento_generico() +
						" ha un numero di righe non coerente con l'unica prevista per un documento di apertura fondo economale. Proposta di prima nota non possibile.");

			Integer cdTerzo = righeDocamm.get(0).getCd_terzo();

			TestataPrimaNota testataPrimaNota = new TestataPrimaNota(documento.getDt_da_competenza_coge(), documento.getDt_a_competenza_coge());

			//Registrazione conto COSTO ANTICIPO
			BigDecimal imCosto = documento.getIm_totale();
			if (imCosto.compareTo(BigDecimal.ZERO)!=0) {
				Voce_epBulk contoCreditoEconomo = this.findContoCreditoEconomo(userContext, documento.getEsercizio());
				Voce_epBulk contoDebitoEconomo = this.findContoDebitoEconomo(userContext, documento.getEsercizio());
				testataPrimaNota.openDettaglioCostoRicavoPartita(documento, contoCreditoEconomo.getCd_voce_ep(), imCosto, cdTerzo);
				testataPrimaNota.openDettaglioPatrimonialePartita(documento, contoDebitoEconomo.getCd_voce_ep(), imCosto, cdTerzo);
			}
			return this.generaScrittura(userContext, documento, Collections.singletonList(testataPrimaNota), false);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaRimborso(UserContext userContext, RimborsoBulk rimborso) throws ComponentException {
		try {
			TestataPrimaNota testataPrimaNota = new TestataPrimaNota(rimborso.getDt_da_competenza_coge(), rimborso.getDt_a_competenza_coge());

			//Registrazione conto COSTO ANTICIPO
			BigDecimal imCostoRimborso = rimborso.getIm_rimborso();
			if (imCostoRimborso.compareTo(BigDecimal.ZERO)!=0) {
				Voce_epBulk aContoCreditoAnticipo = this.findContoCreditoAnticipo(userContext, rimborso.getAnticipo().getEsercizio());
				Voce_epBulk aContoDebitoAnticipo = this.findContoDebitoAnticipo(userContext, rimborso.getAnticipo().getEsercizio());
				testataPrimaNota.closeDettaglioCostoRicavo(rimborso.getAnticipo(), aContoCreditoAnticipo.getCd_voce_ep(), imCostoRimborso);
				testataPrimaNota.closeDettaglioPatrimonialePartita(rimborso.getAnticipo(), aContoDebitoAnticipo.getCd_voce_ep(), imCostoRimborso, rimborso.getAnticipo().getCd_terzo());
			}
			return this.generaScrittura(userContext, rimborso, Collections.singletonList(testataPrimaNota), false);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaMissione(UserContext userContext, MissioneBulk missione) throws ComponentException, ScritturaPartitaDoppiaNotRequiredException {
		try {
			String descMissione = missione.getEsercizio() + "/" + missione.getCd_unita_organizzativa() + "/" + missione.getPg_missione();

			//Le missioni pagate con compenso non creano scritture di prima nota in quanto create direttamente dal compenso stesso
			if (missione.getFl_associato_compenso())
				throw new ScritturaPartitaDoppiaNotRequiredException("Missione " + descMissione + " associata a compenso. Registrazione economica non prevista.");

			if (missione.isAnnullato())
				throw new ScritturaPartitaDoppiaNotRequiredException("Missione " + descMissione + " annullata. Registrazione economica non prevista.");

			if (missione.isMissioneProvvisoria())
				return null;

			Optional<AnticipoBulk> optAnticipo = Optional.ofNullable(missione.getAnticipo()).map(anticipo->
					Optional.of(anticipo).filter(el->el.getCrudStatus()!=OggettoBulk.UNDEFINED).orElseGet(()-> {
						try {
							AnticipoHome home = (AnticipoHome) getHome(userContext, AnticipoBulk.class);
							return (AnticipoBulk)home.findByPrimaryKey(anticipo);
						} catch (ComponentException | PersistencyException e) {
							throw new DetailedRuntimeException(e);
						}
					})).filter(anticipo->anticipo.getIm_anticipo().compareTo(BigDecimal.ZERO)!=0);

			final Optional<Pair<Voce_epBulk, Voce_epBulk>> pairContoCostoAnticipo = optAnticipo.map(anticipo->{
				try {
					return this.findPairCosto(userContext, optAnticipo.get());
				} catch (ComponentException | PersistencyException | RemoteException e) {
					throw new DetailedRuntimeException(e);
				}
			});

			TestataPrimaNota testataPrimaNota = new TestataPrimaNota(missione.getDt_inizio_missione(), missione.getDt_fine_missione());

			//Registrazione conto COSTO MISSIONE
			BigDecimal imCostoMissioneNettoAnticipo = missione.getIm_totale_missione().subtract(optAnticipo.map(AnticipoBulk::getIm_anticipo).orElse(BigDecimal.ZERO));

			final Optional<Pair<Voce_epBulk, Voce_epBulk>> pairContoCostoMissione;
			if (imCostoMissioneNettoAnticipo.compareTo(BigDecimal.ZERO)>0)
				pairContoCostoMissione = Optional.ofNullable(this.findPairCosto(userContext, missione));
			else
				pairContoCostoMissione = pairContoCostoAnticipo;

			if (imCostoMissioneNettoAnticipo.compareTo(BigDecimal.ZERO)>0) {
				if (!pairContoCostoMissione.isPresent())
					throw new ApplicationException("Missione " + descMissione + " di importo positivo. Scrittura prima nota non possibile per impossibilità ad individuare il conto di costo!");
				testataPrimaNota.openDettaglioCostoRicavo(missione, pairContoCostoMissione.get().getFirst().getCd_voce_ep(), imCostoMissioneNettoAnticipo);
				testataPrimaNota.openDettaglioPatrimonialePartita(missione, pairContoCostoMissione.get().getSecond().getCd_voce_ep(), imCostoMissioneNettoAnticipo, missione.getCd_terzo());
			}

			//se esiste anticipo devo fare una serie di registrazioni
			if (optAnticipo.isPresent()) {
				AnticipoBulk anticipo = optAnticipo.get();
				BigDecimal imCostoAnticipo = anticipo.getIm_anticipo();
				if (missione.getIm_totale_missione().compareTo(imCostoAnticipo) < 0)
					imCostoAnticipo = missione.getIm_totale_missione();

				if (imCostoAnticipo.compareTo(BigDecimal.ZERO) != 0) {
					if (!pairContoCostoMissione.isPresent())
						throw new ApplicationException("Missione " + descMissione + " con anticipo di importo non nullo. Scrittura prima nota non possibile per impossibilità ad individuare il conto di costo della missione!");
					if (!pairContoCostoAnticipo.isPresent())
						throw new ApplicationException("Missione " + descMissione + " con anticipo di importo non nullo. Scrittura prima nota non possibile per impossibilità ad individuare il conto di costo dell'anticipo!");

					// 1. scaricare i costi dell'anticipo compensandoli con il conto patrimoniale della missione
					testataPrimaNota.openDettaglioCostoRicavo(missione, pairContoCostoAnticipo.get().getFirst().getCd_voce_ep(), imCostoAnticipo);
					testataPrimaNota.openDettaglioPatrimonialePartita(missione, pairContoCostoMissione.get().getSecond().getCd_voce_ep(), imCostoAnticipo, missione.getCd_terzo());

					//2. chiudere il conto credito per anticipo
					List<Movimento_cogeBulk> allMovimentiPrimaNota = this.findMovimentiPrimaNota(userContext, anticipo);
					String contoPatrimonialeAperturaCreditoAnticipo = this.findMovimentoAperturaCreditoAnticipo(allMovimentiPrimaNota, anticipo, anticipo.getCd_terzo()).getCd_voce_ep();

					testataPrimaNota.closeDettaglioCostoRicavoPartita(anticipo, contoPatrimonialeAperturaCreditoAnticipo, imCostoAnticipo, anticipo.getCd_terzo());
					testataPrimaNota.closeDettaglioPatrimonialePartita(missione, pairContoCostoMissione.get().getSecond().getCd_voce_ep(), imCostoAnticipo, missione.getCd_terzo(), true);
				}
			}
			return this.generaScrittura(userContext, missione, Collections.singletonList(testataPrimaNota), true);
		} catch (PersistencyException|RemoteException e) {
			throw handleException(e);
		}
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaMandato(UserContext userContext, MandatoBulk mandato) throws ComponentException, ScritturaPartitaDoppiaNotRequiredException {
		try {
			//Completo l'oggetto mandato con i sottooggetti che serviranno durante l'elaborazione
			completeMandato(userContext, mandato);

			if (mandato.isMandatoRegolarizzazione())
				throw new ScritturaPartitaDoppiaNotRequiredException("Mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +
						" di regolarizzazione. Registrazione economica non prevista.");

			//Il documento deve essere annullato o esitato altrimenti esce
			if (mandato.isAnnullato())
				return this.proposeScritturaPartitaDoppiaManRevAnnullato(userContext, mandato);
			else if (mandato.isPagato()) {
				if (mandato.getMandato_rigaColl().stream().map(Mandato_rigaBulk::getCd_tipo_documento_amm)
						.anyMatch(el->TipoDocumentoEnum.fromValue(el).isCompenso()))
					return this.proposeScritturaPartitaDoppiaMandatoCompenso(userContext, mandato);
				else if (mandato.getMandato_rigaColl().stream().map(Mandato_rigaBulk::getCd_tipo_documento_amm)
						.anyMatch(el->TipoDocumentoEnum.fromValue(el).isGenericoStipendiSpesa()))
					return this.proposeScritturaPartitaDoppiaMandatoStipendi(userContext, mandato);
				else if (mandato.getMandato_rigaColl().stream().map(Mandato_rigaBulk::getCd_tipo_documento_amm)
						.anyMatch(el->TipoDocumentoEnum.fromValue(el).isGenericoCoriVersamentoSpesa()))
					return this.proposeScritturaPartitaDoppiaMandatoVersamentoCori(userContext, mandato);
				else {
					//devo verificare se trattasi di un mandato generato da righe negative associate a qualche compenso
					Collection<V_doc_cont_compBulk> coll = ((V_doc_cont_compHome) getHome(userContext, V_doc_cont_compBulk.class)).findByDocumento(mandato.getEsercizio(), mandato.getCd_cds(), mandato.getPg_mandato(), V_doc_cont_compBulk.TIPO_DOC_CONT_MANDATO);
					CompensoBulk compenso = null;
					if (!coll.isEmpty()) {
						if (coll.size() > 1)
							throw new ApplicationRuntimeException("Errore nell'individuazione del compenso a cui è collegato il mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato()
									+ ": il mandato risulta associato a troppi compensi.");
						V_doc_cont_compBulk docContCompBulk = coll.stream().findAny().get();
						compenso = (CompensoBulk) getHome(userContext, CompensoBulk.class).findByPrimaryKey(new CompensoBulk(docContCompBulk.getCd_cds_compenso(), docContCompBulk.getCd_uo_compenso(),
								docContCompBulk.getEsercizio_compenso(), docContCompBulk.getPg_compenso()));
					}

					if (compenso != null)
						return this.proposeScritturaPartitaDoppiaMandatoCompenso(userContext, mandato, compenso);
					else if (mandato.getMandato_rigaColl().size() == 1 && mandato.getIm_ritenute().compareTo(BigDecimal.ZERO) > 0 &&
							mandato.getMandato_rigaColl().stream().map(Mandato_rigaBulk::getCd_tipo_documento_amm)
									.anyMatch(el -> TipoDocumentoEnum.fromValue(el).isGenericoSpesa()))
						return this.proposeScritturaPartitaDoppiaMandatoAccantonamentoCoriDocgen(userContext, mandato);
				}

				TestataPrimaNota testataPrimaNota = new TestataPrimaNota();
				if (mandato.isMandatoRegolarizzazione() && mandato.getCd_cds().equals(mandato.getCd_cds_origine()) &&
						mandato.getUnita_organizzativa().getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
					return null;

				//raggruppo i mandatiRiga per Partita
				List<MandatoRigaComplete> dettaglioFinanziarioList = this.completeRigheMandato(userContext, mandato);

				Map<IDocumentoAmministrativoBulk, Map<Integer, List<MandatoRigaComplete>>> mapDettagli =
						dettaglioFinanziarioList.stream()
								.collect(Collectors.groupingBy(MandatoRigaComplete::getDocamm,
										Collectors.groupingBy(MandatoRigaComplete::getCdTerzo)));

				mapDettagli.keySet().forEach(aDocamm -> {
					Map<Integer, List<MandatoRigaComplete>> mapDocAmm = mapDettagli.get(aDocamm);
					mapDocAmm.keySet().forEach(aCdTerzo -> {
						try {
							List<MandatoRigaComplete> mandatoRigaCompleteList = mapDocAmm.get(aCdTerzo);
							addDettagliPrimaNotaMandatoDocumentiVari(userContext, testataPrimaNota, mandato, aDocamm, aCdTerzo, mandatoRigaCompleteList);
						} catch (ComponentException | PersistencyException | RemoteException e) {
							throw new ApplicationRuntimeException(e);
						}
					});
				});
				return this.generaScrittura(userContext, mandato, Collections.singletonList(testataPrimaNota), true);
			}
			throw new ApplicationRuntimeException("Scrittura Economica non prevista in quanto il mandato non risulta pagato.");
		} catch (PersistencyException | RemoteException e) {
			throw handleException(e);
		}
	}

	private List<MandatoRigaComplete> completeRigheMandato(UserContext userContext, MandatoBulk mandato) {
		//raggruppo i mandatiRiga per Partita
		Map<Integer, Map<String, Map<String, Map<String, Map<Long, List<Mandato_rigaBulk>>>>>> mapRigheMandato =
				mandato.getMandato_rigaColl().stream()
						.collect(Collectors.groupingBy(Mandato_rigaBulk::getEsercizio_doc_amm,
								Collectors.groupingBy(Mandato_rigaBulk::getCd_tipo_documento_amm,
										Collectors.groupingBy(Mandato_rigaBulk::getCd_cds_doc_amm,
												Collectors.groupingBy(Mandato_rigaBulk::getCd_uo_doc_amm,
														Collectors.groupingBy(Mandato_rigaBulk::getPg_doc_amm))))));

		List<MandatoRigaComplete> mandatoRigaCompleteList = new ArrayList<>();
		mapRigheMandato.keySet().forEach(aEsercizioDocamm -> {
			Map<String, Map<String, Map<String, Map<Long, List<Mandato_rigaBulk>>>>> mapEsercizioDocamm = mapRigheMandato.get(aEsercizioDocamm);
			mapEsercizioDocamm.keySet().forEach(aTipoDocamm -> {
				Map<String, Map<String, Map<Long, List<Mandato_rigaBulk>>>> mapTipoDocamm = mapEsercizioDocamm.get(aTipoDocamm);
				mapTipoDocamm.keySet().forEach(aCdCdsDocamm -> {
					Map<String, Map<Long, List<Mandato_rigaBulk>>> mapCdCdsDocamm = mapTipoDocamm.get(aCdCdsDocamm);
					mapCdCdsDocamm.keySet().forEach(aCdUoDocamm -> {
						Map<Long, List<Mandato_rigaBulk>> mapCdUoDocamm = mapCdCdsDocamm.get(aCdUoDocamm);
						mapCdUoDocamm.keySet().forEach(aPgDocamm -> {
							try {
								List<Mandato_rigaBulk> listRigheMandato = mapCdUoDocamm.get(aPgDocamm);

								IDocumentoAmministrativoBulk docamm;
								if (TipoDocumentoEnum.fromValue(aTipoDocamm).isDocumentoAmministrativoPassivo()) {
									docamm = (Documento_amministrativo_passivoBulk) getHome(userContext, Documento_amministrativo_passivoBulk.class)
											.findByPrimaryKey(new Documento_amministrativo_passivoBulk(aCdCdsDocamm, aCdUoDocamm, aEsercizioDocamm, aPgDocamm));
								} else if (TipoDocumentoEnum.fromValue(aTipoDocamm).isDocumentoGenericoPassivo()) {
									Documento_genericoHome home = (Documento_genericoHome) getHome(userContext, Documento_genericoBulk.class);
									docamm = (IDocumentoAmministrativoBulk)home.findByPrimaryKey(new Documento_genericoBulk(aCdCdsDocamm, aTipoDocamm, aCdUoDocamm, aEsercizioDocamm, aPgDocamm));
								} else if (TipoDocumentoEnum.fromValue(aTipoDocamm).isMissione()) {
									docamm = (MissioneBulk) getHome(userContext, MissioneBulk.class)
											.findByPrimaryKey(new MissioneBulk(aCdCdsDocamm,aCdUoDocamm, aEsercizioDocamm, aPgDocamm));
								} else if (TipoDocumentoEnum.fromValue(aTipoDocamm).isAnticipo()) {
									docamm = (AnticipoBulk) getHome(userContext, AnticipoBulk.class)
											.findByPrimaryKey(new AnticipoBulk(aCdCdsDocamm,aCdUoDocamm, aEsercizioDocamm, aPgDocamm));
								} else
									throw new ApplicationRuntimeException("Scrittura Economica non gestita per la tipologia di documento "+aTipoDocamm +
											" collegato al mandato "+ mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_manrev() +".");

								mandatoRigaCompleteList.addAll(listRigheMandato.stream().map(rigaMandato->{
									try {
										Integer cdTerzo;
										if (docamm instanceof MissioneBulk)
											return new MandatoRigaComplete(docamm, rigaMandato, null, ((MissioneBulk) docamm).getCd_terzo());
										else if (docamm instanceof AnticipoBulk)
											return new MandatoRigaComplete(docamm, rigaMandato, null, ((AnticipoBulk) docamm).getCd_terzo());
										else {
											List<IDocumentoAmministrativoRigaBulk> docammRighe = this.getRigheDocamm(userContext, docamm).stream()
													.filter(el->el.getScadenzaDocumentoContabile() instanceof Obbligazione_scadenzarioBulk)
													.filter(el->
															((Obbligazione_scadenzarioBulk)el.getScadenzaDocumentoContabile()).getEsercizio().equals(rigaMandato.getEsercizio_obbligazione()) &&
																	((Obbligazione_scadenzarioBulk)el.getScadenzaDocumentoContabile()).getEsercizio_originale().equals(rigaMandato.getEsercizio_ori_obbligazione()) &&
																	((Obbligazione_scadenzarioBulk)el.getScadenzaDocumentoContabile()).getCd_cds().equals(rigaMandato.getCd_cds()) &&
																	((Obbligazione_scadenzarioBulk)el.getScadenzaDocumentoContabile()).getPg_obbligazione().equals(rigaMandato.getPg_obbligazione()) &&
																	((Obbligazione_scadenzarioBulk)el.getScadenzaDocumentoContabile()).getPg_obbligazione_scadenzario().equals(rigaMandato.getPg_obbligazione_scadenzario())
													).collect(Collectors.toList());

											if (docammRighe.isEmpty())
												throw new ApplicationException("Non è stato possibile individuare correttamente la riga del documento " +
															docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getCd_uo()+"/"+docamm.getPg_doc_amm()+
															" associata alla riga del mandato "+ mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_manrev() +".");

											if (docammRighe.stream().collect(Collectors.groupingBy(IDocumentoAmministrativoRigaBulk::getCd_terzo)).size()>1)
												throw new ApplicationException("Risultano più righe del documento " +
														docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getCd_uo()+"/"+docamm.getPg_doc_amm()+
														" con terzi diversi associate alla stessa riga del mandato "+ mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_manrev() +".");
											return new MandatoRigaComplete(docamm, rigaMandato, docammRighe,
													docammRighe.stream().findAny().map(IDocumentoAmministrativoRigaBulk::getCd_terzo).orElse(null));
										}
									} catch (ComponentException ex) {
										throw new ApplicationRuntimeException(ex);
									}
								}).collect(Collectors.toList()));
							} catch (ComponentException | PersistencyException e) {
								throw new ApplicationRuntimeException(e);
							}
						});
					});
				});
			});
		});
		return mandatoRigaCompleteList;
	}

	private TestataPrimaNota proposeTestataPrimaNotaManRevAnnullato(UserContext userContext, IManRevBulk manrev) throws ComponentException, PersistencyException {
		boolean isMandato = manrev instanceof MandatoBulk;
		if (!manrev.isAnnullato())
			throw new ApplicationException((isMandato?"Il mandato ":"La reversale") + manrev.getEsercizio() + "/" + manrev.getCd_cds() + "/" + manrev.getPg_manrev() +
					" non risulta " + (isMandato?"annullato":"annullata")+". Proposta di prima nota non possibile.");

		TestataPrimaNota testataPrimaNota = new TestataPrimaNota();
		//storno scrittura prima nota del mandato
		this.stornoTotaleScritturaPrimaNota(userContext, testataPrimaNota, manrev);
		return testataPrimaNota;
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaManRevAnnullato(UserContext userContext, IManRevBulk manrev) throws ComponentException, PersistencyException {
		TestataPrimaNota testataPrimaNota = proposeTestataPrimaNotaManRevAnnullato(userContext, manrev);
		return this.generaScrittura(userContext, manrev, Collections.singletonList(testataPrimaNota), true);
	}

	private void stornoTotaleScritturaPrimaNota(UserContext userContext, TestataPrimaNota testataPrimaNota, IDocumentoCogeBulk docamm) throws ComponentException, PersistencyException {
		//devo stornare scrittura prima nota del mandato
		//prendo tutte le prime note e scrivo registrazioni di senso inverso
		//recupero la scrittura del compenso
		List<Movimento_cogeBulk> allMovimentiPrimaNota = this.findMovimentiPrimaNota(userContext,docamm);

		allMovimentiPrimaNota.forEach(movimento -> {
			Partita partita = Optional.of(movimento).filter(mov -> Optional.ofNullable(mov.getCd_tipo_documento()).isPresent())
					.map(mov -> new Partita(mov.getCd_tipo_documento(), mov.getCd_cds_documento(), mov.getCd_uo_documento(), mov.getEsercizio_documento(), mov.getPg_numero_documento(),
							mov.getCd_terzo(), TipoDocumentoEnum.fromValue(mov.getCd_tipo_documento()))).orElse(null);
					testataPrimaNota.addDettaglio(movimento.getTi_riga(), Movimento_cogeBulk.getControSezione(movimento.getSezione()), movimento.getSezione(), movimento.getIm_movimento(), partita, movimento.getCd_terzo(), movimento.getCd_contributo_ritenuta());
				});
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaMandatoCompenso(UserContext userContext, MandatoBulk mandato) throws ComponentException, PersistencyException, RemoteException {
		if (mandato.getMandato_rigaColl().stream().map(Mandato_rigaBulk::getCd_tipo_documento_amm)
				.noneMatch(el -> TipoDocumentoEnum.fromValue(el).isCompenso()))
			throw new ApplicationException("Il mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +
					" non risulta pagare un compenso. Proposta di prima nota non possibile.");
		if (mandato.getMandato_rigaColl().isEmpty() || mandato.getMandato_rigaColl().size() > 1)
			throw new ApplicationException("Il mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +
					" ha un numero di righe non coerente con l'unica prevista per un mandato di compenso. Proposta di prima nota non possibile.");

		Mandato_rigaBulk rigaMandato = mandato.getMandato_rigaColl().get(0);
		TestataPrimaNota testataPrimaNota = new TestataPrimaNota();

		CompensoBulk compenso = (CompensoBulk) getHome(userContext, CompensoBulk.class).findByPrimaryKey(new CompensoBulk(rigaMandato.getCd_cds_doc_amm(), rigaMandato.getCd_uo_doc_amm(), rigaMandato.getEsercizio_doc_amm(),
				rigaMandato.getPg_doc_amm()));

		List<Contributo_ritenutaBulk> righeCori = Optional.ofNullable(compenso.getChildren()).orElseGet(() -> {
			try {
				Contributo_ritenutaHome home = (Contributo_ritenutaHome) getHome(userContext, Contributo_ritenutaBulk.class);
				return (java.util.List<Contributo_ritenutaBulk>) home.loadContributiRitenute(compenso);
			} catch (ComponentException | PersistencyException e) {
				throw new DetailedRuntimeException(e);
			}
		});

		//verifico se esiste conguaglio collegato al compenso principale pagato con il mandato
		CompensoBulk compensoConguaglio = ((CompensoHome) getHome(userContext, CompensoBulk.class)).findCompensoConguaglioAssociato(userContext, compenso);

		//se esiste aggiungo le righe di contributo/ritenuta in quanto anche loro contribuiscono a determinare il netto mandato
		if (compensoConguaglio != null && !compensoConguaglio.equalsByPrimaryKey(compenso)) {
			righeCori.addAll(Optional.ofNullable(compensoConguaglio.getChildren()).orElseGet(() -> {
				try {
					Contributo_ritenutaHome home = (Contributo_ritenutaHome) getHome(userContext, Contributo_ritenutaBulk.class);
					return (java.util.List<Contributo_ritenutaBulk>) home.loadContributiRitenute(compensoConguaglio);
				} catch (ComponentException | PersistencyException e) {
					throw new DetailedRuntimeException(e);
				}
			}));
		}

		//recupero la missione se il compenso è da missione
		Optional<MissioneBulk> optMissione = Optional.ofNullable(compenso.getMissione()).map(missione->
				Optional.of(missione).filter(el->el.getCrudStatus()!=OggettoBulk.UNDEFINED).orElseGet(()-> {
					try {
						MissioneHome home = (MissioneHome) getHome(userContext, MissioneBulk.class);
						return (MissioneBulk)home.findByPrimaryKey(compenso.getMissione());
					} catch (ComponentException | PersistencyException e) {
						throw new DetailedRuntimeException(e);
					}
				}));

		//quindi l'anticipo se la missione è stata associata ad esso
		Optional<AnticipoBulk> optAnticipo = optMissione.flatMap(missione->Optional.ofNullable(missione.getAnticipo()).map(anticipo->
				Optional.of(anticipo).filter(el->el.getCrudStatus()!=OggettoBulk.UNDEFINED).orElseGet(()-> {
					try {
						AnticipoHome home = (AnticipoHome) getHome(userContext, AnticipoBulk.class);
						return (AnticipoBulk)home.findByPrimaryKey(anticipo);
					} catch (ComponentException | PersistencyException e) {
						throw new DetailedRuntimeException(e);
					}
				}))).filter(anticipo->anticipo.getIm_anticipo().compareTo(BigDecimal.ZERO)!=0);

		//recupero la scrittura del compenso
		List<Movimento_cogeBulk> allMovimentiPrimaNotaCompenso = this.findMovimentiPrimaNota(userContext, compenso);

		if (allMovimentiPrimaNotaCompenso.isEmpty())
			throw new ApplicationException("Non risulta generata la scrittura Prima Nota del compenso " + compenso.getEsercizio() + "/" + compenso.getPg_compenso() + " associato del mandato.");

		// Effettuo il controllo che le ritenute associate al compenso originario coincida con le ritenute della riga associata al mandato
		// Se la tipologia di contributo ritenuta è IVA o RIVALSA non calcolo le ritenute perchè vanno direttamente al fornitore
		{
			BigDecimal imRitenuteFittizieSoloCompenso = righeCori.stream()
					.filter(el -> el.getCompenso().equalsByPrimaryKey(compenso))
					.filter(Contributo_ritenutaBulk::isContributoEnte)
					.filter(el -> el.isTipoContributoRivalsa() || el.isTipoContributoIva())
					.map(Contributo_ritenutaBulk::getAmmontare)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal imRitenutePositiveSoloCompenso = righeCori.stream()
					.filter(el -> el.getCompenso().equalsByPrimaryKey(compenso))
					.map(Contributo_ritenutaBulk::getAmmontare)
					.filter(el -> el.compareTo(BigDecimal.ZERO) > 0)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			//Aggiungo eventuali quote da trattenere indicate sul compenso
			imRitenutePositiveSoloCompenso = imRitenutePositiveSoloCompenso.add(Optional.ofNullable(compenso.getIm_netto_da_trattenere()).orElse(BigDecimal.ZERO));

			if (imRitenutePositiveSoloCompenso.subtract(imRitenuteFittizieSoloCompenso).compareTo(rigaMandato.getIm_ritenute_riga()) != 0)
				throw new ApplicationException("L'importo delle righe ritenute del compenso associato al mandato non corrisponde con l'importo ritenute associato al mandato.");

			// Effettuo il controllo che il saldo patrimoniale aperto del compenso coincida con la riga del mandato
			BigDecimal imSaldoPatrimoniale = allMovimentiPrimaNotaCompenso.stream()
					.filter(el -> Movimento_cogeBulk.TipoRiga.DEBITO.value().equals(el.getTi_riga()))
					.map(el -> el.isSezioneAvere() ? el.getIm_movimento() : el.getIm_movimento().negate())
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			//se il compenso paga una missione con anticipo, il mandato è al netto dell'anticipo stesso.....
			if (optAnticipo.isPresent()) {
				imSaldoPatrimoniale = imSaldoPatrimoniale.subtract(optAnticipo.map(AnticipoBulk::getIm_anticipo).orElse(BigDecimal.ZERO));

				//se imSaldoPatrimoniale è minore delle ritenute il mandato deve essere stato emesso solo per l'importo delle ritenute
				if (imSaldoPatrimoniale.compareTo(rigaMandato.getIm_ritenute_riga())<0) {
					if (rigaMandato.getIm_ritenute_riga().compareTo(rigaMandato.getIm_mandato_riga()) != 0)
						throw new ApplicationException("Il compenso da missione " + compenso.getEsercizio() + "/" + compenso.getPg_compenso() + ", associato al mandato " +
								mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() + ", al netto dell'anticipo associato ha un importo " +
								"da pagare (" + new it.cnr.contab.util.EuroFormat().format(imSaldoPatrimoniale) + " inferiore alle ritenute da operare (" +
								new it.cnr.contab.util.EuroFormat().format(rigaMandato.getIm_ritenute_riga()) + "). Il mandato dovrebbe avere un importo pari alle sole ritenute da trattenere.");
				} else if (imSaldoPatrimoniale.compareTo(rigaMandato.getIm_mandato_riga()) != 0)
					throw new ApplicationException("La scrittura Prima Nota del compenso da missione " + compenso.getEsercizio() + "/" + compenso.getPg_compenso() + ", associato al mandato "+
							mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +", presenta in Avere movimenti di conti patrimoniali " +
							"per un importo ("+new it.cnr.contab.util.EuroFormat().format(imSaldoPatrimoniale) +
							") che, al netto dell'anticipo associato ("+new it.cnr.contab.util.EuroFormat().format(optAnticipo.map(AnticipoBulk::getIm_anticipo).orElse(BigDecimal.ZERO))+
							"), non coincide con quello del mandato stesso ("+new it.cnr.contab.util.EuroFormat().format(rigaMandato.getIm_mandato_riga()) +").");
			} else if (imSaldoPatrimoniale.compareTo(rigaMandato.getIm_mandato_riga()) != 0)
				throw new ApplicationException("La scrittura Prima Nota del compenso " + compenso.getEsercizio() + "/" + compenso.getPg_compenso() + ", associato al mandato "+
						mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +", presenta in Avere movimenti di conti patrimoniali " +
						"per un importo ("+new it.cnr.contab.util.EuroFormat().format(imSaldoPatrimoniale) +
						") non coincidente con quello del mandato stesso ("+new it.cnr.contab.util.EuroFormat().format(rigaMandato.getIm_mandato_riga()) +").");
		}

		BigDecimal imRitenuteFittizie = righeCori.stream()
				.filter(Contributo_ritenutaBulk::isContributoEnte)
				.filter(el -> el.isTipoContributoRivalsa() || el.isTipoContributoIva())
				.map(Contributo_ritenutaBulk::getAmmontare)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal imRitenutePositive = righeCori.stream()
				.map(Contributo_ritenutaBulk::getAmmontare)
				.filter(el -> el.compareTo(BigDecimal.ZERO) > 0)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		//Aggiungo eventuali quote da trattenere indicate sul compenso e sul compenso conguaglio
		imRitenutePositive = imRitenutePositive.add(Optional.ofNullable(compenso.getIm_netto_da_trattenere()).orElse(BigDecimal.ZERO));
		imRitenutePositive = imRitenutePositive.add(Optional.ofNullable(compensoConguaglio).flatMap(el->Optional.ofNullable(el.getIm_netto_da_trattenere())).orElse(BigDecimal.ZERO));

		//sottraggo le ritenute fittizie
		imRitenutePositive = imRitenutePositive.subtract(imRitenuteFittizie);

		BigDecimal imRitenuteNegative = righeCori.stream().map(Contributo_ritenutaBulk::getAmmontare)
				.filter(el->el.compareTo(BigDecimal.ZERO)<0)
				.reduce(BigDecimal.ZERO, BigDecimal::add).abs();

		BigDecimal imContributiCaricoEnte = righeCori.stream().filter(Contributo_ritenutaBulk::isContributoEnte)
				.map(Contributo_ritenutaBulk::getAmmontare)
				.filter(el->el.compareTo(BigDecimal.ZERO)>0)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		//Per il calcolo del netto mandato effettuo la sottrazione tra:
		//1. il lordo mandato
		//2. le ritenute positive
		BigDecimal imNettoMandato = rigaMandato.getIm_mandato_riga().subtract(imRitenutePositive);

		//Chiudo il patrimoniale principale del compenso.... quello con la partita
		Voce_epBulk voceEpBanca = this.findContoBanca(userContext, CNRUserContext.getEsercizio(userContext));

		final String contoPatrimonialePartita;

		//Se l'importo del mandato coincide con i contributi carico ente, vuol dire che ho solo costi di contributi e non di partita.....
		//quindi il contoPatrimonialePartita non c'è sul compenso
		if (rigaMandato.getIm_mandato_riga().compareTo(imContributiCaricoEnte)==0)
			contoPatrimonialePartita= null;
		else {
			contoPatrimonialePartita = this.findMovimentoAperturaPartita(allMovimentiPrimaNotaCompenso, compenso, compenso.getCd_terzo()).getCd_voce_ep();

			testataPrimaNota.closeDettaglioPatrimonialePartita(compenso, contoPatrimonialePartita, imNettoMandato, compenso.getCd_terzo());
			testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), compenso.getTipoDocumentoEnum().getSezionePatrimoniale(), voceEpBanca.getCd_voce_ep(), imNettoMandato);
		}

		//Registrazione conto CONTRIBUTI-RITENUTE
		righeCori.stream().filter(el->el.getAmmontare().compareTo(BigDecimal.ZERO)>0).forEach(cori->{
			try {
				BigDecimal imCori = cori.getAmmontare();

				// Se la tipologia di contributo ritenuta è IVA o RIVALSA non registro la ritenuta in quanto già registrata in fase di chiusura debito mandato (imNettoMandato contiene imCori)
				// L'Iva, sia per compensa istituzionale che commerciale, va sempre a costo,
				if (!cori.isTipoContributoRivalsa() && !(cori.isTipoContributoIva() && cori.isContributoEnte())) {
					if (cori.isTipoContributoIva() && cori.isContributoPercipiente()) {
						Movimento_cogeBulk movimentoToClose = this.findMovimentoAperturaCoriIVACompenso(allMovimentiPrimaNotaCompenso, cori.getCompenso(), cori.getCompenso().getCd_terzo(), cori.getCd_contributo_ritenuta());
						testataPrimaNota.addDettaglio(movimentoToClose.getTi_riga(), Movimento_cogeBulk.getControSezione(movimentoToClose.getSezione()), movimentoToClose.getCd_voce_ep(), imCori, cori.getCompenso(), movimentoToClose.getCd_terzo(), cori.getCd_contributo_ritenuta());
						testataPrimaNota.closeDettaglioPatrimonialePartita(cori.getCompenso(), contoPatrimonialePartita, imCori, cori.getCompenso().getCd_terzo());
					} else {
						Pair<Voce_epBulk, Voce_epBulk> pairContoCori = this.findPairContiMandato(userContext, cori);
						Voce_epBulk contoVersamentoCori = pairContoCori.getSecond();
						//se rigaMandato.getIm_mandato_riga() è uguale a imContributiCaricoEnte il contoPatrimonialePartita non è impostato.
						//Ma in questo caso non dovrebbe mai verificarsi che ci siano contributi a carico percipiente altrimenti il mandato sarebbe negativo
						if (cori.isContributoPercipiente())
							testataPrimaNota.closeDettaglioPatrimonialePartita(cori.getCompenso(), contoPatrimonialePartita, imCori, cori.getCompenso().getCd_terzo());
						else {
							String contoToClose = this.findMovimentoAperturaCori(allMovimentiPrimaNotaCompenso, cori.getCompenso(), cori.getCompenso().getCd_terzo(), cori.getCd_contributo_ritenuta()).getCd_voce_ep();
							testataPrimaNota.closeDettaglioPatrimonialeCori(cori.getCompenso(), contoToClose, imCori, cori.getCompenso().getCd_terzo(), cori.getCd_contributo_ritenuta());
						}
						testataPrimaNota.openDettaglioPatrimonialeCori(cori.getCompenso(), contoVersamentoCori.getCd_voce_ep(), imCori, cori.getCompenso().getCd_terzo(), cori.getCd_contributo_ritenuta());
					}
				}
			} catch (ComponentException|PersistencyException e) {
				throw new ApplicationRuntimeException(e);
			}
		});

		//se il compenso è da missione con anticipo bilancio il onto tesoreria con l'anticipo stesso
		//se esiste anticipo devo fare registrazioni una serie di registrazioni
		optAnticipo.ifPresent(anticipo->{
			try {
				final Pair<Voce_epBulk, Voce_epBulk> pairContoCostoAnticipo = this.findPairCosto(userContext, optAnticipo.get());

				BigDecimal imCompensoNettoRitenute = compenso.getIm_totale_compenso().subtract(compenso.getIm_cr_ente()).subtract(compenso.getIm_cr_percipiente());
				BigDecimal imCostoAnticipo = anticipo.getIm_anticipo();

				BigDecimal imMovimento = imCostoAnticipo.compareTo(imCompensoNettoRitenute)<0?imCostoAnticipo:imCompensoNettoRitenute;

				List<Movimento_cogeBulk> allMovimentiPrimaNota = this.findMovimentiPrimaNota(userContext,anticipo);
				String contoPatrimonialeAperturaCreditoAnticipo = this.findMovimentoAperturaCreditoAnticipo(allMovimentiPrimaNota, anticipo, anticipo.getCd_terzo()).getCd_voce_ep();

				testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.CREDITO.value(), Movimento_cogeBulk.SEZIONE_AVERE, contoPatrimonialeAperturaCreditoAnticipo, imMovimento, anticipo, anticipo.getCd_terzo(), null);
				testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.DEBITO.value(), Movimento_cogeBulk.SEZIONE_DARE, contoPatrimonialePartita, imMovimento, compenso, compenso.getCd_terzo(), null);
			} catch (ComponentException|PersistencyException|RemoteException e) {
				throw new ApplicationRuntimeException(e);
			}
		});

		if (Optional.ofNullable(compenso.getIm_netto_da_trattenere()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)>0) {
			//Devo chiudere la tesoreria con la quota da trattenere
			Voce_epBulk contoQuotaDaTrattenere = this.findContoQuotaDaTrattenereByConfig(userContext, CNRUserContext.getEsercizio(userContext));

			testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.DEBITO.value(), Movimento_cogeBulk.SEZIONE_DARE, contoQuotaDaTrattenere.getCd_voce_ep(), compenso.getIm_netto_da_trattenere(), compenso, compenso.getCd_terzo(), null);
			testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), Movimento_cogeBulk.SEZIONE_AVERE, voceEpBanca.getCd_voce_ep(), compenso.getIm_netto_da_trattenere());
		}

		return this.generaScrittura(userContext, mandato, Collections.singletonList(testataPrimaNota), true);
	}

	/*
		Questa procedura gestisce la proposta di prima nota di un mandato generato dalle righe negative del compenso
	 */
	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaMandatoCompenso(UserContext userContext, MandatoBulk mandato, CompensoBulk compenso) throws ComponentException, PersistencyException, RemoteException {
		String descMandato = mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato();
		String descEstesaMandato = "mandato " + descMandato + " generato da righe negative del compenso "+compenso.getEsercizio()+"/"+compenso.getCd_unita_organizzativa()+"/"+compenso.getPg_compenso();

		if (mandato.getMandato_rigaColl().isEmpty() || mandato.getMandato_rigaColl().size() > 1)
			throw new ApplicationException("Il mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +
					" ha un numero di righe non coerente con l'unica prevista per un mandato generato da compenso. Proposta di prima nota non possibile.");

		Mandato_rigaBulk rigaMandato = mandato.getMandato_rigaColl().get(0);
		TestataPrimaNota testataPrimaNota = new TestataPrimaNota();

		List<Contributo_ritenutaBulk> righeCoriCompenso = Optional.ofNullable(compenso.getChildren()).orElseGet(() -> {
			try {
				Contributo_ritenutaHome home = (Contributo_ritenutaHome) getHome(userContext, Contributo_ritenutaBulk.class);
				return (java.util.List<Contributo_ritenutaBulk>) home.loadContributiRitenute(compenso);
			} catch (ComponentException | PersistencyException e) {
				throw new DetailedRuntimeException(e);
			}
		});

		//Cerco tra le righeCori quella del mandato.... cerco per importo... e se ne esistono di più per descrizione..... se non si individua lancio eccezione
		List<Contributo_ritenutaBulk> righeCoriMandatoByImporto = righeCoriCompenso.stream()
				.filter(el->el.getAmmontare().compareTo(BigDecimal.ZERO)<0)
				.filter(el->el.getAmmontare().abs().compareTo(rigaMandato.getIm_mandato_riga().subtract(rigaMandato.getIm_ritenute_riga()))==0)
				.collect(Collectors.toList());

		List<Contributo_ritenutaBulk> righeCori = new ArrayList<>();

		if (righeCoriMandatoByImporto.isEmpty())
			throw new ApplicationException("Il " + descEstesaMandato + " ha un importo che non corrisponde a nessuna riga negativa del compenso stesso. Proposta di prima nota non possibile.");
		if (righeCoriMandatoByImporto.size()>1) {
			List<Contributo_ritenutaBulk> righeCoriMandatoByDesc = righeCoriMandatoByImporto.stream()
					.filter(el->rigaMandato.getDs_mandato_riga().contains(el.getCd_contributo_ritenuta()))
					.collect(Collectors.toList());

			if (righeCoriMandatoByDesc.size() != 1)
				throw new ApplicationException("Il " + descEstesaMandato + " ha un importo che corrisponde a più righe negative del compenso stesso. Proposta di prima nota non possibile.");

			righeCori.add(righeCoriMandatoByDesc.get(0));
		}  else
			righeCori.add(righeCoriMandatoByImporto.get(0));

		final CompensoBulk compensoConguaglio;
		if (mandato.getIm_ritenute().compareTo(BigDecimal.ZERO)!=0)
			//verifico se esiste conguaglio collegato al compenso principale pagato con il mandato
			compensoConguaglio = ((CompensoHome) getHome(userContext, CompensoBulk.class)).findCompensoConguaglioAssociato(userContext, compenso);
		else
			compensoConguaglio = null;

		//se esiste aggiungo le righe di contributo/ritenuta in quanto anche loro contribuiscono a determinare il netto mandato
		if (compensoConguaglio != null) {
			String descConguaglio = compensoConguaglio.getEsercizio() + "/" + compensoConguaglio.getCd_cds() + "/" + compensoConguaglio.getPg_compenso();

			List<Contributo_ritenutaBulk> righeCoriConguaglio = Optional.ofNullable(compensoConguaglio.getChildren()).orElseGet(() -> {
				try {
					Contributo_ritenutaHome home = (Contributo_ritenutaHome) getHome(userContext, Contributo_ritenutaBulk.class);
					return (java.util.List<Contributo_ritenutaBulk>) home.loadContributiRitenute(compensoConguaglio);
				} catch (ComponentException | PersistencyException e) {
					throw new DetailedRuntimeException(e);
				}
			});

			List<Contributo_ritenutaBulk> righeCoriConguaglioRitenute = righeCoriConguaglio.stream().filter(el->el.getAmmontare().compareTo(BigDecimal.ZERO)>0).collect(Collectors.toList());

			BigDecimal totRitenute = righeCoriConguaglioRitenute.stream().map(Contributo_ritenutaBulk::getAmmontare)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			if (totRitenute.compareTo(mandato.getIm_ritenute())==0) {
				//Aggiungo alle righe da processare solo le righe cori positive che generano ritenute
				righeCori.addAll(righeCoriConguaglioRitenute);
			} else {
				//cerco di recuperare puntualmente la ritenuta
				List<Ass_mandato_reversaleBulk> result = ((Ass_mandato_reversaleHome) getHome(userContext, Ass_mandato_reversaleBulk.class)).findReversali(userContext, mandato, false);
				for (Ass_mandato_reversaleBulk assManRev : result) {
					ReversaleIHome revHome = (ReversaleIHome) getHome(userContext, ReversaleIBulk.class);
					ReversaleBulk reversale = (ReversaleIBulk) revHome.findByPrimaryKey(new ReversaleIBulk(assManRev.getCd_cds_reversale(), assManRev.getEsercizio_reversale(), assManRev.getPg_reversale()));

					//Cerco tra le righeCori quella della reversale.... cerco per importo... e se ne esistono di più per descrizione..... se non si individua lancio eccezione
					List<Contributo_ritenutaBulk> righeCoriReversaleByImporto = righeCoriConguaglioRitenute.stream()
							.filter(el->el.getAmmontare().compareTo(BigDecimal.ZERO)>0)
							.filter(el->el.getAmmontare().abs().compareTo(reversale.getIm_reversale())==0)
							.collect(Collectors.toList());

					if (righeCoriReversaleByImporto.isEmpty())
						throw new ApplicationException("Il " + descEstesaMandato + " risulta avere una ritenuta di importo che non corrisponde a nessuna delle ritenute " +
								" generate dal conguaglio associato " + descConguaglio + ". Proposta di prima nota non possibile.");
					if (righeCoriReversaleByImporto.size()>1) {
						List<Contributo_ritenutaBulk> righeCoriReversaleByDesc = righeCoriReversaleByImporto.stream()
								.filter(el->reversale.getDs_reversale().contains(el.getCd_contributo_ritenuta()))
								.collect(Collectors.toList());

						if (righeCoriReversaleByDesc.size() != 1)
							throw new ApplicationException("Il " + descEstesaMandato + " risulta avere una ritenuta di importo che corrisponde a più ritenute " +
									" generate dal conguaglio associato " + descConguaglio + ". Proposta di prima nota non possibile.");
						righeCori.add(righeCoriReversaleByDesc.get(0));
					}  else
						righeCori.add(righeCoriReversaleByImporto.get(0));
				}
			}
		}

		//Elaboro le righe Cori che devono aver generato il mandato e le reversali
		righeCori.stream().filter(el->el.getAmmontare().compareTo(BigDecimal.ZERO)!=0).forEach(cori-> {
			try {
				// Se la tipologia di contributo ritenuta è IVA o RIVALSA non registro la ritenuta in quanto già registrata in fase di chiusura debito mandato (imNettoMandato contiene imCori)
				// L'Iva, sia per compensa istituzionale che commerciale, va sempre a costo,
				if (!cori.isTipoContributoRivalsa() && !(cori.isTipoContributoIva() && cori.isContributoEnte())) {
					BigDecimal imCori = cori.getAmmontare();

					//Chiudo il patrimoniale principale del compenso.... quello con la partita
					Voce_epBulk voceEpBanca = this.findContoBanca(userContext, CNRUserContext.getEsercizio(userContext));

					if (cori.isTipoContributoIva() && cori.isContributoPercipiente()) {
						//recupero la scrittura del compenso
						List<Movimento_cogeBulk> allMovimentiPrimaNotaCompenso = this.findMovimentiPrimaNota(userContext, cori.getCompenso());
						Movimento_cogeBulk movimentoToClose = this.findMovimentoAperturaCoriIVACompenso(allMovimentiPrimaNotaCompenso, cori.getCompenso(), cori.getCompenso().getCd_terzo(), cori.getCd_contributo_ritenuta());
						testataPrimaNota.addDettaglio(movimentoToClose.getTi_riga(), Movimento_cogeBulk.getControSezione(movimentoToClose.getSezione()), movimentoToClose.getCd_voce_ep(), imCori, cori.getCompenso(), movimentoToClose.getCd_terzo(), cori.getCd_contributo_ritenuta());
						testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), movimentoToClose.getSezione(), voceEpBanca.getCd_voce_ep(), imCori);
					} else {
						Pair<Voce_epBulk, Voce_epBulk> pairContoCori = this.findPairContiMandato(userContext, cori);
						Voce_epBulk contoVersamentoCori = pairContoCori.getSecond();
						//se rigaMandato.getIm_mandato_riga() è uguale a imContributiCaricoEnte il contoPatrimonialePartita non è impostato.
						//Ma in questo caso non dovrebbe mai verificarsi che ci siano contributi a carico percipiente altrimenti il mandato sarebbe negativo
						if (cori.isContributoPercipiente()) {
							testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), Movimento_cogeBulk.getControSezione(cori.getCompenso().getTipoDocumentoEnum().getSezionePatrimoniale()), voceEpBanca.getCd_voce_ep(), imCori);
							testataPrimaNota.openDettaglioPatrimonialeCori(cori.getCompenso(), contoVersamentoCori.getCd_voce_ep(), imCori, cori.getCompenso().getCd_terzo(), cori.getCd_contributo_ritenuta());
						} else {
							//recupero la scrittura del compenso
							List<Movimento_cogeBulk> allMovimentiPrimaNotaCompenso = this.findMovimentiPrimaNota(userContext, cori.getCompenso());
							Movimento_cogeBulk movimentoCoriToClose = this.findMovimentoAperturaCori(allMovimentiPrimaNotaCompenso, cori.getCompenso(), cori.getCompenso().getCd_terzo(), cori.getCd_contributo_ritenuta());
							if (!movimentoCoriToClose.getIm_movimento().equals(imCori.abs()) || !movimentoCoriToClose.getSezione().equals(Movimento_cogeBulk.SEZIONE_DARE))
								throw new ApplicationException("Il " + descEstesaMandato + " ha un importo che non corrisponde al saldo (" +
										new it.cnr.contab.util.EuroFormat().format(movimentoCoriToClose.getIm_movimento()) +
										") del compenso stesso registrato per il contributo " + cori.getCd_contributo_ritenuta() + ". Proposta di prima nota non possibile.");
							//chiudo il credito verso l'ente e rilevo il minor versamento
							testataPrimaNota.addDettaglio(movimentoCoriToClose.getTi_riga(), Movimento_cogeBulk.getControSezione(movimentoCoriToClose.getSezione()), movimentoCoriToClose.getCd_voce_ep(), imCori.abs(), cori.getCompenso(), movimentoCoriToClose.getCd_terzo(), movimentoCoriToClose.getCd_contributo_ritenuta());
							testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.DEBITO.value(), movimentoCoriToClose.getSezione(), contoVersamentoCori.getCd_voce_ep(), imCori.abs(), cori.getCompenso(), movimentoCoriToClose.getCd_terzo(), movimentoCoriToClose.getCd_contributo_ritenuta());

							if (mandato.getIm_ritenute().compareTo(BigDecimal.ZERO)==0) {
								//apro il conto credito associato alla voce della partita giro e chiudo la tesoreria
								Voce_epBulk aContoRicavo = this.findContoCostoRicavo(userContext, rigaMandato.getElemento_voce());

								testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.CREDITO.value(), Movimento_cogeBulk.SEZIONE_DARE, aContoRicavo.getCd_voce_ep(), imCori.abs());
								testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), Movimento_cogeBulk.SEZIONE_AVERE, voceEpBanca.getCd_voce_ep(), imCori.abs());
							}
						}
					}
				}
			} catch (ComponentException | RemoteException | PersistencyException e) {
				throw new DetailedRuntimeException(e);
			}
		});

		if (mandato.getIm_ritenute().compareTo(BigDecimal.ZERO)!=0 && compensoConguaglio == null) {
			List<Ass_mandato_reversaleBulk> result = ((Ass_mandato_reversaleHome) getHome(userContext, Ass_mandato_reversaleBulk.class)).findReversali(userContext, mandato, false);

			List<ReversaleBulk> reversali = new ArrayList<>();

			for (Ass_mandato_reversaleBulk assManRev : result) {
				ReversaleIHome revHome = (ReversaleIHome) getHome(userContext, ReversaleIBulk.class);
				reversali.add((ReversaleIBulk) revHome.findByPrimaryKey(new ReversaleIBulk(assManRev.getCd_cds_reversale(), assManRev.getEsercizio_reversale(), assManRev.getPg_reversale())));
			}

			BigDecimal totReversali = reversali.stream().map(ReversaleBulk::getIm_reversale)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			if (totReversali.compareTo(mandato.getIm_ritenute())!=0)
				throw new ApplicationException("Il " + descEstesaMandato + " risulta avere un importo ritenute che non corrisponde alla somma delle reversali" +
						" effettivamente vincolate. Proposta di prima nota non possibile.");

			//aggiungo la scrittura della reversale vincolata al mandato
			reversali.forEach(reversale->{
				TestataPrimaNota testataPrimaNotaReversale = null;
				try {
					testataPrimaNotaReversale = proposeTestataPrimaNotaReversale(userContext, reversale, false);
				} catch (ComponentException e) {
					throw new DetailedRuntimeException(e);
				} catch (ScritturaPartitaDoppiaNotRequiredException ignored) {
				}
				Optional.ofNullable(testataPrimaNotaReversale).ifPresent(el->testataPrimaNota.getDett().addAll(el.getDett()));
			});
		}

		return this.generaScrittura(userContext, mandato, Collections.singletonList(testataPrimaNota), false);
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaMandatoStipendi(UserContext userContext, MandatoBulk mandato) throws ComponentException, PersistencyException, RemoteException {
		//recupero il documento generico passivo leggendolo dalla tabelle stipendiCofiBulk
		Stipendi_cofiBulk stipendiCofiBulk = ((Stipendi_cofiHome)getHome(userContext, Stipendi_cofiBulk.class)).findStipendiCofi(mandato);

		if (!Optional.ofNullable(stipendiCofiBulk).isPresent())
			throw new ApplicationException("Il mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +
					" non risulta pagare uno stipendio. Proposta di prima nota non possibile.");

		CompensoBulk compenso = (CompensoBulk)getHome(userContext, CompensoBulk.class).findByPrimaryKey(new CompensoBulk(stipendiCofiBulk.getCd_cds_comp(), stipendiCofiBulk.getCd_uo_comp(), stipendiCofiBulk.getEsercizio_comp(),
				stipendiCofiBulk.getPg_comp()));

		List<Contributo_ritenutaBulk> righeCori = Optional.ofNullable(compenso.getChildren()).orElseGet(()->{
			try {
				Contributo_ritenutaHome home = (Contributo_ritenutaHome) getHome(userContext, Contributo_ritenutaBulk.class);
				return (java.util.List<Contributo_ritenutaBulk>)home.loadContributiRitenute(compenso);
			} catch (ComponentException | PersistencyException e) {
				throw new DetailedRuntimeException(e);
			}
		});

		//Recupero dal compenso tutti i conti patrimoniali aperti
		List<Movimento_cogeBulk> movimenti = this.findMovimentiPrimaNota(userContext, compenso);
		List<Movimento_cogeBulk> dettPnPatrimonialePartita = this.findMovimentiPatrimoniali(movimenti, compenso);

		TestataPrimaNota testataPrimaNota = new TestataPrimaNota();

		//Chiudo i conti patrimoniali
		Voce_epBulk voceEpBanca = this.findContoBanca(userContext, CNRUserContext.getEsercizio(userContext));
		dettPnPatrimonialePartita.stream().filter(el->!Optional.ofNullable(el.getCd_contributo_ritenuta()).isPresent()).forEach(dettPN->{
			Partita partita = new Partita(dettPN);
			testataPrimaNota.closeDettaglioPatrimonialePartita(partita, dettPN.getCd_voce_ep(), dettPN.getIm_movimento(), partita.getCd_terzo());
			testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), compenso.getTipoDocumentoEnum().getSezionePatrimoniale(), voceEpBanca.getCd_voce_ep(), dettPN.getIm_movimento());
		});

		//Devo leggere la reversale vincolata
		BigDecimal imRitenute = righeCori.stream().map(Contributo_ritenutaBulk::getAmmontare)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal imRitenutePositive = righeCori.stream().map(Contributo_ritenutaBulk::getAmmontare)
				.filter(el->el.compareTo(BigDecimal.ZERO)>0)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		if (imRitenutePositive.compareTo(mandato.getIm_ritenute())!=0)
			throw new ApplicationException("L'importo delle righe ritenute del compenso associato al mandato non corrisponde con l'importo ritenute associato al mandato.");

		//Registrazione conto CONTRIBUTI-RITENUTE
		//Solo ritenute con importo positivo perchè quelle negative generano mandato a parte
		righeCori.stream().filter(el->el.getAmmontare().compareTo(BigDecimal.ZERO)>0).forEach(cori->{
			try {
				BigDecimal imCori = cori.getAmmontare();

				// Se la tipologia di contributo ritenuta è IVA o RIVALSA non registro la ritenuta in quanto già registrata in fase di chiusura debito mandato (imNettoMandato contiene imCori)
				if (!cori.isTipoContributoRivalsa() && !(cori.isTipoContributoIva() && cori.isContributoEnte())) {
					Pair<Voce_epBulk, Voce_epBulk> pairContoCori = this.findPairContiMandato(userContext, cori);
					Voce_epBulk contoVersamentoCori = pairContoCori.getSecond();
					//Riduco la tesoreria e rilevo il debito verso l'erario per le ritenute carico ente/percipiente
					testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), Movimento_cogeBulk.getControSezione(compenso.getTipoDocumentoEnum().getSezionePatrimoniale()), voceEpBanca.getCd_voce_ep(), imCori);
					testataPrimaNota.openDettaglioPatrimonialeCori(compenso, contoVersamentoCori.getCd_voce_ep(), imCori, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
				}
			} catch (ComponentException|PersistencyException e) {
				throw new ApplicationRuntimeException(e);
			}
		});

		return this.generaScrittura(userContext, mandato, Collections.singletonList(testataPrimaNota), true);
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaMandatoLiquidazione(UserContext userContext, MandatoBulk mandato, List<Liquidazione_ivaBulk> liquidIvaList) throws ComponentException, PersistencyException, RemoteException {
		if (liquidIvaList == null || liquidIvaList.isEmpty())
			throw new ApplicationException("Il mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +
					" non risulta pagare una liquidazione iva. Proposta di prima nota non possibile.");

		List<Tipo_sezionaleBulk> allTipiSezionale = getHome(userContext, Tipo_sezionaleBulk.class).findAll();
		List<IDocumentoAmministrativoBulk> allDocumentiLiquidati = new ArrayList<>();

		Fattura_passiva_IHome fatturaPassivaIHome = (Fattura_passiva_IHome)getHome(userContext, Fattura_passiva_IBulk.class);
		Nota_di_creditoHome notaCreditoPassivaHome = (Nota_di_creditoHome)getHome(userContext, Nota_di_creditoBulk.class);
		Nota_di_debitoHome notaDebitoPassivaHome = (Nota_di_debitoHome)getHome(userContext, Nota_di_debitoBulk.class);
		Fattura_attiva_IHome fatturaAttivaIHome = (Fattura_attiva_IHome)getHome(userContext, Fattura_attiva_IBulk.class);
		Nota_di_credito_attivaHome notaCreditoAttivaHome = (Nota_di_credito_attivaHome)getHome(userContext, Nota_di_credito_attivaBulk.class);
		Nota_di_debito_attivaHome notaDebitoAttivaHome = (Nota_di_debito_attivaHome)getHome(userContext, Nota_di_debito_attivaBulk.class);
		Liquidazione_ivaHome liquidazioneIvaHome = (Liquidazione_ivaHome) getHome(userContext, Liquidazione_ivaBulk.class);

		Voce_epBulk voceEpBanca = this.findContoBanca(userContext, CNRUserContext.getEsercizio(userContext));

		Optional<Scrittura_partita_doppiaBulk> scritturaMandato = Optional.ofNullable(mandato.getScrittura_partita_doppia())
				.map(spd->Optional.of(spd).filter(el->el.getCrudStatus()!=OggettoBulk.UNDEFINED).orElseGet(()-> {
					try {
						Scrittura_partita_doppiaHome home = (Scrittura_partita_doppiaHome) getHome(userContext, Scrittura_partita_doppiaBulk.class);
						return home.getScrittura(userContext, mandato, Boolean.FALSE).orElse(null);
					} catch (ComponentException e) {
						throw new DetailedRuntimeException(e);
					}
				}));

		//recupero il terzo associato alla UO necessario per chiudere i conti IVA
		TerzoBulk terzoEnte = ((TerzoHome)getHome(userContext, TerzoBulk.class)).findTerzoEnte();
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);

		TestataPrimaNota testataPrimaNota = new TestataPrimaNota();

		//recupero tutte le scritture di liquidazione
		for (Liquidazione_ivaBulk liquidIva : liquidIvaList) {
			List<Liquidazione_ivaBulk> liquidazioniDefinitive = liquidazioneIvaHome.findLiquidazioniMassiveDefinitiveList(liquidIva);

			liquidazioniDefinitive.stream().filter(el->!el.getCd_unita_organizzativa().equals(uoEnte.getCd_unita_organizzativa())).forEach(liqIvaUo->{
				try {
					Map<String, Pair<String, BigDecimal>> saldiVoceEp = this.getSaldiMovimentiCoriIvaDebitoEnte(userContext, liqIvaUo);

					if (saldiVoceEp.size()>1)
						throw new ApplicationRuntimeException("Errore nella generazione scrittura prima nota del mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato()+
								". La scrittura prima nota della liquidazione IVA, di tipo "+liqIvaUo.getTipo_liquidazione()+
								", della UO " + liqIvaUo.getCd_unita_organizzativa() + " per il periodo "+new java.text.SimpleDateFormat("dd/MM/yyyy").format(liqIvaUo.getDt_inizio())+
								" - "+new java.text.SimpleDateFormat("dd/MM/yyyy").format(liqIvaUo.getDt_fine())+" movimenta più conti patrimoniali verso l'ente. Proposta prima nota non possibile.");

					//Trovo il saldo
					BigDecimal saldoDebitoEnte = saldiVoceEp.values().stream()
							.map(el->el.getFirst().equals(Movimento_cogeBulk.SEZIONE_DARE)?el.getSecond():el.getSecond().negate())
							.reduce(BigDecimal.ZERO,BigDecimal::add);

					BigDecimal ivaDebCredPrec = liqIvaUo.getIva_deb_cred_per_prec();

					saldoDebitoEnte = saldoDebitoEnte.add(ivaDebCredPrec);

					if  (saldoDebitoEnte.compareTo(liqIvaUo.getIva_da_versare()) != 0) {
						testataPrimaNota.getDett().stream().filter(el->el.getPartita()!=null).forEach(el-> System.out.println("Compenso: "+Optional.of(el.getPartita()).map(IDocumentoCogeBulk::getPg_doc).orElse(null)+" - Trib: "+el.getCdCori()+" - Conto: "+el.getCdConto()+
								" - Sezione: "+el.getSezione() + " - Importo: " + new it.cnr.contab.util.EuroFormat().format(el.getImporto())));

						throw new ApplicationRuntimeException("Errore nella generazione scrittura prima nota del mandato " +
								mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() + ". Il saldo del conto debito generato dalla scrittura prima nota della liquidazione IVA, " +
								"di tipo "+liqIvaUo.getTipo_liquidazione()+", della UO " + liqIvaUo.getCd_unita_organizzativa() +
								" per il periodo "+new java.text.SimpleDateFormat("dd/MM/yyyy").format(liqIvaUo.getDt_inizio())+
								" - "+new java.text.SimpleDateFormat("dd/MM/yyyy").format(liqIvaUo.getDt_fine())+" ("+new it.cnr.contab.util.EuroFormat().format(saldoDebitoEnte)+
								") non risulterebbe essere uguale all'importo della liquidazione IVA ("+
								new it.cnr.contab.util.EuroFormat().format(liqIvaUo.getIva_da_versare()) + ").");
					}

					saldiVoceEp.keySet().forEach(cdVoceEp -> {
						Pair<String, BigDecimal> saldoVoce = saldiVoceEp.get(cdVoceEp);
						if (saldoVoce.getSecond().compareTo(BigDecimal.ZERO) != 0)
							if (saldoVoce.getFirst().equals(Movimento_cogeBulk.SEZIONE_AVERE)) {
								//Chiudo il debito IVA fattura e lo giro all'ente
								testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.DEBITO.value(), Movimento_cogeBulk.SEZIONE_DARE, cdVoceEp, saldoVoce.getSecond(), null, terzoEnte.getCd_terzo(), null);
								testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), Movimento_cogeBulk.SEZIONE_AVERE, voceEpBanca.getCd_voce_ep(), saldoVoce.getSecond());
							} else {
								testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.DEBITO.value(), Movimento_cogeBulk.SEZIONE_AVERE, cdVoceEp, saldoVoce.getSecond(), null, terzoEnte.getCd_terzo(), null);
								testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), Movimento_cogeBulk.SEZIONE_DARE, voceEpBanca.getCd_voce_ep(), saldoVoce.getSecond());
							}
					});
				} catch(ComponentException|PersistencyException ex) {
					throw new ApplicationRuntimeException(ex);
				}
			});
		}

		return this.generaScrittura(userContext, mandato, Collections.singletonList(testataPrimaNota), true);
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaLiquidazioneUo(UserContext userContext, Liquidazione_ivaBulk liqIvaUo) throws ComponentException {
		try {
			Fattura_passiva_IHome fatturaPassivaIHome = (Fattura_passiva_IHome) getHome(userContext, Fattura_passiva_IBulk.class);
			Nota_di_creditoHome notaCreditoPassivaHome = (Nota_di_creditoHome) getHome(userContext, Nota_di_creditoBulk.class);
			Nota_di_debitoHome notaDebitoPassivaHome = (Nota_di_debitoHome) getHome(userContext, Nota_di_debitoBulk.class);
			Fattura_attiva_IHome fatturaAttivaIHome = (Fattura_attiva_IHome) getHome(userContext, Fattura_attiva_IBulk.class);
			Nota_di_credito_attivaHome notaCreditoAttivaHome = (Nota_di_credito_attivaHome) getHome(userContext, Nota_di_credito_attivaBulk.class);
			Nota_di_debito_attivaHome notaDebitoAttivaHome = (Nota_di_debito_attivaHome) getHome(userContext, Nota_di_debito_attivaBulk.class);
			Liquidazione_ivaHome liquidazioneIvaHome = (Liquidazione_ivaHome) getHome(userContext, Liquidazione_ivaBulk.class);

			//verifico innanzitutto che la luidazione è definitiva
			if (!liqIvaUo.isDefinitiva())
				throw new ApplicationRuntimeException("Errore nella generazione scrittura prima nota del tipo liquidazione IVA " + liqIvaUo.getTipo_liquidazione() +
						" della UO " + liqIvaUo.getCd_unita_organizzativa() + " per il periodo " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(liqIvaUo.getDt_inizio()) +
						" - " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(liqIvaUo.getDt_fine()) + ". La liquidazione non risulta in stato definitivo.");

			Optional<Scrittura_partita_doppiaBulk> scritturaLiquidazione = Optional.ofNullable(liqIvaUo.getScrittura_partita_doppia())
					.map(spd -> Optional.of(spd).filter(el -> el.getCrudStatus() != OggettoBulk.UNDEFINED).orElseGet(() -> {
						try {
							Scrittura_partita_doppiaHome home = (Scrittura_partita_doppiaHome) getHome(userContext, Scrittura_partita_doppiaBulk.class);
							return home.getScrittura(userContext, liqIvaUo, Boolean.FALSE).orElse(null);
						} catch (ComponentException e) {
							throw new DetailedRuntimeException(e);
						}
					}));

			List<Tipo_sezionaleBulk> allTipiSezionale = getHome(userContext, Tipo_sezionaleBulk.class).findAll();
			//Recupero tutti i registri in base al tipo sezionale oggetto di liquidazione
			List<Tipo_sezionaleBulk> tipiSezionale = allTipiSezionale.stream().filter(el -> el.isTipoSezionale(liqIvaUo.getTipo_liquidazione())).collect(Collectors.toList());

			//recupero il terzo associato alla UO necessario per chiudere i conti IVA
			TerzoBulk terzoEnte = ((TerzoHome) getHome(userContext, TerzoBulk.class)).findTerzoEnte();

			TestataPrimaNota testataPrimaNota = new TestataPrimaNota();

			for (Tipo_sezionaleBulk tipoSezionale : tipiSezionale) {
				List<IDocumentoAmministrativoBulk> allDocumentiUoLiquidati = new ArrayList<>();
				if (tipoSezionale.isAcquisti()) {
					{
						SQLBuilder sqlFatturaPassiva = fatturaPassivaIHome.createSQLBuilder();
						sqlFatturaPassiva.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, liqIvaUo.getCd_unita_organizzativa());
						sqlFatturaPassiva.addClause(FindClause.AND, "dt_registrazione", SQLBuilder.GREATER_EQUALS, liqIvaUo.getDt_inizio());
						sqlFatturaPassiva.addClause(FindClause.AND, "dt_registrazione", SQLBuilder.LESS_EQUALS, liqIvaUo.getDt_fine());
						sqlFatturaPassiva.addClause(FindClause.AND, "cd_tipo_sezionale", SQLBuilder.EQUALS, tipoSezionale.getCd_tipo_sezionale());
						allDocumentiUoLiquidati.addAll(fatturaPassivaIHome.fetchAll(sqlFatturaPassiva));
					}
					{
						SQLBuilder sqlNotaCreditoPassiva = notaCreditoPassivaHome.createSQLBuilder();
						sqlNotaCreditoPassiva.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, liqIvaUo.getCd_unita_organizzativa());
						sqlNotaCreditoPassiva.addClause(FindClause.AND, "dt_registrazione", SQLBuilder.GREATER_EQUALS, liqIvaUo.getDt_inizio());
						sqlNotaCreditoPassiva.addClause(FindClause.AND, "dt_registrazione", SQLBuilder.LESS_EQUALS, liqIvaUo.getDt_fine());
						sqlNotaCreditoPassiva.addClause(FindClause.AND, "cd_tipo_sezionale", SQLBuilder.EQUALS, tipoSezionale.getCd_tipo_sezionale());
						allDocumentiUoLiquidati.addAll(notaCreditoPassivaHome.fetchAll(sqlNotaCreditoPassiva));
					}
					{
						SQLBuilder sqlNotaDebitoPassiva = notaDebitoPassivaHome.createSQLBuilder();
						sqlNotaDebitoPassiva.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, liqIvaUo.getCd_unita_organizzativa());
						sqlNotaDebitoPassiva.addClause(FindClause.AND, "dt_registrazione", SQLBuilder.GREATER_EQUALS, liqIvaUo.getDt_inizio());
						sqlNotaDebitoPassiva.addClause(FindClause.AND, "dt_registrazione", SQLBuilder.LESS_EQUALS, liqIvaUo.getDt_fine());
						sqlNotaDebitoPassiva.addClause(FindClause.AND, "cd_tipo_sezionale", SQLBuilder.EQUALS, tipoSezionale.getCd_tipo_sezionale());
						allDocumentiUoLiquidati.addAll(notaDebitoPassivaHome.fetchAll(sqlNotaDebitoPassiva));
					}
				} else {
					{
						SQLBuilder sqlFatturaAttiva = fatturaAttivaIHome.createSQLBuilder();
						sqlFatturaAttiva.addClause(FindClause.AND, "cd_uo_origine", SQLBuilder.EQUALS, liqIvaUo.getCd_unita_organizzativa());
						sqlFatturaAttiva.addClause(FindClause.AND, "dt_registrazione", SQLBuilder.GREATER_EQUALS, liqIvaUo.getDt_inizio());
						sqlFatturaAttiva.addClause(FindClause.AND, "dt_registrazione", SQLBuilder.LESS_EQUALS, liqIvaUo.getDt_fine());
						sqlFatturaAttiva.addClause(FindClause.AND, "cd_tipo_sezionale", SQLBuilder.EQUALS, tipoSezionale.getCd_tipo_sezionale());
						allDocumentiUoLiquidati.addAll(fatturaAttivaIHome.fetchAll(sqlFatturaAttiva));
					}
					{
						SQLBuilder sqlNotaCreditoAttiva = notaCreditoAttivaHome.createSQLBuilder();
						sqlNotaCreditoAttiva.addClause(FindClause.AND, "cd_uo_origine", SQLBuilder.EQUALS, liqIvaUo.getCd_unita_organizzativa());
						sqlNotaCreditoAttiva.addClause(FindClause.AND, "dt_registrazione", SQLBuilder.GREATER_EQUALS, liqIvaUo.getDt_inizio());
						sqlNotaCreditoAttiva.addClause(FindClause.AND, "dt_registrazione", SQLBuilder.LESS_EQUALS, liqIvaUo.getDt_fine());
						sqlNotaCreditoAttiva.addClause(FindClause.AND, "cd_tipo_sezionale", SQLBuilder.EQUALS, tipoSezionale.getCd_tipo_sezionale());
						allDocumentiUoLiquidati.addAll(notaCreditoAttivaHome.fetchAll(sqlNotaCreditoAttiva));
					}
					{
						SQLBuilder sqlNotaDebitoAttiva = notaDebitoAttivaHome.createSQLBuilder();
						sqlNotaDebitoAttiva.addClause(FindClause.AND, "cd_uo_origine", SQLBuilder.EQUALS, liqIvaUo.getCd_unita_organizzativa());
						sqlNotaDebitoAttiva.addClause(FindClause.AND, "dt_registrazione", SQLBuilder.GREATER_EQUALS, liqIvaUo.getDt_inizio());
						sqlNotaDebitoAttiva.addClause(FindClause.AND, "dt_registrazione", SQLBuilder.LESS_EQUALS, liqIvaUo.getDt_fine());
						sqlNotaDebitoAttiva.addClause(FindClause.AND, "cd_tipo_sezionale", SQLBuilder.EQUALS, tipoSezionale.getCd_tipo_sezionale());
						allDocumentiUoLiquidati.addAll(notaDebitoAttivaHome.fetchAll(sqlNotaDebitoAttiva));
					}
				}

				Voce_epBulk aVoceErarioContoIva = this.findContoErarioContoIva(userContext, liqIvaUo.getEsercizio());

				//Con la lista dei documenti che sono andati in liquidazione, vado a recuperare le scritture generate
				for (IDocumentoAmministrativoBulk docamm : allDocumentiUoLiquidati) {
					Integer cdTerzoDocamm;
					if (docamm instanceof Fattura_passivaBulk)
						cdTerzoDocamm = ((Fattura_passivaBulk)docamm).getCd_terzo();
					else
						cdTerzoDocamm = ((Fattura_attivaBulk)docamm).getCd_terzo();

					Map<String, Map<String, Pair<String, BigDecimal>>> saldiCoriVoceEp;

					//recupero tutti i movimenti della partita per ottenere il saldo al netto della scrittura del mandato se già esiste
					if (Optional.of(docamm).filter(Fattura_passivaBulk.class::isInstance).map(Fattura_passivaBulk.class::cast).map(Fattura_passivaBulk::isGenerataDaCompenso).orElse(Boolean.FALSE))
						// Le fatture generate da compenso non creano scritture di prima nota in quanto create direttamente dal compenso stesso
						saldiCoriVoceEp = this.getSaldiMovimentiCoriIva(userContext, ((Fattura_passivaBulk) docamm).getCompenso());
					else
						saldiCoriVoceEp = this.getSaldiMovimentiCoriIva(userContext, docamm);

					//dovrei trovare tra i saldi proprio l'import liquidato
					//Il conto aperto deve essere solo uno per segno...... la presenza di 2 segni capita per le fatture Commerciali con Split
					if (saldiCoriVoceEp.values().stream().flatMap(el -> el.values().stream()).filter(el->el.getFirst().equals(Movimento_cogeBulk.SEZIONE_DARE))
							.filter(el -> el.getSecond().compareTo(BigDecimal.ZERO) != 0).count() > 1)
						throw new ApplicationRuntimeException("Per il documento " + docamm.getCd_tipo_doc_amm() + "/" + docamm.getEsercizio() + "/" + docamm.getCd_cds() + "/" +
								docamm.getPg_doc() + " e per le righe IVA esiste più di un conto che presenta un saldo positivo in segno Dare.");

					if (saldiCoriVoceEp.values().stream().flatMap(el -> el.values().stream()).filter(el->el.getFirst().equals(Movimento_cogeBulk.SEZIONE_AVERE))
							.filter(el -> el.getSecond().compareTo(BigDecimal.ZERO) != 0).count() > 1)
						throw new ApplicationRuntimeException("Per il documento " + docamm.getCd_tipo_doc_amm() + "/" + docamm.getEsercizio() + "/" + docamm.getCd_cds() + "/" +
								docamm.getPg_doc() + " e per le righe IVA esiste più di un conto che presenta un saldo positivo in segno Avere.");

					saldiCoriVoceEp.keySet().forEach(aCdCori -> {
						Map<String, Pair<String, BigDecimal>> saldiCori = saldiCoriVoceEp.get(aCdCori);
						saldiCori.keySet().forEach(cdVoceEp -> {
							Pair<String, BigDecimal> saldoVoce = saldiCori.get(cdVoceEp);
							if (saldoVoce.getSecond().compareTo(BigDecimal.ZERO) != 0)
								if (saldoVoce.getFirst().equals(Movimento_cogeBulk.SEZIONE_AVERE)) {
									//Chiudo il debito IVA fattura e lo giro all'ente
									testataPrimaNota.addDettaglio(docamm.getTipoDocumentoEnum().getTipoPatrimoniale(), Movimento_cogeBulk.SEZIONE_DARE, cdVoceEp, saldoVoce.getSecond(), docamm, cdTerzoDocamm, aCdCori);
									testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.DEBITO.value(), Movimento_cogeBulk.SEZIONE_AVERE, aVoceErarioContoIva.getCd_voce_ep(), saldoVoce.getSecond(), null, terzoEnte.getCd_terzo(), null);
								} else {
									testataPrimaNota.addDettaglio(docamm.getTipoDocumentoEnum().getTipoPatrimoniale(), Movimento_cogeBulk.SEZIONE_AVERE, cdVoceEp, saldoVoce.getSecond(), docamm, cdTerzoDocamm, aCdCori);
									testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.DEBITO.value(), Movimento_cogeBulk.SEZIONE_DARE, aVoceErarioContoIva.getCd_voce_ep(), saldoVoce.getSecond(), null, terzoEnte.getCd_terzo(), null);
								}
						});
					});
				}
			}

			if (liqIvaUo.getIva_liq_esterna().compareTo(BigDecimal.ZERO)!=0) {
				//Cambio il segno perchè se negativo vuol dire che ho un debito
				BigDecimal importoIvaEsterna = liqIvaUo.getIva_liq_esterna().negate();
				Voce_epBulk contoCostoLiquidazioneIvaEsterna = this.findContoCostoLiquidazioneIvaEsterna(userContext, CNRUserContext.getEsercizio(userContext));
				Voce_epBulk contoDebitoUoLiquidazioneIvaEsterna = this.findContoDebitoUoLiquidazioneIvaEsterna(userContext, CNRUserContext.getEsercizio(userContext));
				Voce_epBulk contoDebitoEnteLiquidazioneIvaEsterna = this.findContoDebitoUoLiquidazioneIvaEsterna(userContext, CNRUserContext.getEsercizio(userContext));
				TerzoBulk terzoUo = ((TerzoHome) getHome(userContext, TerzoBulk.class)).findTerzoPerUo(new Unita_organizzativaBulk(liqIvaUo.getCd_unita_organizzativa()));
				testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.COSTO.value(), Movimento_cogeBulk.SEZIONE_DARE, contoCostoLiquidazioneIvaEsterna.getCd_voce_ep(), importoIvaEsterna);
				testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.DEBITO.value(), Movimento_cogeBulk.SEZIONE_AVERE, contoDebitoUoLiquidazioneIvaEsterna.getCd_voce_ep(), importoIvaEsterna, null, terzoUo.getCd_terzo(), null);

				testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.DEBITO.value(), Movimento_cogeBulk.SEZIONE_DARE, contoDebitoUoLiquidazioneIvaEsterna.getCd_voce_ep(), importoIvaEsterna, null, terzoUo.getCd_terzo(), null);
				testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.DEBITO.value(), Movimento_cogeBulk.SEZIONE_AVERE, contoDebitoEnteLiquidazioneIvaEsterna.getCd_voce_ep(), importoIvaEsterna, null, terzoEnte.getCd_terzo(), null);
			}

			BigDecimal ivaDebCredPrec = liqIvaUo.getIva_deb_cred_per_prec();

			BigDecimal saldoDebitoUo = testataPrimaNota.getDett().stream()
					.filter(el -> el.getTipoDett().equals(Movimento_cogeBulk.TipoRiga.DEBITO.value()))
					.filter(el -> !Optional.ofNullable(el.getPartita()).isPresent())
					.map(el -> el.isSezioneDare() ? el.getImporto() : el.getImporto().negate())
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			saldoDebitoUo = saldoDebitoUo.add(ivaDebCredPrec);

			if (saldoDebitoUo.compareTo(liqIvaUo.getIva_da_versare()) != 0) {
				testataPrimaNota.getDett().stream().filter(el -> el.getPartita() != null).forEach(el -> System.out.println("Compenso: " + Optional.of(el.getPartita()).map(IDocumentoCogeBulk::getPg_doc).orElse(null) + " - Trib: " + el.getCdCori() + " - Conto: " + el.getCdConto() +
						" - Sezione: " + el.getSezione() + " - Importo: " + new it.cnr.contab.util.EuroFormat().format(el.getImporto())));

				throw new ApplicationRuntimeException("Errore nella generazione scrittura prima nota del tipo liquidazione IVA '" +
						Stampa_registri_ivaVBulk.TIPI_SEZIONALE_KEYS.get(liqIvaUo.getTipo_liquidazione()) +
						"' della UO " + liqIvaUo.getCd_unita_organizzativa() + " per il periodo " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(liqIvaUo.getDt_inizio()) +
						" - " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(liqIvaUo.getDt_fine()) + ". Il saldo del conto debito (" +
						new it.cnr.contab.util.EuroFormat().format(saldoDebitoUo) +
						") non risulterebbe essere uguale all'importo della liquidazione IVA (" +
						new it.cnr.contab.util.EuroFormat().format(liqIvaUo.getIva_da_versare()) + ").");
			}
			return this.generaScrittura(userContext, liqIvaUo, Collections.singletonList(testataPrimaNota), true);
		} catch (PersistencyException|RemoteException ex) {
			throw new ApplicationRuntimeException(ex);
		}
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaMandatoVersamentoCori(UserContext userContext, MandatoBulk mandato) throws ComponentException, PersistencyException, RemoteException {
		//verifico se trattasi di liquidazione IVA..... per essere tale deve avere un'unica riga ed un documento generico registrato sulla tabella LIQUIDAZIONE
		List<Liquidazione_ivaBulk> liquidazioneIvaList = new ArrayList<>();

		mandato.getMandato_rigaColl().forEach(mr->{
			try {
				liquidazioneIvaList.addAll(((Liquidazione_ivaHome) getHome(userContext, Liquidazione_ivaBulk.class)).findByMandatoRiga(mr));
			} catch(ComponentException|PersistencyException ex) {
				throw new ApplicationRuntimeException(ex);
			}
		});

		if (!liquidazioneIvaList.isEmpty())
			return proposeScritturaPartitaDoppiaMandatoLiquidazione(userContext, mandato, liquidazioneIvaList);

		Collection<Liquid_gruppo_coriBulk> liquidGruppoCoriBulk =
				((Liquid_gruppo_coriHome) getHome(userContext, Liquid_gruppo_coriBulk.class)).findByMandato(userContext, mandato)
						.stream()
						.flatMap(el->{
							if (!el.getFl_accentrato() && !Optional.ofNullable(el.getPg_gruppo_centro()).isPresent())
								return Stream.of(el);
							else {
								try {
									return ((Liquid_gruppo_coriHome) getHome(userContext, Liquid_gruppo_coriBulk.class)).findLiquidazioniAccentrate(userContext, el).stream();
								} catch(ComponentException|PersistencyException ex) {
									throw new ApplicationRuntimeException(ex);
								}
							}
						}).collect(Collectors.toList());

		Voce_epBulk voceEpBanca = this.findContoBanca(userContext, CNRUserContext.getEsercizio(userContext));

		Optional<Scrittura_partita_doppiaBulk> scritturaMandato = Optional.ofNullable(mandato.getScrittura_partita_doppia())
				.map(spd->Optional.of(spd).filter(el->el.getCrudStatus()!=OggettoBulk.UNDEFINED).orElseGet(()-> {
					try {
						Scrittura_partita_doppiaHome home = (Scrittura_partita_doppiaHome) getHome(userContext, Scrittura_partita_doppiaBulk.class);
						return home.getScrittura(userContext, mandato).orElse(null);
					} catch (ComponentException e) {
						throw new DetailedRuntimeException(e);
					}
				}));

		TestataPrimaNota testataPrimaNota = new TestataPrimaNota();

		liquidGruppoCoriBulk.forEach(liquid->{
			try {
				Collection<Liquid_gruppo_cori_detBulk> details = ((Liquid_gruppo_coriHome) getHome(userContext, Liquid_gruppo_coriBulk.class)).findDettagli(userContext, liquid);

				//a questo punto ho la lista dei compensi e dei codici contributi versati
				//Raggruppo per compenso e codice tributo (per evitare che mi escano 2 righe P e C)
				Map<Integer, Map<Long, Map<String, List<Liquid_gruppo_cori_detBulk>>>> detailsCompensi =
						details.stream().collect(Collectors.groupingBy(Liquid_gruppo_cori_detBase::getEsercizio_contributo_ritenuta,
								Collectors.groupingBy(Liquid_gruppo_cori_detKey::getPg_compenso,
										Collectors.groupingBy(Liquid_gruppo_cori_detKey::getCd_contributo_ritenuta))));

				TestataPrimaNota testataPrimaNotaLiquid = new TestataPrimaNota();

				//e per ognuno di essi faccio la scrittura
				detailsCompensi.keySet().forEach(aEsercizioCompenso -> {
					Map<Long, Map<String, List<Liquid_gruppo_cori_detBulk>>> mapPgCompensi = detailsCompensi.get(aEsercizioCompenso);
					mapPgCompensi.keySet().forEach(aPgCompenso -> {
						Map<String, List<Liquid_gruppo_cori_detBulk>> mapCdCori = mapPgCompensi.get(aPgCompenso);
						try {
							CompensoBulk compenso = (CompensoBulk) getHome( userContext, CompensoBulk.class).findByPrimaryKey(
									new CompensoBulk(liquid.getCd_cds_origine(), liquid.getCd_uo_origine(), aEsercizioCompenso, aPgCompenso));

							//verifico se è un compenso da stipendi recuperando il documento generico passivo leggendolo dalla tabelle stipendiCofiBulk
							Stipendi_cofiBulk stipendiCofiBulk = ((Stipendi_cofiHome)getHome(userContext, Stipendi_cofiBulk.class)).findStipendiCofi(compenso);

							//Individuo i mandati associati al compenso per eventuale generazione di scrittura
							List<MandatoBulk> pMandatiCompenso = new ArrayList<>();

							if (stipendiCofiBulk!=null) {
								MandatoBulk mandatoStipendiBulk = (MandatoBulk) getHome(userContext, MandatoIBulk.class)
										.findByPrimaryKey(new MandatoBulk(stipendiCofiBulk.getCd_cds_mandato(), stipendiCofiBulk.getEsercizio_mandato(), stipendiCofiBulk.getPg_mandato()));
								pMandatiCompenso.add(mandatoStipendiBulk);
							}

							List<MandatoBulk> pMandatiAssociati = ((CompensoHome)getHome( userContext, CompensoBulk.class)).findMandatiAssociati(userContext, compenso);

							pMandatiAssociati.stream().filter(el->pMandatiCompenso.stream().noneMatch(el2->el2.equalsByPrimaryKey(el)))
									.forEach(pMandatiCompenso::add);

							pMandatiCompenso.forEach(el-> {
								try {
									completeMandato(userContext, el);
								} catch (ComponentException|PersistencyException e) {
									throw new DetailedRuntimeException(e);
								}
							});

							mapCdCori.keySet()
								.forEach(aCdCori -> {
									try {
										//recupero tutti i movimenti della partita per ottenere il saldo al netto della scrittura del mandato se già esiste
										Map<String, Pair<String, BigDecimal>> saldiCori = this.getSaldiMovimentiCori(userContext, compenso, compenso.getCd_terzo(), aCdCori, scritturaMandato, pMandatiCompenso);

										//dovrei trovare tra i saldi proprio l'import liquidato
										//Il conto aperto deve essere solo uno e deve essere in segno AVERE
										if (saldiCori.values().stream().filter(el -> el.getSecond().compareTo(BigDecimal.ZERO) != 0).count() > 1)
											throw new ApplicationRuntimeException("Per il compenso " + compenso.getEsercizio() + "/" + compenso.getCd_cds() + "/" + compenso.getPg_compenso() +
													" e per il contributo " + aCdCori + " esiste più di un conto che presenta un saldo positivo.");

										saldiCori.keySet().forEach(cdVoceEp -> {
											Pair<String, BigDecimal> saldoVoce = saldiCori.get(cdVoceEp);
											if (saldoVoce.getSecond().compareTo(BigDecimal.ZERO) != 0)
												if (saldoVoce.getFirst().equals(Movimento_cogeBulk.SEZIONE_AVERE)) {
													testataPrimaNotaLiquid.closeDettaglioPatrimonialeCori(compenso, cdVoceEp, saldoVoce.getSecond(), compenso.getCd_terzo(), aCdCori);
													testataPrimaNotaLiquid.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), compenso.getTipoDocumentoEnum().getSezionePatrimoniale(), voceEpBanca.getCd_voce_ep(), saldoVoce.getSecond());
												} else {
													testataPrimaNotaLiquid.openDettaglioPatrimonialeCori(compenso, cdVoceEp, saldoVoce.getSecond(), compenso.getCd_terzo(), aCdCori);
													testataPrimaNotaLiquid.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), Movimento_cogeBulk.getControSezione(compenso.getTipoDocumentoEnum().getSezionePatrimoniale()), voceEpBanca.getCd_voce_ep(), saldoVoce.getSecond());
												}
										});
									} catch (ComponentException | PersistencyException ex) {
										throw new ApplicationRuntimeException(ex);
									}
								});
						} catch(ComponentException|PersistencyException ex) {
							throw new ApplicationRuntimeException(ex);
						}
					});
				});

				BigDecimal saldoTesoreria = testataPrimaNotaLiquid.getDett().stream()
						.filter(el->el.getTipoDett().equals(Movimento_cogeBulk.TipoRiga.TESORERIA.value()))
						.map(el -> el.isSezioneDare() ? el.getImporto() : el.getImporto().negate())
						.reduce(BigDecimal.ZERO, BigDecimal::add);

				if  (saldoTesoreria.negate().compareTo(liquid.getIm_liquidato()) != 0) {
//					testataPrimaNotaLiquid.getDett().stream().filter(el->el.getPartita()!=null).forEach(el-> System.out.println("Compenso: "+Optional.ofNullable(el.getPartita()).map(IDocumentoCogeBulk::getPg_doc).orElse(null)+" - Trib: "+el.getCdCori()+" - Conto: "+el.getCdConto()+
//							" - Sezione: "+el.getSezione() + " - Importo: " + new it.cnr.contab.util.EuroFormat().format(el.getImporto())));
					throw new ApplicationRuntimeException("Errore nella generazione scrittura prima nota del mandato " +
							mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() + ". Il saldo del conto tesoreria (" +
							new it.cnr.contab.util.EuroFormat().format(saldoTesoreria.negate()) +
							") non risulterebbe essere uguale all'importo della liquidazione della UO " + liquid.getCd_uo_origine() + " (" +
							new it.cnr.contab.util.EuroFormat().format(liquid.getIm_liquidato()) + ").");
				}

//				System.out.println("Liquidazione completata della UO: " + liquid.getCd_uo_origine());

				testataPrimaNota.getDett().addAll(testataPrimaNotaLiquid.getDett());
			} catch(ComponentException|PersistencyException ex) {
				throw new ApplicationRuntimeException(ex);
			}
		});

		if (mandato.getIm_ritenute().compareTo(BigDecimal.ZERO)>0) {
			List<Contributo_ritenutaBulk> righeCoriReversaliAssociate = findCoriReversaliAssociate(userContext, mandato);
			righeCoriReversaliAssociate.stream().filter(el->el.getAmmontare().compareTo(BigDecimal.ZERO)>0).forEach(cori->{
				try {
					BigDecimal imCori = cori.getAmmontare();
					CompensoBulk compenso = (CompensoBulk)getHome(userContext, CompensoBulk.class).findByPrimaryKey(cori.getCompenso());

					// Se la tipologia di contributo ritenuta è IVA o RIVALSA non registro la ritenuta in quanto già registrata in fase di chiusura debito mandato (imNettoMandato contiene imCori)
					if (!cori.isTipoContributoRivalsa() && !(cori.isTipoContributoIva() && cori.isContributoEnte())) {
						Pair<Voce_epBulk, Voce_epBulk> pairContoCori = this.findPairContiMandato(userContext, cori);
						Voce_epBulk contoVersamentoCori = pairContoCori.getSecond();
						//Riduco la tesoreria e rilevo il debito verso l'erario per le ritenute carico ente/percipiente
						testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), Movimento_cogeBulk.getControSezione(compenso.getTipoDocumentoEnum().getSezionePatrimoniale()), voceEpBanca.getCd_voce_ep(), imCori);
						testataPrimaNota.openDettaglioPatrimonialeCori(compenso, contoVersamentoCori.getCd_voce_ep(), imCori, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
					}
				} catch (ComponentException|PersistencyException e) {
					throw new ApplicationRuntimeException(e);
				}
			});
		}

		return this.generaScrittura(userContext, mandato, Collections.singletonList(testataPrimaNota), true);
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaMandatoAccantonamentoCoriDocgen(UserContext userContext, MandatoBulk mandato) throws ComponentException, PersistencyException, RemoteException {
		String descMandato = mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato();
		String descEstesaMandato = "Mandato " + descMandato + ", di tipo documento generico con ritenute di conguaglio,";

		boolean isAccantonamentoCoriDocgen = mandato.getMandato_rigaColl().size()==1 && mandato.getIm_ritenute().compareTo(BigDecimal.ZERO)>0 &&
				mandato.getMandato_rigaColl().stream().map(Mandato_rigaBulk::getCd_tipo_documento_amm)
						.anyMatch(el->TipoDocumentoEnum.fromValue(el).isGenericoSpesa());

		if (!isAccantonamentoCoriDocgen)
			throw new ApplicationException("La riga del mandato " + descMandato +
					" non risulta pagare un documento generico. Proposta di prima nota non possibile.");

		//Il mandato di questa tipologia deve essere sempre di importo a 0 dato che è stato emesso a fronte di un conguaglio che prevede quote ulteriori
		//da trattenere al terzo e non ci sono più compensi da pagare.
		if (mandato.getIm_mandato().subtract(mandato.getIm_ritenute()).compareTo(BigDecimal.ZERO)!=0)
			throw new ApplicationException(descEstesaMandato + " di importo non nullo. Proposta di prima nota non possibile.");

		List<MandatoRigaComplete> dettaglioFinanziarioList = this.completeRigheMandato(userContext, mandato);

		if (dettaglioFinanziarioList.size()>1)
			throw new ApplicationException(descEstesaMandato+" con più di una riga. Proposta di prima nota non possibile.");

		MandatoRigaComplete rigaMandato = dettaglioFinanziarioList.stream().findAny()
				.orElseThrow(()->new ApplicationException(descEstesaMandato + " senza righe. Proposta di prima nota non possibile."));

		Optional<Scrittura_partita_doppiaBulk> scritturaMandato = Optional.ofNullable(mandato.getScrittura_partita_doppia())
			.map(spd->Optional.of(spd).filter(el->el.getCrudStatus()!=OggettoBulk.UNDEFINED).orElseGet(()-> {
				try {
					Scrittura_partita_doppiaHome home = (Scrittura_partita_doppiaHome) getHome(userContext, Scrittura_partita_doppiaBulk.class);
					return home.getScrittura(userContext, mandato).orElse(null);
				} catch (ComponentException e) {
					throw new DetailedRuntimeException(e);
				}
			}));

		//recupero tutti i movimenti della partita per ottenere il saldo al netto della scrittura del mandato se già esiste
		Map<String, Pair<String, BigDecimal>> saldiPartita = this.getSaldiMovimentiPartita(userContext, rigaMandato.getDocamm(), rigaMandato.getCdTerzo(), scritturaMandato);

		if (saldiPartita.keySet().isEmpty())
			throw new ApplicationException("Errore nell'individuazione del conto patrimoniale da utilizzare per la scrittura di chiusura del debito " +
					"del documento associato " + rigaMandato.getDocamm().getCd_tipo_doc() + "/" + rigaMandato.getDocamm().getEsercizio() + "/" + rigaMandato.getDocamm().getCd_uo() + "/" + rigaMandato.getDocamm().getPg_doc() + ": non risulta" +
					" movimentato nessun conto patrimoniale.");
		else if (saldiPartita.keySet().size()>1)
			throw new ApplicationException("Errore nell'individuazione del conto patrimoniale da utilizzare per la scrittura di chiusura del debito " +
					"del documento associato " + rigaMandato.getDocamm().getCd_tipo_doc() + "/" + rigaMandato.getDocamm().getEsercizio() + "/" + rigaMandato.getDocamm().getCd_uo() + "/" + rigaMandato.getDocamm().getPg_doc() + ": risultano" +
					" movimentati troppi conti patrimoniali.");

		String cdVocePatrimoniale = saldiPartita.keySet().stream().findAny().orElse(null);
		Voce_epBulk voceEpBanca = this.findContoBanca(userContext, CNRUserContext.getEsercizio(userContext));

		//devo recuperare il compenso di conguaglio tramite le reversali associate al mandato
		CompensoBulk compensoConguaglio = null;

		List<Ass_mandato_reversaleBulk> result = ((Ass_mandato_reversaleHome) getHome(userContext, Ass_mandato_reversaleBulk.class)).findReversali(userContext, mandato, false);

		for (Ass_mandato_reversaleBulk assMandatoReversaleBulk : result) {
			Collection<V_doc_cont_compBulk> result2 = ((V_doc_cont_compHome) getHome(userContext, V_doc_cont_compBulk.class)).findByDocumento(assMandatoReversaleBulk.getEsercizio_reversale(), assMandatoReversaleBulk.getCd_cds_reversale(), assMandatoReversaleBulk.getPg_reversale(), V_doc_cont_compBulk.TIPO_DOC_CONT_REVERSALE);
			if (result2.isEmpty())
				throw new ApplicationRuntimeException("Errore nell'individuazione del compenso di conguaglio a cui è collegato il mandato " + rigaMandato.getMandatoRiga().getEsercizio() + "/" + rigaMandato.getMandatoRiga().getCd_cds() + "/" + rigaMandato.getMandatoRiga().getPg_mandato() +
						": la reversale " + assMandatoReversaleBulk.getEsercizio_reversale() + "/" + assMandatoReversaleBulk.getCd_cds_reversale() + "/" + assMandatoReversaleBulk.getPg_reversale() +
						", collegata al mandato, non risulta collegata a nessun compenso.");
			if (result2.size() > 1)
				throw new ApplicationRuntimeException("Errore nell'individuazione del compenso di conguaglio a cui è collegato il mandato " + rigaMandato.getMandatoRiga().getEsercizio() + "/" + rigaMandato.getMandatoRiga().getCd_cds() + "/" + rigaMandato.getMandatoRiga().getPg_mandato() +
						": la reversale " + assMandatoReversaleBulk.getEsercizio_reversale() + "/" + assMandatoReversaleBulk.getCd_cds_reversale() + "/" + assMandatoReversaleBulk.getPg_reversale() +
						", collegata al mandato, risulta collegata a troppi compensi.");

			V_doc_cont_compBulk docContCompBulk = result2.stream().findAny().get();
			if (compensoConguaglio == null)
				compensoConguaglio = new CompensoBulk(docContCompBulk.getCd_cds_compenso(), docContCompBulk.getCd_uo_compenso(), docContCompBulk.getEsercizio_compenso(), docContCompBulk.getPg_compenso());
			else if (compensoConguaglio.equalsByPrimaryKey(docContCompBulk.getCompenso()))
				throw new ApplicationRuntimeException("Errore nell'individuazione del compenso di conguaglio a cui è collegato il mandato " + descMandato +
						": le reversali, collegate al mandato del compenso principale, risultano collegate a compensi diversi.");
		}

		CompensoBulk compenso = Optional.ofNullable(compensoConguaglio).map(el->{
			try {
				return (CompensoBulk) getHome(userContext, CompensoBulk.class).findByPrimaryKey(el);
			} catch (ComponentException|PersistencyException e) {
				throw new ApplicationRuntimeException(e);
			}
		}).orElse(null);

		if (!compenso.getFl_compenso_conguaglio())
			throw new ApplicationException(descEstesaMandato+" legato a reversali emesse non a fronte di un conguaglio. Proposta di prima nota non possibile.");

		List<Contributo_ritenutaBulk> righeCori = Optional.ofNullable(compenso.getChildren()).orElseGet(()->{
			try {
				Contributo_ritenutaHome home = (Contributo_ritenutaHome) getHome(userContext, Contributo_ritenutaBulk.class);
				return (java.util.List<Contributo_ritenutaBulk>)home.loadContributiRitenute(compenso);
			} catch (ComponentException | PersistencyException e) {
				throw new DetailedRuntimeException(e);
			}
		});

		BigDecimal imRitenutePositive = righeCori.stream()
				.map(Contributo_ritenutaBulk::getAmmontare)
				.filter(el -> el.compareTo(BigDecimal.ZERO) > 0)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		//Aggiungo eventuali quote da trattenere indicate sul compenso e sul compenso conguaglio
		imRitenutePositive = imRitenutePositive.add(Optional.ofNullable(compenso.getIm_netto_da_trattenere()).orElse(BigDecimal.ZERO));

		if (imRitenutePositive.compareTo(mandato.getIm_ritenute())!=0)
			throw new ApplicationException(descEstesaMandato+" con ritenute ("+ new it.cnr.contab.util.EuroFormat().format(mandato.getIm_ritenute()) +
					") diverse da quelle indicate sul conguaglio (" + new it.cnr.contab.util.EuroFormat().format(imRitenutePositive) +
					"). Proposta di prima nota non possibile.");

		//recupero la scrittura del compenso
		List<Movimento_cogeBulk> allMovimentiPrimaNotaCompenso = this.findMovimentiPrimaNota(userContext,compenso);

		TestataPrimaNota testataPrimaNota = new TestataPrimaNota();
		BigDecimal imponibilePartita = rigaMandato.getMandatoRiga().getIm_mandato_riga();

		//Prima chiudo il documento generico per il suo importo totale
		testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.DEBITO.value(), Movimento_cogeBulk.SEZIONE_DARE, cdVocePatrimoniale, rigaMandato.getMandatoRiga().getIm_mandato_riga(), rigaMandato.getDocamm(), rigaMandato.getCdTerzo(), null);

		//Poi movimento il conto banca per l'importo netto del mandato
		BigDecimal imNettoMandato = mandato.getIm_mandato().subtract(mandato.getIm_ritenute());
		testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), Movimento_cogeBulk.SEZIONE_AVERE, voceEpBanca.getCd_voce_ep(), imNettoMandato);

		//Registrazione conto CONTRIBUTI-RITENUTE
		righeCori.stream().filter(el->el.getAmmontare().compareTo(BigDecimal.ZERO)>0).forEach(cori->{
			try {
				BigDecimal imCori = cori.getAmmontare();

				// Se la tipologia di contributo ritenuta è IVA o RIVALSA non registro la ritenuta in quanto già registrata in fase di chiusura debito mandato (imNettoMandato contiene imCori)
				if (!cori.isTipoContributoRivalsa() && !(cori.isTipoContributoIva() && cori.isContributoEnte())) {
					Pair<Voce_epBulk, Voce_epBulk> pairContoCori = this.findPairContiMandato(userContext, cori);
					Voce_epBulk contoVersamentoCori = pairContoCori.getSecond();
					//Per i contributi Percipiente non faccio nulla perchè le scritture si compensano tra loro
					if (cori.isContributoEnte()) {
						String contoToClose = this.findMovimentoAperturaCori(allMovimentiPrimaNotaCompenso, compenso, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta()).getCd_voce_ep();
						testataPrimaNota.closeDettaglioPatrimonialeCori(compenso, contoToClose, imCori, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
					}
					testataPrimaNota.openDettaglioPatrimonialeCori(compenso, contoVersamentoCori.getCd_voce_ep(), imCori, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
				}
			} catch (ComponentException|PersistencyException e) {
				throw new ApplicationRuntimeException(e);
			}
		});

		return this.generaScrittura(userContext, mandato, Collections.singletonList(testataPrimaNota), true);
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaReversale(UserContext userContext, ReversaleBulk reversale) throws ComponentException, ScritturaPartitaDoppiaNotRequiredException {
		TestataPrimaNota testataPrimaNota = proposeTestataPrimaNotaReversale(userContext, reversale, Boolean.TRUE);
		return Optional.ofNullable(testataPrimaNota).map(el->this.generaScrittura(userContext, reversale, Collections.singletonList(el), true)).orElse(null);
	}

	private TestataPrimaNota proposeTestataPrimaNotaReversale(UserContext userContext, ReversaleBulk reversale, boolean bloccoVincoli) throws ComponentException, ScritturaPartitaDoppiaNotRequiredException {
		try {
			if (bloccoVincoli &&
					((Ass_mandato_reversaleHome) getHome(userContext, Ass_mandato_reversaleBulk.class)).findMandati(userContext, reversale, false).stream().findFirst().isPresent())
				throw new ScritturaPartitaDoppiaNotRequiredException("Scrittura Economica non prevista in quanto la reversale risulta vincolata ad un mandato di pagamento.");

			if (reversale.isReversaleRegolarizzazione())
				throw new ScritturaPartitaDoppiaNotRequiredException("Reversale " + reversale.getEsercizio() + "/" + reversale.getCd_cds() + "/" + reversale.getPg_reversale() +
						" di regolarizzazione. Registrazione economica non prevista.");

			//Se le righe del mandato non sono valorizzate le riempio io
			if (!Optional.ofNullable(reversale.getReversale_rigaColl()).filter(el->!el.isEmpty()).isPresent()) {
				reversale.setReversale_rigaColl(new BulkList(((ReversaleHome) getHome(
						userContext, reversale.getClass())).findReversale_riga(userContext, reversale, false)));
				reversale.getReversale_rigaColl().forEach(el->{
					try {
						el.setReversale(reversale);
						((Reversale_rigaHome)getHome(userContext, Reversale_rigaBulk.class)).initializeElemento_voce(userContext, el);
					} catch (ComponentException|PersistencyException e) {
						throw new ApplicationRuntimeException(e);
					}
				});
			}

			if (!Optional.ofNullable(reversale.getReversale_terzo()).filter(el->el.getCrudStatus()!=OggettoBulk.UNDEFINED).isPresent())
				reversale.setReversale_terzo(((ReversaleHome) getHome(userContext, reversale.getClass())).findReversale_terzo(userContext, reversale, false));

			//Il documento deve essere annullato o esitato altrimenti esce
			if (reversale.isAnnullato())
				return this.proposeTestataPrimaNotaManRevAnnullato(userContext, reversale);
			else if (reversale.isIncassato()) {
				if (reversale.getReversale_rigaColl().stream().map(Reversale_rigaBulk::getCd_tipo_documento_amm)
						.anyMatch(el->TipoDocumentoEnum.fromValue(el).isChiusuraFondo()))
					return this.proposeTestataPrimaNotaReversaleChiusuraFondo(userContext, reversale);
				if (reversale.getReversale_rigaColl().stream().map(Reversale_rigaBulk::getCd_tipo_documento_amm)
						.anyMatch(el->TipoDocumentoEnum.fromValue(el).isGenericoRecuperoCrediti()))
					return this.proposeTestataPrimaNotaReversaleCompenso(userContext, reversale);
				if (reversale.getReversale_rigaColl().stream().map(Reversale_rigaBulk::getCd_tipo_documento_amm)
						.anyMatch(el->TipoDocumentoEnum.fromValue(el).isGenericoCoriAccantonamentoEntrata()))
					return this.proposeTestataPrimaNotaReversaleAccantonamentoCori(userContext, reversale);

				//La reversale vincolata non deve generare scritture patrimoniali in quanto rilevate in fase di pagamento mandato
				//Il documento deve essere annullato o esitato altrimenti esce
				TestataPrimaNota testataPrimaNota = new TestataPrimaNota();

				reversale.getReversale_rigaColl().forEach(rigaReversale -> {
					try {
						addDettagliPrimaNotaReversaleDocumentiVari(userContext, testataPrimaNota, rigaReversale);
					} catch (ComponentException | PersistencyException | RemoteException e) {
						throw new ApplicationRuntimeException(e);
					}
				});
				return testataPrimaNota;
			}
			return null;
		} catch (PersistencyException|RemoteException e) {
			throw handleException(e);
		}
	}

	private TestataPrimaNota proposeTestataPrimaNotaReversaleChiusuraFondo(UserContext userContext, ReversaleBulk reversale) throws ComponentException {
		if (reversale.getReversale_rigaColl().stream().map(Reversale_rigaBulk::getCd_tipo_documento_amm)
				.noneMatch(el->TipoDocumentoEnum.fromValue(el).isChiusuraFondo()))
			throw new ApplicationException("La reversale " + reversale.getEsercizio() + "/" + reversale.getCds() + "/" + reversale.getPg_reversale() +
					" non risulta essere a fronte di una chiusura fondo economale. Proposta di prima nota non possibile.");

		if (reversale.getReversale_rigaColl().isEmpty() || reversale.getReversale_rigaColl().size()>1)
			throw new ApplicationException("La reversale " + reversale.getEsercizio() + "/" + reversale.getCds() + "/" + reversale.getPg_reversale() +
					" ha un numero di righe non coerente con l'unica prevista per una reversale di chiusura fondo economale. Proposta di prima nota non possibile.");

		try {
			TestataPrimaNota testataPrimaNota = new TestataPrimaNota();

			Fondo_economaleBulk fondoEconomaleBulk = ((Fondo_economaleHome)getHome(userContext, Fondo_economaleBulk.class)).findFondoByReversale(reversale);

			if (!Optional.ofNullable(fondoEconomaleBulk).isPresent())
				throw new ApplicationException("La reversale " + reversale.getEsercizio() + "/" + reversale.getCds() + "/" + reversale.getPg_reversale() +
						" non risulta associata a nessun fondo economale. Proposta di prima nota non possibile.");

			//recupero le righe del mandato principale di apertura fondo economale per risalire al documento generico
			Collection<Mandato_rigaBulk> righeMandato = ((MandatoHome) getHome(userContext, MandatoIBulk.class)).findMandato_riga(userContext,
					new MandatoIBulk(fondoEconomaleBulk.getCd_cds(), fondoEconomaleBulk.getEsercizio(), fondoEconomaleBulk.getPg_mandato()));

			if (righeMandato.size() !=1)
				throw new ApplicationException("Il mandato " + fondoEconomaleBulk.getEsercizio() + "/" + fondoEconomaleBulk.getCd_cds() + "/" + fondoEconomaleBulk.getPg_mandato() +
						" risulta avere un numero di righe non coerente per un mandato di apertura fondo economale. Proposta di prima nota non possibile.");

			Mandato_rigaBulk rigaMandato = righeMandato.stream().findAny().orElse(null);

			Documento_genericoHome home = (Documento_genericoHome) getHome(userContext, Documento_genericoBulk.class);
			Documento_genericoBulk docamm = (Documento_genericoBulk)home.findByPrimaryKey(new Documento_genericoBulk(rigaMandato.getCd_cds_doc_amm(), rigaMandato.getCd_tipo_documento_amm(),
					rigaMandato.getCd_uo_doc_amm(), rigaMandato.getEsercizio_doc_amm(), rigaMandato.getPg_doc_amm()));

			List<IDocumentoAmministrativoRigaBulk> righeDocamm = this.getRigheDocamm(userContext, docamm);
			if (righeDocamm.size() !=1)
				throw new ApplicationException("Il documento generico " + docamm.getEsercizio() + "/" + docamm.getCd_cds() + "/" + docamm.getPg_documento_generico() +
						" risulta avere un numero di righe non coerente per un documento generico di apertura fondo economale. Proposta di prima nota non possibile.");

			Integer cdTerzo = righeDocamm.get(0).getCd_terzo();

			//2. chiudere il conto credito per anticipo
			List<Movimento_cogeBulk> allMovimentiPrimaNota = this.findMovimentiPrimaNota(userContext,docamm);
			String contoPatrimonialeAperturaCredito = this.findMovimentoAperturaCreditoEconomo(allMovimentiPrimaNota, docamm, cdTerzo).getCd_voce_ep();
			Voce_epBulk voceEpBanca = this.findContoBanca(userContext, CNRUserContext.getEsercizio(userContext));

			testataPrimaNota.closeDettaglioCostoRicavoPartita(docamm, contoPatrimonialeAperturaCredito, reversale.getIm_reversale(), cdTerzo);
			testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), Movimento_cogeBulk.SEZIONE_DARE, voceEpBanca.getCd_voce_ep(), reversale.getIm_reversale());

			return testataPrimaNota;
		} catch (PersistencyException|RemoteException e) {
			throw handleException(e);
		}
	}

	private TestataPrimaNota proposeTestataPrimaNotaReversaleCompenso(UserContext userContext, ReversaleBulk reversale) throws ComponentException, PersistencyException, RemoteException, ScritturaPartitaDoppiaNotRequiredException {
		String descReversale = reversale.getEsercizio() + "/" + reversale.getCd_cds() + "/" + reversale.getPg_reversale();
		String descEstesaReversale = "reversale " + descReversale + " di tipo recupero crediti da conguaglio";

		if (reversale.getReversale_rigaColl().stream().map(Reversale_rigaBulk::getCd_tipo_documento_amm)
				.noneMatch(el->TipoDocumentoEnum.fromValue(el).isGenericoRecuperoCrediti()))
			throw new ApplicationException("La reversale "+descReversale+" non risulta pagare un documento generico di recupero crediti." +
					" Proposta di prima nota non possibile.");
		if (reversale.getReversale_rigaColl().isEmpty() || reversale.getReversale_rigaColl().size() > 1)
			throw new ApplicationException("La reversale "+descReversale+
					" ha un numero di righe non coerente con l'unica prevista per una reversale di tipo recupero crediti da conguaglio." +
					" Proposta di prima nota non possibile.");

		Reversale_rigaBulk rigaReversale = reversale.getReversale_rigaColl().get(0);

		//Una reversale di tipo recupero crediti da conguaglio non deve essere vincolata finanziariamente ad altri oggetti. Gli unici vincoli devono essere quelli da compenso di conguaglio
		if (((Ass_mandato_reversaleHome) getHome(userContext, Ass_mandato_reversaleBulk.class)).findMandati(userContext, reversale, false).stream().findFirst().isPresent())
			throw new ApplicationException("Scrittura Economica non prevista in quanto la " +descEstesaReversale+" risulta vincolata ad un mandato di pagamento.");

		//Devo recuperare il compenso legato alla reversale
		Collection<V_doc_cont_compBulk> listVDocContCompBulkByReversale = ((V_doc_cont_compHome)getHome(userContext, V_doc_cont_compBulk.class)).findByDocumento(reversale.getEsercizio(), reversale.getCd_cds(), reversale.getPg_reversale(), V_doc_cont_compBulk.TIPO_DOC_CONT_REVERSALE);

		if (listVDocContCompBulkByReversale.size() != 1)
			throw new ApplicationException("Errore nell'individuazione del compenso associato alla "+descEstesaReversale+ ": troppi compensi associati (V_DOC_CONT_COMP).");

		V_doc_cont_compBulk vDocContCompBulk = listVDocContCompBulkByReversale.stream().findAny().get();
		CompensoBulk compenso = (CompensoBulk) getHome(userContext, CompensoBulk.class)
				.findByPrimaryKey(new CompensoBulk(vDocContCompBulk.getCd_cds_compenso(), vDocContCompBulk.getCd_uo_compenso(), vDocContCompBulk.getEsercizio_compenso(),
						vDocContCompBulk.getPg_compenso()));

		String descCompenso = compenso.getEsercizio() + "/" + compenso.getCd_cds() + "/" + compenso.getPg_compenso();

		if (!compenso.getFl_compenso_conguaglio())
			throw new ApplicationException("Errore nell'individuazione del compenso associato alla "+descEstesaReversale+ ": il compenso "+descCompenso+" associato risulta non essere " +
					"di tipo conguaglio.");

		//Devo recuperare il compenso legato alla reversale
		Collection<V_doc_cont_compBulk> listVDocContCompBulkByCompenso = ((V_doc_cont_compHome)getHome(userContext, V_doc_cont_compBulk.class)).loadAllDocCont(compenso);

		List<V_doc_cont_compBulk> listDocumentiPrincipali = listVDocContCompBulkByCompenso.stream().filter(V_doc_cont_compBulk::isDocumentoPrincipale).collect(Collectors.toList());
		List<MandatoBulk> listMandati = listVDocContCompBulkByCompenso.stream().filter(V_doc_cont_compBulk::isTipoDocMandato).map(V_doc_cont_compBulk::getManRev)
				.map(MandatoBulk.class::cast).collect(Collectors.toList());
		List<ReversaleBulk> listReversali = listVDocContCompBulkByCompenso.stream().filter(V_doc_cont_compBulk::isTipoDocReversale).map(V_doc_cont_compBulk::getManRev)
				.map(ReversaleBulk.class::cast).collect(Collectors.toList());

		if (listDocumentiPrincipali.size() > 1)
			throw new ApplicationException("Errore nell'individuazione del compenso associato alla "+descEstesaReversale+ ": la reversale risulta collegata al compenso "+descCompenso+
					" che risulta avere troppe documenti collegati definiti come principali (V_DOC_CONT_COMP).");

		if (!vDocContCompBulk.isDocumentoPrincipale()) {
			V_doc_cont_compBulk vDocContCompBulkPrincipale = listDocumentiPrincipali.stream().findAny().get();
			throw new ScritturaPartitaDoppiaNotRequiredException("Scrittura Economica non necessaria per la "+descEstesaReversale+": la scrittura viene effettuata sulla reversale principale "
					+vDocContCompBulkPrincipale.getEsercizio_doc_cont()+"/"+vDocContCompBulkPrincipale.getCd_cds_doc_cont()+"/"+vDocContCompBulkPrincipale.getPg_doc_cont()
					+" del compenso "+descCompenso+".");
		}

		//A questo punto so che il documento in essere è il principale.....e quindi procedo a gestire l'intero compenso di conguaglio su di esso
		List<Contributo_ritenutaBulk> righeCori = Optional.ofNullable(compenso.getChildren()).orElseGet(() -> {
			try {
				Contributo_ritenutaHome home = (Contributo_ritenutaHome) getHome(userContext, Contributo_ritenutaBulk.class);
				return (java.util.List<Contributo_ritenutaBulk>) home.loadContributiRitenute(compenso);
			} catch (ComponentException | PersistencyException e) {
				throw new DetailedRuntimeException(e);
			}
		});

		// Effettuo il controllo che le ritenute associate al compenso originario coincida con le ritenute della riga associata al mandato
		// Se la tipologia di contributo ritenuta è IVA o RIVALSA non calcolo le ritenute perchè vanno direttamente al fornitore
		{
			BigDecimal imRitenuteFittizieSoloCompenso = righeCori.stream()
					.filter(el -> el.getCompenso().equalsByPrimaryKey(compenso))
					.filter(Contributo_ritenutaBulk::isContributoEnte)
					.filter(el -> el.isTipoContributoRivalsa() || el.isTipoContributoIva())
					.map(Contributo_ritenutaBulk::getAmmontare)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal imRitenutePositiveSoloCompenso = righeCori.stream()
					.filter(el -> el.getCompenso().equalsByPrimaryKey(compenso))
					.map(Contributo_ritenutaBulk::getAmmontare)
					.filter(el -> el.compareTo(BigDecimal.ZERO) > 0)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			//Aggiungo eventuali quote da trattenere indicate sul compenso
			imRitenutePositiveSoloCompenso = imRitenutePositiveSoloCompenso.add(Optional.ofNullable(compenso.getIm_netto_da_trattenere()).orElse(BigDecimal.ZERO));

			BigDecimal totReversali = listReversali.stream().map(ReversaleBulk::getIm_reversale).reduce(BigDecimal.ZERO, BigDecimal::add);
			if (imRitenutePositiveSoloCompenso.subtract(imRitenuteFittizieSoloCompenso).compareTo(totReversali) != 0)
				throw new ApplicationException("L'importo delle righe ritenute del compenso "+descCompenso+" non corrisponde con la somma delle reversali associate.");
		}

		BigDecimal imRitenuteFittizie = righeCori.stream()
				.filter(Contributo_ritenutaBulk::isContributoEnte)
				.filter(el -> el.isTipoContributoRivalsa() || el.isTipoContributoIva())
				.map(Contributo_ritenutaBulk::getAmmontare)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal imRitenutePositive = righeCori.stream()
				.map(Contributo_ritenutaBulk::getAmmontare)
				.filter(el -> el.compareTo(BigDecimal.ZERO) > 0)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		//Aggiungo eventuali quote da trattenere indicate sul compenso e sul compenso conguaglio
		imRitenutePositive = imRitenutePositive.add(Optional.ofNullable(compenso.getIm_netto_da_trattenere()).orElse(BigDecimal.ZERO));

		//sottraggo le ritenute fittizie
		imRitenutePositive = imRitenutePositive.subtract(imRitenuteFittizie);

		BigDecimal imRitenuteNegative = righeCori.stream().map(Contributo_ritenutaBulk::getAmmontare)
				.filter(el->el.compareTo(BigDecimal.ZERO)<0)
				.reduce(BigDecimal.ZERO, BigDecimal::add).abs();

		BigDecimal imContributiCaricoEnte = righeCori.stream().filter(Contributo_ritenutaBulk::isContributoEnte)
				.map(Contributo_ritenutaBulk::getAmmontare)
				.filter(el->el.compareTo(BigDecimal.ZERO)>0)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		//Per il calcolo del netto mandato effettuo la sottrazione tra:
		//1. il lordo mandato calcolato aggiungendo anche le righe ritenute negative che hanno sicuramente generato un mandato vincolato
		//2. le ritenute positive
		BigDecimal imTesoreria = imRitenuteNegative.subtract(imRitenutePositive);

		List<Movimento_cogeBulk> allMovimentiPrimaNotaCompenso = this.findMovimentiPrimaNota(userContext,compenso);

		//Chiudo il patrimoniale principale del compenso.... quello con la partita
		Voce_epBulk voceEpBanca = this.findContoBanca(userContext, CNRUserContext.getEsercizio(userContext));

		TestataPrimaNota testataPrimaNota = new TestataPrimaNota();

		testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), compenso.getTipoDocumentoEnum().getSezionePatrimoniale(), voceEpBanca.getCd_voce_ep(), imTesoreria);

		//Registrazione conto CONTRIBUTI-RITENUTE
		righeCori.stream().filter(el->el.getAmmontare().compareTo(BigDecimal.ZERO)!=0).forEach(cori->{
			try {
				BigDecimal imCori = cori.getAmmontare();

				// Se la tipologia di contributo ritenuta è IVA o RIVALSA non registro la ritenuta in quanto già registrata in fase di chiusura debito mandato (imNettoMandato contiene imCori)
				if (!cori.isTipoContributoRivalsa() && !(cori.isTipoContributoIva() && cori.isContributoEnte())) {
					Pair<Voce_epBulk, Voce_epBulk> pairContoCori = this.findPairContiMandato(userContext, cori);
					Voce_epBulk contoVersamentoCori = pairContoCori.getSecond();
					//Per i contributi Percipiente non faccio nulla perchè le scritture si compensano tra loro
					if (cori.isContributoEnte()) {
						String contoToClose = this.findMovimentoAperturaCori(allMovimentiPrimaNotaCompenso, compenso, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta()).getCd_voce_ep();
						testataPrimaNota.closeDettaglioPatrimonialeCori(compenso, contoToClose, imCori, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
					}
					testataPrimaNota.openDettaglioPatrimonialeCori(compenso, contoVersamentoCori.getCd_voce_ep(), imCori, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
				}
			} catch (ComponentException|PersistencyException e) {
				throw new ApplicationRuntimeException(e);
			}
		});

		return testataPrimaNota;
	}

	private void addDettagliPrimaNotaMandatoDocumentiVari(UserContext userContext, TestataPrimaNota testataPrimaNota, MandatoBulk mandato, IDocumentoAmministrativoBulk docamm, Integer cdTerzoDocAmm, List<MandatoRigaComplete> mandatoRigaCompleteList) throws ComponentException, PersistencyException, RemoteException {
		final BigDecimal imLordoRigheMandato = mandatoRigaCompleteList.stream().map(MandatoRigaComplete::getMandatoRiga).map(Mandato_rigaBulk::getIm_mandato_riga).reduce(BigDecimal.ZERO, BigDecimal::add);
		final BigDecimal imRitenuteRigheMandato = mandatoRigaCompleteList.stream().map(MandatoRigaComplete::getMandatoRiga).map(Mandato_rigaBulk::getIm_ritenute_riga).reduce(BigDecimal.ZERO, BigDecimal::add);
		final BigDecimal imNettoRigheMandato = imLordoRigheMandato.subtract(imRitenuteRigheMandato);

		if (imLordoRigheMandato.compareTo(BigDecimal.ZERO)==0 && imRitenuteRigheMandato.compareTo(BigDecimal.ZERO)==0)
			return;

		Voce_epBulk voceEpBanca = this.findContoBanca(userContext, CNRUserContext.getEsercizio(userContext));

		final boolean isExtraUe = Optional.of(docamm).filter(Fattura_passivaBulk.class::isInstance).map(Fattura_passivaBulk.class::cast).map(Fattura_passivaBulk::getFl_extra_ue).orElse(Boolean.FALSE);

		if (isExtraUe) {
			List<IDocumentoAmministrativoRigaBulk> righeDocamm = this.getRigheDocamm(userContext, docamm);
			//Tutte le righe del documento devono essere collegate al mandato
			if (righeDocamm.size() != mandatoRigaCompleteList.stream().flatMap(el->el.getDocammRighe().stream()).count())
				throw new ApplicationException("Il documento extra ue " + docamm.getCd_tipo_doc() + "/" + docamm.getEsercizio() + "/" + docamm.getCd_uo() + "/" + docamm.getPg_doc() +
						" non risulta totalmente collegato al mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +
						". Scrittura prima nota non possibile per impossibilità ad effettuare la scrittura di variazione del tasso di cambio.");
		}

		final boolean isIstituzionale = Optional.of(docamm).filter(Fattura_passivaBulk.class::isInstance).map(Fattura_passivaBulk.class::cast).map(Fattura_passivaBulk::isIstituzionale).orElse(Boolean.FALSE);
		final String cdCoriIvaSplit = this.findCodiceTributoIvaSplit(userContext);

		Optional<Scrittura_partita_doppiaBulk> scritturaMandato = Optional.ofNullable(mandato.getScrittura_partita_doppia())
				.map(spd->Optional.of(spd).filter(el->el.getCrudStatus()!=OggettoBulk.UNDEFINED).orElseGet(()-> {
					try {
						Scrittura_partita_doppiaHome home = (Scrittura_partita_doppiaHome) getHome(userContext, Scrittura_partita_doppiaBulk.class);
						return home.getScrittura(userContext, mandato).orElse(null);
					} catch (ComponentException e) {
						throw new DetailedRuntimeException(e);
					}
				}));

		//recupero tutti i movimenti della partita per ottenere il saldo al netto della scrittura del mandato se già esiste
		Map<String, Pair<String, BigDecimal>> saldiPartita = this.getSaldiMovimentiPartita(userContext, docamm, cdTerzoDocAmm, scritturaMandato);

		Map<String, Pair<String, Pair<BigDecimal,BigDecimal>>> contiPatrimonialiDaChiudere = new HashMap<>();

		if (saldiPartita.keySet().isEmpty())
			throw new ApplicationException("Errore nell'individuazione del conto patrimoniale da utilizzare per la scrittura di chiusura del debito " +
					"del documento associato " + docamm.getCd_tipo_doc() + "/" + docamm.getEsercizio() + "/" + docamm.getCd_uo() + "/" + docamm.getPg_doc() + ": non risulta" +
					" movimentato nessun conto patrimoniale.");
		else if (saldiPartita.keySet().size() == 1) {
			String cdVocePatrimoniale = saldiPartita.keySet().stream().findAny().orElse(null);
			BigDecimal imponibile = imLordoRigheMandato.subtract(imRitenuteRigheMandato);
			contiPatrimonialiDaChiudere.put(cdVocePatrimoniale, Pair.of(Movimento_cogeBulk.SEZIONE_DARE, Pair.of(imponibile, imRitenuteRigheMandato)));
		} else {
			List<IDocumentoAmministrativoRigaBulk> righeDocamm = this.getRigheDocamm(userContext, docamm);

			List<DettaglioFinanziario> list = mandatoRigaCompleteList.stream()
				.map(el -> {
					try {
						ObbligazioneHome obbligazionehome = (ObbligazioneHome) getHome(userContext, ObbligazioneBulk.class);
						ObbligazioneBulk obbligazioneDB = (ObbligazioneBulk) obbligazionehome.findByPrimaryKey(
								new ObbligazioneBulk(el.getMandatoRiga().getCd_cds(), el.getMandatoRiga().getEsercizio_obbligazione(),
										el.getMandatoRiga().getEsercizio_ori_obbligazione(), el.getMandatoRiga().getPg_obbligazione()));
						BigDecimal imponibile = el.getMandatoRiga().getIm_mandato_riga().subtract(el.getMandatoRiga().getIm_ritenute_riga());
						BigDecimal imposta = el.getMandatoRiga().getIm_ritenute_riga();

//						BigDecimal imponibile = el.getDocammRighe().stream().filter(el2->Optional.ofNullable(el2.getIm_imponibile()).isPresent()).map(IDocumentoAmministrativoRigaBulk::getIm_imponibile).reduce(BigDecimal.ZERO, BigDecimal::add);
//						BigDecimal imposta = el.getDocammRighe().stream().filter(el2->Optional.ofNullable(el2.getIm_iva()).isPresent()).map(IDocumentoAmministrativoRigaBulk::getIm_iva).reduce(BigDecimal.ZERO, BigDecimal::add);
						return new DettaglioFinanziario(docamm, cdTerzoDocAmm, obbligazioneDB.getElemento_voce(), null, null,
								imponibile, imposta);
					} catch (ComponentException | PersistencyException e) {
						throw new ApplicationRuntimeException(e);
					}
				})
				.collect(Collectors.toList());

			list.forEach(rigaDettFinVoce->{
				try {
					Pair<Voce_epBulk, Voce_epBulk> pairContoCosto = this.findPairCosto(userContext, rigaDettFinVoce);
					BigDecimal imponibile = rigaDettFinVoce.getImImponibile();
					BigDecimal imposta = rigaDettFinVoce.getImImposta();
					if (Optional.ofNullable(contiPatrimonialiDaChiudere.get(pairContoCosto.getSecond().getCd_voce_ep())).isPresent()) {
						imponibile = imponibile.add(contiPatrimonialiDaChiudere.get(pairContoCosto.getSecond().getCd_voce_ep()).getSecond().getFirst());
						imposta = imposta.add(contiPatrimonialiDaChiudere.get(pairContoCosto.getSecond().getCd_voce_ep()).getSecond().getSecond());
					}
					contiPatrimonialiDaChiudere.put(pairContoCosto.getSecond().getCd_voce_ep(), Pair.of(Movimento_cogeBulk.SEZIONE_DARE, Pair.of(imponibile,imposta)));
				} catch (ComponentException | PersistencyException | RemoteException e) {
					throw new ApplicationRuntimeException(e);
				}
			});
		}

		//Trovo il saldo partita
		contiPatrimonialiDaChiudere.keySet().forEach(cdVocePatrimoniale->{
			BigDecimal saldoContoPartita = saldiPartita.get(cdVocePatrimoniale).getSecond();

			if (saldiPartita.get(cdVocePatrimoniale).getFirst().equals(Movimento_cogeBulk.SEZIONE_DARE))
				throw new ApplicationRuntimeException("Il conto debito verso il fornitore "+cdVocePatrimoniale+" del documento "+
						docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getCd_uo()+"/"+docamm.getPg_doc()+
						" associato al mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +
						" risulta essere nullo. Non è possibile effettuare la scrittura prima nota.");
		});

		BigDecimal saldoPartita = saldiPartita.values().stream().map(Pair::getSecond).reduce(BigDecimal.ZERO, BigDecimal::add);

		//recupero tutti i movimenti cori split della partita per ottenere il saldo
		Map<String, Pair<String, BigDecimal>> saldiCoriSplit = this.getSaldiMovimentiCori(userContext, docamm, cdTerzoDocAmm, cdCoriIvaSplit, scritturaMandato);
		Voce_epBulk aContoCreditoRitenuteSplit = this.findContoCreditoRitenuteSplitPayment(userContext, docamm.getEsercizio());

		//Ricerco il conto patrimoniale della ritenuta split della partita recuperato da CONFIGURAZIONE_CNR
		Optional<String> optCdVoceCoriSplit = saldiCoriSplit.keySet().stream().filter(el->el.equals(aContoCreditoRitenuteSplit.getCd_voce_ep())).findAny();

		if (optCdVoceCoriSplit.isPresent()) {
			String sezioneCoriSplit = saldiCoriSplit.get(optCdVoceCoriSplit.get()).getFirst();
			BigDecimal saldoCoriSplit = saldiCoriSplit.get(optCdVoceCoriSplit.get()).getSecond();

			//Effettuo il controllo di non sfondamento
			if (sezioneCoriSplit.equals(Movimento_cogeBulk.SEZIONE_AVERE) && saldoCoriSplit.compareTo(BigDecimal.ZERO)>=0)
				throw new ApplicationException("Il credito split payment verso il fornitore del documento "+
						docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getCd_uo()+"/"+docamm.getPg_doc()+
						" associato al mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +
						" risulta essere nullo. Non è possibile effettuare la scrittura prima nota.");

			if (sezioneCoriSplit.equals(Movimento_cogeBulk.SEZIONE_DARE) && saldoCoriSplit.compareTo(imRitenuteRigheMandato)<0)
				throw new ApplicationException("Il credito split payment verso il fornitore del documento "+
						docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getCd_uo()+"/"+docamm.getPg_doc()+
						" associato al mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +
						" risulta essere inferiore ("+ new it.cnr.contab.util.EuroFormat().format(saldoCoriSplit) +
						" ) rispetto alle ritenute presenti sul mandato ("+ new it.cnr.contab.util.EuroFormat().format(imRitenuteRigheMandato)+").");

			//Trovo il saldo cori
			saldoPartita = saldoPartita.subtract(saldoCoriSplit);
		}

		//Effettuo il controllo di non sfondamento (non per extra-ue che può essere diverso per il cambio)
		if (!isExtraUe && imNettoRigheMandato.compareTo(saldoPartita)>0)
			throw new ApplicationException("L'importo netto ("+ new it.cnr.contab.util.EuroFormat().format(imNettoRigheMandato) +
					") delle righe del mandato "+mandato.getEsercizio()+"/"+mandato.getCd_cds()+"/"+mandato.getPg_mandato()+
					" è maggiore dal saldo totale fornitore ("+ new it.cnr.contab.util.EuroFormat().format(saldoPartita) + ") del documento associato "+
					docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getCd_uo()+"/"+docamm.getPg_doc()+".");

		contiPatrimonialiDaChiudere.keySet().forEach(cdVocePatrimoniale->{
			BigDecimal imponibileConto = contiPatrimonialiDaChiudere.get(cdVocePatrimoniale).getSecond().getFirst();
			BigDecimal impostaConto = contiPatrimonialiDaChiudere.get(cdVocePatrimoniale).getSecond().getSecond();
			BigDecimal importoLordoConto = imponibileConto.add(impostaConto);
			BigDecimal importoNettoConto = importoLordoConto.subtract(impostaConto);
			testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.DEBITO.value(), Movimento_cogeBulk.SEZIONE_DARE, cdVocePatrimoniale, importoNettoConto, docamm, cdTerzoDocAmm, null);
			testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), Movimento_cogeBulk.SEZIONE_AVERE, voceEpBanca.getCd_voce_ep(), importoNettoConto);
		});

		optCdVoceCoriSplit.ifPresent(CdVoceCoriSplit-> contiPatrimonialiDaChiudere.keySet().forEach(cdVocePatrimoniale->{
			BigDecimal impostaConto = contiPatrimonialiDaChiudere.get(cdVocePatrimoniale).getSecond().getSecond();
			testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.DEBITO.value(), Movimento_cogeBulk.SEZIONE_DARE, cdVocePatrimoniale, impostaConto, docamm, cdTerzoDocAmm, null);
			testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.CREDITO.value(), Movimento_cogeBulk.SEZIONE_AVERE, optCdVoceCoriSplit.get(), impostaConto, docamm, cdTerzoDocAmm, cdCoriIvaSplit);
		}));

		//Registro la differenza di cambio se extraue
		if (isExtraUe && imNettoRigheMandato.compareTo(saldoPartita)!=0) {
			List<Movimento_cogeBulk> allMovimentiPrimaNota = this.findMovimentiPrimaNota(userContext,docamm);
			Map<String, List<Movimento_cogeBulk>> mapVoceCosto = allMovimentiPrimaNota.stream()
					.filter(el->Movimento_cogeBulk.TipoRiga.COSTO.value().equals(el.getTi_riga()))
					.filter(el->Movimento_cogeBulk.SEZIONE_DARE.equals(el.getSezione()))
					.filter(el->!Optional.ofNullable(el.getPg_numero_documento()).isPresent())
					.collect(Collectors.groupingBy(Movimento_cogeBulk::getCd_voce_ep));

			if (mapVoceCosto.keySet().size()>1)
				throw new ApplicationException("Errore nell'individuazione del conto di costo da utilizzare per la scrittura di variazione tasso di cambio " +
						"del documento extra UE associato "+	docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getCd_uo()+"/"+docamm.getPg_doc()+": risultano" +
						" movimentati più di un conto di costo.");
			String cdVoceCosto = mapVoceCosto.keySet().stream().findAny()
					.orElseThrow(()->new ApplicationException("Errore nell'individuazione del conto di costo da utilizzare per la scrittura di variazione tasso di cambio " +
							"del documento associato "+	docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getCd_uo()+"/"+docamm.getPg_doc()+": non risulta" +
							" movimentato nessun conto di costo."));

			if (imNettoRigheMandato.compareTo(saldoPartita)>0) {
				contiPatrimonialiDaChiudere.keySet().forEach(cdVocePatrimoniale->{
					BigDecimal importoLordoMandato = contiPatrimonialiDaChiudere.get(cdVocePatrimoniale).getSecond().getFirst();
					BigDecimal saldoContoPartita = saldiPartita.get(cdVocePatrimoniale).getSecond();
					testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.DEBITO.value(), Movimento_cogeBulk.SEZIONE_AVERE, cdVocePatrimoniale, importoLordoMandato.subtract(saldoContoPartita), docamm, cdTerzoDocAmm, null);
				});
				testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.COSTO.value(), Movimento_cogeBulk.SEZIONE_DARE, cdVoceCosto, imNettoRigheMandato.subtract(saldoPartita), null, cdTerzoDocAmm, null);
			} else {
				contiPatrimonialiDaChiudere.keySet().forEach(cdVocePatrimoniale->{
					BigDecimal importoLordoMandato = contiPatrimonialiDaChiudere.get(cdVocePatrimoniale).getSecond().getFirst();
					BigDecimal saldoContoPartita = saldiPartita.get(cdVocePatrimoniale).getSecond();
					testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.DEBITO.value(), Movimento_cogeBulk.SEZIONE_DARE, cdVocePatrimoniale, saldoContoPartita.subtract(importoLordoMandato), docamm, cdTerzoDocAmm, null);
				});
				testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.COSTO.value(), Movimento_cogeBulk.SEZIONE_AVERE, cdVoceCosto, saldoPartita.subtract(imNettoRigheMandato), null, cdTerzoDocAmm, null);
			}
		}
	}

	private void addDettagliPrimaNotaReversaleDocumentiVari(UserContext userContext, TestataPrimaNota testataPrimaNota, Reversale_rigaBulk rigaReversale) throws ComponentException, PersistencyException, RemoteException {
		BigDecimal imReversale = rigaReversale.getIm_reversale_riga();

		Voce_epBulk voceEpBanca = this.findContoBanca(userContext, CNRUserContext.getEsercizio(userContext));

		//La partita non deve essere registrata in caso di versamento ritenute
		IDocumentoCogeBulk partita;
		if (TipoDocumentoEnum.fromValue(rigaReversale.getCd_tipo_documento_amm()).isDocumentoGenericoAttivo())
			partita = (Documento_generico_attivoBulk) getHome(userContext, Documento_generico_attivoBulk.class).findByPrimaryKey(new Documento_generico_attivoBulk(rigaReversale.getCd_cds_doc_amm(), rigaReversale.getCd_tipo_documento_amm(), rigaReversale.getCd_uo_doc_amm(), rigaReversale.getEsercizio_doc_amm(), rigaReversale.getPg_doc_amm()));
		else if (TipoDocumentoEnum.fromValue(rigaReversale.getCd_tipo_documento_amm()).isFatturaAttiva())
			partita = (Fattura_attiva_IBulk) getHome(userContext, Fattura_attiva_IBulk.class).findByPrimaryKey(new Fattura_attiva_IBulk(rigaReversale.getCd_cds_doc_amm(), rigaReversale.getCd_uo_doc_amm(), rigaReversale.getEsercizio_doc_amm(), rigaReversale.getPg_doc_amm()));
		else if (TipoDocumentoEnum.fromValue(rigaReversale.getCd_tipo_documento_amm()).isNotaCreditoAttiva())
			partita = (Nota_di_credito_attivaBulk) getHome(userContext, Nota_di_credito_attivaBulk.class).findByPrimaryKey(new Nota_di_credito_attivaBulk(rigaReversale.getCd_cds_doc_amm(), rigaReversale.getCd_uo_doc_amm(), rigaReversale.getEsercizio_doc_amm(), rigaReversale.getPg_doc_amm()));
		else if (TipoDocumentoEnum.fromValue(rigaReversale.getCd_tipo_documento_amm()).isNotaDebitoAttiva())
			partita = (Nota_di_debito_attivaBulk) getHome(userContext, Nota_di_debito_attivaBulk.class).findByPrimaryKey(new Nota_di_debito_attivaBulk(rigaReversale.getCd_cds_doc_amm(), rigaReversale.getCd_uo_doc_amm(), rigaReversale.getEsercizio_doc_amm(), rigaReversale.getPg_doc_amm()));
		else
			partita = new Partita(rigaReversale.getCd_tipo_documento_amm(), rigaReversale.getCd_cds_doc_amm(), rigaReversale.getCd_uo_doc_amm(), rigaReversale.getEsercizio_doc_amm(), rigaReversale.getPg_doc_amm(),
				rigaReversale.getCd_terzo(), TipoDocumentoEnum.fromValue(rigaReversale.getCd_tipo_documento_amm()));

		List<Movimento_cogeBulk> movimenti = this.findMovimentiPrimaNota(userContext, partita);
		List<Movimento_cogeBulk> dettPnPatrimonialePartita = this.findMovimentiPatrimoniali(movimenti,partita);

		final BigDecimal totContiEp = BigDecimal.valueOf(dettPnPatrimonialePartita.stream()
				.mapToDouble(el->el.getIm_movimento().doubleValue())
				.sum());

		BigDecimal imDaRipartire = imReversale;
		//Chiudo i conti in percentuale
		for (Iterator<Movimento_cogeBulk> i = dettPnPatrimonialePartita.iterator(); i.hasNext(); ) {
			Movimento_cogeBulk dettPN=i.next();
			BigDecimal imRiga = imReversale.multiply(dettPN.getIm_movimento()).divide(totContiEp,2, RoundingMode.HALF_EVEN);
			if (imRiga.compareTo(imDaRipartire)>0 || !i.hasNext()) {
				testataPrimaNota.addDettaglio(partita.getTipoDocumentoEnum().getTipoPatrimoniale(), Movimento_cogeBulk.getControSezione(partita.getTipoDocumentoEnum().getSezionePatrimoniale()),
						dettPN.getCd_voce_ep(), imDaRipartire, partita, rigaReversale.getCd_terzo(), null);
				break;
			}
			testataPrimaNota.addDettaglio(partita.getTipoDocumentoEnum().getTipoPatrimoniale(), Movimento_cogeBulk.getControSezione(partita.getTipoDocumentoEnum().getSezionePatrimoniale()),
					dettPN.getCd_voce_ep(), imRiga, partita, rigaReversale.getCd_terzo(), null);
			imDaRipartire = imDaRipartire.subtract(imRiga);
		}
		testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), TipoDocumentoEnum.fromValue(rigaReversale.getCd_tipo_documento_amm()).getSezionePatrimoniale(), voceEpBanca.getCd_voce_ep(), imReversale);
	}

	private Ass_ev_voceepBulk findAssEvVoceep(UserContext userContext, Elemento_voceBulk voceBilancio) throws ComponentException, PersistencyException {
		Ass_ev_voceepHome assEvVoceEpHome = (Ass_ev_voceepHome) getHome(userContext, Ass_ev_voceepBulk.class);
		List<Ass_ev_voceepBulk> listAss = assEvVoceEpHome.findVociEpAssociateVoce(voceBilancio);
		if (listAss.isEmpty())
			throw new ApplicationException("Associazione tra voce del piano finanziario " + voceBilancio.getEsercizio() + "/" +
					voceBilancio.getTi_appartenenza() + "/" +
					voceBilancio.getTi_gestione() + "/" +
					voceBilancio.getCd_elemento_voce() + " ed economica non trovata.");
		else if (listAss.size() > 1)
			throw new ApplicationException("Troppi conti economici risultano associati alla voce " + voceBilancio.getEsercizio() + "/" +
					voceBilancio.getTi_appartenenza() + "/" +
					voceBilancio.getTi_gestione() + "/" +
					voceBilancio.getCd_elemento_voce() + ".");

		return listAss.get(0);
	}

	/**
	 * Ritorna il record Ass_ev_voceepBulk che risulta associato al conto economico indicato.
	 */
	private Ass_ev_voceepBulk findAssEvVoceep(UserContext userContext, Voce_epBulk contoEp) throws ComponentException, PersistencyException {
		Ass_ev_voceepHome assEvVoceEpHome = (Ass_ev_voceepHome) getHome(userContext, Ass_ev_voceepBulk.class);
		List<Ass_ev_voceepBulk> listAss = assEvVoceEpHome.findVociEpAssociateConto(contoEp);
		if (listAss.isEmpty())
			throw new ApplicationException("Associazione tra voce del piano finanziario e voce economica " + contoEp.getCd_voce_ep() + " non trovata.");
		else if (listAss.size() > 1)
			throw new ApplicationException("Troppe voci del piano finanziario risultano associate alla voce economica " + contoEp.getCd_voce_ep() + ".");

		return listAss.get(0);
	}

	private Voce_epBulk findContoIva(UserContext userContext, IDocumentoAmministrativoBulk docamm) throws ComponentException, RemoteException, PersistencyException {
		SezionaleBulk sezionale = ((SezionaleHome)getHome(userContext, SezionaleBulk.class)).getSezionaleByTipoDocumento(docamm);

		return Optional.ofNullable(sezionale).flatMap(el->Optional.ofNullable(el.getContoIva())).filter(el->el.getCd_voce_ep()!=null).flatMap(el->{
			try {
				Voce_epHome voceEpHome = (Voce_epHome) getHome(userContext, Voce_epBulk.class);
				return Optional.ofNullable((Voce_epBulk) voceEpHome.findByPrimaryKey(new Voce_epBulk(el.getCd_voce_ep(), el.getEsercizio())));
			} catch(ComponentException|PersistencyException ex) {
				throw new DetailedRuntimeException(ex);
			}
		}).orElseGet(()->{
			try {
				if (docamm.getTipoDocumentoEnum().isDocumentoAmministrativoPassivo())
					return this.findContoIvaCredito(userContext, docamm.getEsercizio());
				else
					return this.findContoIvaDebito(userContext, docamm.getEsercizio());
			} catch(ComponentException|RemoteException ex) {
				throw new DetailedRuntimeException(ex);
			}
		});
	}

	private Voce_epBulk findContoIvaSplit(UserContext userContext, IDocumentoAmministrativoBulk docamm) throws ComponentException, RemoteException, PersistencyException {
		SezionaleBulk sezionale = ((SezionaleHome)getHome(userContext, SezionaleBulk.class)).getSezionaleByTipoDocumento(docamm);

		return Optional.ofNullable(sezionale).flatMap(el->Optional.ofNullable(el.getContoIvaSplit())).filter(el->el.getCd_voce_ep()!=null).flatMap(el->{
			try {
				Voce_epHome voceEpHome = (Voce_epHome) getHome(userContext, Voce_epBulk.class);
				return Optional.ofNullable((Voce_epBulk) voceEpHome.findByPrimaryKey(new Voce_epBulk(el.getCd_voce_ep(), el.getEsercizio())));
			} catch(ComponentException|PersistencyException ex) {
				throw new DetailedRuntimeException(ex);
			}
		}).orElseGet(()->{
			try {
				if (docamm.getTipoDocumentoEnum().isDocumentoAmministrativoPassivo())
					return this.findContoIvaSplitDebito(userContext, docamm.getEsercizio());
				else
					return this.findContoIvaSplitCredito(userContext, docamm.getEsercizio());
			} catch(ComponentException|RemoteException ex) {
				throw new DetailedRuntimeException(ex);
			}
		});
	}

	private Voce_epBulk findContoQuotaDaTrattenereByConfig(UserContext userContext, Integer esercizio) throws ComponentException, RemoteException {
		return this.findContoByConfigurazioneCNR(userContext, esercizio, Configurazione_cnrBulk.PK_ELEMENTO_VOCE_SPECIALE, Configurazione_cnrBulk.SK_NETTO_DA_TRATTENERE, 2);
	}

	private Voce_epBulk findContoCostoLiquidazioneIvaEsterna(UserContext userContext, Integer esercizio) throws ComponentException, RemoteException {
		return this.findContoByConfigurazioneCNR(userContext, esercizio, Configurazione_cnrBulk.PK_VOCEEP_SPECIALE, Configurazione_cnrBulk.SK_CONTI_LIQUIDAZIONE_ESTERNA_IVA, 1);
	}

	private Voce_epBulk findContoDebitoUoLiquidazioneIvaEsterna(UserContext userContext, Integer esercizio) throws ComponentException, RemoteException {
		return this.findContoByConfigurazioneCNR(userContext, esercizio, Configurazione_cnrBulk.PK_VOCEEP_SPECIALE, Configurazione_cnrBulk.SK_CONTI_LIQUIDAZIONE_ESTERNA_IVA, 2);
	}

	private Voce_epBulk findContoErarioContoIva(UserContext userContext, Integer esercizio) throws ComponentException, RemoteException {
		return this.findContoByConfigurazioneCNR(userContext, esercizio, Configurazione_cnrBulk.PK_VOCEEP_SPECIALE, Configurazione_cnrBulk.SK_ERARIO_C_IVA, 1);
	}

	private Voce_epBulk findContoIvaCredito(UserContext userContext, Integer esercizio) throws ComponentException, RemoteException {
		return this.findContoByConfigurazioneCNR(userContext, esercizio, Configurazione_cnrBulk.PK_VOCEEP_SPECIALE, Configurazione_cnrBulk.SK_IVA_CREDITO, 1);
	}

	private Voce_epBulk findContoIvaSplitCredito(UserContext userContext, Integer esercizio) throws ComponentException, RemoteException {
		return this.findContoByConfigurazioneCNR(userContext, esercizio, Configurazione_cnrBulk.PK_VOCEEP_SPECIALE, Configurazione_cnrBulk.SK_IVA_CREDITO, 2);
	}

	private Voce_epBulk findContoIvaDebito(UserContext userContext, Integer esercizio) throws ComponentException, RemoteException {
		return this.findContoByConfigurazioneCNR(userContext, esercizio, Configurazione_cnrBulk.PK_VOCEEP_SPECIALE, Configurazione_cnrBulk.SK_IVA_DEBITO, 1);
	}

	private Voce_epBulk findContoIvaSplitDebito(UserContext userContext, Integer esercizio) throws ComponentException, RemoteException {
		return this.findContoByConfigurazioneCNR(userContext, esercizio, Configurazione_cnrBulk.PK_VOCEEP_SPECIALE, Configurazione_cnrBulk.SK_IVA_DEBITO, 2);
	}

	private Voce_epBulk findContoCreditoRitenuteSplitPayment(UserContext userContext, int esercizio) throws ComponentException, RemoteException {
		return this.findContoByConfigurazioneCNR(userContext, esercizio, Configurazione_cnrBulk.PK_VOCEEP_SPECIALE, Configurazione_cnrBulk.SK_CREDITO_RITENUTE_SPLIT_PAYMENT, 1);
	}

	private String findCodiceTributoIva(UserContext userContext) throws ComponentException, RemoteException {
		return this.findValueByConfigurazioneCNR(userContext, null, Configurazione_cnrBulk.PK_CORI_SPECIALE, Configurazione_cnrBulk.SK_IVA, 1);
	}

	private String findCodiceTributoIvaSplit(UserContext userContext) throws ComponentException, RemoteException {
		return this.findValueByConfigurazioneCNR(userContext, null, Configurazione_cnrBulk.PK_CORI_SPECIALE, Configurazione_cnrBulk.SK_IVA, 3);
	}

	private Voce_epBulk findContoByConfigurazioneCNR(UserContext userContext, int esercizio, String chiavePrimaria, String chiaveSecondaria, int fieldNumber) throws ComponentException, RemoteException {
		String value = this.findValueByConfigurazioneCNR(userContext, esercizio, chiavePrimaria, chiaveSecondaria, fieldNumber);

		return Optional.of(value).map(el->{
			try {
				Voce_epHome voceEpHome = (Voce_epHome) getHome(userContext, Voce_epBulk.class);
				return (Voce_epBulk) voceEpHome.findByPrimaryKey(new Voce_epBulk(el, esercizio));
			} catch(ComponentException|PersistencyException ex) {
				throw new DetailedRuntimeException(ex);
			}
		}).orElseThrow(()->new ApplicationException("Attenzione! Non esiste il conto economico "+value+" indicato nella tabella CONFIGURAZIONE_CNR per l'esercizio "+esercizio
				+" ("+chiavePrimaria+"-"+chiaveSecondaria+"-VAL0"+fieldNumber+")."));
	}

	private String findValueByConfigurazioneCNR(UserContext userContext, Integer esercizio, String chiavePrimaria, String chiaveSecondaria, int fieldNumber) throws ComponentException, RemoteException {
		Configurazione_cnrBulk config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione(userContext, esercizio, null, chiavePrimaria, chiaveSecondaria);

		return Optional.ofNullable(config).flatMap(el->Optional.ofNullable(el.getVal(fieldNumber)))
				.orElseThrow(()->new ApplicationException("Manca la configurazione richiesta nella tabella CONFIGURAZIONE_CNR per l'esercizio "+esercizio
				+" ("+chiavePrimaria+"-"+chiaveSecondaria+"-VAL0"+fieldNumber+")."));
	}

	private Voce_epBulk findContoCostoRicavo(UserContext userContext, Elemento_voceBulk voceBilancio) throws ComponentException, PersistencyException {
		return this.findAssEvVoceep(userContext, voceBilancio).getVoce_ep();
	}

	private Voce_epBulk findContoCosto(UserContext userContext, Tipo_contributo_ritenutaBulk tipoContributoRitenuta, int esercizio, String sezione, String tipoEntePercipiente) throws ComponentException, PersistencyException {
		Ass_tipo_cori_voce_epBulk assTipoCori = this.findAssociazioneCoriVoceEp(userContext, tipoContributoRitenuta, esercizio, sezione, tipoEntePercipiente);
		return Optional.ofNullable(assTipoCori).map(Ass_tipo_cori_voce_epBulk::getVoce_ep)
				.orElseThrow(()->new ApplicationException("Associazione tra tipo contributo/ritenuta ed economica non trovata (" +
						"Tipo Cori: " + tipoContributoRitenuta.getCd_contributo_ritenuta() +
						" - Esercizio: " + esercizio +
						" - Tipo Ente Percipiente: " + tipoEntePercipiente +
						" - Sezione: " + sezione + ")."));
	}

	private Ass_tipo_cori_voce_epBulk findAssociazioneCoriVoceEp(UserContext userContext, Tipo_contributo_ritenutaBulk tipoContributoRitenuta, int esercizio, String sezione, String tipoEntePercipiente) throws ComponentException, PersistencyException {
		Ass_tipo_cori_voce_epHome assTipoCoriVoceEpHome = (Ass_tipo_cori_voce_epHome) getHome(userContext, Ass_tipo_cori_voce_epBulk.class);
		Ass_tipo_cori_voce_epBulk assTipoCori = assTipoCoriVoceEpHome.getAssCoriEp(esercizio, tipoContributoRitenuta.getCd_contributo_ritenuta(), tipoEntePercipiente, sezione);
		return Optional.ofNullable(assTipoCori).orElseThrow(()->new ApplicationException("Associazione tra tipo contributo/ritenuta ed economica non trovata (" +
				"Tipo Cori: " + tipoContributoRitenuta.getCd_contributo_ritenuta() +
				" - Esercizio: " + esercizio +
				" - Tipo Ente Percipiente: " + tipoEntePercipiente +
				" - Sezione: " + sezione + ")."));
	}

	private Voce_epBulk findContoBanca(UserContext userContext, int esercizio) throws ComponentException, RemoteException {
		return this.findContoByConfigurazioneCNR(userContext, esercizio, Configurazione_cnrBulk.PK_VOCEEP_SPECIALE, Configurazione_cnrBulk.SK_BANCA, 1);
	}

	private Voce_epBulk findContoCreditoAnticipo(UserContext userContext, Integer esercizio) throws ComponentException, RemoteException {
		return this.findContoByConfigurazioneCNR(userContext, esercizio, Configurazione_cnrBulk.PK_VOCEEP_SPECIALE, Configurazione_cnrBulk.SK_CREDITO_DEBITO_ANTICIPO, 1);
	}

	private Voce_epBulk findContoDebitoAnticipo(UserContext userContext, Integer esercizio) throws ComponentException, RemoteException {
		return this.findContoByConfigurazioneCNR(userContext, esercizio, Configurazione_cnrBulk.PK_VOCEEP_SPECIALE, Configurazione_cnrBulk.SK_CREDITO_DEBITO_ANTICIPO, 2);
	}

	private Voce_epBulk findContoCreditoEconomo(UserContext userContext, Integer esercizio) throws ComponentException, RemoteException {
		return this.findContoByConfigurazioneCNR(userContext, esercizio, Configurazione_cnrBulk.PK_VOCEEP_SPECIALE, Configurazione_cnrBulk.SK_CREDITO_DEBITO_ECONOMO, 1);
	}

	private Voce_epBulk findContoDebitoEconomo(UserContext userContext, Integer esercizio) throws ComponentException, RemoteException {
		return this.findContoByConfigurazioneCNR(userContext, esercizio, Configurazione_cnrBulk.PK_VOCEEP_SPECIALE, Configurazione_cnrBulk.SK_CREDITO_DEBITO_ECONOMO, 2);
	}

	private Pair<Voce_epBulk, Voce_epBulk> findPairCosto(UserContext userContext, AnticipoBulk anticipo) throws ComponentException, RemoteException, PersistencyException {
		if (Optional.ofNullable(anticipo.getScadenza_obbligazione()).isPresent())
			return this.findPairCosto(userContext, Optional.ofNullable(anticipo.getTerzo()).orElse(new TerzoBulk(anticipo.getCd_terzo())), anticipo.getScadenza_obbligazione().getObbligazione(), Movimento_cogeBulk.TipoRiga.DEBITO.value());
		return null;
	}

	private Pair<Voce_epBulk, Voce_epBulk> findPairCosto(UserContext userContext, MissioneBulk missione) throws ComponentException, RemoteException, PersistencyException {
		if (Optional.ofNullable(missione.getObbligazione_scadenzario()).isPresent())
			return this.findPairCosto(userContext, Optional.ofNullable(missione.getTerzo()).orElse(new TerzoBulk(missione.getCd_terzo())), missione.getObbligazione_scadenzario().getObbligazione(), Movimento_cogeBulk.TipoRiga.DEBITO.value());
		return null;
	}

	private Pair<Voce_epBulk, Voce_epBulk> findPairCosto(UserContext userContext, CompensoBulk compenso) throws ComponentException, RemoteException, PersistencyException {
		if (Optional.ofNullable(compenso.getObbligazioneScadenzario()).isPresent())
			return this.findPairCosto(userContext, Optional.ofNullable(compenso.getTerzo()).orElse(new TerzoBulk(compenso.getCd_terzo())), compenso.getObbligazioneScadenzario().getObbligazione(), Movimento_cogeBulk.TipoRiga.DEBITO.value());
		return null;
	}

	private Pair<Voce_epBulk, Voce_epBulk> findPairCosto(UserContext userContext, TerzoBulk terzo, ObbligazioneBulk obbligazione, String tipoContoPatrimoniale) throws ComponentException, RemoteException, PersistencyException {
		ObbligazioneBulk obbligazioneDB = Optional.ofNullable(obbligazione).filter(el->el.getCrudStatus()!=OggettoBulk.UNDEFINED).orElseGet(()->{
			try {
				ObbligazioneHome obbligazionehome = (ObbligazioneHome) getHome(userContext, ObbligazioneBulk.class);
				return (ObbligazioneBulk) obbligazionehome.findByPrimaryKey(obbligazione);
			} catch (ComponentException | PersistencyException e) {
				throw new DetailedRuntimeException(e);
			}
		});
		return this.findPairCosto(userContext, terzo, obbligazioneDB.getElemento_voce(), Movimento_cogeBulk.TipoRiga.DEBITO.value());
	}

	private Pair<Voce_epBulk, Voce_epBulk> findPairCosto(UserContext userContext, DettaglioFinanziario dettaglioFinanziario) throws ComponentException, RemoteException, PersistencyException {
		return this.findPairCosto(userContext, new TerzoBulk(dettaglioFinanziario.getCdTerzo()), dettaglioFinanziario.getElementoVoce(),
				dettaglioFinanziario.getDocamm().getTipoDocumentoEnum().getTipoPatrimoniale());
	}

	private Pair<Voce_epBulk, Voce_epBulk> findPairCosto(UserContext userContext, TerzoBulk terzo, Elemento_voceBulk elementoVoce, String tipoContoPatrimoniale) throws ComponentException, RemoteException, PersistencyException {
		Voce_epBulk aContoCosto = this.findContoCostoRicavo(userContext, elementoVoce);
		Voce_epBulk aContoContropartita = this.findContoAnag(userContext, terzo, elementoVoce, aContoCosto, tipoContoPatrimoniale);
		return Pair.of(aContoCosto, aContoContropartita);
	}

	/**
	 * Ritorna il conto di costo ed il conto di contropartita associato al contributoRitenuta
	 *
	 * @param userContext userContext
	 * @param cori Contributo_ritenutaBulk
	 * @return Pair - first=costo - second=controcosto
	 * @throws ComponentException ComponentException
	 * @throws PersistencyException PersistencyException
	 */
	private Pair<Voce_epBulk, Voce_epBulk> findPairCostoCompenso(UserContext userContext, Contributo_ritenutaBulk cori) throws ComponentException, PersistencyException {
		Voce_epBulk voceCosto = this.findContoCosto(userContext, cori.getTipoContributoRitenuta(), cori.getEsercizio(), cori.getSezioneCostoRicavo(), cori.getTi_ente_percipiente());
		Voce_epBulk voceContropartita = this.findContoContropartitaCosto(userContext, voceCosto);
		return Pair.of(voceCosto, voceContropartita);
	}

	private Pair<Voce_epBulk, Voce_epBulk> findPairContiMandato(UserContext userContext, Contributo_ritenutaBulk cori) throws ComponentException, PersistencyException {
		Ass_tipo_cori_voce_epBulk coriVoceEp = this.findAssociazioneCoriVoceEp(userContext, cori.getTipoContributoRitenuta(), cori.getEsercizio(), cori.getSezioneCostoRicavo(), cori.getTi_ente_percipiente());
		Voce_epBulk voceContropartita = this.findContoContropartitaCosto(userContext, coriVoceEp.getVoce_ep());
		return Pair.of(voceContropartita, coriVoceEp.getVoce_ep_contr());
	}

	/**
	 * Ritorna il conto di contropartita associato al conto di costo indicato recuperandolo dalla tabella ASS_EV_VOCEEP
	 */
	private Voce_epBulk findContoContropartitaCosto(UserContext userContext, Voce_epBulk voceEpBulk) throws ComponentException, PersistencyException {
		return Optional.ofNullable(this.findAssEvVoceep(userContext, voceEpBulk)).map(Ass_ev_voceepBulk::getVoce_ep_contr)
				.orElseThrow(()->new ApplicationException("Associazione tra voce del piano finanziario e voce economica " + voceEpBulk.getCd_voce_ep() + " non trovata."));
	}

	private Voce_epBulk findContoAnag(UserContext userContext, TerzoBulk terzo, Elemento_voceBulk voceBilancio, Voce_epBulk aContoCosto, String tipoConto) throws ComponentException, RemoteException, PersistencyException {
		String valueAssociazioneConti = this.findValueByConfigurazioneCNR(userContext, CNRUserContext.getEsercizio(userContext), Configurazione_cnrBulk.PK_ECONOMICO_PATRIMONIALE, Configurazione_cnrBulk.SK_ASSOCIAZIONE_CONTI, 1);

		if (valueAssociazioneConti.equals("TERZO"))
			return this.findContoAnag(userContext, terzo, tipoConto);
		else if (valueAssociazioneConti.equals("CONTO")) {
			String valueTipoEP = this.findValueByConfigurazioneCNR(userContext, CNRUserContext.getEsercizio(userContext), Configurazione_cnrBulk.PK_ECONOMICO_PATRIMONIALE, Configurazione_cnrBulk.SK_TIPO_ECONOMICO_PATRIMONIALE, 1);
			if (valueTipoEP.equals("PARALLELA"))
				return this.findContoAnag(userContext, voceBilancio);
			return this.findContoAnag(userContext, voceBilancio);
			//TODO da ripristinare
			//return this.findContoAnag(userContext, aContoCosto);
		}
		throw new ApplicationRuntimeException("Attenzione! Non è gestito il valore "+valueAssociazioneConti+" indicato nella tabella CONFIGURAZIONE_CNR per l'esercizio "+CNRUserContext.getEsercizio(userContext)
				+" ("+Configurazione_cnrBulk.PK_ECONOMICO_PATRIMONIALE+"-"+Configurazione_cnrBulk.SK_ASSOCIAZIONE_CONTI+"-VAL01).");
	}

	private Voce_epBulk findContoAnag(UserContext userContext, Elemento_voceBulk voceBilancio) throws ComponentException, PersistencyException {
		return Optional.ofNullable(this.findAssEvVoceep(userContext, voceBilancio).getVoce_ep_contr())
				.orElseThrow(()->new ApplicationRuntimeException("Conto di contropartita mancante in associazione con la voce del piano finanziario " +
						voceBilancio.getEsercizio() + "/" + voceBilancio.getTi_appartenenza() + "/"  +
						voceBilancio.getTi_gestione() + "/" +
						voceBilancio.getCd_elemento_voce() + "."));
	}

	private Voce_epBulk findContoAnag(UserContext userContext, Voce_epBulk aContoCosto) {
		return Optional.of(aContoCosto).filter(el->Optional.ofNullable(el.getCd_voce_ep_contr()).isPresent())
				.flatMap(el->{
					try {
						Voce_epHome voceEpHome = (Voce_epHome) getHome(userContext, Voce_epBulk.class);
						return Optional.ofNullable((Voce_epBulk) voceEpHome.findByPrimaryKey(new Voce_epBulk(el.getCd_voce_ep_contr(), el.getEsercizio())));
					} catch(ComponentException|PersistencyException ex) {
						throw new DetailedRuntimeException(ex);
					}
				})
				.orElseThrow(()->new ApplicationRuntimeException("Conto di contropartita mancante in associazione con il conto economico " +
						aContoCosto.getEsercizio() + "/" + aContoCosto.getCd_voce_ep() + "."));
	}

	private Voce_epBulk findContoAnag(UserContext userContext, TerzoBulk terzo, String tipoConto) throws ComponentException, PersistencyException {
		Voce_epBulk voceEpBulk = null;

		if (Optional.ofNullable(terzo).isPresent()) {
			TerzoHome terzohome = (TerzoHome) getHome(userContext, TerzoBulk.class);
			TerzoBulk terzoDB = (TerzoBulk) terzohome.findByPrimaryKey(terzo);

			Anagrafico_esercizioHome anagesehome = (Anagrafico_esercizioHome) getHome(userContext, Anagrafico_esercizioBulk.class);
			Anagrafico_esercizioBulk anagEse = (Anagrafico_esercizioBulk) anagesehome.findByPrimaryKey(new Anagrafico_esercizioBulk(terzoDB.getCd_anag(), CNRUserContext.getEsercizio(userContext)));

			if (tipoConto.equals(Movimento_cogeBulk.TipoRiga.DEBITO.value())) {
				if (Optional.ofNullable(anagEse).flatMap(el -> Optional.ofNullable(el.getCd_voce_debito_ep())).isPresent()) {
					Voce_epHome voceEpHome = (Voce_epHome) getHome(userContext, Voce_epBulk.class);
					voceEpBulk = (Voce_epBulk) voceEpHome.findByPrimaryKey(new Voce_epBulk(anagEse.getCd_voce_debito_ep(), anagEse.getEsercizio_voce_debito_ep()));
				}
			} else {
				if (Optional.ofNullable(anagEse).flatMap(el -> Optional.ofNullable(el.getCd_voce_credito_ep())).isPresent()) {
					Voce_epHome voceEpHome = (Voce_epHome) getHome(userContext, Voce_epBulk.class);
					voceEpBulk = (Voce_epBulk) voceEpHome.findByPrimaryKey(new Voce_epBulk(anagEse.getCd_voce_credito_ep(), anagEse.getEsercizio_voce_credito_ep()));
				}
			}
			return Optional.ofNullable(voceEpBulk)
					.orElseThrow(()->new ApplicationRuntimeException("Conto " +
							(tipoConto.equals(Movimento_cogeBulk.TipoRiga.DEBITO.value()) ? "debito" : "credito") +
							" associato al codice terzo " + terzo.getCd_terzo() + " non individuato."));
		}
		throw new ApplicationRuntimeException("Conto " +
				(tipoConto.equals(Movimento_cogeBulk.TipoRiga.DEBITO.value()) ? "debito" : "credito") +
				" non individuato! Non risulta essere stato specificato il codice terzo di cui individuare l'associazione!");
	}

	private Map<String, Pair<String, BigDecimal>> getSaldiMovimentiPartita(UserContext userContext, IDocumentoAmministrativoBulk docamm, Integer cdTerzoDocAmm, Optional<Scrittura_partita_doppiaBulk> scritturaToExclude) throws ComponentException, PersistencyException {
		boolean isAttivaEconomica = ((Configurazione_cnrHome)getHome(userContext, Configurazione_cnrBulk.class)).isAttivaEconomica(docamm.getEsercizio());
		if (isAttivaEconomica)
			return ((Movimento_cogeHome) getHome(userContext, Movimento_cogeBulk.class)).getSaldiMovimentiPartita(docamm, cdTerzoDocAmm, scritturaToExclude);
		else {
			try {
				Map<String, Pair<String, BigDecimal>> result = new HashMap<>();

				Collection<Movimento_cogeBulk> allMovimentiCoge = proposeScritturaPartitaDoppia(userContext, docamm).getAllMovimentiColl()
						.stream().filter(el->docamm.getEsercizio().equals(el.getEsercizio_documento()))
						.filter(el->docamm.getCd_cds().equals(el.getCd_cds_documento()))
						.filter(el->docamm.getCd_uo().equals(el.getCd_uo_documento()))
						.filter(el->docamm.getPg_doc().equals(el.getPg_numero_documento()))
						.filter(el->docamm.getCd_tipo_doc().equals(el.getCd_tipo_documento()))
						.filter(el->cdTerzoDocAmm.equals(el.getCd_terzo()))
						.filter(el->!Optional.ofNullable(el.getCd_contributo_ritenuta()).isPresent())
						.filter(el->
							(docamm.getTipoDocumentoEnum().isDocumentoPassivo() && el.getTi_riga().equals(Movimento_cogeBulk.TipoRiga.DEBITO.value())) ||
							(docamm.getTipoDocumentoEnum().isDocumentoAttivo() && el.getTi_riga().equals(Movimento_cogeBulk.TipoRiga.CREDITO.value()))
						)
						.collect(Collectors.toList());

				Map<String, List<Movimento_cogeBulk>> mapVoceEp = allMovimentiCoge.stream().collect(Collectors.groupingBy(Movimento_cogeBulk::getCd_voce_ep));

				mapVoceEp.keySet().forEach(cdVoceEp->{
					List<Movimento_cogeBulk> movimentiList = mapVoceEp.get(cdVoceEp);
					BigDecimal totaleDare = movimentiList.stream()
							.filter(Movimento_cogeBulk::isSezioneDare)
							.map(Movimento_cogeBulk::getIm_movimento).reduce(BigDecimal.ZERO, BigDecimal::add);

					BigDecimal totaleAvere = movimentiList.stream()
							.filter(Movimento_cogeBulk::isSezioneAvere)
							.map(Movimento_cogeBulk::getIm_movimento).reduce(BigDecimal.ZERO, BigDecimal::add);

					BigDecimal saldo = totaleDare.subtract(totaleAvere);
					if (saldo.compareTo(BigDecimal.ZERO)>=0)
						result.put(cdVoceEp, Pair.of(Movimento_cogeBulk.SEZIONE_DARE, saldo));
					else
						result.put(cdVoceEp, Pair.of(Movimento_cogeBulk.SEZIONE_AVERE, saldo.abs()));
				});
				return result;

			} catch (ScritturaPartitaDoppiaNotRequiredException ignored) {
			}
		}
		return new HashMap<>();
	}

	private Map<String, Pair<String, BigDecimal>> getSaldiMovimentiCori(UserContext userContext, IDocumentoAmministrativoBulk docamm, Integer cdTerzoDocamm, String cdCori, Optional<Scrittura_partita_doppiaBulk> scritturaToExclude) throws ComponentException, PersistencyException {
		return getSaldiMovimentiCori(userContext, docamm, cdTerzoDocamm, cdCori, scritturaToExclude, null);
	}

	private Map<String, Pair<String, BigDecimal>> getSaldiMovimentiCori(UserContext userContext, IDocumentoAmministrativoBulk docamm, Integer cdTerzoDocamm, String cdCori, Optional<Scrittura_partita_doppiaBulk> scritturaToExclude, List<MandatoBulk> pMandatiCompenso) throws ComponentException, PersistencyException {
		Map<String, Pair<String, BigDecimal>> result = new HashMap<>();
		List<IDocumentoCogeBulk> docMovimentiCori = new ArrayList<>();

		boolean isAttivaEconomicaDocamm = ((Configurazione_cnrHome) getHome(userContext, Configurazione_cnrBulk.class)).isAttivaEconomica(docamm.getEsercizio());
		if (isAttivaEconomicaDocamm)
			result.putAll(((Movimento_cogeHome) getHome(userContext, Movimento_cogeBulk.class)).getSaldiMovimentiCori(docamm, cdTerzoDocamm, cdCori, scritturaToExclude));
		else {
			docMovimentiCori.add(docamm);
			if (pMandatiCompenso!=null && !pMandatiCompenso.isEmpty())
				docMovimentiCori.addAll(pMandatiCompenso);

			Collection<Movimento_cogeBulk> allMovimentiCoge = new ArrayList<>();

			docMovimentiCori.forEach(docMov-> {
				try {
					allMovimentiCoge.addAll(proposeScritturaPartitaDoppia(userContext, docMov).getAllMovimentiColl()
									.stream().filter(el -> docamm.getEsercizio().equals(el.getEsercizio_documento()))
									.filter(el -> docamm.getCd_cds().equals(el.getCd_cds_documento()))
									.filter(el -> docamm.getCd_uo().equals(el.getCd_uo_documento()))
									.filter(el -> docamm.getPg_doc().equals(el.getPg_numero_documento()))
									.filter(el -> docamm.getCd_tipo_doc().equals(el.getCd_tipo_documento()))
									.filter(el -> cdTerzoDocamm.equals(el.getCd_terzo()))
									.filter(el -> cdCori.equals(el.getCd_contributo_ritenuta()))
									.collect(Collectors.toList()));
				} catch (ScritturaPartitaDoppiaNotRequiredException ignored) {
				} catch (ComponentException e) {
					throw new ApplicationRuntimeException(e);
				}
			});

			Map<String, List<Movimento_cogeBulk>> mapVoceEp = allMovimentiCoge.stream().collect(Collectors.groupingBy(Movimento_cogeBulk::getCd_voce_ep));

			mapVoceEp.keySet().forEach(cdVoceEp -> {
				List<Movimento_cogeBulk> movimentiList = mapVoceEp.get(cdVoceEp);
				BigDecimal totaleDare = movimentiList.stream()
						.filter(Movimento_cogeBulk::isSezioneDare)
						.map(Movimento_cogeBulk::getIm_movimento).reduce(BigDecimal.ZERO, BigDecimal::add);

				BigDecimal totaleAvere = movimentiList.stream()
						.filter(Movimento_cogeBulk::isSezioneAvere)
						.map(Movimento_cogeBulk::getIm_movimento).reduce(BigDecimal.ZERO, BigDecimal::add);

				BigDecimal saldo = totaleDare.subtract(totaleAvere);
				if (saldo.compareTo(BigDecimal.ZERO) >= 0)
					result.put(cdVoceEp, Pair.of(Movimento_cogeBulk.SEZIONE_DARE, saldo));
				else
					result.put(cdVoceEp, Pair.of(Movimento_cogeBulk.SEZIONE_AVERE, saldo.abs()));
			});
		}
		return result;
	}

	/**
	 * Ritorna la lista delle righe prima nota associate al documento
	 * @param userContext userContext
	 * @param docamm docamm
	 * @return List<Movimento_cogeBulk> List<Movimento_cogeBulk>
	 * @throws ComponentException ComponentException
	 * @throws PersistencyException PersistencyException
	 */
	private List<Movimento_cogeBulk> findMovimentiPrimaNota(UserContext userContext, IDocumentoCogeBulk docamm) throws ComponentException, PersistencyException {
		//Se è attiva l'economica per l'esercizio del documento allora leggo la scrittura, altrimenti ritorno ciò che proporrei dato che negli anni dove
		//l'economica non è attiva la scrittura è diversa.
		boolean isAttivaEconomica = ((Configurazione_cnrHome)getHome(userContext, Configurazione_cnrBulk.class)).isAttivaEconomica(docamm.getEsercizio());
		if (isAttivaEconomica) {
			Scrittura_partita_doppiaHome partitaDoppiaHome = Optional.ofNullable(getHome(userContext, Scrittura_partita_doppiaBulk.class))
					.filter(Scrittura_partita_doppiaHome.class::isInstance)
					.map(Scrittura_partita_doppiaHome.class::cast)
					.orElseThrow(() -> new DetailedRuntimeException("Partita doppia Home not found"));
			final Optional<Scrittura_partita_doppiaBulk> scritturaOpt = partitaDoppiaHome.findByDocumentoAmministrativo(docamm);
			if (scritturaOpt.isPresent()) {
				Scrittura_partita_doppiaBulk scrittura = scritturaOpt.get();
				scrittura.setMovimentiDareColl(new BulkList(((Scrittura_partita_doppiaHome) getHome(userContext, scrittura.getClass()))
						.findMovimentiDareColl(userContext, scrittura, false)));
				scrittura.setMovimentiAvereColl(new BulkList(((Scrittura_partita_doppiaHome) getHome(userContext, scrittura.getClass()))
						.findMovimentiAvereColl(userContext, scrittura, false)));
				return scrittura.getAllMovimentiColl();
			}
		} else {
			try {
				return proposeScritturaPartitaDoppia(userContext, docamm).getAllMovimentiColl();
			} catch (ScritturaPartitaDoppiaNotRequiredException ignored) {
			}
		}
		return Collections.EMPTY_LIST;
	}

	private Movimento_cogeBulk findMovimentoAperturaPartita(List<Movimento_cogeBulk> movimentiCoge, IDocumentoCogeBulk docamm, Integer cdTerzo) throws ComponentException {
		List<Movimento_cogeBulk> result = movimentiCoge.stream()
				.filter(el->docamm.getTipoDocumentoEnum().getSezionePatrimoniale().equals(el.getSezione()))
				.filter(el->docamm.getTipoDocumentoEnum().getTipoPatrimoniale().equals(el.getTi_riga()))
				.filter(el->docamm.getCd_tipo_doc().equals(el.getCd_tipo_documento()))
				.filter(el->docamm.getEsercizio().equals(el.getEsercizio_documento()))
				.filter(el->docamm.getPg_doc().equals(el.getPg_numero_documento()))
				.filter(el->cdTerzo.equals(el.getCd_terzo()))
				.filter(el->el.getCd_contributo_ritenuta()==null)
				.collect(Collectors.toList());

		if (result.size()>1)
			throw new ApplicationException("Errore nell'individuazione del singolo movimento di prima nota di apertura della partita "
				+docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getPg_doc()+": sono state individuate troppe righe.");
		return result.stream().findFirst()
				.orElseThrow(()->new ApplicationRuntimeException("Errore nell'individuazione del singolo movimento di prima nota della partita "
						+docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getPg_doc()+": non è stata individuata nessuna riga."));
	}

	private Movimento_cogeBulk findMovimentoAperturaCori(List<Movimento_cogeBulk> movimentiCoge, IDocumentoCogeBulk docamm, Integer cdTerzo, String cdCori) throws ComponentException {
		List<Movimento_cogeBulk> result = movimentiCoge.stream()
				.filter(el->docamm.getTipoDocumentoEnum().getTipoPatrimoniale().equals(el.getTi_riga()))
				.filter(el->docamm.getCd_tipo_doc().equals(el.getCd_tipo_documento()))
				.filter(el->docamm.getEsercizio().equals(el.getEsercizio_documento()))
				.filter(el->docamm.getPg_doc().equals(el.getPg_numero_documento()))
				.filter(el->cdTerzo.equals(el.getCd_terzo()))
				.filter(el->cdCori.equals(el.getCd_contributo_ritenuta()))
				.collect(Collectors.toList());

		if (result.size()>1)
			throw new ApplicationException("Errore nell'individuazione del singolo movimento di prima nota di apertura del contributo "+cdCori
					+ " associato alla partita "+docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getPg_doc()+": sono state individuate troppe righe.");
		return result.stream().findFirst()
				.orElseThrow(()->new ApplicationRuntimeException("Errore nell'individuazione del singolo movimento di prima nota di apertura del contributo "+cdCori
				 	+ " associato alla partita "+docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getPg_doc()+": non è stata individuata nessuna riga."));
	}

	private Movimento_cogeBulk findMovimentoAperturaCoriIVACompenso(List<Movimento_cogeBulk> movimentiCoge, CompensoBulk docamm, Integer cdTerzo, String cdCori) throws ComponentException {

		List<Movimento_cogeBulk> result = movimentiCoge.stream()
				.filter(el->docamm.getTipoDocumentoEnum().getSezioneIva().equals(el.getSezione()))
				.filter(el-> (docamm.getFl_split_payment()?Movimento_cogeBulk.TipoRiga.CREDITO.value():Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO.value())
						.equals(el.getTi_riga()))
				.filter(el->docamm.getCd_tipo_doc().equals(el.getCd_tipo_documento()))
				.filter(el->docamm.getEsercizio().equals(el.getEsercizio_documento()))
				.filter(el->docamm.getPg_doc().equals(el.getPg_numero_documento()))
				.filter(el->cdTerzo.equals(el.getCd_terzo()))
				.filter(el->cdCori.equals(el.getCd_contributo_ritenuta()))
				.collect(Collectors.toList());

		if (result.size()>1)
			throw new ApplicationException("Errore nell'individuazione del singolo movimento di prima nota di apertura del contributo "+cdCori
					+ " associato alla partita "+docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getPg_doc()+": sono state individuate troppe righe.");
		return result.stream().findFirst()
				.orElseThrow(()->new ApplicationRuntimeException("Errore nell'individuazione del singolo movimento di prima nota di apertura del contributo "+cdCori
						+ " associato alla partita "+docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getPg_doc()+": non è stata individuata nessuna riga."));
	}
	private Movimento_cogeBulk findMovimentoAperturaCreditoAnticipo(List<Movimento_cogeBulk> movimentiCoge, AnticipoBulk docamm, Integer cdTerzo) throws ComponentException {
		List<Movimento_cogeBulk> result = movimentiCoge.stream()
				.filter(el->docamm.getTipoDocumentoEnum().getSezioneEconomica().equals(el.getSezione()))
				.filter(el->docamm.getTipoDocumentoEnum().getTipoEconomica().equals(el.getTi_riga()))
				.filter(el->docamm.getCd_tipo_doc().equals(el.getCd_tipo_documento()))
				.filter(el->docamm.getEsercizio().equals(el.getEsercizio_documento()))
				.filter(el->docamm.getPg_doc().equals(el.getPg_numero_documento()))
				.filter(el->cdTerzo.equals(el.getCd_terzo()))
				.filter(el->el.getCd_contributo_ritenuta()==null)
				.collect(Collectors.toList());

		if (result.size()>1)
			throw new ApplicationException("Errore nell'individuazione del singolo movimento di prima nota di apertura credito dell'anticipo "
					+docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getPg_doc()+": sono state individuate troppe righe.");
		return result.stream().findFirst()
				.orElseThrow(()->new ApplicationRuntimeException("Errore nell'individuazione del singolo movimento di prima nota di apertura credito dell'anticipo "
						+docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getPg_doc()+": non è stata individuata nessuna riga."));
	}

	private Movimento_cogeBulk findMovimentoAperturaCreditoEconomo(List<Movimento_cogeBulk> movimentiCoge, Documento_genericoBulk docamm, Integer cdTerzo) throws ComponentException {
		List<Movimento_cogeBulk> result = movimentiCoge.stream()
				.filter(el->docamm.getTipoDocumentoEnum().getSezioneEconomica().equals(el.getSezione()))
				.filter(el->docamm.getTipoDocumentoEnum().getTipoEconomica().equals(el.getTi_riga()))
				.filter(el->docamm.getCd_tipo_doc().equals(el.getCd_tipo_documento()))
				.filter(el->docamm.getEsercizio().equals(el.getEsercizio_documento()))
				.filter(el->docamm.getPg_doc().equals(el.getPg_numero_documento()))
				.filter(el->cdTerzo.equals(el.getCd_terzo()))
				.filter(el->el.getCd_contributo_ritenuta()==null)
				.collect(Collectors.toList());

		if (result.size()>1)
			throw new ApplicationException("Errore nell'individuazione del singolo movimento di prima nota di apertura credito dell'economo "
					+docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getPg_doc()+": sono state individuate troppe righe.");
		return result.stream().findFirst()
				.orElseThrow(()->new ApplicationRuntimeException("Errore nell'individuazione del singolo movimento di prima nota di apertura credito dell'economo "
						+docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getPg_doc()+": non è stata individuata nessuna riga."));
	}

	/**
	 * Ritorna la lista delle righe prima nota associate che movimentano i conti patrimoniali di tipo debito/credito associati al documento
	 * @param docamm docamm
	 * @return List<Movimento_cogeBulk> List<Movimento_cogeBulk>
	 */
	private List<Movimento_cogeBulk> findMovimentiPatrimoniali(List<Movimento_cogeBulk> movimentiCoge, IDocumentoCogeBulk docamm) {
		List<Movimento_cogeBulk> movimenti = movimentiCoge.stream().filter(el->el.getSezione().equals(docamm.getTipoDocumentoEnum().getSezionePatrimoniale()))
				.filter(el->docamm.getTipoDocumentoEnum().getTipoPatrimoniale().equals(el.getTi_riga())).collect(Collectors.toList());

		if (movimenti.isEmpty()) {
			throw new ApplicationRuntimeException("Non è stato possibile individuare la riga di tipo debito/credito per il documento " +
					docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getCd_cds()+"/"+docamm.getCd_uo()+"/"+docamm.getPg_doc()+
					". Proposta di prima nota non possibile.");
		}
		return movimenti;
	}

	private Scrittura_partita_doppiaBulk generaScrittura(UserContext userContext, IDocumentoCogeBulk doccoge, List<TestataPrimaNota> testataPrimaNota, boolean accorpaConti) {
		Scrittura_partita_doppiaBulk scritturaPartitaDoppia = this.makeScrittura(userContext, doccoge, testataPrimaNota, accorpaConti);

		//Metto il controllo che Dare=Avere
		if (scritturaPartitaDoppia!=null && scritturaPartitaDoppia.getImTotaleDare().compareTo(scritturaPartitaDoppia.getImTotaleAvere())!=0)
			throw new ApplicationRuntimeException("Errore nella generazione scrittura prima nota del documento "+
					doccoge.getTipoDocumentoEnum()+"/"+doccoge.getEsercizio()+"/"+doccoge.getCd_cds()+"/"+doccoge.getCd_uo()+
					"/"+doccoge.getPg_doc()+". Il totale Dare (" +
					new it.cnr.contab.util.EuroFormat().format(scritturaPartitaDoppia.getImTotaleDare()) +
					") non risulterebbe essere uguale al totale Avere (" +
					new it.cnr.contab.util.EuroFormat().format(scritturaPartitaDoppia.getImTotaleAvere())+").");

		if (doccoge instanceof MandatoBulk || doccoge instanceof ReversaleBulk) {
			//Metto il controllo che il conto tesoreria sia sempre pari al netto mandato/reversale se richiesto di fare controllo
			BigDecimal saldoTesoreria = Optional.ofNullable(scritturaPartitaDoppia).map(spd->spd.getAllMovimentiColl().stream()
					.filter(Movimento_cogeBulk::isRigaTipoTesoreria)
					.map(el -> el.isSezioneDare() ? el.getIm_movimento() : el.getIm_movimento().negate())
					.reduce(BigDecimal.ZERO, BigDecimal::add)).orElse(BigDecimal.ZERO);
			if (doccoge instanceof MandatoBulk && saldoTesoreria.negate().compareTo(((MandatoBulk) doccoge).getIm_netto()) != 0)
				throw new ApplicationRuntimeException("Errore nella generazione scrittura prima nota del mandato "+doccoge.getEsercizio()+"/"+
						doccoge.getCd_cds()+"/"+doccoge.getPg_doc()+". Il saldo del conto tesoreria (" +
						new it.cnr.contab.util.EuroFormat().format(saldoTesoreria.negate()) +
						") non risulterebbe essere uguale all'importo netto del mandato (" +
						new it.cnr.contab.util.EuroFormat().format(((MandatoBulk) doccoge).getIm_netto())+").");
			if (doccoge instanceof ReversaleBulk && saldoTesoreria.compareTo(((ReversaleBulk) doccoge).getIm_reversale()) != 0)
				throw new ApplicationRuntimeException("Errore nella generazione scrittura prima nota della reversale "+doccoge.getEsercizio()+"/"+
						doccoge.getCd_cds()+"/"+doccoge.getPg_doc()+". Il saldo del conto tesoreria  (" +
						new it.cnr.contab.util.EuroFormat().format(saldoTesoreria) +
						") non risulterebbe essere uguale all'importo della reversale (" +
						new it.cnr.contab.util.EuroFormat().format(((ReversaleBulk) doccoge).getIm_reversale())+").");
		}

		return scritturaPartitaDoppia;
	}

	private Scrittura_partita_doppiaBulk makeScrittura(UserContext userContext, IDocumentoCogeBulk doccoge, List<TestataPrimaNota> testataPrimaNota, boolean accorpaConti) {
		if (!testataPrimaNota.stream().flatMap(el->el.getDett().stream()).findAny().isPresent())
			return null;

		Scrittura_partita_doppiaBulk scritturaPartitaDoppia = new Scrittura_partita_doppiaBulk();

		scritturaPartitaDoppia.setToBeCreated();
		scritturaPartitaDoppia.setDt_contabilizzazione(doccoge.getDt_contabilizzazione());
		scritturaPartitaDoppia.setUser(userContext.getUser());
		scritturaPartitaDoppia.setCd_unita_organizzativa(doccoge.getCd_uo());
		scritturaPartitaDoppia.setCd_cds(doccoge.getCd_cds());
		scritturaPartitaDoppia.setTi_scrittura(Scrittura_partita_doppiaBulk.TIPO_PRIMA_SCRITTURA);
		scritturaPartitaDoppia.setStato(Scrittura_partita_doppiaBulk.STATO_DEFINITIVO);
		scritturaPartitaDoppia.setDs_scrittura(
				"Contabilizzazione: "
						.concat(doccoge.getCd_tipo_doc()).concat(": ")
						.concat(doccoge.getCd_cds()).concat("/")
						.concat(doccoge.getCd_uo()).concat("/")
						.concat(String.valueOf(doccoge.getEsercizio())).concat("/")
						.concat(String.valueOf(doccoge.getPg_doc()))
		);

		scritturaPartitaDoppia.setEsercizio(doccoge.getEsercizio());
		scritturaPartitaDoppia.setEsercizio_documento_amm(doccoge.getEsercizio());
		scritturaPartitaDoppia.setCd_cds_documento(doccoge.getCd_cds());
		scritturaPartitaDoppia.setCd_uo_documento(doccoge.getCd_uo());
		scritturaPartitaDoppia.setCd_tipo_documento(doccoge.getCd_tipo_doc());
		scritturaPartitaDoppia.setPg_numero_documento(doccoge.getPg_doc());
		scritturaPartitaDoppia.setDt_inizio_liquid(doccoge.getDtInizioLiquid());
		scritturaPartitaDoppia.setDt_fine_liquid(doccoge.getDtFineLiquid());
		scritturaPartitaDoppia.setTipo_liquidazione(doccoge.getTipoLiquid());
		scritturaPartitaDoppia.setReport_id_liquid(doccoge.getReportIdLiquid());
		scritturaPartitaDoppia.setAttiva(Scrittura_partita_doppiaBulk.ATTIVA_YES);

		testataPrimaNota.forEach(testata -> {
			if (accorpaConti) {
				//Prima analizzo i conti patrimoniali con partita senza cori
				//I conti patrimoniali devono essere accorpati per partita e distinti tra modificabili e non
				Map<IDocumentoCogeBulk, Map<Integer, Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>>>> mapPartitePatrimonialiNoCori =
						testata.getDett().stream().filter(DettaglioPrimaNota::isDettaglioPatrimoniale)
								.filter(el -> Optional.ofNullable(el.getPartita()).isPresent())
								.filter(el -> !Optional.ofNullable(el.getCdCori()).isPresent())
								.collect(Collectors.groupingBy(DettaglioPrimaNota::getPartita,
										Collectors.groupingBy(DettaglioPrimaNota::getCdTerzo,
												Collectors.groupingBy(DettaglioPrimaNota::getTipoDett,
														Collectors.groupingBy(DettaglioPrimaNota::isModificabile,
																Collectors.groupingBy(DettaglioPrimaNota::getCdConto))))));

				mapPartitePatrimonialiNoCori.keySet().forEach(aPartita -> {
					Map<Integer, Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>>> mapCdTerzo = mapPartitePatrimonialiNoCori.get(aPartita);
					mapCdTerzo.keySet().forEach(aCdTerzo -> {
						Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>> mapTipoDettPatrimoniali = mapCdTerzo.get(aCdTerzo);
						mapTipoDettPatrimoniali.keySet().forEach(aTipoDett -> {
							Map<Boolean, Map<String, List<DettaglioPrimaNota>>> mapModificabile = mapTipoDettPatrimoniali.get(aTipoDett);
							mapModificabile.keySet().forEach(aTipoModific -> {
								Map<String, List<DettaglioPrimaNota>> mapContiPatrimoniali = mapModificabile.get(aTipoModific);
								mapContiPatrimoniali.keySet().forEach(aContoPatrimoniale -> {
									try {
										Pair<String, BigDecimal> saldoPatrimoniale = this.getSaldo(mapContiPatrimoniali.get(aContoPatrimoniale));
										Movimento_cogeBulk movcoge = addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, saldoPatrimoniale.getFirst(), aContoPatrimoniale, saldoPatrimoniale.getSecond(), aPartita, aCdTerzo, null);
										Optional.ofNullable(movcoge).ifPresent(el -> el.setFl_modificabile(aTipoModific));
									} catch (ComponentException e) {
										throw new ApplicationRuntimeException(e);
									}
								});
							});
						});
					});
				});

				//Poi i conti patrimoniali con partita e cori distinti sempre tra modificabili e non
				Map<IDocumentoCogeBulk, Map<Integer, Map<String, Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>>>>> mapPartitePatrimonialiCori =
						testata.getDett().stream().filter(DettaglioPrimaNota::isDettaglioPatrimoniale)
								.filter(el -> Optional.ofNullable(el.getPartita()).isPresent())
								.filter(el -> Optional.ofNullable(el.getCdCori()).isPresent())
								.collect(Collectors.groupingBy(DettaglioPrimaNota::getPartita,
										Collectors.groupingBy(DettaglioPrimaNota::getCdTerzo,
												Collectors.groupingBy(DettaglioPrimaNota::getCdCori,
														Collectors.groupingBy(DettaglioPrimaNota::getTipoDett,
																Collectors.groupingBy(DettaglioPrimaNota::isModificabile,
																		Collectors.groupingBy(DettaglioPrimaNota::getCdConto)))))));

				mapPartitePatrimonialiCori.keySet().forEach(aPartita -> {
					Map<Integer, Map<String, Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>>>> mapCdTerzo = mapPartitePatrimonialiCori.get(aPartita);
					mapCdTerzo.keySet().forEach(aCdTerzo -> {
						Map<String, Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>>> mapCoriPatrimoniali = mapCdTerzo.get(aCdTerzo);
						mapCoriPatrimoniali.keySet().forEach(aCdCori -> {
							Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>> mapTipoDettPatrimoniali = mapCoriPatrimoniali.get(aCdCori);
							mapTipoDettPatrimoniali.keySet().forEach(aTipoDett -> {
								Map<Boolean, Map<String, List<DettaglioPrimaNota>>> mapModificabile = mapTipoDettPatrimoniali.get(aTipoDett);
								mapModificabile.keySet().forEach(aTipoModific -> {
									Map<String, List<DettaglioPrimaNota>> mapContiPatrimoniali = mapModificabile.get(aTipoModific);
									mapContiPatrimoniali.keySet().forEach(aContoPatrimoniale -> {
										try {
											Pair<String, BigDecimal> saldoPatrimoniale = this.getSaldo(mapContiPatrimoniali.get(aContoPatrimoniale));
											Movimento_cogeBulk movcoge = addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, saldoPatrimoniale.getFirst(), aContoPatrimoniale, saldoPatrimoniale.getSecond(), aPartita, aCdTerzo, aCdCori);
											Optional.ofNullable(movcoge).ifPresent(el -> el.setFl_modificabile(aTipoModific));
										} catch (ComponentException e) {
											throw new ApplicationRuntimeException(e);
										}
									});
								});
							});
						});
					});
				});

				//Poi analizzo i conti patrimoniali senza partita
				{
					Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>> mapTipoDettPatrimonialiSenzaPartita = testata.getDett().stream().filter(DettaglioPrimaNota::isDettaglioPatrimoniale)
							.filter(el -> !Optional.ofNullable(el.getPartita()).isPresent())
							.collect(Collectors.groupingBy(DettaglioPrimaNota::getTipoDett,
									Collectors.groupingBy(DettaglioPrimaNota::isModificabile,
											Collectors.groupingBy(DettaglioPrimaNota::getCdConto))));

					mapTipoDettPatrimonialiSenzaPartita.keySet().forEach(aTipoDett -> {
						Map<Boolean, Map<String, List<DettaglioPrimaNota>>> mapModificabile = mapTipoDettPatrimonialiSenzaPartita.get(aTipoDett);
						mapModificabile.keySet().forEach(aTipoModific -> {
							Map<String, List<DettaglioPrimaNota>> mapContiPatrimonialiSenzaPartita = mapModificabile.get(aTipoModific);
							mapContiPatrimonialiSenzaPartita.keySet().forEach(aContoPatrimoniale -> {
								try {
									Pair<String, BigDecimal> saldoPatrimoniale = this.getSaldo(mapContiPatrimonialiSenzaPartita.get(aContoPatrimoniale));
									Movimento_cogeBulk movcoge = addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, saldoPatrimoniale.getFirst(), aContoPatrimoniale, saldoPatrimoniale.getSecond());
									Optional.ofNullable(movcoge).ifPresent(el -> el.setFl_modificabile(aTipoModific));
								} catch (ComponentException e) {
									throw new ApplicationRuntimeException(e);
								}
							});
						});
					});
				}

				//Poi analizzo i conti IVA
				{
					Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>> mapTipoDettIva = testata.getDett().stream().filter(DettaglioPrimaNota::isDettaglioIva)
							.filter(el -> !Optional.ofNullable(el.getPartita()).isPresent())
							.collect(Collectors.groupingBy(DettaglioPrimaNota::getTipoDett,
									Collectors.groupingBy(DettaglioPrimaNota::isModificabile,
											Collectors.groupingBy(DettaglioPrimaNota::getCdConto))));

					mapTipoDettIva.keySet().forEach(aTipoDett -> {
						Map<Boolean, Map<String, List<DettaglioPrimaNota>>> mapModificabile = mapTipoDettIva.get(aTipoDett);
						mapModificabile.keySet().forEach(aTipoModific -> {
							Map<String, List<DettaglioPrimaNota>> mapContiIva = mapModificabile.get(aTipoModific);
							mapContiIva.keySet().forEach(aContoIva -> {
								try {
									Pair<String, BigDecimal> saldoIva = this.getSaldo(mapContiIva.get(aContoIva));
									Movimento_cogeBulk movcoge = addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, saldoIva.getFirst(), aContoIva, saldoIva.getSecond());
									Optional.ofNullable(movcoge).ifPresent(el -> el.setFl_modificabile(aTipoModific));
								} catch (ComponentException e) {
									throw new ApplicationRuntimeException(e);
								}
							});
						});
					});
				}

				//Poi analizzo i conti COSTO/RICAVO distinguendoli tra modificabili e non
				{
					Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>> mapTipoDettCostoRicavo = testata.getDett().stream()
							.filter(DettaglioPrimaNota::isDettaglioCostoRicavo)
							.collect(Collectors.groupingBy(DettaglioPrimaNota::getTipoDett,
									Collectors.groupingBy(DettaglioPrimaNota::isModificabile,
											Collectors.groupingBy(DettaglioPrimaNota::getCdConto))));

					mapTipoDettCostoRicavo.keySet().forEach(aTipoDett -> {
						Map<Boolean, Map<String, List<DettaglioPrimaNota>>> mapModificabile = mapTipoDettCostoRicavo.get(aTipoDett);
						mapModificabile.keySet().forEach(aTipoModific -> {
							Map<String, List<DettaglioPrimaNota>> mapContiCostoRicavo = mapModificabile.get(aTipoModific);
							mapContiCostoRicavo.keySet().forEach(aContoCostoRicavo -> {
								try {
									Pair<String, BigDecimal> saldoCostoRicavo = this.getSaldo(mapContiCostoRicavo.get(aContoCostoRicavo));
									Movimento_cogeBulk movcoge = addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, saldoCostoRicavo.getFirst(), aContoCostoRicavo, saldoCostoRicavo.getSecond());
									Optional.ofNullable(movcoge).ifPresent(el -> el.setFl_modificabile(aTipoModific));
								} catch (ComponentException e) {
									throw new ApplicationRuntimeException(e);
								}
							});
						});
					});
				}

				//Poi analizzo tutti gli altri tipi di conti
				{
					Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>> mapTipoDettAltro = testata.getDett().stream()
							.filter(el -> !el.isDettaglioPatrimoniale())
							.filter(el -> !el.isDettaglioIva())
							.filter(el -> !el.isDettaglioCostoRicavo())
							.collect(Collectors.groupingBy(DettaglioPrimaNota::getTipoDett,
									Collectors.groupingBy(DettaglioPrimaNota::isModificabile,
											Collectors.groupingBy(DettaglioPrimaNota::getCdConto))));

					mapTipoDettAltro.keySet().forEach(aTipoDett -> {
						Map<Boolean, Map<String, List<DettaglioPrimaNota>>> mapModificabile = mapTipoDettAltro.get(aTipoDett);
						mapModificabile.keySet().forEach(aTipoModific -> {
							Map<String, List<DettaglioPrimaNota>> mapContiAltro = mapModificabile.get(aTipoModific);
							mapContiAltro.keySet().forEach(aContoAltro -> {
								try {
									Pair<String, BigDecimal> saldoCostoRicavo = this.getSaldo(mapContiAltro.get(aContoAltro));
									Movimento_cogeBulk movcoge = addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, saldoCostoRicavo.getFirst(), aContoAltro, saldoCostoRicavo.getSecond());
									Optional.ofNullable(movcoge).ifPresent(el -> el.setFl_modificabile(aTipoModific));
								} catch (ComponentException e) {
									throw new ApplicationRuntimeException(e);
								}
							});
						});
					});
				}
			} else {
				//Prima analizzo i conti con partita senza cori
				//I conti devono essere accorpati per partita
				{
					Map<IDocumentoCogeBulk, Map<Integer, Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>>>> mapPartite = testata.getDett().stream()
							.filter(el -> Optional.ofNullable(el.getPartita()).isPresent())
							.filter(el -> !Optional.ofNullable(el.getCdCori()).isPresent())
							.collect(Collectors.groupingBy(DettaglioPrimaNota::getPartita,
									Collectors.groupingBy(DettaglioPrimaNota::getCdTerzo,
											Collectors.groupingBy(DettaglioPrimaNota::getTipoDett,
													Collectors.groupingBy(DettaglioPrimaNota::isModificabile,
															Collectors.groupingBy(DettaglioPrimaNota::getCdConto))))));

					mapPartite.keySet().forEach(aPartita -> {
						Map<Integer, Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>>> mapCdTerzo = mapPartite.get(aPartita);
						mapCdTerzo.keySet().forEach(aCdTerzo -> {
							Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>> mapTipoDett = mapCdTerzo.get(aCdTerzo);
							mapTipoDett.keySet().forEach(aTipoDett -> {
								Map<Boolean, Map<String, List<DettaglioPrimaNota>>> mapModificabile = mapTipoDett.get(aTipoDett);
								mapModificabile.keySet().forEach(aTipoModific -> {
									Map<String, List<DettaglioPrimaNota>> mapConti = mapModificabile.get(aTipoModific);
									mapConti.keySet().forEach(aConto -> {
										try {
											BigDecimal imDare = mapConti.get(aConto).stream()
													.filter(DettaglioPrimaNota::isSezioneDare)
													.map(DettaglioPrimaNota::getImporto).reduce(BigDecimal.ZERO, BigDecimal::add);
											Optional.ofNullable(addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, Movimento_cogeBulk.SEZIONE_DARE, aConto, imDare, aPartita, aCdTerzo, null))
													.ifPresent(el -> el.setFl_modificabile(aTipoModific));

											BigDecimal imAvere = mapConti.get(aConto).stream()
													.filter(DettaglioPrimaNota::isSezioneAvere)
													.map(DettaglioPrimaNota::getImporto).reduce(BigDecimal.ZERO, BigDecimal::add);
											Optional.ofNullable(addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, Movimento_cogeBulk.SEZIONE_AVERE, aConto, imAvere, aPartita, aCdTerzo, null))
													.ifPresent(el -> el.setFl_modificabile(aTipoModific));
										} catch (ComponentException e) {
											throw new ApplicationRuntimeException(e);
										}
									});
								});
							});
						});
					});
				}

				//Poi analizzo i conti con partita e cori
				//I conti devono essere accorpati per partita
				{
					Map<IDocumentoCogeBulk, Map<Integer, Map<String, Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>>>>> mapPartite = testata.getDett().stream()
							.filter(el -> Optional.ofNullable(el.getPartita()).isPresent())
							.filter(el -> Optional.ofNullable(el.getCdCori()).isPresent())
							.collect(Collectors.groupingBy(DettaglioPrimaNota::getPartita,
									Collectors.groupingBy(DettaglioPrimaNota::getCdTerzo,
											Collectors.groupingBy(DettaglioPrimaNota::getCdCori,
													Collectors.groupingBy(DettaglioPrimaNota::getTipoDett,
															Collectors.groupingBy(DettaglioPrimaNota::isModificabile,
																	Collectors.groupingBy(DettaglioPrimaNota::getCdConto)))))));

					mapPartite.keySet().forEach(aPartita -> {
						Map<Integer, Map<String, Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>>>> mapCdTerzo = mapPartite.get(aPartita);
						mapCdTerzo.keySet().forEach(aCdTerzo -> {
							Map<String, Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>>> mapPartitePatrimonialiCori = mapCdTerzo.get(aCdTerzo);
							mapPartitePatrimonialiCori.keySet().forEach(aCdCori -> {
								Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>> mapTipoDett = mapPartitePatrimonialiCori.get(aCdCori);
								mapTipoDett.keySet().forEach(aTipoDett -> {
									Map<Boolean, Map<String, List<DettaglioPrimaNota>>> mapModificabile = mapTipoDett.get(aTipoDett);
									mapModificabile.keySet().forEach(aTipoModific -> {
										Map<String, List<DettaglioPrimaNota>> mapConti = mapModificabile.get(aTipoModific);
										mapConti.keySet().forEach(aConto -> {
											try {
												BigDecimal imDare = mapConti.get(aConto).stream()
														.filter(DettaglioPrimaNota::isSezioneDare)
														.map(DettaglioPrimaNota::getImporto).reduce(BigDecimal.ZERO, BigDecimal::add);
												Optional.ofNullable(addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, Movimento_cogeBulk.SEZIONE_DARE, aConto, imDare, aPartita, aCdTerzo, aCdCori))
														.ifPresent(el -> el.setFl_modificabile(aTipoModific));

												BigDecimal imAvere = mapConti.get(aConto).stream()
														.filter(DettaglioPrimaNota::isSezioneAvere)
														.map(DettaglioPrimaNota::getImporto).reduce(BigDecimal.ZERO, BigDecimal::add);
												Optional.ofNullable(addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, Movimento_cogeBulk.SEZIONE_AVERE, aConto, imAvere, aPartita, aCdTerzo, aCdCori))
														.ifPresent(el -> el.setFl_modificabile(aTipoModific));
											} catch (ComponentException e) {
												throw new ApplicationRuntimeException(e);
											}
										});
									});
								});
							});
						});
					});
				}

				//Poi analizzo i conti senza partita
				{
					Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>> mapTipoDett = testata.getDett().stream()
							.filter(el -> !Optional.ofNullable(el.getPartita()).isPresent())
							.collect(Collectors.groupingBy(DettaglioPrimaNota::getTipoDett,
									Collectors.groupingBy(DettaglioPrimaNota::isModificabile, Collectors.groupingBy(DettaglioPrimaNota::getCdConto))));

					mapTipoDett.keySet().forEach(aTipoDett -> {
						Map<Boolean, Map<String, List<DettaglioPrimaNota>>> mapModificabile = mapTipoDett.get(aTipoDett);
						mapModificabile.keySet().forEach(aTipoModific -> {
							Map<String, List<DettaglioPrimaNota>> mapConti = mapModificabile.get(aTipoModific);
							mapConti.keySet().forEach(aConto -> {
								try {
									BigDecimal imDare = mapConti.get(aConto).stream()
											.filter(DettaglioPrimaNota::isSezioneDare)
											.map(DettaglioPrimaNota::getImporto).reduce(BigDecimal.ZERO, BigDecimal::add);
									Optional.ofNullable(addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, Movimento_cogeBulk.SEZIONE_DARE, aConto, imDare))
											.ifPresent(el -> el.setFl_modificabile(aTipoModific));

									BigDecimal imAvere = mapConti.get(aConto).stream()
											.filter(DettaglioPrimaNota::isSezioneAvere)
											.map(DettaglioPrimaNota::getImporto).reduce(BigDecimal.ZERO, BigDecimal::add);
									Optional.ofNullable(addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, Movimento_cogeBulk.SEZIONE_AVERE, aConto, imAvere))
											.ifPresent(el -> el.setFl_modificabile(aTipoModific));
								} catch (ComponentException e) {
									throw new ApplicationRuntimeException(e);
								}
							});
						});
					});
				}
			}
		});
		scritturaPartitaDoppia.setIm_scrittura(scritturaPartitaDoppia.getImTotaleAvere());
		return scritturaPartitaDoppia;
	}

	private Movimento_cogeBulk addMovimentoCoge(UserContext userContext, Scrittura_partita_doppiaBulk scritturaPartitaDoppia, IDocumentoCogeBulk doccoge, TestataPrimaNota testata, String aTipoDett, String aSezione, String aCdConto, BigDecimal aImporto) throws ComponentException {
		return this.addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, aSezione, aCdConto, aImporto, null, null, null);
	}

	private Movimento_cogeBulk addMovimentoCoge(UserContext userContext, Scrittura_partita_doppiaBulk scritturaPartitaDoppia, IDocumentoCogeBulk doccoge, TestataPrimaNota testata, String aTipoDett, String aSezione, String aCdConto, BigDecimal aImporto, IDocumentoCogeBulk aPartita, Integer aCdTerzo, String aCdCori) throws ComponentException{
		try {
			if (aImporto.compareTo(BigDecimal.ZERO)==0)
				return null;

			Movimento_cogeBulk movimentoCoge = new Movimento_cogeBulk();

			ContoHome contoHome = (ContoHome) getHome(userContext, ContoBulk.class);
			ContoBulk contoBulk = (ContoBulk)contoHome.findByPrimaryKey(new ContoBulk(aCdConto, CNRUserContext.getEsercizio(userContext)));
			movimentoCoge.setToBeCreated();
			movimentoCoge.setUser(userContext.getUser());

			movimentoCoge.setConto(contoBulk);
			movimentoCoge.setIm_movimento(aImporto);
			movimentoCoge.setTerzo(
					Optional.ofNullable(aCdTerzo)
							.map(cdTerzo -> {
								try {
									return findByPrimaryKey(userContext, new TerzoBulk(cdTerzo));
								} catch (ComponentException e) {
									return handleException(e);
								}
							})
							.filter(TerzoBulk.class::isInstance)
							.map(TerzoBulk.class::cast)
							.orElse(null)
			);
			movimentoCoge.setDt_da_competenza_coge(testata.getDtDa());
			movimentoCoge.setDt_a_competenza_coge(testata.getDtA());
			movimentoCoge.setStato(Movimento_cogeBulk.STATO_DEFINITIVO);
			movimentoCoge.setCd_contributo_ritenuta(aCdCori);
			movimentoCoge.setFl_modificabile(Boolean.FALSE);

			if (doccoge instanceof Fattura_passivaBulk) {
				Fattura_passivaBulk fatpas = (Fattura_passivaBulk) doccoge;

				movimentoCoge.setCd_cds(fatpas.getCd_cds_origine());
				movimentoCoge.setEsercizio(fatpas.getEsercizio());
				movimentoCoge.setCd_unita_organizzativa(fatpas.getCd_uo_origine());
				movimentoCoge.setTi_istituz_commerc(fatpas.getTi_istituz_commerc());
			} else if (doccoge instanceof Fattura_attivaBulk) {
				Fattura_attivaBulk fatatt = (Fattura_attivaBulk) doccoge;

				movimentoCoge.setCd_cds(fatatt.getCd_cds_origine());
				movimentoCoge.setEsercizio(fatatt.getEsercizio());
				movimentoCoge.setCd_unita_organizzativa(fatatt.getCd_uo_origine());
				movimentoCoge.setTi_istituz_commerc(TipoIVA.ISTITUZIONALE.value());
			} else if (doccoge instanceof Documento_genericoBulk) {
				Documento_genericoBulk documento_genericoBulk = (Documento_genericoBulk) doccoge;

				movimentoCoge.setCd_cds(documento_genericoBulk.getCd_cds_origine());
				movimentoCoge.setEsercizio(documento_genericoBulk.getEsercizio());
				movimentoCoge.setCd_unita_organizzativa(documento_genericoBulk.getCd_uo_origine());
				movimentoCoge.setTi_istituz_commerc(documento_genericoBulk.getTi_istituz_commerc());
			} else if (doccoge instanceof CompensoBulk) {
				CompensoBulk compensoBulk = (CompensoBulk) doccoge;

				movimentoCoge.setCd_cds(compensoBulk.getCd_cds_origine());
				movimentoCoge.setEsercizio(compensoBulk.getEsercizio());
				movimentoCoge.setCd_unita_organizzativa(compensoBulk.getCd_uo_origine());
				movimentoCoge.setTi_istituz_commerc(compensoBulk.getTi_istituz_commerc());
			} else if (doccoge instanceof MandatoBulk) {
				MandatoBulk mandatoBulk = (MandatoBulk) doccoge;

				movimentoCoge.setCd_cds(mandatoBulk.getCd_cds_origine());
				movimentoCoge.setEsercizio(mandatoBulk.getEsercizio());
				movimentoCoge.setCd_unita_organizzativa(mandatoBulk.getCd_uo_origine());
				movimentoCoge.setTi_istituz_commerc(TipoIVA.ISTITUZIONALE.value());
			} else if (doccoge instanceof ReversaleBulk) {
				ReversaleBulk reversaleBulk = (ReversaleBulk) doccoge;

				movimentoCoge.setCd_cds(reversaleBulk.getCd_cds_origine());
				movimentoCoge.setEsercizio(reversaleBulk.getEsercizio());
				movimentoCoge.setCd_unita_organizzativa(reversaleBulk.getCd_uo_origine());
				movimentoCoge.setTi_istituz_commerc(TipoIVA.ISTITUZIONALE.value());
			} else if (doccoge instanceof AnticipoBulk) {
				AnticipoBulk anticipo = (AnticipoBulk) doccoge;

				movimentoCoge.setCd_cds(anticipo.getCd_cds_origine());
				movimentoCoge.setEsercizio(anticipo.getEsercizio());
				movimentoCoge.setCd_unita_organizzativa(anticipo.getCd_uo_origine());
				movimentoCoge.setTi_istituz_commerc(TipoIVA.ISTITUZIONALE.value());
			} else if (doccoge instanceof MissioneBulk) {
				movimentoCoge.setTi_istituz_commerc(((MissioneBulk) doccoge).getTi_istituz_commerc());
			} else if (doccoge instanceof Liquidazione_ivaBulk) {
				movimentoCoge.setTi_istituz_commerc(TipoIVA.ISTITUZIONALE.value());
			}

			if (aPartita!=null) {
				movimentoCoge.setCd_tipo_documento(aPartita.getCd_tipo_doc());
				movimentoCoge.setCd_cds_documento(aPartita.getCd_cds());
				movimentoCoge.setCd_uo_documento(aPartita.getCd_uo());
				movimentoCoge.setEsercizio_documento(aPartita.getEsercizio());
				movimentoCoge.setPg_numero_documento(aPartita.getPg_doc());
			}

			movimentoCoge.setTi_riga(aTipoDett);

			if (aSezione.equals(Movimento_cogeBulk.SEZIONE_DARE))
				scritturaPartitaDoppia.addToMovimentiDareColl(movimentoCoge);
			else
				scritturaPartitaDoppia.addToMovimentiAvereColl(movimentoCoge);
/*
			logger.info("TipoRiga: " + aTipoDett + " - Conto: " + aCdConto + " - Sezione: " + aSezione + " - Importo: " + aImporto +
					(aPartita!=null?" - Partita: " +
							aPartita.getCd_tipo_doc() + "/" + aPartita.getCd_cds() + "/" + aPartita.getCd_uo() + "/" + aPartita.getEsercizio() + "/" + aPartita.getPg_doc():""));
*/
			return movimentoCoge;
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	private boolean hasAutofattura(UserContext userContext, IDocumentoAmministrativoBulk docamm) throws ComponentException {
		try {
			final Optional<Fattura_passivaBulk> optionalFattura_passivaBulk = Optional.of(docamm)
					.filter(Fattura_passivaBulk.class::isInstance)
					.map(Fattura_passivaBulk.class::cast);
			if (optionalFattura_passivaBulk.isPresent()) {
				if (!optionalFattura_passivaBulk.get().isCommerciale())
					return false;
				if (!optionalFattura_passivaBulk.get().getFl_autofattura()) {
					AutofatturaHome autofatturaHome = (AutofatturaHome) getHome(userContext, AutofatturaBulk.class);
					AutofatturaBulk autof = autofatturaHome.findFor(optionalFattura_passivaBulk.get());
					return Optional.ofNullable(autof).isPresent();
				}
				return optionalFattura_passivaBulk.get().getFl_autofattura();
			}
			return false;
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	private boolean registraIvaCoge(UserContext usercontext, IDocumentoAmministrativoBulk docamm) {
		if (docamm instanceof Fattura_passivaBulk) {
			Fattura_passivaBulk fatpas = (Fattura_passivaBulk) docamm;
			Optional.of(fatpas.getTipo_sezionale()).filter(el->el.getCrudStatus()==OggettoBulk.UNDEFINED).ifPresent(el->{
				try {
					fatpas.setTipo_sezionale((Tipo_sezionaleBulk) getHome(usercontext, Tipo_sezionaleBulk.class).findByPrimaryKey(fatpas.getTipo_sezionale()));
				} catch (ComponentException | PersistencyException e) {
					throw new DetailedRuntimeException(e);
				}
			});
			return fatpas.registraIvaCoge();
		} else if (docamm instanceof Fattura_attivaBulk) {
			Fattura_attivaBulk fatatt = (Fattura_attivaBulk) docamm;
			return fatatt.registraIvaCoge();
		}
		return false;
	}

	private Pair<String, BigDecimal> getSaldo(List<DettaglioPrimaNota> dettaglioPrimaNotaList) {
		BigDecimal totaleDare = dettaglioPrimaNotaList.stream()
				.filter(DettaglioPrimaNota::isSezioneDare)
				.map(DettaglioPrimaNota::getImporto).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totaleAvere = dettaglioPrimaNotaList.stream()
				.filter(DettaglioPrimaNota::isSezioneAvere)
				.map(DettaglioPrimaNota::getImporto).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal saldo = totaleDare.subtract(totaleAvere);
		if (saldo.compareTo(BigDecimal.ZERO)>=0)
			return Pair.of(Movimento_cogeBulk.SEZIONE_DARE, saldo);
		else
			return Pair.of(Movimento_cogeBulk.SEZIONE_AVERE, saldo.abs());
	}

	private boolean isMandatoVersamentoIva(UserContext userContext, Mandato_rigaBulk mandatoRiga) throws PersistencyException, ComponentException {
		Liquidazione_ivaHome liqHome= (Liquidazione_ivaHome)getHome(userContext, Liquidazione_ivaBulk.class);

		SQLBuilder sql= liqHome.createSQLBuilder();

		sql.addClause(FindClause.AND,"cd_tipo_documento",SQLBuilder.EQUALS, mandatoRiga.getCd_tipo_documento_amm());
		sql.addClause(FindClause.AND,"cd_cds_doc_amm",SQLBuilder.EQUALS, mandatoRiga.getCd_cds_doc_amm());
		sql.addClause(FindClause.AND,"cd_uo_doc_amm",SQLBuilder.EQUALS, mandatoRiga.getCd_uo_doc_amm());
		sql.addClause(FindClause.AND,"esercizio_doc_amm",SQLBuilder.EQUALS, mandatoRiga.getEsercizio());
		sql.addClause(FindClause.AND,"pg_doc_amm",SQLBuilder.EQUALS, mandatoRiga.getPg_doc_amm());
		sql.addClause(FindClause.AND,"stato",SQLBuilder.EQUALS, Liquidazione_ivaVBulk.DEFINITIVO);
		sql.addClause(FindClause.AND,"report_id",SQLBuilder.EQUALS, BigDecimal.ZERO);

		sql.addOrderBy("cd_unita_organizzativa");

		return liqHome.fetchAll(sql).size()>0;
	}

	private List<Contributo_ritenutaBulk> getCORI(UserContext userContext, Mandato_rigaBulk mandatoRiga) throws PersistencyException, ComponentException {
		Documento_generico_rigaHome dgHome= (Documento_generico_rigaHome)getHome(userContext, Documento_generico_rigaBulk.class);
		Ass_obb_acr_pgiroHome assObbAccHome= (Ass_obb_acr_pgiroHome)getHome(userContext, Ass_obb_acr_pgiroBulk.class);
		Contributo_ritenutaHome coriHome= (Contributo_ritenutaHome)getHome(userContext, Contributo_ritenutaBulk.class);

		List<Contributo_ritenutaBulk> coriList = new ArrayList<>();

		Documento_generico_rigaBulk docRiga = (Documento_generico_rigaBulk)dgHome.findByPrimaryKey(new Documento_generico_rigaBulk(mandatoRiga.getEsercizio_doc_amm().toString(),
				mandatoRiga.getCd_tipo_documento_amm(), mandatoRiga.getCd_uo_doc_amm(), mandatoRiga.getEsercizio_doc_amm(), mandatoRiga.getPg_doc_amm(), BigDecimal.ONE.longValue()));

		if (docRiga.getPg_obbligazione()!=null) {
			List<Ass_obb_acr_pgiroBulk> assObbAccList = (List<Ass_obb_acr_pgiroBulk>)assObbAccHome.findPgiroMandatoRiga(userContext, mandatoRiga);

			assObbAccList.forEach(assObbAcc->{
				try {
					SQLBuilder sqlCori= coriHome.createSQLBuilder();
					sqlCori.addClause(FindClause.AND,"cd_cds_accertamento",SQLBuilder.EQUALS,assObbAcc.getCd_cds());
					sqlCori.addClause(FindClause.AND,"esercizio_accertamento",SQLBuilder.EQUALS,assObbAcc.getEsercizio());
					sqlCori.addClause(FindClause.AND,"esercizio_ori_accertamento",SQLBuilder.EQUALS,assObbAcc.getEsercizio_ori_accertamento());
					sqlCori.addClause(FindClause.AND,"pg_accertamento",SQLBuilder.EQUALS,assObbAcc.getPg_accertamento());

					coriList.addAll(coriHome.fetchAll(sqlCori));
				} catch (PersistencyException e) {
					throw new ApplicationRuntimeException(e);
				}
			});
		}
		return coriList;
	}

	private List<Contributo_ritenutaBulk> getCORI(UserContext userContext, Reversale_rigaBulk reversaleRiga) throws PersistencyException, ComponentException {
		Documento_generico_rigaHome dgHome= (Documento_generico_rigaHome)getHome(userContext, Documento_generico_rigaBulk.class);
		Ass_obb_acr_pgiroHome assObbAccHome= (Ass_obb_acr_pgiroHome)getHome(userContext, Ass_obb_acr_pgiroBulk.class);
		Contributo_ritenutaHome coriHome= (Contributo_ritenutaHome)getHome(userContext, Contributo_ritenutaBulk.class);

		List<Contributo_ritenutaBulk> coriList = new ArrayList<>();

		Documento_generico_rigaBulk docRiga = (Documento_generico_rigaBulk)dgHome.findByPrimaryKey(new Documento_generico_rigaBulk(reversaleRiga.getEsercizio_doc_amm().toString(),
				reversaleRiga.getCd_tipo_documento_amm(), reversaleRiga.getCd_uo_doc_amm(), reversaleRiga.getEsercizio_doc_amm(), reversaleRiga.getPg_doc_amm(), BigDecimal.ONE.longValue()));

		if (docRiga.getPg_obbligazione()!=null) {
			List<Ass_obb_acr_pgiroBulk> assObbAccList = (List<Ass_obb_acr_pgiroBulk>)assObbAccHome.findPgiroReversaleriga(userContext, reversaleRiga);

			assObbAccList.forEach(assObbAcc->{
				try {
					SQLBuilder sqlCori= coriHome.createSQLBuilder();
					sqlCori.addClause(FindClause.AND,"cd_cds_obbligazione",SQLBuilder.EQUALS,assObbAcc.getCd_cds());
					sqlCori.addClause(FindClause.AND,"esercizio_obbligazione",SQLBuilder.EQUALS,assObbAcc.getEsercizio());
					sqlCori.addClause(FindClause.AND,"esercizio_ori_obbligazione",SQLBuilder.EQUALS,assObbAcc.getEsercizio_ori_obbligazione());
					sqlCori.addClause(FindClause.AND,"pg_obbligazione",SQLBuilder.EQUALS,assObbAcc.getPg_obbligazione());

					coriList.addAll(coriHome.fetchAll(sqlCori));
				} catch (PersistencyException e) {
					throw new ApplicationRuntimeException(e);
				}
			});
		}
		return coriList;
	}

	private List<IDocumentoAmministrativoRigaBulk> getRigheDocamm(UserContext userContext, IDocumentoAmministrativoBulk docamm) {
		return Optional.ofNullable(docamm.getChildren()).filter(el -> !el.isEmpty()).orElseGet(() -> {
			try {
				List<IDocumentoAmministrativoRigaBulk> result;
				if (docamm instanceof Documento_genericoBulk) {
					Documento_genericoHome home = (Documento_genericoHome) getHome(userContext, Documento_genericoBulk.class);
					result = home.findDocumentoGenericoRigheList((Documento_genericoBulk) docamm);
					((Documento_genericoBulk)docamm).setDocumento_generico_dettColl(new BulkList(result));
				} else if (docamm instanceof Fattura_passivaBulk) {
					result = Utility.createFatturaPassivaComponentSession().findDettagli(userContext, (Fattura_passivaBulk) docamm);
					((Fattura_passivaBulk)docamm).setFattura_passiva_dettColl(new BulkList(result));
				} else if (docamm instanceof Fattura_attivaBulk) {
					result = Utility.createFatturaAttivaSingolaComponentSession().findDettagli(userContext, (Fattura_attivaBulk) docamm);
					((Fattura_attivaBulk) docamm).setFattura_attiva_dettColl(new BulkList(result));
				} else
					throw new ApplicationException("Scrittura Economica non possibile. Non risulta gestito il recupero delle righe di dettaglio di un documento di tipo "+ docamm.getCd_tipo_doc()+".");
				return result;
			} catch (ComponentException | PersistencyException | RemoteException | IntrospectionException e) {
				throw new ApplicationRuntimeException(e);
			}
		});
	}

	private MandatoBulk completeMandato(UserContext userContext, MandatoBulk mandato) throws ComponentException, PersistencyException {
		//Se le righe del mandato non sono valorizzate le riempio io
		if (!Optional.ofNullable(mandato.getMandato_rigaColl()).filter(el->!el.isEmpty()).isPresent()) {
			mandato.setMandato_rigaColl(new BulkList(((MandatoHome) getHome(
					userContext, mandato.getClass())).findMandato_riga(userContext, mandato, false)));
			mandato.getMandato_rigaColl().forEach(el->{
				try {
					el.setMandato(mandato);
					((Mandato_rigaHome)getHome(userContext, Mandato_rigaBulk.class)).initializeElemento_voce(userContext, el);
				} catch (ComponentException|PersistencyException e) {
					throw new ApplicationRuntimeException(e);
				}
			});
		}

		if (!Optional.ofNullable(mandato.getMandato_terzo()).filter(el->el.getCrudStatus()!=OggettoBulk.UNDEFINED).isPresent())
			mandato.setMandato_terzo(((MandatoHome) getHome(userContext, mandato.getClass())).findMandato_terzo(userContext, mandato, false));

		if (!Optional.ofNullable(mandato.getUnita_organizzativa()).filter(el->el.getCrudStatus()!=OggettoBulk.UNDEFINED).isPresent())
			mandato.setUnita_organizzativa((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(mandato.getUnita_organizzativa()));

		return mandato;
	}

	private TestataPrimaNota proposeTestataPrimaNotaReversaleAccantonamentoCori(UserContext userContext, ReversaleBulk reversale) throws ComponentException, PersistencyException, RemoteException {
		String keyReversale = reversale.getEsercizio() + "/" + reversale.getCd_cds() + "/" + reversale.getPg_reversale();
		if (reversale.getReversale_rigaColl().stream().map(Reversale_rigaBulk::getCd_tipo_documento_amm).noneMatch(el->TipoDocumentoEnum.fromValue(el).isGenericoCoriAccantonamentoEntrata()))
			throw new ApplicationException("La riga della reversale " + keyReversale + " non risulta incassare un versamento/accantonamento cori. Proposta di prima nota non possibile.");

		if (reversale.getReversale_rigaColl().size()>1)
			throw new ApplicationException("Reversale " + keyReversale + " di versamento/accantonamento cori con più di una riga. Proposta di prima nota non possibile.");

		Reversale_rigaBulk rigaReversale = reversale.getReversale_rigaColl().stream().findAny()
				.orElseThrow(()->new ApplicationException("Reversale " + keyReversale + " di versamento/accantonamento cori senza righe. Proposta di prima nota non possibile."));

		BigDecimal imReversale = rigaReversale.getIm_reversale_riga();

		Voce_epBulk voceEpBanca = this.findContoBanca(userContext, CNRUserContext.getEsercizio(userContext));
		Voce_epBulk aContoRicavo = this.findContoCostoRicavo(userContext, rigaReversale.getElemento_voce());

		TestataPrimaNota testataPrimaNota = new TestataPrimaNota();
		testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.RICAVO.value(), Movimento_cogeBulk.SEZIONE_AVERE, aContoRicavo.getCd_voce_ep(), imReversale);
		testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), Movimento_cogeBulk.SEZIONE_DARE, voceEpBanca.getCd_voce_ep(), imReversale);

		return testataPrimaNota;
	}

	/**
	 * Ritorna la lista dei conti Iva aperti dal documento amministrativo
	 *
	 * @param userContext userContext
	 * @param docamm - il documento amministrativo
	 * @return Map<String, Map<String, Pair<String, BigDecimal>>> mappa con i saldi raggruppati per CdCori e VoceEp
	 * @throws ComponentException ComponentException
	 * @throws PersistencyException PersistencyException
	 */
	private Map<String, Map<String, Pair<String, BigDecimal>>> getSaldiMovimentiCoriIva(UserContext userContext, IDocumentoCogeBulk docamm) throws ComponentException, PersistencyException {
		Optional<Scrittura_partita_doppiaBulk> scritturaOpt = this.getScritturaPartitaDoppia(userContext, docamm);

		return this.getMapCdCoriVoceEp(scritturaOpt.map(spd->spd.getAllMovimentiColl()
				.stream()
				.filter(Movimento_cogeBulk::isRigaTipoIva)
				.collect(Collectors.toList())).orElse(Collections.EMPTY_LIST));
	}

	/**
	 *
	 * Ritorna la lista dei conti Iva Debito aperti dalla liquidazione UO
	 *
	 * @param userContext userContext
	 * @param liquidIva - la liquidazione della UO
	 * @return Map<String, Map<String, Pair<String, BigDecimal>>> mappa con i saldi raggruppati per VoceEp
	 * @throws ComponentException ComponentException
	 * @throws PersistencyException PersistencyException
	 */
	private Map<String, Pair<String, BigDecimal>> getSaldiMovimentiCoriIvaDebitoEnte(UserContext userContext, Liquidazione_ivaBulk liquidIva) throws ComponentException, PersistencyException {
		Optional<Scrittura_partita_doppiaBulk> scritturaOpt = this.getScritturaPartitaDoppia(userContext, liquidIva, Boolean.TRUE);

		return this.getMapVoceEp(scritturaOpt.map(spd->spd.getAllMovimentiColl()
				.stream()
				.filter(el->el.getTi_riga().equals(Movimento_cogeBulk.TipoRiga.DEBITO.value()))
				.filter(el->!Optional.ofNullable(el.getPartita()).isPresent())
				.collect(Collectors.toList())).orElse(Collections.EMPTY_LIST));
	}

	private Optional<Scrittura_partita_doppiaBulk> getScritturaPartitaDoppia(UserContext userContext, IDocumentoCogeBulk docamm) throws ComponentException, PersistencyException {
		return getScritturaPartitaDoppia(userContext, docamm, Boolean.FALSE);
	}

	private Optional<Scrittura_partita_doppiaBulk> getScritturaPartitaDoppia(UserContext userContext, IDocumentoCogeBulk docamm, boolean createIfNotExist) throws ComponentException, PersistencyException {
		Optional<Scrittura_partita_doppiaBulk> scritturaOpt = Optional.empty();

		boolean isAttivaEconomicaDocamm = ((Configurazione_cnrHome) getHome(userContext, Configurazione_cnrBulk.class)).isAttivaEconomica(docamm.getEsercizio());
		if (isAttivaEconomicaDocamm)
			scritturaOpt = ((Scrittura_partita_doppiaHome) getHome(userContext, Scrittura_partita_doppiaBulk.class)).getScrittura(userContext, docamm, Boolean.FALSE);

		//Lancio la proposta di prima nota se non attivaEconomica (!isAttivaEconomicaDocamm) o, in caso contrario, se non generata ma viene richiesto di farlo (createIfNotExist && !scritturaOpt.isPresent())
		if (!isAttivaEconomicaDocamm || (createIfNotExist && !scritturaOpt.isPresent())) {
			try {
				scritturaOpt = Optional.ofNullable(proposeScritturaPartitaDoppia(userContext, docamm));
			} catch (ScritturaPartitaDoppiaNotRequiredException ignored) {
			} catch (ComponentException e) {
				throw new ApplicationRuntimeException(e);
			}

			//Se viene richiesto di generare la proposta provvedo
			if (isAttivaEconomicaDocamm && scritturaOpt.isPresent())
				creaConBulk(userContext, scritturaOpt.get());
		}

		return scritturaOpt;
	}

	/**
	 * Ritorna una mappa in cui raggruppa i movimenti coge per CodiceContributo e Voce
	 */
	private Map<String, Map<String, Pair<String, BigDecimal>>> getMapCdCoriVoceEp(Collection<Movimento_cogeBulk> allMovimentiCoge) {
		Map<String, Map<String, Pair<String, BigDecimal>>> result = new HashMap<>();

		Map<String, Map<String, List<Movimento_cogeBulk>>> mapCdCoriVoceEp = allMovimentiCoge.stream()
				.collect(Collectors.groupingBy(Movimento_cogeBulk::getCd_contributo_ritenuta,
						Collectors.groupingBy(Movimento_cogeBulk::getCd_voce_ep)));

		mapCdCoriVoceEp.keySet().forEach(cdCori -> {
			Map<String, List<Movimento_cogeBulk>> mapVoceEp = mapCdCoriVoceEp.get(cdCori);
			mapVoceEp.keySet().forEach(cdVoceEp -> {
				List<Movimento_cogeBulk> movimentiList = mapVoceEp.get(cdVoceEp);
				BigDecimal totaleDare = movimentiList.stream()
						.filter(Movimento_cogeBulk::isSezioneDare)
						.map(Movimento_cogeBulk::getIm_movimento).reduce(BigDecimal.ZERO, BigDecimal::add);

				BigDecimal totaleAvere = movimentiList.stream()
						.filter(Movimento_cogeBulk::isSezioneAvere)
						.map(Movimento_cogeBulk::getIm_movimento).reduce(BigDecimal.ZERO, BigDecimal::add);

				BigDecimal saldo = totaleDare.subtract(totaleAvere);
				if (saldo.compareTo(BigDecimal.ZERO) >= 0)
					result.put(cdCori, Collections.singletonMap(cdVoceEp, Pair.of(Movimento_cogeBulk.SEZIONE_DARE, saldo)));
				else
					result.put(cdCori, Collections.singletonMap(cdVoceEp, Pair.of(Movimento_cogeBulk.SEZIONE_AVERE, saldo.abs())));
			});
		});
		return result;
	}

	/**
	 * Ritorna una mappa in cui raggruppa i movimenti coge per Voce
	 */
	private Map<String, Pair<String, BigDecimal>> getMapVoceEp(Collection<Movimento_cogeBulk> allMovimentiCoge) {
		Map<String, Pair<String, BigDecimal>> result = new HashMap<>();

		Map<String, List<Movimento_cogeBulk>> mapVoceEp = allMovimentiCoge.stream()
				.collect(Collectors.groupingBy(Movimento_cogeBulk::getCd_voce_ep));

		mapVoceEp.keySet().forEach(cdVoceEp -> {
			List<Movimento_cogeBulk> movimentiList = mapVoceEp.get(cdVoceEp);
			BigDecimal totaleDare = movimentiList.stream()
					.filter(Movimento_cogeBulk::isSezioneDare)
					.map(Movimento_cogeBulk::getIm_movimento).reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal totaleAvere = movimentiList.stream()
					.filter(Movimento_cogeBulk::isSezioneAvere)
					.map(Movimento_cogeBulk::getIm_movimento).reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal saldo = totaleDare.subtract(totaleAvere);
			if (saldo.compareTo(BigDecimal.ZERO) >= 0)
				result.put(cdVoceEp, Pair.of(Movimento_cogeBulk.SEZIONE_DARE, saldo));
			else
				result.put(cdVoceEp, Pair.of(Movimento_cogeBulk.SEZIONE_AVERE, saldo.abs()));
		});

		return result;
	}

	public List<Contributo_ritenutaBulk> findCoriReversaliAssociate(UserContext userContext, MandatoBulk mandato) throws ComponentException, PersistencyException {
		//cerco di recuperare puntualmente la ritenuta
		List<Contributo_ritenutaBulk> righeCoriReversale = new ArrayList<>();
		List<Ass_mandato_reversaleBulk> result = ((Ass_mandato_reversaleHome) getHome(userContext, Ass_mandato_reversaleBulk.class)).findReversali(userContext, mandato, false);
		for (Ass_mandato_reversaleBulk assManRev : result) {
			ReversaleIHome revHome = (ReversaleIHome) getHome(userContext, ReversaleIBulk.class);
			ReversaleBulk reversale = (ReversaleIBulk) revHome.findByPrimaryKey(new ReversaleIBulk(assManRev.getCd_cds_reversale(), assManRev.getEsercizio_reversale(), assManRev.getPg_reversale()));

			Collection<V_doc_cont_compBulk> result2 = ((V_doc_cont_compHome) getHomeCache(userContext).getHome(V_doc_cont_compBulk.class)).findByDocumento(assManRev.getEsercizio_reversale(), assManRev.getCd_cds_reversale(), assManRev.getPg_reversale(), V_doc_cont_compBulk.TIPO_DOC_CONT_REVERSALE);

			if (!result2.isEmpty()) {
				V_doc_cont_compBulk docContCompBulk = result2.stream().findAny().get();
				final CompensoBulk compensoReversale = new CompensoBulk(docContCompBulk.getCd_cds_compenso(), docContCompBulk.getCd_uo_compenso(), docContCompBulk.getEsercizio_compenso(), docContCompBulk.getPg_compenso());

				String descCompensoReversale = compensoReversale.getEsercizio() + "/" + compensoReversale.getCd_cds() + "/" + compensoReversale.getPg_compenso();

				List<Contributo_ritenutaBulk> righeCoriCompensoReversale = Optional.ofNullable(compensoReversale.getChildren()).orElseGet(() -> {
					try {
						Contributo_ritenutaHome home = (Contributo_ritenutaHome) getHome(userContext, Contributo_ritenutaBulk.class);
						return (java.util.List<Contributo_ritenutaBulk>) home.loadContributiRitenute(compensoReversale);
					} catch (ComponentException | PersistencyException e) {
						throw new DetailedRuntimeException(e);
					}
				});

				//Cerco tra le righeCori quella della reversale.... cerco per importo... e se ne esistono di più per descrizione..... se non si individua lancio eccezione
				List<Contributo_ritenutaBulk> righeCoriReversaleByImporto = righeCoriCompensoReversale.stream()
						.filter(el -> el.getAmmontare().compareTo(BigDecimal.ZERO) > 0)
						.filter(el -> el.getAmmontare().abs().compareTo(reversale.getIm_reversale()) == 0)
						.collect(Collectors.toList());

				if (righeCoriReversaleByImporto.isEmpty())
					throw new ApplicationException("Il " + this.getDescrizione(mandato) + " risulta avere una ritenuta di importo che non corrisponde a nessuna delle ritenute " +
							" generate dal compenso associato alla reversale " + this.getDescrizione(compensoReversale) + ". Proposta di prima nota non possibile.");
				if (righeCoriReversaleByImporto.size() > 1) {
					List<Contributo_ritenutaBulk> righeCoriReversaleByDesc = righeCoriReversaleByImporto.stream()
							.filter(el -> reversale.getDs_reversale().contains(el.getCd_contributo_ritenuta()))
							.collect(Collectors.toList());

					if (righeCoriReversaleByDesc.size() != 1)
						throw new ApplicationException("Il " + this.getDescrizione(mandato) + " risulta avere una ritenuta di importo che corrisponde a più ritenute " +
								" generate dal conguaglio associato " + this.getDescrizione(compensoReversale) + ". Proposta di prima nota non possibile.");
					righeCoriReversale.add(righeCoriReversaleByDesc.get(0));
				} else
					righeCoriReversale.add(righeCoriReversaleByImporto.get(0));
			}
		}
		return righeCoriReversale;
	}

	private String getDescrizione(MandatoBulk mandato) {
		return mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato();
	}

	private String getDescrizione(CompensoBulk compenso){
		return compenso.getEsercizio() + "/" + compenso.getCd_cds() + "/" + compenso.getPg_compenso();
	}

}
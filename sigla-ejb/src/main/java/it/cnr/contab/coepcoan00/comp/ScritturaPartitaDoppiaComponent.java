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
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBulk;
import it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaHome;
import it.cnr.contab.compensi00.tabrif.bulk.Ass_tipo_cori_voce_epBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Ass_tipo_cori_voce_epHome;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.pdcep.bulk.*;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_coriBulk;
import it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_coriHome;
import it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_cori_detBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaHome;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaVBulk;
import it.cnr.contab.missioni00.docs.bulk.*;
import it.cnr.contab.ordmag.ordini.bulk.FatturaOrdineBulk;
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
import it.cnr.jada.util.action.SelezionatoreSearchBP;
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

		private final IDocumentoAmministrativoBulk docamm;
		private final Integer cdTerzo;

		private final Elemento_voceBulk elementoVoce;

		private final java.sql.Timestamp dtDaCompetenzaCoge;

		private final java.sql.Timestamp dtACompetenzaCoge;

		private final BigDecimal imImponibile;

		private final BigDecimal imImposta;

		public IDocumentoAmministrativoBulk getDocamm() {
			return docamm;
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

	private static class TestataPrimaNota {
		public TestataPrimaNota(Integer cdTerzo, Timestamp dtDa, Timestamp dtA) {
			super();
			this.cdTerzo = cdTerzo;
			this.dtDa = dtDa;
			this.dtA = dtA;
		}

		private Integer cdTerzo;
		private Timestamp dtDa;
		private Timestamp dtA;
		private List<DettaglioPrimaNota> dett = new ArrayList<>();

		public Integer getCdTerzo() {
			return cdTerzo;
		}

		public void setCdTerzo(Integer cdTerzo) {
			this.cdTerzo = cdTerzo;
		}

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

		public void openDettaglioIva(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo) {
			this.addDettaglioIva(docamm, cdConto, importo, true);
		}

		public void closeDettaglioIva(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo) {
			this.addDettaglioIva(docamm, cdConto, importo, false);
		}

		private void addDettaglioIva(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, boolean isOpen) {
			String tipoDettaglio = null;
			if (docamm.getTipoDocumentoEnum().isDocumentoAmministrativoAttivo()) {
				tipoDettaglio = Movimento_cogeBulk.TipoRiga.IVA_VENDITE.value();
			} else if (docamm.getTipoDocumentoEnum().isDocumentoAmministrativoPassivo()) {
				if (((Fattura_passivaBulk)docamm).getFl_split_payment())
					tipoDettaglio = Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO_SPLIT.value();
				else
					tipoDettaglio = Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO.value();
			}
			if (isOpen) {
				this.addDettaglio(tipoDettaglio, docamm.getTipoDocumentoEnum().getSezioneIva(), cdConto, importo);
				return;
			}
			this.addDettaglio(tipoDettaglio, Movimento_cogeBulk.getControSezione(docamm.getTipoDocumentoEnum().getSezioneIva()), cdConto, importo);
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

		public void openDettaglioPatrimonialeCori(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, Integer cdTerzo, String cdCori) {
			openDettaglioPatrimoniale(docamm, cdConto, importo, true, cdTerzo, cdCori);
		}

		public void openDettaglioPatrimoniale(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, boolean registraPartita, Integer cdTerzo, String cdCori) {
			DettaglioPrimaNota dettPN = this.addDettaglioPatrimoniale(docamm, cdConto, importo, registraPartita, cdTerzo, cdCori, true);
			dettPN.setModificabile(Boolean.TRUE);
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

		public void closeDettaglioPatrimonialeCori(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, Integer cdTerzo, String cdCori) {
			this.closeDettaglioPatrimoniale(docamm, cdConto, importo, true, cdTerzo, cdCori, false);
		}

		public void closeDettaglioPatrimoniale(IDocumentoCogeBulk docamm, String cdConto, BigDecimal importo, boolean registraPartita, Integer cdTerzo, String cdCori, boolean isModificabile) {
			DettaglioPrimaNota dettPN = this.addDettaglioPatrimoniale(docamm, cdConto, importo, registraPartita, cdTerzo, cdCori,false);
			dettPN.setModificabile(isModificabile);
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

		java.sql.Timestamp dt_contabilizzazione;

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
	 * Pre:  E' stata richiesto l'aggiornamento dei saldi coge come conseguenza di un inserimento di
	 *       una nuova scrittura in partita doppia
	 * Post: E' stata richiamata la stored procedure che esegue l'aggiornamento dei saldi coge relativi ai
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
	 * Pre:  E' stata richiesta la ricerca di un terzo e l'utente ha specificato come codice
	 *       il valore '0' ad indicare tutte le classificazioni anagrafiche
	 * Post: Viene restituito un RemoteIterator contenente solamente l'oggetto fittizio ( con codice '0' ) che rappresenta
	 *       tutte le classificazioni anagrafiche
	 *
	 * Nome: Ricerca dell'attributo relativo al terzo con codice '0'
	 * Pre:  E' stata richiesta la ricerca di un terzo e l'utente ha specificato come codice
	 *       un valore diverso da '0'
	 * Post: Viene restituito un RemoteIterator contenente la lista di oggetti di tipo TerzoBulk
	 *       risultante dall'esecuzione della query sul database
	 *
	 * Nome: Ricerca di un attributo diverso da terzo
	 * Pre:  E' stata richiesta la ricerca di un attributo diverso da 'terzo'
	 * Post: Viene restituito un RemoteIterator contenente la lista degli oggettiBulk
	 *       risultante dall'esecuzione della query sul database
	 *
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
	 *      Non  viene consentita il salvataggio.
	 *
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
	 * Pre:  E' stata richiesta l'inserimento di una scrittura in partita doppia già validata
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
	 * Pre:  E' stata richiesta la cancellazione di una scrittura in partita doppia
	 * Post: E' stata richiamata la stored procedure che esegue lo storno e la riemissione della partita doppia ed
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
	 * Pre:  E' stata richiesta l'inizializzazione per inserimento di una scrittura in partita doppia
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
	 * Pre:  E' stata richiesta l'inizializzazione per modifica di una scrittura in partita doppia
	 * Post: La scrittura viene restituita con inizializzata la collezione di movimenti dare e movimenti avere
	 *
	 * Nome: Inizializzazione di un movimento
	 * Pre:  E' stata richiesta l'inizializzazione per visualizzazione di un movimento coge
	 * Post: Il movimento viene restituito con l'inizializzazione di default
	 *
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
				if ( scrittura.getCd_terzo().equals( TerzoBulk.TERZO_NULLO))
					scrittura.setTerzo( getTerzoNullo());
				else
					scrittura.setTerzo( (TerzoBulk) getHome( userContext, TerzoBulk.class).findByPrimaryKey( scrittura.getTerzo()));
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
	 *
	 * Nome: Inizializzazione di un movimento
	 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca di un movimento coge
	 * Post: Il movimento viene restituito con inizializzato come Cds quello di scrivania
	 *
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
	 *
	 * Nome: Inizializzazione di un movimento
	 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca libera di un movimento coge
	 * Post: Il movimento viene restituito con inizializzato come Cds quello di scrivania
	 *
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
	 *
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
		for ( Iterator i = clauses.iterator(); i.hasNext(); )
		{
			clause = (SimpleFindClause) i.next();
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
	 *
	 * Nome: Seleziona conto per movimeto avere
	 * Pre:  E' stata richiesta la ricerca di un conto economico patrimoniale per un movimento avere
	 * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente ed inoltre le clausole che il conto
	 *       abbia esercizio uguale all'esercizio del movimento coge e il tipo sezione diverso da DARE
	 *
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
	 *
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
	 *
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
	 *
	 * Nome: Nessuna associzione fra anagrafico-conto
	 * Pre:  Per la scrittura in partita doppia e' stato selezionato un terzo le cui caratteristiche anagrafiche non
	 *       sono state messe in relazione con neanche un conto definito per la scrittura (sia in avere che in dare)
	 * Post: Una segnalazione di errore viene restituita all'utente
	 *
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
		List result = ((Ass_anag_voce_epHome) getHome(userContext, Ass_anag_voce_epBulk.class)).findAssociazioniPerScrittura( scrittura );
		if ( result == null || result.size() == 0 )
			throw new ApplicationException( "Non esiste nessuna associazione fra l'anagrafica selezionata e i conti economico-patrimoniali" );
		Ass_anag_voce_epBulk ass = (Ass_anag_voce_epBulk) result.get(0);
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
	 *
	 * Nome: validazione superata
	 * Pre:  La scrittura supera la validazione ( metodo validaScrittura)
	 * Post: La scrittura può essere inserita nel database
	 *
	 * Nome: validazione non superata
	 * Pre:  La scrittura non supera la validazione ( metodo validaScrittura)
	 * Post: Una segnalazione di errore viene restituita all'utente
	 *
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
	 *
	 * Nome: Nessun movimento avere
	 * Pre:  Per la scrittura in partita doppia non e' stato definito nessun movimento avere
	 * Post: Una segnalazione di errore viene restituita all'utente
	 *
	 * Nome: Nessun movimento dare
	 * Pre:  Per la scrittura in partita doppia non e' stato definito nessun movimento dare
	 * Post: Una segnalazione di errore viene restituita all'utente
	 *
	 * Nome: Somma movimenti dare diversa da somma movimenti avere
	 * Pre:  La somma degli importi dei movimenti dare e' diversa dalla somma degli importi dei movimenti avere
	 * Post: Una segnalazione di errore viene restituita all'utente
	 *
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
			if (!doccoge.getTipoDocumentoEnum().isScritturaEconomicaRequired())
				throw new ScritturaPartitaDoppiaNotRequiredException("Scrittura Economica non prevista per la tipologia di documento selezionato.");
			else if (doccoge.getTipoDocumentoEnum().isDocumentoAmministrativoPassivo() || doccoge.getTipoDocumentoEnum().isDocumentoAmministrativoAttivo())
				return this.proposeScritturaPartitaDoppiaDocumento(userContext, (IDocumentoAmministrativoBulk) doccoge);
			else if (doccoge.getTipoDocumentoEnum().isAnticipo())
				return this.proposeScritturaPartitaDoppiaAnticipo(userContext, (AnticipoBulk) doccoge);
			else if (doccoge.getTipoDocumentoEnum().isCompenso()) {
				if (((CompensoBulk) doccoge).getFl_compenso_stipendi())
					return this.proposeScritturaPartitaDoppiaCompensoStipendi(userContext, (CompensoBulk) doccoge);
				else if (((CompensoBulk) doccoge).getFl_compenso_conguaglio())
					return this.proposeScritturaPartitaDoppiaCompensoConguagli(userContext, (CompensoBulk) doccoge);
				else
					return this.proposeScritturaPartitaDoppiaCompenso(userContext, (CompensoBulk) doccoge);
			} else if (doccoge.getTipoDocumentoEnum().isMissione())
				return this.proposeScritturaPartitaDoppiaMissione(userContext, (MissioneBulk) doccoge);
			else if (doccoge.getTipoDocumentoEnum().isRimborso())
				return this.proposeScritturaPartitaDoppiaRimborso(userContext, (RimborsoBulk) doccoge);
			else if (doccoge.getTipoDocumentoEnum().isMandato())
				return this.proposeScritturaPartitaDoppiaMandato(userContext, (MandatoBulk) doccoge);
			else if (doccoge.getTipoDocumentoEnum().isReversale())
				return this.proposeScritturaPartitaDoppiaReversale(userContext, (ReversaleBulk) doccoge);
			throw new ApplicationException("Scrittura Economica non gestita per la tipologia di documento selezionato.");
		} catch (ApplicationException|ApplicationRuntimeException e) {
			throw new NoRollbackException(e);
		}
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaDocumento(UserContext userContext, IDocumentoAmministrativoBulk docamm) throws ComponentException, ScritturaPartitaDoppiaNotRequiredException {
		List<TestataPrimaNota> testataPrimaNotaList = this.proposeTestataPrimaNotaDocumento(userContext, docamm);
		return Optional.ofNullable(testataPrimaNotaList).map(el->this.generaScrittura(userContext, docamm, el, true)).orElse(null);
	}

	private List<TestataPrimaNota> proposeTestataPrimaNotaDocumento(UserContext userContext, IDocumentoAmministrativoBulk docamm) throws ComponentException, ScritturaPartitaDoppiaNotRequiredException {
		List<TestataPrimaNota> testataPrimaNotaList = new ArrayList<>();
		final boolean isFatturaPassivaDaOrdini = Optional.of(docamm).filter(Fattura_passivaBulk.class::isInstance).map(Fattura_passivaBulk.class::cast).filter(Fattura_passivaBulk::isDaOrdini).isPresent();

		//Le fatture generate da compenso non creano scritture di prima nota in quanto create direttamente dal compenso stesso
		if (Optional.of(docamm).filter(Fattura_passivaBulk.class::isInstance).map(Fattura_passivaBulk.class::cast).map(Fattura_passivaBulk::isGenerataDaCompenso).orElse(Boolean.FALSE))
			throw new ScritturaPartitaDoppiaNotRequiredException("Scrittura Economica non necessaria in quanto fattura generata da compenso. La scrittura di prima nota viene creata direttamente dal compenso stesso");

		final boolean isCommercialeWithAutofattura = this.hasAutofattura(userContext, docamm);
		final boolean registraIva = this.registraIvaCoge(userContext, docamm) || isCommercialeWithAutofattura;

		List<IDocumentoAmministrativoRigaBulk> righeDocamm = Optional.ofNullable(docamm.getChildren()).filter(el->!el.isEmpty()).orElseGet(()->{
			try {
				if (docamm instanceof Documento_genericoBulk) {
					Documento_genericoHome home = (Documento_genericoHome) getHome(userContext, Documento_genericoBulk.class);
					return home.findDocumentoGenericoRigheList((Documento_genericoBulk) docamm);
				} else if (docamm instanceof Fattura_passivaBulk)
					return  Utility.createFatturaPassivaComponentSession().findDettagli(userContext, (Fattura_passivaBulk) docamm);
				return null;
			} catch (ComponentException | PersistencyException | RemoteException | IntrospectionException e) {
				throw new DetailedRuntimeException(e);
			}
		});

		List<DettaglioFinanziario> righeDettFin = righeDocamm.stream()
				.map(rigaDocAmm->{
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
						return new DettaglioFinanziario(docamm, rigaDocAmm.getCd_terzo(), obbligazioneDB.getElemento_voce(), rigaDocAmm.getDt_da_competenza_coge(), rigaDocAmm.getDt_a_competenza_coge(),
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
						return new DettaglioFinanziario(docamm, rigaDocAmm.getCd_terzo(),
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
						return new DettaglioFinanziario(docamm, rigaDocAmm.getCd_terzo(),
								new Elemento_voceBulk(accertamentoDB.getCd_elemento_voce(), accertamentoDB.getEsercizio(), accertamentoDB.getTi_appartenenza(), accertamentoDB.getTi_gestione()),
								rigaDocAmm.getDt_da_competenza_coge(), rigaDocAmm.getDt_a_competenza_coge(),
								rigaDocAmm.getIm_imponibile(), rigaDocAmm.getIm_iva());
					}
					return null;
				}).collect(Collectors.toList());

		Map<Integer, Map<Timestamp, Map<Timestamp, List<DettaglioFinanziario>>>> mapTerzo =
				righeDettFin.stream().collect(Collectors.groupingBy(DettaglioFinanziario::getCdTerzo,
						Collectors.groupingBy(DettaglioFinanziario::getDtDaCompetenzaCoge,
							Collectors.groupingBy(DettaglioFinanziario::getDtACompetenzaCoge))));

		mapTerzo.keySet().forEach(aCdTerzo -> mapTerzo.get(aCdTerzo).keySet().forEach(aDtDaCompCoge -> mapTerzo.get(aCdTerzo).get(aDtDaCompCoge).keySet().forEach(aDtACompCoge -> {
			List<DettaglioFinanziario> righeDettFinTerzo = mapTerzo.get(aCdTerzo).get(aDtDaCompCoge).get(aDtACompCoge);
			TestataPrimaNota testataPrimaNota = new TestataPrimaNota(aCdTerzo, aDtDaCompCoge, aDtACompCoge);
			testataPrimaNotaList.add(testataPrimaNota);

			Map<Integer, Map<String, Map<String, Map<String, List<DettaglioFinanziario>>>>> mapVoce =
					righeDettFinTerzo.stream().collect(Collectors.groupingBy(rigaDettFin->rigaDettFin.getElementoVoce().getEsercizio(),
							Collectors.groupingBy(rigaDettFin->rigaDettFin.getElementoVoce().getTi_appartenenza(),
									Collectors.groupingBy(rigaDettFin->rigaDettFin.getElementoVoce().getTi_gestione(),
											Collectors.groupingBy(rigaDettFin->rigaDettFin.getElementoVoce().getCd_elemento_voce())))));

			mapVoce.keySet().forEach(aEseVoce -> mapVoce.get(aEseVoce).keySet().forEach(aTiAppartenenza -> mapVoce.get(aEseVoce).get(aTiAppartenenza).keySet().forEach(aTiGestione -> mapVoce.get(aEseVoce).get(aTiAppartenenza).get(aTiGestione).keySet().forEach(aCdVoce -> {
				try {
					List<DettaglioFinanziario> righeDettFinVoce = mapVoce.get(aEseVoce).get(aTiAppartenenza).get(aTiGestione).get(aCdVoce);

					BigDecimal imImponibile = righeDettFinVoce.stream().map(DettaglioFinanziario::getImImponibile)
							.reduce(BigDecimal.ZERO, BigDecimal::add);
					BigDecimal imIva = righeDettFinVoce.stream()
							.map(rigaDettFin -> Optional.ofNullable(rigaDettFin.getImImposta()).orElse(BigDecimal.ZERO))
							.reduce(BigDecimal.ZERO, BigDecimal::add);

					//Registrazione IVA
					if (registraIva) {
						if (isCommercialeWithAutofattura) {
							Pair<Voce_epBulk, Voce_epBulk> pairContoIva = this.findPairAutofatturaIva(userContext, righeDettFinVoce.stream().findAny().get());
							testataPrimaNota.openDettaglioIva(docamm, pairContoIva.getFirst().getCd_voce_ep(), imIva);
							testataPrimaNota.closeDettaglioIva(docamm, pairContoIva.getSecond().getCd_voce_ep(), imIva);
						} else {
							Pair<Voce_epBulk, Voce_epBulk> pairContoIva = this.findPairIva(userContext, righeDettFinVoce.stream().findAny().get());
							testataPrimaNota.openDettaglioIva(docamm, pairContoIva.getFirst().getCd_voce_ep(), imIva);
							if (!isFatturaPassivaDaOrdini){
								testataPrimaNota.openDettaglioCostoRicavo(docamm, pairContoIva.getSecond().getCd_voce_ep(), imIva);
							}
						}
					}

					BigDecimal imCosto = registraIva?imImponibile:imImponibile.add(imIva);
					Pair<Voce_epBulk, Voce_epBulk> pairContoCosto = this.findPairCosto(userContext, righeDettFinVoce.stream().findAny().get());
					if (!isFatturaPassivaDaOrdini)
						testataPrimaNota.openDettaglioCostoRicavo(docamm, pairContoCosto.getFirst().getCd_voce_ep(), imCosto);
					testataPrimaNota.openDettaglioPatrimonialePartita(docamm, pairContoCosto.getSecond().getCd_voce_ep(), imCosto, aCdTerzo);
				} catch (ComponentException|PersistencyException|RemoteException e) {
					throw new ApplicationRuntimeException(e);
				}
			}))));

			if (isFatturaPassivaDaOrdini){
				Fattura_passivaBulk fattura = Optional.of(docamm).filter(Fattura_passivaBulk.class::isInstance).map(Fattura_passivaBulk.class::cast).get();
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

					if (registraIva) {
						BigDecimal imIva = righeFatturaOrdine.stream().map(FatturaOrdineBulk::getIvaPerRigaFattura)
								.reduce(BigDecimal.ZERO, BigDecimal::add);
						testataPrimaNota.openDettaglioCostoRicavo(docamm, conto, imIva);
					}
				}));
			}
		})));
		return testataPrimaNotaList;
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaCompenso(UserContext userContext, CompensoBulk compenso) throws ComponentException, ScritturaPartitaDoppiaNotRequiredException {
		try {
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

			Optional<AnticipoBulk> optAnticipo = optMissione.flatMap(missione->Optional.ofNullable(missione.getAnticipo()).map(anticipo->
				Optional.of(anticipo).filter(el->el.getCrudStatus()!=OggettoBulk.UNDEFINED).orElseGet(()-> {
					try {
						AnticipoHome home = (AnticipoHome) getHome(userContext, AnticipoBulk.class);
						return (AnticipoBulk)home.findByPrimaryKey(anticipo);
					} catch (ComponentException | PersistencyException e) {
						throw new DetailedRuntimeException(e);
					}
				}))).filter(anticipo->anticipo.getIm_anticipo().compareTo(BigDecimal.ZERO)!=0);

			final Pair<Voce_epBulk, Voce_epBulk> pairContoCostoAnticipo = optAnticipo.map(anticipo->{
				try {
					return this.findPairCosto(userContext, optAnticipo.get());
				} catch (ComponentException | PersistencyException | RemoteException e) {
					throw new DetailedRuntimeException(e);
				}
			}).orElse(null);

			BigDecimal imContributiCaricoEnte = righeCori.stream().filter(Contributo_ritenutaBulk::isContributoEnte)
					.map(Contributo_ritenutaBulk::getAmmontare)
					.filter(el->el.compareTo(BigDecimal.ZERO)>0)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			if (compenso.getIm_lordo_percipiente().compareTo(BigDecimal.ZERO)==0 && imContributiCaricoEnte.compareTo(BigDecimal.ZERO)==0)
				throw new ScritturaPartitaDoppiaNotRequiredException("Scrittura Economica non necessaria in quanto sia il lordo percipiente che i contributi a carico ente sono nulli.");

			TestataPrimaNota testataPrimaNota = new TestataPrimaNota(compenso.getCd_terzo(), compenso.getDt_da_competenza_coge(), compenso.getDt_a_competenza_coge());

			//Nel caso dei compensi rilevo subito il costo prelevando le informazioni dalla riga del compenso stesso
			//Registrazione conto COSTO COMPENSO
			BigDecimal imCostoCompensoNettoAnticipo = compenso.getIm_lordo_percipiente().subtract(optAnticipo.map(AnticipoBulk::getIm_anticipo).orElse(BigDecimal.ZERO));

			final Pair<Voce_epBulk, Voce_epBulk> pairContoCostoCompenso;
			if (imCostoCompensoNettoAnticipo.compareTo(BigDecimal.ZERO)>0)
				pairContoCostoCompenso = this.findPairCosto(userContext, compenso);
			else
				pairContoCostoCompenso = pairContoCostoAnticipo;

			if (imCostoCompensoNettoAnticipo.compareTo(BigDecimal.ZERO)>0){
				testataPrimaNota.openDettaglioCostoRicavo(compenso, pairContoCostoCompenso.getFirst().getCd_voce_ep(), imCostoCompensoNettoAnticipo);
				testataPrimaNota.openDettaglioPatrimonialePartita(compenso, pairContoCostoCompenso.getSecond().getCd_voce_ep(), imCostoCompensoNettoAnticipo, compenso.getCd_terzo());
			}

			//Registrazione conto CONTRIBUTI-RITENUTE
			//Vengono registrati tutti i CORI a carico Ente
			//Se anticipo risulta maggiore/uguale al compenso allora vengono registrati anche i CORI carico percipiente perchè il mandato non verrà emesso
			boolean isCompensoMaggioreAnticipo = optAnticipo.map(AnticipoBulk::getIm_anticipo).map(imAnticipo->
					compenso.getIm_netto_percipiente().compareTo(imAnticipo)>0).orElse(Boolean.TRUE);

			righeCori.stream().filter(el->el.getAmmontare().compareTo(BigDecimal.ZERO)!=0)
					.filter(el->!isCompensoMaggioreAnticipo || el.isContributoEnte() || el.isTipoContributoIva())
					.forEach(cori->{
						try {
							BigDecimal imCostoCori = cori.getAmmontare();
							Pair<Voce_epBulk, Voce_epBulk> pairContoCostoCori;

							if (cori.isTipoContributoRivalsa()) {
								// Se la tipologia di contributo ritenuta è RIVALSA va tutto sul costo principale del compenso
								testataPrimaNota.openDettaglioCostoRicavo(compenso, pairContoCostoCompenso.getFirst().getCd_voce_ep(), imCostoCori);
								testataPrimaNota.openDettaglioPatrimonialePartita(compenso, pairContoCostoCompenso.getSecond().getCd_voce_ep(), imCostoCori, compenso.getCd_terzo());
							} else if (cori.isTipoContributoIva()) {
								//Il primo giro viene fatto solo per il contributo a carico ente.... nel caso di split esiste anche la riga carico
								//percipiente che, in questa fase, deve solo rilevare il credito verso il fornitore che sarà chiuso all'atto del pagamento
								if (cori.isContributoEnte()) {
									if (compenso.isIstituzionale())
										// Se la tipologia di contributo ritenuta è IVA ed è istituzionale va tutto sul costo principale del compenso
										testataPrimaNota.openDettaglioCostoRicavo(compenso, pairContoCostoCompenso.getFirst().getCd_voce_ep(), imCostoCori);
									else if (compenso.isCommerciale()) {
										// Se la tipologia di contributo ritenuta è IVA Commerciale il conto è del tributo
										Voce_epBulk voceCosto = this.findContoCosto(userContext, cori.getTipoContributoRitenuta(), cori.getEsercizio(), cori.getSezioneCostoRicavo(), cori.getTi_ente_percipiente());
										if (compenso.getFl_split_payment().equals(Boolean.TRUE))
											testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO_SPLIT.value(), compenso.getTipoDocumentoEnum().getSezioneEconomica(), voceCosto.getCd_voce_ep(), imCostoCori, compenso, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
										else
											testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO.value(), compenso.getTipoDocumentoEnum().getSezioneEconomica(), voceCosto.getCd_voce_ep(), imCostoCori, compenso, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
									}
									testataPrimaNota.openDettaglioPatrimonialePartita(compenso, pairContoCostoCompenso.getSecond().getCd_voce_ep(), imCostoCori, compenso.getCd_terzo());

									if (compenso.getFl_split_payment().equals(Boolean.TRUE)) {
										Ass_tipo_cori_voce_epBulk assTipoCori = this.findAssociazioneCoriVoceEp(userContext, cori.getTipoContributoRitenuta(), cori.getEsercizio(), compenso.getTipoDocumentoEnum().getSezionePatrimoniale(), cori.getTi_ente_percipiente());
										testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO_SPLIT.value(), compenso.getTipoDocumentoEnum().getSezionePatrimoniale(), assTipoCori.getCd_voce_ep_contr(), imCostoCori, compenso, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
										testataPrimaNota.closeDettaglioPatrimonialePartita(compenso, pairContoCostoCompenso.getSecond().getCd_voce_ep(), imCostoCori, compenso.getCd_terzo(), true);
									}
								} else { //Contributo Percipiente
									if (compenso.getFl_split_payment().equals(Boolean.TRUE)) {
										Voce_epBulk voceCosto = this.findContoCosto(userContext, cori.getTipoContributoRitenuta(), cori.getEsercizio(), cori.getSezioneCostoRicavo(), cori.getTi_ente_percipiente());
										testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO_SPLIT.value(), compenso.getTipoDocumentoEnum().getSezioneEconomica(), voceCosto.getCd_voce_ep(), imCostoCori, compenso, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
										testataPrimaNota.openDettaglioPatrimonialePartita(compenso, pairContoCostoCompenso.getSecond().getCd_voce_ep(), imCostoCori, compenso.getCd_terzo());
									}
								}
							} else {
								pairContoCostoCori = this.findPairCostoCompenso(userContext, cori);
								testataPrimaNota.openDettaglioCostoRicavo(compenso, pairContoCostoCori.getFirst().getCd_voce_ep(), imCostoCori);
								testataPrimaNota.openDettaglioPatrimonialeCori(compenso, pairContoCostoCori.getSecond().getCd_voce_ep(), imCostoCori, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
							}



/**

							if (Optional.of(cori).map(Contributo_ritenutaBulk::getTipoContributoRitenuta)
									.map(Tipo_contributo_ritenutaBulk::getClassificazioneCori)
									.filter(el -> (el.isTipoIva() && compenso.isIstituzionale()) || el.isTipoRivalsa()).isPresent()) {
								// Se la tipologia di contributo ritenuta è IVA istituzionale o RIVALSA va tutto sul costo principale del compenso
								testataPrimaNota.openDettaglioCostoRicavo(compenso, pairContoCostoCompenso.getFirst().getCd_voce_ep(), imCostoCori);
								testataPrimaNota.openDettaglioPatrimonialePartita(compenso, pairContoCostoCompenso.getSecond().getCd_voce_ep(), imCostoCori, compenso.getCd_terzo());
							} else if (Optional.of(cori).map(Contributo_ritenutaBulk::getTipoContributoRitenuta)
										.map(Tipo_contributo_ritenutaBulk::getClassificazioneCori)
										.filter(el -> el.isTipoIva() && compenso.isCommerciale()).isPresent()) {
								// Se la tipologia di contributo ritenuta è IVA Commerciale il conto è del tributo ma la contropartita è del compenso
								Voce_epBulk voceCosto = this.findContoCosto(userContext, cori.getTipoContributoRitenuta(), cori.getEsercizio(), cori.getSezioneCostoRicavo(), cori.getTi_ente_percipiente());
								testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.CREDITO.value(), compenso.getTipoDocumentoEnum().getSezioneEconomica(), voceCosto.getCd_voce_ep(), imCostoCori, compenso, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
								testataPrimaNota.openDettaglioPatrimonialePartita(compenso, pairContoCostoCompenso.getSecond().getCd_voce_ep(), imCostoCori, compenso.getCd_terzo());
							} else {
								pairContoCostoCori = this.findPairCostoCompenso(userContext, cori);
								testataPrimaNota.openDettaglioCostoRicavo(compenso, pairContoCostoCori.getFirst().getCd_voce_ep(), imCostoCori);
								testataPrimaNota.openDettaglioPatrimonialeCori(compenso, pairContoCostoCori.getSecond().getCd_voce_ep(), imCostoCori, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
							}
 */
						} catch (ComponentException|PersistencyException e) {
							throw new ApplicationRuntimeException(e);
						}
					});

			//se esiste anticipo devo fare registrazioni una serie di registrazioni
			optAnticipo.ifPresent(anticipo->{
				try {
					BigDecimal imCostoAnticipo = anticipo.getIm_anticipo();

					// 1. scaricare i costi dell'anticipo leggendo la voce bilancio associata allo stesso
					testataPrimaNota.openDettaglioCostoRicavo(compenso, pairContoCostoAnticipo.getFirst().getCd_voce_ep(), imCostoAnticipo);
					testataPrimaNota.openDettaglioPatrimonialePartita(compenso, pairContoCostoCompenso.getSecond().getCd_voce_ep(), imCostoAnticipo, compenso.getCd_terzo());

					//2. chiudere il conto credito per anticipo
					List<Movimento_cogeBulk> allMovimentiPrimaNota = this.findMovimentiPrimaNota(userContext,anticipo);
					String contoPatrimonialeAperturaCreditoAnticipo = this.findMovimentoAperturaCreditoAnticipo(allMovimentiPrimaNota, anticipo, anticipo.getCd_terzo()).getCd_voce_ep();

					testataPrimaNota.closeDettaglioCostoRicavoPartita(anticipo, contoPatrimonialeAperturaCreditoAnticipo, imCostoAnticipo, anticipo.getCd_terzo());
					testataPrimaNota.closeDettaglioPatrimonialePartita(compenso, pairContoCostoCompenso.getSecond().getCd_voce_ep(), imCostoAnticipo, compenso.getCd_terzo(), true);
				} catch (ComponentException|PersistencyException e) {
					throw new ApplicationRuntimeException(e);
				}
			});

			return this.generaScrittura(userContext, compenso, Collections.singletonList(testataPrimaNota), true);
		} catch (PersistencyException|RemoteException e) {
			throw handleException(e);
		}
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaCompensoConguagli(UserContext userContext, CompensoBulk compenso) throws ComponentException {
		throw new ApplicationException("Gestione da effettuare.");
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
			TestataPrimaNota testataPrimaNotaCompenso = new TestataPrimaNota(compenso.getCd_terzo(), compenso.getDt_da_competenza_coge(), compenso.getDt_a_competenza_coge());
			testataPrimaNotaDocumentoPassivo.getDett().forEach(el-> testataPrimaNotaCompenso.addDettaglio(el.getTipoDett(), el.getSezione(), el.getCdConto(), el.getImporto(), el.getPartita(), el.getCdTerzo(), el.getCdCori()));

			//quindi aggiungo le ritenute

			//Per gli stipendi, essendo già stato regsistrato il conto di costo tramite il documento generico, e non essendo stato da nessuna parte registrato il legame
			//tra i codici contributi e le righe del documento generico, la gestione dei contributi a carico ente viene fatta senza registrare il codice CORI
			//Pertanto il giro su CIRI Ente non viene fatto
			/**
			List<Contributo_ritenutaBulk> righeCori = Optional.ofNullable(compenso.getChildren()).orElseGet(()->{
				try {
					Contributo_ritenutaHome home = (Contributo_ritenutaHome) getHome(userContext, Contributo_ritenutaBulk.class);
					return (java.util.List<Contributo_ritenutaBulk>)home.loadContributiRitenute(compenso);
				} catch (ComponentException | PersistencyException e) {
					throw new DetailedRuntimeException(e);
				}
			});

			righeCori.stream().filter(el->el.isContributoEnte())
				.forEach(cori->{
					try {
						BigDecimal imCostoCori = cori.getAmmontare();
						if (imCostoCori.compareTo(BigDecimal.ZERO)!=0) {
							Pair<Voce_epBulk, Voce_epBulk> pairContoCostoCori;
							if (Optional.of(cori).map(Contributo_ritenutaBulk::getTipoContributoRitenuta)
								.map(Tipo_contributo_ritenutaBulk::getClassificazioneCori)
								.filter(el -> (el.isTipoIva() && compenso.isIstituzionale()) || el.isTipoRivalsa()).isPresent()) {
								// Se la tipologia di contributo ritenuta è IVA o RIVALSA il conto ritornato è quello di costo principale del compenso
								throw new ApplicationRuntimeException("E' stato individuato sul compenso stipendi un contributO di tipo IVA e/o Rivalsa non previsto.");
							} else {
								pairContoCostoCori = this.findPairCostoCompenso(userContext, cori);
								testataPrimaNotaCompenso.openDettaglioCostoRicavo(compenso, pairContoCostoCori.getFirst().getCd_voce_ep(), imCostoCori);
								testataPrimaNotaCompenso.openDettaglioPatrimonialeCori(compenso, pairContoCostoCori.getSecond().getCd_voce_ep(), imCostoCori, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
							}
						}
					} catch (ComponentException|PersistencyException e) {
						throw new ApplicationRuntimeException(e);
					}
				});
			*/

			return this.generaScrittura(userContext, compenso, Collections.singletonList(testataPrimaNotaCompenso), true);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaAnticipo(UserContext userContext, AnticipoBulk anticipo) throws ComponentException {
		try {
			TestataPrimaNota testataPrimaNota = new TestataPrimaNota(anticipo.getCd_terzo(), anticipo.getDt_da_competenza_coge(), anticipo.getDt_a_competenza_coge());

			//Registrazione conto COSTO ANTICIPO
			BigDecimal imCostoAnticipo = anticipo.getIm_anticipo();
			if (imCostoAnticipo.compareTo(BigDecimal.ZERO)!=0) {
				Pair<Voce_epBulk, Voce_epBulk> pairContoCostoAnticipo = this.findPairContiAnticipo(userContext, anticipo);
				testataPrimaNota.openDettaglioCostoRicavoPartita(anticipo, pairContoCostoAnticipo.getFirst().getCd_voce_ep(), imCostoAnticipo, anticipo.getCd_terzo());
				testataPrimaNota.openDettaglioPatrimonialePartita(anticipo, pairContoCostoAnticipo.getSecond().getCd_voce_ep(), imCostoAnticipo, anticipo.getCd_terzo());
			}
			return this.generaScrittura(userContext, anticipo, Collections.singletonList(testataPrimaNota), false);
		} catch (PersistencyException|RemoteException e) {
			throw handleException(e);
		}
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaRimborso(UserContext userContext, RimborsoBulk rimborso) throws ComponentException {
		try {
			TestataPrimaNota testataPrimaNota = new TestataPrimaNota(rimborso.getCd_terzo(), rimborso.getDt_da_competenza_coge(), rimborso.getDt_a_competenza_coge());

			//Registrazione conto COSTO ANTICIPO
			BigDecimal imCostoRimborso = rimborso.getIm_rimborso();
			if (imCostoRimborso.compareTo(BigDecimal.ZERO)!=0) {
				Pair<Voce_epBulk, Voce_epBulk> pairContoCostoRimborso = this.findPairContiAnticipo(userContext, rimborso.getAnticipo());
				testataPrimaNota.closeDettaglioCostoRicavo(rimborso.getAnticipo(), pairContoCostoRimborso.getFirst().getCd_voce_ep(), imCostoRimborso);
				testataPrimaNota.closeDettaglioPatrimonialePartita(rimborso.getAnticipo(), pairContoCostoRimborso.getSecond().getCd_voce_ep(), imCostoRimborso, rimborso.getAnticipo().getCd_terzo());
			}
			return this.generaScrittura(userContext, rimborso, Collections.singletonList(testataPrimaNota), false);
		} catch (PersistencyException|RemoteException e) {
			throw handleException(e);
		}
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaMissione(UserContext userContext, MissioneBulk missione) throws ComponentException {
		try {
			//Le missioni pagate con compenso non creano scritture di prima nota in quanto create direttamente dal compenso stesso
			if (missione.getFl_associato_compenso() || missione.isMissioneProvvisoria() || missione.isAnnullato())
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

			final Pair<Voce_epBulk, Voce_epBulk> pairContoCostoAnticipo = optAnticipo.map(anticipo->{
				try {
					return this.findPairCosto(userContext, optAnticipo.get());
				} catch (ComponentException | PersistencyException | RemoteException e) {
					throw new DetailedRuntimeException(e);
				}
			}).orElse(null);

			TestataPrimaNota testataPrimaNota = new TestataPrimaNota(missione.getCd_terzo(), missione.getDt_inizio_missione(), missione.getDt_fine_missione());

			//Registrazione conto COSTO MISSIONE
			BigDecimal imCostoMissioneNettoAnticipo = missione.getIm_totale_missione().subtract(optAnticipo.map(AnticipoBulk::getIm_anticipo).orElse(BigDecimal.ZERO));

			final Pair<Voce_epBulk, Voce_epBulk> pairContoCostoMissione;
			if (imCostoMissioneNettoAnticipo.compareTo(BigDecimal.ZERO)>0)
				pairContoCostoMissione = this.findPairCosto(userContext, missione);
			else
				pairContoCostoMissione = pairContoCostoAnticipo;

			if (imCostoMissioneNettoAnticipo.compareTo(BigDecimal.ZERO)>0) {
				testataPrimaNota.openDettaglioCostoRicavo(missione, pairContoCostoMissione.getFirst().getCd_voce_ep(), imCostoMissioneNettoAnticipo);
				testataPrimaNota.openDettaglioPatrimonialePartita(missione, pairContoCostoMissione.getSecond().getCd_voce_ep(), imCostoMissioneNettoAnticipo, missione.getCd_terzo());
			}

			//se esiste anticipo devo fare una serie di registrazioni
			optAnticipo.ifPresent(anticipo->{
				try {
					BigDecimal imCostoAnticipo = anticipo.getIm_anticipo();

					// 1. scaricare i costi dell'anticipo compensandoli con il conto patrimoniale della missione
					testataPrimaNota.openDettaglioCostoRicavo(missione, pairContoCostoAnticipo.getFirst().getCd_voce_ep(), imCostoAnticipo);
					testataPrimaNota.openDettaglioPatrimonialePartita(missione, pairContoCostoMissione.getSecond().getCd_voce_ep(), imCostoAnticipo, missione.getCd_terzo());

					//2. chiudere il conto credito per anticipo
					List<Movimento_cogeBulk> allMovimentiPrimaNota = this.findMovimentiPrimaNota(userContext, anticipo);
					String contoPatrimonialeAperturaCreditoAnticipo = this.findMovimentoAperturaCreditoAnticipo(allMovimentiPrimaNota, anticipo, anticipo.getCd_terzo()).getCd_voce_ep();

					testataPrimaNota.closeDettaglioCostoRicavoPartita(anticipo, contoPatrimonialeAperturaCreditoAnticipo, imCostoAnticipo, anticipo.getCd_terzo());
					testataPrimaNota.closeDettaglioPatrimonialePartita(missione, pairContoCostoMissione.getSecond().getCd_voce_ep(), imCostoAnticipo, missione.getCd_terzo(), true);
				} catch (ComponentException|PersistencyException e) {
					throw new ApplicationRuntimeException(e);
				}
			});
			return this.generaScrittura(userContext, missione, Collections.singletonList(testataPrimaNota), true);
		} catch (PersistencyException|RemoteException e) {
			throw handleException(e);
		}
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaMandato(UserContext userContext, MandatoBulk mandato) throws ComponentException {
		try {
			//Il mandato vincolato ad altri mandati non deve generare scritture patrimoniali in quanto rilevate in fase di pagamento mandato
			if (((Ass_mandato_mandatoHome)getHome(userContext, Ass_mandato_mandatoBulk.class)).findByMandatoCollegato(userContext, mandato, false).stream().findFirst().isPresent())
				return null;

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
				mandato.setUnita_organizzativa((Unita_organizzativaBulk) getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(mandato.getUnita_organizzativa()));

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
				else if (mandato.getMandato_rigaColl().stream().map(Mandato_rigaBulk::getCd_tipo_documento_amm)
						.anyMatch(el->TipoDocumentoEnum.fromValue(el).isGenericoCoriAccantonamentoSpesa()))
					return this.proposeScritturaPartitaDoppiaMandatoAccantonamentoCori(userContext, mandato);
				TestataPrimaNota testataPrimaNota = new TestataPrimaNota(mandato.getMandato_terzo().getCd_terzo(), null, null);
				if (mandato.isMandatoRegolarizzazione() && mandato.getCd_cds().equals(mandato.getCd_cds_origine()) &&
						mandato.getUnita_organizzativa().getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
					return null;

				mandato.getMandato_rigaColl().forEach(rigaMandato -> {
					try {
						if (TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()).isDocumentoAmministrativoPassivo() ||
								TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()).isDocumentoAmministrativoAttivo() ||
								TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()).isAnticipo() ||
								TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()).isMissione())
							addDettagliPrimaNotaMandatoDocumentiVari(userContext, testataPrimaNota, rigaMandato);
					} catch (ComponentException|PersistencyException|RemoteException e) {
						throw new ApplicationRuntimeException(e);
					}
				});
				return this.generaScrittura(userContext, mandato, Collections.singletonList(testataPrimaNota), true);
			}
			return null;
		} catch (PersistencyException | RemoteException e) {
			throw handleException(e);
		}
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaManRevAnnullato(UserContext userContext, IManRevBulk manrev) throws ComponentException, PersistencyException {
		boolean isMandato = manrev instanceof MandatoBulk;
		if (!manrev.isAnnullato())
			throw new ApplicationException((isMandato?"Il mandato ":"La reversale") + manrev.getEsercizio() + "/" + manrev.getCd_cds() + "/" + manrev.getPg_manrev() +
					" non risulta " + (isMandato?"annullato":"annullata")+". Proposta di prima nota non possibile.");

		TestataPrimaNota testataPrimaNota = new TestataPrimaNota(manrev.getTerzo().getCd_terzo(), null, null);
		//storno scrittura prima nota del mandato
		this.stornoTotaleScritturaPrimaNota(userContext, testataPrimaNota, manrev);
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
				.noneMatch(el->TipoDocumentoEnum.fromValue(el).isCompenso()))
			throw new ApplicationException("Il mandato " + mandato.getEsercizio() + "/" + mandato.getCds() + "/" + mandato.getPg_mandato() +
					" non risulta pagare un compenso. Proposta di prima nota non possibile.");
		if (mandato.getMandato_rigaColl().isEmpty() || mandato.getMandato_rigaColl().size()>1)
			throw new ApplicationException("Il mandato " + mandato.getEsercizio() + "/" + mandato.getCds() + "/" + mandato.getPg_mandato() +
					" ha un numero di righe non coerente con l'unica prevista per un mandato di compenso. Proposta di prima nota non possibile.");

		Mandato_rigaBulk rigaMandato = mandato.getMandato_rigaColl().get(0);
		TestataPrimaNota testataPrimaNota = new TestataPrimaNota(mandato.getMandato_terzo().getCd_terzo(), null, null);

		CompensoBulk compenso = (CompensoBulk)getHome(userContext, CompensoBulk.class).findByPrimaryKey(new CompensoBulk(rigaMandato.getCd_cds_doc_amm(), rigaMandato.getCd_uo_doc_amm(), rigaMandato.getEsercizio_doc_amm(),
				rigaMandato.getPg_doc_amm()));

		List<Contributo_ritenutaBulk> righeCori = Optional.ofNullable(compenso.getChildren()).orElseGet(()->{
			try {
				Contributo_ritenutaHome home = (Contributo_ritenutaHome) getHome(userContext, Contributo_ritenutaBulk.class);
				return (java.util.List<Contributo_ritenutaBulk>)home.loadContributiRitenute(compenso);
			} catch (ComponentException | PersistencyException e) {
				throw new DetailedRuntimeException(e);
			}
		});

		//recupero la scrittura del compenso
		List<Movimento_cogeBulk> allMovimentiPrimaNotaCompenso = this.findMovimentiPrimaNota(userContext,compenso);

		if (allMovimentiPrimaNotaCompenso.isEmpty())
			throw new ApplicationException("Non risulta generata la scrittura Prima Nota del compenso "+compenso.getEsercizio()+"/"+compenso.getPg_compenso()+" associato del mandato.");

		BigDecimal imSaldoPatrimoniale = allMovimentiPrimaNotaCompenso.stream()
				.filter(el->Movimento_cogeBulk.TipoRiga.DEBITO.value().equals(el.getTi_riga()))
				.map(el->el.isSezioneAvere()?el.getIm_movimento():el.getIm_movimento().negate())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		if (imSaldoPatrimoniale.compareTo(rigaMandato.getIm_mandato_riga())!=0)
			throw new ApplicationException("La scrittura Prima Nota associata del compenso "+compenso.getEsercizio()+"/"+compenso.getPg_compenso()+" associato del mandato presenta in Avere movimenti di conti patrimoniali " +
					"per un importo non coincidente con quello del mandato stesso.");

		// Se la tipologia di contributo ritenuta è IVA o RIVALSA non calcolo le ritenute perchè vanno direttamente al fornitore
		BigDecimal imRitenuteFittizie = righeCori.stream()
				.filter(el -> el.isContributoEnte())
				.filter(el -> el.isTipoContributoRivalsa() || el.isTipoContributoIva())
				.map(Contributo_ritenutaBulk::getAmmontare)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal imRitenutePositive = righeCori.stream().map(Contributo_ritenutaBulk::getAmmontare)
				.filter(el->el.compareTo(BigDecimal.ZERO)>0)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal imRitenuteNegative = righeCori.stream().map(Contributo_ritenutaBulk::getAmmontare)
				.filter(el->el.compareTo(BigDecimal.ZERO)<0)
				.reduce(BigDecimal.ZERO, BigDecimal::add).abs();

		BigDecimal imContributiCaricoEnte = righeCori.stream().filter(Contributo_ritenutaBulk::isContributoEnte)
				.map(Contributo_ritenutaBulk::getAmmontare)
				.filter(el->el.compareTo(BigDecimal.ZERO)>0)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		//Aggiungo eventuali quote da trattenere indicate sul compenso
		imRitenutePositive = imRitenutePositive.add(Optional.ofNullable(compenso.getIm_netto_da_trattenere()).orElse(BigDecimal.ZERO));

		if (imRitenutePositive.subtract(imRitenuteFittizie).compareTo(rigaMandato.getIm_ritenute_riga())!=0)
			throw new ApplicationException("L'importo delle righe ritenute del compenso associato al mandato non corrisponde con l'importo ritenute associato al mandato.");

		//Per il calcolo del netto mandato effettuo la sottrazione tra:
		//1. il lordo mandato calcolato aggiungendo anche le righe ritenute negative che hanno sicuramente generato un mandato vincolato
		//2. le ritenute positive
		BigDecimal imNettoMandato = rigaMandato.getIm_mandato_riga().add(imRitenuteNegative).subtract(rigaMandato.getIm_ritenute_riga());

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
		righeCori.stream().filter(el->el.getAmmontare().compareTo(BigDecimal.ZERO)!=0).forEach(cori->{
			try {
				BigDecimal imCori = cori.getAmmontare();

				// Se la tipologia di contributo ritenuta è IVA o RIVALSA non registro la ritenuta in quanto già registrata in fase di chiusura debito mandato (imNettoMandato contiene imCori)
				// L'Iva, sia per compensa istituzionale che commerciale, va sempre a costo,
				if (!cori.isTipoContributoRivalsa() && !(cori.isTipoContributoIva() && cori.isContributoEnte())) {
					if (cori.isTipoContributoIva() && cori.isContributoPercipiente()) {
						String contoToClose = this.findMovimentoAperturaCoriIVACompenso(allMovimentiPrimaNotaCompenso, compenso, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta()).getCd_voce_ep();
						testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO_SPLIT.value(), compenso.getTipoDocumentoEnum().getSezionePatrimoniale(), contoToClose, imCori, compenso, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
						testataPrimaNota.closeDettaglioPatrimonialePartita(compenso, contoPatrimonialePartita, imCori, compenso.getCd_terzo());
					} else {
						Pair<Voce_epBulk, Voce_epBulk> pairContoCori = this.findPairContiMandato(userContext, cori);
						Voce_epBulk contoVersamentoCori = pairContoCori.getSecond();
						//se rigaMandato.getIm_mandato_riga() è uguale a imContributiCaricoEnte il contoPatrimonialePartita non è impostato.
						//Ma in questo caso non dovrebbe mai verificarsi che ci siano contributi a carico percipiente altrimenti il mandato sarebbe negativo
						if (cori.isContributoPercipiente())
							testataPrimaNota.closeDettaglioPatrimonialePartita(compenso, contoPatrimonialePartita, imCori, compenso.getCd_terzo());
						else {
							String contoToClose = this.findMovimentoAperturaCori(allMovimentiPrimaNotaCompenso, compenso, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta()).getCd_voce_ep();
							testataPrimaNota.closeDettaglioPatrimonialeCori(compenso, contoToClose, imCori, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
						}
						testataPrimaNota.openDettaglioPatrimonialeCori(compenso, contoVersamentoCori.getCd_voce_ep(), imCori, compenso.getCd_terzo(), cori.getCd_contributo_ritenuta());
					}
				}
			} catch (ComponentException|PersistencyException e) {
				throw new ApplicationRuntimeException(e);
			}
		});

		return this.generaScrittura(userContext, mandato, Collections.singletonList(testataPrimaNota), true);
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaMandatoStipendi(UserContext userContext, MandatoBulk mandato) throws ComponentException, PersistencyException, RemoteException {
		//recupero il documento generico passivo leggendolo dalla tabelle stipendiCofiBulk
		Stipendi_cofiBulk stipendiCofiBulk = ((Stipendi_cofiHome)getHome(userContext, Stipendi_cofiBulk.class)).findStipendiCofi(mandato);

		if (!Optional.ofNullable(stipendiCofiBulk).isPresent())
			throw new ApplicationException("Il mandato " + mandato.getEsercizio() + "/" + mandato.getCds() + "/" + mandato.getPg_mandato() +
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

		TestataPrimaNota testataPrimaNota = new TestataPrimaNota(mandato.getMandato_terzo().getCd_terzo(), null, null);

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

		BigDecimal imRitenuteNegative = righeCori.stream().map(Contributo_ritenutaBulk::getAmmontare)
				.filter(el->el.compareTo(BigDecimal.ZERO)<0)
				.reduce(BigDecimal.ZERO, BigDecimal::add).abs();

		if (imRitenutePositive.compareTo(mandato.getIm_ritenute())!=0)
			throw new ApplicationException("L'importo delle righe ritenute del compenso associato al mandato non corrisponde con l'importo ritenute associato al mandato.");

		//Registrazione conto CONTRIBUTI-RITENUTE
		righeCori.stream().filter(el->el.getAmmontare().compareTo(BigDecimal.ZERO)!=0).forEach(cori->{
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

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaMandatoVersamentoCori(UserContext userContext, MandatoBulk mandato) throws ComponentException, PersistencyException, RemoteException {
		Collection<Liquid_gruppo_coriBulk> liquidGruppoCoriBulk = ((Liquid_gruppo_coriHome) getHome(userContext, Liquid_gruppo_coriBulk.class)).findByMandato(userContext, mandato);

		Voce_epBulk voceEpBanca = this.findContoBanca(userContext, CNRUserContext.getEsercizio(userContext));

		Stream<Liquid_gruppo_cori_detBulk> details = liquidGruppoCoriBulk.stream().flatMap(el->{
			try {
				return ((Liquid_gruppo_coriHome) getHome(userContext, Liquid_gruppo_coriBulk.class)).findDettagli(userContext, el).stream();
			} catch(ComponentException|PersistencyException ex) {
				throw new ApplicationRuntimeException(ex);
			}
		});

		TestataPrimaNota testataPrimaNota = new TestataPrimaNota(mandato.getMandato_terzo().getCd_terzo(), null, null);

		//a questo punto ho la lista dei compensi e dei codici contributi versati
		details.forEach(detail->{
			try {
				CompensoBulk compenso = (CompensoBulk) getHome( userContext, CompensoBulk.class).findByPrimaryKey( new CompensoBulk(detail.getCd_cds_origine(), detail.getCd_uo_origine(), detail.getEsercizio_contributo_ritenuta(), detail.getPg_compenso()));

				//verifico se è un compenso da stipendi recuperando il documento generico passivo leggendolo dalla tabelle stipendiCofiBulk
				Stipendi_cofiBulk stipendiCofiBulk = ((Stipendi_cofiHome)getHome(userContext, Stipendi_cofiBulk.class)).findStipendiCofi(compenso);

				Integer cdTerzoMandato = compenso.getCd_terzo();
				if (stipendiCofiBulk!=null) {
					MandatoBulk mandatoStipendiBulk = (MandatoBulk) getHome(userContext, MandatoIBulk.class)
							.findByPrimaryKey(new MandatoBulk(stipendiCofiBulk.getCd_cds_mandato(), stipendiCofiBulk.getEsercizio_mandato(), stipendiCofiBulk.getPg_mandato()));

					mandatoStipendiBulk.setMandato_terzo(((MandatoHome) getHome(userContext, mandato.getClass())).findMandato_terzo(userContext, mandatoStipendiBulk, false));

					cdTerzoMandato = mandatoStipendiBulk.getMandato_terzo().getCd_terzo();
				}

				Map<String, Pair<String, BigDecimal>> saldiCori = ((Movimento_cogeHome) getHome(userContext, Movimento_cogeBulk.class)).getSaldiMovimentiCori(compenso, cdTerzoMandato, detail.getCd_contributo_ritenuta());

				//dovrei trovare tra i saldi proprio l'import liquidato
				//Il conto aperto deve essere solo uno
				if (saldiCori.values().stream().filter(el->el.getSecond().compareTo(BigDecimal.ZERO)!=0).collect(Collectors.toList()).size()>1)
					throw new ApplicationRuntimeException("Per il compenso "+compenso.getEsercizio()+"/"+compenso.getCd_cds()+"/"+compenso.getPg_compenso()+
							" e per il contributo "+detail.getCd_contributo_ritenuta()+" esiste più di un conto che presenta un saldo positivo.");

				saldiCori.keySet().stream().forEach(cdVoceEp->{
					Pair<String, BigDecimal> saldoVoce = saldiCori.get(cdVoceEp);
					if (saldoVoce.getSecond().compareTo(BigDecimal.ZERO)!=0)
						if (saldoVoce.getFirst().equals(Movimento_cogeBulk.SEZIONE_AVERE)) {
							testataPrimaNota.closeDettaglioPatrimonialeCori(compenso, cdVoceEp, saldoVoce.getSecond(), compenso.getCd_terzo(), detail.getCd_contributo_ritenuta());
							testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), compenso.getTipoDocumentoEnum().getSezionePatrimoniale(), voceEpBanca.getCd_voce_ep(), saldoVoce.getSecond());
						} else {
							testataPrimaNota.openDettaglioPatrimonialeCori(compenso, cdVoceEp, saldoVoce.getSecond(), compenso.getCd_terzo(), detail.getCd_contributo_ritenuta());
							testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), Movimento_cogeBulk.getControSezione(compenso.getTipoDocumentoEnum().getSezionePatrimoniale()), voceEpBanca.getCd_voce_ep(), saldoVoce.getSecond());
						}
				});
			} catch(ComponentException|PersistencyException ex) {
				throw new ApplicationRuntimeException(ex);
			}
		});

		return this.generaScrittura(userContext, mandato, Collections.singletonList(testataPrimaNota), true);
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaMandatoAccantonamentoCori(UserContext userContext, MandatoBulk mandato) throws ComponentException, PersistencyException, RemoteException {
		if (mandato.getMandato_rigaColl().stream().map(Mandato_rigaBulk::getCd_tipo_documento_amm).noneMatch(el->TipoDocumentoEnum.fromValue(el).isGenericoCoriAccantonamentoSpesa()))
			throw new ApplicationException("La riga del mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +
					" non risulta pagare un versamento/accantonamento cori. Proposta di prima nota non possibile.");

		if (mandato.getMandato_rigaColl().size()>1)
			throw new ApplicationException("Mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +
					" di versamento/accantonamento cori con più di una riga. Proposta di prima nota non possibile.");

		if (mandato.getMandato_rigaColl().size()>1)
			throw new ApplicationException("Mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +
					" di versamento/accantonamento cori con più di una riga. Proposta di prima nota non possibile.");

		Mandato_rigaBulk rigaMandato = mandato.getMandato_rigaColl().stream().findAny()
				.orElseThrow(()->new ApplicationException("Mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +
						" di versamento/accantonamento cori senza righe. Proposta di prima nota non possibile."));

		final BigDecimal imNettoMandato = mandato.getIm_mandato().subtract(mandato.getIm_ritenute());

		if (imNettoMandato.compareTo(BigDecimal.ZERO)!=0)
			throw new ApplicationException("Mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +
					" di versamento/accantonamento cori di importo non nullo. Proposta di prima nota non possibile.");

		//Devo recuperare il compenso legato ai mandati di versamento/accantonamento cori
		Collection<Ass_comp_doc_cont_nmpBulk> listCompensi = ((Ass_comp_doc_cont_nmpHome)getHome(userContext, Ass_comp_doc_cont_nmpBulk.class)).findByDocumento(userContext, mandato);
		if (listCompensi.size()>1)
			throw new ApplicationException("Errore nell'individuazione del compenso associato alla riga del mandato di accantonamento spesa "+rigaMandato.getEsercizio()+"/"+rigaMandato.getPg_mandato()+": troppi conpmesi associati.");

		Ass_comp_doc_cont_nmpBulk assCompDocContNmpBulk = listCompensi.stream().findAny().orElseThrow(()->new ApplicationException("Errore nell'individuazione del compenso associato alla riga del mandato di accantonamento spesa "+rigaMandato.getEsercizio()+"/"+rigaMandato.getPg_mandato()+": compenso non esistente."));
		CompensoBulk compenso = (CompensoBulk) getHome(userContext, CompensoBulk.class)
				.findByPrimaryKey(new CompensoBulk(assCompDocContNmpBulk.getCd_cds_compenso(), assCompDocContNmpBulk.getCd_uo_compenso(), assCompDocContNmpBulk.getEsercizio_compenso(),
						assCompDocContNmpBulk.getPg_compenso()));

		List<Contributo_ritenutaBulk> righeCori = Optional.ofNullable(compenso.getChildren()).orElseGet(()->{
			try {
				Contributo_ritenutaHome home = (Contributo_ritenutaHome) getHome(userContext, Contributo_ritenutaBulk.class);
				return (java.util.List<Contributo_ritenutaBulk>)home.loadContributiRitenute(compenso);
			} catch (ComponentException | PersistencyException e) {
				throw new DetailedRuntimeException(e);
			}
		});

		BigDecimal imSaldoContributi = righeCori.stream()
				.map(Contributo_ritenutaBulk::getAmmontare)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		if (imSaldoContributi.compareTo(BigDecimal.ZERO)!=0)
			throw new ApplicationException("Mandato " + mandato.getEsercizio() + "/" + mandato.getCd_cds() + "/" + mandato.getPg_mandato() +
					" di versamento/accantonamento cori con ritenute il cui saldo totale non è nullo. Proposta di prima nota non possibile.");

		//recupero la scrittura del compenso
		List<Movimento_cogeBulk> allMovimentiPrimaNotaCompenso = this.findMovimentiPrimaNota(userContext,compenso);

		TestataPrimaNota testataPrimaNota = new TestataPrimaNota(mandato.getMandato_terzo().getCd_terzo(), null, null);

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

		return this.generaScrittura(userContext, mandato, Collections.singletonList(testataPrimaNota), true);
	}

	private Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppiaReversale(UserContext userContext, ReversaleBulk reversale) throws ComponentException, ScritturaPartitaDoppiaNotRequiredException {
		try {
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

			if (!Optional.ofNullable(reversale.getReversale_terzo()).isPresent())
				reversale.setReversale_terzo(((ReversaleHome) getHome(userContext, reversale.getClass())).findReversale_terzo(userContext, reversale, false));

			//La reversale vincolata non deve generare scritture patrimoniali in quanto rilevate in fase di pagamento mandato
			if (((Ass_mandato_reversaleHome)getHome(userContext, Ass_mandato_reversaleBulk.class)).findMandati(userContext, reversale, false).stream().findFirst().isPresent())
				throw new ScritturaPartitaDoppiaNotRequiredException("Scrittura Economica non prevista in quanto la reversale risulta vincolata ad un mandato di pagamento.");
			//Il documento deve essere annullato o esitato altrimenti esce
			else if (reversale.isAnnullato())
				return this.proposeScritturaPartitaDoppiaManRevAnnullato(userContext, reversale);
			else if (reversale.isIncassato()) {
				TestataPrimaNota testataPrimaNota = new TestataPrimaNota(reversale.getReversale_terzo().getCd_terzo(), null, null);

				reversale.getReversale_rigaColl().forEach(rigaReversale -> {
					try {
						if (TipoDocumentoEnum.fromValue(rigaReversale.getCd_tipo_documento_amm()).isDocumentoAmministrativoAttivo())
							addDettagliPrimaNotaReversaleDocumentiVari(userContext, testataPrimaNota, rigaReversale);
					} catch (ComponentException|PersistencyException|RemoteException e) {
						throw new ApplicationRuntimeException(e);
					}
				});
				return this.generaScrittura(userContext, reversale, Collections.singletonList(testataPrimaNota), true);
			}
			return null;
		} catch (PersistencyException|IntrospectionException e) {
			throw handleException(e);
		}
	}

	private void addDettagliPrimaNotaMandatoDocumentiVari(UserContext userContext, TestataPrimaNota testataPrimaNota, Mandato_rigaBulk rigaMandato) throws ComponentException, PersistencyException, RemoteException {
		if (!TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()).isDocumentoAmministrativoPassivo() &&
				!TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()).isDocumentoAmministrativoAttivo() &&
				!TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()).isAnticipo() &&
				!TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()).isMissione())
			throw new ApplicationException("La riga del mandato " + rigaMandato.getEsercizio() + "/" + rigaMandato.getCd_cds() + "/" + rigaMandato.getPg_mandato() +
					" non risulta pagare un documento/anticipo/missione. Proposta di prima nota non possibile.");

		final BigDecimal imNettoMandato = rigaMandato.getIm_mandato_riga().subtract(rigaMandato.getIm_ritenute_riga());

		//La partita non deve essere registrata in caso di versamento ritenute
		IDocumentoCogeBulk partita;
		if (TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()).isFatturaPassiva() || TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()).isNotaDebitoPassiva() ||
				TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()).isNotaCreditoPassiva()) {
			partita = (Documento_amministrativo_passivoBulk) getHome(userContext, Documento_amministrativo_passivoBulk.class).findByPrimaryKey(new Documento_amministrativo_passivoBulk(rigaMandato.getCd_cds_doc_amm(), rigaMandato.getCd_uo_doc_amm(), rigaMandato.getEsercizio_doc_amm(), rigaMandato.getPg_doc_amm()));
		} else if (TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()).isFatturaAttiva() || TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()).isNotaDebitoAttiva() ||
				TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()).isNotaCreditoAttiva()) {
			partita = (Documento_amministrativo_attivoBulk) getHome(userContext, Documento_amministrativo_attivoBulk.class).findByPrimaryKey(new Documento_amministrativo_attivoBulk(rigaMandato.getCd_cds_doc_amm(), rigaMandato.getCd_uo_doc_amm(), rigaMandato.getEsercizio_doc_amm(), rigaMandato.getPg_doc_amm()));
		} else if (TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()).isGenericoSpesa()) {
			partita = (Documento_generico_passivoBulk) getHome(userContext, Documento_generico_passivoBulk.class).findByPrimaryKey(new Documento_generico_passivoBulk(rigaMandato.getCd_cds_doc_amm(), rigaMandato.getCd_tipo_documento_amm(), rigaMandato.getCd_uo_doc_amm(), rigaMandato.getEsercizio_doc_amm(), rigaMandato.getPg_doc_amm()));
		} else if (TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()).isMissione()) {
			partita = (MissioneBulk) getHome(userContext, MissioneBulk.class).findByPrimaryKey(new MissioneBulk(rigaMandato.getCd_cds_doc_amm(), rigaMandato.getCd_uo_doc_amm(), rigaMandato.getEsercizio_doc_amm(), rigaMandato.getPg_doc_amm()));
		} else
			partita = new Partita(rigaMandato.getCd_tipo_documento_amm(), rigaMandato.getCd_cds_doc_amm(), rigaMandato.getCd_uo_doc_amm(), rigaMandato.getEsercizio_doc_amm(), rigaMandato.getPg_doc_amm(), rigaMandato.getCd_terzo(),
					TipoDocumentoEnum.fromValue(rigaMandato.getCd_tipo_documento_amm()));

		Voce_epBulk voceEpBanca = this.findContoBanca(userContext, CNRUserContext.getEsercizio(userContext));

		List<Movimento_cogeBulk> movimenti = this.findMovimentiPrimaNota(userContext, partita);
		List<Movimento_cogeBulk> dettPnPatrimonialePartita = this.findMovimentiPatrimoniali(movimenti,partita);

		final BigDecimal totContiEp = BigDecimal.valueOf(dettPnPatrimonialePartita.stream()
						.mapToDouble(el->el.getIm_movimento().doubleValue())
						.sum());

		BigDecimal imDaRipartire = imNettoMandato;
		//Chiudo i conti in percentuale
		for (Iterator<Movimento_cogeBulk> i = dettPnPatrimonialePartita.iterator(); i.hasNext(); ) {
			Movimento_cogeBulk dettPN= i.next();
			BigDecimal imRiga = imNettoMandato.multiply(dettPN.getIm_movimento()).divide(totContiEp,2, RoundingMode.HALF_EVEN);
			if (imRiga.compareTo(imDaRipartire)>0 || !i.hasNext()) {
				testataPrimaNota.addDettaglio(partita.getTipoDocumentoEnum().getTipoPatrimoniale(), Movimento_cogeBulk.getControSezione(partita.getTipoDocumentoEnum().getSezionePatrimoniale()),
						dettPN.getCd_voce_ep(), imDaRipartire, partita, rigaMandato.getCd_terzo(), null);
				break;
			}
			testataPrimaNota.addDettaglio(partita.getTipoDocumentoEnum().getTipoPatrimoniale(), Movimento_cogeBulk.getControSezione(partita.getTipoDocumentoEnum().getSezionePatrimoniale()),
					dettPN.getCd_voce_ep(), imRiga, partita, rigaMandato.getCd_terzo(), null);
			imDaRipartire = imDaRipartire.subtract(imRiga);
		}
		testataPrimaNota.addDettaglio(Movimento_cogeBulk.TipoRiga.TESORERIA.value(), partita.getTipoDocumentoEnum().getSezionePatrimoniale(), voceEpBanca.getCd_voce_ep(), imNettoMandato);
	}

	private void addDettagliPrimaNotaReversaleDocumentiVari(UserContext userContext, TestataPrimaNota testataPrimaNota, Reversale_rigaBulk rigaReversale) throws ComponentException, PersistencyException, RemoteException {
		if (!TipoDocumentoEnum.fromValue(rigaReversale.getCd_tipo_documento_amm()).isDocumentoAmministrativoAttivo())
			throw new ApplicationException("La riga della reversale " + rigaReversale.getEsercizio() + "/" + rigaReversale.getCd_cds() + "/" + rigaReversale.getPg_reversale() +
					" non risulta pagare un documento. Proposta di prima nota non possibile.");

		BigDecimal imReversale = rigaReversale.getIm_reversale_riga();

		Voce_epBulk voceEpBanca = this.findContoBanca(userContext, CNRUserContext.getEsercizio(userContext));

		//La partita non deve essere registrata in caso di versamento ritenute
		IDocumentoCogeBulk partita;
		if (TipoDocumentoEnum.fromValue(rigaReversale.getCd_tipo_documento_amm()).isGenericoEntrata()) {
			partita = (Documento_generico_attivoBulk) getHome(userContext, Documento_generico_attivoBulk.class).findByPrimaryKey(new Documento_generico_attivoBulk(rigaReversale.getCd_cds_doc_amm(), rigaReversale.getCd_tipo_documento_amm(), rigaReversale.getCd_uo_doc_amm(), rigaReversale.getEsercizio_doc_amm(), rigaReversale.getPg_doc_amm()));
		} else
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

	private Voce_epBulk findContoIvaDebito(UserContext userContext, TipoDocumentoEnum tipoDocumento) throws ComponentException, RemoteException, PersistencyException {
		Configurazione_cnrBulk configIva = Utility.createConfigurazioneCnrComponentSession().getConfigurazione(userContext, null, null, Configurazione_cnrBulk.PK_CORI_SPECIALE, Configurazione_cnrBulk.SK_IVA);

		Ass_tipo_cori_voce_epHome aEffCoriHome = (Ass_tipo_cori_voce_epHome) getHome(userContext, Ass_tipo_cori_voce_epBulk.class);
		Ass_tipo_cori_voce_epBulk aEffCori = aEffCoriHome.getAssCoriEp(CNRUserContext.getEsercizio(userContext), configIva.getVal01(), Contributo_ritenutaBulk.TIPO_ENTE, tipoDocumento.getSezioneEconomica());

		return Optional.ofNullable(aEffCori).flatMap(el -> Optional.ofNullable(el.getVoce_ep_contr())).filter(el -> Optional.ofNullable(el.getCd_voce_ep()).isPresent())
				.orElseThrow(() -> new ApplicationRuntimeException("Conto ep di contropartita non trovato (Esercizio: " + CNRUserContext.getEsercizio(userContext) +
						" - Codice Contributo: " + configIva.getVal01() + " - Tipe E/P: " + Contributo_ritenutaBulk.TIPO_ENTE + " - Sezione: " + tipoDocumento.getSezioneEconomica() + ")"));
	}

	private Voce_epBulk findContoIvaCredito(UserContext userContext, TipoDocumentoEnum tipoDocumento) throws ComponentException, RemoteException, PersistencyException {
		Configurazione_cnrBulk configIva = Utility.createConfigurazioneCnrComponentSession().getConfigurazione(userContext, null, null, Configurazione_cnrBulk.PK_CORI_SPECIALE, Configurazione_cnrBulk.SK_IVA);

		Ass_tipo_cori_voce_epHome aEffCoriHome = (Ass_tipo_cori_voce_epHome) getHome(userContext, Ass_tipo_cori_voce_epBulk.class);
		Ass_tipo_cori_voce_epBulk aEffCori = aEffCoriHome.getAssCoriEp(CNRUserContext.getEsercizio(userContext), configIva.getVal01(), Contributo_ritenutaBulk.TIPO_ENTE, tipoDocumento.getSezioneEconomica());

		return Optional.ofNullable(aEffCori).flatMap(el -> Optional.ofNullable(el.getVoce_ep())).filter(el -> Optional.ofNullable(el.getCd_voce_ep()).isPresent())
				.orElseThrow(() -> new ApplicationRuntimeException("Conto ep di contropartita non trovato (Esercizio: " + CNRUserContext.getEsercizio(userContext) +
						" - Codice Contributo: " + configIva.getVal01() + " - Tipe E/P: " + Contributo_ritenutaBulk.TIPO_ENTE + " - Sezione: " + tipoDocumento.getSezioneEconomica() + ")"));
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

	private Voce_epBulk findContoBanca(UserContext userContext, int esercizio) throws ComponentException, RemoteException, PersistencyException {
		Configurazione_cnrBulk configBanca = Utility.createConfigurazioneCnrComponentSession().getConfigurazione(userContext, esercizio, null, Configurazione_cnrBulk.PK_VOCEEP_SPECIALE, Configurazione_cnrBulk.SK_BANCA);

		return Optional.ofNullable(configBanca).flatMap(el->Optional.ofNullable(el.getVal01())).map(el->{
			try {
				Voce_epHome voceEpHome = (Voce_epHome) getHome(userContext, Voce_epBulk.class);
				return (Voce_epBulk) voceEpHome.findByPrimaryKey(new Voce_epBulk(configBanca.getVal01(), esercizio));
			} catch(ComponentException|PersistencyException ex) {
				throw new DetailedRuntimeException(ex);
			}
		}).orElseThrow(()->new ApplicationException("Manca la configurazione del conto BANCA nella tabella CONFIGURAZIONE_CNR per l'esercizio "+esercizio
			+" ("+Configurazione_cnrBulk.PK_VOCEEP_SPECIALE+"-"+Configurazione_cnrBulk.SK_BANCA+"-VAL01)"));
	}

	private Pair<Voce_epBulk, Voce_epBulk> findPairContiAnticipo(UserContext userContext, AnticipoBulk anticipo) throws ComponentException, RemoteException, PersistencyException {
		Configurazione_cnrBulk configBanca = Utility.createConfigurazioneCnrComponentSession().getConfigurazione(userContext, anticipo.getEsercizio(), null, Configurazione_cnrBulk.PK_VOCEEP_SPECIALE, Configurazione_cnrBulk.SK_CREDITO_DEBITO_ANTICIPO);

		Voce_epBulk aContoCreditoAnticipo = Optional.ofNullable(configBanca).flatMap(el->Optional.ofNullable(el.getVal01())).map(el->{
			try {
				Voce_epHome voceEpHome = (Voce_epHome) getHome(userContext, Voce_epBulk.class);
				return (Voce_epBulk) voceEpHome.findByPrimaryKey(new Voce_epBulk(configBanca.getVal01(), anticipo.getEsercizio()));
			} catch(ComponentException|PersistencyException ex) {
				throw new DetailedRuntimeException(ex);
			}
		}).orElseThrow(()->new ApplicationException("Manca la configurazione del conto CREDITO anticipo nella tabella CONFIGURAZIONE_CNR per l'esercizio "+anticipo.getEsercizio()
				+" ("+Configurazione_cnrBulk.PK_VOCEEP_SPECIALE+"-"+Configurazione_cnrBulk.SK_CREDITO_DEBITO_ANTICIPO+"-VAL01)"));

		Voce_epBulk aContoDebitoAnticipo = Optional.ofNullable(configBanca).flatMap(el->Optional.ofNullable(el.getVal02())).map(el->{
			try {
				Voce_epHome voceEpHome = (Voce_epHome) getHome(userContext, Voce_epBulk.class);
				return (Voce_epBulk) voceEpHome.findByPrimaryKey(new Voce_epBulk(configBanca.getVal02(), anticipo.getEsercizio()));
			} catch(ComponentException|PersistencyException ex) {
				throw new DetailedRuntimeException(ex);
			}
		}).orElseThrow(()->new ApplicationException("Manca la configurazione del conto DEBITO anticipo nella tabella CONFIGURAZIONE_CNR per l'esercizio "+anticipo.getEsercizio()
				+" ("+Configurazione_cnrBulk.PK_VOCEEP_SPECIALE+"-"+Configurazione_cnrBulk.SK_CREDITO_DEBITO_ANTICIPO+"-VAL02)"));

		return Pair.of(aContoCreditoAnticipo, aContoDebitoAnticipo);

	}

	private Pair<Voce_epBulk, Voce_epBulk> findPairCosto(UserContext userContext, AnticipoBulk anticipo) throws ComponentException, RemoteException, PersistencyException {
		return this.findPairCosto(userContext, Optional.ofNullable(anticipo.getTerzo()).orElse(new TerzoBulk(anticipo.getCd_terzo())), anticipo.getScadenza_obbligazione().getObbligazione(), Movimento_cogeBulk.TipoRiga.DEBITO.value());
	}

	private Pair<Voce_epBulk, Voce_epBulk> findPairCosto(UserContext userContext, MissioneBulk missione) throws ComponentException, RemoteException, PersistencyException {
		return this.findPairCosto(userContext, Optional.ofNullable(missione.getTerzo()).orElse(new TerzoBulk(missione.getCd_terzo())), missione.getObbligazione_scadenzario().getObbligazione(), Movimento_cogeBulk.TipoRiga.DEBITO.value());
	}

	private Pair<Voce_epBulk, Voce_epBulk> findPairCosto(UserContext userContext, CompensoBulk compenso) throws ComponentException, RemoteException, PersistencyException {
		return this.findPairCosto(userContext, Optional.ofNullable(compenso.getTerzo()).orElse(new TerzoBulk(compenso.getCd_terzo())), compenso.getObbligazioneScadenzario().getObbligazione(), Movimento_cogeBulk.TipoRiga.DEBITO.value());
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

	private Pair<Voce_epBulk, Voce_epBulk> findPairIva(UserContext userContext, DettaglioFinanziario dettaglioFinanziario) throws ComponentException, RemoteException, PersistencyException {
		Voce_epBulk aContoIvaDebito = this.findContoIvaDebito(userContext, dettaglioFinanziario.getDocamm().getTipoDocumentoEnum());
		Voce_epBulk aContoContropartita = this.findContoCostoRicavo(userContext, dettaglioFinanziario.getElementoVoce());
		return Pair.of(aContoIvaDebito, aContoContropartita);
	}

	private Pair<Voce_epBulk, Voce_epBulk> findPairAutofatturaIva(UserContext userContext, DettaglioFinanziario dettaglioFinanziario) throws ComponentException, RemoteException, PersistencyException {
		Voce_epBulk aContoIvaDebito = this.findContoIvaDebito(userContext, dettaglioFinanziario.getDocamm().getTipoDocumentoEnum());
		Voce_epBulk aContoIvaCredito = this.findContoIvaCredito(userContext, dettaglioFinanziario.getDocamm().getTipoDocumentoEnum());
		return Pair.of(aContoIvaDebito, aContoIvaCredito);
	}

	private Pair<Voce_epBulk, Voce_epBulk> findPairCosto(UserContext userContext, DettaglioFinanziario dettaglioFinanziario) throws ComponentException, RemoteException, PersistencyException {
		return this.findPairCosto(userContext, new TerzoBulk(dettaglioFinanziario.getCdTerzo()), dettaglioFinanziario.getElementoVoce(),
				dettaglioFinanziario.getDocamm().getTipoDocumentoEnum().isDocumentoAmministrativoAttivo()?Movimento_cogeBulk.TipoRiga.CREDITO.value() : Movimento_cogeBulk.TipoRiga.DEBITO.value());
	}

	private Pair<Voce_epBulk, Voce_epBulk> findPairCosto(UserContext userContext, TerzoBulk terzo, Elemento_voceBulk elementoVoce, String tipoContoPatrimoniale) throws ComponentException, RemoteException, PersistencyException {
		Voce_epBulk aContoCosto = this.findContoCostoRicavo(userContext, elementoVoce);
		Voce_epBulk aContoContropartita = this.findContoAnag(userContext, terzo, elementoVoce, tipoContoPatrimoniale);
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

	private Pair<Voce_epBulk, Voce_epBulk> findPairRicavoCompenso(UserContext userContext, Contributo_ritenutaBulk cori) throws ComponentException, PersistencyException, RemoteException {
		AccertamentoHome accertamentohome = (AccertamentoHome) getHome(userContext, AccertamentoBulk.class);
		AccertamentoBulk accertamentoBulk = (AccertamentoBulk) accertamentohome.findByPrimaryKey(new AccertamentoBulk(cori.getCd_cds_accertamento(), cori.getEsercizio_accertamento(), cori.getEsercizio_ori_accertamento(), cori.getPg_accertamento()));
		Voce_epBulk aContoCosto = this.findContoCostoRicavo(userContext, new Elemento_voceBulk(accertamentoBulk.getCd_elemento_voce(), accertamentoBulk.getEsercizio(), accertamentoBulk.getTi_appartenenza(), accertamentoBulk.getTi_gestione()));
		Voce_epBulk aContoContropartita = this.findContoAnag(userContext, cori.getCompenso().getTerzo(), new Elemento_voceBulk(accertamentoBulk.getCd_elemento_voce(), accertamentoBulk.getEsercizio(), accertamentoBulk.getTi_appartenenza(), accertamentoBulk.getTi_gestione()), Movimento_cogeBulk.TipoRiga.CREDITO.value());
		return Pair.of(aContoCosto, aContoContropartita);
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

	private Voce_epBulk findContoAnag(UserContext userContext, TerzoBulk terzo, Elemento_voceBulk voceBilancio, String tipoConto) throws ComponentException, RemoteException, PersistencyException {
		Configurazione_cnrBulk config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione(userContext, CNRUserContext.getEsercizio(userContext), null, Configurazione_cnrBulk.PK_ECONOMICO_PATRIMONIALE, Configurazione_cnrBulk.SK_ASSOCIAZIONE_CONTI);

		if (Optional.ofNullable(config).filter(el->el.getVal01().equals("TERZO")).isPresent())
			return findContoAnag(userContext, terzo, tipoConto);
		else if (Optional.ofNullable(config).filter(el->el.getVal01().equals("CONTO")).isPresent()) {
			Configurazione_cnrBulk configTipoEP = Utility.createConfigurazioneCnrComponentSession().getConfigurazione(userContext, CNRUserContext.getEsercizio(userContext), null, Configurazione_cnrBulk.PK_ECONOMICO_PATRIMONIALE, Configurazione_cnrBulk.SK_TIPO_ECONOMICO_PATRIMONIALE);
			if (Optional.ofNullable(configTipoEP).filter(el->el.getVal01().equals("PARALLELA")).isPresent())
				return findContoAnag(userContext, voceBilancio);
			return findContoAnag(userContext, voceBilancio);
		}
		throw new ApplicationRuntimeException("Manca la configurazione del tipo proposta conto debito/credito (Tabella CONFIGURAZIONE_CNR - Chiave Primaria: "+Configurazione_cnrBulk.PK_ECONOMICO_PATRIMONIALE+" - Chiave Secondaria: "+Configurazione_cnrBulk.SK_ASSOCIAZIONE_CONTI);
	}

	private Voce_epBulk findContoAnag(UserContext userContext, Elemento_voceBulk voceBilancio) throws ComponentException, PersistencyException {
		return Optional.ofNullable(this.findAssEvVoceep(userContext, voceBilancio).getVoce_ep_contr())
				.orElseThrow(()->new ApplicationRuntimeException("Conto di contropartita mancante in associazione con la voce del piano finanziario " +
						voceBilancio.getEsercizio() + "/" + voceBilancio.getTi_appartenenza() + "/"  +
						voceBilancio.getTi_gestione() + "/" +
						voceBilancio.getCd_elemento_voce() + "."));
	}

	private Voce_epBulk findContoAnag(UserContext userContext, TerzoBulk terzo, String tipoConto) throws ComponentException, PersistencyException {
		Voce_epBulk voceEpBulk = null;

		TerzoHome terzohome = (TerzoHome) getHome(userContext, TerzoBulk.class);
		TerzoBulk terzoDB = (TerzoBulk) terzohome.findByPrimaryKey(terzo);

		if (Optional.ofNullable(terzo).isPresent()) {
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
		}
		return Optional.ofNullable(voceEpBulk)
				.orElseThrow(()->new ApplicationRuntimeException("Conto " +
						(tipoConto.equals(Movimento_cogeBulk.TipoRiga.DEBITO.value()) ? "debito" : "credito") +
						" associato al codice terzo " + terzo.getCd_terzo() + " non individuato."));
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
				.filter(el->docamm.getTipoDocumentoEnum().getSezionePatrimoniale().equals(el.getSezione()))
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
				.filter(el->Movimento_cogeBulk.getControSezione(docamm.getTipoDocumentoEnum().getSezioneIva()).equals(el.getSezione()))
				.filter(el-> (docamm.getFl_split_payment()?Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO_SPLIT.value():Movimento_cogeBulk.TipoRiga.IVA_ACQUISTO.value())
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
			throw new ApplicationException("Errore nell'individuazione del singolo movimento di prima nota di apertura credito dell'anticipo' "
					+docamm.getCd_tipo_doc()+"/"+docamm.getEsercizio()+"/"+docamm.getPg_doc()+": sono state individuate troppe righe.");
		return result.stream().findFirst()
				.orElseThrow(()->new ApplicationRuntimeException("Errore nell'individuazione del singolo movimento di prima nota di apertura credito dell'anticipo "
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

		testataPrimaNota.forEach(testata->{
			if (accorpaConti) {
				//Prima analizzo i conti patrimoniali con partita senza cori
				//I conti patrimoniali devono essere accorpati per partita e distinti tra modificabili e non
				Map<IDocumentoCogeBulk, Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>>> mapPartitePatrimonialiNoCori =
						testata.getDett().stream().filter(DettaglioPrimaNota::isDettaglioPatrimoniale)
						.filter(el->Optional.ofNullable(el.getPartita()).isPresent())
						.filter(el->!Optional.ofNullable(el.getCdCori()).isPresent())
						.collect(Collectors.groupingBy(DettaglioPrimaNota::getPartita,
								Collectors.groupingBy(DettaglioPrimaNota::getTipoDett,
										Collectors.groupingBy(DettaglioPrimaNota::isModificabile, Collectors.groupingBy(DettaglioPrimaNota::getCdConto)))));

				mapPartitePatrimonialiNoCori.keySet().forEach(aPartita -> {
					Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>> mapTipoDettPatrimoniali = mapPartitePatrimonialiNoCori.get(aPartita);
					mapTipoDettPatrimoniali.keySet().forEach(aTipoDett -> {
						Map<Boolean, Map<String, List<DettaglioPrimaNota>>> mapModificabile = mapTipoDettPatrimoniali.get(aTipoDett);
						mapModificabile.keySet().forEach(aTipoModific -> {
							Map<String, List<DettaglioPrimaNota>> mapContiPatrimoniali = mapModificabile.get(aTipoModific);
							mapContiPatrimoniali.keySet().forEach(aContoPatrimoniale -> {
								try {
									Pair<String, BigDecimal> saldoPatrimoniale = this.getSaldo(mapContiPatrimoniali.get(aContoPatrimoniale));
									Movimento_cogeBulk movcoge = addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, saldoPatrimoniale.getFirst(), aContoPatrimoniale, saldoPatrimoniale.getSecond(), aPartita, null);
									Optional.ofNullable(movcoge).ifPresent(el->el.setFl_modificabile(aTipoModific));
								} catch (ComponentException e) {
									throw new ApplicationRuntimeException(e);
								}
							});
						});
					});
				});

				//Poi i conti patrimoniali con partita e cori distinti sempre tra modificabili e non
				Map<IDocumentoCogeBulk, Map<String, Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>>>> mapPartitePatrimonialiCori =
						testata.getDett().stream().filter(DettaglioPrimaNota::isDettaglioPatrimoniale)
								.filter(el->Optional.ofNullable(el.getPartita()).isPresent())
								.filter(el->Optional.ofNullable(el.getCdCori()).isPresent())
								.collect(Collectors.groupingBy(DettaglioPrimaNota::getPartita,
										Collectors.groupingBy(DettaglioPrimaNota::getCdCori,
											Collectors.groupingBy(DettaglioPrimaNota::getTipoDett,
												Collectors.groupingBy(DettaglioPrimaNota::isModificabile, Collectors.groupingBy(DettaglioPrimaNota::getCdConto))))));

				mapPartitePatrimonialiCori.keySet().forEach(aPartita -> {
					Map<String, Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>>> mapCoriPatrimoniali = mapPartitePatrimonialiCori.get(aPartita);
					mapCoriPatrimoniali.keySet().forEach(aCdCori -> {
						Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>> mapTipoDettPatrimoniali = mapCoriPatrimoniali.get(aCdCori);
						mapTipoDettPatrimoniali.keySet().forEach(aTipoDett -> {
							Map<Boolean, Map<String, List<DettaglioPrimaNota>>> mapModificabile = mapTipoDettPatrimoniali.get(aTipoDett);
							mapModificabile.keySet().forEach(aTipoModific -> {
								Map<String, List<DettaglioPrimaNota>> mapContiPatrimoniali = mapModificabile.get(aTipoModific);
								mapContiPatrimoniali.keySet().forEach(aContoPatrimoniale -> {
									try {
										Pair<String, BigDecimal> saldoPatrimoniale = this.getSaldo(mapContiPatrimoniali.get(aContoPatrimoniale));
										Movimento_cogeBulk movcoge = addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, saldoPatrimoniale.getFirst(), aContoPatrimoniale, saldoPatrimoniale.getSecond(), aPartita, aCdCori);
										Optional.ofNullable(movcoge).ifPresent(el->el.setFl_modificabile(aTipoModific));
									} catch (ComponentException e) {
										throw new ApplicationRuntimeException(e);
									}
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
									Collectors.groupingBy(DettaglioPrimaNota::isModificabile, Collectors.groupingBy(DettaglioPrimaNota::getCdConto))));

					mapTipoDettPatrimonialiSenzaPartita.keySet().forEach(aTipoDett -> {
						Map<Boolean, Map<String, List<DettaglioPrimaNota>>> mapModificabile = mapTipoDettPatrimonialiSenzaPartita.get(aTipoDett);
						mapModificabile.keySet().forEach(aTipoModific -> {
							Map<String, List<DettaglioPrimaNota>> mapContiPatrimonialiSenzaPartita = mapModificabile.get(aTipoModific);
							mapContiPatrimonialiSenzaPartita.keySet().forEach(aContoPatrimoniale -> {
								try {
									Pair<String, BigDecimal> saldoPatrimoniale = this.getSaldo(mapContiPatrimonialiSenzaPartita.get(aContoPatrimoniale));
									Movimento_cogeBulk movcoge = addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, saldoPatrimoniale.getFirst(), aContoPatrimoniale, saldoPatrimoniale.getSecond());
									Optional.ofNullable(movcoge).ifPresent(el->el.setFl_modificabile(aTipoModific));
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
									Collectors.groupingBy(DettaglioPrimaNota::isModificabile, Collectors.groupingBy(DettaglioPrimaNota::getCdConto))));

					mapTipoDettIva.keySet().forEach(aTipoDett -> {
						Map<Boolean, Map<String, List<DettaglioPrimaNota>>> mapModificabile = mapTipoDettIva.get(aTipoDett);
						mapModificabile.keySet().forEach(aTipoModific -> {
							Map<String, List<DettaglioPrimaNota>> mapContiIva = mapModificabile.get(aTipoModific);
							mapContiIva.keySet().forEach(aContoIva -> {
								try {
									Pair<String, BigDecimal> saldoIva = this.getSaldo(mapContiIva.get(aContoIva));
									Movimento_cogeBulk movcoge = addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, saldoIva.getFirst(), aContoIva, saldoIva.getSecond());
									Optional.ofNullable(movcoge).ifPresent(el->el.setFl_modificabile(aTipoModific));
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
									Collectors.groupingBy(DettaglioPrimaNota::isModificabile, Collectors.groupingBy(DettaglioPrimaNota::getCdConto))));

					mapTipoDettCostoRicavo.keySet().forEach(aTipoDett -> {
						Map<Boolean, Map<String, List<DettaglioPrimaNota>>> mapModificabile = mapTipoDettCostoRicavo.get(aTipoDett);
						mapModificabile.keySet().forEach(aTipoModific -> {
							Map<String, List<DettaglioPrimaNota>> mapContiCostoRicavo = mapModificabile.get(aTipoModific);
							mapContiCostoRicavo.keySet().forEach(aContoCostoRicavo -> {
								try {
									Pair<String, BigDecimal> saldoCostoRicavo = this.getSaldo(mapContiCostoRicavo.get(aContoCostoRicavo));
									Movimento_cogeBulk movcoge = addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, saldoCostoRicavo.getFirst(), aContoCostoRicavo, saldoCostoRicavo.getSecond());
									Optional.ofNullable(movcoge).ifPresent(el->el.setFl_modificabile(aTipoModific));
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
							.filter(el->!el.isDettaglioPatrimoniale())
							.filter(el->!el.isDettaglioIva())
							.filter(el->!el.isDettaglioCostoRicavo())
							.collect(Collectors.groupingBy(DettaglioPrimaNota::getTipoDett,
									Collectors.groupingBy(DettaglioPrimaNota::isModificabile, Collectors.groupingBy(DettaglioPrimaNota::getCdConto))));

					mapTipoDettAltro.keySet().forEach(aTipoDett -> {
						Map<Boolean, Map<String, List<DettaglioPrimaNota>>> mapModificabile = mapTipoDettAltro.get(aTipoDett);
						mapModificabile.keySet().forEach(aTipoModific -> {
							Map<String, List<DettaglioPrimaNota>> mapContiAltro = mapModificabile.get(aTipoModific);
							mapContiAltro.keySet().forEach(aContoAltro -> {
								try {
									Pair<String, BigDecimal> saldoCostoRicavo = this.getSaldo(mapContiAltro.get(aContoAltro));
									Movimento_cogeBulk movcoge = addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, saldoCostoRicavo.getFirst(), aContoAltro, saldoCostoRicavo.getSecond());
									Optional.ofNullable(movcoge).ifPresent(el->el.setFl_modificabile(aTipoModific));
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
					Map<IDocumentoCogeBulk, Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>>> mapPartite = testata.getDett().stream()
							.filter(el -> Optional.ofNullable(el.getPartita()).isPresent())
							.filter(el -> !Optional.ofNullable(el.getCdCori()).isPresent())
							.collect(Collectors.groupingBy(DettaglioPrimaNota::getPartita,
									Collectors.groupingBy(DettaglioPrimaNota::getTipoDett,
											Collectors.groupingBy(DettaglioPrimaNota::isModificabile, Collectors.groupingBy(DettaglioPrimaNota::getCdConto)))));

					mapPartite.keySet().forEach(aPartita -> {
						Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>> mapTipoDett = mapPartite.get(aPartita);
						mapTipoDett.keySet().forEach(aTipoDett -> {
							Map<Boolean, Map<String, List<DettaglioPrimaNota>>> mapModificabile = mapTipoDett.get(aTipoDett);
							mapModificabile.keySet().forEach(aTipoModific -> {
								Map<String, List<DettaglioPrimaNota>> mapConti = mapModificabile.get(aTipoModific);
								mapConti.keySet().forEach(aConto -> {
									try {
										BigDecimal imDare = mapConti.get(aConto).stream()
												.filter(DettaglioPrimaNota::isSezioneDare)
												.map(DettaglioPrimaNota::getImporto).reduce(BigDecimal.ZERO, BigDecimal::add);
										Optional.ofNullable(addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, Movimento_cogeBulk.SEZIONE_DARE, aConto, imDare, aPartita, null))
												.ifPresent(el->el.setFl_modificabile(aTipoModific));

										BigDecimal imAvere = mapConti.get(aConto).stream()
												.filter(DettaglioPrimaNota::isSezioneAvere)
												.map(DettaglioPrimaNota::getImporto).reduce(BigDecimal.ZERO, BigDecimal::add);
										Optional.ofNullable(addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, Movimento_cogeBulk.SEZIONE_AVERE, aConto, imAvere, aPartita, null))
												.ifPresent(el->el.setFl_modificabile(aTipoModific));
									} catch (ComponentException e) {
										throw new ApplicationRuntimeException(e);
									}
								});
							});
						});
					});
				}

				//Poi analizzo i conti con partita e cori
				//I conti devono essere accorpati per partita
				{
					Map<IDocumentoCogeBulk, Map<String, Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>>>> mapPartite = testata.getDett().stream()
							.filter(el -> Optional.ofNullable(el.getPartita()).isPresent())
							.filter(el -> Optional.ofNullable(el.getCdCori()).isPresent())
							.collect(Collectors.groupingBy(DettaglioPrimaNota::getPartita,
									Collectors.groupingBy(DettaglioPrimaNota::getCdCori,
										Collectors.groupingBy(DettaglioPrimaNota::getTipoDett,
											Collectors.groupingBy(DettaglioPrimaNota::isModificabile, Collectors.groupingBy(DettaglioPrimaNota::getCdConto))))));

					mapPartite.keySet().forEach(aPartita -> {
						Map<String, Map<String, Map<Boolean, Map<String, List<DettaglioPrimaNota>>>>> mapPartitePatrimonialiCori = mapPartite.get(aPartita);
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
											Optional.ofNullable(addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, Movimento_cogeBulk.SEZIONE_DARE, aConto, imDare, aPartita, aCdCori))
													.ifPresent(el->el.setFl_modificabile(aTipoModific));

											BigDecimal imAvere = mapConti.get(aConto).stream()
													.filter(DettaglioPrimaNota::isSezioneAvere)
													.map(DettaglioPrimaNota::getImporto).reduce(BigDecimal.ZERO, BigDecimal::add);
											Optional.ofNullable(addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, Movimento_cogeBulk.SEZIONE_AVERE, aConto, imAvere, aPartita, aCdCori))
													.ifPresent(el->el.setFl_modificabile(aTipoModific));
										} catch (ComponentException e) {
											throw new ApplicationRuntimeException(e);
										}
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
											.ifPresent(el->el.setFl_modificabile(aTipoModific));

									BigDecimal imAvere = mapConti.get(aConto).stream()
											.filter(DettaglioPrimaNota::isSezioneAvere)
											.map(DettaglioPrimaNota::getImporto).reduce(BigDecimal.ZERO, BigDecimal::add);
									Optional.ofNullable(addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, Movimento_cogeBulk.SEZIONE_AVERE, aConto, imAvere))
											.ifPresent(el->el.setFl_modificabile(aTipoModific));
								} catch (ComponentException e) {
									throw new ApplicationRuntimeException(e);
								}
							});
						});
					});
				}
			}
		});
		scritturaPartitaDoppia.setIm_scrittura( scritturaPartitaDoppia.getImTotaleAvere());
		return scritturaPartitaDoppia;
	}

	private Movimento_cogeBulk addMovimentoCoge(UserContext userContext, Scrittura_partita_doppiaBulk scritturaPartitaDoppia, IDocumentoCogeBulk doccoge, TestataPrimaNota testata, String aTipoDett, String aSezione, String aCdConto, BigDecimal aImporto) throws ComponentException {
		return this.addMovimentoCoge(userContext, scritturaPartitaDoppia, doccoge, testata, aTipoDett, aSezione, aCdConto, aImporto, null, null);
	}

	private Movimento_cogeBulk addMovimentoCoge(UserContext userContext, Scrittura_partita_doppiaBulk scritturaPartitaDoppia, IDocumentoCogeBulk doccoge, TestataPrimaNota testata, String aTipoDett, String aSezione, String aCdConto, BigDecimal aImporto, IDocumentoCogeBulk aPartita, String aCdCori) throws ComponentException{
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
					Optional.ofNullable(testata.getCdTerzo())
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
			if (docamm.getTipoDocumentoEnum().isDocumentoAmministrativoPassivo()) {
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
			}
			return false;
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	private boolean registraIvaCoge(UserContext usercontext, IDocumentoAmministrativoBulk docamm) {
		if (docamm.getTipoDocumentoEnum().isFatturaPassiva()) {
			Fattura_passivaBulk fatpas = (Fattura_passivaBulk) docamm;
			Optional.of(fatpas.getTipo_sezionale()).filter(el->el.getCrudStatus()==OggettoBulk.UNDEFINED).ifPresent(el->{
				try {
					fatpas.setTipo_sezionale((Tipo_sezionaleBulk) getHome(usercontext, Tipo_sezionaleBulk.class).findByPrimaryKey(fatpas.getTipo_sezionale()));
				} catch (ComponentException | PersistencyException e) {
					throw new DetailedRuntimeException(e);
				}
			});
			return fatpas.registraIvaCoge();
		} else if (docamm.getTipoDocumentoEnum().isFatturaAttiva()) {
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


}

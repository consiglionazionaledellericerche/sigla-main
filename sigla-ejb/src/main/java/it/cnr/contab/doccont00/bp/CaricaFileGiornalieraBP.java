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

package it.cnr.contab.doccont00.bp;


import it.cnr.contab.doccont00.intcass.giornaliera.*;
import it.cnr.contab.doccont00.intcass.giornaliera.FlussoGiornaleDiCassa.InformazioniContoEvidenza;
import it.cnr.contab.doccont00.intcass.giornaliera.FlussoGiornaleDiCassa.InformazioniContoEvidenza.MovimentoContoEvidenza;
import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util.ApplicationMessageFormatException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Optional;


public class CaricaFileGiornalieraBP extends BulkBP {
    private static final long serialVersionUID = 1L;

    public CaricaFileGiornalieraBP() {
        super();
    }

    public CaricaFileGiornalieraBP(String s) {
        super(s);
    }

    @Override
    public RemoteIterator find(ActionContext actioncontext,
                               CompoundFindClause compoundfindclause, OggettoBulk oggettobulk,
                               OggettoBulk oggettobulk1, String s) throws BusinessProcessException {
        return null;
    }

    @Override
    public void openForm(PageContext pagecontext, String action, String target,
                         String encType) throws IOException, ServletException {
        super.openForm(pagecontext, action, target, "multipart/form-data");
    }

    @Override
    protected void init(Config config, ActionContext actioncontext)
            throws BusinessProcessException {
        super.init(config, actioncontext);
        setModel(actioncontext, new FlussoGiornaleDiCassaBulk());
    }

    public void caricaFileSIOPE(ActionContext actioncontext, File file) throws BusinessProcessException, ComponentException, RemoteException {
        final DocumentiContabiliService documentiContabiliService = SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class);
        try {
            JAXBContext jc = JAXBContext.newInstance("it.cnr.si.siopeplus.giornaledicassa.custom");
            it.cnr.si.siopeplus.giornaledicassa.custom.ObjectFactory obj = new it.cnr.si.siopeplus.giornaledicassa.custom.ObjectFactory();
            it.cnr.si.siopeplus.giornaledicassa.FlussoGiornaleDiCassa flussoGiornaleDiCassa =
                    (it.cnr.si.siopeplus.giornaledicassa.FlussoGiornaleDiCassa) jc.createUnmarshaller().unmarshal(file);
            String identificativoFlusso = Optional.ofNullable(flussoGiornaleDiCassa.getIdentificativoFlussoBT())
                    .map(s -> s.substring(0, s.indexOf("#")))
                    .orElseThrow(() -> new ApplicationMessageFormatException("IdentificativoFlusso non trovato!"));
            documentiContabiliService.messaggioGiornaleDiCassa(flussoGiornaleDiCassa, identificativoFlusso);
        } catch (JAXBException e) {
            throw handleException(e);
        }
    }

    public void caricaFile(ActionContext actioncontext, File file) throws BusinessProcessException, ComponentException, RemoteException {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        JAXBContext jc;
        String versioneflussoGiornaliera = null;
        FlussoGiornaleDiCassa b;
        try {
            jc = JAXBContext
                    .newInstance("it.cnr.contab.doccont00.intcass.giornaliera");

            ObjectFactory obj = new ObjectFactory();
            b = (it.cnr.contab.doccont00.intcass.giornaliera.FlussoGiornaleDiCassa) jc.createUnmarshaller().unmarshal(file);

            FlussoGiornaleDiCassaBulk flusso = new FlussoGiornaleDiCassaBulk(b.getEsercizio(), b.getIdentificativoFlusso());
            flusso.setUser(actioncontext.getUserContext().getUser());
            flusso.setCodiceAbiBt(new Long(b.getCodiceABIBT()));
            flusso.setIdentificativoFlusso(b.getIdentificativoFlusso());
            flusso.setDataOraCreazioneFlusso(new Timestamp(b.getDataOraCreazioneFlusso().toGregorianCalendar().getTime().getTime()));
            flusso.setDataInizioPeriodoRif(new Timestamp(b.getDataInizioPeriodoRiferimento().toGregorianCalendar().getTime().getTime()));
            flusso.setDataFinePeriodoRif(new Timestamp(b.getDataFinePeriodoRiferimento().toGregorianCalendar().getTime().getTime()));
            flusso.setCodiceEnte(b.getCodiceEnte());
            flusso.setDescrizioneEnte(b.getDescrizioneEnte());
            flusso.setCodiceEnteBt(b.getCodiceEnteBT());
            flusso.setEsercizio(b.getEsercizio());
            for (int progressivo = 0; progressivo < b.getInformazioniContoEvidenza().size(); progressivo++) {
                InformazioniContoEvidenza info = b.getInformazioniContoEvidenza().get(progressivo);
                InformazioniContoEvidenzaBulk infoBulk = new InformazioniContoEvidenzaBulk(flusso.getEsercizio(), flusso.getIdentificativoFlusso(), info.getContoEvidenza());
                infoBulk.setDescrizioneContoEvidenza(info.getDescrizioneContoEvidenza());
                infoBulk.setSaldoPrecedenteContoEvid(info.getSaldoPrecedenteContoEvidenza());
                infoBulk.setTotaleEntrateContoEvidenza(info.getTotaleEntrateContoEvidenza());
                infoBulk.setTotaleUsciteContoEvidenza(info.getTotaleUsciteContoEvidenza());
                infoBulk.setSaldoFinaleContoEvidenza(info.getSaldoFinaleContoEvidenza());
                for (int progressivoMov = 0; progressivoMov < info.getMovimentoContoEvidenza().size(); progressivoMov++) {
                    MovimentoContoEvidenza mov = info.getMovimentoContoEvidenza().get(progressivoMov);
                    MovimentoContoEvidenzaBulk movBulk = new MovimentoContoEvidenzaBulk(flusso.getEsercizio(), flusso.getIdentificativoFlusso(), info.getContoEvidenza(), "I", new Long(progressivoMov + 1));
                    movBulk.setTipoMovimento(mov.getTipoMovimento());
                    movBulk.setTipoDocumento(mov.getTipoDocumento());
                    movBulk.setTipoOperazione(mov.getTipoOperazione());
                    movBulk.setTiPagamentoFunzDelegato(mov.getTipologiaPagamentoFunzionarioDelegato());
                    movBulk.setNumPagFunzDelegato(mov.getNumeroPagamentoFunzionarioDelegato());
                    movBulk.setNumeroDocumento(mov.getNumeroDocumento());
                    movBulk.setProgressivoDocumento(mov.getProgressivoDocumento().longValue());
                    movBulk.setImporto(mov.getImporto());
                    movBulk.setImportoRitenute(mov.getImportoRitenute());
                    if (mov.getNumeroBollettaQuietanza() != null)
                        movBulk.setNumeroBollettaQuietanza(mov.getNumeroBollettaQuietanza().toString());
                    if (mov.getNumeroBollettaQuietanzaStorno() != null)
                        movBulk.setNumeroBollettaQuietanzaS(mov.getNumeroBollettaQuietanzaStorno().toString());
                    movBulk.setDataMovimento(new Timestamp(mov.getDataMovimento().toGregorianCalendar().getTime().getTime()));
                    if (Optional.ofNullable(mov.getDataValutaEnte()).isPresent())
                        movBulk.setDataValutaEnte(new Timestamp(mov.getDataValutaEnte().toGregorianCalendar().getTime().getTime()));
                    movBulk.setTipoEsecuzione(mov.getTipoEsecuzione());
                    movBulk.setCoordinate(mov.getCoordinate());
                    movBulk.setCodiceRifOperazione(mov.getCodiceRiferimentoOperazione());
                    movBulk.setCodiceRifInterno(mov.getCodiceRiferimentoInterno());
                    movBulk.setTipoContabilita(mov.getTipoContabilita());
                    movBulk.setDestinazione(mov.getDestinazione());
                    movBulk.setAssoggettamentoBollo(mov.getAssoggettamentoBollo());
                    movBulk.setImportoBollo(mov.getImportoBollo());
                    movBulk.setAssoggettamentoSpese(mov.getAssoggettamentoSpese());
                    movBulk.setImportoSpese(mov.getImportoSpese());
                    movBulk.setAssoggettamentoCommissioni(mov.getAssoggettamentoCommissioni());
                    movBulk.setImportoCommissioni(mov.getImportoCommissioni());
                    if (mov.getCliente() != null) {
                        movBulk.setAnagraficaCliente(mov.getCliente().getAnagraficaCliente());
                        movBulk.setIndirizzoCliente(mov.getCliente().getIndirizzoCliente());
                        movBulk.setCapCliente(mov.getCliente().getCapCliente());
                        movBulk.setLocalitaCliente(mov.getCliente().getLocalitaCliente());
                        movBulk.setProvinciaCliente(mov.getCliente().getProvinciaCliente());
                        movBulk.setStatoCliente(mov.getCliente().getStatoCliente());
                        movBulk.setPartitaIvaCliente(mov.getCliente().getPartitaIvaCliente());
                        movBulk.setCodiceFiscaleCliente(mov.getCliente().getCodiceFiscaleCliente());
                    }
                    if (mov.getDelegato() != null) {
                        movBulk.setAnagraficaDelegato(mov.getDelegato().getAnagraficaDelegato());
                        movBulk.setIndirizzoDelegato(mov.getDelegato().getIndirizzoDelegato());
                        movBulk.setCapDelegato(mov.getDelegato().getCapDelegato());
                        movBulk.setLocalitaDelegato(mov.getDelegato().getLocalitaDelegato());
                        movBulk.setProvinciaDelegato(mov.getDelegato().getProvinciaDelegato());
                        movBulk.setStatoDelegato(mov.getDelegato().getStatoDelegato());
                        movBulk.setCodiceFiscaleDelegato(mov.getDelegato().getCodiceFiscaleDelegato());
                    }
                    if (mov.getCreditoreEffettivo() != null) {
                        movBulk.setAnagraficaCreditoreEff(mov.getCreditoreEffettivo().getAnagraficaCreditoreEffettivo());
                        movBulk.setIndirizzoCreditoreEff(mov.getCreditoreEffettivo().getIndirizzoCreditoreEffettivo());
                        movBulk.setCapCreditoreEff(mov.getCreditoreEffettivo().getCapCreditoreEffettivo());
                        movBulk.setLocalitaCreditoreEff(mov.getCreditoreEffettivo().getLocalitaCreditoreEffettivo());
                        movBulk.setProvinciaCreditoreEff(mov.getCreditoreEffettivo().getProvinciaCreditoreEffettivo());
                        movBulk.setStatoCreditoreEff(mov.getCreditoreEffettivo().getStatoCreditoreEffettivo());
                        movBulk.setPartitaIvaCreditoreEff(mov.getCreditoreEffettivo().getPartitaIvaCreditoreEffettivo());
                        movBulk.setCodiceFiscaleCreditoreEff(mov.getCreditoreEffettivo().getCodiceFiscaleCreditoreEffettivo());
                    }
                    movBulk.setCausale(mov.getCausale());
                    movBulk.setNumeroSospeso(mov.getNumeroSospeso());
                    movBulk.setToBeCreated();
                    infoBulk.addToMovConto(movBulk);
                }
                infoBulk.setToBeCreated();
                flusso.addToInfoConto(infoBulk);
            }
            flusso.setSaldoComplessivoPrec(b.getSaldoComplessivoPrecedente());
            flusso.setTotaleComplessivoEntrate(b.getTotaleComplessivoEntrate());
            flusso.setTotaleComplessivoUscite(b.getTotaleComplessivoUscite());
            flusso.setSaldoComplessivoFinale(b.getSaldoComplessivoFinale());
            if (b.getTotaliEsercizio() != null) {
                flusso.setFondoDiCassa(b.getTotaliEsercizio().getFondoDiCassa());
                flusso.setTotaleReversaliRiscosse(b.getTotaliEsercizio().getTotaleReversaliRiscosse());
                flusso.setTotaleSospesiEntrata(b.getTotaliEsercizio().getTotaleSospesiEntrata());
                flusso.setTotaleEntrate(b.getTotaliEsercizio().getTotaleEntrate());
                flusso.setDeficitDiCassa(b.getTotaliEsercizio().getDeficitDiCassa());
                flusso.setTotaleMandatiPagati(b.getTotaliEsercizio().getTotaleMandatiPagati());
                flusso.setTotaleSospesiUscita(b.getTotaliEsercizio().getTotaleSospesiUscita());
                flusso.setTotaleUscite(b.getTotaliEsercizio().getTotaleUscite());
                flusso.setSaldoEsercizio(b.getTotaliEsercizio().getSaldoEsercizio());
            }
            if (b.getTotaliDisponibilitaLiquide() != null) {
                flusso.setSaldoContiCorrenti(b.getTotaliDisponibilitaLiquide().getSaldoContiCorrenti());
                flusso.setSaldoContiBi(b.getTotaliDisponibilitaLiquide().getSaldoContiBI());
                flusso.setTotaleConti(b.getTotaliDisponibilitaLiquide().getTotaleConti());
                flusso.setVincoliContiCorrenti(b.getTotaliDisponibilitaLiquide().getVincoliContiCorrenti());
                flusso.setVincoliContiBi(b.getTotaliDisponibilitaLiquide().getVincoliContiBI());
                flusso.setTotaleVincoli(b.getTotaliDisponibilitaLiquide().getTotaleVincoli());
                flusso.setAnticipazioneAccordata(b.getTotaliDisponibilitaLiquide().getAnticipazioneAccordata());
                flusso.setAnticipazioneUtilizzata(b.getTotaliDisponibilitaLiquide().getAnticipazioneUtilizzata());
                flusso.setDisponibilita(b.getTotaliDisponibilitaLiquide().getDisponibilita());
            }
            flusso.setToBeCreated();
            flusso = (FlussoGiornaleDiCassaBulk) ((CRUDComponentSession) createComponentSession("JADAEJB_CRUDComponentSession", CRUDComponentSession.class))
                    .creaConBulk(actioncontext.getUserContext(false), flusso);

        } catch (UnmarshalException e) {
            throw new ApplicationException("Conversione file non riuscita");
        } catch (JAXBException e) {
            throw handleException(e);
        }

    }
}
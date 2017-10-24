package it.cnr.contab.ordmag.magazzino.comp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioHome;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagAzBulk;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagAzHome;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoRigaMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.LottoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagHome;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.PersistencyException;

public class MovimentiMagComponent
        extends it.cnr.jada.comp.CRUDComponent
        implements ICRUDMgr, Cloneable, Serializable {

    public final static String TIPO_TOTALE_COMPLETO = "C";
    public final static String TIPO_TOTALE_PARZIALE = "P";
    private final static int INSERIMENTO = 1;
    private final static int MODIFICA = 2;
    private final static int CANCELLAZIONE = 3;

    public MovimentiMagComponent() {

        /*Default constructor*/


    }
	public List<MovimentiMagBulk> caricoDaOrdine(UserContext userContext, EvasioneOrdineBulk evasioneOrdine,
			OrdineAcqConsegnaBulk consegna, OrdineAcqBulk ordine, EvasioneOrdineRigaBulk evasioneOrdineRiga, 
			List<MovimentiMagBulk> listaMovimentiScarico)
			throws ComponentException, PersistencyException, ApplicationException {
		MovimentiMagHome homeMag = (MovimentiMagHome)getHome(userContext, MovimentiMagBulk.class);
		MovimentiMagBulk movimentoMag = new MovimentiMagBulk();
		
		Bene_servizioBulk bene = recuperoBeneServizio(userContext, consegna.getOrdineAcqRiga().getCdBeneServizio());
		
		if (bene.getFlScadenza() != null && bene.getFlScadenza() && consegna.getDtScadenza() == null){
			throw new ApplicationException("Indicare la data di scadenza per la consegna "+consegna.getConsegnaOrdineString());
		}
		
		movimentoMag.setBeneServizio(consegna.getOrdineAcqRiga().getBeneServizio());
		movimentoMag.setDataBolla(evasioneOrdine.getDataBolla());
		movimentoMag.setDivisa(ordine.getDivisa());
		movimentoMag.setCambio(ordine.getCambio());
		movimentoMag.setDtMovimento(new Timestamp(System.currentTimeMillis()));
		movimentoMag.setDtRiferimento(evasioneOrdine.getDataConsegna());
		movimentoMag.setMagazzino(evasioneOrdine.getNumerazioneMag().getMagazzino());
		movimentoMag.setOrdineAcqConsegna(consegna);
		movimentoMag.setUnitaOperativaOrd(consegna.getUnitaOperativaOrd());
		movimentoMag.setPrezzoUnitario(consegna.getOrdineAcqRiga().getPrezzoUnitario());
		movimentoMag.setQuantita(evasioneOrdineRiga.getQuantitaEvasa());
		movimentoMag.setSconto1(consegna.getOrdineAcqRiga().getSconto1());
		movimentoMag.setSconto2(consegna.getOrdineAcqRiga().getSconto2());
		movimentoMag.setSconto3(consegna.getOrdineAcqRiga().getSconto3());
		movimentoMag.setLottoFornitore(consegna.getLottoFornitore());
		movimentoMag.setDtScadenza(consegna.getDtScadenza());
		movimentoMag.setStato(MovimentiMagBulk.STATO_INSERITO);
		movimentoMag.setTerzo(ordine.getFornitore());
		TipoMovimentoMagBulk tipoMovimento = null;
		
		switch (consegna.getTipoConsegna()) {
			case "MAG":  tipoMovimento = evasioneOrdine.getNumerazioneMag().getMagazzino().getTipoMovimentoMagCarMag();
			break;
			case "TRA":  tipoMovimento = evasioneOrdine.getNumerazioneMag().getMagazzino().getTipoMovimentoMagCarTra();
			break;
			case "FMA":  tipoMovimento = evasioneOrdine.getNumerazioneMag().getMagazzino().getTipoMovimentoMagCarFma();
			break;
		}

		movimentoMag.setTipoMovimentoMag(tipoMovimento);
		
		TipoMovimentoMagAzBulk tipoMovimentoAz = new TipoMovimentoMagAzBulk(tipoMovimento.getCdCds(), tipoMovimento.getCdTipoMovimento());
		
		TipoMovimentoMagAzHome home = (TipoMovimentoMagAzHome)getHome(userContext, TipoMovimentoMagAzBulk.class);
		tipoMovimentoAz = (TipoMovimentoMagAzBulk)home.findByPrimaryKey(tipoMovimentoAz);
		
		LottoMagBulk lotto = new LottoMagBulk();
		lotto.setToBeCreated();
		lotto.setBeneServizio(movimentoMag.getBeneServizio());
		lotto.setDivisa(movimentoMag.getDivisa());
		lotto.setCambio(movimentoMag.getCambio());
		lotto.setDtCarico(tipoMovimentoAz.getAggDataUltimoCarico().equals("S") ? movimentoMag.getDtRiferimento() : null);
		lotto.setQuantitaCarico(tipoMovimentoAz.getAggDataUltimoCarico().equals("S") ? movimentoMag.getQuantita() : null);
		
		if (!consegna.isConsegnaMagazzino()){
			lotto.setGiacenza(BigDecimal.ZERO);
			lotto.setQuantitaValore(BigDecimal.ZERO);
		} else {
			switch (tipoMovimentoAz.getModAggQtaMagazzino()) {
				case TipoMovimentoMagAzBulk.AZIONE_AZZERA:  lotto.setGiacenza(BigDecimal.ZERO);
				break;
				case TipoMovimentoMagAzBulk.AZIONE_SOMMA:  lotto.setGiacenza(Utility.nvl(lotto.getGiacenza()).add(movimentoMag.getQuantita()));
				break;
				case TipoMovimentoMagAzBulk.AZIONE_SOSTITUISCE:  lotto.setGiacenza(Utility.nvl(lotto.getGiacenza()).add(movimentoMag.getQuantita()));
				break;
				case TipoMovimentoMagAzBulk.AZIONE_SOTTRAE:  lotto.setGiacenza(movimentoMag.getQuantita());
				break;
			}
			switch (tipoMovimentoAz.getModAggQtaValMagazzino()) {
				case TipoMovimentoMagAzBulk.AZIONE_AZZERA:  lotto.setQuantitaValore(BigDecimal.ZERO);
				break;
				case TipoMovimentoMagAzBulk.AZIONE_SOMMA:  lotto.setQuantitaValore(Utility.nvl(lotto.getQuantitaValore()).add(movimentoMag.getQuantita()));
				break;
				case TipoMovimentoMagAzBulk.AZIONE_SOSTITUISCE:  lotto.setQuantitaValore(Utility.nvl(lotto.getQuantitaValore()).add(movimentoMag.getQuantita()));
				break;
				case TipoMovimentoMagAzBulk.AZIONE_SOTTRAE:  lotto.setQuantitaValore(movimentoMag.getQuantita());
				break;
			}
		}
		switch (tipoMovimentoAz.getModAggValoreLotto()) {
			case TipoMovimentoMagAzBulk.AZIONE_AZZERA:  lotto.setValoreUnitario(BigDecimal.ZERO);
			break;
			case TipoMovimentoMagAzBulk.AZIONE_SOMMA:  lotto.setValoreUnitario(Utility.nvl(lotto.getValoreUnitario()).add(movimentoMag.getPrezzoUnitario()));
			break;
			case TipoMovimentoMagAzBulk.AZIONE_SOSTITUISCE:  lotto.setValoreUnitario(Utility.nvl(lotto.getValoreUnitario()).add(movimentoMag.getPrezzoUnitario()));
			break;
			case TipoMovimentoMagAzBulk.AZIONE_SOTTRAE:  lotto.setValoreUnitario(movimentoMag.getPrezzoUnitario());
			break;
		}
		switch (tipoMovimentoAz.getModAggValoreLotto()) {
			case TipoMovimentoMagAzBulk.AZIONE_AZZERA:  lotto.setCostoUnitario(BigDecimal.ZERO);
			break;
			case TipoMovimentoMagAzBulk.AZIONE_SOMMA:  lotto.setCostoUnitario(Utility.nvl(lotto.getCostoUnitario()).add(movimentoMag.getPrezzoUnitario()));
			break;
			case TipoMovimentoMagAzBulk.AZIONE_SOSTITUISCE:  lotto.setCostoUnitario(Utility.nvl(lotto.getCostoUnitario()).add(movimentoMag.getPrezzoUnitario()));
			break;
			case TipoMovimentoMagAzBulk.AZIONE_SOTTRAE:  lotto.setCostoUnitario(movimentoMag.getPrezzoUnitario());
			break;
		}
		switch (tipoMovimentoAz.getModAggQtaInizioAnno()) {
			case TipoMovimentoMagAzBulk.AZIONE_AZZERA:  lotto.setQuantitaInizioAnno(BigDecimal.ZERO);
			break;
			case TipoMovimentoMagAzBulk.AZIONE_SOMMA:  lotto.setQuantitaInizioAnno(Utility.nvl(lotto.getQuantitaInizioAnno()).add(movimentoMag.getQuantita()));
			break;
			case TipoMovimentoMagAzBulk.AZIONE_SOSTITUISCE:  lotto.setQuantitaInizioAnno(Utility.nvl(lotto.getQuantitaInizioAnno()).add(movimentoMag.getQuantita()));
			break;
			case TipoMovimentoMagAzBulk.AZIONE_SOTTRAE:  lotto.setQuantitaValore(movimentoMag.getQuantita());
			break;
		}
		lotto.setLottoFornitore(tipoMovimentoAz.getRiportaLottoFornitore().equals("S") ? movimentoMag.getLottoFornitore() : null);
		lotto.setDtScadenza(movimentoMag.getDtScadenza());
		lotto.setEsercizio(CNRUserContext.getEsercizio(userContext));
		lotto.setMagazzino(movimentoMag.getMagazzino());
		lotto.setOrdineAcqConsegna(movimentoMag.getOrdineAcqConsegna()	);
		lotto.setDivisa(movimentoMag.getDivisa());
		lotto.setTerzo(movimentoMag.getTerzo());
		lotto.setStato(MovimentiMagBulk.STATO_INSERITO);
		lotto = (LottoMagBulk)super.creaConBulk(userContext, lotto);
		
		movimentoMag.setLottoMag(lotto);
		movimentoMag.setToBeCreated();
		MovimentiMagBulk movimentoMagScarico = null;
		if (!consegna.isConsegnaMagazzino()){
			movimentoMagScarico = (MovimentiMagBulk)movimentoMag.clone();
			movimentoMagScarico.setTipoMovimentoMag(tipoMovimentoAz.getTipoMovimentoMagRif());
			movimentoMag.setPgMovimento(homeMag.recuperoProgressivoMovimento(userContext));
			movimentoMagScarico.setPgMovimento(homeMag.recuperoProgressivoMovimento(userContext));
			movimentoMagScarico.setPgMovimentoRif(movimentoMag.getPgMovimento());
			movimentoMag.setPgMovimentoRif(movimentoMagScarico.getPgMovimento());
		}
		super.creaConBulk(userContext, movimentoMag);
		if (movimentoMagScarico != null){
			movimentoMagScarico = (MovimentiMagBulk)super.creaConBulk(userContext, movimentoMagScarico);
			listaMovimentiScarico.add(movimentoMagScarico);
		}
		return listaMovimentiScarico;
	}

    private Bene_servizioBulk recuperoBeneServizio(it.cnr.jada.UserContext userContext, String cdBeneServizio)
    		throws ComponentException, PersistencyException {
    	Bene_servizioHome home = (Bene_servizioHome)getHome(userContext, Bene_servizioBulk.class);
    	Bene_servizioBulk bene = (Bene_servizioBulk)home.findByPrimaryKey(new Bene_servizioBulk(cdBeneServizio));
    	return bene;
    }

    public List<BollaScaricoMagBulk> generaBollaScarico(UserContext userContext, List<MovimentiMagBulk> listaMovimentiScarico)
    				throws ComponentException, PersistencyException, ApplicationException {
    	if (!listaMovimentiScarico.isEmpty()){
    		List<BollaScaricoMagBulk> listaBolleScarico = new ArrayList<>();
    		for (MovimentiMagBulk movimento : listaMovimentiScarico){
    			BollaScaricoMagBulk bollaScarico = null;
    			for (BollaScaricoMagBulk bolla : listaBolleScarico){
    				if (bolla.getUnitaOperativaOrd().equalsByPrimaryKey(movimento.getUnitaOperativaOrd())){
    					bollaScarico = bolla;
    				}
    			}
    			if (bollaScarico == null){
    				bollaScarico = new BollaScaricoMagBulk();
    				bollaScarico.setDtBollaSca(movimento.getDtRiferimento());
    				bollaScarico.setMagazzino(movimento.getMagazzino());
    				bollaScarico.setStato(OrdineAcqBulk.STATO_INSERITO);
    				bollaScarico.setUnitaOperativaOrd(movimento.getUnitaOperativaOrd());
    				bollaScarico.setToBeCreated();
    			}

    			BollaScaricoRigaMagBulk riga = new BollaScaricoRigaMagBulk();
    			riga.setCoeffConv(movimento.getCoeffConv());
    			riga.setBeneServizio(movimento.getBeneServizio());
    			riga.setUnitaMisura(movimento.getUnitaMisura());
    			riga.setQuantita(movimento.getQuantita());
    			riga.setOrdineAcqConsegna(movimento.getOrdineAcqConsegna());
    			riga.setMovimentiMag(movimento);
    			riga.setLottoMag(movimento.getLottoMag());
    			riga.setToBeCreated();
    			bollaScarico.addToRighe(riga);
    			listaBolleScarico.add(bollaScarico);
    		}
    		for (BollaScaricoMagBulk bolla : listaBolleScarico){
    			bolla = (BollaScaricoMagBulk)super.creaConBulk(userContext, bolla);
    			for (MovimentiMagBulk movimento : listaMovimentiScarico){
    				if (movimento.getUnitaOperativaOrd().equalsByPrimaryKey(bolla.getUnitaOperativaOrd())){
    					movimento.setBollaScaricoMag(bolla);
    					movimento.setToBeUpdated();
    					super.modificaConBulk(userContext, movimento);
    				}
    			}
    		}
    		return listaBolleScarico;
    	}
    	return null;
    }
}

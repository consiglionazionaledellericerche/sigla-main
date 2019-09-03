package it.cnr.contab.ordmag.magazzino.comp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioHome;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaHome;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoHome;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagAzBulk;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagAzHome;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdHome;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagHome;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoRigaMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.LottoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.LottoMagHome;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagHome;
import it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoBulk;
import it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoHome;
import it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoRigaLottoBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
import it.cnr.contab.ordmag.ordini.dto.ImportoOrdine;
import it.cnr.contab.ordmag.ordini.dto.ParametriCalcoloImportoOrdine;
import it.cnr.contab.ordmag.ordini.ejb.OrdineAcqComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.RemoteIterator;

public class MovimentiMagComponent extends CRUDComponent implements ICRUDMgr, Cloneable, Serializable {
	private static final long serialVersionUID = 1L;

	public final static String TIPO_TOTALE_COMPLETO = "C";
    public final static String TIPO_TOTALE_PARZIALE = "P";

    private MovimentiMagBulk createMovimentoMagazzino(UserContext userContext, 
    		UnitaOperativaOrdBulk unitaOperativa, MagazzinoBulk magazzino, TipoMovimentoMagBulk tipoMovimento, 
    		Bene_servizioBulk beneServizio, java.math.BigDecimal quantitaBeneServizio, java.sql.Timestamp dataRiferimento,
    		DivisaBulk divisa, BigDecimal cambio, UnitaMisuraBulk unitaMisura, BigDecimal coeffConv) throws PersistencyException,ComponentException {
		MovimentiMagHome homeMag = (MovimentiMagHome)getHome(userContext, MovimentiMagBulk.class);
		
		MovimentiMagBulk movimentoMag = new MovimentiMagBulk();
		movimentoMag.setBeneServizioUt(beneServizio);
		movimentoMag.setQuantita(quantitaBeneServizio);
		movimentoMag.setDtMovimento(new Timestamp(System.currentTimeMillis()));
		movimentoMag.setDtRiferimento(dataRiferimento);
		movimentoMag.setMagazzinoUt(magazzino);
		movimentoMag.setUnitaOperativaOrd(unitaOperativa);
		movimentoMag.setTipoMovimentoMag(tipoMovimento);
		movimentoMag.setPgMovimento(homeMag.recuperoProgressivoMovimento(userContext));
		movimentoMag.setDivisa(divisa);
		movimentoMag.setCambio(cambio);
		movimentoMag.setUnitaMisura(unitaMisura);
		movimentoMag.setCoeffConv(coeffConv);
		movimentoMag.setStato(MovimentiMagBulk.STATO_INSERITO);
		
		movimentoMag.setToBeCreated();
    	return movimentoMag;
    }

    private MovimentiMagBulk createMovimentoMagazzino(UserContext userContext, OrdineAcqConsegnaBulk consegna, 
    		EvasioneOrdineRigaBulk evasioneOrdineRiga, Bene_servizioBulk bene) throws PersistencyException,ComponentException {
    	MagazzinoBulk magazzino = (MagazzinoBulk)findByPrimaryKey(userContext, evasioneOrdineRiga.getEvasioneOrdine().getNumerazioneMag().getMagazzino());
		MovimentiMagBulk movimentoMag = createMovimentoMagazzino(userContext, 
							consegna.getUnitaOperativaOrd(), 
							magazzino,
							magazzino.getTipoMovimentoMag(consegna.getTipoConsegna()),
							consegna.getOrdineAcqRiga().getBeneServizio(),
							evasioneOrdineRiga.getQuantitaEvasa(),
							evasioneOrdineRiga.getEvasioneOrdine().getDataConsegna(),
							consegna.getOrdineAcqRiga().getOrdineAcq().getDivisa(),
							consegna.getOrdineAcqRiga().getOrdineAcq().getCambio(),
							consegna.getOrdineAcqRiga().getUnitaMisura(),
							consegna.getOrdineAcqRiga().getCoefConv());

		movimentoMag.setDataBolla(evasioneOrdineRiga.getEvasioneOrdine().getDataBolla());
		movimentoMag.setOrdineAcqConsegnaUt(consegna);

		try {
			movimentoMag.setPrezzoUnitario(recuperoPrezzoUnitario(userContext, consegna));
		} catch (RemoteException e) {
			throw new ComponentException(e);
		}
		
		movimentoMag.setSconto1(consegna.getOrdineAcqRiga().getSconto1());
		movimentoMag.setSconto2(consegna.getOrdineAcqRiga().getSconto2());
		movimentoMag.setSconto3(consegna.getOrdineAcqRiga().getSconto3());
		movimentoMag.setLottoFornitore(consegna.getLottoFornitore());
		movimentoMag.setDtScadenza(consegna.getDtScadenza());
		movimentoMag.setTerzo(consegna.getOrdineAcqRiga().getOrdineAcq().getFornitore());

    	return movimentoMag;
    }

    private LottoMagBulk createLottoMagazzino(UserContext userContext, MovimentiMagBulk movimentoCaricoMag) throws ComponentException, PersistencyException {
		LottoMagBulk lotto = new LottoMagBulk();
		lotto.setBeneServizio(movimentoCaricoMag.getBeneServizioUt());
		lotto.setDivisa(movimentoCaricoMag.getDivisa());
		lotto.setCambio(movimentoCaricoMag.getCambio());
		lotto.setDtScadenza(movimentoCaricoMag.getDtScadenza()!=null?movimentoCaricoMag.getDtScadenza():lotto.getDtScadenza());
		lotto.setEsercizio(CNRUserContext.getEsercizio(userContext));
		lotto.setMagazzino(movimentoCaricoMag.getMagazzinoUt());
		lotto.setOrdineAcqConsegna(movimentoCaricoMag.getOrdineAcqConsegnaUt()!=null?movimentoCaricoMag.getOrdineAcqConsegnaUt():lotto.getOrdineAcqConsegna());
		lotto.setDivisa(Optional.ofNullable(movimentoCaricoMag.getDivisa()).orElse(lotto.getDivisa()));
		lotto.setTerzo(Optional.ofNullable(movimentoCaricoMag.getTerzo()).orElse(lotto.getTerzo()));
		lotto.setStato(LottoMagBulk.STATO_INSERITO);

		lotto.setToBeCreated();
    	return lotto;
    }

    /**
     * Effetta operazione di carico e/o scarico magazzino a fronte di ordine
     * Ritorna la riga di scarico se create, oggetto vuoto in caso di solo carico
     * 
     * @param userContext
     * @param consegna
     * @param evasioneOrdineRiga
     * @param listaMovimentiScarico
     * @return riga di scarico se creata (MovimentiMagBulk)
     * @throws ComponentException
     * @throws PersistencyException
     * @throws ApplicationException
     */
    public MovimentiMagBulk caricoDaOrdine(UserContext userContext, OrdineAcqConsegnaBulk consegna, EvasioneOrdineRigaBulk evasioneOrdineRiga) throws ComponentException, PersistencyException, ApplicationException {
    	MovimentiMagBulk movimentoScaricoMag = null; 

    	Bene_servizioHome beneHome = (Bene_servizioHome)getHome(userContext, Bene_servizioBulk.class);
    	Bene_servizioBulk bene = (Bene_servizioBulk)beneHome.findByPrimaryKey(consegna.getOrdineAcqRiga().getBeneServizio());

    	if (bene.getFlScadenza() != null && bene.getFlScadenza() && consegna.getDtScadenza() == null)
			throw new ApplicationException("Indicare la data di scadenza per la consegna "+consegna.getConsegnaOrdineString());
		
		//creo il movimento di carico di magazzino
		MovimentiMagBulk movimentoCaricoMag = createMovimentoMagazzino(userContext, consegna, evasioneOrdineRiga, bene);

		//creo il movimento di scarico di magazzino
		if (!consegna.isConsegnaMagazzino()){
			//creo il movimento di scarico
			TipoMovimentoMagAzBulk tipoMovimentoAz = new TipoMovimentoMagAzBulk(movimentoCaricoMag.getTipoMovimentoMag().getCdCds(), movimentoCaricoMag.getTipoMovimentoMag().getCdTipoMovimento());
			TipoMovimentoMagAzHome home = (TipoMovimentoMagAzHome)getHome(userContext,TipoMovimentoMagAzBulk.class);
			tipoMovimentoAz = (TipoMovimentoMagAzBulk)home.findByPrimaryKey(tipoMovimentoAz);
			
			Optional.ofNullable(tipoMovimentoAz)
				.map(TipoMovimentoMagAzBulk::getTipoMovimentoMagRif)
				.orElseThrow(()->new ApplicationException("Attenzione! Errore nell'individuazione del magazzino di riferimento per l'operazione di scarico."));
				
			movimentoScaricoMag = createMovimentoMagazzino(userContext, consegna, evasioneOrdineRiga, bene);
			movimentoScaricoMag.setTipoMovimentoMag(tipoMovimentoAz.getTipoMovimentoMagRif());
		}

    	//creo il lotto di magazzino a partire dal movimento di carico
		LottoMagBulk lotto = (LottoMagBulk)super.creaConBulk(userContext, createLottoMagazzino(userContext, movimentoCaricoMag));

		//associo il lotto di magazzino al movimento di carico
		movimentoCaricoMag.setLottoMag(lotto);
		super.creaConBulk(userContext, movimentoCaricoMag);

		if (movimentoScaricoMag != null) {
			//associo il lotto di magazzino al movimento di scarico
			movimentoScaricoMag.setLottoMag(lotto);
			//creo il legame del movimento di scarico con il movimento di carico
			movimentoScaricoMag.setPgMovimentoRif(movimentoCaricoMag.getPgMovimento());
			movimentoScaricoMag.setToBeCreated();
			movimentoScaricoMag = (MovimentiMagBulk)super.creaConBulk(userContext, movimentoScaricoMag);

			//creo il legame del movimento di carico con il movimento di scarico
			movimentoCaricoMag.setPgMovimentoRif(movimentoScaricoMag.getPgMovimento());
			movimentoCaricoMag.setToBeUpdated();
			super.modificaConBulk(userContext, movimentoCaricoMag);
			
			//codice di controllo: il lotto creato deve avere le movimentazioni a zero essendo carico/scarico contestuale
	    	LottoMagHome lottoHome = (LottoMagHome)getHome(userContext,LottoMagBulk.class);
			lotto = (LottoMagBulk)lottoHome.findByPrimaryKey(lotto);
			if (lotto.getGiacenza().compareTo(BigDecimal.ZERO)!=0 || lotto.getQuantitaValore().compareTo(BigDecimal.ZERO)!=0) {
				lotto.setGiacenza(BigDecimal.ZERO);
				lotto.setQuantitaValore(BigDecimal.ZERO);
				lotto.setToBeUpdated();
				lottoHome.update(lotto, userContext);
			}
		}
		
		return movimentoScaricoMag;
	}

    public List<BollaScaricoMagBulk> generaBolleScarico(UserContext userContext, List<MovimentiMagBulk> listaMovimenti)
    				throws ComponentException, PersistencyException, ApplicationException {
    	try {
	    	if (!listaMovimenti.isEmpty()){
	    		List<BollaScaricoMagBulk> listaBolleScarico = new ArrayList<>();
	
				listaMovimenti.stream()
	   				.collect(Collectors.groupingBy(MovimentiMagBulk::getUnitaOperativaOrd,
	   							Collectors.groupingBy(MovimentiMagBulk::getMagazzinoUt,
	   									Collectors.groupingBy(MovimentiMagBulk::getDtRiferimento))))
	   				.entrySet().stream().forEach(unitaOperativaSet->{
	   					unitaOperativaSet.getValue().entrySet().stream().forEach(magazzinoSet->{
	   						magazzinoSet.getValue().entrySet().stream().forEach(dtRiferimentoSet->{
	   	    	    			BollaScaricoMagBulk bollaScarico = new BollaScaricoMagBulk();
	   							try {
		   	    					bollaScarico.setDtBollaSca(dtRiferimentoSet.getKey());
		   	    					bollaScarico.setMagazzino(magazzinoSet.getKey());
		   	    					bollaScarico.setStato(OrdineAcqBulk.STATO_INSERITO);
		   	    					bollaScarico.setUnitaOperativaOrd(unitaOperativaSet.getKey());
		   	    					bollaScarico.setToBeCreated();
	
		   	    					dtRiferimentoSet.getValue().stream().forEach(movimento->{
		   	   	    	    			BollaScaricoRigaMagBulk riga = new BollaScaricoRigaMagBulk();
		   	   	    	    			riga.setMovimentiMag(movimento);
		   	   	    	    			riga.setToBeCreated();
		   	   	    	    			bollaScarico.addToRigheColl(riga);
		   	    					});
			   	    	    		listaBolleScarico.add((BollaScaricoMagBulk)super.creaConBulk(userContext, bollaScarico));
	   							} catch (ComponentException ex) {
	   								throw new DetailedRuntimeException(ex);
	   							}
	   						});
	   					});
					});
				
				return listaBolleScarico;
	    	}
	    	return null;
		} catch (DetailedRuntimeException ex) {
			throw handleException(ex.getDetail());
		}
    }

    private BigDecimal recuperoPrezzoUnitario(UserContext userContext, OrdineAcqConsegnaBulk cons) throws RemoteException, ComponentException{
    	OrdineAcqComponentSession ordineComponent = Utility.createOrdineAcqComponentSession();
        ParametriCalcoloImportoOrdine parametri = new ParametriCalcoloImportoOrdine();
    	OrdineAcqRigaBulk riga = cons.getOrdineAcqRiga();
        OrdineAcqBulk ordine = riga.getOrdineAcq();
    	parametri.setCambio(ordine.getCambio());
    	parametri.setDivisa(ordine.getDivisa());
    	parametri.setDivisaRisultato(getEuro(userContext));
    	parametri.setPercProrata(ordine.getPercProrata());
    	parametri.setCoefacq(riga.getCoefConv());
    	parametri.setPrezzo(riga.getPrezzoUnitario());
    	parametri.setSconto1(riga.getSconto1());
    	parametri.setSconto2(riga.getSconto2());
    	parametri.setSconto3(riga.getSconto3());
    	parametri.setVoceIva(riga.getVoceIva());
    	parametri.setQtaOrd(cons.getQuantita());
    	parametri.setArrAliIva(cons.getArrAliIva());
    	ImportoOrdine importo = ordineComponent.calcoloImportoOrdinePerMagazzino(userContext,parametri);
    	return importo.getImponibile().add(Utility.nvl(importo.getImportoIvaInd()).add(Utility.nvl(importo.getArrAliIva())));
    }
    
    private DivisaBulk getEuro(UserContext userContext) throws ComponentException {
    	try {
    		DivisaBulk divisaDefault = ((DivisaHome)getHome(userContext, DivisaBulk.class)).getDivisaDefault(userContext);
    		Optional.ofNullable(divisaDefault)
    				.map(DivisaBulk::getCd_divisa)
    				.orElseThrow(()->new it.cnr.jada.comp.ApplicationException("Impossibile caricare la valuta di default! Prima di poter inserire un ordine, immettere tale valore."));
    		return divisaDefault;
    	} catch (javax.ejb.EJBException|PersistencyException e) {
    		handleException(e);
    	}
    	return null;
    }

    public ScaricoMagazzinoBulk scaricaMagazzino(UserContext userContext, ScaricoMagazzinoBulk scaricoMagazzino) throws ComponentException, PersistencyException {
    	List<BollaScaricoMagBulk> bolleList = new ArrayList<>();
		if (scaricoMagazzino.getUnitaOperativaAbilitata()==null || scaricoMagazzino.getUnitaOperativaAbilitata().getCdUnitaOperativa()==null)
			throw new ApplicationException("Errore nello scarico magazzino! Manca l'indicazione della Unità Operativa.");
		if (scaricoMagazzino.getMagazzinoAbilitato()==null || scaricoMagazzino.getMagazzinoAbilitato().getCdMagazzino()==null)
			throw new ApplicationException("Errore nello scarico magazzino! Manca l'indicazione del Magazzino.");
		if (scaricoMagazzino.getTipoMovimentoMag()==null || scaricoMagazzino.getTipoMovimentoMag().getCdTipoMovimento()==null)
			throw new ApplicationException("Errore nello scarico magazzino! Manca l'indicazione del Tipo Movimento.");
		if (scaricoMagazzino.getDataCompetenza()==null)
			throw new ApplicationException("Errore nello scarico magazzino! Manca l'indicazione della Data Competenza.");
		if (scaricoMagazzino.getScaricoMagazzinoRigaColl()==null||scaricoMagazzino.getScaricoMagazzinoRigaColl().isEmpty())
			throw new ApplicationException("Errore nello scarico magazzino! Manca l'indicazione dei beni/servizi da movimentare.");

		DivisaBulk divisaDefault = ((DivisaHome)getHome(userContext, DivisaBulk.class)).getDivisaDefault(userContext);
		if (divisaDefault==null || divisaDefault.getCd_divisa()==null)
			throw new it.cnr.jada.comp.ApplicationException("Impossibile caricare la valuta di default! Prima di poter effettuare lo scarico, immettere tale valore.");

		LottoMagHome lottoHome = (LottoMagHome)getHome(userContext, LottoMagBulk.class);
		
		List<MovimentiMagBulk> listaMovimenti = new ArrayList<>();

		try {
			scaricoMagazzino.getScaricoMagazzinoRigaColl().stream()
			.forEach(scaricoMagazzinoRiga->{
				scaricoMagazzinoRiga.setAnomalia(null);
				List<String> errorList = new ArrayList<>();

				try {
					//richiamo per ogni riga i lotti e faccio i lock 
					Map<LottoMagBulk, BigDecimal> mapLotti = new HashMap<LottoMagBulk, BigDecimal>();
					if (scaricoMagazzinoRiga.isImputazioneScaricoSuBeneEnable()) {
						java.util.Collection<LottoMagBulk> lottiList = lottoHome.findLottiMagazzinoByClause(scaricoMagazzinoRiga);
						BigDecimal qtResidua = scaricoMagazzinoRiga.getQtScaricoConvertita();
						scaricoMagazzinoRiga.setScaricoMagazzinoRigaLottoColl(new BulkList<ScaricoMagazzinoRigaLottoBulk>());
						for (Iterator<LottoMagBulk> iterator = lottiList.iterator(); iterator.hasNext();) {
							LottoMagBulk lottoMagazzino = iterator.next();
							//Aggiorno i lotti sull'oggetto ScaricoMagazzinoRiga
							ScaricoMagazzinoRigaLottoBulk rigaLotto = new ScaricoMagazzinoRigaLottoBulk();
							rigaLotto.setLottoMagazzino(lottoMagazzino);
							rigaLotto.setQtScarico(BigDecimal.ZERO);
							scaricoMagazzinoRiga.getScaricoMagazzinoRigaLottoColl().add(rigaLotto);

							if (qtResidua.compareTo(BigDecimal.ZERO)>0) {
								LottoMagBulk lottoMagazzinoDB = (LottoMagBulk)lottoHome.findAndLock(lottoMagazzino);

								//metto sull'oggetto ScaricoMagazzinoRiga il lotto bloccato
								rigaLotto.setLottoMagazzino(lottoMagazzinoDB);

								BigDecimal qtScarico = lottoMagazzinoDB.getGiacenza().compareTo(qtResidua)>0?qtResidua:lottoMagazzinoDB.getGiacenza();
								qtResidua = qtResidua.subtract(qtScarico);
								mapLotti.put(lottoMagazzinoDB, qtScarico);
							}
						}
					} else {
						scaricoMagazzinoRiga.getScaricoMagazzinoRigaLottoColl().stream()
							.filter(rigaLotto->rigaLotto.getQtScarico().compareTo(BigDecimal.ZERO)>0)
							.forEach(rigaLotto->{
								try{
									LottoMagBulk lottoMagazzinoDB = (LottoMagBulk)lottoHome.findAndLock(rigaLotto.getLottoMagazzino());
									mapLotti.put(lottoMagazzinoDB, rigaLotto.getQtScaricoConvertita());

									//metto sull'oggetto ScaricoMagazzinoRiga il lotto bloccato
									rigaLotto.setLottoMagazzino(lottoMagazzinoDB);
								} catch (OutdatedResourceException|BusyResourceException|PersistencyException ex) {
									throw new DetailedRuntimeException(ex);
								}
							});
					}
					
					//Verifico che i lotti hanno giacenza sufficiente
					mapLotti.entrySet().stream()
						.filter(set->set.getKey().getGiacenza().compareTo(set.getValue())<0)
						.forEach(set->{
							errorList.add("Per il lotto "+
									set.getKey().getEsercizio()+"/"+set.getKey().getCdNumeratoreMag()+"/"+set.getKey().getPgLotto()+
									" alla data "+new java.text.SimpleDateFormat("dd/MM/yyyy").format(scaricoMagazzino.getDataCompetenza())+
									" la giacenza ("+new it.cnr.contab.util.Importo5CifreFormat().format(set.getKey().getGiacenza())+
									") è inferiore alla quantità da scaricare ("+
									new it.cnr.contab.util.Importo5CifreFormat().format(set.getValue())+")");
						});
					
					//Verifico che la quantità da scaricare sia pari a quella richiesta
					BigDecimal totGiacenzaLotti = mapLotti.entrySet().stream()
													.map(Entry::getValue)
													.reduce(BigDecimal.ZERO, BigDecimal::add);
					if (totGiacenzaLotti.compareTo(scaricoMagazzinoRiga.getQtScaricoConvertita())<0)
						errorList.add("Alla data "+new java.text.SimpleDateFormat("dd/MM/yyyy").format(scaricoMagazzino.getDataCompetenza())+
								" la giacenza totale dei lotti ("+new it.cnr.contab.util.Importo5CifreFormat().format(totGiacenzaLotti)+
								") è inferiore alla quantità richiesta da scaricare ("+
								new it.cnr.contab.util.Importo5CifreFormat().format(scaricoMagazzinoRiga.getQtScaricoConvertita())+")");

					if (errorList.isEmpty()) {
						//Effettuo le movimentazioni di magazzino
						mapLotti.keySet().stream()
							.forEach(lottoMagazzino->{
								try {
									BigDecimal qtScarico = mapLotti.get(lottoMagazzino);
									//creo il movimento di carico di magazzino
									MovimentiMagBulk movimentoMag = createMovimentoMagazzino(userContext, 
															scaricoMagazzinoRiga.getUnitaOperativaRicevente(), 
															scaricoMagazzino.getMagazzinoAbilitato(),
															scaricoMagazzino.getTipoMovimentoMag(),
															scaricoMagazzinoRiga.getBeneServizio(),
															qtScarico,
															scaricoMagazzino.getDataCompetenza(),
															divisaDefault,
															BigDecimal.ONE,
															scaricoMagazzinoRiga.getUnitaMisura(),
															scaricoMagazzinoRiga.getCoefConv());
									
									movimentoMag.setLottoMag(lottoMagazzino);
									listaMovimenti.add((MovimentiMagBulk)super.creaConBulk(userContext, movimentoMag));
								} catch (ComponentException|PersistencyException ex) {
									throw new DetailedRuntimeException(ex);
								}
							});
					} else
						scaricoMagazzinoRiga.setAnomalia(
								errorList.stream().collect(Collectors.joining(", ")));
				} catch (OutdatedResourceException|BusyResourceException|PersistencyException|DetailedRuntimeException ex) {
					throw new DetailedRuntimeException(ex);
				}
			});
		} catch (DetailedRuntimeException ex) {
			throw handleException(ex.getDetail());
		}
		
		if (!listaMovimenti.isEmpty())
			bolleList = generaBolleScarico(userContext, listaMovimenti);

		scaricoMagazzino.setBolleScaricoColl(bolleList); 
		return scaricoMagazzino;
    }

	public ScaricoMagazzinoBulk initializeScaricoMagazzino(UserContext usercontext, ScaricoMagazzinoBulk scaricoMagazzinoBulk) throws PersistencyException, ComponentException {
		ScaricoMagazzinoHome scaricoMagazzinoHome = (ScaricoMagazzinoHome)getHome(usercontext, ScaricoMagazzinoBulk.class);
		UnitaOperativaOrdHome unitaOperativaHome = (UnitaOperativaOrdHome)getHome(usercontext, UnitaOperativaOrdBulk.class);
		
		SQLBuilder sqlUop = scaricoMagazzinoHome.selectUnitaOperativaAbilitataByClause(usercontext, scaricoMagazzinoBulk, 
				unitaOperativaHome, new UnitaOperativaOrdBulk(), new CompoundFindClause());
		List<UnitaOperativaOrdBulk> listUop=unitaOperativaHome.fetchAll(sqlUop);
		scaricoMagazzinoBulk.setUnitaOperativaAbilitata(Optional.ofNullable(listUop)
														.map(e->{
															if (e.stream().count()>1) {
																return null;
															};
															return e.stream().findFirst().orElse(null);
														}).orElse(null));
					

		MagazzinoHome magazzinoHome = (MagazzinoHome)getHome(usercontext, MagazzinoBulk.class);
		SQLBuilder sqlMagazzino = scaricoMagazzinoHome.selectMagazzinoAbilitatoByClause(usercontext, scaricoMagazzinoBulk, 
				magazzinoHome, new MagazzinoBulk(), new CompoundFindClause());
		List<MagazzinoBulk> listMagazzino=magazzinoHome.fetchAll(sqlMagazzino);
		scaricoMagazzinoBulk.setMagazzinoAbilitato(Optional.ofNullable(listMagazzino)
				.map(e->{
					if (e.stream().count()>1) {
						return null;
					};
					return e.stream().findFirst().orElse(null);
				}).orElse(null));
		
		scaricoMagazzinoBulk.setDataMovimento(DateUtils.truncate(it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp()));
		scaricoMagazzinoBulk.setDataCompetenza(DateUtils.truncate(it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp()));
		return scaricoMagazzinoBulk;
	}

    public RemoteIterator preparaQueryBolleScaricoDaVisualizzare(UserContext userContext, List<BollaScaricoMagBulk> bolle) throws ComponentException{
		BollaScaricoMagHome homeBolla= (BollaScaricoMagHome)getHome(userContext, BollaScaricoMagBulk.class);
		return iterator(userContext,
				homeBolla.selectBolleGenerate(bolle),
				BollaScaricoMagBulk.class,
				"default");
    }
    
}
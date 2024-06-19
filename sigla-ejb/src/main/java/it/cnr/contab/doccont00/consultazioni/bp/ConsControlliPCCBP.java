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

package it.cnr.contab.doccont00.consultazioni.bp;

import com.opencsv.CSVWriter;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.ControlliPCCParams;
import it.cnr.contab.doccont00.consultazioni.bulk.VControlliPCCBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util00.bulk.storage.AllegatoParentIBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CondizioneComplessaBulk;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConsControlliPCCBP extends SelezionatoreListaBP implements SearchProvider {
    public static final String EMPTY = "";
    private static final long serialVersionUID = 1L;
    protected StoreService storeService;
    protected Integer esercizio;

    public ConsControlliPCCBP() {
        super();
    }

    @Override
    public void setMultiSelection(boolean flag) {
        super.setMultiSelection(flag);
        super.table.setOnselect("select");
    }


    @Override
    protected void init(Config config, ActionContext context)
            throws BusinessProcessException {
        setMultiSelection(true);
        try {
            setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Class.forName(config.getInitParameter("bulkClassName"))));
            OggettoBulk model = (OggettoBulk) getBulkInfo().getBulkClass().newInstance();
            setModel(context, model);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw handleException(e);
        }
        setColumns(getBulkInfo().getColumnFieldPropertyDictionary());
        super.init(config, context);
        openIterator(context);
        storeService = SpringUtil.getBean("storeService", StoreService.class);
        esercizio = CNRUserContext.getEsercizio(context.getUserContext());
    }

    public it.cnr.jada.ejb.CRUDComponentSession createComponentSession() throws javax.ejb.EJBException, RemoteException, BusinessProcessException {
        return (it.cnr.jada.ejb.CRUDComponentSession) createComponentSession("JADAEJB_CRUDComponentSession", it.cnr.jada.ejb.CRUDComponentSession.class);
    }

    @Override
    public Button[] createToolbar() {
        final Properties properties = it.cnr.jada.util.Config.getHandler().getProperties(getClass());
        return Stream.concat(Arrays.asList(super.createToolbar()).stream(),
                Arrays.asList(
                        new Button(properties, "CRUDToolbar.estrazionecsv"),
                        new Button(properties, "CRUDToolbar.allegaticsv")
                ).stream()).toArray(Button[]::new);
    }

    @Override
    public RemoteIterator search(
            ActionContext actioncontext,
            CompoundFindClause compoundfindclause,
            OggettoBulk oggettobulk)
            throws BusinessProcessException {
        try {
            return createComponentSession().cerca(actioncontext.getUserContext(),
                    compoundfindclause,
                    (OggettoBulk) getBulkInfo().getBulkClass().newInstance());
        } catch (ComponentException | RemoteException | IllegalAccessException | InstantiationException e) {
            throw handleException(e);
        }
    }

    public void openIterator(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            setIterator(actioncontext, search(
                    actioncontext,
                    Optional.ofNullable(getCondizioneCorrente())
                            .map(CondizioneComplessaBulk::creaFindClause)
                            .filter(CompoundFindClause.class::isInstance)
                            .map(CompoundFindClause.class::cast)
                            .orElseGet(() -> new CompoundFindClause()),
                    getModel())
            );
        } catch (RemoteException e) {
            throw new BusinessProcessException(e);
        }
    }

    private String[] createArray(String... values) {
        List<String> baseList = new ArrayList<>();
        if (Optional.ofNullable(values).isPresent()) {
            baseList.addAll(Arrays.asList(values));
        }
        for (int i = baseList.size(); i <= 57; i++) {
            baseList.add(null);
        }
        return baseList.toArray(new String[57]);
    }

    public void elaboraCSV(ControlliPCCParams controlliPCCParams, List<VControlliPCCBulk> vControlliPCCBulks) throws BusinessProcessException {
        try {
            String fileName = UUID.randomUUID().toString().concat(".csv");
            final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            decimalFormat.setGroupingUsed(false);
            File file = new File(System.getProperty("tmp.dir.SIGLAWeb") + "/tmp/", fileName);

            CSVWriter writer = new CSVWriter(
                    new FileWriter(file),
                    ';',
                    CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            //La prima riga deve essere vuota
            writer.writeNext(createArray());
            writer.writeNext(createArray("Codice del modello", "GESTIONE IMPORTI DOCUMENTI", EMPTY, "i campi contrassegnati da * sono obbligatori"));
            writer.writeNext(createArray("Versione del modello", "1"));
            writer.writeNext(createArray("Utente che trasmette il file (Codice Fiscale)", controlliPCCParams.getCodiceFiscale()));
            writer.writeNext(createArray("DATI IDENTIFICATIVI FATTURA*", EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                    "TIPO OPERAZIONE*", "VARIAZIONE IMPORTI DOCUMENTI\n" +
                            "Tutti i campi sono obbligatori\n" +
                            "Sezione da compilare solo per le righe del modello per le quali Azione = 'SID'"
                    , EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, "REGIME IVA\n" +
                            "Sezione da compilare solo per le righe del modello per le quali Azione = 'MI'", "RICEZIONE/RIFIUTO/COMUNICAZIONE SCADENZA \n" +
                            "Sezione da compilare solo per le righe del modello per le quali Azione  = 'RC' ;  Azione = 'RF'; Azione = 'CS'", EMPTY, "ESITO ELABORAZIONE"));
            writer.writeNext(createArray("IDENTIFICATIVO 1", EMPTY, "IDENTIFICATIVO 3"));
            writer.writeNext(createArray("Numero progressivo di registrazione", "IDENTIFICATIVO 2", EMPTY,
                    "Data documento (SDI 2.1.1.3 Data)", "Codice fiscale fornitore", "Codice ufficio", "Azione", "Imponibile", "Imposta",
                    "Importo non commerciale*", "Importo sospeso in Contenzioso*", "Data inizio sospesione in Contenzioso*",
                    "Importo sospeso in contestazione/adempimenti normativi*", "Data inizio sospesione in contestazione /adempimenti normativi*",
                    "Importo sospeso per data esito regolare verifica di conformità*", "Data inizio sospensione per data esito regolare verifica di conformità*",
                    "Importo non liquidabile*", "Flag split (S/N)", "Data", "Numero protocollo di entrata", "Codice segnalazione", "Descrizione segnalazione"));
            writer.writeNext(createArray(EMPTY, "Lotto SDI", "Numero fattura \n" + "(SDI 2.1.1.4 Numero)"));
            final boolean isOperazioneSID = controlliPCCParams.getTipoOperazione().equalsIgnoreCase(ControlliPCCParams.TipoOperazioneType.SID.name());
            final boolean isComunicazioneScadenza = Objects.equals(ControlliPCCParams.TipoOperazioneType.RC.name(), controlliPCCParams.getTipoOperazione());
            vControlliPCCBulks.stream().forEach(vControlliPCCBulk -> {
                writer.writeNext(createArray(
                        EMPTY,
                        Optional.ofNullable(vControlliPCCBulk.getIdentificativoSdi())
                                .map(String::valueOf)
                                .orElse(EMPTY), // Lotto SDI
                        Optional.ofNullable(vControlliPCCBulk.getNumeroDocumento())
                                .map(String::valueOf)
                                .orElse(EMPTY), // Numero fattura (SDI 2.1.1.4 Numero)
                        Optional.ofNullable(vControlliPCCBulk.getDataDocumento())
                                .map(timestamp -> DateTimeFormatter.ofPattern("dd/MM/yyyy").format(timestamp.toLocalDateTime()))
                                .orElse(EMPTY), // Data documento (SDI 2.1.1.3 Data)
                        Optional.ofNullable(vControlliPCCBulk.getCodiceFiscale())
                                .orElse(EMPTY), // Codice fiscale fornitore
                        Optional.ofNullable(vControlliPCCBulk.getCodiceDestinatario())
                                .orElse(EMPTY), // Codice ufficio
                        controlliPCCParams.getTipoOperazione(), // Azione
                        !isOperazioneSID ? EMPTY : Optional.ofNullable(vControlliPCCBulk.getImponibile())
                                .map(bigDecimal -> decimalFormat.format(bigDecimal))
                                .orElse(String.valueOf(BigDecimal.ZERO)), // Imponibile
                        !isOperazioneSID ? EMPTY : Optional.ofNullable(vControlliPCCBulk.getImposta())
                                .map(bigDecimal -> decimalFormat.format(bigDecimal))
                                .orElse(String.valueOf(BigDecimal.ZERO)), // Imposta
                        !isOperazioneSID ? EMPTY : Optional.ofNullable(BigDecimal.ZERO)
                                .map(bigDecimal -> decimalFormat.format(bigDecimal))
                                .orElse(String.valueOf(BigDecimal.ZERO)), // Importo non commerciale*
                        !isOperazioneSID ? EMPTY : Optional.ofNullable(vControlliPCCBulk.getCausale())
                                .filter(s -> s.equalsIgnoreCase(Fattura_passivaBulk.CONT))
                                .map(s -> vControlliPCCBulk.getImponibile())
                                .map(bigDecimal -> decimalFormat.format(bigDecimal))
                                .orElse(String.valueOf(BigDecimal.ZERO)), // Importo sospeso in Contenzioso*
                        !isOperazioneSID ? EMPTY : Optional.ofNullable(vControlliPCCBulk.getCausale())
                                .filter(s -> s.equalsIgnoreCase(Fattura_passivaBulk.CONT))
                                .map(s -> vControlliPCCBulk.getDtInizioSospensione())
                                .map(timestamp -> DateTimeFormatter.ofPattern("dd/MM/yyyy").format(timestamp.toLocalDateTime()))
                                .orElse(EMPTY), // Data inizio sospesione in Contenzioso*
                        !isOperazioneSID ? EMPTY : Optional.ofNullable(vControlliPCCBulk.getCausale())
                                .filter(s -> s.equalsIgnoreCase(Fattura_passivaBulk.CONT_NORM))
                                .map(s -> vControlliPCCBulk.getImponibile())
                                .map(bigDecimal -> decimalFormat.format(bigDecimal))
                                .orElse(String.valueOf(BigDecimal.ZERO)), // Importo sospeso in contestazione/adempimenti normativi*
                        !isOperazioneSID ? EMPTY : Optional.ofNullable(vControlliPCCBulk.getCausale())
                                .filter(s -> s.equalsIgnoreCase(Fattura_passivaBulk.CONT_NORM))
                                .map(s -> vControlliPCCBulk.getDtInizioSospensione())
                                .map(timestamp -> DateTimeFormatter.ofPattern("dd/MM/yyyy").format(timestamp.toLocalDateTime()))
                                .orElse(EMPTY), // Data inizio sospesione in contestazione /adempimenti normativi*
                        !isOperazioneSID ? EMPTY : Optional.ofNullable(vControlliPCCBulk.getCausale())
                                .filter(s -> s.equalsIgnoreCase(Fattura_passivaBulk.CONT_CONF))
                                .map(s -> vControlliPCCBulk.getImponibile())
                                .map(bigDecimal -> decimalFormat.format(bigDecimal))
                                .orElse(String.valueOf(BigDecimal.ZERO)), // Importo sospeso per data esito regolare verifica di conformità*
                        !isOperazioneSID ? EMPTY : Optional.ofNullable(vControlliPCCBulk.getCausale())
                                .filter(s -> s.equalsIgnoreCase(Fattura_passivaBulk.CONT_CONF))
                                .map(s -> vControlliPCCBulk.getDtInizioSospensione())
                                .map(timestamp -> DateTimeFormatter.ofPattern("dd/MM/yyyy").format(timestamp.toLocalDateTime()))
                                .orElse(EMPTY), // Data inizio sospensione per data esito regolare verifica di conformità*
                        !isOperazioneSID ? EMPTY : Optional.ofNullable(vControlliPCCBulk.getStatoLiquidazione())
                                .map(s -> s.equalsIgnoreCase(Fattura_passivaBulk.NOLIQ) ? vControlliPCCBulk.getImponibile() : vControlliPCCBulk.getImTotaleNC())
                                .map(bigDecimal -> decimalFormat.format(bigDecimal))
                                .orElse(String.valueOf(BigDecimal.ZERO)), // Importo non liquidabile*
                        EMPTY, // Flag split (S/N) Per ora non gestito
                        !isComunicazioneScadenza ? EMPTY : Optional.ofNullable(vControlliPCCBulk.getDataScadenza())
                                .map(timestamp -> DateTimeFormatter.ofPattern("dd/MM/yyyy").format(timestamp.toLocalDateTime()))
                                .orElse(EMPTY) // RICEZIONE/RIFIUTO/COMUNICAZIONE SCADENZA Sezione da compilare solo per le righe del modello per le quali Azione  = 'RC' ;  Azione = 'RF'; Azione = 'CS'
                ));
            });
            writer.close();
            final StorageObject storageObject = storeService.storeSimpleDocument(
                    new FileInputStream(file),
                    "text/csv",
                    storeService.getStorageObjectByPath(AllegatoParentIBulk.getStorePath(AllegatiPCCBP.COMUNICAZIONI_PCC, esercizio), true).getPath(),
                    Stream.of(
                            new AbstractMap.SimpleEntry<>(StoragePropertyNames.NAME.value(), fileName),
                            new AbstractMap.SimpleEntry<>(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), Arrays.asList("P:cm:titled")),
                            new AbstractMap.SimpleEntry<>(StoragePropertyNames.TITLE.value(),
                                    String.format("Operazione %s con codice fiscale %s",
                                        ControlliPCCParams.TipoOperazioneType.valueOf(controlliPCCParams.getTipoOperazione()).label(),
                                        controlliPCCParams.getCodiceFiscale()
                                    )
                            ),
                            new AbstractMap.SimpleEntry<>(StoragePropertyNames.OBJECT_TYPE_ID.value(), "cmis:document"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            );
        } catch (IOException e) {
            throw handleException(e);
        }

    }
}
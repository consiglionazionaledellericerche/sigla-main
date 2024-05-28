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
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CondizioneComplessaBulk;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class ConsControlliPCCBP extends SelezionatoreListaBP implements SearchProvider {
    private static final long serialVersionUID = 1L;
    public static final String EMPTY = "";
    private String fileName;
    public ConsControlliPCCBP() {
        super();
    }

    @Override
    public void setMultiSelection(boolean flag) {
        super.setMultiSelection(flag);
        super.table.setOnselect("select");
    }

    public boolean isScaricaCSVHidden() {
        return !Optional.ofNullable(fileName).isPresent();
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
                        new Button(properties, "CRUDToolbar.scaricacsv")
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

    public void scaricaCSV(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
        if (fileName != null) {
            final Path path = Paths.get(System.getProperty("tmp.dir.SIGLAWeb"), "/tmp/", fileName);
            FileInputStream fileInputStream = new FileInputStream(path.toFile());
            ((HttpActionContext) actioncontext).getResponse().setContentLength(fileInputStream.available());
            ((HttpActionContext) actioncontext).getResponse().setContentType("text/csv");
            OutputStream os = ((HttpActionContext) actioncontext).getResponse().getOutputStream();
            ((HttpActionContext) actioncontext).getResponse().setDateHeader("Expires", 0);
            byte[] buffer = new byte[((HttpActionContext) actioncontext).getResponse().getBufferSize()];
            int buflength;
            while ((buflength = fileInputStream.read(buffer)) > 0) {
                os.write(buffer, 0, buflength);
            }
            fileInputStream.close();
            os.flush();
            Files.deleteIfExists(path);
            fileName = null;
        }
    }

    public void elaboraCSV(ControlliPCCParams controlliPCCParams, List<VControlliPCCBulk> vControlliPCCBulks) throws BusinessProcessException{
        try {
            this.fileName = UUID.randomUUID().toString().concat(".csv");
            final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            File file = new File(System.getProperty("tmp.dir.SIGLAWeb") + "/tmp/", this.fileName);
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            //La prima riga deve essere vuota
            writer.writeNext(new String[]{});
            writer.writeNext(new String[]{"Codice del modello","GESTIONE IMPORTI DOCUMENTI", EMPTY,"i campi contrassegnati da * sono obbligatori"});
            writer.writeNext(new String[]{"Versione del modello","1"});
            writer.writeNext(new String[]{"Utente che trasmette il file (Codice Fiscale)", controlliPCCParams.getCodiceFiscale()});
            writer.writeNext(new String[]{"DATI IDENTIFICATIVI FATTURA*", EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                    "TIPO OPERAZIONE*","VARIAZIONE IMPORTI DOCUMENTI\n" +
                    "Tutti i campi sono obbligatori\n" +
                    "Sezione da compilare solo per le righe del modello per le quali Azione = 'SID'"
                    , EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,"REGIME IVA\n" +
                    "Sezione da compilare solo per le righe del modello per le quali Azione = 'MI'","RICEZIONE/RIFIUTO/COMUNICAZIONE SCADENZA \n" +
                    "Sezione da compilare solo per le righe del modello per le quali Azione  = 'RC' ;  Azione = 'RF'; Azione = 'CS'", EMPTY,"ESITO ELABORAZIONE"});
            writer.writeNext(new String[]{"IDENTIFICATIVO 1", EMPTY,"IDENTIFICATIVO 3"});
            writer.writeNext(new String[]{"Numero progressivo di registrazione","IDENTIFICATIVO 2", EMPTY,
                    "Data documento (SDI 2.1.1.3 Data)","Codice fiscale fornitore","Codice ufficio","Azione","Imponibile","Imposta",
                    "Importo non commerciale*","Importo sospeso in Contenzioso*","Data inizio sospesione in Contenzioso*",
                    "Importo sospeso in contestazione/adempimenti normativi*","Data inizio sospesione in contestazione /adempimenti normativi*",
                    "Importo sospeso per data esito regolare verifica di conformità*","Data inizio sospensione per data esito regolare verifica di conformità*",
                    "Importo non liquidabile*","Flag split (S/N)","Data","Numero protocollo di entrata","Codice segnalazione","Descrizione segnalazione"});
            writer.writeNext(new String[]{EMPTY,"Lotto SDI","Numero fattura \n" + "(SDI 2.1.1.4 Numero)"});
            final boolean isOperazioneSID = controlliPCCParams.getTipoOperazione().equalsIgnoreCase(ControlliPCCParams.TipoOperazioneType.SID.name());
            final boolean isComunicazioneScadenza = Arrays.asList(
                    ControlliPCCParams.TipoOperazioneType.RC.name()
                    ).contains(controlliPCCParams.getTipoOperazione());
            vControlliPCCBulks.stream().forEach(vControlliPCCBulk -> {
                writer.writeNext(new String[]{
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
                                .filter(s -> s.equalsIgnoreCase(Fattura_passivaBulk.NOLIQ))
                                .map(s -> vControlliPCCBulk.getImponibile())
                                .map(bigDecimal -> decimalFormat.format(bigDecimal))
                                .orElse(String.valueOf(BigDecimal.ZERO)), // Importo non liquidabile*
                        EMPTY, // Flag split (S/N) Per ora non gestito
                        !isComunicazioneScadenza ? EMPTY : Optional.ofNullable(vControlliPCCBulk.getDataScadenza())
                                .map(timestamp -> DateTimeFormatter.ofPattern("dd/MM/yyyy").format(timestamp.toLocalDateTime()))
                                .orElse(EMPTY), // RICEZIONE/RIFIUTO/COMUNICAZIONE SCADENZA Sezione da compilare solo per le righe del modello per le quali Azione  = 'RC' ;  Azione = 'RF'; Azione = 'CS'
                });
            });
            writer.close();
            setMessage(INFO_MESSAGE, "Il file è stato creato, per poterlo scaricare utilizzare <b>Scarica CSV</b>");
        } catch (IOException e) {
            throw handleException(e);
        }

    }
}
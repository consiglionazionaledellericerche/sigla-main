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

package it.cnr.contab.cori00.bp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.EcfBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_crBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk;
import it.cnr.contab.cori00.ejb.Liquid_coriComponentSession;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import java.io.*;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;

public class LiquidazioneCoriF24EPBP extends it.cnr.jada.util.action.SelezionatoreListaBP {

    private String file;
    private boolean abilitatoF24;

    public LiquidazioneCoriF24EPBP() {
        super("Tr");
    }

    public LiquidazioneCoriF24EPBP(String function) {
        super(function + "Tr");
        table.setMultiSelection(true);
    }

    public it.cnr.jada.util.jsp.Button[] createToolbar() {
        Button[] toolbar = new Button[4];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.reset");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.details");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.f24Tot");
        // attenzione il link al file viene settato dinamicamente con il nome del file nella writeToolbar(PageContext pagecontext)
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.download");
        return toolbar;
    }

    public boolean isScaricaButtonEnabled() {
        if (getFile() != null)
            return true;
        else
            return false;
    }

    public boolean isF24ButtonHidden() {
        if (isAbilitatoF24())
            return false;
        else
            return true;
    }

    public boolean isF24ButtonEnabled() {
        if (getModel() != null && isAbilitatoF24())
            return false;
        else
            return true;
    }

    public boolean isAbilitatoF24() {
        return abilitatoF24;
    }

    public void setAbilitatoF24(boolean abilitatoF24) {
        this.abilitatoF24 = abilitatoF24;
    }

    public void writeToolbar(PageContext pagecontext) throws IOException, ServletException {
        Button[] toolbar = getToolbar();
        if (getFile() != null) {
            HttpServletRequest httpservletrequest = (HttpServletRequest) pagecontext.getRequest();
            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append(JSPUtils.getAppRoot(httpservletrequest));
            toolbar[3].setHref("javascript:doPrint('" + stringbuffer + getFile() + "')");
        }
        super.writeToolbar(pagecontext);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Liquid_coriComponentSession createComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
        return (Liquid_coriComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCORI00_EJB_Liquid_coriComponentSession", Liquid_coriComponentSession.class);
    }

    protected void init(Config config, ActionContext context) throws BusinessProcessException {
        super.init(config, context);
        try {
            setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Liquid_coriBulk.class));
            refresh(context);
            setAbilitatoF24(UtenteBulk.isAbilitatoF24EP(context.getUserContext()));
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public void refresh(ActionContext context) throws BusinessProcessException {
        try {
            ((Liquid_coriComponentSession) createComponentSession()).eliminaPendenti_f24Tot(context.getUserContext());
            Liquid_coriBulk bulk = new Liquid_coriBulk(); //viene richiamato il select del component
            setIterator(context, createComponentSession().cerca(context.getUserContext(), null, bulk));
            setFile(null);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * @param s            Stringa in Input
     * @param allineamento del testo "D" Destra - "S" Sinistra
     * @param dimensione   richiesta del campo
     * @param riempimento  carattere di riempimento per raggiungere la dimensione richiesta
     * @return La stringa formattata e riempita con l'allinemento richiesto
     */
    public String Formatta(String s, String allineamento, Integer dimensione, String riempimento) {
        if (s == null)
            s = riempimento;
        if (s.length() < dimensione) {
            if (allineamento.compareTo("D") == 0) {
                while (s.length() < dimensione)
                    s = riempimento + s;
                return s.toUpperCase();
            } else {
                while (s.length() < dimensione)
                    s = s + riempimento;
                return s.toUpperCase();
            }
        } else if (s.length() > dimensione) {
            s = s.substring(0, dimensione);
            return s.toUpperCase();
        }
        return s.toUpperCase();
    }

    public void EstrazioneTot(ActionContext context) throws ComponentException, RemoteException, BusinessProcessException {
        try {
            //	da fare
            // lanciare popolamento della tabella da cui pescare i dati
            // estrarli per popolare la tabella definitiva
            AnagraficoBulk ente = ((Liquid_coriComponentSession) createComponentSession()).getAnagraficoEnte(context.getUserContext());
            java.util.List lista = null;
            it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk liquidazione = null;
            ((Liquid_coriComponentSession) createComponentSession()).eliminaPendenti_f24Tot(context.getUserContext());
            for (Iterator i = getSelectedElements(context).iterator(); i.hasNext(); ) {
                liquidazione = (it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk) i.next();
                ((Liquid_coriComponentSession) createComponentSession()).Popola_f24Tot(context.getUserContext(), liquidazione);
            }
            it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk liquid = new Liquid_coriBulk(liquidazione.getCd_cds(), liquidazione.getCd_unita_organizzativa(), liquidazione.getEsercizio(), null);
            lista = ((Liquid_coriComponentSession) createComponentSession()).EstraiListaTot(context.getUserContext(), liquid);

            File f = null;
            String data_formattata = Formatta(new Integer(EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.DAY_OF_MONTH)).toString(), "D", 2, "0").concat(
                    Formatta(new Integer(EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.MONTH) + 1).toString(), "D", 2, "0") +
                            EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.YEAR));
            if (liquidazione != null)
                f = new File(System.getProperty("tmp.dir.SIGLAWeb") + "/tmp/",
                        "F24EP-" + liquidazione.getCd_unita_organizzativa().replace(".", "-") + "-" + data_formattata + ".T24");
            else
                throw new ApplicationException("Non ci sono dati!");

            OutputStream os = (OutputStream) new FileOutputStream(f);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);

            if (lista != null && !lista.isEmpty()) {
                //Testata - posizionale lunghezza 1900 caratteri
                String Codice_Fiscale = ente.getCodice_fiscale();

                //Testata parte iniziale Fissa Tipo Record "A"
                bw.append("A");
                bw.append(Formatta(null, "S", 14, " "));
                bw.append(Formatta("F24EP", "S", 5, " "));
                bw.append("14");
                bw.append(Formatta(Codice_Fiscale, "S", 16, " "));
                bw.append(Formatta(null, "S", 177, " "));
                bw.append(Formatta(ente.getRagione_sociale(), "S", 60, " "));
                bw.append(Formatta(null, "S", 164, " "));
                bw.append(Formatta(null, "S", 1, " ")); //???"A"
                bw.append(Formatta(null, "S", 14, " "));
                bw.append(Formatta(null, "S", 67, " "));
                bw.append("001");
                bw.append("001");//?
                bw.append(Formatta(null, "S", 100, " "));
                bw.append("1");
                bw.append(Formatta(null, "S", 1269, " "));
                bw.append("A");
                //bw.newLine(); da problemi per il formato del file
                bw.append("\r\n");
                // Fine Testata
                //parte iniziale Fissa Tipo Record "M"
                bw.append("M");
                bw.append(Formatta(Codice_Fiscale, "S", 11, " "));
                bw.append(Formatta(null, "S", 5, " "));
                bw.append(Formatta("1", "D", 8, "0"));
                bw.append(Formatta(null, "S", 3, " "));
                bw.append(Formatta(null, "S", 25, " "));
                bw.append(Formatta(null, "S", 20, " "));
                bw.append(Formatta(null, "S", 16, " "));
                bw.append(Formatta(null, "S", 1, " "));
                bw.append("E");
                bw.append(Formatta(null, "S", 426, " "));
                bw.append(Formatta(ente.getRagione_sociale(), "S", 55, " "));
                bw.append(Formatta(null, "S", 1195, " "));
                bw.append("14");
                bw.append(Formatta(Codice_Fiscale, "D", 11, "0"));

                String conto = ((Liquid_coriComponentSession) createComponentSession()).getContoSpecialeEnteF24(context.getUserContext());
                bw.append(conto);

                bw.append(Formatta(null, "S", 1, " "));
                bw.append(Formatta(null, "S", 60, " ")); // email contribuente

                bw.append("EURO"); //valuta
                BigDecimal tot_f24 = new BigDecimal(0);
                for (Iterator i = lista.iterator(); i.hasNext(); ) {
                    it.cnr.contab.cori00.views.bulk.F24ep_tempTotBulk f24ep = (it.cnr.contab.cori00.views.bulk.F24ep_tempTotBulk) i.next();
                    tot_f24 = tot_f24.add(f24ep.getImporto_debito());
                }
                bw.append(Formatta(new it.cnr.contab.util.EuroFormat().format(tot_f24), "S", 15, " "));
                bw.append("01-01-0000"); //data versamento
                bw.append("A");
                //bw.newLine(); da problemi per il formato del file
                bw.append("\r\n");

                // fine record tipo M
                int riga = 1;
                Integer tot_righe = 1;
                BigDecimal tot1 = new BigDecimal(0);

                for (Iterator i = lista.iterator(); i.hasNext(); ) {
                    it.cnr.contab.cori00.views.bulk.F24ep_tempTotBulk f24ep = (it.cnr.contab.cori00.views.bulk.F24ep_tempTotBulk) i.next();
                    if (riga == 1) {
                        bw.append("V");
                        bw.append(Formatta(Codice_Fiscale, "S", 16, " "));
                        bw.append(Formatta("1", "D", 8, "0"));
                        bw.append(Formatta(null, "S", 3, " "));
                        bw.append(Formatta(null, "S", 25, " "));
                        bw.append(Formatta(null, "S", 20, " "));
                        bw.append(Formatta(null, "S", 16, " "));
                        //modificato
                        //bw.append("6");
                        //bw.append("7");
                        bw.append("D");
                        // SEZIONE VERSAMENTI ???
                        bw.append(Formatta(null, "S", 3, " ")); //CODICE UFFICIO FINANZIARIO
                        bw.append(Formatta(null, "D", 11, "0")); //CODICE ATTO
                        bw.append(Formatta(null, "S", 18, " ")); //IDENTIFICATIVO OPERAZIONE nUOVO
                    }
                    tot1 = tot1.add(f24ep.getImporto_debito());
                    bw.append(Formatta(f24ep.getTipo_riga_f24(), "S", 1, " "));
                    bw.append(Formatta(f24ep.getCodice_tributo(), "S", 6, " "));
                    if (f24ep.getTipo_riga_f24().compareTo(Gruppo_crBulk.ENTI_LOCALI) == 0 ||
                            f24ep.getTipo_riga_f24().compareTo(Gruppo_crBulk.ERARIO) == 0 ||
                            f24ep.getTipo_riga_f24().compareTo(Gruppo_crBulk.REGIONI) == 0 ||
                            f24ep.getTipo_riga_f24().compareTo(Gruppo_crBulk.FONDI_PENSIONE) == 0) {
                        // Vecchie tipologie inalterate
                        bw.append(Formatta(f24ep.getCodice_ente(), "S", 5, " "));
                        bw.append(Formatta(null, "S", 17, " "));  // ESTREMI IDENTIFICATIVI NON COMPILARE PER VECCHIO F24EP
                        bw.append(Formatta(Formatta(f24ep.getMese_rif(), "D", 4, "0"), "S", 6, " "));
                        bw.append(Formatta(Formatta(f24ep.getAnno_rif(), "D", 4, "0"), "S", 6, " "));
                        bw.append(Formatta(f24ep.getImporto_debito().movePointRight(2).toString(), "D", 15, "0"));
                        bw.append(Formatta(null, "D", 15, "0")); // nuovo da gestire
                        //bw.append(Formatta(f24ep.getImporto_credito().movePointRight(2).toString(),"D",15,"0"));
                    } else {
                        //nuovi
                        if (f24ep.getTipo_riga_f24().compareTo(Gruppo_crBulk.INPS) == 0) {
                            String sede_inps = ((Liquid_coriComponentSession) createComponentSession()).getSedeInpsF24(context.getUserContext());
                            bw.append(Formatta(sede_inps, "S", 5, " "));
                            bw.append(Formatta(f24ep.getCd_matricola_inps(), "S", 17, " "));  // ESTREMI IDENTIFICATIVI NON COMPILARE PER VECCHIO F24EP
                            bw.append(Formatta(f24ep.getPeriodo_da(), "S", 6, " "));
                            // non valorizzare per i tributi usati nel CNR
                            bw.append(Formatta(null, "S", 6, " "));
                            bw.append(Formatta(f24ep.getImporto_debito().movePointRight(2).toString(), "D", 15, "0"));
                            bw.append(Formatta(null, "D", 15, "0")); // nuovo da gestire
                            //bw.append(Formatta(f24ep.getImporto_credito().movePointRight(2).toString(),"D",15,"0"));
                        }
                        if (f24ep.getTipo_riga_f24().compareTo(Gruppo_crBulk.INAIL) == 0) {
                            Configurazione_cnrBulk conf_inail = ((Liquid_coriComponentSession) createComponentSession()).getSedeInailF24(context.getUserContext());
                            bw.append(Formatta(conf_inail.getVal01(), "S", 5, " "));
                            // ???? numero posizione assicurativa ???
                            bw.append(Formatta(conf_inail.getVal02(), "S", 17, " "));  // ESTREMI IDENTIFICATIVI NON COMPILARE PER VECCHIO F24EP
                            // ??? causale fonte risoluzione 97
                            bw.append(Formatta("P", "S", 6, " "));
                            bw.append(Formatta(null, "S", 6, " "));
                            bw.append(Formatta(f24ep.getImporto_debito().movePointRight(2).toString(), "D", 15, "0"));
                            bw.append(Formatta(null, "D", 15, "0")); // nuovo da gestire
                            //bw.append(Formatta(f24ep.getImporto_credito().movePointRight(2).toString(),"D",15,"0"));
                        }
                        if (f24ep.getTipo_riga_f24().compareTo(Gruppo_crBulk.INPDAP) == 0) {
                            String sede_inpdap = ((Liquid_coriComponentSession) createComponentSession()).getSedeInpdapF24(context.getUserContext());
                            bw.append(Formatta(sede_inpdap, "S", 5, " "));
                            //??? FACOLTATIVO ???
                            bw.append(Formatta(null, "S", 17, " "));  // ESTREMI IDENTIFICATIVI
                            bw.append(Formatta(f24ep.getPeriodo_da(), "S", 6, " "));
                            bw.append(Formatta(f24ep.getPeriodo_a(), "S", 6, " "));
                            bw.append(Formatta(f24ep.getImporto_debito().movePointRight(2).toString(), "D", 15, "0"));
                            bw.append(Formatta(null, "D", 15, "0")); // nuovo da gestire
                            //bw.append(Formatta(f24ep.getImporto_credito().movePointRight(2).toString(),"D",15,"0"));
                        }
                        if (f24ep.getTipo_riga_f24().compareTo(Gruppo_crBulk.INPGI) == 0) {
                            String sede_inpgi = ((Liquid_coriComponentSession) createComponentSession()).getSedeInpgiF24(context.getUserContext());
                            bw.append(Formatta(null, "S", 5, " "));
                            //??? FACOLTATIVO ???
                            bw.append(Formatta(Formatta(sede_inpgi, "S", 5, "0"), "S", 17, " "));  // ESTREMI IDENTIFICATIVI
                            bw.append(Formatta(Formatta(f24ep.getMese_rif(), "D", 4, "0"), "S", 6, " "));
                            bw.append(Formatta(Formatta(f24ep.getAnno_rif(), "D", 4, "0"), "S", 6, " "));
                            bw.append(Formatta(f24ep.getImporto_debito().movePointRight(2).toString(), "D", 15, "0"));
                            bw.append(Formatta(null, "D", 15, "0")); // nuovo da gestire
                            //bw.append(Formatta(f24ep.getImporto_credito().movePointRight(2).toString(),"D",15,"0"));
                        }
                    }
                    if (riga == 22) {
                        tot_righe = tot_righe + 1;
                        //saldo di sezione
                        bw.append(Formatta(null, "S", 58, " ")); // Nuovo filler
                        bw.append(Formatta(tot1.movePointRight(2).toString(), "D", 15, "0"));
                        bw.append(Formatta(null, "D", 15, "0"));
                        bw.append("P");
                        bw.append(Formatta(tot1.movePointRight(2).toString(), "D", 15, "0"));
                        bw.append(Formatta(null, "S", 4, " "));
                        //fine saldo di sezione
                        //saldo finale
                        bw.append(Formatta(tot1.movePointRight(2).toString(), "D", 15, "0")); //?? congruente
                        bw.append("01010000"); //data versamento
                        bw.append(Formatta(null, "S", 82, " "));
                        bw.append("A");
                        bw.append("\r\n");

                        riga = 1;
                        tot1 = new BigDecimal(0);
                    } else
                        riga = riga + 1;
                }
                // completo ultima riga
                if (riga != 1) {
                    while (riga <= 22) {
                        bw.append(Formatta(null, "S", 1, " "));//getTipo_riga_f24
                        bw.append(Formatta(null, "S", 6, " "));//codice tributo
                        bw.append(Formatta(null, "S", 5, " "));//codice ente
                        bw.append(Formatta(null, "S", 17, " "));// ESTREMI IDENTIFICATIVI NON COMPILARE PER VECCHIO F24EP
                        bw.append(Formatta(null, "S", 6, " "));//mese
                        bw.append(Formatta(null, "S", 6, " "));//anno
                        bw.append(Formatta(null, "D", 15, "0"));//importo a debito
                        bw.append(Formatta(null, "D", 15, "0"));//importo a credito nuovo
                        riga = riga + 1;
                    }
                    //saldo di sezione
                    bw.append(Formatta(null, "S", 58, " ")); // Nuovo filler
                    bw.append(Formatta(tot1.movePointRight(2).toString(), "D", 15, "0"));
                    bw.append(Formatta(null, "D", 15, "0"));
                    bw.append("P");
                    bw.append(Formatta(tot1.movePointRight(2).toString(), "D", 15, "0"));
                    bw.append(Formatta(null, "S", 4, " "));
                    //fine saldo di sezione
                    //saldo finale
                    bw.append(Formatta(tot1.movePointRight(2).toString(), "D", 15, "0")); //?? congruente
                    bw.append("01010000"); //data versamento
                    bw.append(Formatta(null, "S", 82, " "));
                    bw.append("A");
                    bw.append("\r\n");
                } else
                    tot_righe = tot_righe - 1;

                //Coda
                bw.append("Z");
                bw.append(Formatta(null, "S", 14, " "));
                bw.append(Formatta(tot_righe.toString(), "D", 9, "0"));// numero record tipo V
                bw.append(Formatta("1", "D", 9, "0"));// numero record tipo M
                bw.append(Formatta(null, "S", 1864, " "));
                bw.append("A");
                bw.append("\r\n");
                //fine Coda
                bw.flush();
                bw.close();
                osw.close();
                os.close();
                ///
                setFile("/tmp/" + f.getName());
                setMessage("Operazione conclusa correttamente.");
            } else {
                bw.flush();
                bw.close();
                osw.close();
                os.close();
                throw new ApplicationException("Non ci sono dati!");
            }

        } catch (FileNotFoundException e) {
            throw new ApplicationException("File non trovato!");
        } catch (IllegalArgumentException e) {
            throw new ApplicationException("Formato file non valido!");
        } catch (IOException e) {
            throw new ApplicationException("Errore nella scrittura del file!");
        }
    }
}

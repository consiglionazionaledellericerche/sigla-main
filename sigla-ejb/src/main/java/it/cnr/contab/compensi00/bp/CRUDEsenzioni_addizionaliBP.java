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

package it.cnr.contab.compensi00.bp;

import it.cnr.contab.compensi00.ejb.Esenzioni_addizionaliComponentSession;
import it.cnr.contab.compensi00.tabrif.bulk.Esenzioni_addizionaliBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.RecordFormatException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;

public class CRUDEsenzioni_addizionaliBP extends SimpleCRUDBP {

    private final SimpleDetailCRUDController dettagliCRUDController = new SimpleDetailCRUDController("dettaglioCRUDController", Esenzioni_addizionaliBulk.class, "dettagli", this) {

        public void remove(ActionContext actioncontext) throws BusinessProcessException, ValidationException {
            try {
                for (Iterator i = getDettagliCRUDController().getSelectedModels(actioncontext).iterator(); i.hasNext(); ) {
                    Esenzioni_addizionaliBulk esenzione_addizionale = (Esenzioni_addizionaliBulk) i.next();
                    esenzione_addizionale.setToBeDeleted();
                    createComponentSession().eliminaConBulk(actioncontext.getUserContext(), esenzione_addizionale);
                }
            } catch (ComponentException e) {
                handleException(e);
            } catch (RemoteException e) {
                handleException(e);
            }
            super.remove(actioncontext);
        }
    };

    public CRUDEsenzioni_addizionaliBP() {
        super();
    }

    public CRUDEsenzioni_addizionaliBP(String s) {
        super(s);
    }

    /**
     * Disabilito il bottone di ricerca libera.
     */
    public boolean isNewButtonHidden() {

        return true;
    }

    public boolean isFreeSearchButtonHidden() {

        return true;
    }

    /**
     * Disabilito il bottone di ricerca.
     */
    public boolean isSearchButtonHidden() {
        return true;
    }

    public boolean isDeleteButtonHidden() {
        return true;
    }

    public boolean isStartSearchButtonHidden() {
        return true;
    }

    public boolean isSaveButtonEnabled() {
        return true;
    }

    public boolean isEstraiButtonEnabled() {
        Esenzioni_addizionaliBulk bulk = (Esenzioni_addizionaliBulk) getModel();
		return bulk != null && bulk.getDettagli() != null && !bulk.getDettagli().isEmpty();
	}

    public void openForm(javax.servlet.jsp.PageContext context, String action, String target) throws java.io.IOException, javax.servlet.ServletException {

        openForm(context, action, target, "multipart/form-data");
    }

    public void doCarica(ActionContext context, File file) throws BusinessProcessException, ComponentException, IOException {
        java.io.InputStream in;
        Esenzioni_addizionaliBulk bulk = null;
        createComponentSession().cancella_pendenti(context.getUserContext());
        try {
            in = new java.io.BufferedInputStream(new FileInputStream(file), (int) file.length());

            HSSFWorkbook wb = new HSSFWorkbook(in);
            HSSFSheet s = wb.getSheet(wb.getSheetName(0));
            HSSFRow r;
            HSSFCell c;
            String codcat = "";
            String comune = "";
            String prov = "";
            BigDecimal imp = new BigDecimal(-1);
            for (int i = 0; i <= s.getLastRowNum(); i++) {
                r = s.getRow(i);
                if (r == null)
                    throw new ApplicationException("Formato file non valido!");
                c = null;
                codcat = null;
                comune = null;
                prov = null;
                imp = new BigDecimal(-1);
                if (r.getLastCellNum() < 4)
                    throw new ApplicationException("Formato file non valido!");
                c = r.getCell((short) 0);
                if (c != null) {
                    if (c.getCellType() == 1)
                        codcat = c.getStringCellValue();
                    c = r.getCell((short) 1);
                    if (c.getCellType() == 1)
                        comune = c.getStringCellValue();
                    c = r.getCell((short) 2);
                    if (c.getCellType() == 1)
                        prov = c.getStringCellValue();
                    c = r.getCell((short) 3);
                    if (c.getCellType() == 0)
                        imp = new BigDecimal(c.getNumericCellValue());
                }
                if (!((codcat != null && comune != null && prov != null && imp.compareTo(new BigDecimal(-1)) != 0) ||
                        ((codcat == null && comune == null && prov == null && imp.compareTo(new BigDecimal(-1)) == 0))))
                    if (i != 0)
                        throw new ApplicationException("Formato file non valido! Riga: " + new Integer(i + 1));
                    else
                        throw new ApplicationException("Formato file non valido!");
            }
            for (int i = 0; i <= s.getLastRowNum(); i++) {
                r = s.getRow(i);
                c = null;
                codcat = null;
                comune = null;
                prov = null;
                imp = new BigDecimal(-1);
                c = r.getCell((short) 0);
                if (c != null) {
                    if (c.getCellType() == 1)
                        codcat = c.getStringCellValue();
                    c = r.getCell((short) 1);
                    if (c.getCellType() == 1)
                        comune = c.getStringCellValue();
                    c = r.getCell((short) 2);
                    if (c.getCellType() == 1)
                        prov = c.getStringCellValue();
                    c = r.getCell((short) 3);
                    if (c.getCellType() == 0)
                        imp = new BigDecimal(c.getNumericCellValue());
                }
                if (imp.compareTo(new BigDecimal(0)) != -1) {
                    bulk = new Esenzioni_addizionaliBulk();
                    bulk.setCd_catastale(codcat.trim());
                    bulk.setDs_comune(comune.trim());
                    bulk.setCd_provincia(prov);
                    bulk.setImporto(imp.setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                    bulk = createComponentSession().verifica_aggiornamento(context.getUserContext(), bulk);
                    if (bulk != null)
                        getDettagliCRUDController().add(context, bulk);


                }
            }
        } catch (FileNotFoundException e) {
            throw new ApplicationException("File non trovato!");
        } catch (IllegalArgumentException e) {
            throw new ApplicationException("Formato file non valido!");
        } catch (RecordFormatException e) {
            throw new ApplicationException("Errore nella lettura del file!");
        }
    }

    public SimpleDetailCRUDController getDettagliCRUDController() {
        return dettagliCRUDController;
    }

    public Esenzioni_addizionaliComponentSession createComponentSession()
            throws BusinessProcessException {
        return (Esenzioni_addizionaliComponentSession) super.createComponentSession("CNRCOMPENSI00_EJB_Esenzioni_addizionaliComponentSession", Esenzioni_addizionaliComponentSession.class);
    }

    public void Aggiornamento(UserContext context, Esenzioni_addizionaliBulk bulk) throws ComponentException, RemoteException, BusinessProcessException {
        createComponentSession().Aggiornamento(context, bulk);
        setMessage("Salvataggio eseguito in modo corretto.");
    }

    protected it.cnr.jada.util.jsp.Button[] createToolbar() {
        Button[] toolbar = super.createToolbar();
        Button[] newToolbar = new Button[toolbar.length + 1];
        int i;
        for (i = 0; i < toolbar.length; i++)
            newToolbar[i] = toolbar[i];
        newToolbar[i] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.Estrai");
        return newToolbar;
    }
}

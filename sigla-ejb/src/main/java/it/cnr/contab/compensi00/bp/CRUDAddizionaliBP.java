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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.*;
import it.cnr.contab.compensi00.ejb.AddizionaliComponentSession;
import it.cnr.contab.compensi00.tabrif.bulk.AddizionaliBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;
import org.apache.poi.util.RecordFormatException;

public class CRUDAddizionaliBP extends SimpleCRUDBP{

	public CRUDAddizionaliBP() {
		super();
	}

	public CRUDAddizionaliBP(String s) {
		super(s);
	}
	/**
	 *	Disabilito il bottone di ricerca libera.
	 */
	public boolean isNewButtonHidden() {
		
		return true;
	}
	public boolean isFreeSearchButtonHidden() {
		
		return true;
	}
	/**
	 *	Disabilito il bottone di ricerca.
	 */
	public boolean isSearchButtonHidden() {
		return true;
	}
	public boolean isDeleteButtonHidden() {
		return true;
	}
	public boolean isStartSearchButtonHidden(){
		return true;
	}
	public boolean isSaveButtonEnabled(){
		return true;
	}
	public boolean isEstraiButtonEnabled() {
		AddizionaliBulk bulk = (AddizionaliBulk) getModel();
		if (bulk != null && bulk.getDettagli()!=null && !bulk.getDettagli().isEmpty())
		  return true;
		return false;
	}
	public void openForm(javax.servlet.jsp.PageContext context,String action,String target) throws java.io.IOException,javax.servlet.ServletException {

		openForm(context,action,target,"multipart/form-data");
	}
	public void doCarica(ActionContext context,File file) throws BusinessProcessException, ComponentException, IOException {
		java.io.InputStream in;
		AddizionaliBulk bulk=null;
		createComponentSession().cancella_pendenti(context.getUserContext());
		try {
			in = new java.io.BufferedInputStream(new FileInputStream(file),(int)file.length());
			
		  HSSFWorkbook wb =new HSSFWorkbook(in);
		  HSSFSheet s =wb.getSheet(wb.getSheetName(0));
		  HSSFRow r;
		  HSSFCell c;
		  String codcat=new String();
		  String comune=new String();
		  String prov=new String();
		  String ali=new String();
		  BigDecimal ali_num=new BigDecimal(0);
		  for(int i=0;i<=s.getLastRowNum();i++){
			  r=s.getRow(i);	  
			  if (r==null)
				  throw new ApplicationException("Formato file non valido!");
			  c=null;
			  codcat=null;
			  comune=null;
			  prov=null;
			  ali=null;
			  ali_num=null;
			  if(r.getLastCellNum()<4)
				  throw new ApplicationException("Formato file non valido!");
				  c = r.getCell((short)0);
				  if (c.getCellType()==1)
					  codcat=c.getStringCellValue();
				  c = r.getCell((short)1);
				  if (c.getCellType()==1)
					  comune=c.getStringCellValue();
				  c = r.getCell((short)2);
				  if (c.getCellType()==1)
					  prov=c.getStringCellValue(); 
				  c = r.getCell((short)3);
				  if (c.getCellType()==1)
					  ali=c.getStringCellValue();	
				  else if (c.getCellType()==0)
					  ali_num=new BigDecimal(c.getNumericCellValue()).setScale(2,java.math.BigDecimal.ROUND_HALF_UP);
				  if (!((codcat!=null && comune!=null && prov!=null &&(ali!=null || ali_num!=null))||
						  (codcat==null && comune==null && prov==null &&(ali==null || ali_num==null)))) 	  
				  throw new ApplicationException("Formato file non valido!");		  
		  }  
		  for(int i=0;i<=s.getLastRowNum();i++){
			  r=s.getRow(i);	  
			  c=null;
			  codcat=null;
			  comune=null;
			  prov=null;
			  ali=null;			  		  
			  ali_num=null;
				  c = r.getCell((short)0);
				  if (c.getCellType()==1)
					  codcat=c.getStringCellValue();
				  c = r.getCell((short)1);
				  if (c.getCellType()==1)
					  comune=c.getStringCellValue();
				  c = r.getCell((short)2);
				  if (c.getCellType()==1)
					  prov=c.getStringCellValue(); 
				  c = r.getCell((short)3);
				  if (c.getCellType()==1)
					  ali=c.getStringCellValue();
				  else if (c.getCellType()==0)
					  ali_num=new BigDecimal(c.getNumericCellValue()).setScale(2,java.math.BigDecimal.ROUND_HALF_UP);
				  
			  if(ali!= null && ali.compareTo("0*")==0)
				  //  ali= null;
				  // 04/02/2016 dal 2016 lo 0* significa che non esiste delebera e quindi l'aliquota è 0
				  ali= "0";
			  if(ali==null && ali_num!=null)
				  ali=ali_num.toString();
			  if(ali!=null){
				  bulk = new AddizionaliBulk();
				  bulk.setCd_catastale(codcat.trim());
				  bulk.setDs_comune(comune.trim());
				  bulk.setCd_provincia(prov);
				  bulk.setAliquota(new BigDecimal(ali.replace(",",".")));
				  bulk=createComponentSession().verifica_aggiornamento(context.getUserContext(),(AddizionaliBulk)bulk);
				  if(bulk !=null)				
					  getDettagliCRUDController().add(context,bulk);
			  }
		  }
		} catch (FileNotFoundException e) {
			  throw new ApplicationException("File non trovato!");
		}
		catch (IllegalArgumentException e) {
			throw new ApplicationException("Formato file non valido!");
		}
		catch (RecordFormatException e) {
			throw new ApplicationException("Errore nella lettura del file!");
		}
	}
	private final SimpleDetailCRUDController dettagliCRUDController = new SimpleDetailCRUDController("dettaglioCRUDController",AddizionaliBulk.class,"dettagli",this){
		 
		public void remove(ActionContext actioncontext) throws  BusinessProcessException, ValidationException {
			try {
				for(Iterator i=getDettagliCRUDController().getSelectedModels(actioncontext).iterator();i.hasNext();){
					AddizionaliBulk addizionale=(AddizionaliBulk)i.next();
					addizionale.setToBeDeleted();
					createComponentSession().eliminaConBulk(actioncontext.getUserContext(),addizionale);
				}
			} catch (ComponentException e) {
				handleException(e);
			} catch (RemoteException e) {
				handleException(e);			
			}
			super.remove(actioncontext);
		}
	};
	public SimpleDetailCRUDController getDettagliCRUDController() {
		return dettagliCRUDController;
	}
  public AddizionaliComponentSession createComponentSession()
      throws BusinessProcessException
  {
      return (AddizionaliComponentSession)super.createComponentSession("CNRCOMPENSI00_EJB_AddizionaliComponentSession", AddizionaliComponentSession.class);
  }
  	public void Aggiornamento_scaglione(UserContext context,AddizionaliBulk bulk) throws ComponentException, RemoteException, BusinessProcessException{
  		createComponentSession().Aggiornamento_scaglione(context,bulk);
  		setMessage("Salvataggio eseguito in modo corretto.");
  	}
  	protected it.cnr.jada.util.jsp.Button[] createToolbar() 
	{		
		Button[] toolbar = super.createToolbar();
		Button[] newToolbar = new Button[ toolbar.length + 1];
		int i;
		for ( i = 0; i < toolbar.length; i++ )
			newToolbar[i] = toolbar[i];
		newToolbar[ i ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.Estrai");		
		return newToolbar;
	}
}

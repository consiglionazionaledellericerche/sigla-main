package it.cnr.contab.docamm00.consultazioni.bp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.poi.hssf.record.RecordFormatException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.compensi00.bp.AbstractEstrazioneFiscaleBP;
import it.cnr.contab.docamm00.consultazioni.bulk.V_estrai_glaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;


public class Estrazione_glaBP extends AbstractEstrazioneFiscaleBP
{	
	
	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[3];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.ricerca");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.elabora");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.stampa");
		return toolbar;
	}
	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		
		V_estrai_glaBulk bulk = new V_estrai_glaBulk();
		bulk.setAnagrafico(new AnagraficoBulk());
		bulk.setEsercizio_pagamento(new Long(CNRUserContext.getEsercizio(context.getUserContext())));
		setModel(context,bulk);
		super.init(config,context);
	}
	   
	public boolean isRicercaButtonEnabled()
	{
		return true;
	}
	public boolean isStampaButtonEnabled()
	{
		V_estrai_glaBulk bulk=null;
		if(this.getModel()!=null){
			 bulk=(V_estrai_glaBulk)getModel();
		    if(bulk.getFile()!=null)
		    	return true;
		    else
		    	return false;
		}else
				return false;
		
	}
	
	/* 
	 * Necessario per la creazione di una form con enctype di tipo "multipart/form-data"
	 * Sovrascrive quello presente nelle superclassi
	 * 
	*/

	public void openForm(javax.servlet.jsp.PageContext context,String action,String target) throws java.io.IOException,javax.servlet.ServletException {

			openForm(context,action,target,"multipart/form-data");
	
	}
	public void doCarica(ActionContext context,File file) throws BusinessProcessException, ComponentException, IOException {
		java.io.InputStream in = new java.io.BufferedInputStream(new FileInputStream(file),(int)file.length());
		  HSSFWorkbook wb =new HSSFWorkbook(in);
		  HSSFSheet s =wb.getSheet(wb.getSheetName(0));
		  HSSFRow row;
		  HSSFCell c;
		  try{			  
			     //File to store data in form of CSV
			  
			      File f = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",file.getName().substring(0, file.getName().length()-4)+".csv");
			      OutputStream os = (OutputStream)new FileOutputStream(f);
			      OutputStreamWriter osw = new OutputStreamWriter(os);
			      BufferedWriter bw = new BufferedWriter(osw);
			      for (int i = 0 ; i <= s.getLastRowNum(); i++) {
			        row = s.getRow(i);
			        if (row.getLastCellNum() > 0) {
			            for (int j = 0; j <= row.getLastCellNum(); j++){
			            	c=row.getCell((short)j);
			            	if(c!=null){
				            	if (c.getCellType()==HSSFCell.CELL_TYPE_STRING)
				            		bw.append(c.getStringCellValue());
				            	else if (c.getCellType()==HSSFCell.CELL_TYPE_NUMERIC)
				            		if (HSSFDateUtil.isCellDateFormatted(c)){
				            				Date dateValue = c.getDateCellValue();
				            				bw.append((new SimpleDateFormat("dd/MM/yyyy")).format(dateValue));
				            		}else
				            		bw.append(new BigDecimal(c.getNumericCellValue()).setScale(2,java.math.BigDecimal.ROUND_HALF_EVEN).toString());
			            	}
				            bw.write(';');
			            }
			         }
			         bw.newLine();
		        }
			    bw.flush();
			    
			    bw.close();
			    osw.close();
			    os.close();
				try {
					Map parameters = new HashMap();							        
					JRCsvDataSource source = new JRCsvDataSource(f);
		            source.setFieldDelimiter(';');
		            
		            source.setUseFirstRowAsHeader(true);
		            source.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
		            
		            JasperDesign jasperDesign = JRXmlLoader.load(System.getProperty("tmp.dir.SIGLAWeb")+"/reports/Estrazione_gla_csv.jrxml");
		            JasperReport report = net.sf.jasperreports.engine.JasperCompileManager.compileReport(jasperDesign);
		            JasperPrint jasperPrint = net.sf.jasperreports.engine.JasperFillManager.fillReport(report,parameters,source); 			
		            JasperExportManager.exportReportToPdfFile(jasperPrint,System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/"+file.getName().substring(0, file.getName().length()-4)+".pdf");
		            //r.p. sembra non funzionare
		            //f.delete();
		            V_estrai_glaBulk bulk =(V_estrai_glaBulk)getModel();
		            bulk.setFile("/tmp/"+file.getName().substring(0, file.getName().length()-4)+".pdf");
		           
				} catch (JRException e) {
					throw new ApplicationException("Errore: "+e.getMessage());
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
	@Override
	public void writeToolbar(PageContext pagecontext) throws IOException, ServletException {
		Button[] toolbar = getToolbar();
		V_estrai_glaBulk bulk =(V_estrai_glaBulk)getModel();
	        HttpServletRequest httpservletrequest = (HttpServletRequest)pagecontext.getRequest();
	        StringBuffer stringbuffer = new StringBuffer();
	        stringbuffer.append(pagecontext.getRequest().getScheme());
	        stringbuffer.append("://");
	        stringbuffer.append(pagecontext.getRequest().getServerName());
	        stringbuffer.append(':');
	        stringbuffer.append(pagecontext.getRequest().getServerPort());
	        stringbuffer.append(JSPUtils.getAppRoot(httpservletrequest));
			toolbar[2].setHref("javascript:doPrint('"+stringbuffer+ bulk.getFile() + "')");
		super.writeToolbar(pagecontext);
	}
}

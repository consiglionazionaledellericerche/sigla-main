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

/*
 * Created on Jul 22, 2005
 *
 * Crea un file e lo riempie con i valori del BP passato
 */
package it.cnr.contab.util;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.RemoteIteratorEnumeration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;      
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.NoSuchObjectException;
import java.sql.Timestamp;
import java.util.Dictionary;
import java.util.Enumeration;

import org.apache.poi.hssf.usermodel.*;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExcelFile extends java.io.File{
	private static final Integer NUMERO_MAX_RIGHE = new Integer(65000);
	private static final String  ESTENSIONE = ".xls";

	/**
	 * @param Nome del file da creare senza l'indicazione dell'estensione
	 */
	public ExcelFile(String arg0) {
		super(arg0.concat(ESTENSIONE));
	}
	
	/**
 	* @param arg0
 	* @param arg1
 	* @return
 	* @throws java.io.IOException
 	*/
	public static File createTempFile(String arg0, String arg1)
		throws IOException {
		return File.createTempFile(arg0, ESTENSIONE);
	}
	
	public it.cnr.contab.util.ExcelFile caricaFile(String longDescription,Dictionary columns,RemoteIterator remoteIterator,String user) {
//	public static File caricaFile(String longDescription,Dictionary columns,RemoteIterator remoteIterator,File excelFile, String user) {
      try{
		  HSSFWorkbook wb = new HSSFWorkbook(); // Istanzio la classe workbook
		  HSSFSheet s = wb.createSheet(); // creo un foglio
		  HSSFRow r = null; // dichiaro r di tipo riga
		  HSSFCell c = null; // dichiaro c di tipo cella
		  s.setDefaultColumnWidth((short)20);
		  HSSFCellStyle cellStyle = wb.createCellStyle();
		  cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
		  HSSFFont font = wb.createFont();
		  font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		  cellStyle.setFont(font);
		  wb.setSheetName(0, longDescription,HSSFWorkbook.ENCODING_COMPRESSED_UNICODE );
		  short cellnum = (short) -1;
		  r = s.createRow(0); //creo la prima riga
		  for(Enumeration enumeration1 = columns.elements(); enumeration1.hasMoreElements();)//comincio col creare l'intestazione delle colonne
		  {
			  ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty)enumeration1.nextElement();
			  cellnum++;
			  c = r.createCell(cellnum);
			  c.setCellValue(columnfieldproperty.getLabel());
			  c.setCellStyle(cellStyle);
			  c.setCellType(HSSFCell.CELL_TYPE_STRING);
		  }
		  int j = 0;
		  for(Enumeration enumeration2 =  new RemoteIteratorEnumeration(remoteIterator); enumeration2.hasMoreElements(); j++)
		  {
			  Object obj = enumeration2.nextElement();
			  r = s.createRow(j+1);
			  cellnum = (short) -1;
			  for(Enumeration enumeration3 = columns.elements(); enumeration3.hasMoreElements();)
			  {
				  cellnum++;
				  ColumnFieldProperty columnfieldproperty1 = (ColumnFieldProperty)enumeration3.nextElement();
				  c = r.createCell((short)cellnum);
				  Object obj2 = Introspector.getPropertyValue(obj,columnfieldproperty1.getProperty());
				  String valoreStringa = columnfieldproperty1.getStringValueFrom(obj,Introspector.getPropertyValue(obj, columnfieldproperty1.getProperty()));
				  if(obj2 != null){									
					if (obj2 instanceof String){								   
					  c.setCellType(HSSFCell.CELL_TYPE_STRING);
					  c.setCellValue(valoreStringa);
					}else if (obj2 instanceof Number){								   
					  c.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					  c.setCellValue(new BigDecimal(obj2.toString()).doubleValue());
					}else if (obj2 instanceof Timestamp){								   
					  c.setCellType(HSSFCell.CELL_TYPE_STRING);
					  c.setCellValue(valoreStringa);
					}else{								   
					  c.setCellType(HSSFCell.CELL_TYPE_STRING);
					  c.setCellValue(valoreStringa);
					}    
				}  
		     }
		  }	
//		  FileOutputStream excelOutput = new FileOutputStream(excelFile);
	      FileOutputStream excelOutput = new FileOutputStream(this);
		  wb.write(excelOutput);// assegno lo stream al FileOutputStream
		  excelOutput.close(); // chiudo il file
		}
		catch (FileNotFoundException e) {
			throw new ComponentException(e.getMessage());
		}
		catch(Throwable throwable){
		  throw new ComponentException(throwable.getMessage());
		}		  
		finally {
			try{
			  Object obj = remoteIterator;
			  if(obj instanceof it.cnr.jada.ejb.BulkLoaderIterator){
			   ((it.cnr.jada.ejb.BulkLoaderIterator)obj).close();
			   ((it.cnr.jada.ejb.BulkLoaderIterator)obj).ejbRemove();			  
			  }
			  else if(obj instanceof it.cnr.jada.ejb.TransactionalBulkLoaderIterator){
				  ((it.cnr.jada.ejb.TransactionalBulkLoaderIterator)obj).ejbRemove();
			  }	
			}
			catch(NoSuchObjectException ex){	
			}
			catch(Throwable _ex) {
			}
	    return this;
//		return excelFile;
		}		  
	}
}

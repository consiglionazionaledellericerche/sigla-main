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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 13/09/2007
 */
package it.cnr.contab.anagraf00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class EcfBase extends EcfKey implements Keyed {
//    CL002001 VARCHAR(20)
	private java.lang.String cl002001;
 
//    CL003001 VARCHAR(20)
	private java.lang.String cl003001;
 
//    CL004001 DECIMAL(22,0)
	private java.math.BigDecimal cl004001;
 
//    CL004002 DECIMAL(22,0)
	private java.math.BigDecimal cl004002;
 
//    CL005001 DECIMAL(22,0)
	private java.math.BigDecimal cl005001;
 
//    CL006001 DECIMAL(22,0)
	private java.math.BigDecimal cl006001;
 
//    CL007001 DECIMAL(22,0)
	private java.math.BigDecimal cl007001;
 
//    CL008001 DECIMAL(22,0)
	private java.math.BigDecimal cl008001;
 
//    CL008002 DECIMAL(22,0)
	private java.math.BigDecimal cl008002;
 
//    CL009001 DECIMAL(22,0)
	private java.math.BigDecimal cl009001;
 
//    CL010001 DECIMAL(22,0)
	private java.math.BigDecimal cl010001;
 
//    CL011001 DECIMAL(22,0)
	private java.math.BigDecimal cl011001;
 
//    FR002001 VARCHAR(20)
	private java.lang.String fr002001;
 
//    FR003001 VARCHAR(20)
	private java.lang.String fr003001;
 
//    FR004001 DECIMAL(22,0)
	private java.math.BigDecimal fr004001;
 
//    FR004002 DECIMAL(22,0)
	private java.math.BigDecimal fr004002;
 
//    FR005001 DECIMAL(22,0)
	private java.math.BigDecimal fr005001;
 
//    FR006001 DECIMAL(22,0)
	private java.math.BigDecimal fr006001;
 
//    FR007001 DECIMAL(22,0)
	private java.math.BigDecimal fr007001;
 
//    FR008001 DECIMAL(22,0)
	private java.math.BigDecimal fr008001;
 
//    FR009001 DECIMAL(22,0)
	private java.math.BigDecimal fr009001;
 
//    FR009002 DECIMAL(22,0)
	private java.math.BigDecimal fr009002;
 
//    FR010001 DECIMAL(22,0)
	private java.math.BigDecimal fr010001;
 
//    FR011001 DECIMAL(22,0)
	private java.math.BigDecimal fr011001;
 
//    FR012001 DECIMAL(22,0)
	private java.math.BigDecimal fr012001;
 
//    FR013001 DECIMAL(22,0)
	private java.math.BigDecimal fr013001;
 
//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
	private Long prog_estrazione; 
 
	public EcfBase() {
		super();
	}
	public EcfBase(java.lang.Long prog) {
		super(prog);
	}
	public java.lang.String getCl002001() {
		return cl002001;
	}
	public void setCl002001(java.lang.String cl002001)  {
		this.cl002001=cl002001;
	}
	public java.lang.String getCl003001() {
		return cl003001;
	}
	public void setCl003001(java.lang.String cl003001)  {
		this.cl003001=cl003001;
	}
	public java.math.BigDecimal getCl004001() {
		return cl004001;
	}
	public void setCl004001(java.math.BigDecimal cl004001)  {
		this.cl004001=cl004001;
	}
	public java.math.BigDecimal getCl004002() {
		return cl004002;
	}
	public void setCl004002(java.math.BigDecimal cl004002)  {
		this.cl004002=cl004002;
	}
	public java.math.BigDecimal getCl005001() {
		return cl005001;
	}
	public void setCl005001(java.math.BigDecimal cl005001)  {
		this.cl005001=cl005001;
	}
	public java.math.BigDecimal getCl006001() {
		return cl006001;
	}
	public void setCl006001(java.math.BigDecimal cl006001)  {
		this.cl006001=cl006001;
	}
	public java.math.BigDecimal getCl007001() {
		return cl007001;
	}
	public void setCl007001(java.math.BigDecimal cl007001)  {
		this.cl007001=cl007001;
	}
	public java.math.BigDecimal getCl008001() {
		return cl008001;
	}
	public void setCl008001(java.math.BigDecimal cl008001)  {
		this.cl008001=cl008001;
	}
	public java.math.BigDecimal getCl008002() {
		return cl008002;
	}
	public void setCl008002(java.math.BigDecimal cl008002)  {
		this.cl008002=cl008002;
	}
	public java.math.BigDecimal getCl009001() {
		return cl009001;
	}
	public void setCl009001(java.math.BigDecimal cl009001)  {
		this.cl009001=cl009001;
	}
	public java.math.BigDecimal getCl010001() {
		return cl010001;
	}
	public void setCl010001(java.math.BigDecimal cl010001)  {
		this.cl010001=cl010001;
	}
	public java.math.BigDecimal getCl011001() {
		return cl011001;
	}
	public void setCl011001(java.math.BigDecimal cl011001)  {
		this.cl011001=cl011001;
	}
	public java.lang.String getFr002001() {
		return fr002001;
	}
	public void setFr002001(java.lang.String fr002001)  {
		this.fr002001=fr002001;
	}
	public java.lang.String getFr003001() {
		return fr003001;
	}
	public void setFr003001(java.lang.String fr003001)  {
		this.fr003001=fr003001;
	}
	public java.math.BigDecimal getFr004001() {
		return fr004001;
	}
	public void setFr004001(java.math.BigDecimal fr004001)  {
		this.fr004001=fr004001;
	}
	public java.math.BigDecimal getFr004002() {
		return fr004002;
	}
	public void setFr004002(java.math.BigDecimal fr004002)  {
		this.fr004002=fr004002;
	}
	public java.math.BigDecimal getFr005001() {
		return fr005001;
	}
	public void setFr005001(java.math.BigDecimal fr005001)  {
		this.fr005001=fr005001;
	}
	public java.math.BigDecimal getFr006001() {
		return fr006001;
	}
	public void setFr006001(java.math.BigDecimal fr006001)  {
		this.fr006001=fr006001;
	}
	public java.math.BigDecimal getFr007001() {
		return fr007001;
	}
	public void setFr007001(java.math.BigDecimal fr007001)  {
		this.fr007001=fr007001;
	}
	public java.math.BigDecimal getFr008001() {
		return fr008001;
	}
	public void setFr008001(java.math.BigDecimal fr008001)  {
		this.fr008001=fr008001;
	}
	public java.math.BigDecimal getFr009001() {
		return fr009001;
	}
	public void setFr009001(java.math.BigDecimal fr009001)  {
		this.fr009001=fr009001;
	}
	public java.math.BigDecimal getFr009002() {
		return fr009002;
	}
	public void setFr009002(java.math.BigDecimal fr009002)  {
		this.fr009002=fr009002;
	}
	public java.math.BigDecimal getFr010001() {
		return fr010001;
	}
	public void setFr010001(java.math.BigDecimal fr010001)  {
		this.fr010001=fr010001;
	}
	public java.math.BigDecimal getFr011001() {
		return fr011001;
	}
	public void setFr011001(java.math.BigDecimal fr011001)  {
		this.fr011001=fr011001;
	}
	public java.math.BigDecimal getFr012001() {
		return fr012001;
	}
	public void setFr012001(java.math.BigDecimal fr012001)  {
		this.fr012001=fr012001;
	}
	public java.math.BigDecimal getFr013001() {
		return fr013001;
	}
	public void setFr013001(java.math.BigDecimal fr013001)  {
		this.fr013001=fr013001;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public Long getProg_estrazione() {
		return prog_estrazione;
	}
	public void setProg_estrazione(Long prog_estrazione) {
		this.prog_estrazione = prog_estrazione;
	}
}
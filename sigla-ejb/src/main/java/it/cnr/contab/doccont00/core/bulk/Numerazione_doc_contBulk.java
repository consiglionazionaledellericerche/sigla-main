package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Numerazione_doc_contBulk extends Numerazione_doc_contBase {
	static final public String TIPO_OBB  		= "OBB";
	static final public String TIPO_OBB_PGIRO  	= "OBB_PGIRO";
	static final public String TIPO_OBB_RES     = "OBB_RES";
	static final public String TIPO_OBB_RES_IMPROPRIA = "OBB_RESIM";
	static final public String TIPO_IMP  		= "IMP";
	static final public String TIPO_IMP_RES		= "IMP_RES";	
	static final public String TIPO_ACR  		= "ACR";
	static final public String TIPO_ACR_PLUR  	= "ACR_PLUR";		
	static final public String TIPO_ACR_RES		= "ACR_RES";	
	static final public String TIPO_ACR_PGIRO  	= "ACR_PGIRO";
	static final public String TIPO_ACR_PGIR_R	= "ACR_PGIR_R";
	static final public String TIPO_ACR_SIST  	= "ACR_SIST";	
	static final public String TIPO_MAN  		= "MAN";	
	static final public String TIPO_REV  		= "REV";
	static final public String TIPO_REV_PROVV  	= "REV_PROVV";
	static final public String TIPO_DIST  		= "DIST";	
	
public Numerazione_doc_contBulk() {
	super();
}
public Numerazione_doc_contBulk(java.lang.String cd_cds,java.lang.String cd_tipo_documento_cont,java.lang.Integer esercizio) {
	super(cd_cds,cd_tipo_documento_cont,esercizio);
}
}

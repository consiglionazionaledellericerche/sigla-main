<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<SCRIPT LANGUAGE="Javascript">
function escludi(index){
	var names=new Array();
	var num_flags=2;
	names[1]="main.fl_gestione_inventario";
	names[2]="main.fl_gestione_magazzino";
	
	for (i=1; i<=num_flags;i++)
		if (index!=i)
			document.mainForm.elements(names[i]).checked=false;	
}
</SCRIPT>
<script language="javascript" src="scripts/css.js"></script>
<title>Categoria Gruppo Inventario</title>
</head>
<body class="Form">

<% it.cnr.contab.docamm00.bp.CRUDCategoriaGruppoInventBP bp = (it.cnr.contab.docamm00.bp.CRUDCategoriaGruppoInventBP)BusinessProcess.getBusinessProcess(request);%>
<% it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk cgi = (it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk)bp.getModel();%>
<%	 bp.openFormWindow(pageContext); %>
   
	<div class="Group">
		<table class="Panel">
			<tr>
				<td><% bp.getController().writeFormLabel( out, "cd_proprio"); %></td>
				<td><% bp.getController().writeFormInput( out, "cd_proprio"); %></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel( out, "ds_categoria_gruppo"); %></td>
					<td colspan="3"> <% bp.getController().writeFormInput( out, "ds_categoria_gruppo"); %></td>
			</tr>
	       <tr>
				<td><% bp.getController().writeFormInput( out, null,"fl_gestione_inventario",false,null,"onclick=\"escludi(1);submitForm('doDefault')\"");%>
				<% bp.getController().writeFormLabel( out, "fl_gestione_inventario"); %></td>
				<td><% bp.getController().writeFormInput( out, null,"fl_gestione_magazzino",false,null,"onclick=\"escludi(2);submitForm('doDefault')\"");%>
				<% bp.getController().writeFormLabel( out, "fl_gestione_magazzino"); %></td>
			</tr>
			<tr>
				<%if (cgi.getFl_gestione_inventario()!=null && cgi.getFl_gestione_inventario().booleanValue()){%>
					<td><% bp.getController().writeFormInput( out, null,"fl_ammortamento",false,null,"");%>
						<% bp.getController().writeFormLabel( out, "fl_ammortamento"); %></td>	    
					<td><% bp.getController().writeFormInput( out, null,"fl_gestione_targa",false,null,"");%>
						<% bp.getController().writeFormLabel( out, "fl_gestione_targa"); %></td> 
					<td><% bp.getController().writeFormInput( out, null,"fl_gestione_seriale",false,null,"");%>
						<% bp.getController().writeFormLabel( out, "fl_gestione_seriale"); %></td>	   
							   
				<%}%>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"cd_nodo_padre");%>
				</td>
				<td colspan="5"> 
					<% bp.getController().writeFormInput(out,"cd_nodo_padre");%>
					<% bp.getController().writeFormInput(out,"ds_nodo_padre");%>
					<% bp.getController().writeFormInput(out,"find_nodo_padre");%>
				</td>
			</tr>
    			<tr>
						<td colspan="5"> 
							<% bp.getCrudAssociazioneVoci().writeHTMLTable(pageContext,"associazioneVoci",true,false,true,"100%","100px",true);%>
						</td>
					</tr> 
				<tr>				
				<td>
					<% bp.getCrudAssociazioneVoci().writeFormLabel(out,"elemento_voce");%>
				</td> 
				<td colspan="3"> 
					<% bp.getCrudAssociazioneVoci().writeFormInput(out,"cd_elemento_voce");%>
					<% bp.getCrudAssociazioneVoci().writeFormInput(out,"ds_elemento_voce");%>
					<% bp.getCrudAssociazioneVoci().writeFormInput(out,"elemento_voce");%> 
				</td>
          </tr>
	
	    	</table>
	    	<%	bp.closeFormWindow(pageContext); %>
	</div>
</body>
</html>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*,
	        it.cnr.jada.util.action.*,
	        it.cnr.contab.config00.bp.*"
%>
<body class="Form">
<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request); %>
	<table class="Panel">
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_unita_padre"); %></td>			 
	<td>
	  <% bp.getController().writeFormInputByStatus( out, "cd_unita_padre"); %>
	  <% bp.getController().writeFormInputByStatus( out, "ds_unita_padre"); %>
      <% bp.getController().writeFormInputByStatus( out, "find_unita_padre"); %>
      <% bp.getController().writeFormInput( out, "crea_unita_padre"); %>	
	</td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_proprio_unita"); %></td>	
	<td><% bp.getController().writeFormInputByStatus( out, "cd_proprio_unita");
	    	 bp.getController().writeFormInput(out,"default","fl_rubrica",false,null,"onclick=\"submitForm('doCheckFl_rubrica')\"");
		     bp.getController().writeFormLabel( out, "fl_rubrica"); %>
	</td> 
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "esercizio_inizio"); %></td>
	<td><% bp.getController().writeFormInputByStatus( out, "esercizio_inizio"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_unita_organizzativa"); %></td>	
	<td><% bp.getController().writeFormInput( out, "cd_unita_organizzativa");%> </td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "ds_unita_organizzativa"); %></td>	
	<td><% bp.getController().writeFormInput( out, "ds_unita_organizzativa"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_uoDiRiferimento"); %></td>				 
	<td>
	    <% bp.getController().writeFormInput( out, "cd_uoDiRiferimento"); %>
	    <% bp.getController().writeFormInput( out, "ds_uoDiRiferimento"); %>
	    <% bp.getController().writeFormInput( out, "find_uoDiRiferimento"); %>
	</td>			 
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_responsabile"); %>
	</td>	
	<td>
	    <% bp.getController().writeFormInput( out, "cd_responsabile"); %>
  	    <% bp.getController().writeFormInput( out, "ds_responsabile"); %>	
	    <% bp.getController().writeFormInput( out, "find_responsabile"); %>
            <% bp.getController().writeFormInput( out, "crea_responsabile"); %>			
	</td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_responsabile_amm"); %>
	</td>	
	<td>
	  <% bp.getController().writeFormInput( out, "cd_responsabile_amm"); %>
          <% bp.getController().writeFormInput( out, "ds_responsabile_amm"); %>
          <% bp.getController().writeFormInput( out, "find_responsabile_amm"); %>
          <% bp.getController().writeFormInput( out, "crea_responsabile_amm"); %>		
	</td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "esercizio_fine"); %></td>	
		<td><% bp.getController().writeFormInput( out, "esercizio_fine"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "id_funzione_pubblica"); %></td>	
		<td><% bp.getController().writeFormInput( out, "id_funzione_pubblica"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "codiceaooipa"); %></td>
		<td><% bp.getController().writeFormInput( out, "codiceaooipa"); %></td>
	</tr>
	</table>
</body>
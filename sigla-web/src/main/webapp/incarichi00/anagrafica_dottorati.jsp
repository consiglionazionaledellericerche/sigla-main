<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*"
%>

<%
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
</head>

<body class="Form">
<% bp.openFormWindow(pageContext);%>
    <div class="card p-2 w-100">
        <table class="Panel w-100">
        <tr><td>
<div class="Group card p-2 mt-2" style="width:100%">
    <fieldset>
        <legend style="color:red">Identificativo</legend>
    	<table>
    		<tr>
    			<td><% bp.getController().writeFormLabel(out, "id"); %>	</td>
    			<td><% bp.getController().writeFormInput(out,"id");%></td>
    			<td><% bp.getController().writeFormLabel(out, "find_PhdtipoDottorati"); %></td>
                <td><% bp.getController().writeFormInput(out, "find_PhdtipoDottorati"); %></td>
    		</tr>
    	</table>
    </fieldset>
    <fieldset>
        <legend style="color:red">Universit&aacute;</legend>
        <table>
            <tr>
            	<td><% bp.getController().writeFormLabel(out, "regione");%></td>
            	<td><% bp.getController().writeFormInput(out, "regione");%></td>
            </tr>
    	    <tr>
    			<td><% bp.getController().writeFormLabel(out, "universitaCapofila");%></td>
    			<td><% bp.getController().writeFormInput(out, "universitaCapofila");%></td>
    		</tr>
    		<tr>
    			<td><% bp.getController().writeFormLabel(out, "cdTerzo"); %></td>
    			<td><% bp.getController().writeFormInput(out, "cdTerzo"); %></td>
    		</tr>
            <tr>
    			<td><% bp.getController().writeFormLabel(out, "telefonoAteneo"); %></td>
    		    <td><% bp.getController().writeFormInput(out, "telefonoAteneo"); %></td>
    		</tr>
    		<tr>
    		    <td><% bp.getController().writeFormLabel(out, "emailAteneo"); %></td>
                <td><% bp.getController().writeFormInput(out, "emailAteneo"); %></td>
    		</tr>
    		<tr>
    			<td><% bp.getController().writeFormLabel(out, "altreUniversita"); %></td>
                <td><% bp.getController().writeFormInput(out, "altreUniversita"); %></td>
    		</tr>
    		<tr>
                <td><% bp.getController().writeFormLabel(out, "find_TipocorsoDottorati"); %></td>
                <td><% bp.getController().writeFormInput(out, "find_TipocorsoDottorati"); %></td>
            </tr>
    	</table>
    	</fieldset>
        <fieldset>
            <legend style="color:red">Dati Azienda</legend>
            <table>
                <tr>
    			    <td><% bp.getController().writeFormLabel(out, "azienda"); %></td>
                    <td><% bp.getController().writeFormInput(out, "azienda"); %></td>
    			</tr>
                <tr>
    			    <td><% bp.getController().writeFormLabel(out, "aziendaSettore"); %></td>
                    <td><% bp.getController().writeFormInput(out, "aziendaSettore"); %></td>
    			</tr>
                <tr>
    			    <td><% bp.getController().writeFormLabel(out, "aziendaSede"); %></td>
                    <td><% bp.getController().writeFormInput(out, "aziendaSede"); %></td>
    			</tr>
    		</table>
        </fieldset>
        <fieldset>
            <legend style="color:red">Dati Infrastruttura Europea</legend>
            <table>
                <tr>
    			    <td><% bp.getController().writeFormLabel(out, "nomeInfraEuropea"); %></td>
                    <td><% bp.getController().writeFormInput(out, "nomeInfraEuropea"); %></td>
    			</tr>
                <tr>
    			    <td><% bp.getController().writeFormLabel(out, "descrInfraEuropea"); %></td>
                    <td><% bp.getController().writeFormInput(out, "descrInfraEuropea"); %></td>
    			</tr>
    		</table>
    	</fieldset>
    	<fieldset>
            <legend style="color:red">Dati Borse</legend>
            <table>
                 <tr>
                     <td><% bp.getController().writeFormLabel(out, "numeroBorseFinanziate"); %></td>
                     <td><% bp.getController().writeFormInput(out, "numeroBorseFinanziate"); %></td>
                 </tr>
                 <tr>
                     <td><% bp.getController().writeFormLabel(out, "find_CicloDottorati"); %></td>
                     <td><% bp.getController().writeFormInput(out, "find_CicloDottorati"); %></td>
                 </tr>
    			 <tr>
    			     <td><% bp.getController().writeFormLabel(out, "numeroCicliFinanziati"); %></td>
                     <td><% bp.getController().writeFormInput(out, "numeroCicliFinanziati"); %></td>
    			 </tr>
            </table>
        </fieldset>
        <fieldset>
        <legend style="color:red">Dati Corso</legend>
    		<table>
                <tr>
    		        <td><% bp.getController().writeFormLabel(out, "dipartimentoUniversita"); %></td>
                    <td><% bp.getController().writeFormInput(out, "dipartimentoUniversita"); %></td>
    		    </tr>
    			<tr>
    			    <td><% bp.getController().writeFormLabel(out, "corsoDottorato"); %></td>
                    <td><% bp.getController().writeFormInput(out, "corsoDottorato"); %></td>
    			</tr>
    			<tr>
    			    <td><% bp.getController().writeFormLabel(out, "tematica"); %></td>
                    <td><% bp.getController().writeFormInput(out, "tematica"); %></td>
    			</tr>
    		</table>
    	</fieldset>
        <fieldset>
        <legend style="color:red">Dati CNR e Data</legend>
            <table>
    			<tr>
    			    <td><% bp.getController().writeFormLabel(out, "ricercatore"); %></td>
                    <td><% bp.getController().writeFormInput(out, "ricercatore"); %></td>
    			</tr>
    			<tr>
    			    <td><% bp.getController().writeFormLabel(out, "istitutoCnr"); %></td>
                    <td><% bp.getController().writeFormInput(out, "istitutoCnr"); %></td>
    			</tr>
    			<tr>
    			    <td><% bp.getController().writeFormLabel(out, "dipartimentoCnr"); %></td>
                    <td><% bp.getController().writeFormInput(out, "dipartimentoCnr"); %></td>
    			</tr>
    			<tr>
    			    <td><% bp.getController().writeFormLabel(out, "dataStipulaConvenzione"); %></td>
                    <td><% bp.getController().writeFormInput(out, "dataStipulaConvenzione"); %></td>
    			</tr>
    			<tr>
    			    <td><% bp.getController().writeFormLabel(out, "note"); %></td>
                    <td><% bp.getController().writeFormInput(out, "note"); %></td>
    			</tr>
    		</table>
        </fieldset>
    </td>
    </tr>
    </table>
</div>
    <% bp.closeFormWindow(pageContext); %>
</body>


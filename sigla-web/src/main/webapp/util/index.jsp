<%@ page pageEncoding="UTF-8"  session="false" import="it.cnr.jada.action.*,it.cnr.contab.utenze00.bp.*,it.cnr.contab.utenze00.bulk.*" %>
<html>
	<head>
		<% 	it.cnr.jada.util.jsp.JSPUtils.printBaseUrl(pageContext); %>
		<title><%=pageContext.getServletContext().getAttribute("APPLICATION_TITLE_VERSION") %></title>
		<script language="javascript" src="scripts/css.js"></script>
<script language="javascript" type="text/javascript">
//<!--
//--> end hide JavaScript
</script>
</head>

<script language="javascript" type="text/javascript">
//<!--
    var agt=navigator.userAgent.toLowerCase();

    var is_major = parseInt(navigator.appVersion);
    var is_minor = parseFloat(navigator.appVersion);

    var is_nav  = ((agt.indexOf('mozilla')!=-1) && (agt.indexOf('spoofer')==-1)
                && (agt.indexOf('compatible') == -1) && (agt.indexOf('opera')==-1)
                && (agt.indexOf('webtv')==-1) && (agt.indexOf('hotjava')==-1));
    var is_nav6up = (is_nav && (is_major >= 5));
    var is_ie     = ((agt.indexOf("msie") != -1) && (agt.indexOf("opera") == -1));
    var is_ie3    = (is_ie && (is_major < 4));
    var is_ie4    = (is_ie && (is_major == 4) && (agt.indexOf("msie 4")!=-1) );
    var is_ie5up  = (is_ie && !is_ie3 && !is_ie4);
    var is_ff0 = (agt.indexOf('firefox/0.')!=-1);
    var is_ff1 = (agt.indexOf('firefox/1.')!=-1);
    var is_ff2 = (agt.indexOf('firefox/2.')!=-1);
    //if (!is_ie5up && !is_nav6up)
    //     document.write("<body>Browser non supportato</body>");
    if (is_ff0 || is_ff1 || is_ff2) {
        document.write("<body>");
        document.write("<h2>Il Browser Mozilla Firefox in versione precetente alla 3.0 non è supportato.</h2>");
        document.write("<h3>E' necessario aggiornare Mozilla Firefox all'ultima versione al seguente link:</h3>");
        document.write("<h3><a href=\"http://www.mozilla-europe.org/it/firefox/\">Mozilla Firefox 3.0</a></h3>");
        document.write("</body>");
    }
//--> end hide JavaScript
</script>

	<frameset id="home" border="0" rows="36,*,20">
		<frame name="header" scrolling="NO" noresize src="util/header.jsp" marginwidth="2" marginheight="2" frameborder="NO">
	    <frame name="desktop" scrolling="NO" noresize src="utenze00/form_login.jsp" class="frameColBorder">
		<frame name="messageimg" src="util/messagebar.jsp" scrolling="NO" marginwidth="0" marginheight="0">
	</frameset>
	<noframes>
		<body>
		<p>Browser non supportato
		</body>
	</noframes>

</html>
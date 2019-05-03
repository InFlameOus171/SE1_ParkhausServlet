<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Parkhaus</title>
</head>
<body>
<script type="text/javascript" src="https://gc.kis.v2.scr.kaspersky-labs.com/B66F286D-C456-794B-B267-37BFAD4C5B09/main.js" charset="UTF-8"></script>
<script src="https://ccmjs.github.io/mkaul-components/parkhaus/versions/ccm.parkhaus-4.0.0.js"></script>
<ccm-parkhaus-4-0-0 key='["ccm.get",{"name":"parkhaus","url":"https://ccm2.inf.h-brs.de"},"1555838243447X4024946418419397"]'></ccm-parkhaus-4-0-0>

<form name="priceForm" method="post" action="DemoServlet">
    Tagestarif in %: <input type="text" name="tagespreisMultiplikator"/> <br/>
    Nachttarif in %: <input type="text" name="nachtpreisMultiplikator"/> <br/>
    <input type="submit" value="Preiseinstellung" />
</form>

</body>
</html>
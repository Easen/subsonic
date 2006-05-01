<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="iso-8859-1" %>
<%@ include file="include.jsp" %>

<html><head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/style.css"/>" rel="stylesheet">
</head>

<body>

<c:import url="settingsHeader.jsp">
    <c:param name="cat" value="internetRadio"/>
</c:import>

<table>
    <tr>
        <th><fmt:message key="internetradiosettings.name"/></th>
        <th><fmt:message key="internetradiosettings.streamurl"/></th>
        <th><fmt:message key="internetradiosettings.homepageurl"/></th>
        <th><fmt:message key="internetradiosettings.enabled"/></th>
    </tr>

    <c:forEach items="${model.internetRadios}" var="radio">
        <tr>
            <form method="post" action="internetRadioSettings.view">
                <input type="hidden" name="id" value="${radio.id}"/>
                <td><input type="text" name="name" size="20" value="${radio.name}"/></td>
                <td><input type="text" name="streamUrl" size="40" value="${radio.streamUrl}"/></td>
                <td><input type="text" name="homepageUrl" size="40" value="${radio.homepageUrl}"/></td>
                <td align="center"><input type="checkbox" ${radio.enabled ? "checked" : ""} name="enabled"/></td>
                <td><input type="submit" name="edit" value="<fmt:message key="common.save"/>"/></td>
                <td><input type="submit" name="delete" value="<fmt:message key="common.delete"/>"/></td>
            </form>
        </tr>
    </c:forEach>

    <tr>
        <form method="post" action="internetRadioSettings.view">
            <td><input type="text" name="name" size="20"/></td>
            <td><input type="text" name="streamUrl" size="40"/></td>
            <td><input type="text" name="homepageUrl" size="40"/></td>
            <td align="center"><input name="enabled" checked type="checkbox"/></td>
            <td><input type="submit" name="create" value="<fmt:message key="common.create"/>"/></td>
        </form>
    </tr>
</table>

<c:if test="${not empty model.error}">
    <p style="color:red;"><fmt:message key="${model.error}"/></p>
</c:if>

<c:if test="${model.reload}">
    <script language="javascript" type="text/javascript">parent.frames.left.location.href="left.view?"</script>
</c:if>

</body></html>
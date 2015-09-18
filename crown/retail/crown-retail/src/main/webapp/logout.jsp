
<%
request.getSession().invalidate();
response.sendRedirect("login.jsf");
%>

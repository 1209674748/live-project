<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>123</title>
  </head>
  <body>
  <form action="OrderServlet" autocomplete="on">
  姓名:<input type="text" name="name" placeholder="请输入真实姓名" required><br>
  身份证号: <input type="text" name="ID" required><br>
  手机号: <input type="text" name="phone" autocomplete="on" required><br>
  想要预约的口罩数量<input type="number" name="number" required><br>
  <input type="submit">
</form>
  </body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: osys
  Date: 2022/3/25
  Time: 16:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>测试</title>
    </head>
    <body>
        <form name="f1" id="f1" action="${pageContext.request.contextPath}/Demo" method="post">
            <table>
                <tr>
                    <td>创建数组:</td>
                    <td><label for="demo"></label><input type="text" name="demo" id="demo"></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit"></td>
                </tr>
            </table>
        </form>

        <form name="f2" id="f2" action="${pageContext.request.contextPath}/Demo2" method="post">
            <table>
                <tr>
                    <td>创建数组:</td>
                    <td><label for="demo2"></label><input type="text" name="demo2" id="demo2"></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit"></td>
                </tr>
            </table>
        </form>

        <form name="f3" id="f3" action="${pageContext.request.contextPath}/Demo3" method="post">
            <table>
                <tr>
                    <td>创建数组:</td>
                    <td><label for="demo3"></label><input type="text" name="demo3" id="demo3"></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit"></td>
                </tr>
            </table>
        </form>

        <form name="f4" id="f4" action="${pageContext.request.contextPath}/Demo4" method="post">
            <table>
                <tr>
                    <td>创建数组:</td>
                    <td><label for="demo4"></label><input type="text" name="demo4" id="demo4"></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit"></td>
                </tr>
            </table>
        </form>

        <form name="f5" id="f5" action="${pageContext.request.contextPath}/Demo5" method="post">
            <table>
                <tr>
                    <td>创建数组:</td>
                    <td><label for="demo5"></label><input type="text" name="demo5" id="demo5"></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit"></td>
                </tr>
            </table>
        </form>
    </body>
</html>

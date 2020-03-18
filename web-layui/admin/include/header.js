function ok() {
    return layui.data('token').Authorization !== undefined && layui.data('user').info !== undefined;
}

function logout() {
    layui.data('token', null);
    layui.data('user', null);
    window.location.href = "index.html";
}

document.write("<div class=\"layui-header\">");
document.write("    <div class=\"layui-logo\">商城后端系统<\/div>");
document.write("    <ul class=\"layui-nav layui-layout-left\">");
document.write("        <li class=\"layui-nav-item\"><a href=\"index.html\">控制台<\/a><\/li>");
document.write("        <li class=\"layui-nav-item\">");
document.write("            <a href=\"javascript:void(0);\">其它<\/a>");
document.write("            <dl class=\"layui-nav-child\">");
document.write("                <dd><a href=\"https://github.com/littleredhat1997\">GitHub<\/a><\/dd>");
document.write("            <\/dl>");
document.write("        <\/li>");
document.write("    <\/ul>");
if (ok()) {
    document.write("    <ul class=\"layui-nav layui-layout-right\">");
    document.write("        <li class=\"layui-nav-item\">");
    document.write("            <a href=\"javascript:void(0);\">");
    document.write("                <img id=\"header-avatar\" src=\"" + layui.data('user').info.avatar + "\" class=\"layui-nav-img\">");
    document.write("                    <span id=\"header-nickname\">" + layui.data('user').info.nickname + "</span>");
    document.write("            <\/a>");
    document.write("            <dl class=\"layui-nav-child\">");
    document.write("                <dd><a href=\"javascript:void(0);\">基本资料NONE<\/a><\/dd>");
    document.write("                <dd><a href=\"javascript:void(0);\">修改密码NONE<\/a><\/dd>");
    document.write("            <\/dl>");
    document.write("        <\/li>");
    document.write("        <li class=\"layui-nav-item\"><a href=\"javascript:void(0);\" onclick=\"logout()\">退出<\/a><\/li>");
    document.write("    <\/ul>");
}
document.write("<\/div>");

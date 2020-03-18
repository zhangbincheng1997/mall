function ok() {
    return layui.data('token').Authorization !== undefined && layui.data('user').info !== undefined;
}

function logout() {
    layui.data('token', null);
    layui.data('user', null);
    window.location.href = "index.html";
}

document.write("<div class=\"site-nav-bg\">");
document.write("    <div class=\"site-nav w1200\">");
document.write("        <p class=\"sn-back-home\">");
document.write("            <i class=\"layui-icon layui-icon-home\"><\/i>");
document.write("            <a href=\"index.html\">控制台<\/a>");
document.write("        <\/p>");
document.write("        <div class=\"sn-quick-menu\">");
if (!ok()) {
    document.write("            <div class=\"login\"><a href=\"login.html\">登录<\/a><\/div>");
    document.write("            <div class=\"login\"><a href=\"register.html\">注册<\/a><\/div>");
} else {
    document.write("            <div class=\"login\"><a href=\"info.html\">管理<\/a><\/div>");
    document.write("            <div class=\"login\"><a href=\"javascript:void(0);\" onclick=\"logout()\">退出<\/a><\/div>");
}
document.write("        <\/div>");
document.write("    <\/div>");
document.write("<\/div>");

let nickname = sessionStorage.getItem("nickname");
if (nickname == null) {
        document.write(`
            <a class="nav-link" href="/user/login">login</a>
        `);
} else {
    document.write(
        `<span style="position: absolute; right: 0px; padding: 7px;">&nbsp;&nbsp;<b>`
        + nickname
        + `</b>님 환영합니다!&nbsp;&nbsp;</span>`
    );
}
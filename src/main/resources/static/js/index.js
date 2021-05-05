let index = {
    init: function () {
        if (sessionStorage.getItem("access_token")) {
            $.ajax({
                type: "GET",
                url: "/api/problems",
                headers: {"Authorization": sessionStorage.getItem("access_token"), "Content-type": "application/json"},
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).done(function (result) {
                console.log(result);
                let problemList = result.data;
                alert("문제 GET 성공");
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
        else {
            location.href = "/user/login";
        }
    }
}
index.init();
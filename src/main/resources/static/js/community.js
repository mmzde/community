//评论文章
function postcomment() {

    var questionId = $("#question_id").val();
    var comment_content = $("#comment_content").val();

    comment2target(questionId, 1, comment_content);
}


function comment2target(targetId, type, content) {


    if (!comment_content) {
        alert("不能回复空内容");
        return;
    }

    $.ajax({
        type: "post",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=4bc98ff7ae188a5f5470&redirect_uri=http://localhost:8080/callback&scope=user&state=1")
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message)

                }
            }
        },
        dataType: "json",
    });
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    comment2target(commentId,2,content);
}

//展开二级评论
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //折叠二级评论
        comments.removeClass("in");
        e.classList.remove("active");
        e.removeAttribute("data-collapse");
    } else {

        $.getJSON("/comment/"+id,function (data) {

            var commentBody = $("#comment-body-" + id);
            var items = [];

            $.each(data.data,function (comment) {

                $("<div/>",{
                    "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    html:items.join("")
                })
            })


            $("<div/>",{
                "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 collapse sub-comments",
                "id":"comment-"+id,
                html:items.join("")
            }).appendChild(commentBody);
            //展开二级评论
            comments.addClass("in");
            //二级评论标记状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        });


    }

}
var main = { //main이란 객체 생성해서 function 유효 범위 선언
        init : function () {
            var _this = this;
            /*var flag = false;*/
            $('#btn-save').on('click', function () {
                _this.save();
            });

            $('#btn-update').on('click', function () {
                _this.update();
            });
            $('#btn-delete').on('click', function () {
                _this.delete();
            });
            $('#btn-comment-save').on('click', function () {
                _this.commentSave();
            });
            document.querySelectorAll('#btn-comment-update').forEach(function (item) {
                item.addEventListener('click', function () { // 버튼 클릭 이벤트 발생시
                    const form = this.closest('form'); // btn의 가장 가까운 조상의 Element(form)를 반환 (closest)
                    _this.commentUpdate(form); // 해당 form으로 업데이트 수행
                });
            });
    },
    save: function () {
        let data = {
            title : $('#title').val(),
            content: $('#content').val()
        }
        let fileImg = $('#filename')[0].files[0];

        let formdata = new FormData();
        formdata.append("p", new Blob([JSON.stringify(data)],
            {type: "application/json"}))
        formdata.append("f", fileImg);

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts/save',
            contentType: false,
            data: formdata,
            processData:false,
        }).done(function () {
            alert('글이 등록되었습니다.');
            window.location.href = '/'; //글 등록이 성공하면 메인페이지("/")로 이동
        }).fail(function (error) {
            alert('error');
        });
    },
    update : function () {

        var data = {
            title: $('#title').val(),
            content: $('#content').val(),
            deleteFlag: $('#flag').val() //삭제 버튼 눌렀을 경우 true가 감
        };

        var id = $('#id').val();

        let fileImg = $('#filename')[0].files[0];

        let formdata = new FormData();
        formdata.append("p", new Blob([JSON.stringify(data)],
            {type: "application/json"}))
        formdata.append("f", fileImg);

        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/'+id,
            contentType: false,
            data: formdata,
            processData:false,
        }).done(function() {
            alert('글이 수정되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    <!--delete 기능 추가-->
    delete : function () {
        var id = $('#id').val();
        $.ajax({
            type: 'DELETE', <!--삭제 관련 http 메소드인 DELETE-->
            url: '/api/v1/posts/'+id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function() {
            alert('글이 삭제되었습니다.');
            window.location.href = '/'; <!--삭제 후 메인화면으로 복귀-->
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    <!--comment save 기능 추가-->
    commentSave : function () {
        const data = {
            postsId: $('#postsId').val(),
            comment: $('#comment').val()
        }

        if (!data.comment || data.comment.trim() === "") {
            alert("공백 또는 입력하지 않은 부분이 있습니다.");
            return false;
        } else {
            $.ajax({
                type: 'POST',
                url: '/api/v1/posts/' + data.postsId + '/comments',
                dataType: 'text',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function () {
                alert('댓글이 등록되었습니다.');
                window.location.reload();
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    },
    commentUpdate : function (form) {
        const data = {
            id: form.querySelector('#id').value,
            postsId: form.querySelector('#postsId').value,
            comment: form.querySelector('#comment-content').value
        }
        if (!data.comment || data.comment.trim() === "") {
            alert("공백 또는 입력하지 않은 부분이 있습니다.");
            return false;
        }
        const con_check = confirm("수정하시겠습니까?");
        if (con_check === true) {
            $.ajax({
                type: 'PUT',
                url: '/api/v1/posts/' + data.postsId + '/comments/' + data.id,
                dataType: 'JSON',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function () {
                window.location.reload();
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    },

    /** 댓글 삭제 */
    commentDelete : function (postsId, commentId) {
        const con_check = confirm("삭제하시겠습니까?");
        if (con_check === true) {
            $.ajax({
                type: 'DELETE',
                url: '/api/v1/posts/' + postsId + '/comments/' + commentId,
                dataType: 'JSON',
            }).done(function () {
                alert('댓글이 삭제되었습니다.');
                window.location.reload();
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    }

};
main.init();
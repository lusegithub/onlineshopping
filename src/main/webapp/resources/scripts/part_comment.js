/**
 * Created by vincent on 2017/7/12.
 */
var webRoot='/onlineshopping';
if(sessionStorage.getItem('status')!=0){
    location.href=webRoot+'/index.html';
}
$(document).ready(function () {
//传递的参数信息
    var url=location.search;
    var goodsId=url.substr(6);
//nickname
    var name=sessionStorage.getItem('nickname');
//时间信息
    var mydate=new Date();

    var y=mydate.getUTCFullYear();
    var m=mydate.getUTCMonth()+1;
    var d=mydate.getUTCDate();

    var t=y+'-'+m+'-'+d;

    $("#submit_button").click(function(){
        var grade=$("input:radio:checked").val();
        var chara=document.getElementById('comment_text').value;
        if(grade!=null&&chara!=''){
            $.ajax({
                url:webRoot+'/user/goods/'+goodsId+'/comment',
                type:'POST',
                data:{nickname:name,time:t,score:grade,content:chara},
                success:function(data){
                    if(data.resultCode==0){
                        alert('提交评论成功！')
                        history.back();
                    }else{
                        alert(data.resultInfo);
                    }
                }
            })
            .done(function(){
                console.log('success');
            })
            .fail(function () {
                console.log('fail');
            })
            .always(function(){
                console.log('complete');
            });
        }else {
            alert('评分与评论内容不能为空');
        }
    });
})

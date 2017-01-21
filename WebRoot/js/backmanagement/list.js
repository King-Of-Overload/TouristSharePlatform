/**
 *精选数据显示
 */
 $(function(){
   $.ajax({
     type:'POST',
     async:true,
     cache:false,
     data:{requestType:'bestChoose'},
     url:'tripuser_getAllBestChoose.do',
     success:function(response){
       $('.table').empty();
       $('.table').append('<tr>'+
        '<th width="20%" style="text-align:left; padding-left:20px;">ID</th>'+
        '<th>图片</th>'+
        '<th>标题</th>'+
        '<th>是否热门</th>'+
        '<th>点击量</th>'+
        '<th width="10%">更新时间</th>'+
        '<th width="310">操作</th>'+
      '</tr><volist name="list" id="vo">');
       var jsonResult=JSON.parse(response);
       for(var i=0;i<jsonResult.length;i++){
         var bestChoose=jsonResult[i];
         var html;
         html='<tr><td style="text-align:left; padding-left:20px;">';
         html+='<input type="checkbox" name="bcId" value="'+bestChoose.bestid+'"/>'+bestChoose.bestid+'</td>';
         html+='<td width="10%"><img src="'+bestChoose.bestcover+'" alt="" width="180" height="120" /></td>';
         html+='<td>'+bestChoose.bestname+'</td>';
         if(bestChoose.bestishot=='0'){
           html+='<td><font color="#00CC99">是</font></td>';
         }else{
           html+='<td>否</td>';
         }
         html+='<td>'+bestChoose.bestclickcount+'</td>';
         html+='<td>'+bestChoose.bestdate+'</td>';
         html+='<td><div class="button-group"> <a class="button border-main" href="add.html"><span class="icon-edit"></span> 修改</a> <a class="button border-red" href="javascript:void(0)" onclick="return del(1,1,1)"><span class="icon-trash-o"></span> 删除</a> </div></td>';
         html+='</tr>';
         $('.table').append(html);
       }
        $('.table').append('<tr><td style="text-align:left; padding:19px 0;padding-left:20px;margin-right=10px;"><input type="checkbox" id="checkall"/>'+
        '全选'+
        '  <td colspan="7" style="text-align:left;padding-left:20px;"><a href="javascript:void(0)" class="button border-red icon-trash-o" style="padding:5px 15px;" onclick="DelSelect()"> 删除</a></td>');
     },
     error:function(response){
       console.log(response);
     }
   });
 });

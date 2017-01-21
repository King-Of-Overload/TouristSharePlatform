/*
弹出插件 AND 分享插件
autho：smohan
http://www.smohan.net
*/

//这是弹出层，IE9以下无法圆角
;(function($){$.fn.SmohanPopLayer=function(options){var Config={Shade:true,Event:"click",Content:"Content",Title:"Smohan.net"};var options=$.extend(Config,options);var layer_width=$('#'+options.Content).outerWidth(true);var layer_height=$('#'+options.Content).outerHeight(true)
var layer_top=(layer_height+40)/2;var layer_left=(layer_width+40)/2;var load_left=(layer_width-36)/2;var load_top=(layer_height-100)/2;var layerhtml="";if(options.Shade==true){layerhtml+='<div class="Smohan_Layer_Shade" style="display:none;"></div>';}
layerhtml+='<div class="Smohan_Layer_box" style="width:'+layer_width+'px;height:'+layer_height+'px; margin-top:-'+layer_top+'px;margin-left:-'+layer_left+'px;display:none;" id="layer_'+options.Content+'">';layerhtml+='<h3><b class="text">'+options.Title+'</b><a href="javascript:void(0)" class="close"></a></h3>';layerhtml+='<div class="layer_content">';layerhtml+='<div class="loading" style="left:'+load_left+'px;top:'+load_top+'px;"></div>';layerhtml+='<div id="'+options.Content+'" style="display:block;">'+$("#"+options.Content).html()+'</div>';layerhtml+='</div>';layerhtml+='</div>';$('body').prepend(layerhtml);if(options.Event=="unload"){$('#layer_'+options.Content).animate({opacity:'show',marginTop:'-'+layer_top+'px'},"slow",function(){$('.Smohan_Layer_Shade').show();$('.Smohan_Layer_box .loading').hide();});}else{$(this).on(options.Event,function(e){$('#layer_'+options.Content).animate({opacity:'show',marginTop:'-'+layer_top+'px'},"slow",function(){$('.Smohan_Layer_Shade').show();$('.Smohan_Layer_box .loading').hide();});});}
$('.Smohan_Layer_box .close').click(function(e){$('.Smohan_Layer_box').animate({opacity:'hide',marginTop:'-300px'},"slow",function(){$('.Smohan_Layer_Shade').hide();$('.Smohan_Layer_box .loading').show();});});};})(jQuery);

//分享	
$(document).ready(function(e) {
	var share_html = "";
	//share_html += '<a href="javascript:void(0)" id="smohan_share" title="分享"></a>';
	share_html += '<div id="Share"><ul>';
	share_html += '<li title="分享到QQ空间"><a href="javascript:void(0)" class="share1"></a><span></span></li>';
	share_html += '<li title="分享到新浪微博"><a href="javascript:void(0)" class="share2"></a><span></span></li>';
	share_html += '<li title="分享到人人网"><a href="javascript:void(0)" class="share3" ></a><span></span></li>';
	share_html += '<li title="分享到朋友网"><a href="javascript:void(0)" class="share4"></a><span></span></li>';
	share_html += '<li title="分享到腾讯微博"><a href="javascript:void(0)" class="share5"></a><span></span></li>';
	share_html += '<li title="分享到开心网"><a href="javascript:void(0)" class="share6"></a><span></span></li>';
	share_html += '</ul></div>';
	$('body').prepend(share_html);
    $('.share').SmohanPopLayer({Shade : true,Event:'click',Content : 'Share', Title : '分享攻略到各大社区'});
    $('#Share li').each(function() {
    $(this).hover(function(e) {
	  $(this).find('a').animate({ marginTop: 2}, 'easeInOutExpo');
	  $(this).find('span').animate({opacity:0.2},'easeInOutExpo');
	 },function(){
	  $(this).find('a').animate({ marginTop: 12}, 'easeInOutExpo');
	  $(this).find('span').animate({opacity:1},'easeInOutExpo');
   });
});
var share_url = encodeURIComponent(location.href);
var share_title = encodeURIComponent(document.title);
var share_pic = "http://www.smohan.net/images/smohan.png";  //默认的分享图片
var share_from = encodeURIComponent("芳草寻源官方网站"); //分享自（仅用于QQ空间和朋友网，新浪的只需更改appkey 和 ralateUid就行）
//Qzone
$('#Share li a.share1').click(function(e) {
    window.open("http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url="+share_url+"&title="+share_title+"&pics="+share_pic+"&site="+share_from+"","newwindow");
});	  
//Sina Weibo
$('#Share li a.share2').click(function(e) {
var param = {
    url:share_url ,
    appkey:'678438995',
    title:share_title,
    pic:share_pic,
    ralateUid:'3061825921',
    rnd:new Date().valueOf()
  }
  var temp = [];
  for( var p in param ){
    temp.push(p + '=' + encodeURIComponent( param[p] || '' ) )
  }
window.open('http://v.t.sina.com.cn/share/share.php?' + temp.join('&'));	
});
//renren
$('#Share li a.share3').click(function(e) {
window.open('http://widget.renren.com/dialog/share?resourceUrl='+share_url+'&title='+share_title+'&images='+share_pic+'','newwindow');
});
//pengyou
$('#Share li a.share4').click(function(e) {
window.open('http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?to=pengyou&url='+share_url+'&pics='+share_pic+'&title='+share_title+'&site='+share_from+'','newwindow');
});
//tq
$('#Share li a.share5').click(function(e) {
window.open('http://share.v.t.qq.com/index.php?c=share&a=index&title='+share_title+'&site='+share_from+'&pic='+share_pic+'&url='+share_url+'','newwindow');
});
//kaixin
$('#Share li a.share6').click(function(e) {
window.open('http://www.kaixin001.com/repaste/bshare.php?rtitle='+share_title+'&rurl='+share_url+'&from=芳草寻源','newwindow');	
}); 
});
/*加入收藏*/
function addfavorite(){
  var Url = "http://www.smohan.net";
  var Title = "芳草寻源官方网站";
  if(document.all){
	 window.external.addFavorite(Url,Title);
	}else if(window.sidebar){
     window.sidebar.addPanel(Title,Url,"");
   }else{
	 alert("请使用Ctrl+D键导入书签！");
   }	
}
$(function() {
	addItems(1,30);
});

var pageNo,pageSize;
function addItems(page,size){
	pageNo = page;
	pageSize = size;
	var url = "api/prize/order/listPage?userid=-1&page="+page+"&size="+size;
	$.get(url, function( data ) {}).done(function(data) {
		var result = "";
		$(data.result).each(function(index, value) {
			var statu = "";
			if(value.sellState==1){
				statu = '<span id="statu'+value.id+'"><span class="label label-success">ONSEAL</span><span>';
			}else{
				statu = '<span id="statu'+value.id+'"><span class="label label-important">SALEOUT</span><span>';
			}
			var content = '<tr>'+
				'<td class="center"><span class="thumbnail" style="width: 100px;margin-bottom:0px !important"><a title="'+value.prize.desc+'" href="'+value.prize.headPhoto+'"><img src="'+value.prize.thumbnail_path+'"/></a><span></td>'+
				'<td class="center">'+validatecoins(value.coins)+'</td>'+
				'<td class="center">'+validate(value.user.nickname)+'</td>'+
				'<td class="center">'+validate(value.name)+'</td>'+
				'<td class="center">'+validate(value.email)+'</td>'+
				'<td class="center">'+validate(value.phone)+'</td>'+
				'<td class="center">'+validate(value.address)+'</td>'+
				'<td class="center">'+validate(value.description)+'</td>'+
				'<td class="center">'+validate(value.statu)+'</td>'+
				'<td class="center">'+statu+'</td>'+
				'<td class="center">'+
				'<a class="btn btn-success" onclick="check('+value.id+',\'PASS\')" href="#"><i class="icon icon-black icon-check"></i>Pass</a>&nbsp;'+
				'<a class="btn btn-danger" onclick="check('+value.id+',\'FAIL\')" href="#"><i class="icon icon-black icon-close"></i>Deny</a>&nbsp;'+
				'<a class="btn btn-info" onclick="deleteImg('+value.id+')" href="#"><i class="icon icon-black icon-trash"></i>Delete</a>'+
			'</td>'+
			'</tr>';
			result = result+content;
		});
		$("#user_tbody").html(result);
		pagination(page,size,data.count);
	});

}
function validate(value){
	if(value==null||value==undefined){
		return '';
	}else{
		return value;
	}
}
function validateHeadPhoto(value){
	if(value==null||value==undefined){
		return 'comicDir/user_portrait.jpg';
	}else{
		return value;
	}
}
function pagination(page,size,count){
	if(page==0){
		$(".pagination_ul").html("");
		return;
	}
	var max ;
	if(count%size==0){
		max= parseInt(count/size);
	}else{
		max= parseInt(count/size)+1;
	}
		var innerHtml_pre;
		if(page>=3){
			innerHtml_pre = '<li><a onclick="addItems(1,'+size+')" href="#" title="1">|<</a></li>'+
							'<li><a onclick="addItems('+(page-2)+','+size+')" href="#">'+(page-2)+'</a></li>'+
							'<li><a onclick="addItems('+(page-1)+','+size+')" href="#">'+(page-1)+'</a></li>';
		}else{
			if(page==1){
				innerHtml_pre = '';
			}
			if(page==2){
				innerHtml_pre = '<li><a onclick="addItems(1,'+size+')" href="#">1</a></li>';
				
			}
		}
		var innerHtml_active = '<li class="active"><a onclick="addItems('+page+','+size+')" href="#">'+page+'</a></li>';
		
		var innerHtml_suffix = '';
		if(max-page>=3){
			innerHtml_suffix = 	'<li><a onclick="addItems('+(page+1)+','+size+')" href="#">'+(page+1)+'</a></li>'+
								'<li><a onclick="addItems('+(page+2)+','+size+')" href="#">'+(page+2)+'</a></li>'+
								'<li><a title="'+max+'" href="#" onclick="addItems('+max+','+size+')">>|</a></li>';
		}else{
			if(max-page==0){
				innerHtml_suffix = '';
			}
			if(max-page==1){
				innerHtml_suffix = 	'<li><a onclick="addItems('+(page+1)+','+size+')" href="#">'+(page+1)+'</a></li>';
			}
			if(max-page==2){
				innerHtml_suffix = 	'<li><a onclick="addItems('+(page+1)+','+size+')" href="#">'+(page+1)+'</a></li>'+
									'<li><a onclick="addItems('+(page+2)+','+size+')" href="#">'+(page+2)+'</a></li>';
			}
		}
		$(".pagination_ul").html(innerHtml_pre+innerHtml_active+innerHtml_suffix);
}
function validatecoins(coins){
	var greenNum='&nbsp;&nbsp;&nbsp;&nbsp;0';
	var yellowNum='&nbsp;&nbsp;&nbsp;&nbsp;0';
	var redNum='&nbsp;&nbsp;&nbsp;&nbsp;0';
	if(coins==null||coins==undefined){
		 greenNum='&nbsp;&nbsp;&nbsp;&nbsp;0';
		 yellowNum='&nbsp;&nbsp;&nbsp;&nbsp;0';
		 redNum='&nbsp;&nbsp;&nbsp;&nbsp;0';
	}else{
		if((coins.greenNum!=null)&&(coins.greenNum!=undefined)){
			greenNum=coins.greenNum;
			if(greenNum>10&&greenNum<100){
				greenNum = "&nbsp;&nbsp;"+greenNum;
			}else if(greenNum<10){
				greenNum = "&nbsp;&nbsp;&nbsp;&nbsp;"+greenNum;
			}
		}
		if((coins.yellowNum!=null)&&(coins.yellowNum!=undefined)){
			yellowNum=coins.yellowNum;
			if(yellowNum>10&&yellowNum<100){
				yellowNum = "&nbsp;&nbsp;"+yellowNum;
			}else if(yellowNum<10){
				yellowNum = "&nbsp;&nbsp;&nbsp;&nbsp;"+yellowNum;
			}
		}
		 if((coins.redNum!=null)&&(coins.redNum!=undefined)){
			 redNum=coins.redNum;
			 if(redNum>10&&redNum<100){
				 redNum = "&nbsp;&nbsp;"+redNum;
				}else if(redNum<10){
					redNum = "&nbsp;&nbsp;&nbsp;&nbsp;"+redNum;
				}
		 }
	}
	var spans = '<span style="background-color:#E2EFD9; padding-left: 20px; padding-top: 5px; padding-bottom: 15px;">'+
	greenNum+'</span><span style="background-color:rgb(255, 243, 203); padding-left: 20px; padding-top: 5px; padding-bottom: 15px;">'+
	yellowNum+'</span><span style="background-color:rgb(255, 153, 153); padding-left: 20px; padding-top: 5px; padding-bottom: 15px;">'+
	redNum+'</span>';/*value.coins*/
	return spans;
}
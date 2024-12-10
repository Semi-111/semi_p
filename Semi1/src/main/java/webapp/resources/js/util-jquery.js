const ajaxRequest = function(url, method, formData, dataType, callback, file = false, contentType = 'text') {
	const sentinelNode = document.querySelector('.sentinel');
	
	const settings = {
			type: method, 
			data: formData,
			success:function(data) {
				callback(data);
			},
			beforeSend: function(jqXHR) {
				jqXHR.setRequestHeader('AJAX', true);
				
				if(sentinelNode) {
					sentinelNode.setAttribute('data-loading', 'true');
				}
			},
			complete: function () {
			},
			error: function(jqXHR) {
				if(jqXHR.status === 403) {
					alert('권한이 필요 합니다.');
					return false;
				} else if(jqXHR.status === 406) {
					alert('요청 처리가 실패 했습니다.');
					return false;
		    	}
		    	
				console.log(jqXHR.responseText);
			}
	};
	
	if(file) {
		settings.processData = false;  // file 전송시 필수. 서버로전송할 데이터를 쿼리문자열로 변환여부
		settings.contentType = false;  // file 전송시 필수. 서버에전송할 데이터의 Content-Type. 기본:application/x-www-urlencoded
	}
	
	// 전송방식 : json 으로 전송하는 경우
	if(contentType.toLowerCase() === 'json') {
		settings.contentType = 'application/json; charset=utf-8';
	}
	
	$.ajax(url, settings);
};

// AJAX 시작과 종료
$(function(){
	$(document)
	   .ajaxStart(function(){ // AJAX 시작
		   $('#loadingLayout .loader').center();
		   $('#loadingLayout').fadeTo('slow', 0.5);
	   })
	   .ajaxComplete(function(){ // AJAX 종료
		   $('#loadingLayout').hide();
	   });
});

// 엔터 처리
$(function(){
	$('input').not($(':button')).keydown(function (evt) {
		if (evt.key === 'Enter') {
			let fields = $(this).parents('form,body').find('button,input,textarea,select');
			let index = fields.index(this);
			if ( index > -1 && ( index + 1 ) < fields.length ) {
				fields.eq( index + 1 ).focus();
			}
			
			return false;
		}
	});
});

// 스크롤바 존재 여부를 확인하는 함수
function checkScrollBar() {
	let hContent = $('body').height();
	let hWindow = $(window).height();
	if(hContent > hWindow) {
		return true;
	}
	
	return false;
}

// jQuery 함수 구현 : 개체를 화면 중앙에 위치하는 함수
jQuery.fn.center = function () {
    this.css('position', 'absolute');
    this.css('top', Math.max(0, (($(window).height() - $(this).outerHeight()) / 2) + 
                                                $(window).scrollTop()) + "px");
    this.css('left', Math.max(0, (($(window).width() - $(this).outerWidth()) / 2) + 
                                                $(window).scrollLeft()) + "px");
    return this;
}

// jQuery 함수 구현 : jquery 1.9에서 사라진 toggle() 함수
jQuery.fn.extend({
    cycle: function (fn) {
        let args = arguments,
		guid = fn.guid || jQuery.guid++,
		i = 0,
		toggler = function (event) {
		    // Figure out which function to execute
		    let lastToggle = (jQuery._data(this, 'lastToggle' + fn.guid) || 0) % i;
		    jQuery._data(this, 'lastToggle' + fn.guid, lastToggle + 1);

		    // Make sure that clicks stop
		    event.preventDefault();

		    // and execute the function
		    return args[lastToggle].apply(this, arguments) || false;
		};

        // link all the functions, so any of them can unbind this click handler
        toggler.guid = guid;
        while (i < args.length) {
            args[i++].guid = guid;
        }

        return this.click(toggler);
    }
});
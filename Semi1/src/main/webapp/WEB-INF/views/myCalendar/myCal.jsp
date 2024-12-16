<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

<style type="text/css">
.body-container {
	max-width: 840px;
}

.textDate {
	cursor: pointer;
	display: block;
}

.scheduleSubject {
	display: block;
	max-width: 100px;
	margin: 1.5px 0;
	font-size: 13px;
	color: #555;
	cursor: pointer;
	background: #f8f9fa;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.scheduleMore {
	display: block;
	width: 100px;
	margin: 0 0 1.5px;
	font-size: 13px;
	color: #555;
	cursor: pointer;
	text-align: right;
}

.daySubject {
	cursor: pointer;
	max-width: 330px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.daySubject:hover {
	color: #F28011;
}

.preMonthDate, .nextMonthDate {
	color: #AAA;
}

.saturdayDate {
	color: #0000ff;
}

.sundayDate {
	color: #ff0000;
}
</style>
</head>
<body>

	<header>
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>

	<main>
		<div class="container">
			<div class="body-container">
				<div class="body-title">
					<h3>
						<i class="bi bi-calendar2-event"></i> 일정관리
					</h3>
				</div>

				<div class="body-main">

					<ul class="nav nav-tabs" id="myTab" role="tablist">
						<li class="nav-item" role="presentation">
							<button class="nav-link active" id="tab-1" data-bs-toggle="tab"
								data-bs-target="#nav-1" type="button" role="tab"
								aria-controls="1" aria-selected="true">월별일정</button>
						</li>
						<li class="nav-item" role="presentation">
							<button class="nav-link" id="tab-2" data-bs-toggle="tab"
								data-bs-target="#nav-2" type="button" role="tab"
								aria-controls="2" aria-selected="true">상세일정</button>
						</li>
						<li class="nav-item" role="presentation">
							<button class="nav-link" id="tab-3" data-bs-toggle="tab"
								data-bs-target="#nav-3" type="button" role="tab"
								aria-controls="3" aria-selected="true">년도</button>
						</li>
					</ul>

					<div class="tab-content" id="nav-tabContent">
						<div class="tab-pane fade show active" id="nav-1" role="tabpanel"
							aria-labelledby="nav-tab-1"></div>
						<div class="tab-pane fade" id="nav-2" role="tabpanel"
							aria-labelledby="nav-tab-2"></div>
						<div class="tab-pane fade" id="nav-3" role="tabpanel"
							aria-labelledby="nav-tab-2"></div>
					</div>

				</div>
			</div>
		</div>
	</main>

	<!-- Modal -->
	<div class="modal fade" id="myDialogModal" data-bs-backdrop="static"
		data-bs-keyboard="false" tabindex="-1"
		aria-labelledby="imyDialogModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="myDialogModalLabel">스케쥴 등록</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body pt-0 pb-0">
					<form name="scheduleForm">
						<table class="table">
							<tr>
								<td class="col-2">제 목</td>
								<td><input type="text" name="subject" id="form-subject"
									class="form-control"> <small
									class="form-control-plaintext help-block"> * 제목은 필수
										입니다. </small></td>
							</tr>

							<tr>
								<td class="col-2">일정분류</td>
								<td><select name="color" id="form-color"
									class="form-select">
										<option value="green">개인일정</option>
										<option value="blue">가족일정</option>
										<option value="tomato">회사일정</option>
										<option value="purple">기타일정</option>
								</select></td>
							</tr>

							<tr>
								<td class="col-2">종일일정</td>
								<td>
									<div class="form-check form-control-plaintext">
										<input class="form-check-input" type="checkbox" name="allDay"
											id="form-allDay" value="1"> <label
											class="form-check-label" for="form-allDay">하루종일</label>
									</div>
								</td>
							</tr>

							<tr>
								<td class="col-2">시작일자</td>
								<td>
									<div class="row">
										<div class="col col-sm-4 pe-1">
											<input type="date" name="sday" id="form-sday"
												class="form-control">
										</div>
										<div class="col col-sm-3">
											<input type="time" name="stime" id="form-stime"
												class="form-control" style="display: none;">
										</div>
									</div> <small class="form-control-plaintext help-block"> *
										시작날짜는 필수입니다. </small>
								</td>
							</tr>

							<tr>
								<td class="col-2">종료일자</td>
								<td>
									<div class="row">
										<div class="col col-sm-4 pe-1">
											<input type="date" name="eday" id="form-eday"
												class="form-control">
										</div>
										<div class="col col-sm-3">
											<input type="time" name="etime" id="form-etime"
												class="form-control" style="display: none;">
										</div>
									</div> <small class="form-control-plaintext help-block">
										종료일자는 선택사항이며, 시작일자보다 작을 수 없습니다. </small>
								</td>
							</tr>

							<tr>
								<td class="col-2">일정반복</td>
								<td>
									<div class="row">
										<div class="col col-sm-4 pe-1">
											<select name="repeat" id="form-repeat" class="form-select">
												<option value="0">반복안함</option>
												<option value="1">년반복</option>
											</select>
										</div>
										<div class="col col-sm-3">
											<input type="text" name="repeat_cycle" id="form-repeat_cycle"
												class="form-control">
										</div>
									</div>
								</td>
							</tr>

							<tr>
								<td class="col-2">메 모</td>
								<td><textarea name="memo" id="form-memo"
										class="form-control" style="height: 70px; resize: none;"></textarea>
								</td>
							</tr>

							<tr>
								<td colspan="2" class="text-center" style="border-bottom: none;">
									<input type="hidden" name="num" id="form-num" value="0">
									<button type="button" class="btn btn-dark"
										id="btnScheduleSendOk">등록 완료</button>
									<button type="button" class="btn btn-light"
										id="btnScheduleSendCancel">등록 취소</button>
								</td>
							</tr>

						</table>
					</form>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
$(function(){
    // 실행과 동시에 처음 탭에 출력
    let url = '${pageContext.request.contextPath}/schedule/month';
    let query = '';
    
    schedule(url, query, '#nav-1');
});

$(function(){
    // 탭을 클릭할 때 마다
    $('button[role="tab"]').on('click', function(e){
    	let tab = $(this).attr('aria-controls');
        let selector = '#nav-' + tab;
        
        let url;
        if(tab === '1') {
            url = '${pageContext.request.contextPath}/schedule/month';
        } else if(tab === '2') {
            url = '${pageContext.request.contextPath}/schedule/day';
        } else if(tab === '3') {
            url = '${pageContext.request.contextPath}/schedule/year';
        }

        let query = '';
        schedule(url, query, selector);
    });
    
    // 탭이 변경 될때 마다
    $('button[role="tab"]').on('shown.bs.tab', function(e){
		// let tab = $(this).attr('aria-controls');
        // let selector = '#nav-' + tab;
		// today();
    });
    
});

function login() {
	location.href = '${pageContext.request.contextPath}/member/login';
}

function ajaxFun(url, method, formData, dataType, fn, file = false) {
	const settings = {
			type: method, 
			data: formData,
			dataType:dataType,
			success:function(data) {
				fn(data);
			},
			beforeSend: function(jqXHR) {
				jqXHR.setRequestHeader('AJAX', true);
			},
			complete: function () {
			},
			error: function(jqXHR) {
				if(jqXHR.status === 403) {
					login();
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
	
	$.ajax(url, settings);
}

function schedule(url, query, selector) {
	const fn = function(data){
		$(selector).html(data);
		today();
	};
	
	ajaxFun(url, 'get', query, 'text', fn);	
}

function today() {
	let date = '${today}';
	$('.textDate').each(function (i) {
        let s = $(this).attr('data-date');
        if(s === date) {
        	$(this).parent().css('background', '#FFFFd9');
        }
    });
}

// 월별 - 월을 변경하는 경우
function changeMonth(year, month) {
	let url = '${pageContext.request.contextPath}/schedule/month';
	let query = 'year=' + year + '&month=' + month;
	
	schedule(url, query, '#nav-1');
}

// 상세 - 날짜를 변경하는 경우
function changeDate(date) {
	let url = '${pageContext.request.contextPath}/schedule/day';
	let query = 'date=' + date;
	
	schedule(url, query, '#nav-2');
}

// 년도 - 년도를 변경하는 경우
function changeYear(year) {
	let url = '${pageContext.request.contextPath}/schedule/year';
	let query = 'year=' + year;
	
	schedule(url, query, '#nav-3');
}

//월별 - 스케쥴 제목을 클릭한 경우
$(function(){
	$('body').on('click', '.scheduleSubject', function(){
		let date = $(this).attr('data-date');
		let num = $(this).attr('data-num');
		let url = '${pageContext.request.contextPath}/schedule/day';
		let query = 'date=' + date + '&num=' + num;
		
		const tabEl = document.querySelector('#myTab #tab-2');
		const tab = new bootstrap.Tab(tabEl);
		tab.show();
		
		schedule(url, query, '#nav-2');
	});
});

// 월별 - more(더보기) 를 클릭 한 경우
$(function(){
	$('body').on('click', '.scheduleMore', function(){
		let date = $(this).attr('data-date');
		let url = '${pageContext.request.contextPath}/schedule/day';
		let query = 'date=' + date;
		
		const tabEl = document.querySelector('#myTab #tab-2');
		const tab = new bootstrap.Tab(tabEl);
		tab.show();

		schedule(url, query, '#nav-2');
	});
});

// 월별 - 날짜를 클릭한 경우 : 일정 등록
$(function(){
	$('body').on('click', '#largeCalendar .textDate', function(){
		// 폼 reset
		$('form[name=scheduleForm]').each(function(){
			this.reset();
		});
		
		$('#form-repeat_cycle').hide();
		$('#form-allDay').prop('checked', true);
		$('#form-allDay').removeAttr('disabled');
		$('#form-stime').hide();
		$('#form-etime').hide();
		$('#form-eday').closest('tr').show();
		
		let date = $(this).attr('data-date');
		date = date.substr(0, 4) + '-' + date.substr(4, 2) + '-' + date.substr(6, 2);

		$('form[name=scheduleForm] input[name=sday]').val(date);
		$('form[name=scheduleForm] input[name=eday]').val(date);
		
		$('#myDialogModalLabel').html('스케쥴 등록');
		$('#btnScheduleSendOk').attr('data-mode', 'insert');
		$('#btnScheduleSendOk').html(' 등록 완료 ');
		$('#btnScheduleSendCancel').html(' 등록 취소 ');
		
		$('#myDialogModal').modal('show');
	});
});

// 상세일정 - 날짜 클릭
$(function(){
	$('body').on('click', '#smallCalendar .textDate', function(){
		let date = $(this).attr('data-date');
		let url = '${pageContext.request.contextPath}/schedule/day';
		let query = 'date=' + date;
		
		schedule(url, query, '#nav-2');
	});
});

// 상세일정 - 다른일정 제목 클릭
$(function(){
	$('body').on('click', '.daySubject', function(){
		let date = $(this).attr('data-date');
		let num = $(this).attr('data-num');
		let url = '${pageContext.request.contextPath}/schedule/day';
		let query = 'date=' + date + '&num=' + num;
		
		schedule(url, query, '#nav-2');
	});
});

// 상세일정 - 수정 버튼
$(function(){
	$('body').on('click', '#btnUpdate', function(){
		let date = $(this).attr('data-date');
		let num = $(this).attr('data-num');

		let subject = $('.date-schedule input[name=subject]').val();
		let color = $('.date-schedule input[name=color]').val();
		let allDay = $('.date-schedule input[name=allDay]').val();
		let sday = $('.date-schedule input[name=sday]').val();
		let stime = $('.date-schedule input[name=stime]').val();
		let eday = $('.date-schedule input[name=eday]').val();
		if(! eday ) eday = sday;
		let etime = $('.date-schedule input[name=etime]').val();
		let repeat = $('.date-schedule input[name=repeat]').val();
		let repeat_cycle = $('.date-schedule input[name=repeat_cycle]').val();
		let memo = $('.date-schedule input[name=memo]').val();
		
		$('#form-num').val(num);
		$('#form-subject').val(subject);
		$('#form-color').val(color);
		if(allDay === '1') {
			$('#form-allDay').prop('checked', true);
		} else {
			$('#form-allDay').prop('checked', false);
		}
		$('#form-sday').val(sday);
		$('#form-stime').val(stime);
		$('#form-eday').val(eday);
		$('#form-etime').val(etime);
		if(stime) {
			$('#form-stime').show();
			$('#form-etime').show()
		} else {
			$('#form-stime').hide();
			$('#form-etime').hide()
		}		
		$('#form-repeat').val(repeat);
		$('#form-repeat_cycle').val(repeat_cycle);
		if(repeat === '1') {
			$('#form-repeat_cycle').show();
			$('#form-eday').closest('tr').hide();
		} else {
			$('#form-repeat_cycle').val('');
			$('#form-repeat_cycle').hide();
			$('#form-eday').closest('tr').show();
		}		
		$('#form-memo').val(memo);
		
		$('#myDialogModalLabel').html('스케쥴 수정');
		$('#btnScheduleSendOk').attr('data-mode', 'update');
		$('#btnScheduleSendOk').attr('data-num', num);
		$('#btnScheduleSendOk').attr('data-date', date);
		
		$('#btnScheduleSendOk').html(' 수정 완료 ');
		$('#btnScheduleSendCancel').html(' 수정 취소 ');
		
		$('#myDialogModal').modal('show');
	});
});

// 상세일정 - 삭제 버튼
$(function(){
	$('body').on('click', '#btnDelete', function(){
		if(! confirm('일정을 삭제 하시겠습니까 ? ')) {
			return false;
		}
		
		let date = $(this).attr('data-date');
		let num = $(this).attr('data-num');
		let url = '${pageContext.request.contextPath}/schedule/delete';
		let query = 'num=' + num;
		
		const fn = function(data) {
			if(data.state === 'true') {
				let url = '${pageContext.request.contextPath}/schedule/day';
				let query = 'date=' + date;
				schedule(url, query, '#nav-2');
			}
		};
		
		ajaxFun(url, 'post', query, 'json', fn);
	});
});

// 년도 - 날짜 클릭
$(function(){
	$('body').on('click', '#yearCalendar .textDate', function(){
		let date = $(this).attr('data-date');
		let url = '${pageContext.request.contextPath}/schedule/day';
		let query = 'date=' + date;
		
		const tabEl = document.querySelector('#myTab #tab-2');
		const tab = new bootstrap.Tab(tabEl);
		tab.show();
		
		schedule(url, query, '#nav-2');
	});
});

// 등록/수정 대화상자 - 등록완료 버튼
$(function(){
	$('#btnScheduleSendOk').click(function(){
		if(! check()) {
			return false;
		}
		
		let mode = $('#btnScheduleSendOk').attr('data-mode');
		let query = $('form[name=scheduleForm]').serialize();
		let url = '${pageContext.request.contextPath}/schedule/' + mode;

		const fn = function(data){
			let state = data.state;
			if(state === 'true') {
				if(mode === 'insert') {
					let dd = $('#form-sday').val().split('-');
					let y = dd[0];
					let m = dd[1];
					if(m.substr(0,1) === '0') m = m.substr(1, 1);
				
				    let url = '${pageContext.request.contextPath}/schedule/month';
				    let query = 'year=' + y + '&month=' + m;
				    schedule(url, query, '#nav-1');
				} else if(mode === 'update') {
					let num = $('#btnScheduleSendOk').attr('data-num');
					let date = $('#btnScheduleSendOk').attr('data-date');
					
					let url = '${pageContext.request.contextPath}/schedule/day'
					let query = 'date='+date+'&num='+num;
						
					schedule(url, query, '#nav-2');
				}
			}
			
			$('#myDialogModal').modal('hide');
			
		};
		
		ajaxFun(url, 'post', query, 'json', fn);		
	});
});

// 등록/수정 대화상자 - 취소 버튼
$(function(){
	$('#btnScheduleSendCancel').click(function(){
		$('#myDialogModal').modal('hide');
	});
});

$(function(){
	$('body').on('click', '#form-allDay', function(){
		
		if(this.checked) {
			$('#form-stime').val('').hide();
			$('#form-etime').val('').hide();
		} else if(this.checked === false && $('#form-repeat').val() === '0'){
			$('#form-stime').val('00:00').show();
			$('#form-etime').val('00:00').show();
		}
	});

	$('body').on('change', '#form-sday', function(){
		$('#form-eday').val($('#form-sday').val());
	});

	$('body').on('change', '#form-repeat', function(){
		if($(this).val() === '0') {
			$('#form-repeat_cycle').val('').hide();
			
			$('#form-allDay').prop('checked', true);
			$('#form-allDay').removeAttr('disabled');
			
			$('#form-eday').val($('#form-sday').val());
			$('#form-eday').closest('tr').show();
		} else {
			$('#form-repeat_cycle').show();
			
			$('#form-allDay').prop('checked', true);
			$('#form-allDay').attr('disabled','disabled');

			$('#form-stime').val('').hide();
			$('#form-eday').val('');
			$('#form-etime').val('');
			$('#form-eday').closest('tr').hide();
		}
	});
});

// 등록내용 유효성 검사
function check() {
	if(! $('#form-subject').val()) {
		$('#form-subject').focus();
		return false;
	}

	if(! $('#form-sday').val()) {
		$('#form-sday').focus();
		return false;
	}

	if($('#form-eday').val()) {
		let s1 = $('#form-sday').val().replace('-', '');
		let s2 = $('#form-eday').val().replace('-', '');
		if(s1 > s2) {
			$('#form-sday').focus();
			return false;
		}
	}
	
	if($('#form-etime').val()) {
		let s1 = $('#form-stime').val().replace(':', '');
		let s2 = $('#form-etime').val().replace(':', '');
		if(s1 > s2) {
			$('#form-stime').focus();
			return false;
		}
	}	
	
	if($('#form-repeat').val() !== '0' && ! /^(\d){1,2}$/g.test($('#form-repeat_cycle').val())) {
		$('#form-repeat_cycle').focus();
		return false;
	}
	
	if($('#form-repeat').val() !== '0' && $('#form-repeat_cycle').val() < 1) {
		$('#form-repeat_cycle').focus();
		return false;
	}
	
	return true;
}

window.addEventListener('load', () => {
	const dateEls = document.querySelectorAll('form[name=scheduleForm] input[type=date]');
	const tiemEls = document.querySelectorAll('form[name=scheduleForm] input[type=time]');
	
	for(let el of dateEls) {
		el.addEventListener('keydown', e => e.preventDefault());
	}
	for(let el of tiemEls) {
		el.addEventListener('keydown', e => e.preventDefault());
	}
});
</script>

</body>
</html>
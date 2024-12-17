<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>TRAINEE - REGISTER</title>

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

<style type="text/css">
.body-container {
	max-width: 800px;
	margin: 10px auto;
	padding: 20px;
	background: #FCFCFC;
	box-shadow: 0 8px 15px rgba(0, 0, 0, 0.15), 0 3px 3px rgba(0, 0, 0, 0.1);
	border-radius: 10px;
	transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.body-container:hover {
	transform: scale(1.01);
	box-shadow: 0 12px 20px rgba(0, 0, 0, 0.25);
}

h3>img {
	width: 30px;
}

.custom-btn {
	width: 130px;
	background-color: #F6EFFA;
	color: #743394;
	font-size: 14px;
	border: 1px solid #8235B6;
	cursor: pointer;
	transition: all 0.3s ease;
	background-color: #F6EFFA;
	margin-top: 2px;
}

.custom-btn:hover {
	background-color: #D5C3E5;
	color: #000;
}
</style>

<script type="text/javascript">
	function memberOk() {
		const f = document.memberForm;

		str = f.userId.value
		if ( !/^[a-zA-Z][a-zA-Z0-9_]{4,8}$/i.test(str) ) {
			alert("아이디를 다시 입력하세요.");
			f.userId.focus();
			return;
		}

		let mode = "${mode}";
		if(mode === "member" && f.userIdValid.value === "false") {
			str = "아이디 중복 검사가 실행되지 않았습니다.";
			$("#userId").closest(".wrap-userId").find(".help-block").html(str);
			f.userId.focus();
			return;
		}
		
		str = f.pwd.value;
		if( !/^(?=.*[a-z])(?=.*[!@#$%^*+=-]|.*[0-9]).{5,8}$/i.test(str) ) { 
			alert("패스워드를 다시 입력하세요. ");
			f.pwd.focus();
			return;
		}
		
		if( str !== f.pwd2.value ) {
	        alert("패스워드가 일치하지 않습니다. ");
	        f.pwd.focus();
	        return;
		}
		
		str = f.name.value;
	    if( !/^[가-힣]{2,5}$/.test(str) ) {
	        alert("이름을 다시 입력하세요. ");
	        f.name.focus();
	        return;
	    }
	    
	    str = f.nickName.value;
	    if( !/^[a-zA-Z0-9가-힣]{2,8}$/.test(str) ) {
	        alert("닉네임을 다시 입력하세요. ");
	        f.nickName.focus();
	        return;
	    }
	    
	    str = f.birth.value;
	    if( !/^\d{4}-\d{2}-\d{2}$/.test(str) ) {
	        alert("생년월일를 입력하세요. ");
	        f.birth.focus();
	        return;
	    }
		
	    str = f.tel.value;
	    if( !/^\d{11}$/.test(str) ) {
	        alert("전화번호를 입력하세요. ");
	        f.tel1.focus();
	        return;
	    }
	    /* (!/^(\d){2,3}$/.test(str)) */
	    
	    str = f.email.value.trim();
	    if( !/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(str) ) {
	        alert("이메일을 입력하세요. ");
	        f.email.focus();
	        return;
	    }
	    
	    str = f.studentNum.value.trim();
	    if(!/^\d{8,11}$/.test(str)) {
	        alert("전체 학번을 입력하세요. ");
	        f.studentNum.focus();
	        return;
	    }
	    
		f.action = "${pageContext.request.contextPath}/member/${mode}";
		f.submit();
	}

	function changeEmail() {
		const f = document.memberForm;

		let str = f.selectEmail.value;
		if (str !== "direct") {
			f.email2.value = str;
			f.email2.readOnly = true;
			f.email1.focus();
		} else {
			f.email2.value = "";
			f.email2.readOnly = false;
			f.email1.focus();
		}
	}

	window.addEventListener('load', () => {
		const dateELS = document.querySelectorAll('form input[type=date]');
		dateELS.forEach( inputEL => inputEL.addEventListener('keydown', e => e.preventDefault()) );
	});

	function userIdCheck() {
		// 아이디 중복 검사
		let userId = $('#userId').val();
		
		if (!/^[a-z][a-z0-9_]{4,8}$/i.test(userId)) {
	        let str = '아이디는 5~8자 이내이며, 첫글자는 영문자로 시작해야 합니다.';
	        $('#userId').focus();
	        $('#userId').closest('.userId-box').find('.help-block').html(str);
	        return;
	    }
		
		let url = '${pageContext.request.contextPath}/member/userIdCheck';
		let query = 'userId=' + userId;
		
		$.ajax({
			type:'post',
			url:url,
			data:query,
			dataType:'json',
			success:function(data) {
				let passed = data.passed;
				
				if(passed === 'true') {
					let s = '<span style="color:blue; font-weight:bold;">' + userId + '</span> 아이디는 사용 가능합니다.';
	                $('#userId').closest('.userId-box').find('.help-block').html(s);
	                $('#userIdValid').val('true');
					//$('#userId').closest('.wrap-userId').find('.help-block').html(s);
					//$('#userIdValid').val('true');
				} else {
					let s = '<span style="color:red; font-weight:bold;">' + userId + '</span> 아이디는 사용할 수 없습니다.';
	                $('#userId').closest('.userId-box').find('.help-block').html(s);
	                $('#userIdValid').val('false');
	                $('#userId').val('');
	                $('#userId').focus();
				}
			}
		});
		
	}
</script>
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
						<i class="bi bi-person-heart" style="color: #8235B6;"></i> <span
							style="color: #8235B6;">${title}</span>
					</h3>
				</div>

				<div class="alert" role="alert" style="background-color: #F6EFFA;">
					<i class="bi bi-person-hearts" style="color: #8235B6;"></i>
					TRAINEE의 회원이 되시면 한층더 유익한 대학 생활을 즐길 수 있습니다.
				</div>

				<div class="body-main">

					<form name="memberForm" method="post">
						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" id="inputid" for="userId">아이디</label>
							<div class="col-sm-8 userId-box">
								<div class="row">
									<div class="col-5 pe-1">
										<input type="text" name="userId" id="userId"
											class="form-control" value="${dto.userId}"
											${mode=="update" ? "readonly ":""} placeholder="아이디">
									</div>
									<div class="col-3 ps-1">
										<input type="hidden" name="userIdValid" id="userIdValid"
											value="false">
										<c:if test="${mode=='member'}">
											<button type="button" class="btn custom-btn"
												onclick="userIdCheck();">아이디중복검사</button>
										</c:if>

									</div>
								</div>
								<c:if test="${mode=='member'}">
									<small class="form-control-plaintext help-block">아이디는
										5~8자이며, 첫글자는 영문자로 시작해야 합니다.</small>
								</c:if>
							</div>
						</div>



						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="pwd">패스워드</label>
							<div class="col-sm-7">
								<input type="password" name="pwd" id="pwd" class="form-control"
									autocomplete="off" placeholder="패스워드"> <small
									class="form-control-plaintext">패스워드는 5~8자이며 하나 이상의 숫자나
									특수문자가 포함되어야 합니다.</small>
							</div>
						</div>

						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="pwd2">패스워드 확인</label>
							<div class="col-sm-7">
								<input type="password" name="pwd2" id="pwd2"
									class="form-control" autocomplete="off" placeholder="패스워드 확인">
								<small class="form-control-plaintext">패스워드를 한번 더 입력해주세요.</small>
							</div>
						</div>

						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="name">이름</label>
							<div class="col-sm-7">
								<input type="text" name="name" id="name" class="form-control"
									value="${dto.name}" ${mode=="update" ? "readonly ":""}
									placeholder="이름"> <small
									class="form-control-plaintext help-block">이름은 한글만
									가능합니다.</small>
							</div>
						</div>

						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="nickName">닉네임</label>
							<div class="col-sm-7">
								<input type="text" name="nickName" id="nickName"
									class="form-control" value="${dto.nickName}"
									${mode=="update" ? "readonly" : ""} placeholder="닉네임">
								<small class="form-control-plaintext">닉네임은 2~8자이며 한글,
									숫자, 영문만 가능합니다.</small>
							</div>
						</div>

						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="birth">생년월일</label>
							<div class="col-sm-7">
								<input type="date" name="birth" id="birth" class="form-control"
									value="${dto.birth}" placeholder="생년월일"> <small
									class="form-control-plaintext">생년월일은 2000-01-01 형식으로
									입력해주세요.</small>
							</div>
						</div>

						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="selectEmail">이메일</label>
							<div class="col-sm-8 row">
								<div class="col-sm">
									<input type="email" name="email" class="form-control"
										maxlength="50" value="${dto.email}" placeholder="이메일을 입력하세요.">
									<small class="form-control-plaintext">@를 포함한 전체 이메일
										형식으로 입력해주세요.</small>
								</div>
							</div>
						</div>

						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="tel">전화번호</label>
							<div class="col-sm-8 row">
								<div class="col-sm">
									<input type="text" name="tel" id="tel" class="form-control"
										value="${dto.tel}" maxlength="11" placeholder="전화번호">
									<small class="form-control-plaintext">숫자만 입력해주세요.</small>
								</div>
							</div>
						</div>

						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="studentNum">학번</label>
							<div class="col-sm-7">
								<input type="text" name="studentNum" id="studentNum"
									class="form-control" value="${dto.studentNum}"
									${mode=="update" ? "readonly" : ""} placeholder="학번"> <small
									class="form-control-plaintext">전체 학번[숫자만]을 입력해주세요.</small>
							</div>
						</div>

						<c:if test="${mode == 'member' }">
							<div class="row mb-3">
								<label class="col-sm-2 col-form-label" for="agree">약관 동의</label>
								<div class="col-sm-8" style="padding-top: 5px;">
									<input type="checkbox" id="agree" name="agree"
										class="form-check-input" checked style="margin-left: 0;"
										onchange="form.sendButton.disabled = !checked"> <label
										class="form-check-label"> <a href="#"
										class="text-decoration-none">이용약관</a>에 동의합니다.
										<a href="#" class="text-decoration-none">서비스 이용약관 </a>동의(필수)
										<a href="#"
										class="text-decoration-none">개인정보 수집 및 이용 </a>동의 (필수)
										<a href="#"
										class="text-decoration-none">광고성 정보 수신 </a>동의(선택)
										<a href="#"
										class="text-decoration-none">만 14세 </a>이상입니다.
									</label>
								</div>
							</div>
						</c:if>

						<div class="row mb-3">
							<div class="text-center">
								<button type="button" name="sendButton" class="btn btn-danger"
									onclick="memberOk();">
									${mode=="member"?"회원가입":"정보수정"} <i class="bi bi-check2"></i>
								</button>
								<button type="button" class="btn btn-secondary"
									onclick="location.href='${pageContext.request.contextPath}/';">
									${mode=="member"?"가입취소":"수정취소"} <i class="bi bi-x"></i>
								</button>
							</div>
						</div>

						<div class="row">
							<p class="form-control-plaintext text-center">${message}</p>
						</div>
					</form>

				</div>
			</div>
		</div>

	</main>

	<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />

</body>
</html>
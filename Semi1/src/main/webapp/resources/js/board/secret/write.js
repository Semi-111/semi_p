// 이미지 미리보기 및 파일 이름 표시 함수
window.previewImage = function(input) {
    const preview = document.getElementById('preview');
    const fileNameElem = document.getElementById('fileName');

    if (input.files && input.files[0]) {
        const file = input.files[0];
        const reader = new FileReader();

        reader.onload = function(e) {
            // 이미지 미리보기
            if (preview) {
                preview.src = e.target.result;
                preview.style.display = 'flex';
            }

            // 파일 이름 표시
            if (fileNameElem) {
                fileNameElem.textContent = `선택된 파일: ${file.name}`;
            }
        };

        reader.readAsDataURL(file);
    } else {
        // 파일 선택 취소 시 미리보기 및 파일 이름 초기화
        if (preview) {
            preview.style.display = 'none';
            preview.src = '';
        }
        if (fileNameElem) {
            fileNameElem.textContent = '선택된 파일 없음';
        }
    }
};

// 게시글 수정 처리 함수
window.updateBoard = function() {
    const form = document.writeForm;
    let str;

    // 제목 검사
    str = form.title.value.trim();
    if (!str) {
        alert('제목을 입력하세요.');
        form.title.focus();
        return false;
    }

    // 내용 검사
    str = form.content.value.trim();
    if (!str) {
        alert('내용을 입력하세요.');
        form.content.focus();
        return false;
    }

    // mode가 'update'일 때만 action 재설정
    if (form.cmNum && form.page) { // cmNum과 page가 존재하면 수정 모드
        form.action = `${contextPath}/bbs/${encodeURIComponent(boardType)}/update?type=${encodeURIComponent(boardType)}&cmNum=${encodeURIComponent(cmNum)}&page=${encodeURIComponent(page)}`;
    }

    form.submit();
};

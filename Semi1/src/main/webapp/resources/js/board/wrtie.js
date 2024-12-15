document.getElementById('file').addEventListener('change', function(event) {
    const fileInput = event.target;
    const fileName = fileInput.files[0]?.name || '';
    const preview = document.querySelector('.preview-image');

    if (fileName) {
        const fileNameDisplay = document.createElement('p');
        fileNameDisplay.classList.add('file-name');
        fileNameDisplay.textContent = `선택된 파일: ${fileName}`;
        fileInput.parentNode.appendChild(fileNameDisplay);
    }

    const file = fileInput.files[0];
    if (file && file.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onload = function(e) {
            let img = preview.querySelector('img');
            if (!img) {
                img = document.createElement('img');
                preview.appendChild(img);
            }
            img.src = e.target.result;
            preview.style.display = 'flex';
        };
        reader.readAsDataURL(file);
    } else {
        preview.style.display = 'none';
        preview.innerHTML = '';
    }
});
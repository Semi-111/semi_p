document.addEventListener('DOMContentLoaded', function() {
    const fileInput = document.getElementById('file');
    const preview = document.querySelector('.preview-image');

    if (fileInput) {
        fileInput.addEventListener('change', function(event) {
            const file = event.target.files[0];
            const fileName = file?.name || '';

            const existingFileName = fileInput.parentNode.querySelector('.file-name');
            if (existingFileName) {
                existingFileName.remove();
            }

            if (fileName) {
                const fileNameDisplay = document.createElement('p');
                fileNameDisplay.classList.add('file-name');
                fileNameDisplay.textContent = `선택된 파일: ${fileName}`;
                fileInput.parentNode.appendChild(fileNameDisplay);
            }

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
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const hamburger = document.querySelector(".hamburger");
    const navLinks = document.querySelector(".nav-links");

    hamburger.addEventListener("click", () => {
        navLinks.classList.toggle("active");
    });

    window.addEventListener("resize", () => {
        if (window.innerWidth > 768) {
            navLinks.style.display = "flex";
            navLinks.classList.remove("active");
        } else {
            navLinks.style.display = "none";
        }
    });
});

function dialogLogin() {
    alert("로그인 모달 창이 열립니다."); // 로그인 모달 창 기능 구현 필요
}
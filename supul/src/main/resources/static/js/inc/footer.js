document.addEventListener("DOMContentLoaded", function () {
    var backButton = document.querySelector(".btn_top");

    backButton.addEventListener("click", function () {
        // 페이지 상단으로 스크롤
        window.scrollTo({
            top: 0,
            behavior: "smooth" // 부드러운 스크롤 효과를 위해 추가
        });
    });
});
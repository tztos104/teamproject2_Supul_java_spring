document.getElementById("searchForm").addEventListener("submit", function (event) {
		event.preventDefault(); // 폼 제출 방지

		var type = document.getElementById("type").value;
		var keyword = document.getElementById("keyword").value;

		// 선택한 검색 조건과 검색어를  전송
		var url = "/board/review/list?type=" + type + "&keyword=" + keyword;
		window.location.href = url;
	});
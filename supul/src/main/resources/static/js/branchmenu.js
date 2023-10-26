
	var selectedName;
	var selectedTitle;

	$(document).ready(function () {
		$('#branchName').change(function () {
			selectedName = $("#branchName option:selected").val();
			// URL 이동
			console.log(selectedName)
			window.location.href = '/reserve/tonggye5/' + selectedName + '/전체';
		});

		$('#title').change(function () {
			selectedTitle = $("#title option:selected").val();
			$("#branchName").val(selectedName).prop("selected", true);
			// URL 이동
			console.log(selectedTitle)
			window.location.href = selectedTitle;
		});
	});
	

	
function cancelPayment() {
		var impUidexe = $("#impUidexe").text();
		var imp_uid = $("#imp_uid").text();
		var merchant_uid = $("#merchantUid").text();

		// 결제 정보
		var paymentData = {

			impUidexe: impUidexe,             // 결제 고유번호
			imp_uid: imp_uid,             // 결제 고유번호
			merchant_uid: merchant_uid   // 주문번호
		};

		// AJAX 요청 보내기
		$.ajax({
			type: "POST",
			url: "/pay/cancelPay",
			data: JSON.stringify(paymentData), // 결제 정보를 JSON 문자열로 변환하여 전송
			contentType: "application/json",
			success: function (data) {
				// 성공적인 응답 처리
				console.log("결제 취소 성공:", data);
				alert("결제 취소 성공했습니다." + data)
				location.href = "/reserve/checkRv"
				// 여기에서 UI를 업데이트하거나 결과를 표시할 수 있습니다.
			},
			error: function (error) {
				// 요청 실패 시 오류 처리
				console.error("Error:", error.responseText);
				alert("결제 취소 실패" + error.responseText)
				// 여기에서 오류 메시지를 표시하거나 다른 조치를 취할 수 있습니다.
			}
		});
	}
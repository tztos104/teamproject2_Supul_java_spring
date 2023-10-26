function confirmcancle() {
	if (confirm('정말 결제를 취소하시겠습니까?\n(결제취소하면 예약이 자동으로 취소됩니다.)')) {
		cancelPayment()
		return true;
	} else {
		return false;
	}
}
     
       function validateClear() {
            var clearInput = document.getElementById("clear");
            var clearValue = clearInput.value;
            var clearPattern = /^[a-zA-Z가-힣]{1,10}$/;

            if (!clearPattern.test(clearValue)) {
                alert("숫자, 영어, 한글로 이루어진 최대 10자까지 입력하세요.");
                clearInput.focus();
                return false;
            }
            return true;
        }
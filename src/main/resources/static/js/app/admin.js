document.addEventListener("DOMContentLoaded", () => {
    fetch("/api/v1/auth/check-session", { method: "GET" })
        .then(response => {
            if (response.status === 401) {
                // 세션이 없으면 로그인 모달 표시
                //const modal = new bootstrap.Modal(document.getElementById('adminLoginModal'));
                const logoutBtn = document.getElementById('adminLogoutBtn');
                logoutBtn.style.display = 'none';
                const modal = new bootstrap.Modal(document.getElementById('adminLoginModal'), {
                    backdrop: 'static', // 배경 클릭 시 닫히지 않도록
                    keyboard: false     // ESC 키 눌러도 닫히지 않도록
                });
                modal.show();
            }
            // 세션이 있으면 아무것도 안 함 (페이지 그대로 표시)
        })
        .catch(error => {
            console.error("세션 확인 중 오류:", error);
        });
});

// 소켓 처리
window.addEventListener('load', () => {
    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        stompClient.subscribe('/topic/orderAlert', function (msg) {
            console.log("받음:", msg.body);
            try {
                // TTS 실행
                const utterance = new SpeechSynthesisUtterance(msg.body);
                console.log("TTS 시작:", msg.body);
                speechSynthesis.speak(utterance);


                const container = document.getElementById('toast-container');
                const isToastVisible = container && container.children.length > 0;

                // 토스트가 이미 표시 중이면 새 토스트는 무시
                if (isToastVisible) {
                    console.log("토스트 표시 중이므로 새 알림 무시");
                    return;
                }
                // 토스트 표시
                showToast(msg.body, true, null, 'admin')

            } catch (error) {
                console.error("에러 발생:", error);
            }

        });
    });
});

// 관리자 로그인 처리 함수
function adminLogin() {
    const id = document.getElementById('adminId').value.trim();
    const password = document.getElementById('adminPassword').value.trim();
    const errorMsg = document.getElementById('loginErrorMsg');

    // 입력 값 확인
    if (!id || !password) {
        errorMsg.textContent = "ID와 Password를 모두 입력해주세요.";
        errorMsg.style.display = 'block';
        return;
    } else {
        errorMsg.style.display = 'none';
    }


    const modalElement = document.getElementById('adminLoginModal');
    const modal = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
    // modal.hide();

    // 2. 서버로 로그인 정보 전송
    // **이 경로는 실제 백엔드 관리자 로그인 API 엔드포인트로 대체되어야 합니다.**
    fetch('/api/v1/auth/admin', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username: id,
            password: password
        })
    })
        .then(response => {
            if (response.ok) {
                // 3. 로그인 성공 시 처리 (HTTP 200 OK)
                //alert(`로그인 성공! 관리자 페이지로 이동합니다.`);
                // 관리자 페이지로 리다이렉션 (원하는 페이지: /admin/index)
                window.location.href = '/admin/index';
            } else {
                // 4. 로그인 실패 시 처리 (HTTP 401, 403 등)
                document.getElementById('loginErrorMsg').textContent = 'ID 또는 Password가 올바르지 않습니다.';
                document.getElementById('loginErrorMsg').style.display = 'block';

                modal.show(); // 실패 시 모달을 다시 열어 사용자에게 오류 메시지를 보여줌
            }
        })
        .catch(error => {
            console.error('로그인 중 오류 발생:', error);
            alert('네트워크 또는 서버 연결 오류가 발생했습니다.');
            modal.show();
        });
}

// 로그아웃 처리
document.getElementById('adminLogoutBtn').addEventListener('click', () => {
    fetch('/api/v1/auth/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                // 로그아웃 성공 → 메인 페이지 이동
                window.location.href = '/';
            } else {
                alert('로그아웃 중 오류가 발생했습니다.');
            }
        })
        .catch(error => {
            console.error('로그아웃 오류:', error);
            alert('네트워크 오류가 발생했습니다.');
        });
});




// 메뉴 링크 활성화 처리
document.querySelectorAll('.menu-link').forEach(link => {
    link.addEventListener('click', function(e) {
        if (this.getAttribute('href') === '#') {
            e.preventDefault();
            document.querySelectorAll('.menu-link').forEach(l => l.classList.remove('active'));
            this.classList.add('active');
        }
    });
});
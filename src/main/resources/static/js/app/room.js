document.addEventListener('DOMContentLoaded', () => {

    // room 파라미터 확인 함수
    function getRoomParam() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('room');
    }

    // 객실 선택 버튼 클릭 이벤트
    const roomSelectBtn = document.getElementById('roomSelectBtn');
    if (roomSelectBtn) {
        roomSelectBtn.addEventListener('click', (e) => {
            e.preventDefault();
            showRoomModal();
        });
    }

    // 객실 선택 확인 함수
    window.confirmRoomSelection = function() {
        const selectedRoom = document.getElementById('roomSelect').value;

        if (selectedRoom) {
            navigateToRoom(selectedRoom);
        } else {
            alert('객실을 선택해주세요.');
        }
    };

    // 선택한 객실로 이동하는 함수
    function navigateToRoom(selectedRoom) {
        const currentUrl = new URL(window.location.href);
        currentUrl.searchParams.set('room', selectedRoom);
        window.location.href = currentUrl.toString();
    }

    // 모달 표시 함수
    function showRoomModal() {
        const roomModalElement = document.getElementById('roomModal');
        const roomModal = new bootstrap.Modal(roomModalElement);

        roomModal.show();

        // 기존 이벤트 리스너 제거 (중복 방지)
        const confirmBtn = document.getElementById('roomConfirm');
        const newConfirmBtn = confirmBtn.cloneNode(true);
        confirmBtn.parentNode.replaceChild(newConfirmBtn, confirmBtn);

        // 새로운 이벤트 리스너 등록
        newConfirmBtn.addEventListener('click', (e) => {
            e.preventDefault();
            const selectedRoom = document.getElementById('roomSelect').value;

            if (selectedRoom) {
                roomModal.hide();

                // URL 파라미터 갱신
                const currentUrl = new URL(window.location.href);
                currentUrl.searchParams.set('room', selectedRoom);

                console.log(currentUrl);

                // 약간의 지연을 주어 모달이 완전히 닫힌 후 이동
                setTimeout(() => {
                    window.location.href = currentUrl.toString();
                }, 300);
            } else {
                alert('객실을 선택해주세요.');
            }
        });
    }

    // 초기 로드 시 처리
    const room = getRoomParam();
    if (!room) {
        showRoomModal();
    } else {

        // URL 파라미터에서 room 가져오기
        const room = getRoomParam();

        console.log('선택된 객실 번호:', room);
        document.getElementById('currentRoom').textContent = `${room} 호`;

        // room이 존재하면 '주문하기' 링크에 적용
        const foodLink = document.getElementById('foodLink');
        const articleLink = document.getElementById('articleLink');

        if (foodLink && articleLink) {
            foodLink.href = `/food-delivery?room=${room}`;
            articleLink.href = `/article-delivery?room=${room}`;
        }


    }

    // 언제든 다시 호출 가능
    window.openRoomModal = showRoomModal;
});
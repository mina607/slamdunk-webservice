
function showToast(message, withButton = false, roomNumber = null, option = null, onClose = null) {
    const container = document.getElementById('toast-container');
    const toast = createToastContainer(message);

    // 옵션별 UI 구성
    if (withButton && roomNumber && !option) {
        toast.appendChild(createOrderButton(roomNumber));
    }

    if (option === 'admin') {
        toast.appendChild(createAdminButton());
    }

    if (option === 'payment') {
        toast.style.width = '432px';
        toast.appendChild(createPaymentButtons(roomNumber, toast, onClose));
    }

    // 닫기(X) 버튼
    toast.appendChild(createCloseButton(toast, onClose));

    container.appendChild(toast);
    animateToast(toast, option, onClose);
}


// 1. 기본 토스트 컨테이너 생성
function createToastContainer(message) {
    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.innerHTML = `
        <span style="
            font-size: 18px;
            font-weight: 500;
            text-align: center;
            margin-top: 15px;
            margin-bottom: 12px;
        ">
            ${message.replace(/\n/g, "<br>")}
        </span>
    `;
    return toast;
}

// 주문 내역 버튼
function createOrderButton(roomNumber) {
    const btn = document.createElement('button');
    btn.className = 'toast-btn';
    btn.textContent = '주문 내역';
    btn.onclick = () => (window.location.href = `/order-status?room=${roomNumber}`);
    return btn;
}

// 주문 관리 버튼
function createAdminButton() {
    const btn = document.createElement('button');
    btn.className = 'toast-btn';
    btn.textContent = '주문 관리';
    btn.onclick = () => (window.location.href = `/order-status`);
    return btn;

}

// 3. 결제 관련 버튼 세트
function createPaymentButtons(roomNumber, toast, onClose) {
    const container = document.createElement('div');
    container.style.display = 'flex';
    container.style.justifyContent = 'space-between';
    container.style.gap = '10px';
    container.style.marginTop = '10px';

    // 즉시 결제 버튼
    const payNowBtn = createButton('즉시 결제', '#2c5530', () => requestPayment(roomNumber, 'IMMEDIATE'));

    // 나중에 결제 버튼
    const payLaterBtn = createButton('나중에 결제', '#899d8b', () => {
        closeToast(toast, onClose);
        placeOrder('food', 'DEFERRED');
    });

    // 취소 버튼
    const cancelBtn = createButton('취소', '#777', () => closeToast(toast, onClose));
    cancelBtn.style.color = '#fff';
    cancelBtn.style.marginTop = '10px';
    cancelBtn.style.width = '100%';

    // 버튼 조립
    container.appendChild(payNowBtn);
    container.appendChild(payLaterBtn);

    const wrapper = document.createElement('div');
    wrapper.appendChild(container);
    wrapper.appendChild(cancelBtn);

    return wrapper;
}

// 4. 일반 버튼 생성 유틸
function createButton(text, bgColor, onClick) {
    const btn = document.createElement('button');
    btn.className = 'toast-btn';
    btn.textContent = text;
    btn.style.backgroundColor = bgColor;
    btn.style.flex = '1';
    btn.onclick = onClick;
    return btn;
}

// 5. 닫기(X) 버튼
function createCloseButton(toast, onClose) {
    const btn = document.createElement('button');
    btn.className = 'toast-close';
    btn.innerHTML = '×';
    btn.onclick = () => closeToast(toast, onClose);
    return btn;
}

// 6. 애니메이션 및 자동 닫기 처리
function animateToast(toast, option, onClose) {
    setTimeout(() => toast.classList.add('show'), 50);

    // 자동 제거 (option이 null일 때만)
    if (option == null) {
        setTimeout(() => closeToast(toast, onClose), 3000);
    }
}

// 7. 토스트 닫기 처리 (콜백 포함)
function closeToast(toast, onClose) {
    toast.classList.remove('show');
    toast.style.opacity = '0';
    toast.style.transform = 'scale(0.9)';
    setTimeout(() => {
        toast.remove();
        if (onClose) onClose();
    }, 300);
}

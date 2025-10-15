let cart = [];
let totalPrice = 0;

// 카테고리 탭 클릭 처리
document.querySelectorAll('.category-tab').forEach(tab => {
    tab.addEventListener('click', () => {
        // 모든 탭에서 active 클래스 제거
        document.querySelectorAll('.category-tab').forEach(t => t.classList.remove('active'));
        // 클릭된 탭에 active 클래스 추가
        tab.classList.add('active');

        // 모든 메뉴 카테고리 숨기기
        document.querySelectorAll('.menu-category').forEach(category => {
            category.classList.add('d-none');
        });

        // 선택된 카테고리만 표시
        const selectedCategory = tab.getAttribute('data-category');
        document.getElementById(selectedCategory).classList.remove('d-none');
    });
});

// 장바구니에 아이템 추가
function addToCart(name, price, icon) {
    const existingItem = cart.find(item => item.name === name);

    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cart.push({
            name: name,
            price: price,
            icon: icon,
            quantity: 1
        });
    }

    updateCartDisplay();
    updateOrderButton();
}

// 장바구니 수량 변경
function updateQuantity(itemName, change) {
    const item = cart.find(item => item.name === itemName);
    if (item) {
        item.quantity += change;
        if (item.quantity <= 0) {
            cart = cart.filter(item => item.name !== itemName);
        }
    }
    updateCartDisplay();
    updateOrderButton();
}

// 장바구니 표시 업데이트
function updateCartDisplay() {
    const orderItemsContainer = document.getElementById('order-items');
    const totalPriceElement = document.getElementById('total-price');

    if (cart.length === 0) {
        orderItemsContainer.innerHTML = `
            <div class="text-center text-muted py-4">
                <i class="fas fa-shopping-cart mb-2" style="font-size: 2rem;"></i>
                <p>선택한 메뉴가 없습니다.</p>
            </div>
        `;
        totalPrice = 0;
    } else {
        let itemsHtml = '';
        totalPrice = 0;

        cart.forEach(item => {
            const itemTotal = item.price * item.quantity;
            totalPrice += itemTotal;

            const priceDisplay = item.price == 0
                ? ""
                : `₩${item.price.toLocaleString()}`;

            itemsHtml += `
                <div class="order-item">
                    <div class="item-info">
                         <h6><span class="item-icon me-2 ${item.icon}"></span>${item.name}</h6>
                         <small>${priceDisplay} × ${item.quantity}</small>
                    </div>
                    <div class="quantity-controls">
                        <button class="quantity-btn" onclick="updateQuantity('${item.name}', -1)">-</button>
                        <span class="mx-2">${item.quantity}</span>
                        <button class="quantity-btn" onclick="updateQuantity('${item.name}', 1)">+</button>
                    </div>
                </div>
            `;
        });

        orderItemsContainer.innerHTML = itemsHtml;
    }

    totalPriceElement.textContent = `₩${totalPrice.toLocaleString()}`;
}

// 주문하기 버튼 상태 업데이트
function updateOrderButton() {
    const placeOrderBtn = document.getElementById('place-order-btn');
    const roomNumber = document.getElementById('room-number').value.trim();
    // const customerName = document.getElementById('customer-name').value.trim();
    const phoneNumber = document.getElementById('phone-number').value.trim();

    if (cart.length > 0 && roomNumber  && phoneNumber) {
        placeOrderBtn.disabled = false;
    } else {
        placeOrderBtn.disabled = true;
    }
}

// 입력 필드 변경 시 주문 버튼 상태 업데이트
document.getElementById('room-number').addEventListener('input', updateOrderButton);
// document.getElementById('customer-name').addEventListener('input', updateOrderButton);
document.getElementById('phone-number').addEventListener('input', updateOrderButton);

// 주문하기 함수
function placeOrder(option = null) {
    const roomNumber = document.getElementById('room-number').value.trim();
    const phoneNumber = document.getElementById('phone-number').value.trim();
    const specialRequests = document.getElementById('special-requests').value.trim();


    // 주문 정보 생성
    const orderInfo = {
        items: cart,
        total: totalPrice,
        roomNumber: roomNumber,
        customer: {
            phone: phoneNumber,
            requests: specialRequests
        },
        orderTime: new Date().toLocaleString('ko-KR')
    };


    console.log('주문 정보:', orderInfo);

    // 서버로 주문 데이터 전송
    fetch('/food-delivery/order-multiple', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            items: cart,
            roomNumber: roomNumber,
            phoneNumber: phoneNumber,
            specialRequests: specialRequests
        })
    })
    .then(response => response.text())
    .then(data => {
        if (data === 'success') {

            // 성공 메시지 표시
            let message = `주문이 완료되었습니다!`;

            if (totalPrice > 0) {
                message += `\n\n₩${totalPrice.toLocaleString()}`;
            }

            message += `\n\n${roomNumber}호로 배달이 시작됩니다.`;

            showToast(message, true, roomNumber);


            // 장바구니 초기화
            cart = [];
            totalPrice = 0;

            // 룸서비스일 때,,,
            if(option == null) {
                updateCartDisplay();
            }

            // 입력 필드 초기화
            document.getElementById('room-number').value = '';
            document.getElementById('phone-number').value = '';
            document.getElementById('special-requests').value = '';

            updateOrderButton();
        } else {
            alert('주문 처리 중 오류가 발생했습니다.');
        }
    })
    .catch(error => {
        console.error('주문 오류:', error);
        alert('주문 처리 중 오류가 발생했습니다.');
    });
}


// 토스트 알림 호출 함수
function showToast(message, withButton = false, roomNumber = null, option = null) {
    const container = document.getElementById('toast-container');

    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.innerHTML = `<span>${message.replace(/\n/g, "<br>")}</span>`;

    // 주문 내역 버튼
    if (withButton && roomNumber && !option) {
        const btn = document.createElement('button');
        btn.textContent = '주문 내역';
        btn.onclick = () => {
            window.location.href = `/order-status?room=${roomNumber}`;
        };
        toast.appendChild(btn);
    }

    // 결제 관련 버튼
    if (option === 'payment') {
        const payNowBtn = document.createElement('button');
        payNowBtn.textContent = '즉시 결제';
        payNowBtn.onclick = () => {
            // IMP 결제 호출
            requestPayment(roomNumber);
        };

        const payLaterBtn = document.createElement('button');
        payLaterBtn.textContent = '나중에 결제';
        payLaterBtn.style.backgroundColor = '#899d8b';
        payLaterBtn.onclick = () => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 1200);
            // 주문 완료 처리
            placeOrder();
        };

        toast.appendChild(payNowBtn);
        toast.appendChild(payLaterBtn);
    }

    container.appendChild(toast);

    // 토스트 애니메이션
    setTimeout(() => toast.classList.add('show'), 50);

    // 5초 뒤 자동 제거 (결제 옵션이 없을 때만)
    if (!option) {
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 1200);
        }, 5000);
    }
}

// 포트원 결제 함수
function requestPayment(roomNumber) {

    if (!window.IMP) {
        alert("결제 모듈이 로드되지 않았습니다.");
        return;
    }

    const IMP = window.IMP;
    IMP.init("imp02008762"); // ← 여기에 본인의 가맹점 식별코드 입력

    IMP.request_pay({
        pg: "html5_inicis", // 테스트용
        pay_method: "card",
        merchant_uid: "order_" + new Date().getTime(),
        name: "룸서비스 결제",
        amount: 15000, // 실제 금액
        buyer_name: `Room ${roomNumber}`,
        buyer_email: "guest@example.com"
    }, function (rsp) {
        if (rsp.success) {
            alert("결제가 완료되었습니다!");
            placeOrder();

        } else {
            alert("결제가 취소되었습니다.");
        }
    });
}

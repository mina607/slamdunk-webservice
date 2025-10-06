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
                        <h6><i class="${item.icon} me-2"></i>${item.name}</h6>
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
function placeOrder() {
    const roomNumber = document.getElementById('room-number').value.trim();
    // const customerName = document.getElementById('customer-name').value.trim();
    const phoneNumber = document.getElementById('phone-number').value.trim();
    const specialRequests = document.getElementById('special-requests').value.trim();

    if (cart.length === 0) {
        alert('주문할 메뉴를 선택해주세요.');
        return;
    }

    if (!roomNumber || !phoneNumber) {
        alert('필수 정보를 모두 입력해주세요.');
        return;
    }

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

    $.ajax({
        type: 'POST',
        url: '/api/order',
        dataType: 'json',
        contentType:'application/json; charset=utf-8',
        data: JSON.stringify(orderInfo)
    }).done(function() {
        alert('등록되었습니다.');
        window.location.href = '/';
    }).fail(function (error) {
        alert(JSON.stringify(error));
    });

    console.log('주문 정보:', orderInfo);

    // 성공 메시지 표시
    //alert(`주문이 완료되었습니다!\n\n객실: ${roomNumber}\n총액: ₩${totalPrice.toLocaleString()}\n\n로봇이 곧 배달을 시작합니다.\n예상 배달 시간: 15-20분`);
    showToast(
        `주문이 완료되었습니다! \n\n  ₩${totalPrice.toLocaleString()} \n\n ${roomNumber} 호로 배달이 시작됩니다.`,
        true, // 버튼 추가 여부
        roomNumber // roomNumber 전달
    );


    // 장바구니 초기화
    cart = [];
    totalPrice = 0;
    updateCartDisplay();

    // 입력 필드 초기화
    document.getElementById('room-number').value = '';
    // document.getElementById('customer-name').value = '';
    document.getElementById('phone-number').value = '';
    document.getElementById('special-requests').value = '';

    updateOrderButton();

    // 실제 구현에서는 여기서 서버로 주문 데이터를 전송
    // fetch('/api/orders', { method: 'POST', body: JSON.stringify(orderInfo) })
}

function showToast(message, withButton = false, roomNumber = null) {
    const container = document.getElementById('toast-container');

    const toast = document.createElement('div');
    toast.className = 'toast';

    toast.innerHTML = `<span>${message.replace(/\n/g, "<br>")}</span>`;

    if (withButton && roomNumber) {
        const btn = document.createElement('button');
        btn.textContent = '주문 내역';
        btn.onclick = () => {
            // 주문 내역 페이지로 이동 (roomNumber 파라미터 전달 가능)
            window.location.href = `/order-status`;
        };
        toast.appendChild(btn);
    }

    container.appendChild(toast);

    // 약간의 지연 후 애니메이션 적용
    setTimeout(() => toast.classList.add('show'), 50);

    // 자동으로 사라지게 (5초 후)
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 1200);
    }, 5000);
}

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


// 시간대별 주문 추이 차트
document.addEventListener('DOMContentLoaded', function() {
    // 시간대별 주문 추이 차트
    const ctx = document.getElementById('orderChart');

    if (ctx) {
        // 서버에서 전달받은 데이터 사용
        const hourlyData = JSON.parse(ctx.dataset.hourlyOrders || '[]');

        const labels = hourlyData.map(item => item.hour + ':00');
        const foodData = hourlyData.map(item => item.foodCount);
        const productData = hourlyData.map(item => item.productCount);

        new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: '룸 서비스 주문',
                        data: foodData,
                        borderColor: '#2c5530',
                        backgroundColor: 'rgba(44, 85, 48, 0.1)',
                        tension: 0.4,
                        fill: true
                    },
                    {
                        label: '물품 주문',
                        data: productData,
                        borderColor: '#eab308',
                        backgroundColor: 'rgba(234, 179, 8, 0.1)',
                        tension: 0.4,
                        fill: true
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top'
                    },
                    tooltip: {
                        mode: 'index',
                        intersect: false
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 5
                        }
                    }
                }
            }
        });
    }
});

// 금액 포맷 함수 (숫자를 3자리마다 콤마 추가)
function formatNumber(num) {
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// 페이지 로드 시 실행
document.addEventListener("DOMContentLoaded", function() {
    const statValue = document.getElementById("today-revenue");

    // 요소가 없으면 종료
    if (!statValue) return;

    // input 태그인지 확인
    const isInput = statValue.tagName === "INPUT";

    // 현재 값 가져오기
    const currentValue = isInput ? statValue.value : statValue.textContent;

    // 값이 있으면 포맷 적용
    if (currentValue && currentValue.trim() !== "") {
        // 숫자만 추출
        const numericValue = currentValue.replace(/[^0-9]/g, "");

        if (isInput) {
            statValue.value = "₩" + formatNumber(numericValue);
        } else {
            statValue.textContent = "₩" + formatNumber(numericValue);
        }
    }

    // (선택사항) input 태그라면 사용자가 직접 입력할 때도 포맷 적용
    if (isInput) {
        statValue.addEventListener("input", function(e) {
            let value = e.target.value.replace(/[^0-9]/g, "");

            // 최대 11자리까지만 허용 (필요 없으면 삭제 가능)
            if (value.length > 11) {
                value = value.slice(0, 11);
            }

            e.target.value = formatNumber(value);
        });
    }
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
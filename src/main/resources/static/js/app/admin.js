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
const ctx = document.getElementById('orderChart').getContext('2d');
const orderChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: ['06:00', '08:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00', '24:00'],
        datasets: [{
            label: '주문 건수',
            data: [2, 5, 8, 15, 12, 10, 18, 22, 16, 8],
            borderColor: '#2c5530',
            backgroundColor: 'rgba(44, 85, 48, 0.1)',
            tension: 0.4,
            fill: true,
            pointRadius: 5,
            pointBackgroundColor: '#2c5530',
            pointBorderColor: '#fff',
            pointBorderWidth: 2,
            pointHoverRadius: 7
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                display: true,
                position: 'top',
                labels: {
                    font: {
                        size: 12,
                        family: "'Segoe UI', sans-serif"
                    },
                    color: '#2c5530',
                    padding: 15
                }
            },
            tooltip: {
                backgroundColor: 'rgba(44, 85, 48, 0.9)',
                padding: 12,
                titleFont: {
                    size: 14
                },
                bodyFont: {
                    size: 13
                },
                cornerRadius: 8
            }
        },
        scales: {
            y: {
                beginAtZero: true,
                ticks: {
                    stepSize: 5,
                    color: '#666',
                    font: {
                        size: 11
                    }
                },
                grid: {
                    color: 'rgba(0, 0, 0, 0.05)'
                }
            },
            x: {
                ticks: {
                    color: '#666',
                    font: {
                        size: 11
                    }
                },
                grid: {
                    display: false
                }
            }
        }
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

        // 포맷된 값 설정
        if (isInput) {
            statValue.value = formatNumber(numericValue);
        } else {
            statValue.textContent = formatNumber(numericValue);
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
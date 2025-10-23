


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
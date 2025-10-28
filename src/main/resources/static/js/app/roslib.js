
// 1. ROS 연결 객체는 이미 설정되어 있다고 가정
var ros = new ROSLIB.Ros({ url : 'ws://192.168.123.250:9090' });
// 2. Scout Status 토픽 객체 생성
var statusListener = new ROSLIB.Topic({
    ros : ros,
    name : '/scout_status',
    // 실제 메시지 타입이 'scout_msgs/msg/ScoutStatus'와 다르다면, ros2 topic info 명령으로 확인한 이름으로 변경하세요.
    messageType : 'scout_msgs/msg/ScoutStatus'
});
// 3. 토픽 구독 및 전압 표시
statusListener.subscribe(function(message) {
    var voltage = message.battery_voltage;

    // --- 셀 구성에 맞춰 전압 범위 설정 ---
    var minVoltage = 21.0;  // 완전 방전 (7S 기준)
    var maxVoltage = 29.4;  // 완전 충전

    // --- 전압을 0~100%로 변환 ---
    var percentage = ((voltage - minVoltage) / (maxVoltage - minVoltage)) * 100;

    // --- 범위 제한 (0~100%) ---
    percentage = Math.max(0, Math.min(100, percentage));

    // --- 표시 ---
    // document.getElementById('battery_display').innerText =
    //     '배터리: ' + percentage.toFixed(1) + '%';
    // --- 표시 ---
    const batteryDisplay = document.getElementById('battery_display');
    const percentageText = '배터리: ' + percentage.toFixed(1) + '%';

// --- 기존 아이콘 제거 ---
    batteryDisplay.innerHTML = '';

// --- 아이콘 요소 생성 ---
    const icon = document.createElement('i');


// --- 배터리 퍼센트 구간별 아이콘 및 색상 설정 ---
    if (percentage >= 80) {
        icon.className = 'fas fa-battery-full text-success';
    } else if (percentage >= 60) {
        icon.className = 'fas fa-battery-three-quarters text-success';
    } else if (percentage >= 30) {
        icon.className = 'fas fa-battery-half text-warning';
    } else if (percentage >= 10) {
        icon.className = 'fas fa-battery-quarter text-danger';
    } else {
        icon.className = 'fas fa-battery-empty text-danger';
    }

    // --- DOM에 아이콘과 텍스트 추가 ---
    batteryDisplay.appendChild(icon);
    batteryDisplay.appendChild(document.createTextNode(' ' + percentageText));

});
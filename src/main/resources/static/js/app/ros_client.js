
// ✅ 1. rosbridge 연결
var ros = new ROSLIB.Ros({
url: 'ws://192.168.123.250:9090'
});

ros.on('connection', function() {
console.log('✅ Connected to rosbridge.');
});
ros.on('error', function(e) {
console.error('❌ Error connecting to rosbridge:', e);
});
ros.on('close', function() {
console.warn('⚠️ Connection closed.');
});

// ✅ 2. speaker_command 구독 설정
var speakerCommandSub = new ROSLIB.Topic({
ros: ros,
name: '/speaker_command',
messageType: 'std_msgs/msg/String'   // ROS2 표준 String 메시지
});

// ✅ 3. 구독 시작 (arrival 감지 시 alert)
speakerCommandSub.subscribe(function(msg) {
console.log('🔊 /speaker_command received:', msg.data);

if (msg.data === 'arrival') {
alert('🟢 배달이 도착했습니다.');
}
});

// ✅ 4. (선택) Scout Status도 함께 구독하고 싶을 때
var statusListener = new ROSLIB.Topic({
ros: ros,
name: '/scout_status',
messageType: 'scout_msgs/msg/ScoutStatus'
});

statusListener.subscribe(function(status) {
console.log('🚗 Scout Status:', status);
});

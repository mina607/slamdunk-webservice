// 1. rosbridge 연결
var ros = new ROSLIB.Ros({
    url: 'ws://192.168.123.250:9090'
});

var mapTopic = new ROSLIB.Topic({
    ros: ros,
    name: '/map',
    messageType: 'nav_msgs/OccupancyGrid'
});

mapTopic.subscribe(function(msg) {
    console.log('📡 Raw map data:', msg);

    const width = msg.info.width;
    const height = msg.info.height;
    const data = msg.data;

    // 🖼 원본 맵 캔버스 생성
    const baseCanvas = document.createElement('canvas');
    baseCanvas.width = width;
    baseCanvas.height = height;
    const baseCtx = baseCanvas.getContext('2d');
    const imgData = baseCtx.createImageData(width, height);

    // 지도 픽셀 채우기
    for (let i = 0; i < data.length; i++) {
        let val = data[i];
        let color = 255;
        if (val === 0) color = 255;       // free
        else if (val === 100) color = 0;  // occupied
        else if (val === -1) color = 200; // unknown

        imgData.data[i * 4 + 0] = color;
        imgData.data[i * 4 + 1] = color;
        imgData.data[i * 4 + 2] = color;
        imgData.data[i * 4 + 3] = 255;
    }
    baseCtx.putImageData(imgData, 0, 0);

    // 변환용 캔버스 생성 (회전/확대용)
    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d');

    // 회전 시 폭/높이 바뀜 → 가로/세로 교체
    canvas.width = height;
    canvas.height = width;

    // 좌우 반전 + 90도 회전
    ctx.translate(canvas.width / 2, canvas.height / 2); // 중심 기준 변환
    ctx.rotate(90 * Math.PI / 180); // 오른쪽으로 90도 회전
    ctx.scale(-1, 1); // 좌우 반전
    ctx.drawImage(baseCanvas, -width / 2, -height / 2);

    // 크기 맞추기 (가로 1100px 기준으로 비율 확대)
    const scale = 1100 / canvas.width;
    const scaledCanvas = document.createElement('canvas');
    scaledCanvas.width = 1100;
    scaledCanvas.height = canvas.height * scale;
    const scaledCtx = scaledCanvas.getContext('2d');
    scaledCtx.imageSmoothingEnabled = false; // 픽셀 깨짐 방지
    scaledCtx.drawImage(canvas, 0, 0, scaledCanvas.width, scaledCanvas.height);

    // 기존 지도 제거 후 새 지도 표시
    const mapDiv = document.getElementById('map');
    mapDiv.innerHTML = '';
    mapDiv.appendChild(scaledCanvas);

    mapTopic.unsubscribe();
});

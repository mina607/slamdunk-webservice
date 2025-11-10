// 1. rosbridge 연결
var ros = new ROSLIB.Ros({
    url : 'ws://192.168.123.250:9090'
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
    const res = msg.info.resolution;
    const data = msg.data;

    const canvas = document.createElement('canvas');
    canvas.width = width;
    canvas.height = height;
    const ctx = canvas.getContext('2d');
    const imgData = ctx.createImageData(width, height);

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
    ctx.putImageData(imgData, 0, 0);
    document.getElementById('map').appendChild(canvas);

    mapTopic.unsubscribe();
});
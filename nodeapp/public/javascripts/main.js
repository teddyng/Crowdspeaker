/**
 * Created by joshua on 02/04/17.
 */
socket = io.connect("http://localhost:8080", {port: 8080, transports: ["websocket"]});

new_song = function (data) {
    console.log("New song: " + data.url)
    loadVideo(data.url);
};


$(document).ready(function () {
    socket.on("new song", new_song);

    $('#new_song').click(function () {
        socket.emit("song", {url: $('#song_url').val()})
    });

    $('#play_all').click(function () {
        socket.emit("play", {});
    });

});


function loadVideo(id) {
    var video = document.getElementById('video');
    var mp4 = document.getElementById('mp4');
    mp4.src = id;

    video.load();
    video.play();
}
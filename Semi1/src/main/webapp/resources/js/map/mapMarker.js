let hoverTimeout;

function createMarker(data) {
    let marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(data.lat, data.lon),
        map: map,
        icon: {
            url: pathContext+'/resources/images/map/marker.png',
            size: new naver.maps.Size(48, 48),
            origin: new naver.maps.Point(0, 0),
            anchor: new naver.maps.Point(25, 25)
        }
    });


    return marker;
}


// TODO : 이미지에 링크 달기
// TODO : 이미지에서 더보기 버튼 누르면 자료 더 가져오기


function initializeMarkers(markersData) {
    markersData.forEach((data, index) => {
        let marker = createMarker(data);

        let infoWindow = new naver.maps.InfoWindow({
            content: `<div style="width:150px;text-align:center;padding:10px;" id="storeName">
                                ${data.stName}
                                </div>`
        });

        markers.push(marker);
        infoWindows.push(infoWindow);
        addMarkerClickListener(marker, data.stId);

        naver.maps.Event.addListener(marker, 'mouseover', function () {
            hoverTimeout = setTimeout(function () {
                infoWindow.open(map, marker);
            }, 1000);
        });

        naver.maps.Event.addListener(marker, 'mouseout', function () {
            clearTimeout(hoverTimeout);
            infoWindow.close();
        });
    });
}


initializeMarkers(markersData);


function addMarkerClickListener(marker, stId) {
    naver.maps.Event.addListener(marker, 'click', function() {
        loadDetails(stId);
    });
}



function refreshMap() {
    const mapBounds = map.getBounds();
    const ne = mapBounds.getNE();
    const sw = mapBounds.getSW();

    const url = path + '/map?lat=' + sw.lat() + '&lon=' + sw.lng() + '&lat2=' + ne.lat() + '&lon2=' + ne.lng();
    window.location.href = url;

}
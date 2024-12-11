let map = new naver.maps.Map('map', {
	center: new naver.maps.LatLng(37.556601, 126.919494),
	zoom: 18
});

let markersData = [
	{ lat: 37.556601, lng: 126.919494, info: 'Marker 1 Information' },
	{ lat: 37.557601, lng: 126.918494, info: 'Marker 2 Information' },
	// Add more marker data as needed
];

let markers = [];
let infoWindows = [];

markersData.forEach((data, index) => {
	let marker = new naver.maps.Marker({
		position: new naver.maps.LatLng(data.lat, data.lng),
		map: map,
		icon: {
			url: '/resources/images/map/marker.png',
			size: new naver.maps.Size(48, 48),
			origin: new naver.maps.Point(0, 0),
			anchor: new naver.maps.Point(25, 25)
		}
	});

	let infoWindow = new naver.maps.InfoWindow({
		content: `<div style="width:150px;text-align:center;padding:10px;">${data.info}</div>`
	});

	markers.push(marker);
	infoWindows.push(infoWindow);

	naver.maps.Event.addListener(marker, 'click', function() {
		if (infoWindow.getMap()) {
			infoWindow.close();
		} else {
			infoWindow.open(map, marker);
		}
	});
});

function toggleSearch() {
	let searchDiv = document.getElementById('search');
	if (searchDiv.classList.contains('closed')) {
		searchDiv.classList.remove('closed');
	} else {
		searchDiv.classList.add('closed');
	}
}

function searchPlaces() {
	// Implement search functionality
}
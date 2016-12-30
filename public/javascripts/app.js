var s;

$(document).ready(function(){
	'use strict';
	init();
});

function init(){
    s = new sigma({
		renderer: {
			container: document.getElementById('container'),
		},
		settings: {
			nodesPowRatio: 1,
			edgeColor: 'target',
			labelThreshold: 20,
			hideEdgesOnMove: true,
		}
	});

    load();
}

function load(){
	$.get('/benchmark', function(data){
		$('#seedModal').modal().modal('hide');
		$('#benchmarkModal').modal().find('p').append(data).modal('show');
		$('.modal-backdrop.in').modal().modal('hide');
		loadJson();
	});
}

function loadJson(){
	$.getJSON('/api', function(graph){
		drawGraph(graph);
	});
}

function drawGraph(graph){
	for(var i = 0; i < graph.nodes.length; i++){
		s.graph.addNode(graph.nodes[i]);
	}
	
	for(var i = 0; i < graph.edges.length; i++){ 
		s.graph.addEdge(graph.edges[i]);
	}
	
	var fa = s.configForceAtlas2({
		worker: true,
		slowDown: 5,
		scaleRatio: 5000,
		gravity: 0.5,
		barnesHutOptimize: true
	});
	// Bind the events:
	fa.bind('start stop', function(e){
		console.log(e.type);
	});

	$('#showGraph').on('click', function (e){
		$('#benchmarkModal').modal('hide');
		s.refresh();
		s.startForceAtlas2();
	});
}

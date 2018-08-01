
// 차트 반복 호출하기
var callChart = function(list) {
	
	for(var i = 0; i < list.length; i++){
		// 데이터 가져오기.
		drawGoogle(list[i]);
	}
}


var drawGoogle = function(data){	
	
	// 정의 되어 있는 차트 목록에서 선택한 정보 가져오기.
	var target = data.target;
	var dataURL = data.dataURL;
	var OptionURL = data.OptionURL;
	var type = data.type;
	
	
	// 데이터 가져오기.
	$.post(dataURL).done(function(data) {
		// json 데이터 변수 담기
		var result = data.result;
		var columns = data.columns;
		var size = data.size;
		// 컬럼 정보 확인 (예외처리)
		if(columns.length == 0){
			alert("잘못된 호출 입니다.");
			return false;
		}
		// 구글 차트에 사용 할 데이터 만들기
		var chartData = new google.visualization.DataTable();
		// 컬럼 데이터 설정
		$.each(columns, function(index, value) {
			chartData.addColumn(value.type, value.value);
		});
		// 차트 데이터 설정
		$.each(result, function(index, value) {
			var row = [];
			for(var i = 0; i < columns.length; i++){
				row[i] = value[columns[i].column];
			}
			chartData.addRows([ row ]);
		});
		
		// 차트 옵션 가져오기
		$.get(OptionURL).done(function(options) {
			// 차트 생성
			var chart = null;
			if("BC" == type){ // 바차트 호출 시 동작
				chart = new google.visualization.BarChart(document.getElementById(target));				
			}else if("CC" == type){ // 컬럼차트 호출 시 동작
				chart = new google.visualization.ColumnChart(document.getElementById(target));
			}else if("CBC" == type){ // 컬럼차트 호출 시 동작
				chart = new google.visualization.ComboChart(document.getElementById(target));
			}else { // 예외 처리 부분
				alert("잘못된 호출 입니다.");
				return false;
			}
			// 신규 차트 그리기
			chart.draw(chartData, options);
		});		
	});
}
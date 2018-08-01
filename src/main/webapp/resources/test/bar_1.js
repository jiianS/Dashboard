var viewList = [
	{dataURL : "/eis/getBar", OptionURL : "/eis/resources/test/bar.json",type :"BC" },
	{dataURL : "/eis/getBar", OptionURL : "/eis/resources/test/column.json",type :"CC" },
	{dataURL : "/eis/getBar", OptionURL : "/eis/resources/test/combo.json",type :"CBC" },
];


function drawGoogle(key) {
	
	// 차트 삽입될 대상 만들기(추가하기)
	var target ="chart_" + $("#chart_body > div").length;
	var targetTag = '<div id="'+ target +'"></div>';
	$("#chart_body").append(targetTag);
	
	//viewList 안에 있는 것을 꺼내쓰겠다. 
	var dataURL= viewList[key].dataURL;
	var OptionURL= viewList[key].OptionURL;
	var type = viewList[key].type;
	
	//데이터 가져오기
	$.ajax({
		url : dataURL
	}).done(
			function(data) {
				//json에 데이터 넣기
				var result = data.result;
				var columns = data.columns;
				var size = data.size;

				//google chart 이용하여 데이터 넣기
				var chartData = new google.visualization.DataTable();

				//column에 데이터 넣기
				for (var i = 0; i < columns.length; i++) {
					chartData.addColumn(columns[i].type, columns[i].value);
				}
				
				// chart 데이터 설정 _> 동적으로 데이터를 받을 수 있게 변경 , column을 추가하려면 HomeController만 수정하면 된다
				$.each(result, function(index, value) {
					var row =[];
					
					for (var i=0; i<columns.length; i++) {
						row[i] =  value[columns[i].column];
					}
					
					chartData.addRows([ row ]);
					
				}); //.each

				//bar.json (옵션) 가져오기
				$.ajax({
					url : OptionURL
				}).done(function (options) {
					// 차트 생성하기
					
					var chart = null;
					
					//BC -> 바차트 / CC => 컬럼 차트 
					if("BC" == type){
						chart = new google.visualization.BarChart(document.getElementById(target));  
					}else if("CC"== type){
						chart = new google.visualization.ColumnChart(document.getElementById(target));
					}else if("CBC"== type){
						chart = new google.visualization.ComboChart(document.getElementById(target));
					}else {
						alert("잘못된 호출입니다.")
						return false;	// 아래 chart.draw() 실행이 되지 않음
					}
					
					chart.draw(chartData, options);
				}); //bar.json -> 설정하기 위함
			
			});//.done
}
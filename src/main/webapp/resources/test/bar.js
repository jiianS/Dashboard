function drawMultSeries(dataURL, OptionURL, target) {
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
					var chart = new google.visualization.BarChart(document.getElementById(target));  /* visualization 뒤에 부분(BarChart)을 바꿔주면 차트 모양이 바뀐다.   */
					chart.draw(chartData, options);
				}); //bar.json -> 설정하기 위함
			
			});//.done
}
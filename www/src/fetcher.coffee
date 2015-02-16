@fetcher = []

@fetcher.push (callback) ->
	baseURL = "http://ylnews.hnyanling.gov.cn/ylnews/"
	$.get "http://ylnews.hnyanling.gov.cn/ylnews/wnewsMore.action?subjectid=9907",null,(data,status) ->
		news = for i in $('#ec_table>tbody>tr', data)
			a = $('a',i)
			link: baseURL + a.attr('href')
			title: do a.text
			date: do ->
				temp = $('td',i)[2].textContent.split('-').map (x)-> parseInt x # I dont know why but here we must use '(x)->parseInt x' rather than just using 'parseInt'
				temp = new Date(temp[0],temp[1]-1,temp[2]+1)
				Date.parse temp
			source: 'rednet'
		callback null,news

do @app.onFetcherReady.trigger
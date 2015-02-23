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
				new Date(temp[0],temp[1]-1,temp[2]+1)
			source: 'rednet'
			baseURL: baseURL
		callback null,news

@fetcher.fetchAll = =>
	@fetcher[0] (err,news) =>
		@data.putNewsList news, (err,links) =>
			@data.getNewsList (err,doc) =>
				@view.updateNewsList doc

do @app.onFetcherReady.trigger
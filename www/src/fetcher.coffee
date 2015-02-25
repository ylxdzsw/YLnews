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

@fetcher.fetchAll = (callback) =>
	count = 0
	allNews = []
	for i in @fetcher
		count += 1
		i (err,news) =>
			count -= 1
			allNews.push news
			return if count isnt 0
			newsArray = allNews.reduce (a,b)->a.concat b
			@data.putNewsList newsArray, ->
			@app.onFetched.trigger newsArray
			callback err,newsArray if callback

@app.onTick.add @fetcher.fetchAll

do @app.onFetcherReady.trigger
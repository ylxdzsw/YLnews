#TODO: on app pause, database connections should be closedd, and reconnect when continue

@data = {}

@data.db = do ->
	name = 'YLnews'
	option = 
		mechanisms: ['indexeddb','websql']
	schema =
		stores: [
			name: 'list'
			keyPath: 'link'
		,
			name: 'detail'
			keyPath: 'link'
		]
	new ydn.db.Storage name, schema, option

db = @data.db

db.onReady (e) =>
	alert JSON.stringify db
	if not do e.getError
		do @app.onDatabaseReady.trigger
	else
		@navigator.notification.alert do e.getError
		#TODO

@data.putNewsList = (news, callback) =>
	#@util.assert db
	news = [news] if not news.map
	data = for i in news
		link: i.link
		date: i.date
		source: i.source
		title: i.title
		baseURL: i.baseURL
		isRead: no
	db.put 'list', data
		.fail (e) ->
			callback e
		.done (links) ->
			callback null, links

@data.getNewsList = (callback) =>
	db.values 'list'
		.fail (e) ->
			callback e
		.done (news) ->
			callback null, news


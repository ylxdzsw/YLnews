@data = {}

@data.putNewsList = (news, callback) =>
	@util.assert db
	news = [news] if not news.map
	data = for i in news
		link: i.link
		date: i.date
		source: i.source
		title: i.title
		baseURL: i.baseURL
		isRead: no
	db.add 'list', data
		.done (links) =>
			do @app.onListStoreUpdated.trigger
			callback null, links if callback
		.fail (e) =>
			do @app.onListStoreUpdated.trigger
			callback e if callback

@data.getNewsList = (callback) =>
	db.values 'list'
		.done (news) ->
			callback null, news
		.fail (e) ->
			callback e

@data.markAsRead = (link, callback) =>
	db.from('list','=',link).patch 'isRead', yes
		.done -> callback null if callback
		.fail (e) -> callback e if callback

db = do ->
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

db.onReady (e) =>
	throw e if e?
	do @app.onDatabaseReady.trigger

@data.db = db
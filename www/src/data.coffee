@data = {}

@data.putNewsList = (news, callback) =>
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

@data.getNewsList = (callback) ->
	db.from 'list'
		.order 'date'
		.reverse()
		.list 20
		.done (news) ->
			callback null, news
		.fail (e) ->
			callback e

@data.putNewsDetail = (news, callback) =>
	db.put 'detail', news
		.done (links) =>
			do @app.onDetailStoreUpdated.trigger
			callback null, links if callback
		.fail (e) =>
			do @app.onDetailStoreUpdated.trigger
			callback e if callback

@data.getNewsDetail = (link, callback) ->
	db.get 'detail', link
		.done (news) ->
			callback null, news
		.fail (e) ->
			callback e

@data.saveOption = (callback) =>
	doc = {}
	doc[k] = do v.get for k, v of @app.option
	db.put 'option', doc, 'theOnlyKey'
		.done ->
			callback null if callback
		.fail (e) ->
			callback e if callback

@data.loadOption = (callback) =>
	db.get 'option', 'theOnlyKey'
		.done (doc) =>
			@app.option[k].set v for k, v of doc when @app.option[k]?
			callback null if callback
		.fail (e) =>
			callback e if callback

@data.markAsRead = (link, callback) =>
	db.from('list','=',link).patch 'isRead', yes
		.done =>
			do @app.onListStoreUpdated.trigger
			callback null if callback
		.fail (e) =>
			do @app.onListStoreUpdated.trigger
			callback e if callback

@data.clearCache = (callback) =>
	db.clear 'detail'
		.done callback

@app.onExtracted.add (news) =>
	@data.putNewsDetail news, ->

db = do ->
	name = 'YLnews'
	option = 
		mechanisms: ['websql','indexeddb','memory']
	schema =
		stores: [
			name: 'list'
			keyPath: 'link'
			indexes: [
				name: 'date'
				keyPath: 'date'
				type: 'DATE'
			]
		,
			name: 'detail'
			keyPath: 'link'
		,
			name: 'option'
		]
	new ydn.db.Storage name, schema, option

db.onReady (e) =>
	throw e if e?
	do @app.onDatabaseConnected.trigger

@data.db = db

do @app.onDatabaseReady.trigger
@app = {}

option = (initValue) ->
	a = @util.property initValue
	a.onChange.add -> do @data.saveOption # this "-> do" is for avoiding consider 'args' as callback function
	a

@app.option =
	ticking: option on
	detailFontSize: option 'medium'

@app.state = {}

$ => document.addEventListener "deviceready", => @app.onDatabaseConnected.add => do @app.run

@app.run = =>
	$.mobile.loading 'show'
	do @view.updateNewsList
	do @data.loadOption
	do -> # don't "optimize" this function, unless you noticed the 'arguments.callee'
		do @app.onTick.trigger if @app.option.ticking
		setTimeOut arguments.callee, 30000 # 30s

@app.checkNetworkState = =>
	networkState = @navigator.connection.type
	if networkState in [Connection.NONE,Connection.UNKNOWN]
		@plugins.toast.showLongBottom "网络连接失败，显示缓存页面"

@app.onTick = do @util.event

@app.onFetcherLoaded = do @util.oneTimeEvent
@app.onExtractorLoaded = do @util.oneTimeEvent
@app.onViewLoaded = do @util.oneTimeEvent
@app.onDataLoaded = do @util.oneTimeEvent

@app.onDatabaseConnected = do @util.oneTimeEvent

@app.onFetched = do @util.event
@app.onExtracted = do @util.event
@app.onNewsListUpdated = do @util.event
@app.onNewsDetailUpdated = do @util.event
@app.onListStoreUpdated = do @util.event
@app.onDetailStoreUpdated = do @util.event


@app.onTick.add @app.checkNetworkState
@app.onNewsListUpdated.add (num) ->
	$.mobile.loading 'hide' if num
@app.onNewsDetailUpdated.add -> $.mobile.loading 'hide'

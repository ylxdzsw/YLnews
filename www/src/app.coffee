@app = {}

@app.option =
	ticking: on

$ => document.addEventListener "deviceready", => do @app.run

@app.run = =>
	do @view.updateNewsList
	do -> # don't "optimize" this function, unless you noticed the 'arguments.callee'
		do @app.onTick.trigger if @app.option.ticking
		setTimeOut arguments.callee, 30000 # 30s

@app.onTick = do @util.event

@app.onFetcherReady = do @util.oneTimeEvent
@app.onExtractorReady = do @util.oneTimeEvent
@app.onViewReady = do @util.oneTimeEvent
@app.onDatabaseReady = do @util.oneTimeEvent

@app.onFetched = do @util.event
@app.onExtracted = do @util.event
@app.onNewsListUpdated = do @util.event
@app.onListStoreUpdated = do @util.event
@app.onDetailStoreUpdated = do @util.event

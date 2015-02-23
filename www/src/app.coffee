@app = {}

$ => document.addEventListener "deviceready", => do @app.run

@app.run = =>
	do @fetcher.fetchAll

@app.onFetcherReady = do @util.oneTimeEvent
@app.onExtractorReady = do @util.oneTimeEvent
@app.onDatabaseReady = do @util.oneTimeEvent

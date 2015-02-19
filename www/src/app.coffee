@app = {}

$ => document.addEventListener "deviceready", => do @app.run

@app.run = =>
	do @fetcher.fetchAll

@app.onFetcherReady = do @util.event
@app.onExtractorReady = do @util.event
@app.onDatabaseReady = do @util.event

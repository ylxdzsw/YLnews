@app = {}

$ => document.addEventListener "deviceready", => do @app.run

@app.run = =>
	do @app.fetchAll

@app.onFetcherReady = do @util.event
@app.onExtractorReady = do @util.event

@app.fetchAll = =>
	@fetcher[0] (err,news) =>
		@view.updateNewsList news


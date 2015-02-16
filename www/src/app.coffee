@app = {}

$ -> document.addEventListener 'deviceready', -> do @app.run

@app.run = ->

@app.onFetcherReady = do @util.event
@app.onExtractorReady = do @util.event

@app.onFetcherReady.add ->
	@fetcher[0] (err,news) ->
		@view.updateNewsList news

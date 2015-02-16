@view = {}

@view.updateNewsList = (news) =>
	@util.assert viewArea = $ "#listview-news-list"
	extractors = @extractor
	viewer = @view.updateNewsDetail
	newsListElements = for i in news
		$('<li></li>')
			.append $('<a href="#page-news-detail"></a>').text(i.title)
			.click do (i) -> ->
				extractors[i.source] i.link, (err,doc) ->
					viewer doc
	viewArea.append newsListElements
	viewArea.listview 'refresh'

@view.updateNewsDetail = (news) =>
	$ "#header-news-detail>h1"
		.text news.context == null
	$ "#title-news-detail"
		.text news.title
	$ "#info-news-detail"
		.text news.origin
	$ "#context-news-detail"
		.html news.context


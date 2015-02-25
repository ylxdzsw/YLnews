@view = {}

@view.updateNewsList = =>
	@data.getNewsList (err,news) =>
		@util.assert viewArea = $ "#listview-news-list"
		newsListElements = for i in news
			$('<li></li>')
				.append $('<a href="#page-news-detail"></a>').text(i.title).attr('data-transition','slide')
				.attr 'data-icon', if i.isRead then 'check' else 'carat-r'
				.click do (i) -> =>
					@data.markAsRead i.link, ->
					@data.getNewsDetail i.link, (err,doc) =>
						if doc
							@view.updateNewsDetail doc
						else						
							@extractor[i.source] i.link, (err,doc) =>
								@view.updateNewsDetail doc
		viewArea.html ''
		viewArea.append newsListElements
		viewArea.listview 'refresh'

@view.updateNewsDetail = (news) =>
	$("#title-news-detail").text(news.title)
	$("#info-news-detail").text(news.origin)
	context = $("#context-news-detail")
	context.html('')
	temp = $()
	if news.context.img.length isnt 0
		for i in news.context.img
			pic = $("<p></p>").append($("<img></img>").attr('src',i))
			temp = temp.add pic
	for i in news.context.p
		p = $("<p></p>").text(i)
		temp = temp.add p
	context.append temp
	
@app.onListStoreUpdated.add =>
	do @view.updateNewsList

@view = {}

@view.updateNewsList = (news) =>
		$("#header-news-list>h1").text(@util)
		@util.assert viewArea = $ "#listview-news-list"
		$("#header-news-list>h1").text('fuck left')
		newsListElements = for i in news
			$('<li></li>').append $('<a></a>').text(i.title).attr('href',i.link)
		viewArea.append newsListElements
		viewArea.listview 'refresh'

		

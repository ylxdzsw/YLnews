$ =>
	$("#menu-clear-cache-news-list").click =>
		$.mobile.loading 'show'
		@data.clearCache ->
			$.mobile.loading 'hide'
			alert '缓存清理成功'
	
	$("#footer-news-detail").click -> scrollTo 0,0
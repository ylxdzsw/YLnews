$ =>
	$("#menu-clear-cache-news-list").click =>
		$.mobile.loading 'show'
		@data.clearCache ->
			$.mobile.loading 'hide'
			alert '缓存清理成功'
	
	$("#footer-news-detail").click -> scrollTo 0,0

	$("#menu-share-news-detail").click =>
		@plugins.socialsharing.share null, null, null, @app.state.detailURL

	$("#menu-copy-link-news-detail").click =>
		@cordova.plugins.clipboard.copy @app.state.detailURL, -> alert "成功将链接复制到剪贴板"

	$("#menu-setting-news-detail").click ->
		$("#pop-up-menu-news-detail").popup('close')

	$(".radio-font-size-pop-up-setting-news-detail").click ->
		app.option.detailFontSize.set this.value
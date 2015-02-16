@extractor = {}

@extractor.rednet = (url,callback) =>
	$.get url,null,(data,status) ->
		content = $('#new_center',data)
		news =
			title: $('.new_neirtxt',content)[0].textContent
			origin: $('.new_txta',content)[0].textContent
			date: $('.new_txta',content)[0].textContent
			context: $('table:nth-child(4)>tbody:nth-child(1)>tr:nth-child(1)>td:nth-child(1)>div:nth-child(1)',content)[0].innerHTML
		callback null,news

@app.onExtractorReady.trigger()
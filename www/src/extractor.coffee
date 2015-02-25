@extractor = {}

@extractor.rednet = (url,callback) =>
	$.get url,null,(data,status) =>
		content = $('#new_center',data)
		news =
			link: url
			title: $('.new_neirtxt',content)[0].textContent
			origin: $('.new_txta',content)[0].textContent
			date: $('.new_txta',content)[0].textContent
			context: do ->
				article = $('table:nth-child(4)>tbody:nth-child(1)>tr:nth-child(1)>td:nth-child(1)>div:nth-child(1)>p',content)
				context =
					img: []
					p: []
				for i in article
					img = $('img',i)
					if img.length is 1
						src = img.attr 'src' # 完整路径
						src = url.match(/http:\/\/.*?(?=\/)/)[0] + src if src[0] is '/' # 绝对路径
						context.img.push src
					else
						context.p.push i.textContent
				context
		@app.onExtracted.trigger news
		callback null,news

do @app.onExtractorReady.trigger
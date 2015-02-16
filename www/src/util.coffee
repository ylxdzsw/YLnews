@util = 
	event: ->
		_ = []
		add: (func) ->
			_.push func
		trigger: (arg) ->
			i arg for i in _
		destroy: ->
			_ = []
	assert: (exp, msg) ->
		if not exp
			a = "Assert failed"
			a = JSON.stringify msg,null,'  ' if msg?
			alert a
			throw a
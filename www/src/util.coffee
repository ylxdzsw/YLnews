@util = 
	event: ->
		_ = []
		add: (func) ->
			_.push func
		trigger: (arg) ->
			i arg for i in _
		destroy: ->
			_ = []
			
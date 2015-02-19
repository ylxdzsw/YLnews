@util = 
	event: ->
		_ = []
		add: (func) ->
			_.push func
		trigger: (arg) ->
			i arg for i in _
		destroy: ->
			_ = []
	oneTimeEvent: ->
		done = no
		_ = []
		add: (func) ->
			if not done
				_.push func
			else
				func _
		trigger: (arg) ->
			if not done
				i arg for i in _
				done = yes
				_ = arg
	assert: (exp, msg) ->
		if not exp
			a = "Assert failed"
			a = JSON.stringify msg,null,'  ' if msg?
			@navigator.notification.alert a
			throw a
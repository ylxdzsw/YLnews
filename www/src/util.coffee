@util = 
	event: ->
		_ = []
		add: (func) ->
			_.push func
		trigger: (arg) -> #Warning: any event handler should not change the arguments passed, because the order that event handlers triggered is not garanteed, so that modifing arguments will make influence to following event handlers
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
	property: (initValue) ->
		_ = initValue
		set: (value) ->
			temp = _
			_ = value
			@onChange.trigger 
				from: temp
				to: value
		get: -> _
		onChange: do @event
	assert: (exp, msg) ->
		if not exp
			a = "Assert failed"
			a = JSON.stringify msg,null,'  ' if msg?
			@navigator.notification.alert a
			throw a

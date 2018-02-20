import 'onsenui/css/onsenui.css'
import 'onsenui/css/onsen-css-components.css'

import Vue from 'vue'
import VueOnsen from 'vue-onsenui'
import YLnews from './YLnews'

Vue.config.productionTip = false
Vue.use(VueOnsen)

new Vue({
  el: '#app',
  template: '<YLnews/>',
  components: { YLnews }
})
